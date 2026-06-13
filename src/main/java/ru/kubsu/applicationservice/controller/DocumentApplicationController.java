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
import ru.kubsu.applicationservice.usecase.document.DocumentCreateUseCase;
import ru.kubsu.applicationservice.usecase.document.DocumentDeleteUseCase;
import ru.kubsu.applicationservice.usecase.document.DocumentFetchByIdUseCase;
import ru.kubsu.applicationservice.usecase.document.DocumentFetchUseCase;
import ru.kubsu.applicationservice.usecase.document.DocumentUpdateUseCase;
import ru.kubsu.contracts.dto.common.PageData;
import ru.kubsu.contracts.dto.service.application.DocumentApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.DocumentApplicationQueryParams;
import ru.kubsu.contracts.dto.service.application.DocumentApplicationTo;
import ru.kubsu.contracts.dto.service.application.DocumentApplicationUpdateRequest;

import java.util.UUID;

/**
 * REST API заявлений на получение документов.
 */
@RestController
@RequestMapping("/document")
@RequiredArgsConstructor
public class DocumentApplicationController {

    /** Сценарий создания заявления. */
    private final DocumentCreateUseCase documentCreateUseCase;

    /** Сценарий получения заявления по идентификатору. */
    private final DocumentFetchByIdUseCase documentFetchByIdUseCase;

    /** Сценарий поиска заявлений. */
    private final DocumentFetchUseCase documentFetchUseCase;

    /** Сценарий обновления заявления. */
    private final DocumentUpdateUseCase documentUpdateUseCase;

    /** Сценарий удаления заявления. */
    private final DocumentDeleteUseCase documentDeleteUseCase;

    /**
     * Создаёт заявление на получение документа.
     *
     * @param request запрос создания
     * @return созданное заявление
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DocumentApplicationTo create(@Valid @RequestBody DocumentApplicationCreateRequest request) {
        return documentCreateUseCase.create(request);
    }

    /**
     * Возвращает заявление на получение документа по идентификатору.
     *
     * @param id идентификатор заявления
     * @return заявление
     */
    @GetMapping("/{id}")
    public DocumentApplicationTo fetchById(@PathVariable UUID id) {
        return documentFetchByIdUseCase.fetchById(id);
    }

    /**
     * Выполняет поиск заявлений на получение документов.
     *
     * @param params параметры поиска
     * @return страница заявлений
     */
    @PostMapping("/search")
    public PageData<DocumentApplicationTo> search(@Valid @RequestBody DocumentApplicationQueryParams params) {
        return documentFetchUseCase.fetch(params);
    }

    /**
     * Обновляет заявление на получение документа.
     *
     * @param id      идентификатор заявления
     * @param request запрос обновления
     * @return обновлённое заявление
     */
    @PutMapping("/{id}")
    public DocumentApplicationTo update(@PathVariable UUID id,
                                        @Valid @RequestBody DocumentApplicationUpdateRequest request) {
        return documentUpdateUseCase.update(id, request);
    }

    /**
     * Выполняет мягкое удаление заявления на получение документа.
     *
     * @param id идентификатор заявления
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        documentDeleteUseCase.delete(id);
    }
}
