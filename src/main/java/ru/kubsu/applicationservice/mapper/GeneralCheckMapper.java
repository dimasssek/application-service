package ru.kubsu.applicationservice.mapper;

import org.springframework.stereotype.Component;
import ru.kubsu.applicationservice.entity.GeneralCheck;
import ru.kubsu.contracts.dto.service.application.GeneralCheckResponse;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusBusiness;

import java.util.EnumMap;
import java.util.Map;

/**
 * Маппер результатов general-check.
 */
@Component
public class GeneralCheckMapper {

    /**
     * Преобразует сущность general-check в ответ API.
     *
     * @param entity сущность general-check
     * @return ответ API
     */
    public GeneralCheckResponse toResponse(GeneralCheck entity) {
        Map<ApplicationStatusBusiness, Integer> statusCounts = new EnumMap<>(ApplicationStatusBusiness.class);
        statusCounts.put(ApplicationStatusBusiness.IN_QUEUE, entity.getInQueueCount());
        statusCounts.put(ApplicationStatusBusiness.BAD, entity.getBadCount());
        statusCounts.put(ApplicationStatusBusiness.APPLIED, entity.getAppliedCount());
        statusCounts.put(ApplicationStatusBusiness.REJECTED, entity.getRejectedCount());

        return new GeneralCheckResponse()
                .setId(entity.getId())
                .setApplicationType(entity.getApplicationType())
                .setDateTo(entity.getDateTo())
                .setProcessedCount(entity.getProcessedCount())
                .setStatusCounts(statusCounts);
    }
}
