package ru.kubsu.applicationservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "application")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String data;

    @Lob
    @Column(nullable = false)
    private byte[] document;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime processedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceType serviceType;
}

public enum ApplicationStatus {
    NEW, PROCESSING, COMPLETED, REJECTED
}