package user_service.com.example.user_service.service;

import lombok.RequiredArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import user_service.com.example.user_service.dto.request.RoleRequest;
import user_service.com.example.user_service.dto.response.RoleResponse;
import user_service.com.example.user_service.entity.Role;
import user_service.com.example.user_service.mapper.RoleMapper;
import user_service.com.example.user_service.repository.PermissionRepository;
import user_service.com.example.user_service.repository.RoleRepository;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {

    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);

         var permissions =  permissionRepository.findAllById(request.getPermissions());
            role.setPermissions(new HashSet<>(permissions));

         role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    public void delete(String role) {
        roleRepository.deleteById(role);
    }


}
