package controllers;

import domain.Miembro;
import domain.Tramo;
import domain.Trayecto;
import domain.Usuario;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import repository.Miembros;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TrayectoController extends BaseController implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

  private Miembro miembro = new Miembro();

  public ModelAndView obtenerTrayectos(Request request, Response response) {

    Map<String, Object> modelo = new HashMap<>();
    this.cargarSesion(request,response,modelo);
    Usuario usuario = obtenerUsuario(request);

    //Obtener el miembro a partir del usuario
    try {
      miembro = Miembros.getInstance().getMiembroConNombreUsuario(usuario.getNombreUsuario());
    } catch (Exception e) {
      //Mostrar Pantalla para que el usuario vaya a postulaciones para ser miembro de una organizacion
      return new ModelAndView(null, "aviso_postulacion.html.hbs");
    }

    //Si obtuve el miembro entonces agrego los trayectos al modelo
    modelo.put("trayectos",miembro.getTrayectos());

    return new ModelAndView(modelo, "trayectos.html.hbs");
  }

  public ModelAndView crearTrayecto(Request request, Response response) {
    Map<String, Object> modelo = new HashMap<>();
    modelo.put("miembro",miembro);
    modelo.put("trayectos",miembro.getTrayectos());
    return new ModelAndView(null, "altaTrayecto.html.hbs");
  }

  public ModelAndView eliminarTrayecto(Request request, Response response) {
    Map<String, Object> modelo = new HashMap<>();
    Trayecto trayectoEliminado = miembro.getTrayectos().stream().filter(t -> t.getId().equals(request.params(":id"))).collect(Collectors.toList()).get(0);
    miembro.getTrayectos().remove(trayectoEliminado);
    modelo.put("trayectos",miembro.getTrayectos());
    return new ModelAndView(modelo, "trayectos.html.hbs");
  }

  public ModelAndView ejecutarAcciones(Request request, Response response) {

    ModelAndView vista = null;

    String accionTrayecto = request.queryParams("accionTrayecto");
    String accionTramo = request.queryParams("accionTramo");

    Trayecto trayectoBuscado = null;
    Tramo tramoBuscado = null;

    //Buscar Trayecto
    if (!accionTrayecto.isEmpty()) {
      trayectoBuscado = obtenerTrayecto(request.queryParams("idTrayecto"));
    }

    //Buscar Tramo
    if (!accionTramo.isEmpty()) {
      tramoBuscado = obtenerTramo(request.queryParams("idTramo"));
      vista = ejecutarAccionesTramos(accionTramo,tramoBuscado);
    } else {
      vista = ejecutarAccionesTrayectos(accionTrayecto, trayectoBuscado);
    }

    return vista;

  }

  private ModelAndView ejecutarAccionesTramos(String accionTramo, Tramo tramoBuscado) {
    ModelAndView vista = null;
    Map<String, Object> modelo = new HashMap<>();
    List<Trayecto> trayectos = new ArrayList<Trayecto>();
    switch (accionTramo) {
      case "eliminar":
        miembro.getTrayectos().stream().forEach(trayecto -> {
          List<Tramo> tramos = new ArrayList<Tramo>();
          trayecto.getTramos().stream().forEach(tramo -> {
            if (!tramo.getId().equals(tramoBuscado.getId())) {
              System.out.println("Tramo a agregar: " + tramo.getId());
              tramos.add(tramo);
            }
          });
          Trayecto trayectoNuevo = trayecto;
          trayectoNuevo.setTramos(tramos);
          trayectos.add(trayectoNuevo);
        });
        miembro.setTrayectos(trayectos);
        modelo.put("trayectos",miembro.getTrayectos());
        vista = new ModelAndView(modelo, "trayectos.html.hbs");
        break;
      case "modificar":
        //sentencias
        break;
      case "agregar":
        //sentencias
        break;
    }
    return vista;
  }

  private ModelAndView ejecutarAccionesTrayectos(String accionTrayecto, Trayecto trayectoBuscado) {
    ModelAndView vista = null;
    Map<String, Object> modelo = new HashMap<>();
    switch (accionTrayecto) {
      case "eliminar":
        miembro.getTrayectos().remove(trayectoBuscado);
        modelo.put("trayectos",miembro.getTrayectos());
        vista = new ModelAndView(modelo, "trayectos.html.hbs");
        break;
      case "modificar":
        vista = new ModelAndView(modelo, "modificacionTrayectos.html.hbs");
        break;
      case "agregar":
        vista = new ModelAndView(modelo, "altaTrayectos.html.hbs");
        break;
    }
    return vista;
  }

  private Tramo obtenerTramo(String idTramo) {
    Long id = Long.valueOf(idTramo);
    List<Tramo> tramos = new ArrayList<Tramo>();
    miembro.getTrayectos().stream().forEach(trayecto ->
        tramos.addAll(trayecto.getTramos().stream().filter(tramo -> tramo.getId().equals(id)).collect(Collectors.toList())));
    return tramos.get(0);
  }

  private Trayecto obtenerTrayecto(String idTrayecto) {
    Long id = Long.valueOf(idTrayecto);
    Trayecto trayectoBuscado = miembro.getTrayectos().stream().filter(trayecto -> trayecto.getId().equals(id)).collect(Collectors.toList()).get(0);
    return trayectoBuscado;
  }
  
}
