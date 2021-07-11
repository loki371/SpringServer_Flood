package restAPI.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import restAPI.ErrorCode;
import restAPI.buckets.RegistrationBucket;
import restAPI.grab.FloodWardService;
import restAPI.models.UserInfo;
import restAPI.models.location.Ward;
import restAPI.models.registration.EState;
import restAPI.models.registration.RegisOrder;
import restAPI.models.registration.Registration;
import restAPI.models.registration.Viewer;
import restAPI.models.role.ERole;
import restAPI.models.role.RoleVolunteer;
import restAPI.object_function.FloodRegistrationAlter;
import restAPI.object_function.I_ObjectFunction;
import restAPI.object_function.SetSavedByFunction;
import restAPI.payload.RegistrationPayload;
import restAPI.payload.SimplePayload;
import restAPI.repository.registration.RegistrationRepository;
import restAPI.repository.registration.ViewerRepository;
import restAPI.repository.role.RoleAuthorityRepository;
import restAPI.repository.role.RoleVolunteerRepository;
import restAPI.security.services.UserDetailsImpl;
import restAPI.services.RegisOrderService;
import restAPI.services.RegistrationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*",maxAge = 3600)
@RestController
@RequestMapping("/v1/api/registrations")
public class RegistrationController {

    @Autowired
    RegistrationService registrationService;

    @Autowired
    FloodWardService floodWardService;

    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    ViewerRepository viewerRepository;

    @Autowired
    RoleVolunteerRepository roleVolunteerRepository;

    @Autowired
    RoleAuthorityRepository roleAuthorityRepository;

    @Autowired
    RegisOrderService regisOrderService;

    int countVolunteerGetList = 1;
    public static final int sizeVolunteerList = 10;

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

        List finalList;

        if (roleVolunteerRepository.existsByUsername(userDetails.getUsername())) {
            finalList = new ArrayList();
            registrationList.removeIf(item ->
                    item.getEState() != EState.STATE_DANGER && item.getEState() != EState.STATE_EMERGENCY);

            int start = countVolunteerGetList * sizeVolunteerList;
            ++countVolunteerGetList;
            int end = countVolunteerGetList * sizeVolunteerList;
            countVolunteerGetList = countVolunteerGetList % 1000000;

            int listSize = registrationList.size();
            System.out.println("Volunteer.getAllRegistrations: " + listSize);
            if (listSize > 0) {
                start = start % listSize;
                end = end % listSize;

                if (start - end < sizeVolunteerList)
                    end = start;

                for (int i = start + 1; i != end; i = (i + 1) % listSize) {
                    Registration item = registrationList.get(i);
                    RegistrationBucket bucket = new RegistrationBucket();

                    bucket.id = item.getId();
                    bucket.eState = item.getEState();
                    bucket.latitude = item.getLatitude();
                    bucket.longitude = item.getLongitude();
                    bucket.name = item.getName();
                    bucket.phone = item.getPhone();
                    bucket.numPerson = item.getNumPerson();
                    bucket.ward = item.getWard();
                    bucket.order = regisOrderService.getOrderById(item.getId());

                    finalList.add(bucket);
                }
            }

        } else if (roleAuthorityRepository.existsByUsername(userDetails.getUsername())) {
            finalList = new ArrayList();

            for (Registration item : registrationList) {
                RegistrationBucket bucket = new RegistrationBucket();

                bucket.id = item.getId();
                bucket.eState = item.getEState();
                bucket.latitude = item.getLatitude();
                bucket.longitude = item.getLongitude();
                bucket.name = item.getName();
                bucket.phone = item.getPhone();
                bucket.numPerson = item.getNumPerson();
                bucket.ward = item.getWard();
//                bucket.order = regisOrderService.getOrderById(item.getId());

                finalList.add(bucket);
            }
        } else {
            finalList = registrationList;
        }

