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

//     USER – BUSINESS ERRORS
    USER_NOT_FOUND(700, "User not found", HttpStatus.NOT_FOUND),
    // → Không tìm thấy user theo id / username

    USER_ALREADY_EXISTS(701, "User already exists", HttpStatus.CONFLICT),
    // → User (username/email) đã tồn tại

    USER_NOT_EXISTS(702, "User not exists", HttpStatus.REQUEST_TIMEOUT),
    // → User không tồn tại trong quá trình xử lý nghiệp vụ

    INCORRECT_PASSWORD(710, "Incorrect password", HttpStatus.BAD_REQUEST),
    // → Sai mật khẩu khi login / đổi mật khẩu

//     REQUEST / VALIDATION ERRORS

    INVALID_REQUEST(703, "Invalid request", HttpStatus.BAD_REQUEST),
    // → Request body / param không hợp lệ

    USERNAME_INVALID(706, "Name must be between {min} and 50 characters", HttpStatus.BAD_REQUEST),
    // → Username không đúng độ dài

    PASSWORD_INVALID(707, "Password must be between {min} and 20 characters", HttpStatus.BAD_REQUEST),
    // → Password không hợp lệ

    EMAIL_INVALID(708, "Email should be valid", HttpStatus.BAD_REQUEST),
    // → Email sai định dạng

    DOB_INVALID(712, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    // → Tuổi không đạt yêu cầu

//    AUTHENTICATION / AUTHORIZATION

    UNAUTHENTICATED(709, "User is not authenticated", HttpStatus.UNAUTHORIZED),
    // → Chưa đăng nhập / token sai / token hết hạn

    UNAUTHORIZED(711, "User is not authorized to perform this action", HttpStatus.FORBIDDEN),
    // → Đã đăng nhập nhưng không đủ quyền (AccessDenied)

//     SYSTEM / FALLBACK ERRORS

    INTERNAL_SERVER_ERROR(704, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    // → Lỗi hệ thống, exception không mong muốn

    UNCATEGORIZED_ERROR(705, "Uncategorized error", HttpStatus.UNAUTHORIZED);
    // → Lỗi không phân loại được (fallback trong GlobalExceptionHandler)

    final int code;

    final String message;

    final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

}
