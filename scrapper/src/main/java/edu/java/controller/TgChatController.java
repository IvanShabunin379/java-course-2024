package edu.java.controller;

import edu.java.utils.ValidationUtils;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat")
@Validated
@Slf4j
public class TgChatController {
    @PostMapping("/{id}")
    public ResponseEntity<HttpStatus> registerChat(@PathVariable long id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ValidationUtils.handleBindingResultErrors(bindingResult);
        }

        // TODO: добавить проверку на то, что чат уже зарегистрирован, если это так, выбрасывать соотв. исключение

        log.info("POST: Chat #{} was registered successfully.", id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> removeChat(@PathVariable @Positive long id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ValidationUtils.handleBindingResultErrors(bindingResult);
        }

        // TODO: добавить проверку на то, что чата с таким id не существует, если это так, выбрасывать соотв. исключение

        log.info("DELETE: Chat #{} was removed successfully.", id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

