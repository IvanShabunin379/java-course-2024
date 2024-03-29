package edu.java.controller;

import edu.java.dto.ApiErrorResponse;
import edu.java.utils.ValidationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
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
@ApiResponses({
    @ApiResponse(
        responseCode = "400",
        description = "Некорректные параметры запроса",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
})
public class TgChatController {
    @Operation(summary = "Зарегистрировать чат")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Чат зарегистрирован"),
        @ApiResponse(
            responseCode = "409",
            description = "Чат уже зарегистрирован",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        )
    })
    @PostMapping("/{id}")
    public void registerChat(@PathVariable long id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ValidationUtils.handleBindingResultErrors(bindingResult);
        }

        // TODO: добавить проверку на то, что чат уже зарегистрирован, если это так, выбрасывать соотв. исключение

        log.info("POST: Chat #{} was registered successfully.", id);
    }

    @Operation(summary = "Удалить чат")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Чат успешно удалён"),
        @ApiResponse(
            responseCode = "404",
            description = "Чат не существует",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        )
    })
    @DeleteMapping("/{id}")
    public void removeChat(@PathVariable @Positive long id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ValidationUtils.handleBindingResultErrors(bindingResult);
        }

        // TODO: добавить проверку на то, что чата с таким id не существует, если это так, выбрасывать соотв. исключение

        log.info("DELETE: Chat #{} was removed successfully.", id);
    }
}

