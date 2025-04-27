package proyecto.integrador.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.integrador.app.entities.Event;
import proyecto.integrador.app.entities.IncentiveType;

public interface IncentiveTypeRepository extends JpaRepository<IncentiveType, Long> {
    IncentiveType findByName(String name);
}
