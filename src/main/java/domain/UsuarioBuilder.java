package domain;

public abstract class UsuarioBuilder {

  protected Usuario nuevoUsuario;

  public Usuario crearUsuario() {
    return nuevoUsuario;
  }

  public void crearNuevoUsuario() {
    nuevoUsuario = new Usuario();
  }

  public void cargarNombreUsuario(String nombreUsuario) {
    nuevoUsuario.setNombreUsuario(nombreUsuario);
  }

  public void cargarcontrasenia(String contrasenia) {
    nuevoUsuario.setContrasenia(contrasenia);
  }

  public abstract void cargarTipoUsuario();

}
