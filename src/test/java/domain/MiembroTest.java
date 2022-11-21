package domain;

import domain.archivocsv.Actividad;
import domain.archivocsv.TipoDeConsumo;
import domain.archivocsv.Unidad;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import repository.FactoresEmision;
import repository.Organizaciones;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MiembroTest extends AbstractPersistenceTest implements WithGlobalEntityManager {



  private Sector rrhh;
  private Sector finanzas;
  private Organizacion techint;
  private Miembro juan;
  private Miembro pedro;
  private Organizaciones organizaciones;
  private TipoDeConsumo tipoDeConsumo;
  private FactorEmision factorEmision;

  @AfterEach
  public void limpiar() {
    this.rollbackTransaction();
  }

  @BeforeEach
  public void setup(){
    this.beginTransaction();
    factorEmision = new FactorEmision(Unidad.lt,100);
    FactoresEmision.getInstance().agregarFactorEmision(factorEmision);
    tipoDeConsumo = new TipoDeConsumo("Transporte", Unidad.lt, Actividad.CombustiónMóvil,100);
    rrhh = new Sector("RRHH");
    finanzas = new Sector("FINANZAS");
    techint = new Organizacion("Techint",
        TipoOrganizacion.EMPRESA,
        new Direccion("provincia","CABA","Cobo","1234"),
        new ArrayList<Sector>(){{add(rrhh);add(finanzas);}},
        ClasificacionOrganizacion.EMPRESA_SECTOR_SECUNDARIO, null);
    juan = new Miembro("Sanchez", "Juan",TipoDocumento.DNI, 22222222,new Direccion("provincia","CABA","Cobo","1234"));
    pedro = new Miembro("Picapiedra", "Pedro", TipoDocumento.DNI, 22233333,new Direccion("provincia","CABA","Cobo","1234"));
  }

  @Test
  public void juanViveEn() {
    assertEquals(juan.getDireccionHogar().getCalle(), "Cobo");
    assertEquals(juan.getDireccionHogar().getAltura(), "1234");
  }
  @Test
  public void pedroYJuanTrabajanEnTechint() {
    juan.postularse(techint,techint.getSectores().get(0));
    pedro.postularse(techint,techint.getSectores().get(0));
    techint.aceptarPostulante(techint.getPostulantes().get(0));
    techint.aceptarPostulante(techint.getPostulantes().get(0));
    assertEquals(techint.getSectores().get(0).getMiembros().contains(pedro), true);
    assertEquals(techint.getSectores().get(0).getMiembros().contains(juan), true);
  }
}