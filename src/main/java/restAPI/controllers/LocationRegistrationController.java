package restAPI.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import restAPI.Constants;
import restAPI.models.role.ERole;
import restAPI.payload.SimplePayload;
import restAPI.repository.location.DistrictRepository;
import restAPI.repository.location.ProvinceRepository;
import restAPI.repository.location.WardRepository;
import restAPI.security.services.UserDetailsImpl;
import restAPI.services.LocationRegistrationService;
import restAPI.services.UserInfoService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/v1/api/locationRegistrations")
public class LocationRegistrationController {

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    ProvinceRepository provinceRepository;

    @Autowired
    DistrictRepository districtRepository;

    @Autowired
    WardRepository wardRepository;

    @Autowired
    LocationRegistrationService locationRegistrationService;

    @PostMapping("/provinces/{provinceId}")
    @PreAuthorize("hasRole('AUTHORITY') or hasRole('VOLUNTEER') or hasRole('RESCUER')")
    public ResponseEntity<?> createNewProvinceRegistration(
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

    @PostMapping("/districts/{districtId}")
    @PreAuthorize("hasRole('AUTHORITY') or hasRole('VOLUNTEER') or hasRole('RESCUER')")
    public ResponseEntity<?> createNewDistrictRegistration(
            @PathVariable String districtId,
            @RequestParam("eRole") ERole eRole,
            Authentication authentication
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (!districtRepository.existsById(districtId))
            return ResponseEntity.notFound().build();

        if (!userInfoService.hasERoleInUserInfo(userDetails.getUserInfo(), eRole))
            return ResponseEntity.badRequest().body(new SimplePayload("this user do not has role " + eRole.name()));

        boolean result = locationRegistrationService.addRegistrationToLocation(
                userDetails.getUsername(),
                districtId,
                Constants.DISTRICT_TYPE,
                eRole);

        if (!result)
            return ResponseEntity.badRequest().body(
                    new SimplePayload("you had another request in queue," +
                            "please delete it before create new one!"));

        return ResponseEntity.ok().body(new SimplePayload("created!"));
    }

    @PostMapping("/wards/{wardId}")
    @PreAuthorize("hasRole('AUTHORITY') or hasRole('VOLUNTEER') or hasRole('RESCUER')")
    public ResponseEntity<?> createNewWardRegistration(
            @PathVariable String wardId,
            @RequestParam("eRole") ERole eRole,
            Authentication authentication
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (!wardRepository.existsById(wardId))
            return ResponseEntity.notFound().build();

        if (!userInfoService.hasERoleInUserInfo(userDetails.getUserInfo(), eRole))
            return ResponseEntity.badRequest().body(new SimplePayload("this user do not has role " + eRole.name()));

        boolean result = locationRegistrationService.addRegistrationToLocation(
                userDetails.getUsername(),
                wardId,
                Constants.WARD_TYPE,
                eRole);

        if (!result)
            return ResponseEntity.badRequest().body(
                    new SimplePayload("you had another request in queue," +
                            "please delete it before create new one!"));

        return ResponseEntity.ok().body(new SimplePayload("created!"));
    }

    @DeleteMapping("/wards/{wardId}")
    @PreAuthorize("hasRole('AUTHORITY') or hasRole('VOLUNTEER') or hasRole('RESCUER')")
    public ResponseEntity<?> deleteWardRegistration(
            @PathVariable String wardId,
            @RequestParam("eRole") ERole eRole,
            Authentication authentication
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (!wardRepository.existsById(wardId))
            return ResponseEntity.notFound().build();

        if (!userInfoService.hasERoleInUserInfo(userDetails.getUserInfo(), eRole))
            return ResponseEntity.badRequest().body(new SimplePayload("this user do not has role " + eRole.name()));

        boolean result = locationRegistrationService.deleteRegistrationOfLocation(
                userDetails.getUsername(),
                wardId,
                Constants.WARD_TYPE,
                eRole);

        if (!result)
            return ResponseEntity.badRequest().body(
                    new SimplePayload("registration do not exists!"));

        return ResponseEntity.ok().body(new SimplePayload("deleted!"));
    }

    @GetMapping("/authorities")
    @PreAuthorize("hasRole('AUTHORITY')")
    public ResponseEntity<?> getAllLocationRegistrationForAuthority(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<Object> list = locationRegistrationService.getListAllLocation(ERole.ROLE_AUTHORITY, userDetails.getUsername());
        return ResponseEntity.ok().body(new SimplePayload(list));
    }

    @GetMapping("/volunteers")
    @PreAuthorize("hasRole('VOLUNTEER')")
    public ResponseEntity<?> getAllLocationRegistrationForVolunteer(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<Object> list = locationRegistrationService.getListAllLocation(ERole.ROLE_VOLUNTEER, userDetails.getUsername());
        return ResponseEntity.ok().body(new SimplePayload(list));
    }

    @GetMapping("/rescuers")
    @PreAuthorize("hasRole('RESCUER')")
    public ResponseEntity<?> getAllLocationRegistrationForRescuer(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<Object> list = locationRegistrationService.getListAllLocation(ERole.ROLE_RESCUER, userDetails.getUsername());
        return ResponseEntity.ok().body(new SimplePayload(list));
    }

    @PostMapping("/authorities/{username}")
    @PreAuthorize("hasRole('AUTHORITY')")
    public ResponseEntity<?> processAuthorityRequest(@PathVariable String username, @RequestParam boolean accept,
                                                     Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        boolean checkingExistsRegistration = locationRegistrationService.checkExistsRequestOfUsername(username, ERole.ROLE_AUTHORITY);
        if (!checkingExistsRegistration)
            return ResponseEntity.notFound().build();

        boolean result = locationRegistrationService.processAuthorityRegistration(userDetails.getUsername(), username, accept);

        if (!result)
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok().build();
    }

    @PostMapping("/rescuers/{username}")
    @PreAuthorize("hasRole('AUTHORITY')")
    public ResponseEntity<?> processRescuerRequest(@PathVariable String username, @RequestParam boolean accept,
                                                   Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        boolean checkingExistsRegistration = locationRegistrationService.checkExistsRequestOfUsername(username, ERole.ROLE_RESCUER);
        if (!checkingExistsRegistration)
            return ResponseEntity.notFound().build();

        locationRegistrationService.processRescuerRegistration(userDetails.getUsername(), username, accept);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/volunteers/{username}")
    @PreAuthorize("hasRole('AUTHORITY')")
    public ResponseEntity<?> processVolunteerRequest(@PathVariable String username, @RequestParam boolean accept,
                                                     Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        boolean checkingExistsRegistration = locationRegistrationService.checkExistsRequestOfUsername(username, ERole.ROLE_VOLUNTEER);
        if (!checkingExistsRegistration)
            return ResponseEntity.notFound().build();

        locationRegistrationService.processVolunteerRegistration(userDetails.getUsername(), username, accept);

        return ResponseEntity.ok().build();
    }
}
