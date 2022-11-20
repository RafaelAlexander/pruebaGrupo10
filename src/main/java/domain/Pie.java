package domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import service.impl.ServiceLocator;
import javax.persistence.*;

@AllArgsConstructor
@Entity
@Table(name ="pies")
public class Pie extends Transporte {

  @ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
  @JoinColumn(name = "direccion_origen_id")
  @Getter private Direccion direccionOrigen;

  @ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
  @JoinColumn(name = "direccion_llegada_id")
  @Getter private Direccion direccionLlegada;

  @Override
  public double calcularDistancia() {
    return ServiceLocator.obtenerInstancia().obtenerCalculadorDeDistancia().obtenerDistancia(direccionOrigen, direccionLlegada).getValor();
  }

  @Override
  public double getHuellaDeCarbono() {
    return 0;
  }

}
