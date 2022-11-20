package domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
public class Estacion extends PersistentEntity{

  @Embedded
  @Getter private Coordenada coordenada;

  public Estacion() {

  }

  public double calcularDistancia(Estacion estacion){
    if(estacion == null){
      return 0.00;
    }
    double x = estacion.getCoordenada().getLatitud()-this.getCoordenada().getLatitud();
    double y = estacion.coordenada.getLongitud()-this.coordenada.getLongitud();
    return Math.sqrt(x*x + y*y);
  }
}
