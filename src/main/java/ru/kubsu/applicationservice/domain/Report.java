package ru.kubsu.applicationservice.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private LocalDate reportDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType reportType;

    @Lob
    @Column(nullable = false)
    private byte[] content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportFormat format;
}

public enum ReportType {
    DAILY, WEEKLY, MONTHLY
}

public enum ReportFormat {
    PDF, XLSX
}