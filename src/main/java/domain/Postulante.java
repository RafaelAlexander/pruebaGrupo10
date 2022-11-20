package domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Postulante extends PersistentEntity{

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "miembro_id")
  @Getter public Miembro miembro;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "sector_id")
  @Getter public Sector sector;

}