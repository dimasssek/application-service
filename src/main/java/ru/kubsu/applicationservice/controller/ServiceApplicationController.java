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
import ru.kubsu.applicationservice.usecase.service.ServiceCreateUseCase;
import ru.kubsu.applicationservice.usecase.service.ServiceDeleteUseCase;
import ru.kubsu.applicationservice.usecase.service.ServiceFetchByIdUseCase;
import ru.kubsu.applicationservice.usecase.service.ServiceFetchUseCase;
import ru.kubsu.applicationservice.usecase.service.ServiceUpdateUseCase;
import ru.kubsu.contracts.dto.common.PageData;
import ru.kubsu.contracts.dto.service.application.ServiceApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ServiceApplicationQueryParams;
import ru.kubsu.contracts.dto.service.application.ServiceApplicationTo;
import ru.kubsu.contracts.dto.service.application.ServiceApplicationUpdateRequest;

import java.util.UUID;

/**
 * REST API заявлений на банковские услуги.
 */
@RestController
@RequestMapping("/service")
@RequiredArgsConstructor
public class ServiceApplicationController {

    /** Сценарий создания заявления. */
    private final ServiceCreateUseCase serviceCreateUseCase;

    /** Сценарий получения заявления по идентификатору. */
    private final ServiceFetchByIdUseCase serviceFetchByIdUseCase;

    /** Сценарий поиска заявлений. */
    private final ServiceFetchUseCase serviceFetchUseCase;

    /** Сценарий обновления заявления. */
    private final ServiceUpdateUseCase serviceUpdateUseCase;

    /** Сценарий удаления заявления. */
    private final ServiceDeleteUseCase serviceDeleteUseCase;

    /**
     * Создаёт заявление на банковскую услугу.
     *
     * @param request запрос создания
     * @return созданное заявление
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceApplicationTo create(@Valid @RequestBody ServiceApplicationCreateRequest request) {
        return serviceCreateUseCase.create(request);
    }

    /**
     * Возвращает заявление на банковскую услугу по идентификатору.
     *
     * @param id идентификатор заявления
     * @return заявление
     */
    @GetMapping("/{id}")
    public ServiceApplicationTo fetchById(@PathVariable UUID id) {
        return serviceFetchByIdUseCase.fetchById(id);
    }

    /**
     * Выполняет поиск заявлений на банковские услуги.
     *
     * @param params параметры поиска
     * @return страница заявлений
     */
    @PostMapping("/search")
    public PageData<ServiceApplicationTo> search(@Valid @RequestBody ServiceApplicationQueryParams params) {
        return serviceFetchUseCase.fetch(params);
    }

    /**
     * Обновляет заявление на банковскую услугу.
     *
     * @param id      идентификатор заявления
     * @param request запрос обновления
     * @return обновлённое заявление
     */
    @PutMapping("/{id}")
    public ServiceApplicationTo update(@PathVariable UUID id,
                                       @Valid @RequestBody ServiceApplicationUpdateRequest request) {
        return serviceUpdateUseCase.update(id, request);
    }

    /**
     * Выполняет мягкое удаление заявления на банковскую услугу.
     *
     * @param id идентификатор заявления
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        serviceDeleteUseCase.delete(id);
    }
}
