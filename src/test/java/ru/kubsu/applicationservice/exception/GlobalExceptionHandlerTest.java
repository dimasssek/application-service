package ru.kubsu.applicationservice.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.kubsu.contracts.dto.common.ErrorResponseTo;
import ru.kubsu.contracts.exception.service.application.ApplicationNotFoundException;
import ru.kubsu.contracts.exception.service.application.ApplicationValidationException;
import ru.kubsu.contracts.exception.service.application.FileParseException;
import ru.kubsu.contracts.exception.service.application.UnsupportedFileFormatException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Проверяет формирование HTTP-ответов глобальным обработчиком исключений.
 */
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    /**
     * Заявление не найдено должно возвращать HTTP 404.
     */
    @Test
    void shouldReturn404WhenApplicationNotFound() {
        ResponseEntity<ErrorResponseTo> response = handler.handleApplicationNotFound(
                new ApplicationNotFoundException(UUID.randomUUID()));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(ApplicationNotFoundException.ERROR_CODE);
    }

    /**
     * Ошибка валидации заявления должна возвращать HTTP 400.
     */
    @Test
    void shouldReturn400WhenApplicationValidationFailed() {
        ResponseEntity<ErrorResponseTo> response = handler.handleApplicationValidation(
                new ApplicationValidationException("Некорректные данные"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(ApplicationValidationException.ERROR_CODE);
    }

    /**
     * Неподдерживаемый формат файла должен возвращать HTTP 400.
     */
    @Test
    void shouldReturn400WhenUnsupportedFileFormat() {
        ResponseEntity<ErrorResponseTo> response = handler.handleUnsupportedFileFormat(
                new UnsupportedFileFormatException("report.pdf"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(UnsupportedFileFormatException.ERROR_CODE);
    }

    /**
     * Ошибка разбора файла должна возвращать HTTP 400.
     */
    @Test
    void shouldReturn400WhenFileParseFailed() {
        ResponseEntity<ErrorResponseTo> response = handler.handleFileParse(
                new FileParseException("Некорректная строка 5"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(FileParseException.ERROR_CODE);
    }
}
