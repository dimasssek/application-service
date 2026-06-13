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
import ru.kubsu.applicationservice.usecase.claim.ClaimCreateUseCase;
import ru.kubsu.applicationservice.usecase.claim.ClaimDeleteUseCase;
import ru.kubsu.applicationservice.usecase.claim.ClaimFetchByIdUseCase;
import ru.kubsu.applicationservice.usecase.claim.ClaimFetchUseCase;
import ru.kubsu.applicationservice.usecase.claim.ClaimUpdateUseCase;
import ru.kubsu.contracts.dto.common.PageData;
import ru.kubsu.contracts.dto.service.application.ClaimApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ClaimApplicationQueryParams;
import ru.kubsu.contracts.dto.service.application.ClaimApplicationTo;
import ru.kubsu.contracts.dto.service.application.ClaimApplicationUpdateRequest;

import java.util.UUID;

/**
 * REST API заявлений-претензий и обращений.
 */
@RestController
@RequestMapping("/claim")
@RequiredArgsConstructor
public class ClaimApplicationController {

    /** Сценарий создания заявления. */
    private final ClaimCreateUseCase claimCreateUseCase;

    /** Сценарий получения заявления по идентификатору. */
    private final ClaimFetchByIdUseCase claimFetchByIdUseCase;

    /** Сценарий поиска заявлений. */
    private final ClaimFetchUseCase claimFetchUseCase;

    /** Сценарий обновления заявления. */
    private final ClaimUpdateUseCase claimUpdateUseCase;

    /** Сценарий удаления заявления. */
    private final ClaimDeleteUseCase claimDeleteUseCase;

    /**
     * Создаёт заявление-претензию или обращение.
     *
     * @param request запрос создания
     * @return созданное заявление
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClaimApplicationTo create(@Valid @RequestBody ClaimApplicationCreateRequest request) {
        return claimCreateUseCase.create(request);
    }

    /**
     * Возвращает заявление-претензию или обращение по идентификатору.
     *
     * @param id идентификатор заявления
     * @return заявление
     */
    @GetMapping("/{id}")
    public ClaimApplicationTo fetchById(@PathVariable UUID id) {
        return claimFetchByIdUseCase.fetchById(id);
    }

    /**
     * Выполняет поиск заявлений-претензий и обращений.
     *
     * @param params параметры поиска
     * @return страница заявлений
     */
    @PostMapping("/search")
    public PageData<ClaimApplicationTo> search(@Valid @RequestBody ClaimApplicationQueryParams params) {
        return claimFetchUseCase.fetch(params);
    }

    /**
     * Обновляет заявление-претензию или обращение.
     *
     * @param id      идентификатор заявления
     * @param request запрос обновления
     * @return обновлённое заявление
     */
    @PutMapping("/{id}")
    public ClaimApplicationTo update(@PathVariable UUID id,
                                     @Valid @RequestBody ClaimApplicationUpdateRequest request) {
        return claimUpdateUseCase.update(id, request);
    }

    /**
     * Выполняет мягкое удаление заявления-претензии или обращения.
     *
     * @param id идентификатор заявления
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        claimDeleteUseCase.delete(id);
    }
}
