package ru.kubsu.applicationservice.report.builder.docx;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.jsreport.java.JsReportException;
import net.jsreport.java.dto.*;
import net.jsreport.java.rest.RenderRequest;
import net.jsreport.java.service.JsReportService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;
import ru.khinc.n2o.demo.report.builder.ReportBuilder;
import ru.khinc.n2o.demo.report.builder.ReportResources;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Построитель отчетов в формате Docx (MS Word).
 *
 * @author vkhlybov
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DocxReportBuilder implements ReportBuilder {

    private final JsReportService jsReportService;

    @Override
    public Report build(ReportResources reportResources) throws IOException {
        if (!(reportResources instanceof DocxReportResources reportData)) {
            throw new IllegalArgumentException("Для построения отчета сформированы некорректные мета данные");
        }

        var template = new Template();
        var asset = new Asset();
        asset.setContent(new String(
                Base64.encodeBase64(
                        Files.readAllBytes(
                                Paths.get(
                                        reportData.getTemplateFile().getPath()
                                )
                        )
                )
        ));
        asset.setEncoding("base64");
        var docx = new Docx();
        docx.setTemplateAsset(asset);
        template.setDocx(docx);
        template.setEngine(Engine.HANDLEBARS);
        template.setRecipe(Recipe.DOCX);

        var renderRequest = new RenderRequest(template, reportData.getJsonData());

        try {
            return jsReportService.render(renderRequest);
        } catch (JsReportException e) {
            log.error("Произошла ошибка при формировании отчета");
            throw new RuntimeException(e);
        }
    }
}
