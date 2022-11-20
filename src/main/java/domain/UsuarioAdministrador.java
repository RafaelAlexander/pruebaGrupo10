package domain;

public class UsuarioAdministrador extends UsuarioBuilder {

  @Override
  public void cargarTipoUsuario() {
    nuevoUsuario.setTipoUsuario(TipoUsuario.ADMINISTRADOR);
  }

}
