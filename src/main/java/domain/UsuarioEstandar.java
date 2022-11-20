package domain;

public class UsuarioEstandar extends UsuarioBuilder {

  @Override
  public void cargarTipoUsuario() {
    nuevoUsuario.setTipoUsuario(TipoUsuario.ESTANDAR);
  }

}
