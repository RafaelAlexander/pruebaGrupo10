package domain.archivocsv;

import domain.Organizacion;
import domain.exception.NoCumpleFormatoException;
import domain.exception.NoEsNumeroException;
import domain.exception.NoExisteEnDominioException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import repository.Organizaciones;
import repository.TiposDeConsumo;

public class BuilderDatoActividad {
  private TipoDeConsumo tipoDeConsumo;
  private int consumo;
  private Month mes;
  private int anual;
  private Periodicidad periodicidad;
  private Organizacion organizacion;

  public BuilderDatoActividad(Organizacion organizacion) {
    this.organizacion = organizacion;
  }

  public BuilderDatoActividad(){
  }

  public void lectorCsv(String path){
    List<String> componentesDA = new ArrayList<>();
    InputStream is = getClass().getResourceAsStream("/csv/" + path);
    Scanner sc = new Scanner(is);
    sc.useDelimiter(";");
    while (sc.hasNext()) {
      componentesDA.add(sc.next());
    }
    sc.close();
    if (componentesDA.size() != 4) {
      throw new NullPointerException("Hay campos nulos");
    }
    this.verificarExisteTipoConsumo(componentesDA.get(0));
    this.verificarConsumo(componentesDA.get(1));
    this.agregarPerioricidad(componentesDA.get(2));
    this.verificarPeriodoDeImputacion(componentesDA.get(3));
    System.out.println("Termine CSV antes de Construir");
    this.construir();
  }

  private void verificarExisteTipoConsumo(String nombreTipoConsumo) {
    TiposDeConsumo tiposDeConsumo = TiposDeConsumo.instancia();
    TipoDeConsumo tipoDeConsumo = tiposDeConsumo.getTipoConsumo(nombreTipoConsumo);
    if (tipoDeConsumo == null) {
      throw new NoExisteEnDominioException("No exite este tipo de consumo.");
    }
    this.tipoDeConsumo = tipoDeConsumo;
  }

  private void verificarConsumo(String consumoEscrito) {
    int consumo;
    if (!isNumeric(consumoEscrito)) {
      throw new NoEsNumeroException("No es numero");
    }

    consumo = Integer.parseInt(consumoEscrito);

    if (consumo <= 0) {
      throw new NullPointerException();
    }
    this.consumo = consumo;
  }

  private void verificarPeriodoDeImputacion(String periodoImputacion) {
    Integer[] periodo = verificar(periodoImputacion);
    if (periodo.length == 2) {
      this.mes = Month.of(periodo[0]);
      this.anual = periodo[1];
    } else {
      this.mes = null;
      this.anual = periodo[0];
    }
  }

  private Integer[] verificar(String periodoImputacion) {
    if (isNumeric(periodoImputacion) && periodoImputacion.length() == 4) {
      return new Integer[]{Integer.valueOf(periodoImputacion)};
    }
    String[] mesAnio = periodoImputacion.split("/");
    if (mesAnio[0].length() == 2
        && mesAnio[1].length() == 4
        && isNumeric(mesAnio[0] + mesAnio[1])) {
      return new Integer[]{Integer.valueOf(mesAnio[0]), Integer.valueOf(mesAnio[1])};
    }
    throw new NoCumpleFormatoException("Su formato de periodicidad no cumple formato.");
  }

  private static boolean isNumeric(String maybeNumeric) {
    Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    if (maybeNumeric == null) {
      return false;
    }
    return pattern.matcher(maybeNumeric).matches();
  }

  public BuilderDatoActividad agregarPerioricidad(String perioricidadEscrita) {
    Periodicidad periodicidad = Periodicidad.valueOf(perioricidadEscrita);
    if (periodicidad == null) {
      throw new NullPointerException("Perioricidad dada es incorrecta.");
    }
    this.periodicidad = periodicidad;
    return this;
  }

  public BuilderDatoActividad agregarTipoDeConsumo(String tiposDeConsumo){
    verificarExisteTipoConsumo(tiposDeConsumo);
    return this;
  }

  public BuilderDatoActividad agregarConsumo(String consumo){
    verificarConsumo(consumo);
    return this;
  }

  private void construir() {
    validacion();
    this.organizacion.agregarDatosActividades(
        this.crearActividad());
  }

  private void validacion() {
    if (this.tipoDeConsumo == null
        || this.organizacion == null
        || this.periodicidad == null
        || this.consumo <= 0
        || this.anual < LocalDate.now().getYear()) {
      throw new NullPointerException();
    }
  }

  public DatoActividad crearActividad() {
    DatoActividad datoActividad = new DatoActividad(this.tipoDeConsumo,
        this.consumo,
        this.mes,
        this.anual,
        this.periodicidad);
    organizacion.agregarDatosActividades(datoActividad);
    return datoActividad;
  }

  public BuilderDatoActividad agregarOrganizacion(String org) {
    organizacion = Organizaciones
        .instancia()
        .obtnerOrganizaionPorId(Long.parseLong(org));
    return this;
  }
}
