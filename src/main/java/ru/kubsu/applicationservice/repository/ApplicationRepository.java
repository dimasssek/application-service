package ru.kubsu.applicationservice.repository;

import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import ru.kubsu.applicationservice.entity.ApplicationEntity;
import ru.kubsu.applicationservice.entity.QApplicationEntity;

import java.util.UUID;

/**
 * Полиморфный репозиторий заявлений клиента.
 */
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, UUID>,
        QuerydslPredicateExecutor<ApplicationEntity>,
        QuerydslBinderCustomizer<QApplicationEntity> {

    /**
     * Настраивает регистронезависимый поиск по номеру заявления.
     *
     * @param bindings привязки QueryDSL
     * @param root     Q-тип заявления
     */
    @Override
    default void customize(QuerydslBindings bindings, QApplicationEntity root) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }
}
