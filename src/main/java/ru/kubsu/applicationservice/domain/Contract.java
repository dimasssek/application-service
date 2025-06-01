package ru.kubsu.applicationservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "contracts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contract {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @Column(nullable = false)
    private String templateType;

    @Lob
    @Column(nullable = false)
    private byte[] signedDocument;

    @OneToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    private LocalDateTime creationDate;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime generatedAt;

    private LocalDateTime signedAt;
}