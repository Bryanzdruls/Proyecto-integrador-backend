package proyecto.integrador.app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name = "incentives")
@Getter
@Setter
public class Incentive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "report_id", nullable = false)
    private Reports report;

    @ManyToOne
    @JoinColumn(name = "incentive_type_id", nullable = false)
    private IncentiveType incentiveType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)  // Relación con User
    private User user;

    private Double monetaryAmount;
    private Integer pointsAmount;
    private String status;
}
