package com.example.paste.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PasteNotFoundException extends RuntimeException {
    public PasteNotFoundException(String uuid) {
        super("Paste with uuid = " + uuid + " not found");
    }
}
