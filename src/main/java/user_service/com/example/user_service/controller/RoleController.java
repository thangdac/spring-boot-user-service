package user_service.com.example.user_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import user_service.com.example.user_service.dto.request.APIResponse;
import user_service.com.example.user_service.dto.request.PermissionRequest;
import user_service.com.example.user_service.dto.request.RoleRequest;
import user_service.com.example.user_service.dto.response.PermissionResponse;
import user_service.com.example.user_service.dto.response.RoleResponse;
import user_service.com.example.user_service.entity.Role;
import user_service.com.example.user_service.service.PermissionService;
import user_service.com.example.user_service.service.RoleService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {

    RoleService roleService;

    @PostMapping
    public APIResponse<RoleResponse> createRole(@RequestBody RoleRequest request) {
        return APIResponse.<RoleResponse>builder()
                .code(200)
                .result(roleService.create(request))
                .build();
    }

    @GetMapping
    public APIResponse<List<RoleResponse>> getAllRoles() {
        return APIResponse.<List<RoleResponse>>builder()
                .code(200)
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{role}")
    public APIResponse<Void> deleteRole(@PathVariable String role) {
        roleService.delete(role);
        return APIResponse.<Void>builder()
                .code(200)
                .build();
    }

}
