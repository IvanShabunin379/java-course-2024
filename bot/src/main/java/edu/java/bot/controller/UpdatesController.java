package edu.java.bot.controller;

import edu.java.dto.LinkUpdateRequest;
import edu.java.utils.ValidationUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
@Slf4j
public class UpdatesController {
    @PostMapping
    public ResponseEntity<HttpStatus> sendLinkUpdate(
        @RequestBody @Valid LinkUpdateRequest linkUpdateRequest,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ValidationUtils.handleBindingResultErrors(bindingResult);
        }

        // TODO: добавить проверку на существование чата
        // TODO: добавить проверку на существование ссылки
        // TODO: добавить проверку на то, что ссылка находится хотя бы в одном чате

        log.info("POST: Link Update for {} has been processed successfully.", linkUpdateRequest.url());
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
