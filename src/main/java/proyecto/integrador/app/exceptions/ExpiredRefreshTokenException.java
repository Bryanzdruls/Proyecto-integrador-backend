package proyecto.integrador.app.exceptions;

public class ExpiredRefreshTokenException extends RuntimeException{

    public ExpiredRefreshTokenException(String message){
        super(message);
    }

}
