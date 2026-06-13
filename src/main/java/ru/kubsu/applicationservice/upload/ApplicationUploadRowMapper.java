package ru.kubsu.applicationservice.upload;

import org.springframework.stereotype.Component;
import ru.kubsu.contracts.dto.service.application.ApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.AuthorityApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ClaimApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.DocumentApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ProductApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ProfileChangeApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ServiceApplicationCreateRequest;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.application.AuthorityType;
import ru.kubsu.contracts.enums.service.application.Channel;
import ru.kubsu.contracts.enums.service.application.ClaimType;
import ru.kubsu.contracts.enums.service.application.ClientChangeType;
import ru.kubsu.contracts.enums.service.application.DocumentDeliveryType;
import ru.kubsu.contracts.enums.service.application.DocumentType;
import ru.kubsu.contracts.enums.service.application.ProductCategory;
import ru.kubsu.contracts.enums.service.application.ServiceActionType;
import ru.kubsu.contracts.enums.service.application.ServiceType;
import ru.kubsu.contracts.enums.service.client.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

/**
 * Маппер строки файла загрузки в запрос создания заявления.
 */
@Component
public class ApplicationUploadRowMapper {

    private static final DateTimeFormatter BIRTH_DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    /**
     * Преобразует колонки строки файла в запрос создания заявления.
     *
     * @param applicationType тип заявления
     * @param rowNumber       номер строки в файле (1-based)
     * @param columns         значения колонок
     * @return запрос создания
     */
    public ApplicationCreateRequest map(ApplicationType applicationType, int rowNumber, String[] columns) {
        String[] normalizedColumns = normalizeColumns(rowNumber, columns,
                ApplicationUploadFieldSchema.totalFieldCount(applicationType));

        ApplicationCreateRequest request = switch (applicationType) {
            case PRODUCT -> mapProduct(normalizedColumns, rowNumber);
            case SERVICE -> mapService(normalizedColumns, rowNumber);
            case PROFILE_CHANGE -> mapProfileChange(normalizedColumns, rowNumber);
            case DOCUMENT -> mapDocument(normalizedColumns, rowNumber);
            case CLAIM -> mapClaim(normalizedColumns, rowNumber);
            case AUTHORITY -> mapAuthority(normalizedColumns, rowNumber);
        };

        fillBaseFields(request, normalizedColumns, rowNumber);
        request.setChannel(Channel.FILE);
        return request;
    }

    private ProductApplicationCreateRequest mapProduct(String[] columns, int rowNumber) {
        ProductApplicationCreateRequest request = new ProductApplicationCreateRequest();
        request.setProductCategory(parseEnum(rowNumber, columns[9], "Категория продукта", ProductCategory::parseFileLabel));
        request.setProductCode(blankToNull(columns[10]));
        request.setProductName(requireNonBlank(rowNumber, columns[11], "Наименование продукта"));
        request.setAmount(parseDecimal(rowNumber, columns[12], "Сумма"));
        request.setTermMonths(parseInteger(rowNumber, columns[13], "Срок (мес.)"));
        request.setPurpose(blankToNull(columns[14]));
        request.setTariffCode(blankToNull(columns[15]));
        request.setExternalProductId(blankToNull(columns[16]));
        return request;
    }

    private ServiceApplicationCreateRequest mapService(String[] columns, int rowNumber) {
        ServiceApplicationCreateRequest request = new ServiceApplicationCreateRequest();
        request.setServiceType(parseEnum(rowNumber, columns[9], "Тип услуги", ServiceType::parseFileLabel));
        request.setServiceName(requireNonBlank(rowNumber, columns[10], "Наименование услуги"));
        request.setActionType(parseEnum(rowNumber, columns[11], "Действие", ServiceActionType::parseFileLabel));
        return request;
    }

    private ProfileChangeApplicationCreateRequest mapProfileChange(String[] columns, int rowNumber) {
        ProfileChangeApplicationCreateRequest request = new ProfileChangeApplicationCreateRequest();
        request.setChangeType(parseEnum(rowNumber, columns[9], "Тип изменения", ClientChangeType::parseFileLabel));
        request.setOldValue(blankToNull(columns[10]));
        request.setNewValue(blankToNull(columns[11]));
        return request;
    }

    private DocumentApplicationCreateRequest mapDocument(String[] columns, int rowNumber) {
        DocumentApplicationCreateRequest request = new DocumentApplicationCreateRequest();
        request.setDocumentType(parseEnum(rowNumber, columns[9], "Тип документа", DocumentType::parseFileLabel));
        request.setDeliveryType(parseOptionalEnum(rowNumber, columns[10], DocumentDeliveryType::parseFileLabel, "Способ получения"));
        request.setPurpose(blankToNull(columns[11]));
        return request;
    }

