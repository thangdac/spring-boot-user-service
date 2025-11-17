package user_service.com.example.user_service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import user_service.com.example.user_service.service.AuthenticationService;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner cleanupTokens(AuthenticationService token) {
		return args -> {
			token.cleanupInvalidatedTokens();
		};
	}

}
