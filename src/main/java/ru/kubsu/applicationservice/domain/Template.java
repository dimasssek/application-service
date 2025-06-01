package ru.kubsu.applicationservice.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "templates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Template {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceType serviceType;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int version;

    @Column(nullable = false)
    private boolean isActive;
}