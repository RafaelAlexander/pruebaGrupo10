package domain;

import domain.exception.TramoSinOrganizacionException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="tramos")
@NoArgsConstructor
@AllArgsConstructor
public class Tramo extends PersistentEntity{

  @OneToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
  @Getter private Organizacion organizacion;

  @ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
  @JoinColumn(name="transporte_id")
  @Getter private Transporte transporte;

  public double calcularDistancia() {
    if (this.organizacion == null) {
      throw new TramoSinOrganizacionException(
          "Error: no se puede calcular una distancia sin estar vinculada con una Organizacion");
    }
    return this.transporte.calcularDistancia();
  }

  public double getHC(Organizacion organizacion){
    if(this.organizacion == organizacion){
      return transporte.getHuellaDeCarbono();
    }
    return 0;
  }

}
