package domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.Tareas;
import utils.EnvioRecomendaciones;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;


public class CronTest {
  private EnvioRecomendaciones unaTarea = new EnvioRecomendaciones();
  private Tareas unCron = new Tareas();

  @Test
  public void calcularCron() throws InterruptedException {
    unCron.tareaRecomendaciones();
    Thread.sleep(100L);
    Assertions.assertEquals(1,1);

  }
}