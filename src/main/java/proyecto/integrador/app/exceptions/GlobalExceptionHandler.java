package proyecto.integrador.app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import proyecto.integrador.app.dto.response.ErrorResponse;
import proyecto.integrador.app.exceptions.reports.DateException;
import proyecto.integrador.app.exceptions.reports.EventNotFoundException;
import proyecto.integrador.app.exceptions.reports.FileException;
import proyecto.integrador.app.exceptions.reports.ReportNotFoundException;
import proyecto.integrador.app.exceptions.users.EmailAlreadyExistsException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                false,
                e.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReportNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReportNotFoundException(ReportNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                false,
                e.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEventNotFoundException(EventNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                false,
                e.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileException.class)
    public ResponseEntity<ErrorResponse> handleEventNotFoundException(FileException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                false,
                e.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DateException.class)
    public ResponseEntity<ErrorResponse> handleDateException(DateException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                false,
                e.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleDateException(EmailAlreadyExistsException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                false,
                e.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
