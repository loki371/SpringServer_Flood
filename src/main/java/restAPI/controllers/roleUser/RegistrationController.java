package restAPI.controllers.roleUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import restAPI.repository.UserRepository;
import restAPI.repository.registration.RegistrationRepository;

@RestController
@RequestMapping("/api/user/registration")
@PreAuthorize("hasRole('USER')")
public class RegistrationController {
    @Autowired
    RegistrationRepository regisRepository;

    @GetMapping
    public ResponseEntity<?> getListRegistration() {
        return null;
    }

}
