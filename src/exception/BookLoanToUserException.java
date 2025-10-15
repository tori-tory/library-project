package exception;

public class BookLoanToUserException extends Exception {
    public BookLoanToUserException() {
        super("Такая же книга уже выдана этому пользователю");
    }
}
