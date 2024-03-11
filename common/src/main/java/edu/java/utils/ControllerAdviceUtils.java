package edu.java.utils;

import edu.java.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;

public final class ControllerAdviceUtils {
    private ControllerAdviceUtils() {
    }

    public static ResponseEntity<ApiErrorResponse> handleException(Exception e, HttpStatus httpStatus, String description) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
            description,
            String.valueOf(httpStatus.value()),
            e.getClass().getSimpleName(),
            e.getMessage(),
            Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
        );

        return ResponseEntity.status(httpStatus).body(apiErrorResponse);
    }
}
