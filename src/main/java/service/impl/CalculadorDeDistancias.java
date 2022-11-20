package service.impl;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import domain.Direccion;
import domain.Distancia;
import javax.ws.rs.core.HttpHeaders;
import service.CalculadorDeDistancia;

public class CalculadorDeDistancias implements CalculadorDeDistancia{
  private static final String URL = "https://ddstpa.com.ar";
  private final WebResource webResource;
  private final String bearerToken = "m8cEzTY24DG3FlZilTNIVCo1RkP6Q2BJuR5fXfsrMCk=";

  public CalculadorDeDistancias() {
    webResource = Client.create().resource(URL);
  }
  @Override
  public Distancia obtenerDistancia(Direccion origen, Direccion destino){
    return new Gson().fromJson(webResource
        .path("/api/distancia")
        .queryParam("localidadOrigenId", origen.getLocalidad())
        .queryParam("calleOrigen", origen.getCalle())
        .queryParam("alturaOrigen", origen.getAltura())
        .queryParam("localidadDestinoId", destino.getLocalidad())
        .queryParam("calleDestino", destino.getCalle())
        .queryParam("alturaDestino", destino.getAltura())
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
        .accept("application/json")
        .get(String.class), Distancia.class);
  }
}
