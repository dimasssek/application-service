package ru.kubsu.applicationservice.upload;

/**
 * Ошибка разбора файла загрузки с номером строки.
 */
public class ApplicationUploadParseException extends RuntimeException {

    /**
     * @param rowNumber номер строки в файле (1-based)
     * @param message   описание ошибки без префикса строки
     */
    public ApplicationUploadParseException(int rowNumber, String message) {
        super(formatMessage(rowNumber, message));
    }

    /**
     * @param rowNumber номер строки в файле (1-based)
     * @param message   описание ошибки без префикса строки
     * @param cause     первопричина
     */
    public ApplicationUploadParseException(int rowNumber, String message, Throwable cause) {
        super(formatMessage(rowNumber, message), cause);
    }

    private static String formatMessage(int rowNumber, String message) {
        return "Строка " + rowNumber + ": " + message;
    }
}
