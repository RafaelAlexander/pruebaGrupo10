package persistence;

import domain.TipoZona;
import domain.Zona;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import repository.Zonas;

public class ZonaTest extends AbstractPersistenceTest implements WithGlobalEntityManager {
  @Test
  public void zonaPersistenceTest() {
    Zona zona = new Zona("DD", TipoZona.PROVINCIA);
    Zonas.getInstance().agregarFactorEmision(zona);
    Zona zonaBis = entityManager().find(Zona.class, zona.getId());
    assert(zona.getId() == zonaBis.getId());
  }
}
