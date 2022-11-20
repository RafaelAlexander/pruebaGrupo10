package domain.exception;

public class TrayectoNoPerteneceAMiembro extends RuntimeException {
  public TrayectoNoPerteneceAMiembro(String msg) {
    super(msg);
  }
}