package restAPI.services;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restAPI.models.UserInfo;
import restAPI.models.role.ERole;
import restAPI.models.role.Role;
import restAPI.models.role.RoleUser;
import restAPI.repository.UserRepository;
import restAPI.repository.role.RoleRepository;
import restAPI.repository.role.RoleUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserInfoUtils {
    @Autowired
    UserRepository userInfoRepository;

    @Autowired
    RoleUtils roleUtils;

    static final String UserInfo_NotFound = "UserInfo not found!";


    public UserInfo getUserInfoByUsername(String username) {
        return userInfoRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException(UserInfo_NotFound));
    }

    public void removeRoleFromUserInfo(String username, ERole eRole) {
        UserInfo userInfo = this.getUserInfoByUsername(username);

        Role roleOfUserInfo = roleUtils.getRoleByERole(eRole);

        userInfo.getRoles().removeIf(
                role -> (role.getName().equals(roleOfUserInfo.getName()))
        );
    }

    public List<String> removeRoleFromAllUserInfo(ERole eRole) {
        List<UserInfo> userInfoList = userInfoRepository.findAllRoleUser();

        Role roleOfUserInfo = roleUtils.getRoleByERole(eRole);

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

        roleOfUserInfo.add(roleUtils.getRoleByERole(eRole));;

        userInfo.setRoles(roleOfUserInfo);

        return roleUser;
    }
}
