package restAPI.controllers;

import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import restAPI.Constants;
import restAPI.models.locationRegistration.AuthorityLocationRegistration;
import restAPI.models.role.ERole;
import restAPI.payload.SimplePayload;
import restAPI.repository.location.ProvinceRepository;
import restAPI.security.services.UserDetailsImpl;
import restAPI.services.LocationRegistrationService;
import restAPI.services.UserInfoService;

import java.util.List;

@RestController
@RequestMapping("/v1/api/locationRegistrations")
public class LocationRegistrationController {

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    ProvinceRepository provinceRepository;

    @Autowired
    LocationRegistrationService locationRegistrationService;

    @PostMapping("/provinces/{provinceId}")
    @PreAuthorize("hasRole('AUTHORITY') or hasRole('VOLUNTEER') or hasRole('RESCUER')")
    public ResponseEntity<?> createNewLocationRegistration(
            @PathVariable String provinceId,
            @RequestParam("eRole") ERole eRole,
            Authentication authentication
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (!provinceRepository.existsById(provinceId))
            return ResponseEntity.notFound().build();

        if (!userInfoService.hasERoleInUserInfo(userDetails.getUserInfo(), eRole))
            return ResponseEntity.badRequest().body(new SimplePayload("this user do not has role " + eRole.name()));

        boolean result = locationRegistrationService.addRegistrationToLocation(
                userDetails.getUsername(),
                provinceId,
                Constants.PROVINCE_TYPE,
                eRole);

        if (!result)
            return ResponseEntity.badRequest().body(
                    new SimplePayload("you had another request in queue," +
                            "please delete it before create new one!"));

        return ResponseEntity.ok().body(new SimplePayload("created!"));
    }


    @GetMapping("/authorities")
    @PreAuthorize("hasRole('AUTHORITY')")
    public ResponseEntity<?> getAllLocationRegistrationForAuthority() {
        List<Object> list = locationRegistrationService.getListAllLocation(ERole.ROLE_AUTHORITY);
        return ResponseEntity.ok().body(new SimplePayload(list));
    }

    @GetMapping("/volunteers")
    @PreAuthorize("hasRole('VOLUNTEER')")
    public ResponseEntity<?> getAllLocationRegistrationForVolunteer() {
        List<Object> list = locationRegistrationService.getListAllLocation(ERole.ROLE_VOLUNTEER);
        return ResponseEntity.ok().body(new SimplePayload(list));
    }

    @GetMapping("/rescuers")
    @PreAuthorize("hasRole('RESCUER')")
    public ResponseEntity<?> getAllLocationRegistrationForRescuer() {
        List<Object> list = locationRegistrationService.getListAllLocation(ERole.ROLE_RESCUER);
        return ResponseEntity.ok().body(new SimplePayload(list));
    }
}
