package ru.kubsu.applicationservice.query;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.util.StringUtils;
import ru.kubsu.applicationservice.entity.ApplicationEntity;
import ru.kubsu.applicationservice.entity.QApplicationEntity;
import ru.kubsu.contracts.exception.service.application.ApplicationValidationException;

/**
 * Парсер ключа сортировки для QueryDSL-запросов по заявлениям.
 */
public final class ApplicationSortParser {

    private static final QApplicationEntity APPLICATION = QApplicationEntity.applicationEntity;

    private ApplicationSortParser() {
    }

    /**
     * Преобразует ключ сортировки в {@link OrderSpecifier}.
     *
     * @param sortKey ключ сортировки в формате "field,asc|desc"
     * @return спецификатор сортировки QueryDSL
     */
    public static OrderSpecifier<?> parse(String sortKey) {
        if (!StringUtils.hasText(sortKey)) {
            return APPLICATION.createdDate.desc();
        }

        String[] parts = sortKey.split(",");
        if (parts.length == 0 || !StringUtils.hasText(parts[0])) {
            throw new ApplicationValidationException("Некорректный ключ сортировки: " + sortKey);
        }

        String field = parts[0].trim();
        Order direction = Order.ASC;
        if (parts.length > 1 && "desc".equalsIgnoreCase(parts[1].trim())) {
            direction = Order.DESC;
        }

        return switch (field) {
            case ApplicationEntity.Fields.number -> new OrderSpecifier<>(direction, APPLICATION.number);
            case ApplicationEntity.Fields.createdDate -> new OrderSpecifier<>(direction, APPLICATION.createdDate);
            case ApplicationEntity.Fields.closedDate -> new OrderSpecifier<>(direction, APPLICATION.closedDate);
            case ApplicationEntity.Fields.statusBusiness -> new OrderSpecifier<>(direction, APPLICATION.statusBusiness);
            case ApplicationEntity.Fields.lastName -> new OrderSpecifier<>(direction, APPLICATION.lastName);
            case ApplicationEntity.Fields.firstName -> new OrderSpecifier<>(direction, APPLICATION.firstName);
            default -> throw new ApplicationValidationException("Недопустимое поле сортировки: " + field);
        };
    }
}
