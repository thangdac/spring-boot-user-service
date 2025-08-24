package user_service.com.example.user_service.Configuration;


import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import user_service.com.example.user_service.Enums.Role;
import user_service.com.example.user_service.entity.User;
import user_service.com.example.user_service.repository.UserRepository;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {

            if (userRepository.findByName("admin").isEmpty()) {
                var roles = new HashSet<String>();
                roles.add(Role.ADMIN.name());

                User user = User.builder()
                        .name("admin")
                        .password(passwordEncoder.encode("123456"))
                        .roles(roles)
                        .build();

                userRepository.save(user);
            }
        };
    };
}
