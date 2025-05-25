package proyecto.integrador.app.services.reports;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import proyecto.integrador.app.dto.request.ReportRequestDTO;
import proyecto.integrador.app.dto.response.ReportResponseDTO;
import proyecto.integrador.app.entities.*;
import proyecto.integrador.app.exceptions.UserNotFoundException;
import proyecto.integrador.app.exceptions.reports.DateException;
import proyecto.integrador.app.exceptions.reports.EventNotFoundException;
import proyecto.integrador.app.exceptions.reports.FileException;
import proyecto.integrador.app.exceptions.reports.ReportNotFoundException;
import proyecto.integrador.app.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import proyecto.integrador.app.services.aws.S3ServiceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Base64;
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
    private S3ServiceImpl s3Service;
    @Value("${aws.s3.bucket-name}")
    private String bucketName;
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
    public ReportResponseDTO createReport(ReportRequestDTO reportRequestDTO, MultipartFile attachmentFile) {
        User user = userRepository.findById(reportRequestDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("El usuario con el id "+reportRequestDTO.getUserId()+" no fue encontrado."));

        Event event = eventRepository.findById(1L)
                .orElseThrow(() -> new EventNotFoundException("Active event not found"));

        LocalDate today = LocalDate.now();
        LocalDate reportDate = reportRequestDTO.getDate();

        if (reportDate.isAfter(today)) {
            throw new DateException("La fecha del reporte no puede ser posterior a hoy.");
        }
        Reports report = new Reports();
        if (attachmentFile != null && !attachmentFile.isEmpty()) {
            try {
                // Convert MultipartFile to temp File
                File tempFile = File.createTempFile("upload-", attachmentFile.getOriginalFilename());
                attachmentFile.transferTo(tempFile);
                Path path = tempFile.toPath();

                // Define S3 key (e.g. "reports/user123/filename.pdf")
                String key = "reports/" + reportRequestDTO.getUserId() + "/" + attachmentFile.getOriginalFilename();

                // Upload to S3
                s3Service.uploadFile(bucketName, key, path);

                // Save key or filename
                report.setAttachment(key);

                // Clean up temp file
                tempFile.delete();


            } catch (IOException e) {
                throw new RuntimeException("No se subio el archivo correctamente: "+e);
            }
        }

        report.setUser(user);
        report.setTitle(reportRequestDTO.getTitle());
        report.setDescription(reportRequestDTO.getDescription());
        report.setDate(reportRequestDTO.getDate());
        report.setType(reportRequestDTO.getType());
        report.setCompanyContactNumber(reportRequestDTO.getCompanyContactNumber());
        report.setUrgency(reportRequestDTO.getUrgency());
        report.setEvent(event);

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
    @Transactional(readOnly = true)
    public List<ReportResponseDTO> getAllReports() {
        return reportRepository.findAllWithAttachmentAndUserEmail();
    }

    // Get a report by ID
    public ReportResponseDTO getReportById(Long id) {
        Reports report = reportRepository.findById(id)
                .orElseThrow(() -> new ReportNotFoundException("Report not found with id: " + id));
        return mapToResponseDTO(report);
    }

    // Update a report
    @Transactional
    public ReportResponseDTO updateReport(Long id, ReportRequestDTO reportRequestDTO, MultipartFile attachmentFile) {
        Reports existingReport = reportRepository.findById(id)
                .orElseThrow(() -> new ReportNotFoundException("Report not found with id: " + id));

        existingReport.setUser(userRepository.findByEmail(reportRequestDTO.getUserEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found")));
        existingReport.setTitle(reportRequestDTO.getTitle());
        existingReport.setDescription(reportRequestDTO.getDescription());
        existingReport.setDate(reportRequestDTO.getDate());
        existingReport.setType(reportRequestDTO.getType());
        existingReport.setCompanyContactNumber(reportRequestDTO.getCompanyContactNumber());
        existingReport.setUrgency(reportRequestDTO.getUrgency());

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
        String attachmentPreSignedUrl = "";
        if (report.getAttachment() == null) {
            attachmentPreSignedUrl = null;
        }else{
            attachmentPreSignedUrl = s3Service.generatePreSignedDownloadUrl(bucketName,report.getAttachment(), Duration.ofMinutes(10));

        }
        return ReportResponseDTO.builder()
                .idReport(report.getIdReport())
                .userEmail(report.getUser().getEmail())
                .title(report.getTitle())
                .description(report.getDescription())
                .date(report.getDate())
                .type(report.getType())
                .companyContactNumber(report.getCompanyContactNumber())
                .urgency(report.getUrgency())
                .attachment(attachmentPreSignedUrl)
                .build();
    }
}
