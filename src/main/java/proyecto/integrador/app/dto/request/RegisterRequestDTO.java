package proyecto.integrador.app.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import proyecto.integrador.app.entities.enums.Role;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RegisterRequestDTO {

    private String fullName;
    private String email;
    private String password;
    private Role role;

}
