package tu.faas.services.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tu.faas.domain.constants.RoleConstants;
import tu.faas.domain.constants.SessionConstants;
import tu.faas.domain.entities.Role;
import tu.faas.domain.entities.User;
import tu.faas.domain.exceptions.*;
import tu.faas.domain.models.binding.AdminAction;
import tu.faas.domain.models.binding.UserEditPasswordModel;
import tu.faas.domain.models.binding.UserLoginBindingModel;
import tu.faas.domain.models.binding.UserRegisterBindingModel;
import tu.faas.domain.models.multipurpose.UserEmailModel;
import tu.faas.domain.models.multipurpose.UserNameModel;
import tu.faas.domain.models.view.UserProfileViewModel;
import tu.faas.domain.models.view.UserUsersViewModel;
import tu.faas.repositories.RoleRepository;
import tu.faas.repositories.UserRepository;
import tu.faas.services.contracts.RestaurantService;
import tu.faas.services.contracts.UserService;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private RoleRepository roleRepository;
    private RestaurantService restaurantService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, RoleRepository roleRepository, RestaurantService restaurantService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
        this.restaurantService = restaurantService;
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

    //TODO maybe session logic should be in controller
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
        if (userRoles.contains(RoleConstants.ROLE_MANAGER)) {
            Set<String> myRestaurants = restaurantService.getRestaurantIdsByManagerId(user.getId());
            session.setAttribute(SessionConstants.MY_RESTAURANTS, myRestaurants);
        }

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

    @Override
    public void editPassword(UserEditPasswordModel userEditPasswordModel, Long userId) {
        if (!userEditPasswordModel.getNewPassword().equals(userEditPasswordModel.getRePassword())) {
            throw new PasswordsNotSame(PasswordsNotSame.NEW_PASSWORD_RE_PASSWORD_DONT_MATCH);
        }

        User user = userRepository.findById(userId).orElseThrow(NoSuchUser::new);

        if (user.getPassword().equals(userEditPasswordModel.getNewPassword())) {
            throw new SamePassword();
        }

        if (!user.getPassword().equals(userEditPasswordModel.getOldPassword())) {
            throw new WrongPassword();
        }

        user.setPassword(userEditPasswordModel.getNewPassword());
        userRepository.save(user);
    }

    @Override
    public List<UserUsersViewModel> getUserViewModelsWithoutAdmin(Long adminId, String search, String option) {
        if ("".equals(search) || search == null) {
            return mapToUserUsersViewModels(userRepository.findAll(), modelMapper, adminId);
        } else {
            if ("".equals(option) || option == null) {
                return mapToUserUsersViewModels(userRepository
                        .findAllByNameContainsIgnoreCaseOrEmailContainsIgnoreCase(search, search), modelMapper, adminId);
            } else if ("name".equals(option)) {
                return mapToUserUsersViewModels(userRepository.findAllByNameContainsIgnoreCase(search), modelMapper, adminId);
            } else {
                return mapToUserUsersViewModels(userRepository.findAllByEmailContainsIgnoreCase(search), modelMapper, adminId);
            }
        }
    }

    @Override
    public Boolean validCredentials(Long userId, String userPassword) {
        return userRepository.existsByIdAndPassword(userId, userPassword);
    }

    @Override
    public void updateUser(AdminAction adminAction, Long adminId) {
        User adminUser = userRepository.findById(adminId).orElseThrow(NoSuchUser::new);
        if (!adminUser.getPassword().equals(adminAction.getPassword())) {
            throw new WrongPassword();
        }

        User userToBeChanged = userRepository.findById(adminAction.getUserId()).orElseThrow(NoSuchUser::new);
        Role managerRole = roleRepository.findRoleByName(RoleConstants.ROLE_MANAGER);

        switch (adminAction.getAction()) {
            case "Make manager":
                userToBeChanged.getRoles().add(managerRole);
                userRepository.save(userToBeChanged);
                break;

            case "Make user":
                userToBeChanged.getRoles().remove(managerRole);
                userRepository.save(userToBeChanged);
                break;

            case "Delete":
                userRepository.delete(userToBeChanged);
                break;
        }
    }

    private UserUsersViewModel mapToUserUsersViewModel(User user, ModelMapper modelMapper) {
        UserUsersViewModel userUsersViewModel = modelMapper.map(user, UserUsersViewModel.class);
        userUsersViewModel.setRoles(user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet()));
        return userUsersViewModel;
    }

    private List<UserUsersViewModel> mapToUserUsersViewModels(List<User> users, ModelMapper modelMapper, Long adminId) {
        return users
                .stream()
                .filter(user -> !user.getId().equals(adminId))
                .map(user -> mapToUserUsersViewModel(user, modelMapper))
                .collect(Collectors.toList());
    }
}
