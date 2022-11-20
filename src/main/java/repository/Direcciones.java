package repository;

import domain.Direccion;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.util.List;

public class Direcciones implements WithGlobalEntityManager {
  private static Direcciones instance = new Direcciones();

  private Direcciones() {
  }

  public static Direcciones getInstance() {
    return instance;
  }

  public void agregarDireccion(Direccion direccion) {
    entityManager().persist(direccion);
  }

  public List<Direccion> geDirecciones() {
    return entityManager()
        .createQuery("from Direccion", Direccion.class)
        .getResultList();
  }
}
