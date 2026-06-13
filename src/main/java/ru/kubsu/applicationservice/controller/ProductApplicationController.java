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
import ru.kubsu.applicationservice.usecase.product.ProductCreateUseCase;
import ru.kubsu.applicationservice.usecase.product.ProductDeleteUseCase;
import ru.kubsu.applicationservice.usecase.product.ProductFetchByIdUseCase;
import ru.kubsu.applicationservice.usecase.product.ProductFetchUseCase;
import ru.kubsu.applicationservice.usecase.product.ProductUpdateUseCase;
import ru.kubsu.contracts.dto.common.PageData;
import ru.kubsu.contracts.dto.service.application.ProductApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ProductApplicationQueryParams;
import ru.kubsu.contracts.dto.service.application.ProductApplicationTo;
import ru.kubsu.contracts.dto.service.application.ProductApplicationUpdateRequest;

import java.util.UUID;

/**
 * REST API продуктовых заявлений.
 */
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductApplicationController {

    /** Сценарий создания заявления. */
    private final ProductCreateUseCase productCreateUseCase;

    /** Сценарий получения заявления по идентификатору. */
    private final ProductFetchByIdUseCase productFetchByIdUseCase;

    /** Сценарий поиска заявлений. */
    private final ProductFetchUseCase productFetchUseCase;

    /** Сценарий обновления заявления. */
    private final ProductUpdateUseCase productUpdateUseCase;

    /** Сценарий удаления заявления. */
    private final ProductDeleteUseCase productDeleteUseCase;

    /**
     * Создаёт продуктовое заявление.
     *
     * @param request запрос создания
     * @return созданное заявление
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductApplicationTo create(@Valid @RequestBody ProductApplicationCreateRequest request) {
        return productCreateUseCase.create(request);
    }

    /**
     * Возвращает продуктовое заявление по идентификатору.
     *
     * @param id идентификатор заявления
     * @return заявление
     */
    @GetMapping("/{id}")
    public ProductApplicationTo fetchById(@PathVariable UUID id) {
        return productFetchByIdUseCase.fetchById(id);
    }

    /**
     * Выполняет поиск продуктовых заявлений.
     *
     * @param params параметры поиска
     * @return страница заявлений
     */
    @PostMapping("/search")
    public PageData<ProductApplicationTo> search(@Valid @RequestBody ProductApplicationQueryParams params) {
        return productFetchUseCase.fetch(params);
    }

    /**
     * Обновляет продуктовое заявление.
     *
     * @param id      идентификатор заявления
     * @param request запрос обновления
     * @return обновлённое заявление
     */
    @PutMapping("/{id}")
    public ProductApplicationTo update(@PathVariable UUID id,
                                       @Valid @RequestBody ProductApplicationUpdateRequest request) {
        return productUpdateUseCase.update(id, request);
    }

    /**
     * Выполняет мягкое удаление продуктового заявления.
     *
     * @param id идентификатор заявления
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        productDeleteUseCase.delete(id);
    }
}
