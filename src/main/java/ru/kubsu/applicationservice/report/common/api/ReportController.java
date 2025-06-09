package ru.kubsu.applicationservice.report.common.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для разметки API контроллеров генерации отчетов и документов.
 *
 * @author vkhlybov
 */
@RestController
@RequestMapping("api/report")
@CrossOrigin
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ReportController {
}
