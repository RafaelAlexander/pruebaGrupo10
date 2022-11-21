import configweb.Bootstrap;
import configweb.Routes;

public class Application {

  public static void main(String[] args) {
    Bootstrap.main(args); //carga info por default
    Routes.main(args); //levanta el servidor
  }
}
