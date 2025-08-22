package user_service.com.example.user_service.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public enum ErrorCode {
    USER_NOT_FOUND(404, "User not found"),
    USER_ALREADY_EXISTS(409, "User already exists"),
    USER_NOT_EXISTS(408   , "User not exists"),
    INVALID_REQUEST(400, "Invalid request"),
    INTERNAL_SERVER_ERROR(500, "Internal server error"),
    UNCATEGORIZED_ERROR(520, "Uncategorized error"),
    USERNAME_INVALID(400, "Name must be between 3 and 50 characters"),
    PASSWORD_INVALID(400, "Password must be between 6 and 20 characters"),
    EMAIL_INVALID(400, "Email should be valid"),
    UNAUTHENTICATED(401, "User is not authenticated"),
    ;
    final int code;
    final String message;
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
