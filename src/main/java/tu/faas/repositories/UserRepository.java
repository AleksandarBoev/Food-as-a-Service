package tu.faas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tu.faas.domain.entities.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByName(String name);
    boolean existsByEmail(String email);
    boolean existsByNameAndPassword(String name, String password);
    User findUserByName(String name);
    boolean existsByIdAndPassword(Long id, String password);

    List<User> findAllByNameContainsIgnoreCase(String name);
    List<User> findAllByEmailContainsIgnoreCase(String email);
    List<User> findAllByNameContainsIgnoreCaseOrEmailContainsIgnoreCase(String name, String email);
}
