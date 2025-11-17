package user_service.com.example.user_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import user_service.com.example.user_service.Validator.DobConstraint;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)

public class UserUpdateRequest {

    @Size(min = 5, max = 50, message = "USERNAME_INVALID")
    String name;

    @Email(message = "EMAIL_INVALID")
    String email;

    @DobConstraint(min = 18, message = "DOB_INVALID")
    LocalDate dateOfBirth;

    List<String> roles;

}
