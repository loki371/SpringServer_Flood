package restAPI.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import restAPI.models.UserInfo;
import restAPI.payload.MessagePayload;
import restAPI.payload.SignupPayload;
import restAPI.payload.UserInfoListPayload;
import restAPI.payload.UserInfoPayload;
import restAPI.repository.UserRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/api/accounts")
public class AccountController {
    @Autowired
    UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public HttpEntity<?> getListAccounts() {
        List<UserInfo> userInfos = userRepository.findAll();
        return new ResponseEntity<>(new UserInfoListPayload(userInfos), HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public HttpEntity<?> deleteListAccounts() {
        userRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getAccount(@PathVariable("username") String username) {
        Optional<UserInfo> userInfo = userRepository.findByUsername(username);

        if (!userInfo.isPresent())
            return new ResponseEntity<>(new MessagePayload("Not found username = " + username), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(new UserInfoPayload(userInfo.get()), HttpStatus.OK);
    }

    @PutMapping("/{username}")
    @PreAuthorize("(#username == authentication.getName()) or hasRole('ADMIN')")
    public ResponseEntity<?> updateAccount(@PathVariable("username") String username, @Valid @RequestBody SignupPayload request) {
        Optional<UserInfo> userInfo = userRepository.findByUsername(username);

        if (!userInfo.isPresent())
            return new ResponseEntity<>(
                    new MessagePayload("Not found username = " + username),
                    HttpStatus.NOT_FOUND);

        UserInfo realUserInfo = userInfo.get(); // realUserInfo = userInfo != null

//        if (!request.getUsername().equals(realUserInfo.getUsername())) {
//            if (userRepository.existsByUsername(request.getUsername()))
//                return new ResponseEntity<>(
//                        new MessagePayload("Username " + realUserInfo.getUsername() + " has been used"),
//                        HttpStatus.NOT_FOUND);
//        }

        if (!request.getEmail().equals(realUserInfo.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail()))
                return new ResponseEntity<>(
                        new MessagePayload("Email " + request.getEmail() + " has been used"),
                        HttpStatus.NOT_FOUND);
        }

//        realUserInfo.setUsername(request.getUsername());
        realUserInfo.setEmail(request.getEmail());
        realUserInfo.setFirstname(request.getFirstname());
        realUserInfo.setLastname(request.getLastname());
        realUserInfo.setPhone(request.getPhone());

        userRepository.save(realUserInfo);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("(#username == authentication.getName()) or hasRole('ADMIN')")
    public ResponseEntity<?> deleteAccount(@PathVariable("username") String username) {
        Optional<UserInfo> userInfo = userRepository.findByUsername(username);

        if (!userInfo.isPresent())
            return new ResponseEntity<>(new MessagePayload("Not found username = " + username), HttpStatus.NOT_FOUND);

        userRepository.delete(userInfo.get());

        return new ResponseEntity<>(new MessagePayload("Delete " + username + " successfully"), HttpStatus.OK);
    }
}
