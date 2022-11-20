package persistence;

import domain.*;
import domain.archivocsv.Actividad;
import domain.archivocsv.TipoDeConsumo;
import domain.archivocsv.Unidad;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import repository.FactoresEmision;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransportePublicoTest extends AbstractPersistenceTest implements WithGlobalEntityManager {
  static Estacion luganoPer = lugano();
  static Estacion simpreVivaPer = simpreViva();
  static Estacion belgranoPer = belgrano();

  @Test
  public void insertarTransportePublicoTest() {
    List<Estacion> estacionesDeEjemplo = new ArrayList<Estacion>();
    estacionesDeEjemplo.add(luganoPer);
    estacionesDeEjemplo.add(simpreVivaPer);
    estacionesDeEjemplo.add(belgranoPer);
    Recorrido belgranoVuelta = new Recorrido(estacionesDeEjemplo, "Belgrano Vuelta");
    Linea linea114 = linea114(belgranoVuelta);
    TransportePublico transportePublico = new TransportePublico(luganoPer,belgranoPer,linea114);
    entityManager().persist(transportePublico);

    TransportePublico tpBis = entityManager().find(TransportePublico.class, transportePublico.getId());
    assertEquals(transportePublico.getId(), tpBis.getId());
  }

  public static Estacion lugano() {
    Coordenada coordena = new Coordenada(1111.0, 2222.0);
    Estacion lugano = new Estacion(coordena);
    return lugano;
  }

  public static Estacion simpreViva(){
    Coordenada coordena = new Coordenada(12222.0, 2222.0);
    Estacion lugano = new Estacion(coordena);
    return lugano;
  }

  public static Estacion belgrano() {
    Coordenada coordena = new Coordenada(4444.0, 5555.0);
    Estacion belgrano = new Estacion(coordena);
    return belgrano;
  }

  public Linea linea114(Recorrido recorrido) {
    FactorEmision factorEmision = new FactorEmision(Unidad.km,1);
    FactoresEmision.getInstance().agregarFactorEmision(factorEmision);
    TipoDeConsumo tipoDeConsumo = new TipoDeConsumo("Bondi por km", Unidad.km, Actividad.CombustionFija,3);
    Linea linea114 = new Linea(TipoLinea.COLECTIVO, Arrays.asList(recorrido),tipoDeConsumo);
    return linea114;
  }
}