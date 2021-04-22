package restAPI.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/AtLocation")
    @PreAuthorize("hasRole('AUTHORITY') or hasRole('RESCUER') or hasRole('VOLUNTEER')")
    public ResponseEntity<?> getAllRegistrations(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<Registration> registrationList = registrationService.getLocationRegistrations(userDetails.getUsername());
        return ResponseEntity.ok().body(new SimplePayload(registrationList));
    }

    @GetMapping("/MyRegistrations")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getMyRegistrations(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<Registration> registrationList = registrationService.getMyRegistrations(userDetails.getUsername());
        return ResponseEntity.ok().body(new SimplePayload(registrationList));
    }
}
