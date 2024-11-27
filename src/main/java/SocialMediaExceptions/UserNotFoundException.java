package SocialMediaExceptions;
import java.lang.Exception;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(String message) {
        super(message);
    }
}
