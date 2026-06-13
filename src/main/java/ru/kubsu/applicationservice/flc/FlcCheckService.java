package ru.kubsu.applicationservice.flc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ApplicationEntity;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusInternal;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

import java.util.List;
import java.util.Optional;

/**
 * Сервис форматно-логического контроля заявлений.
 */
@Service
@RequiredArgsConstructor
public class FlcCheckService {

    /** Фабрика правил ФЛК. */
    private final FlcRuleFactory flcRuleFactory;

    /**
     * Выполняет ФЛК и возвращает статус ошибки, если правило нарушено.
     *
     * @param entity          заявление
     * @param applicationType тип заявления
     * @return статус ошибки ФЛК или пусто
     */
    public Optional<ApplicationStatusInternal> check(ApplicationEntity entity, ApplicationType applicationType) {
        List<FlcRule> rules = flcRuleFactory.createRules();
        for (FlcRule rule : rules) {
            Optional<ApplicationStatusInternal> violation = rule.check(entity, applicationType);
            if (violation.isPresent()) {
                return violation;
            }
        }
        return Optional.empty();
    }
}
