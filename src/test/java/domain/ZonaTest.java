package domain;

import domain.archivocsv.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import repository.*;

import java.time.Month;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ZonaTest extends AbstractPersistenceTest implements WithGlobalEntityManager {
  private static Direccion direccion;
  @AfterEach
  public void despues() {
    this.rollbackTransaction();
  }

  @BeforeEach
  public void setup() {
    this.entityManager().clear();
    this.beginTransaction();
    try {
      crearOrganizacionConActividades1();
      crearOrganizacionConActividades2();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void crearOrganizacionConActividades1() throws Exception {
    direccion = new Direccion("ProvinciaTest", "CABA", "Cobo", "1234");
    Direcciones.getInstance().agregarDireccion(direccion);
    Miembro miembroTest1 = new Miembro("miembroTest1", "miembroTest1", TipoDocumento.DNI, 22222222, direccion);
    Miembro miembroTest2 = new Miembro("miembroTest2", "miembroTest2", TipoDocumento.DNI, 22233333, direccion);
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
    Organizaciones.instancia().agregar(organizacionTest);
    miembroTest1.postularse(organizacionTest, organizacionTest.getSectores().get(0));
    miembroTest2.postularse(organizacionTest, organizacionTest.getSectores().get(0));
    organizacionTest.aceptarPostulante(organizacionTest.getPostulantes().get(0));
    organizacionTest.aceptarPostulante(organizacionTest.getPostulantes().get(1));
    TiposDeConsumo tiposDeConsumo = TiposDeConsumo.instancia();
    FactorEmision factorEmision = new FactorEmision(Unidad.lt, Float.valueOf("12"));
    FactoresEmision.getInstance().agregarFactorEmision(factorEmision);
    TipoDeConsumo tipoDeConsumo = new TipoDeConsumo("Nafta", Unidad.lt, Actividad.CombustionFija, 2);
    tiposDeConsumo.agregarTipoConsumo(tipoDeConsumo);
    BuilderDatoActividad builder = new BuilderDatoActividad(organizacionTest);
      builder.lectorCsv("Nafta.csv");
  }

  private void crearOrganizacionConActividades2() {
    Miembro miembroTest3 = new Miembro("miembroTest3", "miembroTest3", TipoDocumento.DNI, 22222222, direccion);
    Miembro miembroTest4 = new Miembro("miembroTest4", "miembroTest4", TipoDocumento.DNI, 22233333, direccion);
    Miembros.getInstance().agregarMiembro(miembroTest3);
    Miembros.getInstance().agregarMiembro(miembroTest4);
    Sector sectorTest1 = new Sector("RRHH");
    Sector sectorTest2 = new Sector("FINANZAS");
    Organizacion organizacionTest = new Organizacion("SA", TipoOrganizacion.ONG, direccion, new ArrayList<Sector>() {{
      add(sectorTest1);
      add(sectorTest2);
    }}, ClasificacionOrganizacion.ESCUELA, null);
    Organizaciones.instancia().agregar(organizacionTest);
    miembroTest3.postularse(organizacionTest, organizacionTest.getSectores().get(0));
    miembroTest4.postularse(organizacionTest, organizacionTest.getSectores().get(0));
    organizacionTest.aceptarPostulante(organizacionTest.getPostulantes().get(0));
    organizacionTest.aceptarPostulante(organizacionTest.getPostulantes().get(1));
    TiposDeConsumo tiposDeConsumo = TiposDeConsumo.instancia();
    FactorEmision factorEmision = new FactorEmision(Unidad.lt, Float.valueOf("12"));
    FactoresEmision.getInstance().agregarFactorEmision(factorEmision);
    TipoDeConsumo tipoDeConsumo = new TipoDeConsumo("Nafta", Unidad.lt, Actividad.CombustionFija, 2);
    tiposDeConsumo.agregarTipoConsumo(tipoDeConsumo);
    BuilderDatoActividad builder = new BuilderDatoActividad(organizacionTest);
    try {
      builder.lectorCsv("Nafta.csv");
    } catch (Exception e) {
      System.out.println("---------------------------------------------------");
      System.out.println("Test error:" + e.getMessage());
      System.out.println("Test error:" + e.getLocalizedMessage() +" "+ e.getCause() + e);
      throw new NullPointerException();
    }
  }

  private Zona crearZonaProvinciaTest() {
    return new Zona("ProvinciaTest", TipoZona.PROVINCIA);
  }

  private Zona crearZonaLocalidadaTest() {
    return new Zona("LocalidadTest", TipoZona.LOCALIDAD);
  }

  @Test
  public void testZonaProvinciaConOrganizacion() {
    Zona zonaProvincia = crearZonaProvinciaTest();
    assertEquals(true, zonaProvincia.getOrganizacionesZona().size() > 1);
  }

  @Test
  public void testZonaProvinciaConOrganizacionMensualHCDistintoDe0() {
    Zona zonaProvincia = crearZonaProvinciaTest();
    assertEquals(true, zonaProvincia.calcularHCMensual(Month.OCTOBER, 2022) != 0.00);
  }

  @Test
  public void testZonaProvinciaConOrganizacionAnualHCDistintoDe0() {
    Zona zonaProvincia = crearZonaProvinciaTest();
    assertEquals(true, zonaProvincia.calcularHCAnual( 2022) != 0.00);
  }

  @Test
  public void testZonaProvinciaConOrganizacionMensualHCIgual0() {
    Zona zonaProvincia = crearZonaProvinciaTest();
    assertEquals(true, zonaProvincia.calcularHCMensual(Month.APRIL, 2022) == 0.00);
  }

  @Test
  public void testZonaLocalidadConOrganizacionMensualHCIgual0() {
    Zona zonaLocalidad = crearZonaLocalidadaTest();
    assertEquals(true, zonaLocalidad.calcularHCMensual(Month.APRIL, 2022) == 0.00);
  }

  @Test
  public void testZonaProvinciaConOrganizacionAnualHCIgual0() {
    Zona zonaProvincia = crearZonaProvinciaTest();
    assertEquals(true, zonaProvincia.calcularHCAnual( 2023) == 0.00);
  }

  @Test
  public void testZonaLocalidadConOrganizacionAnualHCIgual0() {
    Zona zonaLocalidad = crearZonaLocalidadaTest();
    assertEquals(true, zonaLocalidad.calcularHCAnual(2021) == 0.00);
  }

}
