package controllers;

import domain.Organizacion;
import domain.TipoUsuario;
import domain.recomendaciones.Recomendacion;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import repository.Organizaciones;
import repository.Recomendaciones;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecomendacionController implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {
  public ModelAndView getRecomendacion(Request request, Response response) {
    Map<String, Object> modelo = new HashMap<>();
    String jwt = request.session().attribute("sesionIniciada");
    TipoUsuario tipoUsuario = null;
    Long idUsuario = null;
    if (jwt != null && jwt != "") {
      Jws<Claims> jws = UsuariosController.getDatosDeSesion(jwt);
      String nombre = jws.getBody().get("nombre", String.class);
      String fecha = jws.getBody().get("tiempoExp", String.class);
      String tipo = jws.getBody().get("tipo", String.class);
      idUsuario = Long.valueOf(jws.getBody().get("usuario", String.class));
      tipoUsuario = TipoUsuario.valueOf(tipo);
      if (LocalDateTime.parse(fecha).isBefore(LocalDateTime.now())) {
        request.session().attribute("sesionIniciada", null);
        response.redirect("/login");
      }
      modelo.put("sesionIniciada", nombre);
    } else {
      response.redirect("/login");
    }
    if (tipoUsuario.name().equals(TipoUsuario.ADMINISTRADOR.name())) {
      return new ModelAndView(modelo, "recomendacion.html.hbs");
    } else {
      Organizacion organizacion = Organizaciones.instancia().obtenerOrganizacionPorUsuario(idUsuario);
      if (organizacion == null) {
        modelo.put("mensaje", "Usted no es una organizacion");
        return new ModelAndView(modelo, "home.html.hbs");
      } else {
        List<Recomendacion> recomendacionList = Recomendaciones.instancia().obtenerRecomendaciones();
        if(recomendacionList.size() == 0){
          modelo.put("mensaje", "No hay recomendaciones para usted");
          return new ModelAndView(modelo, "home.html.hbs");
        }
        modelo.put("esOrg", true);
        modelo.put("recomendaciones", recomendacionList);
        return new ModelAndView(modelo, "recomendacion.html.hbs");
      }
    }
  }

  public ModelAndView settearRecomendacion(Request request, Response response) {
    Map<String, Object> modelo = new HashMap<>();
    String jwt = request.session().attribute("sesionIniciada");
    TipoUsuario tipoUsuario = null;
    Long idUsuario = null;
    if (jwt != null && jwt != "") {
      Jws<Claims> jws = UsuariosController.getDatosDeSesion(jwt);
      String nombre = jws.getBody().get("nombre", String.class);
      String fecha = jws.getBody().get("tiempoExp", String.class);
      String tipo = jws.getBody().get("tipo", String.class);
      idUsuario = Long.valueOf(jws.getBody().get("usuario", String.class));
      tipoUsuario = TipoUsuario.valueOf(tipo);
      if (LocalDateTime.parse(fecha).isBefore(LocalDateTime.now())) {
        request.session().attribute("sesionIniciada", null);
        response.redirect("/login");
      }
      modelo.put("sesionIniciada", nombre);
      if (! tipoUsuario.equals(TipoUsuario.ADMINISTRADOR)) {
        modelo.put("mensaje", "Usted no es Administrador");
        return new ModelAndView(modelo, "home.html.hbs");
      }
    } else {
      response.redirect("/login");
    }
    try {
      String nombre = request.queryParams("nombre");
      String link = request.queryParams("link");

      withTransaction(() -> {
        Recomendaciones.instancia().agregarRecomendacion(new Recomendacion(link, nombre));
      });
      modelo.put("mensaje", "Se guardo el link de " + nombre);
    } catch (Exception e) {
      modelo.put("mensaje", "Hubo un error en la creacion. Detalle: " + e.getMessage());
    }
    return new ModelAndView(modelo, "home.html.hbs");
  }
}
