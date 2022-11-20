package domain.exception;

public class NoExisteEnDominioException extends RuntimeException {
  public NoExisteEnDominioException(String msg) {
    super(msg);
  }
}
