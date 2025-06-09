package ru.kubsu.applicationservice.report.implementation.form002;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.khinc.n2o.demo.report.builder.ReportBuilder;
import ru.khinc.n2o.demo.report.builder.docx.DocxReportResources;
import ru.khinc.n2o.demo.report.common.building.ReportDataCollector;
import ru.khinc.n2o.demo.report.common.building.ReportGenerator;
import ru.khinc.n2o.demo.report.common.building.TemplateReader;
import ru.kubsu.applicationservice.report.implementation.form001.Form001ReportParams;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceReportGenerator implements ReportGenerator {

    private String templateFileName;

    private final TemplateReader templateReader;

    private final ReportDataCollector invoiceReportDataCollector;

    private final ReportBuilder docxReportBuilder;

    @Override
    public String getTemplateFileName() {
        return templateFileName;
    }

    @Override
    public TemplateReader getTemplateReader() {
        return templateReader;
    }

    @Override
    public ReportDataCollector getDataCollector() {
        return invoiceReportDataCollector;
    }

    @Override
    public ReportBuilder getReportBuilder() {
        return docxReportBuilder;
    }

    @Override
    public InputStream generate(Object param) throws IOException {
        if (!(param instanceof Form001ReportParams params)) {
            throw new IllegalArgumentException("Параметры для построения отчета некорректны");
        }

        if (params.getFormat().equals(InvoiceFormat.FOR_CUSTOMER)) {
            templateFileName = "templates/invoice/invoice_template.docx";
        } else {
            templateFileName = "templates/invoice/invoice_for_bank_template.docx";
        }

        var resources = new DocxReportResources();
        resources.setName("Invoice");
        resources.setTemplateFile(getTemplateReader().getTemplateFromResource(getTemplateFileName()));

        // здесь идет вариативность: дата коллектор может принимать параметры
        var objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        var data = getDataCollector().collectData(params.getInvoiceId());
        var jsonData = (String) null;
        try {
            jsonData = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error("Ошибка преобразования данных в json");
            throw new RuntimeException(e);
        }
        resources.setJsonData(jsonData);

        return getReportBuilder().build(resources).getContent();
    }
}
