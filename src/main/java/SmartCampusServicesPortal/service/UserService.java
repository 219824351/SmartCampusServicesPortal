package SmartCampusServicesPortal.service;

import SmartCampusServicesPortal.dto.UserDTO;
import SmartCampusServicesPortal.model.User;
import SmartCampusServicesPortal.model.enums.Role;
import SmartCampusServicesPortal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<UserDTO> getUsers(String role, String search) {
        List<User> users;
        if (role != null && !role.isEmpty()) {
            users = userRepository.findByRole(Role.valueOf(role));
        } else if (search != null && !search.isEmpty()) {
            users = userRepository.findByFirstNameContainingOrLastNameContaining(search, search);
        } else {
            users = userRepository.findAll();
        }
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<User> getTechnicians() {
        return userRepository.findByRole(Role.TECHNICIAN);
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void updateUserRole(Long id, Role role) {
        User user = getUserById(id);
        user.setRole(role);
        userRepository.save(user);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole());
        return dto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

