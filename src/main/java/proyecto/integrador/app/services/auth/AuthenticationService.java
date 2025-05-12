package proyecto.integrador.app.services.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import proyecto.integrador.app.config.jwt.JwtServiceImpl;
import proyecto.integrador.app.dto.request.RegisterRequestDTO;
import proyecto.integrador.app.dto.response.UserResponseDTO;
import proyecto.integrador.app.entities.User;
import proyecto.integrador.app.entities.enums.Role;
import proyecto.integrador.app.exceptions.BadUserCredentialsException;
import proyecto.integrador.app.exceptions.ObjectNotFoundException;
import proyecto.integrador.app.mappers.UserMapper;
import proyecto.integrador.app.repository.UserRepository;

import java.util.Map;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtServiceImpl jwtService;

    private final UserMapper mapUser;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                 JwtServiceImpl jwtService, UserMapper mapUser) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.mapUser = mapUser;
    }


    public UserResponseDTO saveUser(RegisterRequestDTO registerRequestDTO) throws BadUserCredentialsException {
        if (userRepository.findByEmail(registerRequestDTO.getEmail()).isPresent()){
            throw new BadUserCredentialsException("Ya existe un usuario con este correo: "+ registerRequestDTO.getEmail() + ".");
        }

        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d/*\\-_.º!?¿'¡#!$%&]{6,}$";
        if (!registerRequestDTO.getPassword().matches(passwordRegex)){
            throw new BadUserCredentialsException("La contraseña debe tener al menos 6 caracteres y contener al menos una letra y un número.");
        }
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!registerRequestDTO.getEmail().matches(emailRegex)){
            throw new BadUserCredentialsException("El correo no es valido.");
        }

        registerRequestDTO.setRole(Role.USER);
        registerRequestDTO.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));

        User user = User.builder()
                .email(registerRequestDTO.getEmail())
                .name(registerRequestDTO.getFullName())
                .password(registerRequestDTO.getPassword())
                .role(registerRequestDTO.getRole())
                .build();

        return mapUser.mapUsuario(userRepository.save(user));
    }

    public String generateToken(String username) throws ObjectNotFoundException {
        return jwtService.generateToken(username);
    }

    public Map<String, Object> validateToken(String token){
        return jwtService.validateToken(token);
    }
}
