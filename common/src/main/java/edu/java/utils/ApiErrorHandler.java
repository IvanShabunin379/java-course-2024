package edu.java.utils;

import edu.java.dto.ApiErrorResponse;
import edu.java.exceptions.ClientResponseException;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

@UtilityClass
public class ApiErrorHandler {
    public static @NotNull Mono<ClientResponseException> handleApiError(@NotNull ClientResponse clientResponse) {
        return clientResponse.bodyToMono(ApiErrorResponse.class)
            .flatMap(apiErrorResponse -> Mono.error(new ClientResponseException(apiErrorResponse)));
    }
}
