package domain;

import domain.archivocsv.Actividad;
import domain.archivocsv.TipoDeConsumo;
import domain.archivocsv.Unidad;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import repository.EmpresaTransportePublico;
import repository.FactoresEmision;
import repository.TiposDeConsumo;


public class TransportePublicoTest extends AbstractPersistenceTest implements WithGlobalEntityManager {
  static Estacion luganoPer;
  static Estacion simpreVivaPer;
  static Estacion belgranoPer;

  @AfterEach
  public void afterTest() {
    this.rollbackTransaction();
  }

  @BeforeEach
  public void setup(){
    this.entityManager().clear();
    this.beginTransaction();
    luganoPer = lugano();
    simpreVivaPer = simpreViva();
    belgranoPer = belgrano();
  }
  @Test
  public void posicionXEstacion() {
    Estacion estacionLugano = lugano();
    assertEquals(estacionLugano.getCoordenada().getLatitud(), 1111.0, 0.0);
  }

  @Test
  public void posicionYEstacion() {
    Estacion estacionLugano = lugano();
    assertEquals(estacionLugano.getCoordenada().getLongitud(), 2222.0, 0.0);
  }

  @Test
  public void listaEstaciones114() {
    Estacion lugano = lugano();
    Estacion belgrano = belgrano();
    List<Estacion> estacionesDeEjemplo = new ArrayList<Estacion>() {{
      add(lugano);
      add(belgrano);
    }};
    Recorrido belgranoVuelta = new Recorrido(estacionesDeEjemplo, "Belgrano Vuelta");
    Linea linea114 = linea114(belgranoVuelta);
    assertEquals(linea114.getRecorridos().contains(belgranoVuelta), true);
  }

  @Test void obtenerDistanciaTest(){
    List<Estacion> estacionesDeEjemplo = new ArrayList<>();
    estacionesDeEjemplo.add(luganoPer);
    estacionesDeEjemplo.add(simpreVivaPer);
    estacionesDeEjemplo.add(belgranoPer);
    Recorrido belgranoVuelta = new Recorrido(estacionesDeEjemplo, "Belgrano Vuelta");
    Linea linea114 = linea114(belgranoVuelta);
    assertEquals(19573.043074813555,linea114.getDistancia(luganoPer, belgranoPer));
  }

  @Test void obtenerHCTest(){
    List<Estacion> estacionesDeEjemplo = new ArrayList<Estacion>();
    estacionesDeEjemplo.add(luganoPer);
    estacionesDeEjemplo.add(simpreVivaPer);
    estacionesDeEjemplo.add(belgranoPer);
    Recorrido belgranoVuelta = new Recorrido(estacionesDeEjemplo, "Belgrano Vuelta");
    Linea linea114 = linea114(belgranoVuelta);
    TransportePublico transportePublico = new TransportePublico(luganoPer,belgranoPer,linea114);
    assertEquals(19573.043074813555,transportePublico.getHuellaDeCarbono());
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
    entityManager().persist(tipoDeConsumo);
    Linea linea114 = new Linea(TipoLinea.COLECTIVO, Arrays.asList(recorrido),tipoDeConsumo);
    EmpresaTransportePublico ep = new EmpresaTransportePublico();
    ep.agregarLinea(linea114);
    return linea114;
  }

}
