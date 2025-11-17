package user_service.com.example.user_service.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public enum ErrorCode {
    USER_NOT_FOUND(700, "User not found", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(701, "User already exists", HttpStatus.CONFLICT),
    USER_NOT_EXISTS(702, "User not exists", HttpStatus.REQUEST_TIMEOUT),
    INVALID_REQUEST(703, "Invalid request", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR(704, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNCATEGORIZED_ERROR(705, "Uncategorized error", HttpStatus.UNAUTHORIZED),
    USERNAME_INVALID(706, "Name must be between {min} and 50 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(707, "Password must be between {min} and 20 characters", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(708, "Email should be valid", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(709, "User is not authenticated", HttpStatus.UNAUTHORIZED),
    INCORRECT_PASSWORD(710, "Incorrect password", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(711, "User is not authorized to perform this action", HttpStatus.FORBIDDEN),
    DOB_INVALID(712, "Your age must be at least {min}", HttpStatus.BAD_REQUEST)

    ;
    final int code;

    final String message;

    final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

}
