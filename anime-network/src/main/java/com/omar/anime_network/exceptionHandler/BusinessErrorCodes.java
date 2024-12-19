package com.omar.anime_network.exceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum BusinessErrorCodes {
    BAD_CREDENTIALS(304, HttpStatus.FORBIDDEN, "Email or password are incorrect");

    private final int code;
    private final HttpStatus httpStatus;
    private final String description;
}