package SmartCampusServicesPortal.repository;

import SmartCampusServicesPortal.model.User;
import SmartCampusServicesPortal.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
    List<User> findByFirstNameContainingOrLastNameContaining(String firstName, String lastName);
}
