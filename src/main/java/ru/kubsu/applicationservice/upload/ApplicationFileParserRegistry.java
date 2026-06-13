package ru.kubsu.applicationservice.upload;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Выбор парсера файла загрузки по расширению.
 */
@Component
public class ApplicationFileParserRegistry {

    /** Парсеры по расширению файла. */
    private final Map<String, ApplicationFileParser> parsersByExtension;

    /**
     * @param parsers зарегистрированные парсеры
     */
    public ApplicationFileParserRegistry(List<ApplicationFileParser> parsers) {
        this.parsersByExtension = parsers.stream()
                .collect(Collectors.toMap(ApplicationFileParser::supportedExtension, Function.identity()));
    }

    /**
     * Разбирает файл по имени и содержимому.
     *
     * @param fileName имя файла
     * @param content  содержимое файла
     * @return результат разбора
     */
    public ParsedApplicationUploadFile parse(String fileName, byte[] content) {
        String extension = resolveExtension(fileName);
        ApplicationFileParser parser = parsersByExtension.get(extension);
        if (parser == null) {
            throw new UnsupportedUploadFileFormatException(fileName);
        }
        return parser.parse(content);
    }

    /**
     * @param fileName имя файла
     * @return true, если формат поддерживается
     */
    public boolean isSupported(String fileName) {
        return parsersByExtension.containsKey(resolveExtension(fileName));
    }

    private String resolveExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
    }
}
