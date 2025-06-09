package ru.kubsu.applicationservice.report.common.building;

import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Вспомогательный класс для получения шаблона из ресурсов приложения.
 *
 * @author vkhlybov
 */
@Component
public class TemplateReader {

    /**
     * Метод получения шаблона отчета из ресурсов.
     *
     * @param fileName имя файла (шаблона)
     * @return объект File с шаблоном
     */
    public File getTemplateFromResource(String fileName) {
        var resource = getClass().getClassLoader().getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("Не найден шаблон для построения отчета: " + fileName);
        } else {
            try {
                return new File(resource.toURI());
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
}
