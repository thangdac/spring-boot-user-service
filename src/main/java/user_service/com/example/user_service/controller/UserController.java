package user_service.com.example.user_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import user_service.com.example.user_service.dto.request.APIResponse;
import user_service.com.example.user_service.dto.request.UserCreationRequest;
import user_service.com.example.user_service.dto.request.UserPasswordUpdateRequest;
import user_service.com.example.user_service.dto.request.UserUpdateRequest;
import user_service.com.example.user_service.dto.response.UserResponse;
import user_service.com.example.user_service.entity.User;
import user_service.com.example.user_service.service.AuthenticationService;
import user_service.com.example.user_service.service.UserService;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;
    private final AuthenticationService authenticationService;

    @GetMapping
    APIResponse<List<UserResponse>> getAllUsers() {

        SecurityContextHolder.getContext().getAuthentication().getAuthorities().forEach(authority ->
                log.info("Authority: {}", authority.getAuthority())
        );

        return APIResponse.<List<UserResponse>>builder()
                .code(200)
                .result(userService.getAllUsers())
                .build();
    }

    @GetMapping("/me")
    APIResponse<UserResponse> getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        return APIResponse.<UserResponse>builder()
                .code(200)
                .result(userService.getUserByName(authentication.getName()))
                .build();
    }

    @GetMapping("/{id}")
    APIResponse<UserResponse> getUserById(@PathVariable String id) {

        return APIResponse.<UserResponse>builder()
                .code(200)
                .result(userService.getUserById(id))
                .build();
    }

    @PostMapping
    APIResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return APIResponse.<UserResponse>builder()
                .code(200)
                .result(userService.createUser(request))
                .build();
    }

    @PutMapping("/{id}")
    APIResponse<UserResponse> updateUser(@PathVariable String id,@RequestBody @Valid UserUpdateRequest request) {

        return APIResponse.<UserResponse>builder()
                .code(200)
                .result(userService.updateUser(id, request))
                .build();
    }

    @PatchMapping("/updatePassword/{id}")
    APIResponse<UserResponse> updateUserPassword(@PathVariable String id,@RequestBody @Valid UserPasswordUpdateRequest request) {
        return APIResponse.<UserResponse>builder()
                .code(200)
                .message("Password updated successfully")
                .result(userService.updateUserPassword(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    APIResponse<User> deleteUser(@PathVariable String id) {

        return APIResponse.<User>builder()
                .code(200)
                .message(userService.deleteUser(id))
                .build();
    }
}
