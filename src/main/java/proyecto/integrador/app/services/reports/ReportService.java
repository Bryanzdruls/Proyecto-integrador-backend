package proyecto.integrador.app.services.reports;

import proyecto.integrador.app.dto.request.ReportRequestDTO;
import proyecto.integrador.app.dto.response.ReportResponseDTO;
import proyecto.integrador.app.entities.*;
import proyecto.integrador.app.exceptions.UserNotFoundException;
import proyecto.integrador.app.exceptions.reports.EventNotFoundException;
import proyecto.integrador.app.exceptions.reports.ReportNotFoundException;
import proyecto.integrador.app.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository; // 👈 Agregado
    private final IncentiveTypeRepository incentiveTypeRepository;
    private final IncentiveRepository incentiveRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository, UserRepository userRepository, EventRepository eventRepository, IncentiveTypeRepository incentiveTypeRepository,IncentiveRepository incentiveRepository) {
        this.userRepository = userRepository;
        this.reportRepository = reportRepository;
        this.eventRepository = eventRepository; // 👈 Asignado
        this.incentiveTypeRepository = incentiveTypeRepository;
        this.incentiveRepository =incentiveRepository;
    }

    // Create a new report
    @Transactional
    public ReportResponseDTO createReport(ReportRequestDTO reportRequestDTO) {
        User user = userRepository.findById(reportRequestDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Event event = eventRepository.findById(1L)
                .orElseThrow(() -> new EventNotFoundException("Active event not found"));

        Reports report = new Reports();
        report.setUser(user);
        report.setTitle(reportRequestDTO.getTitle());
        report.setDescription(reportRequestDTO.getDescription());
        report.setDate(reportRequestDTO.getDate());
        report.setType(reportRequestDTO.getType());
        report.setCompanyContactNumber(reportRequestDTO.getCompanyContactNumber());
        report.setUrgency(reportRequestDTO.getUrgency());
        report.setAttachment(reportRequestDTO.getAttachment());
        report.setEvent(event); // 👈 Asociamos el evento al reporte

        Reports savedReport = reportRepository.save(report);
        if (event.isActive()) {
            // Obtener el tipo de incentivo (por ejemplo, por nombre o id)
            IncentiveType incentiveType = incentiveTypeRepository.findByName("POINTS");  // Ejemplo de tipo de incentivo

            Incentive incentive = new Incentive();
            incentive.setReport(savedReport);
            incentive.setIncentiveType(incentiveType);  // Asociar el tipo de incentivo
            incentive.setUser(user);  // Asociar el usuario que generó el reporte
            incentive.setPointsAmount(10);

            if (user.getScore() == null) {
                user.setScore(0);
            }
            user.setScore(user.getScore()+10);
            incentive.setStatus("ACTIVE");
            userRepository.save(user);
            incentive.setStatus("DONE");
            incentiveRepository.save(incentive);
        }

        return mapToResponseDTO(savedReport);
    }

    // Get all reports
    public List<ReportResponseDTO> getAllReports() {
        List<Reports> reports = reportRepository.findAll();
        return reports.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Get a report by ID
    public ReportResponseDTO getReportById(Long id) {
        Reports report = reportRepository.findById(id)
                .orElseThrow(() -> new ReportNotFoundException("Report not found with id: " + id));
        return mapToResponseDTO(report);
    }

    // Update a report
    @Transactional
    public ReportResponseDTO updateReport(Long id, ReportRequestDTO reportRequestDTO) {
        Reports existingReport = reportRepository.findById(id)
                .orElseThrow(() -> new ReportNotFoundException("Report not found with id: " + id));

        existingReport.setUser(userRepository.findById(reportRequestDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found")));
        existingReport.setTitle(reportRequestDTO.getTitle());
        existingReport.setDescription(reportRequestDTO.getDescription());
        existingReport.setDate(reportRequestDTO.getDate());
        existingReport.setType(reportRequestDTO.getType());
        existingReport.setCompanyContactNumber(reportRequestDTO.getCompanyContactNumber());
        existingReport.setUrgency(reportRequestDTO.getUrgency());
        existingReport.setAttachment(reportRequestDTO.getAttachment());

        Reports updatedReport = reportRepository.save(existingReport);
        return mapToResponseDTO(updatedReport);
    }

    // Delete a report
    @Transactional
    public ReportResponseDTO deleteReport(Long id) {
        Reports existingReport = reportRepository.findById(id)
                .orElseThrow(() -> new ReportNotFoundException("Report not found with id: " + id));

        reportRepository.delete(existingReport);
        return mapToResponseDTO(existingReport);
    }

    // Helper method to map from Report entity to ReportResponseDTO
    private ReportResponseDTO mapToResponseDTO(Reports report) {
        return ReportResponseDTO.builder()
                .idReport(report.getIdReport())
                .userId(Long.valueOf(report.getUser().getIdUser()))
                .title(report.getTitle())
                .description(report.getDescription())
                .date(report.getDate())
                .type(report.getType())
                .companyContactNumber(report.getCompanyContactNumber())
                .urgency(report.getUrgency())
                .attachment(report.getAttachment())
                .build();
    }
}
