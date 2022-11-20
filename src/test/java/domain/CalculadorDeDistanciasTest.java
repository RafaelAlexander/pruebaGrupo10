package domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import service.impl.ServiceLocator;
import service.impl.CalculadorDeDistancias;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;

public class CalculadorDeDistanciasTest extends AbstractPersistenceTest implements WithGlobalEntityManager {

  private Direccion direccionOrigen, direccionDestino;

  @AfterEach
  public void despues() {
    this.rollbackTransaction();
  }

  @BeforeEach
  public void setup() {
    this.beginTransaction();
    direccionOrigen = new Direccion("provincia","1","maipu","100");
    direccionDestino = new Direccion("provincia","457","O'Higgins","200");
  }

  @Test
  public void obtenerDistancia() {
    Distancia distancia = new Distancia(100.00);
    CalculadorDeDistancias servicioMockeado = mock(CalculadorDeDistancias.class);

    when(servicioMockeado.obtenerDistancia(direccionOrigen, direccionDestino)).thenReturn(distancia);
    assertEquals(100.00,
        servicioMockeado.obtenerDistancia(direccionOrigen, direccionDestino).getValor());
  }

  @Test
  public void obtenerDistanciaSinMock() {

    ServiceLocator.obtenerInstancia().agregarServicioDeDistancia(new CalculadorDeDistancias());
    assertDoesNotThrow(
        ()-> ServiceLocator.obtenerInstancia().obtenerCalculadorDeDistancia().obtenerDistancia(direccionOrigen, direccionDestino).getValor());
  }

}
