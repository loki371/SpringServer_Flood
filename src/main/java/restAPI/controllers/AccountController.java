package restAPI.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import restAPI.models.UserInfo;
import restAPI.payload.UserInfoList;
import restAPI.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/v1/api/accounts")
public class AccountController {
    @Autowired
    UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public HttpEntity<?> getListAccounts() {
        List<UserInfo> userInfos = userRepository.findAll();
        return new ResponseEntity<>(new UserInfoList(userInfos), HttpStatus.OK);
    }
}
