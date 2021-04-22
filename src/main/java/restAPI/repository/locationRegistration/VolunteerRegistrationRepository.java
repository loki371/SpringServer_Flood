package restAPI.repository.locationRegistration;

import org.springframework.data.jpa.repository.JpaRepository;
import restAPI.models.locationRegistration.AuthorityLocationRegistration;
import restAPI.models.locationRegistration.VolunteerLocationRegistration;

import java.util.List;
import java.util.Optional;

public interface VolunteerRegistrationRepository extends JpaRepository<VolunteerLocationRegistration, String> {
    public Optional<VolunteerLocationRegistration> findByUsername(String username);
    public List<VolunteerLocationRegistration> findAllByLocationId(String locationId);
    public boolean existsByUsername(String username);
}
