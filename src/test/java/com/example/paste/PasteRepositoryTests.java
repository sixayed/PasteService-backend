package com.example.paste;

import com.example.paste.dao.PasteRepository;
import com.example.paste.dto.PublicStatus;
import com.example.paste.entity.PasteEntity;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class PasteRepositoryTests {

    @Autowired
    PasteRepository pasteRepository;

    @Autowired
    TestEntityManager entityManager;

    private final String uuid = "uuid";
    private final String data = "data";
    private final int limit = 1;
    private final Pageable pageable = PageRequest.of(0, limit);

    @Test
    public void savePaste_successfullyCreates_returnsEntity() {
        //arrange
        PasteEntity pasteEntity = new PasteEntity(
                uuid,
                data,
                LocalDateTime.now(),
                LocalDateTime.now(),
                PublicStatus.PUBLIC
        );

        //act
        pasteRepository.save(pasteEntity);
        Optional<PasteEntity> savedEntity = pasteRepository.getByUuid(uuid);

        //assert
        assertTrue(savedEntity.isPresent());
        assertTrue(new ReflectionEquals(savedEntity.get()).matches(pasteEntity));
    }

    @Test
    public void getLastPublicAndAlivePastes_onePublicExists_returnsOneEntity() {
        //arrange
        PasteEntity pasteEntity = new PasteEntity(
                uuid,
                data,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                PublicStatus.PUBLIC
        );

        //act
        pasteRepository.save(pasteEntity);

        //assert
        assertEquals(1, pasteRepository.getLastPublicAndAlivePastes(pageable).size());
    }

    @Test
    public void getLastPublicAndAlivePastes_onePrivateExists_returnsEmptyList() {
        //arrange
        PasteEntity pasteEntity = new PasteEntity(
                uuid,
                data,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                PublicStatus.PRIVATE
        );

        //act
        pasteRepository.save(pasteEntity);

        //assert
        assertEquals(0, pasteRepository.getLastPublicAndAlivePastes(pageable).size());
    }


    @Test
    public void getLastPublicAndAlivePastes_noAliveExists_returnsEmptyList() {
        //arrange
        PasteEntity pasteEntity = new PasteEntity(
                uuid,
                data,
                LocalDateTime.now(),
                LocalDateTime.now(),
                PublicStatus.PUBLIC
        );

        //act
        pasteRepository.save(pasteEntity);

        //assert
        assertEquals(0, pasteRepository.getLastPublicAndAlivePastes(pageable).size());
    }

    @Test
    public void getLastPublicAndAlivePastes_publicEntitiesBeyondLimit_returnsOneEntity() {
        //arrange
        PasteEntity pasteEntity = new PasteEntity(
                uuid,
                data,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                PublicStatus.PUBLIC
        );

        //act
        pasteRepository.save(pasteEntity);
        pasteRepository.save(pasteEntity);

        //assert
        assertEquals(1, pasteRepository.getLastPublicAndAlivePastes(pageable).size());
    }
}
