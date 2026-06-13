package ru.kubsu.applicationservice.usecase.generalcheck;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kubsu.applicationservice.config.GeneralCheckProperties;
import ru.kubsu.applicationservice.entity.ApplicationEntity;
import ru.kubsu.applicationservice.entity.GeneralCheck;
import ru.kubsu.applicationservice.mapper.GeneralCheckMapper;
import ru.kubsu.applicationservice.repository.GeneralCheckRepository;
import ru.kubsu.applicationservice.service.GeneralCheckSelectionService;
import ru.kubsu.applicationservice.usecase.process.ProcessUseCaseRegistry;
import ru.kubsu.contracts.dto.service.application.GeneralCheckRequest;
import ru.kubsu.contracts.dto.service.application.GeneralCheckResponse;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusBusiness;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.exception.service.application.ApplicationValidationException;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Сценарий пакетной обработки заявлений (general-check).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GeneralCheckUseCase {

    /** Сервис выборки заявлений для обработки. */
    private final GeneralCheckSelectionService generalCheckSelectionService;

    /** Реестр post-processing по типам заявлений. */
    private final ProcessUseCaseRegistry processUseCaseRegistry;

    /** Репозиторий результатов general-check. */
    private final GeneralCheckRepository generalCheckRepository;

    /** Свойства general-check. */
    private final GeneralCheckProperties generalCheckProperties;

    /** Маппер результатов general-check. */
    private final GeneralCheckMapper generalCheckMapper;

    /**
     * Запускает пакетную обработку заявлений указанного типа.
     *
     * @param request параметры запуска
     * @return результат обработки
     */
    @Transactional
    public GeneralCheckResponse execute(GeneralCheckRequest request) {
        List<UUID> applicationIds = generalCheckSelectionService.findApplicationIds(
                request.getApplicationType(),
                request.getDateTo());

        List<ApplicationEntity> processedApplications = processInParallel(request.getApplicationType(), applicationIds);
        Map<ApplicationStatusBusiness, Integer> statusCounts = aggregateStatusCounts(processedApplications);
        GeneralCheck generalCheck = saveGeneralCheck(request, processedApplications.size(), statusCounts);

        return generalCheckMapper.toResponse(generalCheck);
    }

    private List<ApplicationEntity> processInParallel(
            ApplicationType applicationType,
            List<UUID> applicationIds) {
        ExecutorService executorService = Executors.newFixedThreadPool(generalCheckProperties.getPoolSize());
        List<ApplicationEntity> processedApplications = new ArrayList<>();

        try {
            List<Future<ApplicationEntity>> futures = applicationIds.stream()
                    .map(applicationId -> executorService.submit(
                            () -> processUseCaseRegistry.process(applicationType, applicationId)))
                    .toList();

            for (Future<ApplicationEntity> future : futures) {
                try {
                    processedApplications.add(future.get());
                } catch (ExecutionException exception) {
                    throw new ApplicationValidationException(
                            "Ошибка обработки заявления в general-check: " + exception.getCause().getMessage());
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    throw new ApplicationValidationException("General-check прерван");
                }
            }
        } finally {
            executorService.shutdown();
            awaitTermination(executorService);
        }

        return processedApplications;
    }

    private void awaitTermination(ExecutorService executorService) {
        try {
            if (!executorService.awaitTermination(generalCheckProperties.getAwaitTerminationSeconds(), TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                log.warn("Пул general-check не завершился в срок, выполнено принудительное завершение");
            }
        } catch (InterruptedException exception) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
            throw new ApplicationValidationException("General-check прерван при ожидании завершения потоков");
        }
    }

    private Map<ApplicationStatusBusiness, Integer> aggregateStatusCounts(List<ApplicationEntity> processedApplications) {
        Map<ApplicationStatusBusiness, Integer> statusCounts = processedApplications.stream()
                .collect(Collectors.groupingBy(
                        ApplicationEntity::getStatusBusiness,
                        () -> new EnumMap<>(ApplicationStatusBusiness.class),
                        Collectors.summingInt(application -> 1)));

        ApplicationStatusBusiness.GENERAL_CHECK_REPORT_STATUSES
                .forEach(status -> statusCounts.putIfAbsent(status, 0));

        return statusCounts;
    }

    private GeneralCheck saveGeneralCheck(GeneralCheckRequest request,
                                          int processedCount,
                                          Map<ApplicationStatusBusiness, Integer> statusCounts) {
        GeneralCheck generalCheck = new GeneralCheck()
                .setApplicationType(request.getApplicationType())
                .setDateTo(request.getDateTo())
                .setProcessedCount(processedCount)
                .setInQueueCount(statusCounts.get(ApplicationStatusBusiness.IN_QUEUE))
                .setBadCount(statusCounts.get(ApplicationStatusBusiness.BAD))
                .setAppliedCount(statusCounts.get(ApplicationStatusBusiness.APPLIED))
                .setRejectedCount(statusCounts.get(ApplicationStatusBusiness.REJECTED))
                .setCreatedAt(OffsetDateTime.now());

        return generalCheckRepository.save(generalCheck);
    }
}
