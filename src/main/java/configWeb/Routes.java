package configweb;

import controllers.*;

import static spark.Spark.after;
import static spark.Spark.before;

import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class Routes {

  public static void main(String[] args) {

    new configweb.Bootstrap().run();
    Spark.port(9090);
    Spark.staticFileLocation("/public");

    HandlebarsTemplateEngine engine = new HandlebarsTemplateEngine();
    HomeController homeController = new HomeController();
    UsuariosController usuariosController = new UsuariosController();
    PostulacionesController postulacionesController = new PostulacionesController();
    ActividadController actividadController = new ActividadController();
    CalculadoraHCController calculadoraHCController = new CalculadoraHCController();
    ReportesController reportesController = new ReportesController();
    TrayectoController trayectoController = new TrayectoController();
    RecomendacionController recomendacionController = new RecomendacionController();

    //Rutas: Login
    Spark.get("/", homeController::getHome, engine);
    Spark.get("/login", usuariosController::getFormularioLogin, engine);
    Spark.post("/login", usuariosController::iniciarSesion, engine);
    Spark.get("/logout",usuariosController::logout,engine);//post cambio colateral
    Spark.get("/register",usuariosController::registrarUsuario,engine);
    Spark.post("/register", usuariosController::crearUsuario, engine);
    Spark.get("/error",homeController::error);

    //Postulaciones
    Spark.get("/postulaciones", postulacionesController::getPostulacionesPendientes, engine);
    Spark.post( "/postulaciones/nueva", postulacionesController::crearPostulacion, engine);
    Spark.post( "/postulaciones/aceptar", postulacionesController::aceptarPostulacion, engine);
    Spark.post( "/postulaciones/rechazar", postulacionesController::rechazarPostulacion, engine);

    //Rutas: Dato Actividad
    Spark.get("/altaActividad",actividadController::obtenerFormularioCreacion,engine);
    Spark.get("/organizaciones/:id/actividades/:idActividad",actividadController::obtenerActividad, engine);
    Spark.get("/organizaciones/:id/actividades",actividadController::obtenerActividades, engine);
    Spark.post("/organizaciones/:id/actividades/:idActividad/borrar",actividadController::borrarActividad, engine);
    Spark.post("/actividades/nueva",actividadController::crearActividad,engine);
    Spark.post("/error",actividadController::error,engine);

    //Rutas:: Calculo de HC
    Spark.get("/hc", calculadoraHCController::getHC, engine);
    Spark.post("/hc", calculadoraHCController::calculoHC, engine);

    //Rutas:: Reportes
    Spark.get("/reportes", reportesController::getSeleccionReporte, engine);
    Spark.post("/reporte", reportesController::redireccionReporte, engine);
    Spark.post("/hcs",reportesController::verReporteEspecifico, engine);

    //Rutas: Trayectos
    Spark.get("/trayectos", (request, response) -> trayectoController.obtenerTrayectos(request, response), engine);
    Spark.post("/trayectos/accion", (request, response) -> trayectoController.ejecutarAcciones(request, response), engine);
    Spark.delete("/trayectos/:id",(request, response) -> trayectoController.eliminarTrayecto(request, response), engine);

    //Rutas: Recomendaciones
    Spark.get("/recomendaciones", recomendacionController::getRecomendacion, engine);
    Spark.post("/recomendaciones", recomendacionController::settearRecomendacion, engine);

    after((request, response) -> {
      PerThreadEntityManagers.getEntityManager();
      PerThreadEntityManagers.closeEntityManager();
    });
  }

}
