package edu.java.exceptions;

import edu.java.dto.ApiErrorResponse;
import lombok.Getter;

@Getter
public class ClientResponseException extends RuntimeException {
    private final ApiErrorResponse apiErrorResponse;

    public ClientResponseException(ApiErrorResponse apiErrorResponse) {
        this.apiErrorResponse = apiErrorResponse;
    }
}
