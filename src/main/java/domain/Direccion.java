package domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Direccion extends PersistentEntity{

  @Getter private String provincia;
  @Getter private String localidad;
  @Getter private String calle;
  @Getter private String altura;

}