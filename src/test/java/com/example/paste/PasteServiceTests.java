package com.example.paste;

import com.example.paste.config.AppConfig;
import com.example.paste.dao.PasteRepository;
import com.example.paste.dto.PasteRequest;
import com.example.paste.dto.PasteResponse;
import com.example.paste.dto.PasteUrlResponse;
import com.example.paste.dto.PublicStatus;
import com.example.paste.entity.PasteEntity;
import com.example.paste.exception.PasteNotFoundException;
import com.example.paste.service.PasteService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PasteServiceTests {
    @Mock
    PasteRepository repository;
    @Mock
    AppConfig config;
    @InjectMocks
    PasteService service;

    private final String uuid = "uuid";
    private final String data = "data";
    private final int limit = 10;
    private final Pageable pageable = PageRequest.of(0, limit);

    @Test
    public void getByUuid_ValidUuid_returnsPasteResponse() {
        //arrange
        PasteEntity pasteEntity = new PasteEntity(
                uuid,
                data,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                PublicStatus.PUBLIC
        );
        when(repository.getByUuid(uuid)).thenReturn(Optional.of(pasteEntity));

        //act
        PasteResponse response = service.getByUuid(uuid);

        //assert
        assertNotNull(response);
        assertEquals(data, response.getData());
    }

    @Test
    public void getByUuid_InvalidUuid_throwException() {
        //arrange
        String invalidUuid = "invalidUuid";
        when(repository.getByUuid(uuid)).thenReturn(Optional.empty());

        //act && assert
        assertThrows(PasteNotFoundException.class, () -> service.getByUuid(invalidUuid));
    }

    @Test
    public void getLastPublicPastes_nonEmptyList_returnsListOfPastes() {
        //arrange
        List<PasteEntity> entities = new ArrayList<>();
        int entitiesCount = 5;
        for(int i = 0; i < entitiesCount; i++) {
            PasteEntity pasteEntity = new PasteEntity(
                    uuid,
                    data + i,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusHours(1),
                    PublicStatus.PUBLIC
            );

            entities.add(pasteEntity);
        }
        when(config.getPublicListSize()).thenReturn(limit);
        when(repository.getLastPublicAndAlivePastes(pageable)).thenReturn(entities);

        //act
        List<PasteResponse> responses = service.getLastPublicPastes();

        //assert
        assertNotNull(responses);
        assertEquals(entitiesCount, responses.size());
        assertEquals(
                entities.stream()
                        .map(PasteEntity::getData)
                        .collect(Collectors.toList()),
                responses.stream()
                        .map(PasteResponse::getData)
                        .collect(Collectors.toList())
        );
    }

    @Test
    void create_ValidRequest_returnsUrlResponse() {
        //arrange
        String host = "http://test.com";
        String status = "public";
        when(config.getHost()).thenReturn(host);
        when(repository.save(any())).thenReturn(null);

        //act
        PasteUrlResponse urlResponse = service.create(new PasteRequest(data, 3600, status));

        //assert
        assertNotNull(urlResponse);
        assertTrue(urlResponse.getUrl().startsWith(host));
    }

    @Test
    void create_InvalidStatus_throwsResponseStatusException() {
        //arrange
        String invalidStatus = "invalidStatus";
        PasteRequest request = new PasteRequest(data, 3600, invalidStatus);

        //act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> service.create(request));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }
}
