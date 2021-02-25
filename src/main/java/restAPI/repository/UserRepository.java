package restAPI.repository;

import java.util.Optional;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import restAPI.models.UserInfo;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, Long> {
	Optional<UserInfo> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
}
