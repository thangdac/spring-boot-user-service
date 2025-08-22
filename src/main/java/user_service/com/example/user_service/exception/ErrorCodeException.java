package user_service.com.example.user_service.exception;

public class ErrorCodeException extends RuntimeException {

    public ErrorCodeException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    private ErrorCode errorCode;

    public ErrorCode getErrorCode() {
        return errorCode;
    }
    public int getCode() {
        return errorCode.getCode();
    }
}
