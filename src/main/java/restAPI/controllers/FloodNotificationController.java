package restAPI.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import restAPI.payload.SimplePayload;
import restAPI.services.FloodNotificationService;
import restAPI.services.WardService;

import java.util.List;

@RestController
@RequestMapping("/v1/api/floodNotifications")
public class FloodNotificationController {

    @Autowired
    WardService wardService;

    @Autowired
    FloodNotificationService floodNotificationService;

    @PostMapping("/{wardId}")
    @PreAuthorize("hasRole('AUTHORITY')")
    public ResponseEntity<?> createLocationInFloodNotification(@PathVariable String wardId) {
        if (!wardService.existWardId(wardId))
            return ResponseEntity.notFound().build();

        boolean result = floodNotificationService.checkThenAddWardIdToFlood(wardId);
        if (!result)
            return ResponseEntity.badRequest().body(new SimplePayload("object created!", wardId));

        return ResponseEntity.ok().body(new SimplePayload("ok", wardId));
    }

    @GetMapping
    public ResponseEntity<?> getAllFloodLocation() {
        List<String> wardIds = floodNotificationService.getListWardInFlood();
        return ResponseEntity.ok().body(new SimplePayload(wardIds));
    }
}
