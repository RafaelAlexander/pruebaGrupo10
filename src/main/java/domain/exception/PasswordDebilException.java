package domain.exception;

public class PasswordDebilException extends RuntimeException {
  public PasswordDebilException(String mensaje) {
    super(mensaje);
  }
}