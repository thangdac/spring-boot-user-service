package user_service.com.example.user_service.controller;

import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import user_service.com.example.user_service.dto.request.APIResponse;
import user_service.com.example.user_service.dto.request.AuthenticationRequest;
import user_service.com.example.user_service.dto.request.IntrospectRequest;
import user_service.com.example.user_service.dto.response.AuthenticationResponse;
import user_service.com.example.user_service.dto.response.IntrospectResponse;
import user_service.com.example.user_service.service.AuthenticationService;

import java.text.ParseException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    APIResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {

        var resul = authenticationService.authenticate(request);

        return APIResponse.<AuthenticationResponse>builder()
                .code(200)
                .result(resul)
                .build();
    }

    @PostMapping("/introspect")
    APIResponse<IntrospectResponse> login(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {

        var resul = authenticationService.introspect(request);

        return APIResponse.<IntrospectResponse>builder()
                .code(200)
                .result(resul)
                .build();
    }


}
