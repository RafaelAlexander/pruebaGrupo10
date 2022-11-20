package domain;

import lombok.Getter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

// Mapeo sin herencia
// le dice a Hibernate que lo use en las clases de las cual hereda
@MappedSuperclass
public abstract class PersistentEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  @Getter
  private Long id;
}
