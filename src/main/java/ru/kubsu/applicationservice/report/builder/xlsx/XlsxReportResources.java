package ru.kubsu.applicationservice.report.builder.xlsx;

import lombok.Getter;
import lombok.Setter;
import ru.khinc.n2o.demo.report.builder.ReportResources;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Мето данные для построения отчета в формате PDF.
 *
 * @author vkhlybov
 */
@Getter
@Setter
public class XlsxReportResources implements ReportResources {

    /**
     * Наименование документа (отображается в мета данных документа).
     */
    private String name;

    /**
     * Файл шаблона.
     */
    private File templateFile;

    /**
     * Данные в формате json.
     */
    private String jsonData;

    /**
     * Набор функций-helpers (на JavaScript).
     */
    private List<String> helperFunctions = new ArrayList<>();
}
