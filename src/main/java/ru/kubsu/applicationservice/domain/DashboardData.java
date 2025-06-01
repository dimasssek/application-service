package ru.kubsu.applicationservice.domain;

import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardData {

    private int applicationsCount;
    private double approvalRate;
    private LocalDateTime approvalTime;
    private Duration averageProcessingTime;
    private int approvalCount;
}