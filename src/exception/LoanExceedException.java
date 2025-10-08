package exception;

public class LoanExceedException extends Exception {
    public LoanExceedException() {
      super("Не больше трех штук в одни руки!");
    }
}
