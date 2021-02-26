package restAPI.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import restAPI.models.role.ERole;
import restAPI.models.role.RoleUser;
import restAPI.payload.SignupPayload;
import restAPI.payload.SimplePayload;
import restAPI.repository.UserRepository;
import restAPI.repository.role.RoleUserRepository;
import restAPI.services.UserInfoUtils;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/api/users")
@Transactional
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleUserRepository roleUserRepository;

    @Autowired
    UserInfoUtils userInfoUtils;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllRoleUsers() {
        List<String> usernames = roleUserRepository.findAllUsername();

        SimplePayload payload = new SimplePayload(usernames);

        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAllRoleUsers() {
        List<String> UsernameOfRoleUsers = userInfoUtils.removeRoleFromAllUserInfo(ERole.ROLE_USER);

        roleUserRepository.deleteAll();

        SimplePayload payload = new SimplePayload("ok", UsernameOfRoleUsers);

        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    @PreAuthorize("(#username == authentication.getName()) or hasRole('ADMIN')")
    public ResponseEntity<?> getRoleUser(@PathVariable("username") String username) {
        Optional<RoleUser> roleUser = roleUserRepository.findByUsername(username);

        return roleUser.map(
                    info -> new ResponseEntity<>(new SimplePayload(info.getUsername()), HttpStatus.OK))
                .orElseGet(
                    () -> new ResponseEntity<>(new SimplePayload("RoleUser " + username + " is not found"), HttpStatus.NOT_FOUND)
                );
    }

    @PostMapping("/{username}")
    @PreAuthorize("(#username == authentication.getName()) or hasRole('ADMIN')")
    public ResponseEntity<?> addNewRoleUser(@PathVariable("username") String username) {
        if (!userRepository.existsByUsername(username))
            return new ResponseEntity<>(new SimplePayload("User " + username + " is not found"), HttpStatus.BAD_REQUEST);

        if (roleUserRepository.existsByUsername(username))
            return new ResponseEntity<>(new SimplePayload("User " + username + " has role user."), HttpStatus.BAD_REQUEST);

        RoleUser newRoleUser = (RoleUser) userInfoUtils.addNewRoleToUserInfo(username, ERole.ROLE_USER);

        roleUserRepository.save(newRoleUser);

        SimplePayload payload = new SimplePayload("ok", newRoleUser.getUsername());

        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("(#username == authentication.getName()) or hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable("username") String username) {
        if (!userRepository.existsByUsername(username))
            return new ResponseEntity<>(new SimplePayload("User " + username + " is not found"), HttpStatus.BAD_REQUEST);

        if (!roleUserRepository.existsByUsername(username))
            return new ResponseEntity<>(new SimplePayload("RoleUser " + username + " is not found"), HttpStatus.NOT_FOUND);

        userInfoUtils.removeRoleFromUserInfo(username, ERole.ROLE_USER);

        roleUserRepository.deleteByUsername(username);

        return new ResponseEntity<>(new SimplePayload(username), HttpStatus.OK);
    }
}
