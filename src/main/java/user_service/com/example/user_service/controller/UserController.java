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
import user_service.com.example.user_service.service.UserService;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @GetMapping
    APIResponse<List<UserResponse>> getAllUsers() {

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
        APIResponse<UserResponse> response = new APIResponse<>();

        response.setCode(200);
        response.setResult(userService.createUser(request));

        return response;
    }

    @PutMapping("/{id}")
    APIResponse<UserResponse> updateUser(@PathVariable String id,@RequestBody @Valid UserUpdateRequest request) {

        APIResponse<UserResponse> response = new APIResponse<>();

        response.setCode(200);
        response.setResult(userService.updateUser(id, request));

        return response;
    }

    @PatchMapping("/updatePassword/{id}")
    APIResponse<UserResponse> updateUserPassword(@PathVariable String id,@RequestBody @Valid UserPasswordUpdateRequest request) {
        APIResponse<UserResponse> response = new APIResponse<>();

        response.setCode(200);
        response.setMessage("Password updated successfully");
        response.setResult(userService.updateUserPassword(id, request));

        return response;
    }

    @DeleteMapping("/{id}")
    APIResponse<User> deleteUser(@PathVariable String id) {

        APIResponse<User> response = new APIResponse<>();
        String message = userService.deleteUser(id);

        response.setCode(200);
        response.setMessage(message);

        return response;
    }
}
