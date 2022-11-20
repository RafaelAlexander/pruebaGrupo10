package domain;

import domain.archivocsv.Actividad;
import domain.archivocsv.TipoDeConsumo;
import domain.archivocsv.Unidad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.FactoresEmision;
import service.CalculadorDeDistancia;
import service.impl.ServiceLocator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransportePrivadoTest {
  private Direccion direccionOrigen, direccionDestino;
  private CalculadorDeDistancia calculadorDeDistancia;
  private TipoDeConsumo tipoDeConsumo;
  private FactorEmision factorEmision;

  @BeforeEach
  public void setup(){
    calculadorDeDistancia = mock(CalculadorDeDistancia.class);
    ServiceLocator.obtenerInstancia().agregarServicioDeDistancia(calculadorDeDistancia);
    direccionOrigen = new Direccion("provincia","1","maipu","100");
    direccionDestino = new Direccion("provincia","457","O'Higgins","200");
    factorEmision = new FactorEmision(Unidad.lt,100);
    FactoresEmision.getInstance().agregarFactorEmision(factorEmision);
    tipoDeConsumo = new TipoDeConsumo("Transporte", Unidad.lt, Actividad.CombustiónMóvil,100);
  }

  @Test
  public void calcularDistanciaVehiculoParticular() {

    VehiculoParticular vehiculoParticular =
        new VehiculoParticular(direccionOrigen, direccionDestino, TipoVehiculo.AUTO, TipoCombustible.NAFTA,tipoDeConsumo);
    when( calculadorDeDistancia
        .obtenerDistancia(this.direccionOrigen,this.direccionDestino))
        .thenReturn(new Distancia(14.000));
    assertEquals(vehiculoParticular.calcularDistancia(), 14.000, 0);
  }

  @Test
  public void calcularDistanciaBicicleta() {
    Bicicleta mountainBike = new Bicicleta(direccionOrigen, direccionDestino);
    when( calculadorDeDistancia
        .obtenerDistancia(this.direccionOrigen,this.direccionDestino)).thenReturn(new Distancia(14.000));
    assertEquals(mountainBike.calcularDistancia(), 14.000, 0);
  }

  @Test
  public void calcularDistanciaPie() {
    Pie caminandoHaciaUTN = new Pie(direccionOrigen, direccionDestino);
    when( calculadorDeDistancia
        .obtenerDistancia(this.direccionOrigen,this.direccionDestino)).thenReturn(new Distancia(14.000));
    assertEquals(caminandoHaciaUTN.calcularDistancia(), 14.000, 0);
  }


  @Test
  public void calcularPieHC() {
    Pie caminandoHaciaUTN = new Pie(direccionOrigen, direccionDestino);
    when( calculadorDeDistancia
        .obtenerDistancia(this.direccionOrigen,this.direccionDestino)).thenReturn(new Distancia(14.000));
    assertEquals(caminandoHaciaUTN.getHuellaDeCarbono(), 0, 0);
  }
}
