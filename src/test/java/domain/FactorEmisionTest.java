package domain;

import domain.archivocsv.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import repository.FactoresEmision;
import repository.TiposDeConsumo;

import static org.junit.jupiter.api.Assertions.*;

public class FactorEmisionTest extends AbstractPersistenceTest implements WithGlobalEntityManager {

  @AfterEach
  public void despues() {
    this.rollbackTransaction();
  }

  private Direccion direccionOrigen;
  private TipoDeConsumo tipoDeConsumo;
  private TiposDeConsumo tiposDeConsumo;
  private FactorEmision factorEmision;
  @BeforeEach
  public void setup(){
    this.beginTransaction();
    direccionOrigen = new Direccion("", "1","maipu","100");
    tiposDeConsumo = TiposDeConsumo.instancia();
    factorEmision = new FactorEmision(Unidad.lt,Float.valueOf("12"));
    FactoresEmision.getInstance().agregarFactorEmision(factorEmision);
    tipoDeConsumo = new TipoDeConsumo("Nafta", Unidad.lt, Actividad.CombustionFija, 2);
    tiposDeConsumo.agregarTipoConsumo(tipoDeConsumo);
  }

  private Organizacion crearOrganizacionConActividades() {

    Organizacion organizacion = new Organizacion("SA", TipoOrganizacion.ONG,direccionOrigen, null,
        ClasificacionOrganizacion.ESCUELA,
        null);
    BuilderDatoActividad builder = new BuilderDatoActividad(organizacion);
    try {
      builder.lectorCsv("Nafta.csv");
    }catch (Exception pruebaErronea){
      System.out.println("--------------------------------------");
      System.out.println("--------------------------------------");
      System.out.println(pruebaErronea.getMessage());
      throw new NullPointerException();
    }
    return organizacion;
  }

  @Test
  @Disabled
  public void testOrganizacionConActividadesCreadaCorrectamente() {
    Organizacion organizacion = crearOrganizacionConActividades();
    assertTrue(organizacion.getDatosActividades().size() > 0);
  }

  @Test
  @Disabled
  public void testUnidadConsumoDeDatoActividadDeOrganizacion() {
    DatoActividad datoActividad = crearOrganizacionConActividades().getDatosActividades().get(0);
    assertEquals(Unidad.lt, datoActividad.getTipoDeConsumo().getUnidad());
  }
}
