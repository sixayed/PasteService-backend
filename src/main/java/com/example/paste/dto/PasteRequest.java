package com.example.paste.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PasteRequest {
    @NotBlank
    private final String data;
    @Positive
    private final long availableTimeSeconds;
    @NotBlank
    private final String status;
}
