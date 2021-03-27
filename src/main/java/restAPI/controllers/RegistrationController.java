package restAPI.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import restAPI.models.UserInfo;
import restAPI.models.registration.Registration;
import restAPI.payload.RegistrationPayload;
import restAPI.payload.SimplePayload;
import restAPI.security.services.UserDetailsImpl;
import restAPI.services.RegistrationService;

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
}
