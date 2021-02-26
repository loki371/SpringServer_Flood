package restAPI.repository.role;

import org.springframework.data.jpa.repository.JpaRepository;
import restAPI.models.role.RoleAuthority;

public interface RoleAuthorityRepository extends JpaRepository<RoleAuthority, Long> {
    public void deleteByUsername(String username);
}
