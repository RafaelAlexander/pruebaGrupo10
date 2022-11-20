package repository;

import domain.recomendaciones.Recomendacion;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.util.List;

public class Recomendaciones implements WithGlobalEntityManager {
  private final static Recomendaciones instance = new Recomendaciones();

  public static Recomendaciones instancia() {
    return instance;
  }

  private Recomendaciones() {
  }

  public void agregarRecomendacion(Recomendacion recomendacion){
    entityManager().persist(recomendacion);
  }

  public List<Recomendacion> obtenerRecomendaciones() {
    return entityManager().createQuery("from Recomendacion",Recomendacion.class).getResultList();
  }
}
