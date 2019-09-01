package tu.faas.services.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tu.faas.domain.constants.RoleConstants;
import tu.faas.domain.entities.Role;
import tu.faas.domain.entities.User;
import tu.faas.domain.exceptions.*;
import tu.faas.domain.models.binding.UserLoginBindingModel;
import tu.faas.domain.models.binding.UserRegisterBindingModel;
import tu.faas.domain.models.multipurpose.UserEmailModel;
import tu.faas.domain.models.multipurpose.UserNameModel;
import tu.faas.domain.models.view.UserProfileViewModel;
import tu.faas.repositories.RoleRepository;
import tu.faas.repositories.UserRepository;
import tu.faas.services.contracts.UserService;

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
        if (!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getRepassword())) {
            throw new PasswordsNotSame();
        }

        if (userRepository.existsByName(userRegisterBindingModel.getName())) {
            throw new UsernameAlreadyExists();
        }

        if (userRepository.existsByEmail(userRegisterBindingModel.getEmail())) {
            throw new EmailAlreadyExists();
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

    //TODO session logic maybe needs to be in the controller
    //that means this method should return a UserSession object and the controller
    //just writes all the info. But as strings. Dunno if thymeleaf can do stuff...
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

        session.setAttribute("roles", userRoles);
        session.setAttribute("userId", user.getId());
    }

    @Override
    public UserProfileViewModel getUserProfileViewModel(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NoSuchUser::new);
        return modelMapper.map(user, UserProfileViewModel.class);
    }

    @Override
    public UserNameModel getUserNameModel(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NoSuchUser::new);
        return modelMapper.map(user, UserNameModel.class);
    }

    @Override
    public void editUserName(UserNameModel userNameModel, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NoSuchUser::new);
        if (!user.getPassword().equals(userNameModel.getPassword())) {
            throw new WrongPassword();
        }

        if (user.getName().equals(userNameModel.getName())) {
            throw new SameName();
        }

        if (userRepository.existsByName(userNameModel.getName())) {
            throw new UsernameAlreadyExists();
        }

        user.setName(userNameModel.getName());
        userRepository.save(user);
    }

    @Override
    public UserEmailModel getUserEmailModel(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NoSuchUser::new);
        return modelMapper.map(user, UserEmailModel.class);
    }


    @Override
    public void editUserEmail(UserEmailModel userEmailModel, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NoSuchUser::new);
        if (!user.getPassword().equals(userEmailModel.getPassword())) {
            throw new WrongPassword();
        }

        if (user.getEmail().equals(userEmailModel.getEmail())) {
            throw new SameEmail();
        }

        if (userRepository.existsByEmail(userEmailModel.getEmail())) {
            throw new EmailAlreadyExists();
        }

        user.setEmail(userEmailModel.getEmail());
        userRepository.save(user);
    }
}
