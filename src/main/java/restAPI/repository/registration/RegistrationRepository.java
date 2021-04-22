package restAPI.repository.registration;

import org.springframework.data.jpa.repository.JpaRepository;
import restAPI.models.UserInfo;
import restAPI.models.location.Ward;
import restAPI.models.registration.Registration;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    public List<Registration> findAllByWard(Ward ward);
    public List<Registration> findAllByCreateBy(UserInfo userInfo);
}
