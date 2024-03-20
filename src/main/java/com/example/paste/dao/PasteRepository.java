package com.example.paste.dao;

import com.example.paste.entity.PasteEntity;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PasteRepository  extends JpaRepository<PasteEntity, Long> {
    @NonNull
    Optional<PasteEntity> getByUuid(@NonNull String uuid);

    @Query(""" 
            SELECT p FROM PasteEntity p
            WHERE p.status = 'PUBLIC' AND p.lifetime > CURRENT TIMESTAMP
            ORDER BY p.creationDate DESC""")
    List<PasteEntity> getLastPublicAndAlivePastes(Pageable pageable);
}
