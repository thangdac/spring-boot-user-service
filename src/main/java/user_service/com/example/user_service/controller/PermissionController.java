package user_service.com.example.user_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import user_service.com.example.user_service.dto.request.APIResponse;
import user_service.com.example.user_service.dto.request.PermissionRequest;
import user_service.com.example.user_service.dto.response.PermissionResponse;
import user_service.com.example.user_service.service.PermissionService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {

    PermissionService permissionService;

    @PostMapping
    APIResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request) {
        return APIResponse.<PermissionResponse>builder()
                .code(200)
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping
    APIResponse<List<PermissionResponse>> getAllPermissions() {
        return APIResponse.<List<PermissionResponse>>builder()
                .code(200)
                .result(permissionService.getAll())
                .build();
    }

    @DeleteMapping("/{permission}")
    APIResponse<Void> deletePermission(@PathVariable String permission) {
        permissionService.delete(permission);
        return APIResponse.<Void>builder()
                .code(200)
                .build();
    }

}
