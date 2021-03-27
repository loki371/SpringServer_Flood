package restAPI.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import restAPI.models.role.ERole;
import restAPI.models.role.RoleRescuer;
import restAPI.models.role.RoleUser;
import restAPI.payload.SimplePayload;
import restAPI.repository.UserRepository;
import restAPI.repository.role.RoleRescuerRepository;
import restAPI.repository.role.RoleUserRepository;
import restAPI.services.UserInfoService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/api/rescuers")
public class RescuerController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRescuerRepository roleRescuerRepository;

    @Autowired
    UserInfoService userInfoUtils;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllRoleRescuers() {
        List<String> usernames = roleRescuerRepository.findAllUsername();

        SimplePayload payload = new SimplePayload(usernames);

        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAllRoleRescuers() {
        List<String> UsernameOfRoleUsers = userInfoUtils.removeRoleFromAllUserInfo(ERole.ROLE_RESCUER);

        roleRescuerRepository.deleteAll();

        SimplePayload payload = new SimplePayload("ok", UsernameOfRoleUsers);

        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    @PreAuthorize("(#username == authentication.getName()) or hasRole('ADMIN')")
    public ResponseEntity<?> getRoleRescuer(@PathVariable("username") String username) {
        Optional<RoleRescuer> roleUser = roleRescuerRepository.findByUsername(username);

        return roleUser.map(
                    info -> new ResponseEntity<>(new SimplePayload("ok", info), HttpStatus.OK))
                .orElseGet(
                    () -> new ResponseEntity<>(new SimplePayload("RoleRescuer " + username + " is not found"), HttpStatus.NOT_FOUND)
                );
    }

    @PostMapping("/{username}")
    @PreAuthorize("(#username == authentication.getName()) or hasRole('ADMIN')")
    public ResponseEntity<?> addNewRoleRescuer(@PathVariable("username") String username) {
        if (!userRepository.existsByUsername(username))
            return new ResponseEntity<>(new SimplePayload("User " + username + " is not found"), HttpStatus.BAD_REQUEST);

        if (roleRescuerRepository.existsByUsername(username))
            return new ResponseEntity<>(new SimplePayload("User " + username + " has role rescuer."), HttpStatus.BAD_REQUEST);

        RoleRescuer newRoleUser = (RoleRescuer) userInfoUtils.addNewRoleToUserInfo(username, ERole.ROLE_RESCUER);

        roleRescuerRepository.save(newRoleUser);

        SimplePayload payload = new SimplePayload("ok", newRoleUser.getUsername());

        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("(#username == authentication.getName()) or hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> deleteRescuer(@PathVariable("username") String username) {
        if (!userRepository.existsByUsername(username))
            return new ResponseEntity<>(new SimplePayload("User " + username + " is not found"), HttpStatus.BAD_REQUEST);

        if (!roleRescuerRepository.existsByUsername(username))
            return new ResponseEntity<>(new SimplePayload("RoleRescuer " + username + " is not found"), HttpStatus.NOT_FOUND);

        userInfoUtils.removeRoleFromUserInfo(username, ERole.ROLE_RESCUER);

        roleRescuerRepository.deleteByUsername(username);

        return new ResponseEntity<>(new SimplePayload("ok", username), HttpStatus.OK);
    }
}
