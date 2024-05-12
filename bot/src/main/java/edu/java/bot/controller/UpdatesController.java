package edu.java.bot.controller;

import edu.java.bot.service.UpdatesService;
import edu.java.dto.ApiErrorResponse;
import edu.java.dto.LinkUpdateRequest;
import edu.java.utils.ValidationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
@Slf4j
@RequiredArgsConstructor
@ApiResponses({
    @ApiResponse(
        responseCode = "400",
        description = "Некорректные параметры запроса",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
})
public class UpdatesController {
    private final UpdatesService updatesService;

    @Operation(summary = "Отправить обновление")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Обновление обработано"),
        @ApiResponse(
            responseCode = "404",
            description = "Чат не существует",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Ссылка не существует",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Ссылка не отслеживается в данном чате",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        )
    })
    @PostMapping
    public void sendLinkUpdate(
        @RequestBody @Valid LinkUpdateRequest linkUpdateRequest,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ValidationUtils.handleBindingResultErrors(bindingResult);
        }

        updatesService.sendLinkUpdate(linkUpdateRequest.description(), linkUpdateRequest.tgChatIds());
        log.info("POST: Link Update for {} has been processed successfully.", linkUpdateRequest.url());
    }
}
