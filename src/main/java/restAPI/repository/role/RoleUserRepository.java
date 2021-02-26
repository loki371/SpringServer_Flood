package restAPI.repository.role;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import restAPI.models.role.Role;
import restAPI.models.role.RoleUser;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleUserRepository extends JpaRepository<RoleUser, Long> {
    public void deleteByUsername(String username);
}