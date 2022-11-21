package configWeb;

import domain.*;
import domain.archivocsv.Actividad;
import domain.archivocsv.TipoDeConsumo;
import domain.archivocsv.Unidad;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import repository.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Bootstrap implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

	private Usuario crearUsuario(String nombre, String contrasenia){
		GestionUsuarios gestionUsuarios = new GestionUsuarios();
		UsuarioBuilder usuarioAdministradorBuilder = new UsuarioEstandar();
		gestionUsuarios.setUsuarioBuilder(usuarioAdministradorBuilder);
		gestionUsuarios.crearUsuario(nombre, contrasenia);
		Usuario usuario = gestionUsuarios.getUsuario();
		entityManager().persist(gestionUsuarios.getUsuario());
		return usuario;
	}

	private Usuario crearUsuarioAdministrador(String nombre, String contrasenia){
		GestionUsuarios gestionUsuarios = new GestionUsuarios();
		UsuarioBuilder usuarioAdministradorBuilder = new UsuarioAdministrador();
		gestionUsuarios.setUsuarioBuilder(usuarioAdministradorBuilder);
		gestionUsuarios.crearUsuario(nombre, contrasenia);
		Usuario usuario = gestionUsuarios.getUsuario();
		entityManager().persist(gestionUsuarios.getUsuario());
		return usuario;
	}

	private Organizacion crearOrganizacion(Usuario usuario, String razonSocial){
		List<Sector> sectores = Collections.singletonList(new Sector("RRHH"));
		Direccion direccionOrg = new Direccion("Buenos Aires","1","maipu","1231");
		Direcciones.getInstance().agregarDireccion(direccionOrg);
		Organizacion org = new Organizacion(razonSocial,
				TipoOrganizacion.EMPRESA,
				direccionOrg,
				sectores,
				ClasificacionOrganizacion.ESCUELA,
				null);
		org.setUsuario(usuario);
		entityManager().persist(org);
		return org;
	}

	private void crearTipoDeConsumo(String nombre,Unidad unidad,Actividad actividad,int alcance){
		TipoDeConsumo tipoDeConsumo = new TipoDeConsumo(nombre, unidad, actividad, alcance);
	}

	private void crearZona(){
		entityManager().persist(new Zona("Buenos Aires", TipoZona.PROVINCIA));
	}

	public static void main(String[] args) {
		new Bootstrap().run();
	}

	public void run() {
		withTransaction(() -> {
			Usuario organizacion1 = crearUsuario("uCorrectoOrg", "estandarcorrectOrg");
			Usuario organizacion2 = crearUsuario("uCorrectoOrg1", "estandarcorrectOrg1");
			Usuario miembro1 = crearUsuario("usuario2", "soyeldos");
			Usuario miembro2 = crearUsuario("usuario1", "soyeldos1");
			Usuario usuarioAdministrador = crearUsuarioAdministrador( "usuario", "usuarioAdministrador");
			crearTipoDeConsumo("Nafta", Unidad.m3, Actividad.CombustionFija, 100);
			crearTipoDeConsumo("Premium", Unidad.m3, Actividad.CombustionFija, 350);
			crearTipoDeConsumo("Super", Unidad.m3, Actividad.CombustionFija, 600);
			Organizacion organizacion = crearOrganizacion(organizacion1,"La Redonda");
			crearOrganizacion(organizacion2,"Pepita SLA");
			Miembro miembro = crearMiembro(miembro1, organizacion, "Buccellato", "Franco");
			organizacion.agregarPostulante(new Postulante(miembro, organizacion.getSectores().get(0)));
			Postulante postulante = organizacion.getPostulantes().get(0);
			organizacion.aceptarPostulante(postulante);
			crearZona();
		});
	}

	private Miembro crearMiembro(Usuario usuario, Organizacion organizacion, String apellidoMiembro, String nombreMiembro) {
		Direccion direccionHogar = new Direccion("Buenos Aires","1","maipu","1010");
		Miembro miembro = new Miembro(apellidoMiembro, nombreMiembro, TipoDocumento.DNI, 16111222, direccionHogar);
		List<Trayecto> trayectos = crearTrayectos(organizacion,miembro);
		miembro.setUsuario(usuario);
		miembro.setTrayectos(trayectos);
		entityManager().persist(miembro);
		return miembro;
	}

	private List<Trayecto> crearTrayectos(Organizacion organizacion, Miembro miembro) {
		List<Trayecto> trayectos = new ArrayList<>();

		List<Tramo> tramos = crearTramos(organizacion,miembro);
		List<Tramo> tramos1 = crearTramos(organizacion,miembro);
		Trayecto trayectoLun= new Trayecto("Trayecto del Lunes",tramos, LocalDate.now());
		trayectos.add(trayectoLun);
		Trayecto trayectoLun1= new Trayecto("Trayecto del Martes",tramos1, LocalDate.now());
		trayectos.add(trayectoLun1);
		return trayectos;
	}

	private List<Tramo> crearTramos(Organizacion organizacion, Miembro miembro) {
		List<Tramo> tramos = new ArrayList<>();
		Direccion direccionLlegadaBici = new Direccion("Buenos Aires","457","O'Higgins","2516");
		Bicicleta bici = new Bicicleta(miembro.getDireccionHogar(),direccionLlegadaBici);
		Tramo tramoEnBici = new Tramo(organizacion,bici);
		List<Estacion> estacionesDeEjemplo = new ArrayList<Estacion>();
		Estacion luganoPer = lugano();
		Estacion simpreVivaPer = simpreViva();
		Estacion belgranoPer = belgrano();
		estacionesDeEjemplo.add(luganoPer);
		estacionesDeEjemplo.add(simpreVivaPer);
		estacionesDeEjemplo.add(belgranoPer);
		Recorrido belgranoVuelta = new Recorrido(estacionesDeEjemplo, "Belgrano Vuelta");
		Linea linea114 = linea114(belgranoVuelta);
		TransportePublico transportePublico = new TransportePublico(luganoPer,belgranoPer,linea114);
		Tramo tramoEnBici2 = new Tramo(organizacion,transportePublico);
		tramos.add(tramoEnBici);
		tramos.add(tramoEnBici2);
		return tramos;
	}

	public static Estacion lugano() {
		Coordenada coordena = new Coordenada(1111.0, 2222.0);
		Estacion lugano = new Estacion(coordena);
		return lugano;
	}

	public static Estacion simpreViva(){
		Coordenada coordena = new Coordenada(12222.0, 2222.0);
		Estacion lugano = new Estacion(coordena);
		return lugano;
	}

	public static Estacion belgrano() {
		Coordenada coordena = new Coordenada(4444.0, 5555.0);
		Estacion belgrano = new Estacion(coordena);
		return belgrano;
	}

	public Linea linea114(Recorrido recorrido) {
		FactorEmision factorEmision = new FactorEmision(Unidad.km,1);
		FactoresEmision.getInstance().agregarFactorEmision(factorEmision);
		TipoDeConsumo tipoDeConsumo = new TipoDeConsumo("Bondi por km", Unidad.km, Actividad.CombustionFija,3);
		entityManager().persist(tipoDeConsumo);
		Linea linea114 = new Linea(TipoLinea.COLECTIVO, Arrays.asList(recorrido),tipoDeConsumo);
		EmpresaTransportePublico ep = new EmpresaTransportePublico();
		ep.agregarLinea(linea114);
		return linea114;
	}
}
