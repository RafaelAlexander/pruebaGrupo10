package domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.engine.internal.Cascade;
import service.impl.ServiceLocator;
import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="bicicletas")
public class Bicicleta extends Transporte {

  @ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
  @JoinColumn(name="direccion_origen_id", nullable=false)
  @Getter private Direccion direccionOrigen;

  @ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
  @JoinColumn(name="direccion_llegada_id", nullable=false)
  @Getter private Direccion direccionLlegada;

  @Override
  public double calcularDistancia() {
    return
        ServiceLocator.obtenerInstancia()
            .obtenerCalculadorDeDistancia()
            .obtenerDistancia(this.direccionOrigen,this.direccionLlegada)
            .getValor();
  }

  @Override
  public double getHuellaDeCarbono() {
    return 0;
  }

}
