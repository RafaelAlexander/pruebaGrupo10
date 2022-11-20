package controllers;
import domain.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import repository.Organizaciones;
import repository.Zonas;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.time.LocalDateTime;
import java.util.*;


public class ReportesController {
  public ModelAndView getSeleccionReporte(Request request, Response response) {
    Map<String, Object> modelo = new HashMap<>();
    String jwt = request.session().attribute("sesionIniciada");
    if (jwt != null && jwt != "") {
      Jws<Claims> jws = UsuariosController.getDatosDeSesion(jwt);
      String nombre = jws.getBody().get("nombre", String.class);
      String fecha = jws.getBody().get("tiempoExp", String.class);
      String tipo = jws.getBody().get("tipo", String.class);
      Long idUsuario = Long.valueOf(jws.getBody().get("usuario", String.class));
      TipoUsuario tipoUsuario = TipoUsuario.valueOf(tipo);
      if (LocalDateTime.parse(fecha).isBefore(LocalDateTime.now())) {
        request.session().attribute("sesionIniciada", null);
        response.redirect("/login");
      }
      modelo.put("sesionIniciada", nombre);
      if (tipoUsuario.equals(TipoUsuario.ADMINISTRADOR)) {
        modelo.put("mensaje", "Usted es administrador, no esta autorizado para ver los reportes.");
        return new ModelAndView(modelo, "home.html.hbs");
      }
    } else {
      response.redirect("/login");
    }

    return new ModelAndView(modelo, "seleccionReportes.html.hbs");
  }

  public ModelAndView redireccionReporte(Request request, Response response) {

    String id = request.queryParams("tipo");

    Map<String, Object> modelo = new HashMap<>();
    String jwt = request.session().attribute("sesionIniciada");
    if (jwt != null && jwt != "") {
      Jws<Claims> jws = UsuariosController.getDatosDeSesion(jwt);
      String nombre = jws.getBody().get("nombre", String.class);
      String fecha = jws.getBody().get("tiempoExp", String.class);
      String tipo = jws.getBody().get("tipo", String.class);
      Long idUsuario = Long.valueOf(jws.getBody().get("usuario", String.class));
      TipoUsuario tipoUsuario = TipoUsuario.valueOf(tipo);
      if (LocalDateTime.parse(fecha).isBefore(LocalDateTime.now())) {
        request.session().attribute("sesionIniciada", null);
        response.redirect("/login");
      }
      modelo.put("sesionIniciada", nombre);
      if (tipoUsuario.equals(TipoUsuario.ADMINISTRADOR)) {
        modelo.put("mensaje", "Usted es administrador, no esta autorizado para ver los reportes.");
        return new ModelAndView(modelo, "home.html.hbs");
      }
    } else {
      response.redirect("/login");
    }

    switch (id){
      case "2":
        int anio = LocalDateTime.now().getYear();
        List<WrapperTipoOrganizacion> wrapperTipoOrganizacions = new ArrayList<>();
        Arrays.stream(TipoOrganizacion.values())
            .forEach(tipoOrganizacion -> wrapperTipoOrganizacions.add(new WrapperTipoOrganizacion(tipoOrganizacion)));
        Organizaciones.instancia()
            .getOrganizacionesList()
            .stream()
            .forEach(organizacion -> {
              WrapperTipoOrganizacion wt = wrapperTipoOrganizacions
                  .stream()
                  .filter(w->w.getTipoOrganizacion().name()
                      .equals(organizacion.getTipoOrganizacion().name()))
                  .findFirst().orElseThrow(()->new RuntimeException());
          wt.setHc(organizacion.getHuellaCarbonoGeneralAnual(anio));});
        modelo.put("tiposOrgs", wrapperTipoOrganizacions);
        return new ModelAndView(modelo, "tablaResultado.html.hbs");
      case "1":
      case "3":
      case "5":
        List<Zona> zonas = Zonas.getInstance().getZonas();
        if(zonas.size() == 0) {
          modelo.put("mensaje", "Lo lamento pero no hay zonas registradas");
          return new ModelAndView(modelo, "home.html.hbs");
        }
        modelo.put("zonas", zonas);
        if(id.equals("1")){
          return new ModelAndView(modelo, "tablaResultado.html.hbs");
        }
        modelo.put("tipo", id);
        return new ModelAndView(modelo, "seleccionZona.html.hbs");
      case "4":
      case "6":
        List<Organizacion> organizacions = Organizaciones.instancia().getOrganizacionesList();
        if(organizacions.size() == 0){
          modelo.put("mensaje", "Lo lamento pero no hay organizaciones registradas");
          return new ModelAndView(modelo, "resultado.html.hbs");
        }
        modelo.put("tipo", id);
        modelo.put("organizaciones", organizacions);
        return new ModelAndView(modelo, "seleccionOrganizacion.html.hbs");
      default:
        response.status(404);
        break;
    }
    return new ModelAndView(modelo, "resultado.html.hbs");
  }

