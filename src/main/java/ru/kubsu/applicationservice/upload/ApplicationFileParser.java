package ru.kubsu.applicationservice.upload;

/**
 * Разбор файла загрузки заявлений.
 */
public interface ApplicationFileParser {

    /**
     * @return поддерживаемое расширение файла без точки в нижнем регистре
     */
    String supportedExtension();

    /**
     * Разбирает содержимое файла.
     *
     * @param content содержимое файла
     * @return результат разбора
     */
    ParsedApplicationUploadFile parse(byte[] content);
}
