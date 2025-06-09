package ru.kubsu.applicationservice.report.builder.docx;

import lombok.Getter;
import lombok.Setter;
import ru.khinc.n2o.demo.report.builder.ReportResources;

import java.io.File;

/**
 * Мето данные для построения отчета в формате PDF.
 *
 * @author vkhlybov
 */
@Getter
@Setter
public class DocxReportResources implements ReportResources {

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
}
