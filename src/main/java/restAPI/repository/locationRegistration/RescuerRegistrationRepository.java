package restAPI.repository.locationRegistration;

import org.springframework.data.jpa.repository.JpaRepository;
import restAPI.models.locationRegistration.AuthorityLocationRegistration;
import restAPI.models.locationRegistration.RescuerLocationRegistration;

import java.util.List;
import java.util.Optional;

public interface RescuerRegistrationRepository extends JpaRepository<RescuerLocationRegistration, String> {
    public Optional<RescuerLocationRegistration> findByUsername(String username);
    public List<RescuerLocationRegistration> findAllByLocationId(String locationId);
    public boolean existsByUsername(String username);
}
