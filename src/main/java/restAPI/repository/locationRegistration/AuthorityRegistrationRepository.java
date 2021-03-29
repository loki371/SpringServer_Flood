package restAPI.repository.locationRegistration;

import org.springframework.data.jpa.repository.JpaRepository;
import restAPI.models.locationRegistration.AuthorityLocationRegistration;

public interface AuthorityRegistrationRepository extends JpaRepository<AuthorityLocationRegistration, String> {
}
