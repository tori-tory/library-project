package exception;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException() {
        super("Пользователь уже существует");
    }
}
