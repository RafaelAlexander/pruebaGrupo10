package domain;

import domain.archivocsv.TipoDeConsumo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Linea extends PersistentEntity{

  @Enumerated
  @Getter private TipoLinea tipoLinea;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name= "linea_id")
  @Getter private List<Recorrido> recorridos;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "tipoDeConsumo_id")
  @Getter private TipoDeConsumo tipoDeConsumo;

  public Double getDistancia(Estacion estacion1, Estacion estacion2) {
    double valor = 0;
    if(estacion1 == estacion2){
      return 0.00;
    }

    List<Estacion> estacionesInvolucradas = new ArrayList<>(estacionesEntre(estacion1,estacion2));

    for(int i = 0; i<estacionesInvolucradas.size(); i++){
      if(i != estacionesInvolucradas.size() -1){
        valor += estacionesInvolucradas.get(i).calcularDistancia(estacionesInvolucradas.get(i+1));
      }
    }
    return valor;
  }

  private Recorrido buscarRecorrido(Estacion estacion1, Estacion estacion2) {
    return this.recorridos
        .stream()
        .filter(recorrido -> recorrido.contiene(Arrays.asList(estacion1, estacion2)))
        .findFirst().orElseThrow(()->new NullPointerException("No se encontro recorrido con estaciones iguales."));
  }

  public double getHC(Estacion estacion1, Estacion estacion2) {
    return this.tipoDeConsumo.getUnidadFactor()*getDistancia(estacion1, estacion2);
  }

  private List<Estacion> estacionesEntre(Estacion estacion1, Estacion estacion2) {
    List<Estacion> estaciones = buscarRecorrido(estacion1, estacion2).getEstacionList();
    List<Estacion> estacionesA = new ArrayList<>(estaciones);
    List<Estacion> estacionesB = new ArrayList<>(estaciones);

    for (int i = 0; i < estacionesA.indexOf(estacion1); i++) {
      estacionesA.remove(i);
    }
    for (int i = estaciones.indexOf(estacion2); i < estacionesB.size(); i++) {
      estacionesB.remove(i);
    }
    estacionesA.addAll(estacionesB);
    int size_inicial = estaciones.size();
    for (int i = (size_inicial-1) ; i>= 0; i--){
      if(!estacionesA.contains(estaciones.get(i))) {
        estaciones.remove(i);
      }
    }
    return estaciones;
  }
}