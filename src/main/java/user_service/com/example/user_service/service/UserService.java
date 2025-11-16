package user_service.com.example.user_service.service;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import user_service.com.example.user_service.dto.request.UserCreationRequest;
import user_service.com.example.user_service.dto.request.UserPasswordUpdateRequest;
import user_service.com.example.user_service.dto.request.UserUpdateRequest;
import user_service.com.example.user_service.dto.response.UserResponse;
import user_service.com.example.user_service.entity.User;
import user_service.com.example.user_service.exception.ErrorCode;
import user_service.com.example.user_service.exception.ErrorCodeException;
import user_service.com.example.user_service.mapper.UserMapper;
import user_service.com.example.user_service.repository.RoleRepository;
import user_service.com.example.user_service.repository.UserRepository;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    @PreAuthorize("hasRole('admin')")
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    public UserResponse getUserById(String id) {
        return userMapper.toUserResponse(
                    userRepository.findById(id)
                        .orElseThrow(() -> new ErrorCodeException(ErrorCode.USER_NOT_FOUND)));
    }


    public UserResponse getUserByName(String name) {
        return userMapper.toUserResponse(
                userRepository.findByName(name)
                        .orElseThrow(() -> new ErrorCodeException(ErrorCode.USER_NOT_FOUND)));
    }

    public UserResponse createUser(UserCreationRequest request) {

        if (userRepository.existsByName(request.getName())) {
            throw new ErrorCodeException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse updateUser(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ErrorCodeException(ErrorCode.USER_NOT_FOUND));

       userMapper.UpdateUser(user, request);

       var roles = roleRepository.findAllById(request.getRoles());

       user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse updateUserPassword(String id, UserPasswordUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ErrorCodeException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new ErrorCodeException(ErrorCode.INCORRECT_PASSWORD);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public String deleteUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ErrorCodeException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);

        return "User with name '" + user.getName() + "' has been deleted successfully.";
    }
}
