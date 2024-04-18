package edu.java.utils;

import edu.java.dto.ApiErrorResponse;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@UtilityClass
public class ApiErrorHandler {
    public static @NotNull Mono<Exception> handleApiError(@NotNull ClientResponse clientResponse) {
        return clientResponse.bodyToMono(ApiErrorResponse.class)
            .flatMap(apiErrorResponse -> Mono.error(new WebClientResponseException(
                apiErrorResponse.description(),
                clientResponse.statusCode().value(),
                clientResponse.statusCode().toString(),
                clientResponse.headers().asHttpHeaders(),
                null,
                null
            )));
    }
}
