package domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
public class Recorrido extends PersistentEntity{

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name= "recorrido_id")
  @Getter private List<Estacion> estacionList;

  @Getter private String nombre;

  public Recorrido() {

  }

  public boolean contiene(List<Estacion> estacions) {
    return this.estacionList.containsAll(estacions);
  }
}
