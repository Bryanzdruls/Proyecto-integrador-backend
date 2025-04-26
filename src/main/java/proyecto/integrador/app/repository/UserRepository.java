package proyecto.integrador.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import proyecto.integrador.app.entities.User;
import proyecto.integrador.app.entities.enums.Role;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u.role FROM User u WHERE u.email = :email")
    Role findRoleByName(String email);
}
