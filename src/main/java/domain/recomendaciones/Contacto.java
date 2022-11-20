package domain.recomendaciones;

import domain.PersistentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.mail.EmailException;

import javax.persistence.*;

@Entity
@AllArgsConstructor
public class Contacto extends PersistentEntity {

  @Getter private String valor;

  @Embedded
  @Getter private MedioDeRecomendacion medioDeRecomendacion;

  public void enviarRecomendacion(String linkRecomendacion) throws EmailException {
    this.medioDeRecomendacion.enviarRecomendacion(this.valor, linkRecomendacion);
  }
}
