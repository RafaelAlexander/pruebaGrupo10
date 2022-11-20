package domain;

import domain.archivocsv.*;
import domain.exception.NoCumpleFormatoException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import repository.Direcciones;
import repository.FactoresEmision;
import repository.TiposDeConsumo;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.persistence.NoResultException;

public class BuilderDatoActividadTest extends AbstractPersistenceTest implements WithGlobalEntityManager {
  private Direccion direccionOrigen, direccionDestino;

  @AfterEach
  public void despues() {
    this.rollbackTransaction();
  }

  @BeforeEach
  public void setup(){
    this.beginTransaction();
    direccionOrigen = new Direccion("provincia", "1","maipu","100");
    direccionDestino = new Direccion("provincia","457","O'Higgins","200");
    Direcciones.getInstance().agregarDireccion(direccionOrigen);
    Direcciones.getInstance().agregarDireccion(direccionDestino);
  }

  @Test
  public void verificarExisteTipoConsumoTest() {
    List<Sector> sectores = Collections.singletonList(new Sector("RRHH"));
    Organizacion organizacion = new Organizacion("SA", TipoOrganizacion.EMPRESA, direccionOrigen, sectores,
        ClasificacionOrganizacion.ESCUELA,null);
    FactorEmision factorEmision = new FactorEmision(Unidad.lt,Float.valueOf("12"));
    FactoresEmision.getInstance().agregarFactorEmision(factorEmision);
    TipoDeConsumo tipoDeConsumo = new TipoDeConsumo("Nafta", Unidad.lt, Actividad.CombustionFija, 2);
    TiposDeConsumo tiposDeConsumo = TiposDeConsumo.instancia();
    assertThrows(NoResultException.class, () -> new BuilderDatoActividad(organizacion).lectorCsv("NoNafta.csv"));
  }

  @Test
  public void verificarConsumoTest() {
    List<Sector> sectores = Collections.singletonList(new Sector("RRHH"));
    Organizacion organizacion = new Organizacion(
        "SA",
        TipoOrganizacion.EMPRESA,
        direccionOrigen,
        sectores,
        ClasificacionOrganizacion.ESCUELA,null);
    TiposDeConsumo tiposDeConsumo = TiposDeConsumo.instancia();
    FactorEmision factorEmision = new FactorEmision(Unidad.kg,Float.valueOf("12"));
    FactoresEmision.getInstance().agregarFactorEmision(factorEmision);
    tiposDeConsumo.agregarTipoConsumo(new TipoDeConsumo("Nafta", Unidad.kg, Actividad.CombustionFija, 1));
    assertThrows(NullPointerException.class, () -> new BuilderDatoActividad(organizacion).lectorCsv("NaftaNegativa.csv"));
  }

  @Test
  public void verificarPeriodoDeImputacionTest() {
    List<Sector> sectores = Collections.singletonList(new Sector("RRHH"));
    Organizacion organizacion = new Organizacion("SA", TipoOrganizacion.EMPRESA,
        direccionOrigen, sectores,
        ClasificacionOrganizacion.ESCUELA,null);
    TiposDeConsumo tiposDeConsumo = TiposDeConsumo.instancia();
    FactorEmision factorEmision = new FactorEmision(Unidad.kg,Float.valueOf("12"));
    FactoresEmision.getInstance().agregarFactorEmision(factorEmision);
    tiposDeConsumo.agregarTipoConsumo(new TipoDeConsumo("Nafta", Unidad.kg, Actividad.CombustionFija, 1));
    assertThrows(NoCumpleFormatoException.class, () -> new BuilderDatoActividad(organizacion)
        .lectorCsv("NaftaFechaFormato.csv"));
  }

  @Test
  public void construirTestExitoso() {
    Organizacion organizacion = new Organizacion("SA", TipoOrganizacion.ONG, direccionOrigen, null, ClasificacionOrganizacion.ESCUELA,null);
    TiposDeConsumo tiposDeConsumo = TiposDeConsumo.instancia();
    FactorEmision factorEmision = new FactorEmision(Unidad.lt,Float.valueOf("12"));
    FactoresEmision.getInstance().agregarFactorEmision(factorEmision);
    TipoDeConsumo tipoDeConsumo = new TipoDeConsumo("Nafta", Unidad.lt, Actividad.CombustionFija, 2);
    tiposDeConsumo.agregarTipoConsumo(tipoDeConsumo);
    BuilderDatoActividad builder = new BuilderDatoActividad(organizacion);
    try {
      builder.lectorCsv("Nafta.csv");
    }catch (Exception e){
      throw new NullPointerException();
    }
    assertEquals(true, organizacion.getDatosActividades().size() > 0);
  }
}
