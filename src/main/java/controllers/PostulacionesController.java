package controllers;

import domain.Miembro;
import domain.Organizacion;
import domain.Postulante;
import domain.TipoUsuario;
import domain.Sector;
import domain.Usuario;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import repository.Miembros;
import repository.Organizaciones;
import repository.TiposDeConsumo;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class PostulacionesController extends BaseController implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps{

  public ModelAndView getPostulacionesPendientes(Request request, Response response){
    Map<String, Object> modelo = new HashMap<>();
    cargarSesion(request,response,modelo);
    try {
      //Cargo las organizaciones de las que no soy parte
      List<Organizacion> organizaciones = Organizaciones.instancia()
          .obtenerOrganizacionesDeLasCualesNoEsMiembroPorUsuario(obtenerUsuario(request).getId());
      if (organizaciones != null | organizaciones.isEmpty()) {
        modelo.put("hayOrganizacionesDisponibles", Boolean.TRUE);
        //modelo.put("organizacionesDisponibles", organizaciones);
        List<Map<String, String>> organizacionSector = new ArrayList<>();
        organizaciones
            .stream()
            .forEach(
                unaOrganizacion -> {
                  HashMap unaOrganizacionUnSector = new HashMap();
                  unaOrganizacionUnSector.put("razonSocial", (unaOrganizacion.getId() + "-" + unaOrganizacion.getRazonSocial()));
                  unaOrganizacion.getSectores()
                      .stream()
                      .forEach(
                          unSector -> unaOrganizacionUnSector.put("sector", unSector.getName())
                      );
                  organizacionSector.add(unaOrganizacionUnSector);
                }
            );
        modelo.put("organizacionSector", organizacionSector);

        //Cargo los sectores de las organizaciones que no soy parte
        List<Sector> sectores = new ArrayList<>();
        organizaciones
            .stream()
            .forEach(
                unaOrganizacion -> sectores.addAll(unaOrganizacion.getSectores())
            );
        modelo.put("sectores", sectores);
      }

      //Cargo las postulaciones pendientes de las organizaciones que soy parte
      List<Postulante> postulantes = new ArrayList<>();
      Organizaciones
          .instancia().obtenerOrganizacionesPorUsuario(obtenerUsuario(request).getId())
          .stream()
          .forEach(
            unaOrganizacion -> postulantes.addAll(unaOrganizacion.getPostulantes())
      );
      modelo.put("noHayPostulaciones", postulantes.isEmpty());
      modelo.put("postulacionesPendientes", postulantes);
      return new ModelAndView(modelo, "postulaciones.html.hbs");
    } catch (Exception e) {
      response.redirect("/error");
    }
    return null;
  }

  public ModelAndView crearPostulacion(Request request, Response response) {
    Map<String, Object> modelo = new HashMap<>();
    cargarSesion(request,response,modelo);
    try {
      Usuario usuario = obtenerUsuario(request);
      Miembro miembro = Miembros.getInstance().getMiembroConNombreUsuario(usuario.getNombreUsuario());

      //Obtengo la Organización a la cual realice la postulación
      String organizacionIdRazonSocial = request.queryParams("razonSocial");
      Long idOrganizacion = Long.valueOf(organizacionIdRazonSocial.split("-")[0].replaceAll("\\s+",""));
      Organizacion organizacion = Organizaciones.instancia().obtnerOrganizaionPorId(idOrganizacion);
      //Obtengo el Sector al cual realice la postulación
      String sectorBuscado = organizacionIdRazonSocial.split("-")[2].replaceAll("\\s+","");
      Sector sector;
      sector = organizacion.getSectores()
          .stream().filter(
              unSector -> unSector.name.equals(sectorBuscado)
          ).collect(Collectors.toList()).get(0);

      withTransaction(() -> {
        miembro.postularse(organizacion, sector);
      });
      //response.redirect("/postulaciones");
      return new ModelAndView(modelo, "postulacion_exitosa.html.hbs");
    } catch (Exception e) {
      response.redirect("/error");
    }
    return null;
  }

  public ModelAndView aceptarPostulacion(Request request, Response response) {
    Map<String, Object> modelo = new HashMap<>();
    cargarSesion(request,response,modelo);
    try {
      Long sectorId = Long.valueOf(request.queryParams("idSector"));
      String nombreMiembro = request.queryParams("nombreMiembro");
      String apellidoMiembro = request.queryParams("apellidoMiembro");
      Miembro miembroPostulado = Miembros.getInstance().getMiembroConNombreYApellido(nombreMiembro, apellidoMiembro);
      List<Organizacion> organizacionesDeLasQueNoEsParteYTienenElSector = Organizaciones
          .instancia()
          .obtenerOrganizacionesDeLasCualesNoEsMiembroPorUsuarioConEsteSector(
              miembroPostulado.getUsuario().getId(),
              sectorId
          );
      if(!organizacionesDeLasQueNoEsParteYTienenElSector.isEmpty()) {
        Organizacion organizacionPostulada = organizacionesDeLasQueNoEsParteYTienenElSector.get(0);
        Postulante postulante = organizacionPostulada.getPostulantePorMiembroId(miembroPostulado.getId());
        if(postulante != null) {
          withTransaction(() -> {
            organizacionPostulada.aceptarPostulante(postulante);
          });
          //response.redirect("/postulaciones");
          return new ModelAndView(modelo, "postulacion_aceptada.html.hbs");
        } else {
          response.redirect("/error");
        }
      } else {
        response.redirect("/error");
      }
    } catch (Exception e) {
      response.redirect("/error");
    }
    return null;
  }

  public ModelAndView rechazarPostulacion(Request request, Response response) {
    Map<String, Object> modelo = new HashMap<>();
    cargarSesion(request,response,modelo);
    try {
      Long sectorId = Long.valueOf(request.queryParams("idSector"));
      String nombreMiembro = request.queryParams("nombreMiembro");
      String apellidoMiembro = request.queryParams("apellidoMiembro");
      Miembro miembroPostulado = Miembros.getInstance().getMiembroConNombreYApellido(nombreMiembro, apellidoMiembro);
      List<Organizacion> organizacionesDeLasQueNoEsParteYTienenElSector = Organizaciones
          .instancia()
          .obtenerOrganizacionesDeLasCualesNoEsMiembroPorUsuarioConEsteSector(
              miembroPostulado.getUsuario().getId(),
              sectorId
          );
      if(!organizacionesDeLasQueNoEsParteYTienenElSector.isEmpty()) {
        Organizacion organizacionPostulada = organizacionesDeLasQueNoEsParteYTienenElSector.get(0);
        Postulante postulante = organizacionPostulada.getPostulantePorMiembroId(miembroPostulado.getId());
        if(postulante != null) {
          withTransaction(() -> {
            organizacionPostulada.rechazarPostulante(postulante);
          });
          //response.redirect("/postulaciones");
          return new ModelAndView(modelo, "postulacion_rechazada.html.hbs");
        } else {
          response.redirect("/error");
        }
      } else {
        response.redirect("/error");
      }
    } catch (Exception e) {
      response.redirect("/error");
    }
    return null;
  }
}
