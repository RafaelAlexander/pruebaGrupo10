package domain.recomendaciones;

import org.apache.commons.mail.EmailException;
import javax.persistence.*;

@Embeddable
public abstract class MedioDeRecomendacion {
  abstract void enviarRecomendacion(String valorContacto, String linkRecomendacion) throws EmailException;
}
