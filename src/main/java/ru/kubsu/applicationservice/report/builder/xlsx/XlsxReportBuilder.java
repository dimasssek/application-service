package ru.kubsu.applicationservice.report.builder.xlsx;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.jsreport.java.JsReportException;
import net.jsreport.java.dto.*;
import net.jsreport.java.rest.RenderRequest;
import net.jsreport.java.service.JsReportService;
import org.springframework.stereotype.Component;
import ru.khinc.n2o.demo.report.builder.ReportBuilder;
import ru.khinc.n2o.demo.report.builder.ReportResources;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Построитель отчетов в формате PDF.
 *
 * @author vkhlybov
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class XlsxReportBuilder implements ReportBuilder {

    private final JsReportService jsReportService;

    @Override
    public Report build(ReportResources reportResources) throws IOException {
        if (!(reportResources instanceof XlsxReportResources reportData)) {
            throw new IllegalArgumentException("Для построения отчета сформированы некорректные мета данные");
        }

        var template = new Template();
        var pdfMeta = new PdfMeta();
        pdfMeta.setTitle(reportData.getName());
        pdfMeta.setAuthor("101");
        template.setPdfMeta(pdfMeta);
        template.setContent(new String(Files.readAllBytes(Paths.get(reportData.getTemplateFile().getPath()))));
        template.setEngine(Engine.HANDLEBARS);
        template.setRecipe(Recipe.CHROME_PDF);
        template.setHelpers(String.join("; ", reportData.getHelperFunctions()));

        var renderRequest = new RenderRequest(template, reportData.getJsonData());

        try {
            return jsReportService.render(renderRequest);
        } catch (JsReportException e) {
            log.error("Произошла ошибка при формировании отчета");
            throw new RuntimeException(e);
        }
    }
}
