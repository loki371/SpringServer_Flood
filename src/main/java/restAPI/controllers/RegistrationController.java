package restAPI.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import restAPI.ErrorCode;
import restAPI.models.UserInfo;
import restAPI.models.registration.EState;
import restAPI.models.registration.Registration;
import restAPI.models.role.ERole;
import restAPI.object_function.I_ObjectFunction;
import restAPI.object_function.SetSavedByFunction;
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

    @PutMapping("/authorities/{registrationId}")
    @PreAuthorize("hasRole('AUTHORITY')")
    public ResponseEntity<?> authenticateRegistration(@PathVariable Long registrationId, Authentication authentication,
                                                      @RequestParam("oldState") EState oldState,
                                                      @RequestParam("newState") EState newState) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        switch (oldState) {
            case STATE_AUTHENTICATED:
            case STATE_UNAUTHENTICATED:
                break;
            default:
                return ResponseEntity.badRequest().body(new SimplePayload("eState is not valid for volunteer, must be STATE_AUTHENTICATED or STATE_UNAUTHENTICATED"));
        }

        switch (newState) {
            case STATE_AUTHENTICATED:
            case STATE_UNAUTHENTICATED:
                break;
            default:
                return ResponseEntity.badRequest().body(new SimplePayload("eState is not valid for volunteer, must be STATE_AUTHENTICATED or STATE_UNAUTHENTICATED"));
        }

        if (oldState == newState)
            return ResponseEntity.badRequest().body(new SimplePayload("oldState must be != newState"));

        ErrorCode errorCode = registrationService.applyNewEStateToRegistration(
                registrationId,
                userDetails.getUsername(),
                ERole.ROLE_AUTHORITY,
                oldState,
                newState,
                null);

        if (errorCode == ErrorCode.OK)
            return ResponseEntity.ok(new SimplePayload("updated"));
        else
            return ResponseEntity.badRequest().body(new SimplePayload(errorCode.toString()));    }

    @PutMapping("/volunteers/{registrationId}")
    @PreAuthorize("hasRole('VOLUNTEER')")
    public ResponseEntity<?> confirmRegistration(@PathVariable Long registrationId, Authentication authentication,
                                                 @RequestParam("oldState") EState oldState,
                                                 @RequestParam("newState") EState newState) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        switch (oldState) {
            case STATE_DANGER:
            case STATE_SAFE:
            case STATE_EMERGENCY:
                break;
            default:
                return ResponseEntity.badRequest().body(new SimplePayload("oldState is not valid for volunteer"));
        }

        switch (newState) {
            case STATE_DANGER:
            case STATE_SAFE:
            case STATE_EMERGENCY:
                break;
            default:
                return ResponseEntity.badRequest().body(new SimplePayload("newState is not valid for volunteer"));
        }

        if (oldState == newState)
            return ResponseEntity.badRequest().body(new SimplePayload("oldState must be != newState"));

        ErrorCode errorCode = registrationService.applyNewEStateToRegistration(
                registrationId,
                userDetails.getUsername(),
                ERole.ROLE_VOLUNTEER,
                oldState,
                newState,
                null);

        if (errorCode == ErrorCode.OK)
            return ResponseEntity.ok(new SimplePayload("updated"));
        else
            return ResponseEntity.badRequest().body(new SimplePayload(errorCode.toString()));
    }

    @PutMapping("/rescuers/{registrationId}")
    @PreAuthorize("hasRole('RESCUER')")
    public ResponseEntity<?> saveRegistration(@PathVariable Long registrationId, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ErrorCode errorCode = registrationService.applyNewEStateToRegistration(
                registrationId,
                userDetails.getUsername(),
                ERole.ROLE_RESCUER,
                EState.STATE_DANGER,
                EState.STATE_SAVED,
                new SetSavedByFunction());

        if (errorCode == ErrorCode.OK)
            return ResponseEntity.ok(new SimplePayload("updated"));
        else
            return ResponseEntity.badRequest().body(new SimplePayload(errorCode.toString()));
    }
}
