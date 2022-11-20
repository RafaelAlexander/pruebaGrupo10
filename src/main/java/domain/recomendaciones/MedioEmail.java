package domain.recomendaciones;

import domain.exception.UserPasswordEmailException;
import lombok.Getter;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class MedioEmail extends MedioDeRecomendacion {

  public static final String SUBJECT = "Recomendaciones";
  public static final String HOST_NAME_GMAIL = "smtp.gmail.com";
  public static final int PORT = 587;

  @Override
  public void enviarRecomendacion(String valorContacto, String linkRecomendacion) throws EmailException {

    String user = System.getenv("user");
    String password = System.getenv("password");

    if ((user == null || user.isEmpty()) && (password == null || password.isEmpty())) {
      throw new UserPasswordEmailException("No se pudo recuperar el usuario y la contraseña seteadas en las variables de entorno");
    }

    Email email = new SimpleEmail();
    email.setHostName(HOST_NAME_GMAIL);
    email.setSmtpPort(587);
    email.setAuthentication(user, password);
    email.setSSLOnConnect(true);
    email.setFrom(user);
    email.setSubject(SUBJECT);
    email.setMsg(linkRecomendacion);
    email.addTo(valorContacto);
    email.send();

  }

}