        return ResponseEntity.ok().body(new SimplePayload(finalList));
    }

    @PostMapping("/MyRegistrations")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addRegisToRegis(Authentication authentication, @RequestParam long registrationId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UserInfo userInfo = userDetails.getUserInfo();

        Optional<Registration> registration = registrationRepository.findById(registrationId);
        if (registration.isPresent()) {
            if (userInfo.getUsername().equals(registration.get().getName()))
                return ResponseEntity.badRequest().body(new SimplePayload("Đăng ký này đã nằm trong danh sách của bạn"));

            Optional<Viewer> viewerOptional = viewerRepository.findByUsername(userInfo.getUsername());
            Viewer viewer;
            if (viewerOptional.isPresent()) {
                viewer = viewerOptional.get();
            } else {
                viewer = new Viewer(userInfo.getUsername());
            }
            List<Registration> registrationList = viewer.getRegistrationList();
            for (Registration item : registrationList) {
                if (item.getId() == registrationId)
                    return ResponseEntity.badRequest().body(new SimplePayload("Đăng ký này đã nằm trong danh sách của bạn"));
            }

            registrationList.add(registration.get());
            viewerRepository.save(viewer);
            return ResponseEntity.ok(new SimplePayload("ok", viewer));

        } else {
            return ResponseEntity.badRequest().body(new SimplePayload("Đăng ký này không tồn tại"));
        }
    }

    @GetMapping("/MyRegistrations")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getMyRegistrations(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List compositeList = new ArrayList();
        List<Registration> registrationList = registrationService.getMyRegistrations(userDetails.getUsername());

        Viewer viewer;
        Optional<Viewer> viewerOptional = viewerRepository.findByUsername(userDetails.getUsername());
        if (!viewerOptional.isPresent())
            viewer = new Viewer(userDetails.getUsername());
        else
            viewer = viewerOptional.get();

        List<Registration> registrationList1 = viewer.getRegistrationList();

        compositeList.add(registrationList);

        registrationList1.removeIf(item -> !registrationRepository.existsById(item.getId()));
        compositeList.add(registrationList1);


        return ResponseEntity.ok().body(new SimplePayload(compositeList));
    }

    @DeleteMapping("/MyRegistrations")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getDeleteRegistration(Authentication authentication, @RequestParam long registrationId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<Registration> registrationList = registrationRepository.findAllByCreateBy(userDetails.getUserInfo());

        Viewer viewer;
        Optional<Viewer> viewerOptional = viewerRepository.findByUsername(userDetails.getUsername());
        viewer = viewerOptional.orElseGet(() -> new Viewer(userDetails.getUsername()));

        List<Registration> registrationList1 = viewer.getRegistrationList();

        boolean removed = registrationList.removeIf(item ->
                item.getId() == registrationId);

        registrationList1.removeIf(item ->
                item.getId() == registrationId);

        viewer.setRegistrationList(registrationList1);
        viewerRepository.save(viewer);
        if (removed) {
            registrationRepository.deleteById(registrationId);
            System.out.println("delete in registration repo = true");
        }

        return ResponseEntity.ok().body(new SimplePayload("ok"));
    }

    @PostMapping("/MyRegistrations/update")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateRegistration(Authentication authentication, @RequestBody RegistrationPayload request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<Registration> registration = registrationRepository.findById(request.getId());
        if (registration.isPresent()) {
            Registration regis1 = registration.get();

            if (!regis1.getCreateBy().getUsername().equals(userDetails.getUsername()))
                return ResponseEntity.badRequest().body(new SimplePayload("this regis do no belong to you"));

            regis1.setNumPerson(request.getNumPerson());
            regis1.setLatitude(request.getLatitude());
            regis1.setLongitude(request.getLongitude());
            regis1.setPhone(request.getPhone());
            regis1.setName(request.getName());
            registrationRepository.save(regis1);

            return ResponseEntity.ok(new SimplePayload("ok"));
        } else
            return ResponseEntity.badRequest().body(new SimplePayload("this registration is not exists"));
    }

    @PutMapping("/authorities/{registrationId}")
    @PreAuthorize("hasRole('AUTHORITY')")
    public ResponseEntity<?> authenticateRegistration(@PathVariable Long registrationId, Authentication authentication,
                                                      @RequestParam("oldState") EState oldState,
                                                      @RequestParam("newState") EState newState) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        FloodRegistrationAlter lastAction = new FloodRegistrationAlter();
        lastAction.setFloodWardService(floodWardService);

        switch (oldState) {
            case STATE_AUTHENTICATED:
            case STATE_UNAUTHENTICATED:
                break;
            default:
                return ResponseEntity.badRequest().body(new SimplePayload("eState is not valid for authority, must be STATE_AUTHENTICATED or STATE_UNAUTHENTICATED"));
        }

        switch (newState) {
            case STATE_AUTHENTICATED:
            case STATE_UNAUTHENTICATED:
                break;
            default:
                return ResponseEntity.badRequest().body(new SimplePayload("eState is not valid for authority, must be STATE_AUTHENTICATED or STATE_UNAUTHENTICATED"));
        }

        lastAction.setOldState(oldState);
        lastAction.setNewState(newState);
        lastAction.setRegistrationRepository(registrationRepository);

        if (oldState == newState)
            return ResponseEntity.badRequest().body(new SimplePayload("oldState must be != newState"));

        ErrorCode errorCode = registrationService.applyNewEStateToRegistration(
                registrationId,
                userDetails.getUsername(),
                ERole.ROLE_AUTHORITY,
                oldState,
                newState,
                lastAction);


        if (errorCode == ErrorCode.OK) {
            Registration registration = registrationRepository.findById(registrationId).get();
            return ResponseEntity.ok(new SimplePayload("updated", registration.getEState() == EState.STATE_DANGER));
        } else {
            System.out.println("errorCode = " + errorCode.toString());
            return ResponseEntity.badRequest().body(new SimplePayload(errorCode.toString()));
        }
    }

    @PutMapping("/volunteers/{registrationId}")
    @PreAuthorize("hasRole('VOLUNTEER')")
    public ResponseEntity<?> confirmRegistration(@PathVariable Long registrationId, Authentication authentication,
                                                 @RequestParam("oldState") EState oldState,
                                                 @RequestParam("newState") EState newState) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        FloodRegistrationAlter lastAction = new FloodRegistrationAlter();
        lastAction.setFloodWardService(floodWardService);

        switch (oldState) {
            case STATE_DANGER:
            case STATE_SAFE:
            case STATE_EMERGENCY:
            case STATE_SAVED:
                break;
            default:
                return ResponseEntity.badRequest().body(new SimplePayload("oldState is not valid for volunteer"));
        }

        switch (newState) {
            case STATE_DANGER:
            case STATE_SAFE:
            case STATE_EMERGENCY:
            case STATE_SAVED:
                break;
            default:
                return ResponseEntity.badRequest().body(new SimplePayload("newState is not valid for volunteer"));
        }

        lastAction.setOldState(oldState);
        lastAction.setNewState(newState);
        lastAction.setRegistrationRepository(registrationRepository);

        if (oldState == newState)
            return ResponseEntity.badRequest().body(new SimplePayload("oldState must be != newState"));

        ErrorCode errorCode = registrationService.applyNewEStateToRegistration(
                registrationId,
                userDetails.getUsername(),
                ERole.ROLE_VOLUNTEER,
                oldState,
                newState,
                lastAction);

        if (errorCode == ErrorCode.OK) {
            return ResponseEntity.ok(new SimplePayload("updated", false));
        } else
            return ResponseEntity.badRequest().body(new SimplePayload(errorCode.toString()));
    }

    @PutMapping("/updateRegistrations/{registrationId}")
    @PreAuthorize("hasRole('VOLUNTEER')")
    public ResponseEntity<?> confirmRegistration(@PathVariable Long registrationId, Authentication authentication,
                                                 @RequestParam("numPeople") int numPeople,
                                                 @RequestParam("order") int order) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        RoleVolunteer volunteer = roleVolunteerRepository.findByUsername(userDetails.getUsername()).get();
        Ward ward = volunteer.getWard();

        if (ward == null)
            return ResponseEntity.badRequest().body("volunteer do not have any ward");

        if (!floodWardService.checkInFlood(ward.getId()))
            return ResponseEntity.badRequest().body("this location is not in flood");

        Optional<Registration> registrationOptional = registrationRepository.findById(registrationId);

        if (registrationOptional.isPresent()) {

            Registration registration = registrationOptional.get();

            if (!registration.getWard().getId().equals(ward.getId()))
                return ResponseEntity.badRequest().body(new SimplePayload("volunteer - regis do not match"));

            registration.setNumPerson(numPeople);
            registrationRepository.save(registration);

            floodWardService.updateRegistrationInWard(
                    registration.getWard().getId(),
                    registration.getId(),
                    numPeople,
                    order
            );

            return ResponseEntity.ok(new SimplePayload("ok"));

        } else {
            return ResponseEntity.badRequest().body(new SimplePayload("regis is not present"));
        }
    }
}
