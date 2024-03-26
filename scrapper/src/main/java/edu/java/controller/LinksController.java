package edu.java.controller;

import edu.java.dto.AddLinkRequest;
import edu.java.dto.LinkResponse;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.RemoveLinkRequest;
import edu.java.utils.ValidationUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
@Validated
@Slf4j
public class LinksController {
    @GetMapping
    public ResponseEntity<ListLinksResponse> getLinks(
        @RequestHeader("Tg-Chat-Id") @Positive long chatId,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ValidationUtils.handleBindingResultErrors(bindingResult);
        }

        // TODO: добавить проверку на то, что чата с таким id не существует, если это так, выбрасывать соотв. исключение

        log.info("GET: Links were received successfully.");
        return ResponseEntity.status(HttpStatus.OK).body(new ListLinksResponse(List.of(), 0L));
    }

    @PostMapping
    public ResponseEntity<LinkResponse> addLink(
        @RequestHeader("Tg-Chat-Id") @Positive long chatId,
        @RequestBody @Valid AddLinkRequest addLinkRequest,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ValidationUtils.handleBindingResultErrors(bindingResult);
        }

        // TODO: добавить проверку на то, что в данном чате такая ссылка уже есть

        log.info("POST: Link {} was added successfully.", addLinkRequest.link());
        return ResponseEntity.status(HttpStatus.OK).body(new LinkResponse(0L, addLinkRequest.link()));
    }

    @DeleteMapping
    public ResponseEntity<LinkResponse> removeLink(
        @RequestHeader("Tg-Chat-Id") @Positive long chatId,
        @RequestBody @Valid RemoveLinkRequest removeLinkRequest,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ValidationUtils.handleBindingResultErrors(bindingResult);
        }

        // TODO: добавить проверку на то, что чата с таким id не сущ-ет, если это так, выбрасывать соотв. исключение
        // TODO: добавить проверку на то, что ссылки с таким id не сущ-ет, если это так, выбрасывать соотв. исключение
        // TODO: добавить проверку на то, в данном чате нет такой ссылки, если это так, выбрасывать соотв. исключение

        log.info("DELETE: Link {} was removed successfully.", removeLinkRequest.link());
        return ResponseEntity.status(HttpStatus.OK).body(new LinkResponse(0L, removeLinkRequest.link()));
    }
}

