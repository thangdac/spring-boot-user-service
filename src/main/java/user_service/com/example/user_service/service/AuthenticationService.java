package user_service.com.example.user_service.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import user_service.com.example.user_service.dto.request.AuthenticationRequest;
import user_service.com.example.user_service.dto.request.IntrospectRequest;
import user_service.com.example.user_service.dto.request.LogoutRequest;
import user_service.com.example.user_service.dto.response.AuthenticationResponse;
import user_service.com.example.user_service.dto.response.IntrospectResponse;
import user_service.com.example.user_service.entity.InvalidatedToken;
import user_service.com.example.user_service.entity.User;
import user_service.com.example.user_service.exception.ErrorCode;
import user_service.com.example.user_service.exception.ErrorCodeException;
import user_service.com.example.user_service.repository.InvalidatedTokenRepository;
import user_service.com.example.user_service.repository.UserRepository;

import java.text.ParseException;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    InvalidatedTokenRepository invalidatedTokenRepository;
    @NonFinal
    @Value("${jwt.secret}")
    private String SIGNER_KEY;


    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        var user = userRepository.findByName(request.getName())
                .orElseThrow(() -> new ErrorCodeException(ErrorCode.USER_NOT_FOUND));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new ErrorCodeException(ErrorCode.UNAUTHENTICATED);
        }

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    private String generateToken(User user) {

        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getName())
                .issuer("user-service")
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + 3600 * 1000))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", BuildScope(user))
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Error generating token", e);
            throw new RuntimeException(e);
        }
    }

    private String BuildScope(User user){

        StringJoiner joiner = new StringJoiner(" ");

        if(!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(
                    role -> {
                        joiner.add("ROLE_" + role.getName());
                        if(!CollectionUtils.isEmpty(role.getPermissions()))
                            role.getPermissions().forEach(
                                permission -> {
                                    joiner.add(permission.getName());
                                }
                            );
                        });

        return joiner.toString();
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {

        var token = request.getToken();

        Boolean valid = true;

        try {
            verifyToken(token);
        } catch (ErrorCodeException e) {
            valid = false;
        }
        return IntrospectResponse.builder()
                .valid(valid)
                .build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var token = verifyToken(request.getToken());

        String jti = token.getJWTClaimsSet().getJWTID();
        Date expiryTime = token.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jti)
                .expiryTime(expiryTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);
    }

    private SignedJWT verifyToken(String token) throws ParseException, JOSEException {
        JWSVerifier verifier =  new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if(!verified && expiryTime.after(new Date())){
            throw new ErrorCodeException(ErrorCode.UNAUTHENTICATED);
        }

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new ErrorCodeException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    @Transactional
    public void cleanupInvalidatedTokens() {
        Date oneDay = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        invalidatedTokenRepository.deleteByExpiryTimeBefore(oneDay);
    }
}
