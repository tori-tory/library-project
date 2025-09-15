package exception;

public class UserAlreadyExists extends Exception {
    public UserAlreadyExists() {
        super("Пользователь уже существует");
    }
}
