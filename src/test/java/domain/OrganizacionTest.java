package domain;

import domain.archivocsv.Actividad;
import domain.archivocsv.TipoDeConsumo;
import domain.archivocsv.Unidad;
import domain.exception.PostulanteNoInscriptoException;
import domain.exception.SectorAjenoException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import repository.*;
import service.CalculadorDeDistancia;
import service.impl.ServiceLocator;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class OrganizacionTest extends AbstractPersistenceTest implements WithGlobalEntityManager {
  private Direccion direccionOrigen, direccionDestino;
  private TipoDeConsumo tipoDeConsumo;


  @AfterEach
  public void despues() {
    this.rollbackTransaction();
  }

  @BeforeEach
  public void setup(){
    this.entityManager().clear();
    this.beginTransaction();
    CalculadorDeDistancia calculadorDeDistancia = mock(CalculadorDeDistancia.class);
    ServiceLocator.obtenerInstancia().agregarServicioDeDistancia(calculadorDeDistancia);
    direccionOrigen = new Direccion("provincia","1","maipu","100");
    entityManager().persist(direccionOrigen);
    direccionDestino = new Direccion("provincia","457","O'Higgins","200");
    entityManager().persist(direccionDestino);
    FactorEmision factorEmision = new FactorEmision(Unidad.lt, 100);
    FactoresEmision.getInstance().agregarFactorEmision(factorEmision);
    tipoDeConsumo = new TipoDeConsumo("Bondi por km", Unidad.km, Actividad.CombustionFija,3);
    entityManager().persist(tipoDeConsumo);
  }

  @Test
  public void creacionOrganizacionTest() {
    List<Sector> sectores = Collections.singletonList(new Sector("RRHH"));
    Direccion direccionOrg = new Direccion("provincia","CABA","Cobo","1234");
    Direcciones.getInstance().agregarDireccion(direccionOrg);
    Organizacion organizacion = new Organizacion("SA",
        TipoOrganizacion.EMPRESA,
        direccionOrg,
        sectores,
        ClasificacionOrganizacion.ESCUELA,
        null);
    assert(organizacion.getSectores().size() == 1);
    assertEquals(organizacion.getSectores().get(0).getId(), sectores.get(0).getId());
  }
  @Test
  public void aceptarPostulanteExitoTest(){
    List<Sector> sectores = Collections.singletonList(new Sector("RRHH"));
    Direccion direccionOrg = new Direccion("provincia","CABA","Cobo","1234");
    Direcciones.getInstance().agregarDireccion(direccionOrg);
    Organizacion organizacion = new Organizacion("SA", TipoOrganizacion.EMPRESA,
        direccionOrg, sectores,
        ClasificacionOrganizacion.ESCUELA, null);
    Miembro miembro = new Miembro("Maradona", "Diego",TipoDocumento.DNI,111,direccionOrg);
    Miembros.getInstance().agregarMiembro(miembro);
    Postulante postulante = new Postulante(miembro,sectores.get(0));
    organizacion.agregarPostulante(postulante);
    organizacion.aceptarPostulante(postulante);
    entityManager().persist(organizacion);
    assertTrue(organizacion.getPostulantes().contains(postulante));
  }

  @Test
  public void aceptarPostulanteErrorTest(){
    List<Sector> sectores = Collections.singletonList(new Sector("RRHH"));
    Organizacion organizacion = new Organizacion("SA", TipoOrganizacion.EMPRESA,
        new Direccion("provincia","CABA","Cobo","1234"), sectores,
        ClasificacionOrganizacion.ESCUELA, null);
    Miembro miembro = new Miembro("Maradona", "Diego",TipoDocumento.DNI,111,new Direccion("provincia","CABA","Cobo","1234"));
    Miembros.getInstance().agregarMiembro(miembro);
    Postulante postulante = new Postulante(miembro,sectores.get(0));
    Throwable exception = assertThrows(PostulanteNoInscriptoException.class, () -> organizacion.aceptarPostulante(postulante));
    assertEquals("Solicitud no encontrada en la organizacion", exception.getMessage());
  }

  @Test
  public void validarSectorTest(){
    List<Sector> sectores = Collections.singletonList(new Sector("RRHH"));
    Organizacion organizacion = new Organizacion("SA", TipoOrganizacion.EMPRESA,
        new Direccion("provincia","CABA","Cobo","1234"), sectores,
        ClasificacionOrganizacion.ESCUELA, null);
    Throwable exception = assertThrows(SectorAjenoException.class, () -> organizacion.validarSector(null));
    assertEquals("Sector no pertenece a organizacion",exception.getMessage());
  }

  @Test
  public void getHCTest(){
    List<Sector> sectores = Collections.singletonList(new Sector("RRHH"));
    Direccion direccionOrg = new Direccion("provincia","CABA","Cobo","1234");
    Direcciones.getInstance().agregarDireccion(direccionOrg);
    Organizacion organizacion = new Organizacion("SA", TipoOrganizacion.EMPRESA,
        direccionOrg, sectores,
        ClasificacionOrganizacion.ESCUELA, null);
    Miembro miembro = new Miembro("Maradona",
        "Diego",
        TipoDocumento.DNI,
        111,
        direccionOrg);
    Miembros.getInstance().agregarMiembro(miembro);

    Pie caminandoHaciaUTN = new Pie(direccionOrigen, direccionDestino);

    List<Estacion> estacionesDeEjemplo = new ArrayList<>();
    estacionesDeEjemplo.add(lugano());
    estacionesDeEjemplo.add(simpreViva());
    estacionesDeEjemplo.add(belgrano());
    Recorrido belgranoVuelta = new Recorrido(estacionesDeEjemplo, "Belgrano Vuelta");
    Linea l114 = linea114(belgranoVuelta, tipoDeConsumo);
    entityManager().persist(l114);

    TransportePublico transportePublico = new TransportePublico(l114.getRecorridos().get(0).getEstacionList().get(0), l114.getRecorridos().get(0).getEstacionList().get(2), l114);
    Tramo tramo1 = new Tramo(organizacion,transportePublico);
    Tramo tramo2 = new Tramo(organizacion,caminandoHaciaUTN);

    Trayecto trayecto = new Trayecto("",Arrays.asList(tramo1,tramo2), LocalDate.now());

    miembro.agregarTrayecto(trayecto);

    Postulante postulante = new Postulante(miembro,sectores.get(0));
    organizacion.agregarPostulante(postulante);
    organizacion.aceptarPostulante(postulante);
    entityManager().persist(organizacion);
    organizacion = Organizaciones.instancia().getOrganizacionesList().get(0);
    assertEquals(0, organizacion.getHuellaCarbonoGeneralAnual(2021));
    assertNotEquals(0, organizacion.getHuellaCarbonoGeneralMensual(Month.of(LocalDate.now().getMonthValue()), LocalDate.now().getYear()));
  }


  public static Estacion lugano() {
    return new Estacion(new Coordenada(1111.0, 2222.0));
  }

  public static Estacion simpreViva(){
    return new Estacion(new Coordenada(12222.0, 2222.0));
  }

  public static Estacion belgrano() {
    return new Estacion(new Coordenada(4444.0, 5555.0));
  }


  public static Linea linea114(Recorrido recorrido, TipoDeConsumo tipoDeConsumo) {
    FactorEmision factorEmision = new FactorEmision(Unidad.km,1);
    FactoresEmision.getInstance().agregarFactorEmision(factorEmision);
    return new Linea(TipoLinea.COLECTIVO, Collections.singletonList(recorrido),tipoDeConsumo);
  }
}
