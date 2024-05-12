package edu.java.utils;

import edu.java.exceptions.BadRequestException;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@UtilityClass
public class ValidationUtils {
    public static void handleBindingResultErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                    .append(" - ").append(error.getDefaultMessage())
                    .append("; ");
            }

            throw new BadRequestException(errorMsg.toString());
        }
    }
}
