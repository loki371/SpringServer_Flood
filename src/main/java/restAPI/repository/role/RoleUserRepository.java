package restAPI.repository.role;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import restAPI.models.UserInfo;
import restAPI.models.role.Role;
import restAPI.models.role.RoleUser;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleUserRepository extends JpaRepository<RoleUser, Long> {
    void deleteByUsername(String username);

    Optional<RoleUser> findByUsername(String username);

    Boolean existsByUsername(String username);

    @Override
    @Query("select t from RoleUser t")
    List<RoleUser> findAll();

    @Query("select t.username from RoleUser t")
    List<String> findAllUsername();
}