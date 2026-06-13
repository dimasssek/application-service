package ru.kubsu.applicationservice.usecase.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.kubsu.applicationservice.entity.ApplicationEntity;
import ru.kubsu.applicationservice.entity.Client;
import ru.kubsu.applicationservice.service.ClientDataVerificationService;
import ru.kubsu.applicationservice.service.ClientLookupService;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusInternal;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.exception.service.application.ApplicationNotFoundException;

import java.util.Optional;
import java.util.UUID;

/**
 * Базовый сценарий post-processing одного заявления в general-check.
 *
 * @param <E> тип сущности заявления
 */
@Slf4j
@RequiredArgsConstructor
public abstract class BaseProcessUseCase<E extends ApplicationEntity> {

    /** Типизированный репозиторий заявления. */
    protected final JpaRepository<E, UUID> typedRepository;

    /** Сервис поиска клиента. */
    protected final ClientLookupService clientLookupService;

    /** Сервис сверки ИНН и СНИЛС с витриной клиента. */
    protected final ClientDataVerificationService clientDataVerificationService;

    /** Сервис status_inform. */
    protected final StatusInformService statusInformService;

    /**
     * Обрабатывает одно заявление: поиск клиента, бизнес-логика, сохранение и outbox.
     *
     * @param id идентификатор заявления
     * @return сохранённое заявление
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public E process(UUID id) {
        E entity = typedRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException(id));
        ApplicationType applicationType = getApplicationType();

        try {
            Optional<Client> client = clientLookupService.findMatchingClient(entity);
            if (client.isEmpty()) {
                statusInformService.applyStatus(entity, applicationType, ApplicationStatusInternal.CLIENT_NOT_FOUND);
            } else {
                Client foundClient = client.get();
                entity.setClientId(foundClient.getId());
                Optional<ApplicationStatusInternal> verificationStatus =
                        clientDataVerificationService.verify(entity, foundClient);
                if (verificationStatus.isPresent()) {
                    statusInformService.applyStatus(entity, applicationType, verificationStatus.get());
                } else {
                    processBusinessLogic(entity, foundClient);
                }
            }
        } catch (Exception exception) {
            log.warn("Ошибка обработки заявления id={}: {}", id, exception.getMessage(), exception);
            statusInformService.applyStatus(entity, applicationType, ApplicationStatusInternal.INTERNAL_ERROR);
        }

        E saved = typedRepository.save(entity);
        writeOutboxUpdated(saved);
        return saved;
    }

    /**
     * Тип-специфичная бизнес-логика основного процесса после успешной сверки клиентских данных.
     * По умолчанию — успешное завершение обработки; переопределяется при появлении правил по типу.
     *
     * @param entity заявление
     * @param client найденный клиент
     */
    protected void processBusinessLogic(E entity, Client client) {
        statusInformService.applyStatus(entity, getApplicationType(), ApplicationStatusInternal.APPLIED);
    }

    /**
     * @return тип заявления
     */
    protected abstract ApplicationType getApplicationType();

    /**
     * Записывает outbox-события обновления заявления.
     *
     * @param entity сохранённое заявление
     */
    protected abstract void writeOutboxUpdated(E entity);
}
