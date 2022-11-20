package repository;

import domain.Miembro;
import domain.Organizacion;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.util.List;
import java.util.stream.Collectors;

public class Miembros implements WithGlobalEntityManager {
  private final static Miembros instance = new Miembros();

  public static Miembros getInstance() {
    return instance;
  }

  private Miembros() {
  }

  public List<Miembro> getMiembrosList() {
    return entityManager().createQuery("from Miembro").getResultList();
  }

  public void agregarMiembro(Miembro miembro) {
    if(miembro.getId() == null){
      this.entityManager().persist(miembro);
      return;
    }
    this.entityManager().merge(miembro);
  }

  public Miembro getMiembroConNombreUsuario(String nombreUsuario) {
    Miembro miembro = this.getMiembrosList().stream().filter(m -> m.getUsuario().getNombreUsuario().equals(nombreUsuario)).collect(Collectors.toList()).get(0);
    return miembro;
  }

  public Miembro getMiembroConNombreYApellido(String nombre, String apellido) {
    Miembro miembro = this.getMiembrosList()
        .stream()
        .filter(
            unMiembro -> (unMiembro.getNombre().equals(nombre) && unMiembro.getApellido().equals(apellido))
        ).collect(Collectors.toList()).get(0);
    return miembro;
  }

  public Miembro getMiembroByUsuario(Long id) {
    return getMiembrosList().stream().filter(miembro -> miembro.getUsuario().getId().equals(id)).findFirst().orElse(null);
  }
}
