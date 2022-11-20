package utils;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import repository.Organizaciones;
import repository.Zonas;

public class GeneracionHistorial implements Job {

  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    Organizaciones.instancia().getOrganizacionesList().stream().forEach(organizacion -> organizacion.generarHistorial());
    Zonas.getInstance().getZonas().stream().forEach(zona -> zona.generarHistorial());
  }
}
