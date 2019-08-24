package tu.faas.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tu.faas.domain.constants.RoleConstants;
import tu.faas.domain.entities.Role;
import tu.faas.repositories.RoleRepository;

import javax.annotation.PostConstruct;

@Component
public class SeedRoles {
    private RoleRepository roleRepository;

    @Autowired
    public SeedRoles(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void seedRoles() {
        if (roleRepository.count() == 0) {
            Role rootAdminRole = new Role();
            rootAdminRole.setName(RoleConstants.ROLE_ROOT_ADMIN);

            Role adminRole = new Role();
            adminRole.setName(RoleConstants.ROLE_ADMIN);

            Role userRole = new Role();
            userRole.setName(RoleConstants.ROLE_USER);

            roleRepository.save(rootAdminRole);
            roleRepository.save(adminRole);
            roleRepository.save(userRole);
        }
    }
}