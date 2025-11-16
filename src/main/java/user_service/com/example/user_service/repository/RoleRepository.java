package user_service.com.example.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import user_service.com.example.user_service.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}
