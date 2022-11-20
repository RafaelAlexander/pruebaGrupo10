package domain;

import javax.persistence.*;

@Entity
@Table(name ="transportes")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Transporte extends PersistentEntity{

  abstract double calcularDistancia();
  abstract double getHuellaDeCarbono();

}