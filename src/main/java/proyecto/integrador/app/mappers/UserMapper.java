package proyecto.integrador.app.mappers;

import org.springframework.stereotype.Component;
import proyecto.integrador.app.dto.response.UserResponseDTO;
import proyecto.integrador.app.entities.User;

import java.util.List;

@Component
public class UserMapper {

    public UserResponseDTO mapUsuario(User user) {
        if (user == null) {
            return null;
        }
        return UserResponseDTO.builder()
                .idUser(user.getIdUser())
                .role(user.getRole())
                .fullName(user.getName())
                .email(user.getEmail())
                .build();
    }

    public List<UserResponseDTO> mapUsuarioList(List<User> userList) {
        return userList.stream()
                .map(this::mapUsuario)
                .toList();
    }
}
