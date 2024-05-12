package edu.java.exceptions;

import edu.java.dto.ApiErrorResponse;
import lombok.Getter;

@Getter
public class ClientErrorException extends RuntimeException {
    private final ApiErrorResponse apiErrorResponse;

    public ClientErrorException(ApiErrorResponse apiErrorResponse) {
        this.apiErrorResponse = apiErrorResponse;
    }
}
