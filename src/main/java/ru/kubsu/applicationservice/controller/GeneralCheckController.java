package ru.kubsu.applicationservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kubsu.applicationservice.usecase.generalcheck.GeneralCheckUseCase;
import ru.kubsu.contracts.dto.service.application.GeneralCheckRequest;
import ru.kubsu.contracts.dto.service.application.GeneralCheckResponse;

/**
 * REST API пакетной обработки заявлений (general-check).
 */
@RestController
@RequestMapping("/general-check")
@RequiredArgsConstructor
public class GeneralCheckController {

    /** Сценарий general-check. */
    private final GeneralCheckUseCase generalCheckUseCase;

    /**
     * Запускает пакетную обработку заявлений указанного типа.
     *
     * @param request параметры запуска
     * @return результат обработки
     */
    @PostMapping
    public GeneralCheckResponse execute(@Valid @RequestBody GeneralCheckRequest request) {
        return generalCheckUseCase.execute(request);
    }
}
