package ru.kubsu.applicationservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.kubsu.applicationservice.usecase.upload.ApplicationUploadUseCase;
import ru.kubsu.contracts.dto.service.application.ApplicationUploadResponse;

/**
 * REST API загрузки заявлений из файла.
 */
@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplicationUploadController {

    /** Сценарий загрузки заявлений из файла. */
    private final ApplicationUploadUseCase applicationUploadUseCase;

    /**
     * Загружает заявления из txt/xlsx-файла.
     *
     * @param file загруженный файл
     * @return результат загрузки
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApplicationUploadResponse upload(@RequestPart("file") MultipartFile file) {
        return applicationUploadUseCase.upload(file);
    }
}
