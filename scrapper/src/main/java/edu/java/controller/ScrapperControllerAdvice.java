package edu.java.controller;

import edu.java.dto.ApiErrorResponse;
import edu.java.exceptions.BadRequestException;
import edu.java.service.exceptions.LinkInChatAlreadyExistsException;
import edu.java.service.exceptions.LinkInChatNotFoundException;
import edu.java.service.exceptions.LinkNotFoundException;
import edu.java.service.exceptions.TgChatAlreadyExistsException;
import edu.java.service.exceptions.TgChatNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import static edu.java.utils.ControllerAdviceUtils.handleException;

@RestControllerAdvice
public class ScrapperControllerAdvice {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequestException(BadRequestException e) {
        return handleException(e, HttpStatus.BAD_REQUEST, "Invalid request parameters.");
    }

    @ExceptionHandler(TgChatNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleTgChatNotFoundException(TgChatNotFoundException e) {
        return handleException(e, HttpStatus.NOT_FOUND, "Tg-chat with this id not found.");
    }

    @ExceptionHandler(LinkNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleLinkNotFoundException(LinkNotFoundException e) {
        return handleException(e, HttpStatus.NOT_FOUND, "Link with this id not found.");
    }

    @ExceptionHandler(LinkInChatNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleLinkInChatNotFoundException(LinkInChatNotFoundException e) {
        return handleException(e, HttpStatus.NOT_FOUND, "This link does not already tracked..");
    }

    @ExceptionHandler(TgChatAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleTgChatAlreadyExistsException(TgChatAlreadyExistsException e) {
        return handleException(e, HttpStatus.CONFLICT, "Tg-chat with this id already exists.");
    }

    @ExceptionHandler(LinkInChatAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleLinkInChatAlreadyExistsException(LinkInChatAlreadyExistsException e) {
        return handleException(e, HttpStatus.CONFLICT, "This link already tracked.");
    }
}
