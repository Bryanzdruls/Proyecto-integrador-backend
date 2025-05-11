package proyecto.integrador.app.repository;

import org.springframework.data.jpa.repository.Query;
import proyecto.integrador.app.dto.response.ReportResponseDTO;
import proyecto.integrador.app.entities.Reports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Reports, Long> {
    @Query("SELECT new proyecto.integrador.app.dto.response.ReportResponseDTO(" +
            "r.idReport, u.email, r.title, r.description, r.date, r.type, " +
            "r.companyContactNumber, r.urgency, r.attachment) " +
            "FROM Reports r JOIN r.user u")
    List<ReportResponseDTO> findAllWithAttachmentAndUserEmail();

}
