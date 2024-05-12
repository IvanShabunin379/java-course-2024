package edu.java.utils;

import edu.java.dto.ApiErrorResponse;
import edu.java.exceptions.ClientErrorException;
import edu.java.exceptions.ServerErrorException;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

@UtilityClass
public class ApiErrorHandler {
    public static @NotNull Mono<ClientErrorException> handleClientError(@NotNull ClientResponse clientResponse) {
        return clientResponse.bodyToMono(ApiErrorResponse.class)
            .flatMap(apiErrorResponse -> Mono.error(new ClientErrorException(apiErrorResponse)));
    }

    public static @NotNull Mono<ServerErrorException> handleServerError(@NotNull ClientResponse clientResponse) {
        return clientResponse.bodyToMono(ApiErrorResponse.class)
            .flatMap(
                apiErrorResponse -> Mono.error(new ServerErrorException(HttpStatus.valueOf(apiErrorResponse.code())))
            );
    }
}
