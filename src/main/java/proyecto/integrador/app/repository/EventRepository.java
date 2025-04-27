package proyecto.integrador.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.integrador.app.entities.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
