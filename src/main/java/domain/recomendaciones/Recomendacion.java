package domain.recomendaciones;

import domain.PersistentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Entity;

@Entity
@AllArgsConstructor
public class Recomendacion extends PersistentEntity {

  @Getter
  public String link;

  @Getter
  public String nombre;

  public Recomendacion(){

  }
}
