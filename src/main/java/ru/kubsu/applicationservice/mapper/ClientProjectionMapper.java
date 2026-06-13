package ru.kubsu.applicationservice.mapper;

import org.springframework.stereotype.Component;
import ru.kubsu.applicationservice.entity.Client;
import ru.kubsu.contracts.dto.service.client.ClientTo;

/**
 * Маппер проекции клиента из события client-service.
 */
@Component
public class ClientProjectionMapper {

    /**
     * Преобразует {@link ClientTo} в сущность витрины клиента.
     *
     * @param source транспортный объект клиента из события
     * @return сущность витрины
     */
    public Client toEntity(ClientTo source) {
        return new Client()
                .setId(source.getId())
                .setLastName(source.getLastName())
                .setFirstName(source.getFirstName())
                .setPatronymic(source.getPatronymic())
                .setBirthDate(source.getBirthDate())
                .setGender(source.getGender())
                .setIdentityDocumentSeries(source.getIdentityDocumentSeries())
                .setIdentityDocumentNumber(source.getIdentityDocumentNumber())
                .setIdentityDocumentIssueDate(source.getIdentityDocumentIssueDate())
                .setItn(source.getItn())
                .setInsuranceNumber(source.getInsuranceNumber())
                .setActualDate(source.getActualDate())
                .setAddressDefined(source.isAddressDefined())
                .setResidenceAddressName(source.getResidenceAddressName())
                .setStatus(source.getStatus());
    }
}