  public ModelAndView verReporteEspecifico(Request request, Response response) {
    String id = request.queryParams("tipo");
    String objetoBusqueda = request.queryParams("vReporte");

    Map<String, Object> modelo = new HashMap<>();
    String jwt = request.session().attribute("sesionIniciada");
    if (jwt != null && jwt != "") {
      Jws<Claims> jws = UsuariosController.getDatosDeSesion(jwt);
      String nombre = jws.getBody().get("nombre", String.class);
      String fecha = jws.getBody().get("tiempoExp", String.class);
      String tipo = jws.getBody().get("tipo", String.class);
      Long idUsuario = Long.valueOf(jws.getBody().get("usuario", String.class));
      TipoUsuario tipoUsuario = TipoUsuario.valueOf(tipo);
      if (LocalDateTime.parse(fecha).isBefore(LocalDateTime.now())) {
        request.session().attribute("sesionIniciada", null);
        response.redirect("/login");
      }
      modelo.put("sesionIniciada", nombre);
      if (tipoUsuario.equals(TipoUsuario.ADMINISTRADOR)) {
        modelo.put("mensaje", "Usted es administrador, no esta autorizado para ver los reportes.");
        return new ModelAndView(modelo, "home.html.hbs");
      }
    } else {
      response.redirect("/login");
    }
    Zona zona;
    Organizacion organizacion;
    List<HistorialHC> historialHCS;
    switch (id) {
      case "3":
        zona = Zonas.getInstance().getZonas().stream().filter(z->z.getId()==(Long.valueOf(objetoBusqueda))).findFirst().orElseThrow(()->new RuntimeException());
        modelo.put("esTercerReporte", true);
        modelo.put("nombre", zona.getNombreProvinciaLocalidad());
        modelo.put("interno", zona.getHCActualParticular());
        modelo.put("externo", zona.getHCActualExterna());
        return new ModelAndView(modelo, "tablaResultado.html.hbs");
      case "4":
         organizacion = Organizaciones.instancia().obtnerOrganizaionPorId(Long.valueOf(objetoBusqueda));
        modelo.put("esTercerReporte", true);
        modelo.put("nombre", organizacion.getName());
        modelo.put("interno", organizacion.getHuellaCarbonoParticularAnual(LocalDateTime.now().getYear()));
        modelo.put("externo", organizacion.getHuellaCarbonoExternaAnual(LocalDateTime.now().getYear()));
        return new ModelAndView(modelo, "tablaResultado.html.hbs");
      case "5":
        zona = Zonas.getInstance().getZonas().stream().filter(z->z.getId()==(Long.valueOf(objetoBusqueda))).findFirst().orElseThrow(()->new RuntimeException());
        historialHCS = zona.getHistorialHCS();
        if(historialHCS.size()<1){
          modelo.put("mensaje", "No hay historial para la zona.");
          return new ModelAndView(modelo, "home.html.hbs");
        }
        modelo.put("historial", historialHCS);
        return new ModelAndView(modelo, "tablaResultado.html.hbs");
      case "6":
        organizacion = Organizaciones.instancia().obtnerOrganizaionPorId(Long.valueOf(objetoBusqueda));
        historialHCS = organizacion.getHistoria();
        if(historialHCS.size()<1){
          modelo.put("mensaje", "No hay historial para la organizacion.");
          return new ModelAndView(modelo, "home.html.hbs");
        }
        modelo.put("historial", historialHCS);
        return new ModelAndView(modelo, "tablaResultado.html.hbs");
    }
    return null;
  }
}
