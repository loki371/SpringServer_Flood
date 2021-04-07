package restAPI.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import restAPI.models.UserInfo;
import restAPI.models.registration.Registration;
import restAPI.payload.RegistrationPayload;
import restAPI.payload.SimplePayload;
import restAPI.security.services.UserDetailsImpl;
import restAPI.services.RegistrationService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/v1/api/registrations")
public class RegistrationController {

    @Autowired
    RegistrationService registrationService;

    @PostMapping("/users")
    public ResponseEntity<?> addUserRegistration(@RequestBody RegistrationPayload request, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (!registrationService.checkValidRegistrationRequest(request))
            return ResponseEntity.badRequest().body(new SimplePayload("registration request is not valid", request));

        UserInfo userInfo = userDetails.getUserInfo();
        Registration registration = registrationService.createNewRegistration(request, userInfo);

        return ResponseEntity.ok().body(new SimplePayload(registration));
    }

    @GetMapping
    public ResponseEntity<?> getAllRegistrations() {
        List<Registration> registrationList = registrationService.getAllRegistrations();
        return ResponseEntity.ok().body(new SimplePayload(registrationList));
    }
}
