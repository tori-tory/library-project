package exception;

public class LoanNotFoundException extends Exception {
    public LoanNotFoundException() {
      super("Выдачи не найдены");
    }
}
