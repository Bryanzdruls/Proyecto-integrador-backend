package proyecto.integrador.app.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequestDTO {

    private Long userId; // User ID (reference to User entity)
    private String title;
    private String userEmail;
    private String description; // 'desc' renamed to 'description'
    private LocalDate date;
    private String type;
    private String companyContactNumber;
    private String urgency;
}
