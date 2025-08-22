package user_service.com.example.user_service.repository;
import org.springframework.stereotype.Repository;
import user_service.com.example.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByName(String name);
    Optional<User> findByName(String name);
}
