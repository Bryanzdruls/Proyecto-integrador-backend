package proyecto.integrador.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import proyecto.integrador.app.entities.enums.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponseDTO {

    private String accessToken;
    private String refreshToken;
    private Role role;

}
