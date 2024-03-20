package com.example.paste.controller;

import com.example.paste.dto.PasteRequest;
import com.example.paste.dto.PasteResponse;
import com.example.paste.dto.PasteUrlResponse;
import com.example.paste.service.PasteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class PasteController {

    private final PasteService pasteService;

    @GetMapping("/")
    public Collection<PasteResponse> getPublicPasteList() {
        return pasteService.getLastPublicPastes();
    }

    @GetMapping("/{uuid}")
    public PasteResponse getByHash(@PathVariable String uuid) {
        return pasteService.getByUuid(uuid);
    }

    @PostMapping("/")
    public PasteUrlResponse add(@Valid @RequestBody PasteRequest requestBody) {
        return pasteService.create(requestBody);
    }
}
