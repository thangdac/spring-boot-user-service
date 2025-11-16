package user_service.com.example.user_service.exception;

import lombok.Getter;

@Getter
public class ErrorCodeException extends RuntimeException {

    public ErrorCodeException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    private final ErrorCode errorCode;
}
