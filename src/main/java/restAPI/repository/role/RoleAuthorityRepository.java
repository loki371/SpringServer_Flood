package restAPI.repository.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import restAPI.models.role.RoleAuthority;
import restAPI.models.role.RoleUser;

import java.util.List;
import java.util.Optional;

public interface RoleAuthorityRepository extends JpaRepository<RoleAuthority, Long> {
    public void deleteByUsername(String username);

    Optional<RoleAuthority> findByUsername(String username);

    Boolean existsByUsername(String username);

    @Override
    @Query("select t from RoleAuthority t")
    List<RoleAuthority> findAll();

    @Query("select t.username from RoleAuthority t")
    List<String> findAllUsername();
}
