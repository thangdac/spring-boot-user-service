package user_service.com.example.user_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import user_service.com.example.user_service.dto.request.APIResponse;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    ResponseEntity<APIResponse<Object>> handlingGeneralException(Exception ex) {

        APIResponse<Object> response = new APIResponse<>();

        response.setCode(ErrorCode.UNCATEGORIZED_ERROR.getCode());
        response.setMessage(ErrorCode.UNCATEGORIZED_ERROR.getMessage()+ ": " + ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ErrorCodeException.class)
    ResponseEntity<APIResponse<Object>> handlingErrorCodeException(ErrorCodeException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        APIResponse<Object> response = new APIResponse<>();

        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<APIResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String enumKey = Objects.requireNonNull(ex.getFieldError()).getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(enumKey);

        APIResponse<Object> response = new APIResponse<>();
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(response);
    }


}
