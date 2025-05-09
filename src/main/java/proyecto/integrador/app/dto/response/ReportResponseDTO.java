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
    private Long idReport;
    private String userEmail; // <- aquí ahora va el email
    private String title;
    private String description;
    private LocalDate date;
    private String type;
    private String companyContactNumber;
    private String urgency;
    //private byte[] attachment;
    private String attachment;

    public ReportResponseDTO(Long idReport, String userEmail, String title, String description, LocalDate date, String type, String companyContactNumber, String urgency) {
        this.idReport = idReport;
        this.userEmail = userEmail;
        this.title = title;
        this.description = description;
        this.date = date;
        this.type = type;
        this.companyContactNumber = companyContactNumber;
        this.urgency = urgency;
    }
}

