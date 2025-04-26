package proyecto.integrador.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponseDTO {

    private Long idReport; // ID of the report
    private Long userId;   // User ID associated with the report
    private String title;
    private String description; // 'desc' renamed to 'description'
    private LocalDate date;
    private String type;
    private String companyContactNumber;
    private String urgency;
    private String attachment;
}
