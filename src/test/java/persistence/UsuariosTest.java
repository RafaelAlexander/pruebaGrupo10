package persistence;

import domain.*;
import domain.exception.PasswordDebilException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import repository.AdministradorUsuarios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UsuariosTest extends AbstractPersistenceTest implements WithGlobalEntityManager {
  /**@AfterEach
  public void limpiar() {
    this.rollbackTransaction();
  }

  @BeforeEach
  public void setup() {
    this.beginTransaction();
  }

  private Usuario usuarioAdministradorCorrecto() {
    GestionUsuarios gestionadorUsuarioAdmin = new GestionUsuarios();
    UsuarioBuilder usuarioAdministradorBuilder = new UsuarioAdministrador();
    gestionadorUsuarioAdmin.setUsuarioBuilder(usuarioAdministradorBuilder);
    gestionadorUsuarioAdmin.crearUsuario("uCorrecto","administradorcorrecto");
    Usuario usuarioCreado = gestionadorUsuarioAdmin.getUsuario();
    entityManager().persist(usuarioCreado);
    return usuarioCreado;
  }

  @Test
  public void testPersistenciaDeUsuario() {
    Usuario usuarioInicial = usuarioAdministradorCorrecto();
    AdministradorUsuarios administradorUsuarios = AdministradorUsuarios.getInstance();
    administradorUsuarios.agregarUsuario(usuarioInicial);
    Usuario usuarioObtenido = administradorUsuarios.obtenerUsuario(usuarioInicial.getId());
    assertEquals(usuarioInicial.getNombreUsuario(), usuarioObtenido.getNombreUsuario());
  }**/
}
