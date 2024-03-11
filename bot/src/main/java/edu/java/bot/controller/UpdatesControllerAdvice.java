package edu.java.bot.controller;

import edu.java.dto.ApiErrorResponse;
import edu.java.exceptions.BadRequestException;
import edu.java.exceptions.LinkInChatNotFoundException;
import edu.java.exceptions.LinkNotFoundException;
import edu.java.exceptions.TgChatNotFoundException;
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
        return handleException(e, HttpStatus.NOT_FOUND, "Link with this id not found in tg-chat with such id.");
    }
}
