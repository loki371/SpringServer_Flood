package restAPI.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import restAPI.grab.FloodWardService;
import restAPI.models.registration.EState;
import restAPI.models.registration.Registration;
import restAPI.payload.SimplePayload;
import restAPI.repository.registration.RegistrationRepository;
import restAPI.services.FloodNotificationService;
import restAPI.services.RegistrationService;
import restAPI.services.WardService;

import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/v1/api/floodNotifications")
public class FloodNotificationController {

    @Autowired
    WardService wardService;

    @Autowired
    FloodNotificationService floodNotificationService;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    FloodWardService floodWardService;

    @PostMapping("/{wardId}")
    @PreAuthorize("hasRole('AUTHORITY')")
    public ResponseEntity<?> createLocationInFloodNotification(@PathVariable String wardId) {
        if (!wardService.existWardId(wardId))
            return ResponseEntity.notFound().build();

        boolean result = floodNotificationService.checkThenAddWardIdToFlood(wardId);

        if (result) {
            List<Registration> registrations = registrationService.changeNotifyFloodRegistrationAndGetAllRegistrations(wardId);

            floodWardService.createFloodingLocation(wardId, registrations);

            return ResponseEntity.ok().body(new SimplePayload("ok", wardId));

        } else
            return ResponseEntity.badRequest().body(new SimplePayload("object has been in FloodLocationList!", wardId));
    }

    @GetMapping
    public ResponseEntity<?> getAllFloodLocation() {
        List<String> wardIds = floodNotificationService.getListWardInFlood();
        return ResponseEntity.ok().body(new SimplePayload(wardIds));
    }

    @DeleteMapping("/{wardId}")
    @PreAuthorize("hasRole('AUTHORITY')")
    public ResponseEntity<?> deleteLocationInFloodNotification(@PathVariable String wardId) {
        if (!wardService.existWardId(wardId))
            return ResponseEntity.notFound().build();

        boolean result = floodNotificationService.checkThenRemoveWardIdToFlood(wardId);
        if (!result) {
            floodWardService.removeFloodWard(wardId);
            return ResponseEntity.badRequest().body(new SimplePayload("object is not in FloodLocationList!", wardId));
        }
        return ResponseEntity.ok().body(new SimplePayload("ok", wardId));
    }
}
