package restAPI.repository.locationRegistration;

import org.springframework.data.jpa.repository.JpaRepository;
import restAPI.models.locationRegistration.RescuerLocationRegistration;

public interface RescuerRegistrationRepository extends JpaRepository<RescuerLocationRegistration, String> {
}
