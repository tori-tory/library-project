package exception;

public class BookNotAvailableCopiesException extends Exception {

  public BookNotAvailableCopiesException() {
      super("Нет доступных экземпляров");
    }
}
