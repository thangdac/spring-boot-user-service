package user_service.com.example.user_service.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import user_service.com.example.user_service.dto.request.PermissionRequest;
import user_service.com.example.user_service.dto.response.PermissionResponse;
import user_service.com.example.user_service.entity.Permission;
import user_service.com.example.user_service.mapper.PermissionMapper;
import user_service.com.example.user_service.repository.PermissionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create (PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);

        permission = permissionRepository.save(permission);

        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll() {
        List<Permission> permissions = permissionRepository.findAll();

        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void delete (String permission) {
        permissionRepository.deleteById(permission);
    }

}
