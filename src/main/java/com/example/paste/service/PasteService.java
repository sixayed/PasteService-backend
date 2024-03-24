package com.example.paste.service;

import com.example.paste.config.AppConfig;
import com.example.paste.dao.PasteRepository;
import com.example.paste.dto.PasteRequest;
import com.example.paste.dto.PasteResponse;
import com.example.paste.dto.PasteUrlResponse;
import com.example.paste.dto.PublicStatus;
import com.example.paste.entity.PasteEntity;
import com.example.paste.exception.PasteNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasteService {

    private final PasteRepository repository;

    private final AppConfig config;

    public PasteResponse getByUuid(@NonNull String uuid) {
        return repository.getByUuid(uuid)
                .map(paste -> new PasteResponse(paste.getData()))
                .orElseThrow(() -> new PasteNotFoundException(uuid));
    }

    public List<PasteResponse> getLastPublicPastes() {
        return repository.getLastPublicAndAlivePastes(PageRequest.of(0, config.getPublicListSize()))
                .stream()
                .map(paste -> new PasteResponse(paste.getData()))
                .toList();
    }

    public PasteUrlResponse create(@NonNull PasteRequest request) {
        if(!validateStatus(request.getStatus())) {
            log.warn("Status {} is invalid", request.getStatus());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Unknown status");
        }
        String uuid = generateUuid();
        try {
            LocalDateTime dateNow = LocalDateTime.now()
                    .atZone(ZoneId.of("Europe/Moscow")).toLocalDateTime();

            PasteEntity pasteEntity = new PasteEntity(
                    uuid,
                    request.getData(),
                    dateNow,
                    dateNow.plusSeconds(request.getAvailableTimeSeconds()),
                    PublicStatus.valueOf(request.getStatus().toUpperCase())
            );

            repository.save(pasteEntity);
        }
        catch (Exception e) {
            log.warn("Creating error: " + e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid data"); // пока пусть будет так
        }

        return new PasteUrlResponse(config.getHost() + "/" + uuid);
    }

    private boolean validateStatus(String status) {
        try {
            PublicStatus.valueOf(status.toUpperCase());
        }
        catch (IllegalArgumentException e) {
            return false;
        }

        return true;
    }

    private String generateUuid() {
        return UUID.randomUUID().toString();
    }
}
