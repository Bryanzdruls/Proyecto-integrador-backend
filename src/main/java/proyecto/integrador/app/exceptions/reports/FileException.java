package proyecto.integrador.app.exceptions.reports;

import java.io.IOException;

public class FileException extends RuntimeException{
    public FileException(String message, IOException e) {
        super(message,e);
    }
}
