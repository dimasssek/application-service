package ru.kubsu.applicationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.kubsu.contracts.dto.common.ErrorResponseTo;
import ru.kubsu.contracts.exception.service.application.ApplicationNotFoundException;
import ru.kubsu.contracts.exception.service.application.ApplicationServiceException;
import ru.kubsu.contracts.exception.service.application.ApplicationValidationException;
import ru.kubsu.contracts.exception.service.application.FileParseException;
import ru.kubsu.contracts.exception.service.application.UnsupportedFileFormatException;

/**
 * Глобальный обработчик исключений application-service.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключение «заявление не найдено».
     *
     * @param exception исключение
     * @return HTTP 404 с телом ошибки
     */
    @ExceptionHandler(ApplicationNotFoundException.class)
    public ResponseEntity<ErrorResponseTo> handleApplicationNotFound(ApplicationNotFoundException exception) {
        return buildResponse(HttpStatus.NOT_FOUND, exception);
    }

    /**
     * Обрабатывает ошибки бизнес-валидации заявления.
     *
     * @param exception исключение
     * @return HTTP 400 с телом ошибки
     */
    @ExceptionHandler(ApplicationValidationException.class)
    public ResponseEntity<ErrorResponseTo> handleApplicationValidation(ApplicationValidationException exception) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception);
    }

    /**
     * Обрабатывает исключение неподдерживаемого формата файла.
     *
     * @param exception исключение
     * @return HTTP 400 с телом ошибки
     */
    @ExceptionHandler(UnsupportedFileFormatException.class)
    public ResponseEntity<ErrorResponseTo> handleUnsupportedFileFormat(UnsupportedFileFormatException exception) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception);
    }

    /**
     * Обрабатывает исключение ошибки разбора файла.
     *
     * @param exception исключение
     * @return HTTP 400 с телом ошибки
     */
    @ExceptionHandler(FileParseException.class)
    public ResponseEntity<ErrorResponseTo> handleFileParse(FileParseException exception) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception);
    }

    /**
     * Обрабатывает ошибки валидации входных DTO.
     *
     * @param exception исключение валидации
     * @return HTTP 400 с телом ошибки
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseTo> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Ошибка валидации входных данных");
        ErrorResponseTo body = new ErrorResponseTo()
                .setCode(ApplicationValidationException.ERROR_CODE)
                .setMessage(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Формирует HTTP-ответ с кодом и телом ошибки.
     *
     * @param status    HTTP-статус
     * @param exception доменное исключение
     * @return ответ с ErrorResponseTo
     */
    private ResponseEntity<ErrorResponseTo> buildResponse(HttpStatus status, ApplicationServiceException exception) {
        ErrorResponseTo body = new ErrorResponseTo()
                .setCode(exception.getCode())
                .setMessage(exception.getMessage());
        return ResponseEntity.status(status).body(body);
    }
}
