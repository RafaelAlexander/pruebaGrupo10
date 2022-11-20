package persistence;

import domain.Coordenada;
import domain.Estacion;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EstacionTest extends AbstractPersistenceTest implements WithGlobalEntityManager {

  @Test
  public void estacionPersistenceTest() {
    Estacion estacion = new Estacion(new Coordenada(1,2));
    entityManager().persist(estacion);
    Estacion estacion1 = entityManager().find(Estacion.class, estacion.getId());
    //TODO: Este assert no podria fallar dado que no estamos forzando el commit?
    assertEquals(estacion1.getId(), estacion.getId());
  }
}
