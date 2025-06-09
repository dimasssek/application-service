package ru.kubsu.applicationservice.report.common.building;

import ru.khinc.n2o.demo.report.builder.ReportBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Интерфейс построителя отчетов.
 *
 * @author vkhlybov
 */
public interface ReportGenerator {

    /**
     * Метод получения имени файла шаблона.
     *
     * @return имя файла шаблона
     */
    String getTemplateFileName();

    /**
     * Метод получения читателя шаблона.
     *
     * @return читатель шаблона
     */
    TemplateReader getTemplateReader();

    /**
     * Метод получения сборщика данных.
     *
     * @return сборщик данных
     */
    ReportDataCollector getDataCollector();

    /**
     * Метод получения построителя отчета.
     *
     * @return построитель отчета
     */
    ReportBuilder getReportBuilder();

    /**
     * Метод генерации отчета.
     *
     * @param params параметры для построения
     * @return сгенерированный отчет
     */
    InputStream generate(Object params) throws IOException;
}
