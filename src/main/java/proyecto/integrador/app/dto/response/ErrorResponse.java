package proyecto.integrador.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private boolean ok;
    private String message;
    private int status;
}
