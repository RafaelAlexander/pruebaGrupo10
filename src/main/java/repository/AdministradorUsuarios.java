package repository;

import domain.Usuario;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.util.List;

public class AdministradorUsuarios implements WithGlobalEntityManager {

  private static AdministradorUsuarios instance = new AdministradorUsuarios();

  private AdministradorUsuarios() {
  }

  public List<Usuario> listar() {
    return entityManager()
        .createQuery("from Usuario", Usuario.class)
        .getResultList();
  }

  public static AdministradorUsuarios instancia() {
    if (instance == null) {
      instance = new AdministradorUsuarios();
    }
    return instance;
  }

  public void agregarUsuario(Usuario usuario) {
    entityManager().persist(usuario);
  }

  public Usuario obtenerUsuario(Long usuarioId) {
    return entityManager().find(Usuario.class, usuarioId);
  }

  public Usuario obtenerUsuarioPorName(String name){
    return listar().stream().filter(usuario -> usuario.getNombreUsuario().equals(name)).findFirst().orElseThrow(()->new NullPointerException());
  }

}
