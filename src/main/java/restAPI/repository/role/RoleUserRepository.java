package restAPI.repository.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import restAPI.models.role.Role;

import java.util.Optional;

@Repository
public interface RoleUserRepository extends JpaRepository<Role, Long> {
}