package domain.exception;

public class UserPasswordEmailException extends RuntimeException {
  public UserPasswordEmailException(String msg) {
    super(msg);
  }
}
