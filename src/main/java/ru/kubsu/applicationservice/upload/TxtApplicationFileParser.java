package ru.kubsu.applicationservice.upload;

import org.springframework.stereotype.Component;
import ru.kubsu.contracts.dto.service.application.ApplicationCreateRequest;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Разбор txt-файла загрузки заявлений (разделитель — табуляция).
 */
@Component
public class TxtApplicationFileParser implements ApplicationFileParser {

    /** Маппер строки файла в запрос создания. */
    private final ApplicationUploadRowMapper applicationUploadRowMapper;

    /**
     * @param applicationUploadRowMapper маппер строки файла
     */
    public TxtApplicationFileParser(ApplicationUploadRowMapper applicationUploadRowMapper) {
        this.applicationUploadRowMapper = applicationUploadRowMapper;
    }

    @Override
    public String supportedExtension() {
        return "txt";
    }

    @Override
    public ParsedApplicationUploadFile parse(byte[] content) {
        String text = new String(content, StandardCharsets.UTF_8);
        String[] lines = text.split("\\R");
        if (lines.length == 0 || lines[0].isBlank()) {
            throw new ApplicationUploadParseException(1, "не указан тип заявления");
        }

        ApplicationType applicationType = parseApplicationType(lines[0], 1);
        List<ApplicationCreateRequest> requests = new ArrayList<>();

        for (int index = 1; index < lines.length; index++) {
            int rowNumber = index + 1;
            if (lines[index].isBlank()) {
                continue;
            }
            String[] columns = lines[index].split("\t", -1);
            requests.add(applicationUploadRowMapper.map(applicationType, rowNumber, columns));
        }

        if (requests.isEmpty()) {
            throw new ApplicationUploadParseException(2, "файл не содержит строк с данными");
        }

        return new ParsedApplicationUploadFile(applicationType, requests);
    }

    private ApplicationType parseApplicationType(String line, int rowNumber) {
        try {
            return ApplicationType.parseFileTypeLabel(line);
        } catch (IllegalArgumentException exception) {
            throw new ApplicationUploadParseException(rowNumber, exception.getMessage(), exception);
        }
    }
}
