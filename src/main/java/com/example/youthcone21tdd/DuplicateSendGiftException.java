package com.example.youthcone21tdd;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DuplicateSendGiftException extends RuntimeException {
    public DuplicateSendGiftException(String s) {
    }
}
