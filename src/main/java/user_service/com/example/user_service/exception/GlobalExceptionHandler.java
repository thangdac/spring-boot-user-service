package user_service.com.example.user_service.exception;

import jakarta.validation.ConstraintViolation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import user_service.com.example.user_service.dto.request.APIResponse;

import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";


    @ExceptionHandler(value = Exception.class)
    ResponseEntity<APIResponse<Object>> handlingGeneralException(Exception ex) {

        APIResponse<Object> response = new APIResponse<>();

        response.setCode(ErrorCode.UNCATEGORIZED_ERROR.getCode());
        response.setMessage(ErrorCode.UNCATEGORIZED_ERROR.getMessage()+ ": " + ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    //custom exception handler
    @ExceptionHandler(value = ErrorCodeException.class)
    ResponseEntity<APIResponse<Object>> handlingErrorCodeException(ErrorCodeException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        APIResponse<Object> response = new APIResponse<>();

        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<APIResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String enumKey = Objects.requireNonNull(ex.getFieldError()).getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST;

        Map<String, Object> attributes = null;

        try {
            errorCode = ErrorCode.valueOf(enumKey);

            var constraintViolations = ex.getBindingResult()
                    .getAllErrors().getFirst().unwrap(ConstraintViolation.class);

            attributes = constraintViolations.getConstraintDescriptor().getAttributes();

        } catch (IllegalArgumentException e) {
            // Use default INVALID_REQUEST error code
        }

        APIResponse<Object> response = new APIResponse<>();
        response.setCode(errorCode.getCode());
        response.setMessage(
                Objects.nonNull(attributes)
                        ? parseMessage(errorCode.getMessage(), attributes)
                        :errorCode.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    private String parseMessage(String message, Map<String, Object> attributes) {

        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        message = message.replace("{" + MIN_ATTRIBUTE + "}", minValue);

        return message;
    }

}
