package controllers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import domain.TipoUsuario;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class HomeController extends BaseController {

  public ModelAndView getHome(Request request, Response response) {
    Map<String, Object> modelo = new HashMap<>();
    String jwt = request.session().attribute("sesionIniciada");

    try {
      Jws<Claims> jws = UsuariosController.getDatosDeSesion(jwt);
      if (jwt != null) {
        String nombre = jws.getBody().get("nombre", String.class);
        String fecha = jws.getBody().get("tiempoExp", String.class);
        if(LocalDateTime.parse(fecha).isAfter(LocalDateTime.now())) {
          modelo.put("sesionIniciada", nombre);
        }
      }
    } catch (Exception e){

    }
    return new ModelAndView(modelo, "home.html.hbs");
  }
}
