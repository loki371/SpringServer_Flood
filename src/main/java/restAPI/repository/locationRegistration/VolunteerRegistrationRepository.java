package restAPI.repository.locationRegistration;

import org.springframework.data.jpa.repository.JpaRepository;
import restAPI.models.locationRegistration.VolunteerLocationRegistration;

public interface VolunteerRegistrationRepository extends JpaRepository<VolunteerLocationRegistration, String> {
}
