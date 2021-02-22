package restAPI.repository.registration;

import org.springframework.data.jpa.repository.JpaRepository;
import restAPI.models.registration.Registration;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
}
