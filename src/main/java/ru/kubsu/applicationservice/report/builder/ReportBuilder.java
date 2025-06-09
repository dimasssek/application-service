package ru.kubsu.applicationservice.report.builder;

import net.jsreport.java.dto.Report;

import java.io.IOException;

/**
 * Интерфейс построителя отчетов с помощью JsReport.
 *
 * @author vkhlybov
 */
public interface ReportBuilder {

    /**
     * Метод построения отчета по мета данным.
     *
     * @param reportResources мета данные отчета
     * @return сформированный отчет
     */
    Report build(ReportResources reportResources) throws IOException;
}
