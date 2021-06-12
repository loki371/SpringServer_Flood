package restAPI.repository.registration;

import org.springframework.data.jpa.repository.JpaRepository;
import restAPI.models.UserInfo;
import restAPI.models.location.Ward;
import restAPI.models.registration.Registration;
import restAPI.models.registration.Viewer;

import java.util.List;
import java.util.Optional;

public interface ViewerRepository extends JpaRepository<Viewer, Long> {
    public Optional<Viewer> findByUsername(String username);
}