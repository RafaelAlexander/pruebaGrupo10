package domain;

import domain.archivocsv.Actividad;
import domain.archivocsv.TipoDeConsumo;
import domain.archivocsv.Unidad;
import org.junit.jupiter.api.Test;
import repository.FactoresEmision;
import service.CalculadorDeDistancia;
import service.impl.ServiceLocator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrayectoTest {
  private Direccion Palermo = new Direccion("provincia","1","maipu","100");
  private Direccion Lugano = new Direccion("provincia","457","O'Higgins","200");
  @Test
  public void calcularDistanciaDeTrayecto() {
    CalculadorDeDistancia calculadorDeDistancia = mock(CalculadorDeDistancia.class);
    ServiceLocator.obtenerInstancia().agregarServicioDeDistancia(calculadorDeDistancia);
    when(calculadorDeDistancia.obtenerDistancia(Palermo,Lugano)).thenReturn(new Distancia(12));
    List<Tramo> tramos = getTramos();
    Trayecto trayectoPalermoLugano = new Trayecto("",tramos,  LocalDate.now());
    assertEquals(trayectoPalermoLugano.calcularDistancia(), 12, 0);
  }

  private List<Tramo> getTramos() {
    FactorEmision factorEmision = new FactorEmision(Unidad.lt,100);
    FactoresEmision.getInstance().agregarFactorEmision(factorEmision);
    TipoDeConsumo tipoDeConsumo = new TipoDeConsumo("Transporte", Unidad.lt, Actividad.CombustiónMóvil,100);
    Organizacion ypf = new Organizacion("YPF",
        TipoOrganizacion.EMPRESA,
        new Direccion("provincia",
            "CABA",
            "Cobo",
            "1234"),
        Collections.singletonList(new Sector("Ventas")),
        ClasificacionOrganizacion.EMPRESA_SECTOR_SECUNDARIO,
        null);
    VehiculoParticular miAuto = new VehiculoParticular(Palermo, Lugano, TipoVehiculo.AUTO, TipoCombustible.NAFTA,tipoDeConsumo);
    Tramo unTramoSolo = new Tramo(ypf, miAuto);
    List<Tramo> tramos = new ArrayList<Tramo>();
    tramos.add(unTramoSolo);
    return tramos;
  }

}
