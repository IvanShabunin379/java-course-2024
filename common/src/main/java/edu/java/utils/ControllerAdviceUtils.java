package edu.java.utils;

import edu.java.dto.ApiErrorResponse;
import java.util.Arrays;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@UtilityClass
public class ControllerAdviceUtils {
    public ResponseEntity<ApiErrorResponse> handleException(
        Exception e,
        HttpStatus httpStatus,
        String description
    ) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
            description,
            String.valueOf(httpStatus),
            e.getClass().getSimpleName(),
            e.getMessage(),
            Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
        );

        return ResponseEntity.status(httpStatus).body(apiErrorResponse);
    }
}
