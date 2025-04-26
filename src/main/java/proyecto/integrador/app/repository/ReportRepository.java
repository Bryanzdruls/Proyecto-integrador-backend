package proyecto.integrador.app.repository;

import proyecto.integrador.app.entities.Reports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Reports, Long> {
}
