package ru.kubsu.applicationservice.flc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Фабрика цепочки правил ФЛК (Factory Method).
 */
@Component
@RequiredArgsConstructor
public class FlcRuleFactory {

    /** Проверка обязательных полей. */
    private final FlcEmptyRequiredRule flcEmptyRequiredRule;

    /** Проверка даты подачи. */
    private final FlcInvalidSubmissionRule flcInvalidSubmissionRule;

    /**
     * Возвращает общую для всех типов заявлений цепочку правил ФЛК.
     *
     * @return список правил
     */
    public List<FlcRule> createRules() {
        return List.of(flcEmptyRequiredRule, flcInvalidSubmissionRule);
    }
}
