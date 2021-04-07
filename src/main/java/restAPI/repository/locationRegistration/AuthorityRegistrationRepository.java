package restAPI.repository.locationRegistration;

import org.springframework.data.jpa.repository.JpaRepository;
import restAPI.models.locationRegistration.AuthorityLocationRegistration;

import java.util.List;
import java.util.Optional;

public interface AuthorityRegistrationRepository extends JpaRepository<AuthorityLocationRegistration, String> {
    public Optional<AuthorityLocationRegistration> findByUsername(String username);

    public boolean existsByUsername(String username);
}
