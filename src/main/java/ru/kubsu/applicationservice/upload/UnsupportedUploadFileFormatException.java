package ru.kubsu.applicationservice.upload;

/**
 * Неподдерживаемый формат файла загрузки.
 */
public class UnsupportedUploadFileFormatException extends RuntimeException {

    /**
     * @param fileName имя файла
     */
    public UnsupportedUploadFileFormatException(String fileName) {
        super("Неподдерживаемый формат файла: " + fileName + ". Допустимы: .txt, .xlsx");
    }
}
