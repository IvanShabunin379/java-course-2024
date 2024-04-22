package edu.java.bot.controller;

import edu.java.dto.ApiErrorResponse;
import edu.java.exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import static edu.java.utils.ControllerAdviceUtils.handleException;

@RestControllerAdvice
public class UpdatesControllerAdvice {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequestException(BadRequestException e) {
        return handleException(e, HttpStatus.BAD_REQUEST, "Invalid request parameters.");
    }
}
