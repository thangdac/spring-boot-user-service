package user_service.com.example.user_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import user_service.com.example.user_service.dto.request.APIResponse;
import user_service.com.example.user_service.dto.request.UserCreationRequest;
import user_service.com.example.user_service.dto.request.UserUpdateRequest;
import user_service.com.example.user_service.dto.response.UserResponse;
import user_service.com.example.user_service.entity.User;
import user_service.com.example.user_service.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @GetMapping
    APIResponse<List<User>> getAllUsers() {

        APIResponse<List<User>> response = new APIResponse<>();

        response.setCode(200);
        response.setResult(userService.getAllUsers());

        return response;
    }
    @GetMapping("/{id}")
    APIResponse<UserResponse> getUserById(@PathVariable String id) {

        APIResponse<UserResponse> response = new APIResponse<>();
        response.setCode(200);
        response.setResult(userService.getUserById(id));

        return response;
    }

    @PostMapping
    APIResponse<User> createUser(@RequestBody @Valid UserCreationRequest request) {
        APIResponse<User> response = new APIResponse<>();

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

    @DeleteMapping("/{id}")
    APIResponse<User> deleteUser(@PathVariable String id) {

        APIResponse<User> response = new APIResponse<>();

        String message = userService.deleteUser(id);

        response.setCode(200);
        response.setMessage(message);

        return response;
    }
}
