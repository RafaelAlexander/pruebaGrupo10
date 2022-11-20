package domain;

import domain.exception.PasswordDebilException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UsuarioYClaveTest {

  private GestionUsuarios gestionadorUsuarioAdministrador(){
    GestionUsuarios gestionadorUsuarioAdmin = new GestionUsuarios();
    UsuarioBuilder usuarioAdministradorBuilder = new UsuarioAdministrador();
    gestionadorUsuarioAdmin.setUsuarioBuilder(usuarioAdministradorBuilder);
    return gestionadorUsuarioAdmin;
  }
  private GestionUsuarios gestionadorUsuarioEstandar(){
    GestionUsuarios gestionadorUsuarioEstandar = new GestionUsuarios();
    UsuarioBuilder usuarioEstandarBuilder = new UsuarioEstandar();
    gestionadorUsuarioEstandar.setUsuarioBuilder(usuarioEstandarBuilder);
    return gestionadorUsuarioEstandar;
  }
  private Usuario usuarioAdministradorCorrecto() {
    GestionUsuarios gestionadorUsuarioAdmin = new GestionUsuarios();
    UsuarioBuilder usuarioAdministradorBuilder = new UsuarioAdministrador();
    gestionadorUsuarioAdmin.setUsuarioBuilder(usuarioAdministradorBuilder);
    gestionadorUsuarioAdmin.crearUsuario("uCorrecto","administradorcorrecto");
    return gestionadorUsuarioAdmin.getUsuario();
  }

  @Test
  public void claveTieneSecuenciaDeLetrasYNumeros() {
    assertThrows(PasswordDebilException.class, () ->
        gestionadorUsuarioEstandar().crearUsuario("uPassSecuenciaMixta", "abc12345")
    );
  }

  @Test
  public void claveTieneSecuenciaDeLetras() {
    assertThrows(PasswordDebilException.class, () ->
        gestionadorUsuarioEstandar().crearUsuario("uPassSecuenciaLetras", "bcdefghij")
    );
  }

  @Test
  public void claveTieneSecuenciaDeNumeros() {
    assertThrows(PasswordDebilException.class, () ->
        gestionadorUsuarioEstandar().crearUsuario("uPassSecuenciaNumeros", "23456789")
    );
  }

  @Test
  public void claveDeCaracteresRepetidos() {
    assertThrows(PasswordDebilException.class, () ->
        gestionadorUsuarioEstandar().crearUsuario("uPassrepeticion", "bbbbbbbb")
    );
  }

  @Test
  public void claveTienePalabraProhibida() {
    assertThrows(PasswordDebilException.class, () ->
        gestionadorUsuarioEstandar().crearUsuario("uPassPalabraProhibida", "huella")
    );
  }

  @Test
  public void claveMasCortaQueLoPermitido() {
    assertThrows(PasswordDebilException.class, () ->
        gestionadorUsuarioEstandar().crearUsuario("uClaveCorta", "clave")
    );
  }

  @Test
  public void claveIgualAlUsuario() {
    assertThrows(PasswordDebilException.class, () ->
        gestionadorUsuarioEstandar().crearUsuario("uClaveIgualUsuario", "uClaveIgualUsuario")
    );
  }

  @Test
  public void testPasswordEstaEnTop10000() {
    assertThrows(PasswordDebilException.class, () ->
        gestionadorUsuarioEstandar().crearUsuario("uClaveTop10000", "password")
    );
  }

  @Test
  public void claveCorrecta() {
    Usuario usuarioCorrecto = usuarioAdministradorCorrecto();
    assertEquals(TipoUsuario.ADMINISTRADOR, usuarioCorrecto.getTipoUsuario());
  }

}
