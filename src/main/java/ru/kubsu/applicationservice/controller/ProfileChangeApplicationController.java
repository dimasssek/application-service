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
import ru.kubsu.applicationservice.usecase.profilechange.ProfileChangeCreateUseCase;
import ru.kubsu.applicationservice.usecase.profilechange.ProfileChangeDeleteUseCase;
import ru.kubsu.applicationservice.usecase.profilechange.ProfileChangeFetchByIdUseCase;
import ru.kubsu.applicationservice.usecase.profilechange.ProfileChangeFetchUseCase;
import ru.kubsu.applicationservice.usecase.profilechange.ProfileChangeUpdateUseCase;
import ru.kubsu.contracts.dto.common.PageData;
import ru.kubsu.contracts.dto.service.application.ProfileChangeApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ProfileChangeApplicationQueryParams;
import ru.kubsu.contracts.dto.service.application.ProfileChangeApplicationTo;
import ru.kubsu.contracts.dto.service.application.ProfileChangeApplicationUpdateRequest;

import java.util.UUID;

/**
 * REST API заявлений на изменение данных клиента.
 */
@RestController
@RequestMapping("/profile-change")
@RequiredArgsConstructor
public class ProfileChangeApplicationController {

    /** Сценарий создания заявления. */
    private final ProfileChangeCreateUseCase profilechangeCreateUseCase;

    /** Сценарий получения заявления по идентификатору. */
    private final ProfileChangeFetchByIdUseCase profilechangeFetchByIdUseCase;

    /** Сценарий поиска заявлений. */
    private final ProfileChangeFetchUseCase profilechangeFetchUseCase;

    /** Сценарий обновления заявления. */
    private final ProfileChangeUpdateUseCase profilechangeUpdateUseCase;

    /** Сценарий удаления заявления. */
    private final ProfileChangeDeleteUseCase profilechangeDeleteUseCase;

    /**
     * Создаёт заявление на изменение данных клиента.
     *
     * @param request запрос создания
     * @return созданное заявление
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileChangeApplicationTo create(@Valid @RequestBody ProfileChangeApplicationCreateRequest request) {
        return profilechangeCreateUseCase.create(request);
    }

    /**
     * Возвращает заявление на изменение данных клиента по идентификатору.
     *
     * @param id идентификатор заявления
     * @return заявление
     */
    @GetMapping("/{id}")
    public ProfileChangeApplicationTo fetchById(@PathVariable UUID id) {
        return profilechangeFetchByIdUseCase.fetchById(id);
    }

    /**
     * Выполняет поиск заявлений на изменение данных клиента.
     *
     * @param params параметры поиска
     * @return страница заявлений
     */
    @PostMapping("/search")
    public PageData<ProfileChangeApplicationTo> search(@Valid @RequestBody ProfileChangeApplicationQueryParams params) {
        return profilechangeFetchUseCase.fetch(params);
    }

    /**
     * Обновляет заявление на изменение данных клиента.
     *
     * @param id      идентификатор заявления
     * @param request запрос обновления
     * @return обновлённое заявление
     */
    @PutMapping("/{id}")
    public ProfileChangeApplicationTo update(@PathVariable UUID id,
                                             @Valid @RequestBody ProfileChangeApplicationUpdateRequest request) {
        return profilechangeUpdateUseCase.update(id, request);
    }

    /**
     * Выполняет мягкое удаление заявления на изменение данных клиента.
     *
     * @param id идентификатор заявления
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        profilechangeDeleteUseCase.delete(id);
    }
}
