package restAPI.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restAPI.models.UserInfo;
import restAPI.models.role.ERole;
import restAPI.models.role.Role;
import restAPI.models.role.RoleUser;
import restAPI.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserInfoService {
    @Autowired
    UserRepository userInfoRepository;

    @Autowired
    RoleService roleService;

    static final String UserInfo_NotFound = "UserInfo not found!";


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

        RoleUser roleUser = new RoleUser(userInfo);

        Set<Role> roleOfUserInfo = userInfo.getRoles();

        roleOfUserInfo.add(roleService.getRoleByERole(eRole));;

        userInfo.setRoles(roleOfUserInfo);

        return roleUser;
    }
}
