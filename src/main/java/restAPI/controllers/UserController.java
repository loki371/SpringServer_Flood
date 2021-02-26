package restAPI.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import restAPI.Constants;
import restAPI.models.UserInfo;
import restAPI.models.role.ERole;
import restAPI.models.role.Role;
import restAPI.models.role.RoleUser;
import restAPI.payload.SimplePayload;
import restAPI.repository.UserRepository;
import restAPI.repository.role.RoleRepository;
import restAPI.repository.role.RoleUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/v1/api/users")
public class UserController {
    @Autowired
    UserRepository userInfoRepository;

    @Autowired
    RoleUserRepository roleUserRepository;

    @Autowired
    RoleRepository roleRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        List<RoleUser> listRole = roleUserRepository.findAll();
        SimplePayload payload = new SimplePayload("ok", listRole);
        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAllUsers() {
        List<UserInfo> userInfoList = userInfoRepository.findAllRoleUser();

        Role roleUser = roleRepository.findByName(ERole.ROLE_USER).get();

        ArrayList<String> roleUserUserNames = new ArrayList<>();

        userInfoList.forEach(
                userInfo -> {
//                    userInfo.setRoleUser(null);

                    userInfo.getRoles().removeIf(
                            role -> (role.getName().equals(roleUser.getName()))
                    );

                    roleUserUserNames.add(userInfo.getUsername());
                }
        );

        userInfoRepository.saveAll(userInfoList);
        roleUserRepository.deleteAll();

        SimplePayload payload = new SimplePayload("ok", roleUserUserNames);
        return new ResponseEntity<>(payload, HttpStatus.OK);
    }
}
