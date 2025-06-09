package ru.kubsu.applicationservice.report.implementation.form001;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.allegro.finance.tradukisto.ValueConverters;
import ru.khinc.n2o.demo.report.common.building.ReportDataCollector;
import ru.khinc.n2o.demo.report.common.building.TranslationService;
import ru.khinc.n2o.demo.report.implementation.invoice.model.InvoiceReportData;
import ru.khinc.n2o.demo.repository.InvoiceRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class Form001ReportDataCollector implements ReportDataCollector {

    private final InvoiceRepository invoiceRepository;

    private final TranslationService translationService;

    @Override
    @Transactional
    public Object collectData(Object params) {
        if (!(params instanceof UUID invoiceId)) {
            throw new IllegalArgumentException("Идентификатор счета некорректен");
        }

        var invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(NoSuchElementException::new);

        var sumConverter = ValueConverters.RUSSIAN_INTEGER;
        var totalSumRus = sumConverter.asWords(invoice.getTotalSum().intValue());
        var totalSumDe = translationService.translateToGerman(totalSumRus);

        return InvoiceReportData.builder()
                .number(invoice.getNumber())
                .date(invoice.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .periodRus(String.format(
                        "%s %d",
                        formatMonthInRussian(
                                invoice.getPayslip().getMonth()),
                        invoice.getPayslip().getYear()))
                .periodDe(String.format(
                        "%s %d",
                        StringUtils.capitalize(
                                translationService.translateToGerman(
                                        formatMonthInRussian(
                                                invoice.getPayslip().getMonth()))),
                        invoice.getPayslip().getYear()))
                .primaryClassesAmount(invoice.getTotalPrimaryClasses())
                .primaryClassesPrice(BigDecimal.valueOf(30))
                .primaryClassesSum(invoice.getPrimaryClassesTotalSum())
                .additionalHoursAmount(invoice.getTotalAdditionalHours())
                .additionalHoursPrice(BigDecimal.valueOf(10))
                .additionalHoursSum(invoice.getAdditionalHoursTotalSum())
                .totalSum(invoice.getTotalSum())
                .totalSumRus(totalSumRus)
                .totalSumDe(totalSumDe)
                .build();
    }

    private String formatMonthInRussian(int month) {
        return switch (month) {
            case 1 -> "январе";
            case 2 -> "феврале";
            case 3 -> "марте";
            case 4 -> "апреле";
            case 5 -> "мае";
            case 6 -> "июне";
            case 7 -> "июле";
            case 8 -> "августе";
            case 9 -> "сентябре";
            case 10 -> "октябре";
            case 11 -> "ноябре";
            case 12 -> "декабре";
            default -> "ОШИБКА ОПРЕДЕЛЕНИЯ МЕСЯЦА";
        };
    }
}
