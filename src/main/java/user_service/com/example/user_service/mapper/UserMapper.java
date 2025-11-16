package user_service.com.example.user_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import user_service.com.example.user_service.dto.request.UserCreationRequest;
import user_service.com.example.user_service.dto.request.UserUpdateRequest;
import user_service.com.example.user_service.dto.response.UserResponse;
import user_service.com.example.user_service.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void UpdateUser(@MappingTarget  User user, UserUpdateRequest request );
}
