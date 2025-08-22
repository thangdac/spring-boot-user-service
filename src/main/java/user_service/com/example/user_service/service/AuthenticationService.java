package user_service.com.example.user_service.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import user_service.com.example.user_service.dto.request.AuthenticationRequest;
import user_service.com.example.user_service.dto.request.IntrospectRequest;
import user_service.com.example.user_service.dto.response.AuthenticationResponse;
import user_service.com.example.user_service.dto.response.IntrospectResponse;
import user_service.com.example.user_service.exception.ErrorCode;
import user_service.com.example.user_service.exception.ErrorCodeException;
import user_service.com.example.user_service.repository.UserRepository;

import java.text.ParseException;
import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    UserRepository userRepository;

    @NonFinal
    @Value("${jwt.secret}")
    private String SIGNER_KEY;


    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        var user = userRepository.findByName(request.getName())
                .orElseThrow(() -> new ErrorCodeException(ErrorCode.USER_NOT_FOUND));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new ErrorCodeException(ErrorCode.UNAUTHENTICATED);
        }

        var token = generateToken(user.getName());

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    private String generateToken(String name) {

        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(name)
                .issuer("user-service")
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + 3600 * 1000)) // 1 hour expiration
                .claim("role", "USER") //
                .build();

        Payload paload = new Payload(claimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, paload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Error generating token", e);
            throw new RuntimeException(e);
        }
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {

        var token = request.getToken();
        if (token == null || token.isEmpty()) {
            throw new ErrorCodeException(ErrorCode.UNAUTHENTICATED);
        }

        JWSVerifier verifier =  new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .valid(verified && expirationTime != null && expirationTime.after(new Date()))
                .build();
    }

}
