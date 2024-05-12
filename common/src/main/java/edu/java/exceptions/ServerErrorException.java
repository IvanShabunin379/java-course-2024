package edu.java.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public class ServerErrorException extends RuntimeException {
    private final HttpStatus httpStatus;
}
