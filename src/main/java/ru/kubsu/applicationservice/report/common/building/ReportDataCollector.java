package ru.kubsu.applicationservice.report.common.building;

/**
 * Интерфейс сборщика данных для построения отчета.
 *
 * @author vkhlybov
 */
public interface ReportDataCollector {

    /**
     * Метод сборки данных.
     * Реализация метода оперирует моделями, реализованными в пакете model конкретного отчета.
     *
     * @param params параметры
     * @return собранные данные
     */
    Object collectData(Object params);
}
