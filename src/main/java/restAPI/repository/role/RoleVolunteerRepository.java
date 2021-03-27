package restAPI.repository.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import restAPI.models.role.RoleUser;
import restAPI.models.role.RoleVolunteer;

import java.util.List;
import java.util.Optional;

public interface RoleVolunteerRepository extends JpaRepository<RoleVolunteer, Long> {
    public void deleteByUsername(String username);

    Optional<RoleVolunteer> findByUsername(String username);

    Boolean existsByUsername(String username);

    @Override
    @Query("select t from RoleVolunteer t")
    List<RoleVolunteer> findAll();

    @Query("select t.username from RoleVolunteer t")
    List<String> findAllUsername();
}
