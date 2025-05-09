package proyecto.integrador.app.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import proyecto.integrador.app.dto.request.ReportRequestDTO;
import proyecto.integrador.app.dto.response.ReportResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.integrador.app.dto.response.SuccessResponse;
import proyecto.integrador.app.exceptions.UserNotFoundException;
import proyecto.integrador.app.services.reports.ReportService;

import java.util.List;

@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "*", allowedHeaders = {"Authorization", "Content-Type"})
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // Create a new report
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<ReportResponseDTO>> createReport(
            @RequestPart("report") ReportRequestDTO reportRequestDTO,
            @RequestPart(value = "attachment", required = false) MultipartFile attachmentFile
    ) {
        ReportResponseDTO createdReport = reportService.createReport(reportRequestDTO, attachmentFile);
        SuccessResponse<ReportResponseDTO> response = new SuccessResponse<>(true, createdReport);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }



    // Get all reports
    @GetMapping
    public ResponseEntity<SuccessResponse<List<ReportResponseDTO>>> getAllReports() {
        List<ReportResponseDTO> reports = reportService.getAllReports();
        SuccessResponse<List<ReportResponseDTO>> response = new SuccessResponse<>(true, reports);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Get a report by its ID
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<ReportResponseDTO>> getReportById(@PathVariable("id") Long id) {
        ReportResponseDTO report = reportService.getReportById(id);
        SuccessResponse<ReportResponseDTO> response = new SuccessResponse<>(true, report);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // Update a report
    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<ReportResponseDTO>> updateReport(@PathVariable("id") Long id, @RequestBody ReportRequestDTO reportRequestDTO,@RequestPart(value = "attachment", required = false) MultipartFile attachmentFile) {
        ReportResponseDTO updatedReport = reportService.updateReport(id, reportRequestDTO, attachmentFile);
        SuccessResponse<ReportResponseDTO> response = new SuccessResponse<>(true, updatedReport);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Delete a report
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<ReportResponseDTO>> deleteReport(@PathVariable("id") Long id) {
        ReportResponseDTO deletedReport = reportService.deleteReport(id);
        SuccessResponse<ReportResponseDTO> response = new SuccessResponse<>(true, deletedReport);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
