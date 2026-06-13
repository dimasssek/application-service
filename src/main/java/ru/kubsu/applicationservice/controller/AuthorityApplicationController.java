package ru.kubsu.applicationservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.kubsu.applicationservice.usecase.authority.AuthorityCreateUseCase;
import ru.kubsu.applicationservice.usecase.authority.AuthorityDeleteUseCase;
import ru.kubsu.applicationservice.usecase.authority.AuthorityFetchByIdUseCase;
import ru.kubsu.applicationservice.usecase.authority.AuthorityFetchUseCase;
import ru.kubsu.applicationservice.usecase.authority.AuthorityUpdateUseCase;
import ru.kubsu.contracts.dto.common.PageData;
import ru.kubsu.contracts.dto.service.application.AuthorityApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.AuthorityApplicationQueryParams;
import ru.kubsu.contracts.dto.service.application.AuthorityApplicationTo;
import ru.kubsu.contracts.dto.service.application.AuthorityApplicationUpdateRequest;

import java.util.UUID;

/**
 * REST API заявлений на полномочия и доступы.
 */
@RestController
@RequestMapping("/authority")
@RequiredArgsConstructor
public class AuthorityApplicationController {

    /** Сценарий создания заявления. */
    private final AuthorityCreateUseCase authorityCreateUseCase;

    /** Сценарий получения заявления по идентификатору. */
    private final AuthorityFetchByIdUseCase authorityFetchByIdUseCase;

    /** Сценарий поиска заявлений. */
    private final AuthorityFetchUseCase authorityFetchUseCase;

    /** Сценарий обновления заявления. */
    private final AuthorityUpdateUseCase authorityUpdateUseCase;

    /** Сценарий удаления заявления. */
    private final AuthorityDeleteUseCase authorityDeleteUseCase;

    /**
     * Создаёт заявление на полномочия и доступы.
     *
     * @param request запрос создания
     * @return созданное заявление
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorityApplicationTo create(@Valid @RequestBody AuthorityApplicationCreateRequest request) {
        return authorityCreateUseCase.create(request);
    }

    /**
     * Возвращает заявление на полномочия и доступы по идентификатору.
     *
     * @param id идентификатор заявления
     * @return заявление
     */
    @GetMapping("/{id}")
    public AuthorityApplicationTo fetchById(@PathVariable UUID id) {
        return authorityFetchByIdUseCase.fetchById(id);
    }

    /**
     * Выполняет поиск заявлений на полномочия и доступы.
     *
     * @param params параметры поиска
     * @return страница заявлений
     */
    @PostMapping("/search")
    public PageData<AuthorityApplicationTo> search(@Valid @RequestBody AuthorityApplicationQueryParams params) {
        return authorityFetchUseCase.fetch(params);
    }

    /**
     * Обновляет заявление на полномочия и доступы.
     *
     * @param id      идентификатор заявления
     * @param request запрос обновления
     * @return обновлённое заявление
     */
    @PutMapping("/{id}")
    public AuthorityApplicationTo update(@PathVariable UUID id,
                                         @Valid @RequestBody AuthorityApplicationUpdateRequest request) {
        return authorityUpdateUseCase.update(id, request);
    }

    /**
     * Выполняет мягкое удаление заявления на полномочия и доступы.
     *
     * @param id идентификатор заявления
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        authorityDeleteUseCase.delete(id);
    }
}
