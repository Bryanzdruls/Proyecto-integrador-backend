package proyecto.integrador.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "events")
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;

    @OneToMany(mappedBy = "event")
    @JsonIgnore
    private List<Reports> reports;



    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return (startDate.isBefore(today) || startDate.isEqual(today)) &&
                (endDate.isAfter(today) || endDate.isEqual(today));
    }
}
