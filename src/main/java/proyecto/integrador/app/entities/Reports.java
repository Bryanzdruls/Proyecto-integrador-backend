package proyecto.integrador.app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reports")
public class Reports {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_report")
    private Long idReport;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    private String title;
    private String description;
    private LocalDate date;
    private String type;
    private String companyContactNumber;
    private String urgency;
    private String attachment;
}
