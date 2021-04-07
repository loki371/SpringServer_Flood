package restAPI.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import restAPI.models.role.ERole;
import restAPI.models.role.RoleRescuer;
import restAPI.models.role.RoleVolunteer;
import restAPI.payload.SimplePayload;
import restAPI.repository.UserRepository;
import restAPI.repository.role.RoleRescuerRepository;
import restAPI.repository.role.RoleVolunteerRepository;
import restAPI.services.UserInfoService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/v1/api/volunteers")
public class VolunteerController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleVolunteerRepository roleRepository;

    @Autowired
    UserInfoService userInfoUtils;

    private final ERole finalERole = ERole.ROLE_VOLUNTEER;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllRoleRescuers() {
        List<String> usernames = roleRepository.findAllUsername();

        SimplePayload payload = new SimplePayload(usernames);

        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAllRoleRescuers() {
        List<String> UsernameOfRoleUsers = userInfoUtils.removeRoleFromAllUserInfo(finalERole);

        roleRepository.deleteAll();

        SimplePayload payload = new SimplePayload("ok", UsernameOfRoleUsers);

        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    @PreAuthorize("(#username == authentication.getName()) or hasRole('ADMIN')")
    public ResponseEntity<?> getRoleRescuer(@PathVariable("username") String username) {
        Optional<RoleVolunteer> roleUser = roleRepository.findByUsername(username);

        return roleUser.map(
                    info -> new ResponseEntity<>(new SimplePayload("ok", info), HttpStatus.OK))
                .orElseGet(
                    () -> new ResponseEntity<>(new SimplePayload("RoleVolunteer " + username + " is not found"), HttpStatus.NOT_FOUND)
                );
    }

    @PostMapping("/{username}")
    @PreAuthorize("(#username == authentication.getName()) or hasRole('ADMIN')")
    public ResponseEntity<?> addNewRoleRescuer(@PathVariable("username") String username) {
        if (!userRepository.existsByUsername(username))
            return new ResponseEntity<>(new SimplePayload("User " + username + " is not found"), HttpStatus.BAD_REQUEST);

        if (roleRepository.existsByUsername(username))
            return new ResponseEntity<>(new SimplePayload("User " + username + " has role volunteer."), HttpStatus.BAD_REQUEST);

        RoleVolunteer newRoleUser = (RoleVolunteer) userInfoUtils.addNewRoleToUserInfo(username, finalERole);

        roleRepository.save(newRoleUser);

        SimplePayload payload = new SimplePayload("ok", newRoleUser.getUsername());

        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("(#username == authentication.getName()) or hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> deleteRescuer(@PathVariable("username") String username) {
        if (!userRepository.existsByUsername(username))
            return new ResponseEntity<>(new SimplePayload("User " + username + " is not found"), HttpStatus.BAD_REQUEST);

        if (!roleRepository.existsByUsername(username))
            return new ResponseEntity<>(new SimplePayload("RoleVolunteer" + username + " is not found"), HttpStatus.NOT_FOUND);

        userInfoUtils.removeRoleFromUserInfo(username, finalERole);

        roleRepository.deleteByUsername(username);

        return new ResponseEntity<>(new SimplePayload("ok", username), HttpStatus.OK);
    }
}
