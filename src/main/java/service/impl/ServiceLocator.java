package service.impl;

import service.CalculadorDeDistancia;

/**
 * Utilizamos un ServiceLocator para desacoplarnos del servicio que vamos a utilizar en si,
 * para por ejemplo calcular la distancia. Es decir, para el testing podriamos configurar un mock
 * para los casos reales se empleara el productivo.
 */
public class ServiceLocator {
  private static ServiceLocator instance = new ServiceLocator();
  private CalculadorDeDistancia calculadorDeDistancia;

  private ServiceLocator(){
  }

  public void agregarServicioDeDistancia(CalculadorDeDistancia calculadorDeDistancia){
    this.calculadorDeDistancia = calculadorDeDistancia;
  }

  public static ServiceLocator obtenerInstancia(){
    return instance;
  }

  public CalculadorDeDistancia obtenerCalculadorDeDistancia(){
    return this.calculadorDeDistancia;
  }

}
