package domain.recomendaciones;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import domain.PersistentEntity;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class MedioWhatsApp extends MedioDeRecomendacion {

  public static final String ACCOUNT_SID = "AC3c959c7c2e8b6b37b6a7f1a3cbe6fa62";
  public static final String AUTH_TOKEN = "48d1f023b725dec4046d550f8685646d";
  public static final String WHATSAPP_EMISOR = "whatsapp:+14155238886";

  /*
   * Para que el servicio nos envies las notificaciones, previamente cada contacto,
   * debe enviar un mensaje de WhatsApp "join religious-pen" al numero +14155238886
   */
  @Override
  public void enviarRecomendacion(String valorContacto, String linkRecomendacion) {
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    Message message = Message.creator(
            new com.twilio.type.PhoneNumber("whatsapp:+549"+valorContacto), //Nro destino de mensajes, ejemplo: whatsapp:+5491135465972
            new com.twilio.type.PhoneNumber(WHATSAPP_EMISOR),   //Nro origen de mensajes
            "Entiendo que esta con la nobleza causa de cuidar el planeta por eso le recomendacion el/los siguiente/s enlace/s: " + linkRecomendacion)
        .create();
  }

}