    private ClaimApplicationCreateRequest mapClaim(String[] columns, int rowNumber) {
        ClaimApplicationCreateRequest request = new ClaimApplicationCreateRequest();
        request.setClaimType(parseEnum(rowNumber, columns[9], "Тип претензии", ClaimType::parseFileLabel));
        request.setClaimSubject(requireNonBlank(rowNumber, columns[10], "Предмет претензии"));
        request.setDescription(blankToNull(columns[11]));
        request.setExternalRef(blankToNull(columns[12]));
        return request;
    }

    private AuthorityApplicationCreateRequest mapAuthority(String[] columns, int rowNumber) {
        AuthorityApplicationCreateRequest request = new AuthorityApplicationCreateRequest();
        request.setAuthorityType(parseEnum(rowNumber, columns[9], "Тип полномочия", AuthorityType::parseFileLabel));
        request.setPersonName(requireNonBlank(rowNumber, columns[10], "ФИО лица"));
        request.setDocumentNumber(blankToNull(columns[11]));
        request.setValidUntil(parseDate(rowNumber, columns[12], "Действует до"));
        return request;
    }

    private void fillBaseFields(ApplicationCreateRequest request, String[] columns, int rowNumber) {
        request.setLastName(requireNonBlank(rowNumber, columns[0], "Фамилия"));
        request.setFirstName(requireNonBlank(rowNumber, columns[1], "Имя"));
        request.setPatronymic(blankToNull(columns[2]));
        request.setBirthDate(requireDate(rowNumber, columns[3], "Дата рождения"));
        request.setGender(parseEnum(rowNumber, columns[4], "Пол", Gender::parseFileLabel));
        request.setIdentityDocumentSeries(requireNonBlank(rowNumber, columns[5], "Серия документа"));
        request.setIdentityDocumentNumber(blankToNull(columns[6]));
        request.setItn(blankToNull(columns[7]));
        request.setInsuranceNumber(blankToNull(columns[8]));
    }

    private String[] normalizeColumns(int rowNumber, String[] columns, int expectedSize) {
        if (columns.length > expectedSize) {
            throw new ApplicationUploadParseException(rowNumber,
                    "ожидается не более " + expectedSize + " полей, получено " + columns.length);
        }
        if (columns.length == expectedSize) {
            return columns;
        }
        return Arrays.copyOf(columns, expectedSize);
    }

    private String requireNonBlank(int rowNumber, String value, String fieldName) {
        String normalized = blankToNull(value);
        if (normalized == null) {
            throw new ApplicationUploadParseException(rowNumber, "не заполнено обязательное поле «" + fieldName + "»");
        }
        return normalized;
    }

    private String blankToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private LocalDate requireDate(int rowNumber, String value, String fieldName) {
        LocalDate date = parseDate(rowNumber, value, fieldName);
        if (date == null) {
            throw new ApplicationUploadParseException(rowNumber, "не заполнено обязательное поле «" + fieldName + "»");
        }
        return date;
    }

    private LocalDate parseDate(int rowNumber, String value, String fieldName) {
        String normalized = blankToNull(value);
        if (normalized == null) {
            return null;
        }
        try {
            return LocalDate.parse(normalized, BIRTH_DATE_FORMAT);
        } catch (DateTimeParseException exception) {
            throw new ApplicationUploadParseException(rowNumber,
                    "некорректная дата в поле «" + fieldName + "» (ожидается dd.MM.yyyy)", exception);
        }
    }

    private BigDecimal parseDecimal(int rowNumber, String value, String fieldName) {
        String normalized = blankToNull(value);
        if (normalized == null) {
            return null;
        }
        try {
            return new BigDecimal(normalized.replace(',', '.'));
        } catch (NumberFormatException exception) {
            throw new ApplicationUploadParseException(rowNumber,
                    "некорректное число в поле «" + fieldName + "»", exception);
        }
    }

    private Integer parseInteger(int rowNumber, String value, String fieldName) {
        String normalized = blankToNull(value);
        if (normalized == null) {
            return null;
        }
        try {
            return Integer.valueOf(normalized);
        } catch (NumberFormatException exception) {
            throw new ApplicationUploadParseException(rowNumber,
                    "некорректное целое число в поле «" + fieldName + "»", exception);
        }
    }

    private <T> T parseEnum(int rowNumber, String value, String fieldName, EnumParser<T> parser) {
        String normalized = blankToNull(value);
        if (normalized == null) {
            throw new ApplicationUploadParseException(rowNumber, "не заполнено обязательное поле «" + fieldName + "»");
        }
        try {
            return parser.parse(normalized);
        } catch (IllegalArgumentException exception) {
            throw new ApplicationUploadParseException(rowNumber, exception.getMessage(), exception);
        }
    }

    private <T> T parseOptionalEnum(int rowNumber, String value, EnumParser<T> parser, String fieldName) {
        String normalized = blankToNull(value);
        if (normalized == null) {
            return null;
        }
        try {
            return parser.parse(normalized);
        } catch (IllegalArgumentException exception) {
            throw new ApplicationUploadParseException(rowNumber, exception.getMessage(), exception);
        }
    }

    @FunctionalInterface
    private interface EnumParser<T> {
        T parse(String value);
    }
}
