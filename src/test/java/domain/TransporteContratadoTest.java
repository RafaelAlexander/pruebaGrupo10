package domain;

import domain.archivocsv.Actividad;
import domain.archivocsv.TipoDeConsumo;
import domain.archivocsv.Unidad;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import repository.FactoresEmision;

public class TransporteContratadoTest {

  static TipoDeConsumo tipoDeConsumo;
  static FactorEmision factorEmision;

  @BeforeAll
  public static void setup(){
    factorEmision = new FactorEmision(Unidad.lt,100);
    FactoresEmision.getInstance().agregarFactorEmision(factorEmision);
    tipoDeConsumo = new TipoDeConsumo("Transporte", Unidad.lt,Actividad.CombustiónMóvil,100);
  }

  @Test
  public void transporteContratadoSuccessTest(){
    ServicioContratado luis = new ServicioContratado(TipoServicioContratado.REMIS, "Luis REMIS");
    TransporteContratado remis = new TransporteContratado(new Direccion("provincia","CABA","Cobo","1234"),
        new Direccion("provincia","CABA","Cobo","1234"), luis,tipoDeConsumo);
    assertEquals(remis.getServicioContratado(), luis);
  }


}
