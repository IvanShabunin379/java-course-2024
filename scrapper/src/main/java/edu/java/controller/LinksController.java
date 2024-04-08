package edu.java.controller;

import edu.java.domain.model.jdbc.Link;
import edu.java.dto.AddLinkRequest;
import edu.java.dto.ApiErrorResponse;
import edu.java.dto.LinkResponse;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.RemoveLinkRequest;
import edu.java.service.LinksService;
import edu.java.utils.ValidationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
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
@ApiResponses({
    @ApiResponse(
        responseCode = "400",
        description = "Некорректные параметры запроса",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
})
public class LinksController {
    private final LinksService linksService;

    public LinksController(LinksService linksService) {
        this.linksService = linksService;
    }

    @Operation(summary = "Получить все отслеживаемые ссылки")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ссылки успешно получены"),
        @ApiResponse(
            responseCode = "404",
            description = "Чат не существует",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        )
    })
    @GetMapping
    public ListLinksResponse getLinks(
        @RequestHeader("Tg-Chat-Id") @Positive long chatId,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ValidationUtils.handleBindingResultErrors(bindingResult);
        }

        List<LinkResponse> linkResponses = linksService.listAll(chatId)
            .stream()
            .map(link -> new LinkResponse(link.id(), link.url()))
            .toList();

        log.info("GET: Links were received successfully.");
        return new ListLinksResponse(linkResponses, (long) linkResponses.size());
    }

    @Operation(summary = "Добавить отслеживание ссылки")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ссылка успешно добавлена"),
        @ApiResponse(
            responseCode = "409",
            description = "Ссылка уже отслеживается",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        )
    })
    @PostMapping
    public LinkResponse addLink(
        @RequestHeader("Tg-Chat-Id") @Positive long chatId,
        @RequestBody @Valid AddLinkRequest addLinkRequest,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ValidationUtils.handleBindingResultErrors(bindingResult);
        }

        Link addedLink = linksService.add(chatId, addLinkRequest.link());
        log.info("POST: Link {} was added successfully.", addLinkRequest.link());
        return new LinkResponse(addedLink.id(), addedLink.url());
    }

    @Operation(summary = "Убрать отслеживание ссылки")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ссылка успешно убрана"),
        @ApiResponse(
            responseCode = "404",
            description = "Ссылка не найдена",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        )
    })
    @DeleteMapping
    public LinkResponse removeLink(
        @RequestHeader("Tg-Chat-Id") @Positive long chatId,
        @RequestBody @Valid RemoveLinkRequest removeLinkRequest,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ValidationUtils.handleBindingResultErrors(bindingResult);
        }

        Link removedLink = linksService.remove(chatId, removeLinkRequest.link());
        log.info("DELETE: Link {} was removed successfully.", removeLinkRequest.link());
        return new LinkResponse(removedLink.id(), removedLink.url());
    }
}
