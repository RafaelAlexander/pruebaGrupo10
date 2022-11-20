package controllers;

import domain.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import repository.AdministradorUsuarios;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.time.LocalDateTime;
import java.util.*;

public class BaseController {

  public Usuario obtenerUsuario(Request request){
    String jwt = request.session().attribute("sesionIniciada");
    Jws<Claims> jws = UsuariosController.getDatosDeSesion(jwt);;
    Long idUsuario = Long.valueOf(jws.getBody().get("usuario", String.class));
    return AdministradorUsuarios.instancia().obtenerUsuario(idUsuario);
  }

  public void cargarSesion(Request request, Response response, Map<String, Object> modelo){
    String jwt = request.session().attribute("sesionIniciada");
    if (jwt != null && jwt != "") {
      Jws<Claims> jws = UsuariosController.getDatosDeSesion(jwt);
      String fecha = jws.getBody().get("tiempoExp", String.class);
      if (LocalDateTime.parse(fecha).isBefore(LocalDateTime.now())) {
        request.session().attribute("sesionIniciada", null);
        response.redirect("/login");
      }
      Usuario usuario = AdministradorUsuarios.instancia().obtenerUsuario(
          Long.valueOf(jws.getBody().get("usuario", String.class)));
      modelo.put("usuario", usuario);
      modelo.put("sesionIniciada", usuario.getNombreUsuario());
    } else response.redirect("/login");
  }

  public ModelAndView error(Request request, Response response){
    return new ModelAndView(null,"error.html.hbs");
  }

}
