package controllers;

import domain.Organizacion;
import domain.archivocsv.*;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import repository.Organizaciones;
import repository.TiposDeConsumo;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ActividadController extends BaseController implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

  public ModelAndView obtenerFormularioCreacion(Request request, Response response) {
    Map<String, Object> modelo = new HashMap<>();
    cargarSesion(request,response,modelo);
    Organizacion org = Organizaciones.instancia()
        .obtenerOrganizacionPorUsuario(obtenerUsuario(request).getId());
    if (org != null) {
      modelo.put("organizacion", org);
      modelo.put("tiposDeConsumo", TiposDeConsumo.instancia().getTipoConsumos());
      modelo.put("periodicidades", Arrays.asList("Anual", "Mensual"));
    }
    return new ModelAndView(modelo, "actividad.html.hbs");
  }

  private void cargaActividadCSV(Request request, Organizacion organizacion) {
    if (request.queryParams("csv") == null) return;
    try {
      BuilderDatoActividad builder = new BuilderDatoActividad(organizacion);
      builder.lectorCsv(request.queryParams("csv"));
    } catch (Exception e) {
      throw new NullPointerException();
    }
  }

  private void cargaActividadManual(Request request, Organizacion organizacion) {
      BuilderDatoActividad bda = new BuilderDatoActividad(organizacion);
      DatoActividad datoActividad = bda
          .agregarConsumo(request.queryParams("consumo"))
          .agregarTipoDeConsumo(request.queryParams("tipoDeConsumo"))
          .agregarPerioricidad(request.queryParams("periodicidad"))
          .crearActividad();
  }

  public ModelAndView crearActividad(Request request, Response response) {
    withTransaction(() -> {
      Organizacion organizacion = Organizaciones.instancia()
          .obtenerOrganizacionPorUsuario(obtenerUsuario(request).getId());
      try {
        cargaActividadManual(request,organizacion);
        cargaActividadCSV(request,organizacion);
        response.redirect("/organizaciones/" + organizacion.getId() + "/actividades");
      } catch (Exception e) {
        response.redirect("/error");
      }
    });
    return null;
  }

  public ModelAndView obtenerActividades(Request request, Response response) {
    String id = request.params(":id");
    Map<String, Object> modelo = new HashMap<>();
    cargarSesion(request,response,modelo);
    modelo.put("actividades", Organizaciones.instancia()
        .obtnerOrganizaionPorId(Long.parseLong(id))
        .getDatosActividades());
    return new ModelAndView(modelo, "actividades.html.hbs");
  }
  public ModelAndView obtenerActividad(Request request, Response response) {
    String id = request.params(":id"); // Org
    String idActividad = request.params(":idActividad");
    Map<String, Object> modelo = new HashMap<>();
    cargarSesion(request,response,modelo);
    modelo.put("actividad", Organizaciones.instancia()
        .obtnerOrganizaionPorId(Long.parseLong(id))
        .getDatosActividades()
            .stream()
            .filter(datoActividad -> datoActividad.getId().equals(Long.parseLong(idActividad)))
            .findFirst()
        );
    return new ModelAndView(modelo, "detalle_actividad.html.hbs");
  }

  public ModelAndView borrarActividad(Request request, Response response) {
    String id = request.params(":id"); // Org
    String idActividad = request.params(":idActividad");
    Map<String, Object> modelo = new HashMap<>();
    cargarSesion(request,response,modelo);

    withTransaction(() -> {
      try {
        Organizacion org = Organizaciones.instancia()
            .obtenerOrganizacionPorUsuario(obtenerUsuario(request).getId());

        if (org.getId().equals(id)) {
          org.quitarActividadId(Long.parseLong(idActividad));
        } else {
          response.redirect("/error");
        }
        response.redirect("/organizaciones/" + org.getId() + "/actividades");
      } catch (Exception e) {
        response.redirect("/error");
      }
    });

    modelo.put("actividad", Organizaciones.instancia()
        .obtnerOrganizaionPorId(Long.parseLong(id))
        .getDatosActividades()
    );
    return new ModelAndView(modelo, "actividades.html.hbs");
  }
}