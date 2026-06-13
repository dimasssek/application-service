package ru.kubsu.applicationservice.usecase.upload;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.kubsu.applicationservice.upload.ApplicationFileParserRegistry;
import ru.kubsu.applicationservice.upload.ApplicationUploadParseException;
import ru.kubsu.applicationservice.upload.ParsedApplicationUploadFile;
import ru.kubsu.applicationservice.upload.UnsupportedUploadFileFormatException;
import ru.kubsu.applicationservice.usecase.CreateUseCaseRegistry;
import ru.kubsu.contracts.dto.service.application.ApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ApplicationUploadResponse;

import java.io.IOException;

/**
 * Сценарий загрузки заявлений из файла.
 */
@Service
@RequiredArgsConstructor
public class ApplicationUploadUseCase {

    /** Реестр парсеров файлов загрузки. */
    private final ApplicationFileParserRegistry applicationFileParserRegistry;

    /** Реестр сценариев создания заявлений. */
    private final CreateUseCaseRegistry createUseCaseRegistry;

    /**
     * Загружает заявления из txt/xlsx-файла.
     *
     * @param file загруженный файл
     * @return результат загрузки
     */
    @Transactional
    public ApplicationUploadResponse upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return failure("Файл не передан или пуст");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isBlank()) {
            return failure("Имя файла не указано");
        }

        if (!applicationFileParserRegistry.isSupported(fileName)) {
            return failure(new UnsupportedUploadFileFormatException(fileName).getMessage());
        }

        try {
            ParsedApplicationUploadFile parsedFile = applicationFileParserRegistry.parse(fileName, file.getBytes());
            for (ApplicationCreateRequest request : parsedFile.requests()) {
                createUseCaseRegistry.create(parsedFile.applicationType(), request);
            }
            return success();
        } catch (ApplicationUploadParseException exception) {
            return failure(exception.getMessage());
        } catch (UnsupportedUploadFileFormatException exception) {
            return failure(exception.getMessage());
        } catch (IOException exception) {
            return failure("Не удалось прочитать файл: " + exception.getMessage());
        }
    }

    private ApplicationUploadResponse success() {
        return new ApplicationUploadResponse()
                .setUploaded(true)
                .setDetailedError(null);
    }

    private ApplicationUploadResponse failure(String detailedError) {
        return new ApplicationUploadResponse()
                .setUploaded(false)
                .setDetailedError(detailedError);
    }
}
