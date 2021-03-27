package restAPI.repository.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import restAPI.models.role.ERole;
import restAPI.models.role.Role;
import restAPI.models.role.RoleRescuer;
import restAPI.models.role.RoleUser;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRescuerRepository extends JpaRepository<RoleRescuer, Long> {
    void deleteByUsername(String username);

    Optional<RoleRescuer> findByUsername(String username);

    Boolean existsByUsername(String username);

    @Override
    @Query("select t from RoleRescuer t")
    List<RoleRescuer> findAll();

    @Query("select t.username from RoleRescuer t")
    List<String> findAllUsername();
}