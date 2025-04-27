package proyecto.integrador.app.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyecto.integrador.app.entities.User;
import proyecto.integrador.app.exceptions.UserNotFoundException;
import proyecto.integrador.app.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Obtener todos los usuarios
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Obtener un usuario por ID
    public User getUserById(Integer id) {
        return userRepository.findById(id.longValue()).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    // Crear un nuevo usuario
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Actualizar un usuario
    public User updateUser(Integer id, User user) {
        if (!userRepository.existsById(id.longValue())) {
            throw new UserNotFoundException("User not found");
        }
        user.setIdUser(id);
        return userRepository.save(user);
    }

    // Eliminar un usuario
    public void deleteUser(Integer id) {
        userRepository.deleteById(id.longValue());
    }

    // Actualizar el puntaje de un usuario
    public User updateUserScore(Integer id, Integer score) {
        User user = getUserById(id);
        user.setScore(user.getScore() == null ? 0 : user.getScore() + score); // Asegúrate de inicializar el puntaje si es null
        return userRepository.save(user);
    }
}
