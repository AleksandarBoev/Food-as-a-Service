package tu.faas.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tu.faas.domain.constants.RoleConstants;
import tu.faas.domain.entities.Role;
import tu.faas.domain.entities.User;
import tu.faas.domain.exceptions.NoSuchUser;
import tu.faas.domain.exceptions.UserAlreadyExists;
import tu.faas.domain.models.binding.UserLoginBindingModel;
import tu.faas.domain.models.binding.UserRegisterBindingModel;
import tu.faas.repositories.RoleRepository;
import tu.faas.repositories.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
    }

    @Override
    public void registerUser(UserRegisterBindingModel userRegisterBindingModel) {
        if (userRepository.existsByName(userRegisterBindingModel.getName())) {
            throw new UserAlreadyExists(UserAlreadyExists.USERNAME_ALREADY_EXISTS);
        }

        if (userRepository.existsByEmail(userRegisterBindingModel.getEmail())) {
            throw new UserAlreadyExists(UserAlreadyExists.EMAIL_ALREADY_EXISTS);
        }

        User user = modelMapper.map(userRegisterBindingModel, User.class);

        Set<Role> roles = new HashSet<>();
        if (userRepository.count() == 0) { //first registered user becomes root admin
            roles.add(roleRepository.findRoleByName(RoleConstants.ROLE_ROOT_ADMIN));
            roles.add(roleRepository.findRoleByName(RoleConstants.ROLE_MANAGER));
        }

        roles.add(roleRepository.findRoleByName(RoleConstants.ROLE_USER));
        user.setRoles(roles);

        userRepository.save(user);
    }

    @Override
    public void loginUser(UserLoginBindingModel userLoginBindingModel, HttpSession session) {
        if (!userRepository.existsByNameAndPassword(
                userLoginBindingModel.getName(), userLoginBindingModel.getPassword())) {
            throw new NoSuchUser();
        }

        User user = userRepository.findUserByName(userLoginBindingModel.getName());
        session.setAttribute("username", user.getName());
        Set<String> userRoles = user.getRoles()
                .stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());
        //TODO try the HashSet equals trick.
        session.setAttribute("roles", userRoles);
        session.setAttribute("userId", user.getId());
    }
}
