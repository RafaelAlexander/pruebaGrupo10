package domain;

import domain.archivocsv.Unidad;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@AllArgsConstructor
@Entity
@Table(name="factores_emision")
public class FactorEmision extends PersistentEntity{

  @Enumerated(EnumType.STRING)
  @Column(name = "unidad")
  @Getter private Unidad unidad;

  @Column(name = "factor")
  @Getter @Setter private float factor;

  public boolean esUnidad(Unidad unidad) {
    return this.getUnidad().equals(unidad);
  }

  public FactorEmision(){

  }
}
