package domain;

import domain.recomendaciones.Contacto;
import domain.recomendaciones.MedioDeRecomendacion;
import domain.recomendaciones.MedioEmail;
import domain.recomendaciones.MedioWhatsApp;
import org.apache.commons.mail.EmailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

public class MedioDeRecomendacionTest extends AbstractPersistenceTest implements WithGlobalEntityManager {

  private MedioDeRecomendacion medioWhatsApp;
  private Contacto contactoWhatsApp;

  @BeforeEach
  public void setup(){
    medioWhatsApp = new MedioWhatsApp();
    contactoWhatsApp = new Contacto("1164465780", medioWhatsApp);
    MedioDeRecomendacion medioEmail = new MedioEmail();
    Contacto contactoEmail = new Contacto("merubinho@frba.utn.edu.ar", medioEmail);
  }
  @Test
  public void medioDeRecomendacionWhatsApp() throws EmailException {
    contactoWhatsApp.enviarRecomendacion("compreRamWeb.com");
  }

  @Test
  @Disabled
  public void medioDeRecomendacionEmail() throws EmailException {
    MedioEmail medioEmail = new MedioEmail();
    Contacto contactoEmail = new Contacto("merubinho@frba.utn.edu.ar", medioEmail);
    contactoEmail.enviarRecomendacion("www.mercadolibre.com.ar");
  }

}
