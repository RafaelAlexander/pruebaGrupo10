package domain;

import lombok.Getter;
import javax.persistence.*;

@Entity
@Table(name="transportes_publicos")
public class TransportePublico extends Transporte {

  @ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
  @JoinColumn(name= "estacionInicial_id")
  @Getter private Estacion estacionInicial;

  @ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
  @JoinColumn(name= "estacionFinal_id")
  @Getter private Estacion estacionFinal;

  @ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
  @JoinColumn(name= "linea_id")
  @Getter private Linea linea;

  public TransportePublico(Estacion estacionInicial, Estacion estacionFinal, Linea linea) {
    this.estacionFinal = estacionFinal;
    this.estacionInicial = estacionInicial;
    this.linea = linea;
  }

  public TransportePublico() {

  }

  @Override
  public double calcularDistancia() {
    return this.linea.getDistancia(estacionInicial, estacionFinal);
  }

  @Override
  public double getHuellaDeCarbono() {
    return this.linea.getHC(estacionInicial, estacionFinal);
  }
}