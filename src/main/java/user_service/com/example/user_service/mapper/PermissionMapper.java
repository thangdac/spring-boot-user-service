package user_service.com.example.user_service.mapper;

import org.mapstruct.Mapper;
import user_service.com.example.user_service.dto.request.PermissionRequest;
import user_service.com.example.user_service.dto.response.PermissionResponse;
import user_service.com.example.user_service.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);

}
