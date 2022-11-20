package domain.exception;

public class MiembroSinSectorEnUnaOrganizacion extends RuntimeException {
  public MiembroSinSectorEnUnaOrganizacion(String detalleError) {
    super(detalleError);
  }
}
