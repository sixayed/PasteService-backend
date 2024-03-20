package com.example.paste.entity;

import com.example.paste.dto.PublicStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(force = true)
@Table(name = "Pastes")
public class PasteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String uuid;
    private String data;
    private LocalDateTime creationDate;
    private LocalDateTime lifetime;

    @Enumerated(EnumType.STRING)
    private PublicStatus status;

    public PasteEntity(String uuid, String data, LocalDateTime creationDate,
                       LocalDateTime lifetime, PublicStatus status) {
        this.uuid = uuid;
        this.data = data;
        this.creationDate = creationDate;
        this.lifetime = lifetime;
        this.status = status;
    }
}
