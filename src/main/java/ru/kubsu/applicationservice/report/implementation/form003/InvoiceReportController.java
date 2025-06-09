package ru.kubsu.applicationservice.report.implementation.form003;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.khinc.n2o.demo.report.common.api.ReportController;
import ru.kubsu.applicationservice.report.implementation.form001.Form001ReportGenerator;
import ru.kubsu.applicationservice.report.implementation.form001.Form001ReportParams;

import java.io.IOException;
import java.util.UUID;

/**
 * Контроллер для обработки запросов построения отчетов.
 *
 * @author vkhlybov
 */
@ReportController
@RequiredArgsConstructor
public class InvoiceReportController {

    private final Form001ReportGenerator invoiceReportGenerator;

    @GetMapping(value = "/invoice/{invoiceId}", produces = "application/msword")
    public ByteArrayResource build(@PathVariable UUID invoiceId,
                                   @RequestParam InvoiceFormat format) throws IOException {
        return new ByteArrayResource(
                invoiceReportGenerator.generate(
                                Form001ReportParams.builder()
                                        .invoiceId(invoiceId)
                                        .format(format)
                                        .build())
                        .readAllBytes());
    }
}
