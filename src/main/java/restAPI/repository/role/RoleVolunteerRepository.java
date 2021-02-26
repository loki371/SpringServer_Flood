package restAPI.repository.role;

import org.springframework.data.jpa.repository.JpaRepository;
import restAPI.models.role.RoleVolunteer;

public interface RoleVolunteerRepository extends JpaRepository<RoleVolunteer, Long> {
    public void deleteByUsername(String username);
}
