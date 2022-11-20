package controllers;

import domain.Miembro;
import domain.TipoUsuario;
import domain.Usuario;
import domain.TipoDocumento;
import domain.Direccion;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import repository.AdministradorUsuarios;
import repository.Miembros;
import repository.Organizaciones;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class UsuariosController implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

  public ModelAndView getFormularioLogin(Request request, Response response) {
    return new ModelAndView(null, "login.html.hbs");
  }

  public ModelAndView iniciarSesion(Request request, Response response) {
    String nombre = request.queryParams("nameUser");
    String contra = request.queryParams("passUser");
    //Por body
    try {
    Usuario usuario = AdministradorUsuarios.instancia().obtenerUsuarioPorName(nombre);
    if (usuario.getContrasenia().equals(contra)) {
        SecretKey key = Keys.hmacShaKeyFor("fTLnCU8H#8gA%1CzVAgDLtz@5VJmyPQdh5v9*#yk3".getBytes(StandardCharsets.UTF_8));
        String jwt = Jwts.builder().setSubject("InicioHuellaCarbono")
            .claim("tiempoExp", LocalDateTime.now().plusMinutes(30).toString())
            .claim("usuario", usuario.getId().toString())
            .claim("nombre", usuario.getNombreUsuario())
            .claim("tipo", usuario.getTipoUsuario().toString())
            .signWith(key).compact();
        request.session().attribute("sesionIniciada", jwt);
    }
    response.redirect("/");

    } catch (Exception e) {
      response.redirect("/login");
    }
    return null;
  }

  public ModelAndView logout(Request request, Response response){
    request.session().attribute("sesionIniciada", null);
    response.redirect("/");
    return null;
  }

  public static Jws<Claims> getDatosDeSesion(String jwt){
    Jws<Claims> jws;
    SecretKey key = Keys.hmacShaKeyFor("fTLnCU8H#8gA%1CzVAgDLtz@5VJmyPQdh5v9*#yk3".getBytes(StandardCharsets.UTF_8));
    jws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
    return jws;
  }

  public ModelAndView crearUsuario(Request request, Response response){
   withTransaction(()->{
     try{
       Usuario usuario = new Usuario();
       usuario.setTipoUsuario(TipoUsuario.ESTANDAR);
       usuario.setNombreUsuario(request.queryParams("user"));
       usuario.setContrasenia(request.queryParams("password"));
       usuario.setContrasenia(request.queryParams("password"));
       AdministradorUsuarios.instancia().agregarUsuario(usuario);
       if(Miembros.getInstance().getMiembroByUsuario(usuario.getId()) != null) {
         response.redirect("/error");
       }
       Direccion direccion =
           new Direccion(
               request.queryParams("provincia"),
               request.queryParams("localidad"),
               request.queryParams("calle"),
               request.queryParams("altura")
           );
       Miembro nuevoMiembro = new Miembro(
           request.queryParams("apellido"),
           request.queryParams("nombre"),
           TipoDocumento.valueOf(request.queryParams("tipoDocumento")),
           Integer.valueOf(request.queryParams("numeroDocumento")),
           direccion
       );
       nuevoMiembro.setUsuario(usuario);
       Miembros.getInstance().agregarMiembro(nuevoMiembro);
     }catch(Exception e){
       response.redirect("/error");
     }
    });
   response.redirect("/login");
   return null;
  }

  public ModelAndView registrarUsuario(Request request, Response response) {
    return new ModelAndView(null,"registrar.html.hbs");
  }
}
