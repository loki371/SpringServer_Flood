package restAPI.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restAPI.models.location.Ward;
import restAPI.models.role.ERole;
import restAPI.models.role.Role;
import restAPI.repository.role.*;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    static final String Role_NotFound = "Role not found!";

    public Role getRoleByERole(ERole eRole) {
        return roleRepository.findByName(eRole)
                .orElseThrow(() -> new RuntimeException(Role_NotFound));
    }


}
