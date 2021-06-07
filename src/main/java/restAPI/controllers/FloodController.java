package restAPI.controllers;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import restAPI.buckets.RegistrationBucket;
import restAPI.grab.FloodWardService;
import restAPI.grab.flood_ward.entity.FloodRegistration;
import restAPI.grab.flood_ward.entity.Location;
import restAPI.models.location.Ward;
import restAPI.models.registration.Registration;
import restAPI.models.role.RoleRescuer;
import restAPI.payload.SimplePayload;
import restAPI.repository.role.RoleRescuerRepository;
import restAPI.security.services.UserDetailsImpl;
import restAPI.services.RegistrationService;

import java.util.LinkedList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/v1/api/rescuerSaving")
public class FloodController {

    @Autowired
    FloodWardService floodWardService;

    @Autowired
    RoleRescuerRepository roleRescuerRepository;

    @GetMapping("/destinations")
    @PreAuthorize("hasRole('RESCUER')")
    public ResponseEntity<?> getListDestination(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        RoleRescuer roleRescuer = roleRescuerRepository.findByUsername(userDetails.getUsername()).get();
        if (roleRescuer.getWard() == null)
            return ResponseEntity.badRequest().body(new SimplePayload("you must sign up to 1 ward"));

        if(!floodWardService.checkInFlood(roleRescuer.getWard().getId()))
            return ResponseEntity.badRequest().body(new SimplePayload("this ward do not have flood"));

        if (!floodWardService.checkRescuerStarted(userDetails.getUsername(), roleRescuer.getWard().getId()))
            return ResponseEntity.badRequest().body(new SimplePayload("rescuer must start before get destinations"));

        List<FloodRegistration> floodRegistrationList =
                floodWardService.getFloodRegistrationOfRescuer(
                        roleRescuer.getWard().getId(),
                        userDetails.getUsername());

        List<RegistrationBucket> resultList = new LinkedList<>();
        for (FloodRegistration item : floodRegistrationList) {
            Registration item1 = item.getRegistration();
            RegistrationBucket bucket = new RegistrationBucket();
            bucket.id = item1.getId();
            bucket.eState = item1.getEState();
            bucket.latitude = item1.getLatitude();
            bucket.longitude = item1.getLongitude();
            bucket.name = item1.getName();
            bucket.phone = item1.getPhone();
            bucket.numPerson = item1.getNumPerson();
            bucket.ward = item1.getWard();
            resultList.add(bucket);
        }
        return ResponseEntity.ok(new SimplePayload(resultList));
    }

    @PostMapping("/GPS")
    @PreAuthorize("hasRole('RESCUER')")
    public ResponseEntity<?> setGPS(Authentication authentication,
                                    @RequestParam float longitude,
                                    @RequestParam float latitude) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        RoleRescuer roleRescuer = roleRescuerRepository.findByUsername(userDetails.getUsername()).get();
        if (roleRescuer.getWard() == null)
            return ResponseEntity.badRequest().body(new SimplePayload("you do not have any ward"));

        Ward ward = roleRescuer.getWard();
        if (!floodWardService.checkInFlood(ward.getId()))
            return ResponseEntity.badRequest().body(new SimplePayload("this location do not have flood"));

        if (!floodWardService.checkRescuerStarted(userDetails.getUsername(), roleRescuer.getWard().getId()))
            return ResponseEntity.badRequest().body(new SimplePayload("rescuer must start before get destinations"));

        Location location = new Location(longitude, latitude);
        floodWardService.setGPSForRescuer(userDetails.getUsername(), location, ward.getId());

