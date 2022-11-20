package domain;

import service.impl.ValidadorClave;

public class GestionUsuarios {

  private UsuarioBuilder usuarioBuilder;

  private ValidadorClave validadorClave = new ValidadorClave();

  public void setUsuarioBuilder(UsuarioBuilder usuarioBuilder) {
    this.usuarioBuilder = usuarioBuilder;
  }

  public Usuario getUsuario() {
    return usuarioBuilder.crearUsuario();
  }

  public void crearUsuario(String nombreUsuario, String contrasenia) {
    validadorClave.validarClave(nombreUsuario, contrasenia);
    usuarioBuilder.crearNuevoUsuario();
    usuarioBuilder.cargarNombreUsuario(nombreUsuario);
    usuarioBuilder.cargarcontrasenia(contrasenia);
    usuarioBuilder.cargarTipoUsuario();
  }

}
