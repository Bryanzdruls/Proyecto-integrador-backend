package proyecto.integrador.app.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import proyecto.integrador.app.entities.User;
import proyecto.integrador.app.exceptions.UserNotFoundException;
import proyecto.integrador.app.exceptions.users.EmailAlreadyExistsException;
import proyecto.integrador.app.repository.UserRepository;
import proyecto.integrador.app.services.user.xmlreport.XmlReportGenerator;

import java.io.ByteArrayInputStream;
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

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Usuario con el email: "+email+ " no fue encontrado."));
    }

    // Crear un nuevo usuario
    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Ya existe un usuario asociado a este email.");
        }
        return userRepository.save(user);
    }

    // Actualizar un usuario
    public User updateUser(Integer id, User user) {
        User existingUser = userRepository.findById(id.longValue())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Optional<User> userWithSameEmail = userRepository.findByEmail(user.getEmail());
        if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getIdUser().equals(id)) {
            throw new EmailAlreadyExistsException("Ya existe un usuario asociado a este email.");
        }
        existingUser.setIdUser(id);
        existingUser.setEmail(user.getEmail());
        existingUser.setName(user.getName());
        existingUser.setScore(user.getScore());
        existingUser.setRewardValue(user.getRewardValue());
        existingUser.setRole(user.getRole());
        return userRepository.save(existingUser);
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

    public InputStreamResource generateXmlReport() {
        List<User> users = userRepository.findAll();
        try{
            return new InputStreamResource(new XmlReportGenerator().generateUserXmlReport(users));
        }catch (Exception e){
            throw new RuntimeException("Error al generar el reporte");
        }
    }
}
