package repository;

import domain.Zona;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.util.List;

public class Zonas implements WithGlobalEntityManager {
  private final static Zonas instance = new Zonas();

  public static Zonas getInstance() {
    return instance;
  }

  private Zonas() {
  }

  public List<Zona> getZonas() {
    return entityManager().createQuery("from Zona", Zona.class).getResultList();
  }

  public void agregarFactorEmision(Zona zona) {
    this.entityManager().persist(zona);
  }
}
