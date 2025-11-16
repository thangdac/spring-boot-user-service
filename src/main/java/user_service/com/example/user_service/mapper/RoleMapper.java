package user_service.com.example.user_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import user_service.com.example.user_service.dto.request.RoleRequest;
import user_service.com.example.user_service.dto.response.RoleResponse;
import user_service.com.example.user_service.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

     RoleResponse toRoleResponse(Role role);
}
