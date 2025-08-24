package user_service.com.example.user_service.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)

public class UserPasswordUpdateRequest {

    @Size(min = 6, message = "Password must be at least 8 characters long")
    String oldPassword;


    @Size(min = 6, message = "Password must be at least 8 characters long")
    String newPassword;

}
