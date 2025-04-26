package proyecto.integrador.app.exceptions;

public class BadUserCredentialsException extends RuntimeException{

    public BadUserCredentialsException(String message){
        super(message);
    }

}
