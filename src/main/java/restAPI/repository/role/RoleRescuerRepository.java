package restAPI.repository.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import restAPI.models.role.ERole;
import restAPI.models.role.Role;
import restAPI.models.role.RoleRescuer;

import java.util.Optional;

@Repository
public interface RoleRescuerRepository extends JpaRepository<RoleRescuer, Long> {
    public void deleteByUsername(String username);
}