        return ResponseEntity.ok().body(new SimplePayload("ok"));
    }

    @PostMapping("/saveDestinations/{regisId}")
    @PreAuthorize("hasRole('RESCUER')")
    public ResponseEntity<?> saveDestination(Authentication authentication,
                                             @PathVariable Long regisId,
                                             @RequestParam int numPeople) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        RoleRescuer roleRescuer = roleRescuerRepository.findByUsername(userDetails.getUsername()).get();
        if (roleRescuer.getWard() == null)
            return ResponseEntity.badRequest().body(new SimplePayload("you do not have any ward"));

        Ward ward = roleRescuer.getWard();
        if (!floodWardService.checkInFlood(ward.getId()))
            return ResponseEntity.badRequest().body(new SimplePayload("this location do not have flood"));

        if (!floodWardService.checkRescuerStarted(userDetails.getUsername(), roleRescuer.getWard().getId()))
            return ResponseEntity.badRequest().body(new SimplePayload("rescuer must start before get destinations"));

        if (!floodWardService.checkContainValidRegis(ward.getId(), regisId))
            return ResponseEntity.badRequest().body(new SimplePayload("this flood ward do not contain this regisId"));

        boolean result = floodWardService.saveDestination(userDetails.getUsername(), ward.getId(), regisId);

        if (!result)
            return ResponseEntity.badRequest().body(new SimplePayload("this rescuer do not contain regis or num people in rescuer's board is maximum"));

        return ResponseEntity.ok(new SimplePayload("ok"));
    }

    @PostMapping("/peopleGetOut")
    @PreAuthorize("hasRole('RESCUER')")
    public ResponseEntity<?> setPeopleGetOut(@RequestParam int numPeople,
                                             Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        RoleRescuer roleRescuer = roleRescuerRepository.findByUsername(userDetails.getUsername()).get();
        if (roleRescuer.getWard() == null)
            return ResponseEntity.badRequest().body(new SimplePayload("you do not have any ward"));

        Ward ward = roleRescuer.getWard();
        if (!floodWardService.checkInFlood(ward.getId()))
            return ResponseEntity.badRequest().body(new SimplePayload("this location do not have flood"));

        if (!floodWardService.checkRescuerStarted(userDetails.getUsername(), roleRescuer.getWard().getId()))
            return ResponseEntity.badRequest().body(new SimplePayload("rescuer must start before get destinations"));

        if (floodWardService.peopleGetOutFromRescuerBoard(ward.getId(), userDetails.getUsername(), numPeople))
            return ResponseEntity.ok(new SimplePayload("everything is ok"));
        else
            return ResponseEntity.badRequest().body(new SimplePayload("numPeople = " + numPeople + " is not valid"));
    }

    @PostMapping("/go")
    @PreAuthorize("hasRole('RESCUER')")
    public ResponseEntity<?> startToRescue(Authentication authentication,
                                           @RequestParam float longitude,
                                           @RequestParam float latitude,
                                           @RequestParam int boardSize) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RoleRescuer roleRescuer = roleRescuerRepository.findByUsername(userDetails.getUsername()).get();

        Ward ward = roleRescuer.getWard();
        if (ward == null)
            return ResponseEntity.badRequest().body(new SimplePayload("you do not have any ward"));

        if (!floodWardService.checkInFlood(ward.getId()))
            return ResponseEntity.badRequest().body(new SimplePayload("this location do not have any flood"));

        floodWardService.startSavingFromRescuer(
                roleRescuer,
                new Location(longitude, latitude),
                boardSize);

        return ResponseEntity.ok(new SimplePayload("ok"));
    }

    @PostMapping("/stop")
    @PreAuthorize("hasRole('RESCUER')")
    public ResponseEntity<?> stopRescuing(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RoleRescuer roleRescuer = roleRescuerRepository.findByUsername(userDetails.getUsername()).get();

        Ward ward = roleRescuer.getWard();
        if (ward == null)
            return ResponseEntity.badRequest().body(new SimplePayload("you do not have any ward"));

        if (!floodWardService.checkInFlood(ward.getId()))
            return ResponseEntity.badRequest().body(new SimplePayload("this location do not have any flood"));

        if (!floodWardService.checkRescuerStarted(userDetails.getUsername(), roleRescuer.getWard().getId()))
            return ResponseEntity.badRequest().body(new SimplePayload("rescuer must start before get destinations"));


        floodWardService.stop(userDetails.getUsername(), ward.getId());

        return ResponseEntity.ok(new SimplePayload("ok"));
    }
}
