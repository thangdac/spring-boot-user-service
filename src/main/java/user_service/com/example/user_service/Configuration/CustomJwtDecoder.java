package user_service.com.example.user_service.Configuration;

import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import user_service.com.example.user_service.repository.InvalidatedTokenRepository;

import javax.crypto.spec.SecretKeySpec;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    private InvalidatedTokenRepository invalidatedTokenRepository;

    private NimbusJwtDecoder jwtDecoder = null;


    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            // Parse token k verify để lấy jti
            SignedJWT signedJWT = SignedJWT.parse(token);
            String jti = signedJWT.getJWTClaimsSet().getJWTID();

            // CHECK BLACKLIST
            if (invalidatedTokenRepository.existsById(jti)) {
                throw new JwtException("Token is blacklisted");
            }
        }catch (Exception e) {
            throw new JwtException("Invalid token: " + e.getMessage());
        }

        // Tạo decoder nếu chưa tạo
        if (jwtDecoder == null) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HS512");

            jwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }
        // Decode chính xác
        return jwtDecoder.decode(token);
    }

}
