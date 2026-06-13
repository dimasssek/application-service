package ru.kubsu.applicationservice.support;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Генератор xlsx-файлов для тестов и примеров загрузки.
 */
public final class UploadXlsxTestWriter {

    private UploadXlsxTestWriter() {
    }

    /**
     * Создаёт xlsx с первой строкой типа и строками данных.
     *
     * @param typeLine первая строка — тип заявления
     * @param dataRows строки данных
     * @return содержимое xlsx
     */
    public static byte[] writeWorkbook(String typeLine, List<String[]> dataRows) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("upload");
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue(typeLine);

            for (int rowIndex = 0; rowIndex < dataRows.size(); rowIndex++) {
                Row row = sheet.createRow(rowIndex + 1);
                String[] values = dataRows.get(rowIndex);
                for (int columnIndex = 0; columnIndex < values.length; columnIndex++) {
                    if (values[columnIndex] != null) {
                        row.createCell(columnIndex).setCellValue(values[columnIndex]);
                    }
                }
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}
