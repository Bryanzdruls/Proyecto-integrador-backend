package proyecto.integrador.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import proyecto.integrador.app.entities.Event;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.reports WHERE e.id = :id")
    Optional<Event> findByIdWithReports(@Param("id") Long id);
}
