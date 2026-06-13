package ru.kubsu.applicationservice.upload;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;
import ru.kubsu.contracts.dto.service.application.ApplicationCreateRequest;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Разбор xlsx-файла загрузки заявлений.
 */
@Component
public class XlsxApplicationFileParser implements ApplicationFileParser {

    /** Маппер строки файла в запрос создания. */
    private final ApplicationUploadRowMapper applicationUploadRowMapper;

    /** Форматтер значений ячеек Excel. */
    private final DataFormatter dataFormatter = new DataFormatter();

    /**
     * @param applicationUploadRowMapper маппер строки файла
     */
    public XlsxApplicationFileParser(ApplicationUploadRowMapper applicationUploadRowMapper) {
        this.applicationUploadRowMapper = applicationUploadRowMapper;
    }

    @Override
    public String supportedExtension() {
        return "xlsx";
    }

    @Override
    public ParsedApplicationUploadFile parse(byte[] content) {
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(content))) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null || sheet.getPhysicalNumberOfRows() == 0) {
                throw new ApplicationUploadParseException(1, "файл не содержит данных");
            }

            Row typeRow = sheet.getRow(0);
            if (typeRow == null) {
                throw new ApplicationUploadParseException(1, "не указан тип заявления");
            }

            ApplicationType applicationType = parseApplicationType(readCell(typeRow, 0), 1);
            List<ApplicationCreateRequest> requests = new ArrayList<>();

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                int rowNumber = rowIndex + 1;
                Row row = sheet.getRow(rowIndex);
                if (row == null || isBlankRow(row, applicationType)) {
                    continue;
                }

                String[] columns = readRow(row, applicationType);
                requests.add(applicationUploadRowMapper.map(applicationType, rowNumber, columns));
            }

            if (requests.isEmpty()) {
                throw new ApplicationUploadParseException(2, "файл не содержит строк с данными");
            }

            return new ParsedApplicationUploadFile(applicationType, requests);
        } catch (IOException exception) {
            throw new ApplicationUploadParseException(1, "не удалось прочитать xlsx-файл", exception);
        }
    }

    private ApplicationType parseApplicationType(String line, int rowNumber) {
        try {
            return ApplicationType.parseFileTypeLabel(line);
        } catch (IllegalArgumentException exception) {
            throw new ApplicationUploadParseException(rowNumber, exception.getMessage(), exception);
        }
    }

    private boolean isBlankRow(Row row, ApplicationType applicationType) {
        int fieldCount = ApplicationUploadFieldSchema.totalFieldCount(applicationType);
        for (int columnIndex = 0; columnIndex < fieldCount; columnIndex++) {
            if (readCell(row, columnIndex) != null) {
                return false;
            }
        }
        return true;
    }

    private String[] readRow(Row row, ApplicationType applicationType) {
        int fieldCount = ApplicationUploadFieldSchema.totalFieldCount(applicationType);
        String[] columns = new String[fieldCount];
        for (int columnIndex = 0; columnIndex < fieldCount; columnIndex++) {
            columns[columnIndex] = readCell(row, columnIndex);
        }
        return columns;
    }

    private String readCell(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) {
            return null;
        }
        String value = dataFormatter.formatCellValue(cell);
        return value == null || value.isBlank() ? null : value.trim();
    }
}
