package restAPI.repository;

import java.util.List;
import java.util.Optional;

import org.apache.catalina.User;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import restAPI.models.UserInfo;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, Long> {

	@Query("select t1 from UserInfo t1 INNER JOIN FETCH t1.roles inner join RoleUser t2 on t1.username = t2.username ")
	List<UserInfo> findAllRoleUser();

	@Query("select t1 from UserInfo t1 INNER JOIN FETCH t1.roles where t1.username=:username")
	Optional<UserInfo> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

	@Override
	@EntityGraph(attributePaths = "roles")
	List<UserInfo> findAll();
}
