package controllers;

import domain.AgenteSectorial;
import domain.Miembro;
import domain.Organizacion;
import domain.TipoUsuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import repository.AgentesSectoriales;
import repository.Miembros;
import repository.Organizaciones;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

public class CalculadoraHCController {
  public ModelAndView getHC(Request request, Response response){
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
          modelo.put("mensaje", "Usted es administrador, no esta autorizado para hacer un calculo HC.");
          return new ModelAndView(modelo, "home.html.hbs");
        }
      } else {
        response.redirect("/login");
      }
    List<String> meses= new ArrayList<>();
    Arrays.stream(Month.values()).forEach(month -> meses.add(month.name()));
    modelo.put("meses", meses);
    return new ModelAndView(modelo, "calculadoraHC.html.hbs");
  }

  public ModelAndView calculoHC(Request request, Response response) {
    Map<String, Object> modelo = new HashMap<>();
    List<String> meses= new ArrayList<>();
    Arrays.stream(Month.values()).forEach(month -> meses.add(month.name()));
    modelo.put("meses", meses);
    String jwt = request.session().attribute("sesionIniciada");
    try {
      if (jwt != null && jwt != "") {
        Jws<Claims> jws = UsuariosController.getDatosDeSesion(jwt);
        String nombre = jws.getBody().get("nombre", String.class);
        String fecha = jws.getBody().get("tiempoExp", String.class);
        String tipo = jws.getBody().get("tipo", String.class);
        Long idUsuario = Long.valueOf(jws.getBody().get("usuario", String.class));
        TipoUsuario tipoUsuario = TipoUsuario.valueOf(tipo);
        if(LocalDateTime.parse(fecha).isBefore(LocalDateTime.now())) {
          request.session().attribute("sesionIniciada", null);
          response.redirect("/login");
        }
        modelo.put("sesionIniciada", nombre);
        if(tipoUsuario.equals(TipoUsuario.ADMINISTRADOR)) {
          modelo.put("mensaje",  "Usted es administrador, no esta autorizado para hacer un calculo HC.");
          return new ModelAndView(modelo,"home.html.hbs");
        }
        String mensaje = "";
        int anio = Integer.valueOf(request.queryParams("anio"));
        String mes = request.queryParams("mes");
        Organizacion organizacion = Organizaciones.instancia().obtenerOrganizacionPorUsuario(idUsuario);
        if(organizacion != null) {
          mensaje =hcOrganizacion(organizacion, mes, anio);
          modelo.put("mensaje", mensaje);
          return new ModelAndView(modelo, "calculadoraHC.html.hbs");
        }
        Miembro miembro = Miembros.getInstance().getMiembroByUsuario(idUsuario);
        if(miembro != null) {
          mensaje = hcMiembro(miembro, mes, anio);
          modelo.put("mensaje", mensaje);
          return new ModelAndView(modelo, "calculadoraHC.html.hbs");
        }
        AgenteSectorial agenteSectorial = AgentesSectoriales.getInstance().getAgenteSectorialByUsuario(idUsuario);
        if(agenteSectorial != null){
          mensaje = hcAgente(agenteSectorial, mes, anio);
          modelo.put("mensaje", mensaje);
          return new ModelAndView(modelo, "calculadoraHC.html.hbs");
        }
        modelo.put("mensaje", "Usted tiene un usuario estandar sin ninguna asociacion. Por favor comuniquese con el servicio al cliente.");
        return new ModelAndView(modelo, "resultado.html.hbs");
      } else {
        response.redirect("/login");
      }
    } catch (Exception e){
      modelo.put("mensaje", e.getMessage());
    }
    return new ModelAndView(modelo, "calculadoraHC.html.hbs");
  }

  public String hcOrganizacion(Organizacion organizacion, String mes, int anio){
    double hc = 0;
    if(mes.equals("N/A")){
      hc = organizacion.getHuellaCarbonoGeneralAnual(anio);
    } else{
      hc = organizacion.getHuellaCarbonoGeneralMensual(Month.valueOf(mes),anio);
    }
    return "Su HC actual es de: "+hc;
  }

  public String hcMiembro(Miembro miembro, String mes, int anio) {
    double hc = 0;
    List<Organizacion> organizacions =  Organizaciones.instancia().getOrganizacionesList();
    if(mes.equals("N/A")){
      hc = organizacions.stream().mapToDouble(org -> miembro.getHCAnual(anio, org)).sum();
    } else{
      hc = organizacions.stream().mapToDouble(org -> miembro.getHCMensual(Month.valueOf(mes), anio, org)).sum();
    }
    return "Su HC actual es de: "+hc;
  }

  public String hcAgente(AgenteSectorial agenteSectorial, String mes, int anio) {
    double hc = 0;
    if(mes.equals("N/A")) {
      hc = agenteSectorial.getZona().calcularHCAnual(anio);
    } else{
      hc = agenteSectorial.getZona().calcularHCMensual(Month.valueOf(mes), anio);
    }
    return "Su HC actual es de: "+hc;
  }
}
