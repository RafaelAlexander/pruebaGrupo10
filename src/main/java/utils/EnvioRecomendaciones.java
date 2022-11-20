package utils;

import domain.recomendaciones.Recomendacion;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import repository.Organizaciones;
import repository.Recomendaciones;

import java.util.List;
import java.util.stream.Collectors;

public class EnvioRecomendaciones implements Job {
  public void execute(JobExecutionContext context) throws JobExecutionException {
    List<Recomendacion> recomendaciones = Recomendaciones.instancia().obtenerRecomendaciones();
    String recomendacionesTextoPlano = recomendaciones.stream().map(recomendacion -> recomendacion.getLink()).collect(Collectors.joining(","));
    if (! recomendacionesTextoPlano.isEmpty()) {
      Organizaciones.instancia().getOrganizacionesList().stream().forEach(organizacion -> organizacion.enviarMensajeAContactos(recomendacionesTextoPlano));
    }
  }

}