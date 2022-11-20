package service;


import domain.Distancia;
import domain.Direccion;

/**
 * Implementamos la interfaz para luego mockear las pruebas,
 * y para distribuir el trabajo de implementacion.
 */
public interface CalculadorDeDistancia {
  Distancia obtenerDistancia(Direccion origen, Direccion destino);
}
