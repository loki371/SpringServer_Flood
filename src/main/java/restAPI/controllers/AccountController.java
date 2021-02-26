package restAPI.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import restAPI.models.UserInfo;
import restAPI.models.role.ERole;
import restAPI.payload.*;
import restAPI.repository.UserRepository;
import restAPI.repository.role.RoleAuthorityRepository;
import restAPI.repository.role.RoleRescuerRepository;
import restAPI.repository.role.RoleUserRepository;
import restAPI.repository.role.RoleVolunteerRepository;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/api/accounts")
public class AccountController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleUserRepository roleUserRepository;

    @Autowired
    RoleRescuerRepository roleRescuerRepository;

    @Autowired
    RoleVolunteerRepository roleVolunteerRepository;

    @Autowired
    RoleAuthorityRepository roleAuthorityRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public HttpEntity<?> getListAccounts() {
        List<UserInfo> userInfos = userRepository.findAll();
        return new ResponseEntity<>(
                new SimplePayload(userInfos),
                HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public HttpEntity<?> deleteListAccounts() {
        roleUserRepository.deleteAll();
        roleAuthorityRepository.deleteAll();
        roleRescuerRepository.deleteAll();
        roleVolunteerRepository.deleteAll();
        userRepository.deleteAll();
        return new ResponseEntity<>(new SimplePayload("ok"), HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getAccount(@PathVariable("username") String username) {
        Optional<UserInfo> userInfo = userRepository.findByUsername(username);

        return userInfo.map(
                    info -> new ResponseEntity<>(new SimplePayload(info), HttpStatus.OK))
                .orElseGet(
                    () -> new ResponseEntity<>(new SimplePayload("Not found username = " + username), HttpStatus.NOT_FOUND)
                );
    }

    @PutMapping("/{username}")
    @PreAuthorize("(#username == authentication.getName()) or hasRole('ADMIN')")
    public ResponseEntity<?> updateAccount(@PathVariable("username") String username, @Valid @RequestBody SignupPayload request) {
        Optional<UserInfo> userInfo = userRepository.findByUsername(username);

        if (!userInfo.isPresent())
            return new ResponseEntity<>(
                    new SimplePayload("Not found username = " + username),
                    HttpStatus.NOT_FOUND);

        UserInfo realUserInfo = userInfo.get(); // realUserInfo = userInfo != null

        if (!request.getEmail().equals(realUserInfo.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail()))
                return new ResponseEntity<>(
                        new SimplePayload("Email " + request.getEmail() + " has been used"),
                        HttpStatus.NOT_FOUND);
        }

        realUserInfo.setEmail(request.getEmail());
        realUserInfo.setFirstname(request.getFirstname());
        realUserInfo.setLastname(request.getLastname());
        realUserInfo.setPhone(request.getPhone());

        userRepository.save(realUserInfo);

        return new ResponseEntity<>(new SimplePayload(realUserInfo), HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("(#username == authentication.getName()) or hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> deleteAccount(@PathVariable("username") String username) {
        Optional<UserInfo> userInfo = userRepository.findByUsername(username);

        if (!userInfo.isPresent())
            return new ResponseEntity<>(new SimplePayload("Not found username = " + username), HttpStatus.NOT_FOUND);

        UserInfo realUserInfo = userInfo.get();
        realUserInfo.getRoles().forEach(
                role -> {
                    ERole nameRole = role.getName();
                    switch (nameRole) {
                        case ROLE_USER:
                            roleUserRepository.deleteByUsername(realUserInfo.getUsername());
                            break;
                        case ROLE_AUTHORITY:
                            roleAuthorityRepository.deleteByUsername(realUserInfo.getUsername());
                            break;
                        case ROLE_RESCUER:
                            roleRescuerRepository.deleteByUsername(realUserInfo.getUsername());
                            break;
                        case ROLE_VOLUNTEER:
                            roleVolunteerRepository.deleteByUsername(realUserInfo.getUsername());
                    }
                }
        );

        userRepository.delete(realUserInfo);

        return new ResponseEntity<>(new SimplePayload("Delete " + username + " successfully"), HttpStatus.OK);
    }
}
