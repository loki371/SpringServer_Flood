package restAPI.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restAPI.models.UserInfo;
import restAPI.models.location.Ward;
import restAPI.models.role.*;
import restAPI.repository.UserRepository;
import restAPI.repository.role.RoleAuthorityRepository;
import restAPI.repository.role.RoleRescuerRepository;
import restAPI.repository.role.RoleUserRepository;
import restAPI.repository.role.RoleVolunteerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserInfoService {
    @Autowired
    UserRepository userInfoRepository;

    @Autowired
    RoleService roleService;

    static final String UserInfo_NotFound = "UserInfo not found!";

    @Autowired
    RoleAuthorityRepository roleAuthorityRepository;
    @Autowired
    RoleRescuerRepository roleRescuerRepository;
    @Autowired
    RoleVolunteerRepository roleVolunteerRepository;
    @Autowired
    RoleUserRepository roleUserRepository;


    public UserInfo getUserInfoByUsername(String username) {
        return userInfoRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException(UserInfo_NotFound));
    }

    public void removeRoleFromUserInfo(String username, ERole eRole) {
        UserInfo userInfo = this.getUserInfoByUsername(username);

        Role roleOfUserInfo = roleService.getRoleByERole(eRole);

        userInfo.getRoles().removeIf(
                role -> (role.getName().equals(roleOfUserInfo.getName()))
        );
        userInfoRepository.save(userInfo);
    }

    public List<String> removeRoleFromAllUserInfo(ERole eRole) {
        List<UserInfo> userInfoList = userInfoRepository.findAllRoleUser();

        Role roleOfUserInfo = roleService.getRoleByERole(eRole);

        ArrayList<String> roleUserNames = new ArrayList<>();

        userInfoList.forEach(
                userInfo -> {

                    userInfo.getRoles().removeIf(
                            role -> (role.getName().equals(roleOfUserInfo.getName()))
                    );

                    roleUserNames.add(userInfo.getUsername());
                }
        );

        userInfoRepository.saveAll(userInfoList);

        return roleUserNames;
    }

    public Object addNewRoleToUserInfo(String username, ERole eRole) {
        UserInfo userInfo = this.getUserInfoByUsername(username);

        Object role = null;

        switch (eRole) {
            case ROLE_USER:
                role = new RoleUser(userInfo);
                break;
            case ROLE_RESCUER:
                role = new RoleRescuer(userInfo);
                break;
            case ROLE_VOLUNTEER:
                role = new RoleVolunteer(userInfo);
                break;
            case ROLE_AUTHORITY:
                role = new RoleAuthority(userInfo);
                break;
        }
        Set<Role> roleOfUserInfo = userInfo.getRoles();

        roleOfUserInfo.add(roleService.getRoleByERole(eRole));;

        userInfo.setRoles(roleOfUserInfo);

        return role;
    }

    public boolean hasERoleInUserInfo(UserInfo userInfo, ERole eRole) {
        for (Role userRole : userInfo.getRoles()) {
            if (userRole.getName() == eRole)
                return true;
        }
        return false;
    }

    public Ward getWardOfUserRole(String username, ERole eRole) {
        switch (eRole) {
            case ROLE_USER:
            case ROLE_ADMIN:
                return null;

            case ROLE_RESCUER:
                Optional<RoleRescuer> roleRescuer = roleRescuerRepository.findByUsername(username);
                return roleRescuer.map(RoleRescuer::getWard).orElse(null);

            case ROLE_AUTHORITY:
                Optional<RoleAuthority> roleAuthority = roleAuthorityRepository.findByUsername(username);
                return roleAuthority.map(RoleAuthority::getWard).orElse(null);

            case ROLE_VOLUNTEER:
                Optional<RoleVolunteer> roleVolunteer = roleVolunteerRepository.findByUsername(username);
                return roleVolunteer.map(RoleVolunteer::getWard).orElse(null);
        }
        return null;
    }
}
