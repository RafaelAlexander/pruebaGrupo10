package domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="servicios_contratados")
public class ServicioContratado extends PersistentEntity{

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_servicio_contratado")
  @Getter private TipoServicioContratado tipoServicioContratado;
  @Getter private String nombre;

}
