package domain;

import domain.archivocsv.Actividad;
import domain.archivocsv.BuilderDatoActividad;
import domain.archivocsv.TipoDeConsumo;
import domain.archivocsv.Unidad;
import domain.exception.AgenteNoPerteneceZonaException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import repository.Direcciones;
import repository.FactoresEmision;
import repository.Miembros;
import repository.TiposDeConsumo;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AgenteSectorialTest extends AbstractPersistenceTest implements WithGlobalEntityManager {

  private Direccion direccion;
  @AfterEach
  public void despues() {
    this.rollbackTransaction();
  }

  @BeforeEach
  public void setup() {
    this.beginTransaction();
    crearOrganizacionConActividades();
  }

  private void crearOrganizacionConActividades() {
    direccion = new Direccion("ProvinciaTest", "CABA", "Cobo", "1234");
    Direcciones.getInstance().agregarDireccion(direccion);
    Miembro miembroTest1 = new Miembro("Sanchez",
        "Juan",
        TipoDocumento.DNI,
        22222222,
        direccion);
    Miembro miembroTest2 = new Miembro("Picapiedra",
        "Pedro",
        TipoDocumento.DNI,
        22233333,
        direccion);
    Miembros.getInstance().agregarMiembro(miembroTest1);
    Miembros.getInstance().agregarMiembro(miembroTest2);
    Sector sectorTest1 = new Sector("RRHH");
    Sector sectorTest2 = new Sector("FINANZAS");
    Organizacion organizacionTest = new Organizacion("SA",
        TipoOrganizacion.ONG,
        direccion,
        new ArrayList<Sector>() {{
          add(sectorTest1);
          add(sectorTest2);
        }},
        ClasificacionOrganizacion.ESCUELA, null);
    miembroTest1.postularse(organizacionTest, organizacionTest.getSectores().get(0));
    miembroTest2.postularse(organizacionTest, organizacionTest.getSectores().get(0));
    organizacionTest.aceptarPostulante(organizacionTest.getPostulantes().get(0));
    organizacionTest.aceptarPostulante(organizacionTest.getPostulantes().get(0));
    TiposDeConsumo tiposDeConsumo = TiposDeConsumo.instancia();
    FactorEmision factorEmision = new FactorEmision(Unidad.lt, Float.valueOf("12"));
    FactoresEmision.getInstance().agregarFactorEmision(factorEmision);
    TipoDeConsumo tipoDeConsumo = new TipoDeConsumo("Nafta", Unidad.lt, Actividad.CombustionFija, 2);
    tiposDeConsumo.agregarTipoConsumo(tipoDeConsumo);
    BuilderDatoActividad builder = new BuilderDatoActividad(organizacionTest);
    try {
      builder.lectorCsv("Nafta.csv");
    } catch (Exception e) {
      throw new NullPointerException();
    }
  }

  private Zona crearZonaProvinciaTest() {
    return new Zona("ProvinciaTest", TipoZona.PROVINCIA);
  }

  private AgenteSectorial crearAgenteSectorialQueNoPerteneceTest() {
    return new AgenteSectorial(
        "ApellidoTest",
        "NombreTest",
        TipoDocumento.DNI,
        12345678,
        new Direccion("ProvinciaERROR", "CABA", "Cobo", "1234")
    );
  }

  private AgenteSectorial crearAgenteSectorialQuePerteneceTest() {
    return new AgenteSectorial(
        "ApellidoTest",
        "NombreTest",
        TipoDocumento.DNI,
        12345678,
        direccion
    );
  }

  @Test
  public void testAgregarZonaAAgenteSectorialQueNoPerteneceALaZona() {
    Zona zonaProvincia = crearZonaProvinciaTest();
    AgenteSectorial agenteSectorialTest = crearAgenteSectorialQueNoPerteneceTest();
    assertThrows(AgenteNoPerteneceZonaException.class, () ->
        agenteSectorialTest.asociarZona(zonaProvincia)
    );
  }

  @Test
  public void testAgregarZonaAAgenteSectorial() {
    Zona zonaProvincia = crearZonaProvinciaTest();
    AgenteSectorial agenteSectorialTest = crearAgenteSectorialQuePerteneceTest();
    agenteSectorialTest.asociarZona(zonaProvincia);
    assertEquals(true, agenteSectorialTest.getZona() != null);
  }

}
