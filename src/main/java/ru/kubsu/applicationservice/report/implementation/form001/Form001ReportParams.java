package ru.kubsu.applicationservice.report.implementation.form001;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder(builderClassName = "Builder")
public class Form001ReportParams {

    private UUID invoiceId;

    private InvoiceFormat format;
}
