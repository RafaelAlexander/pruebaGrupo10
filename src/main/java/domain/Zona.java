package domain;

import lombok.Getter;
import repository.Organizaciones;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name="zonas")
public class Zona extends PersistentEntity {

  private String nombreProvinciaLocalidad;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_zona")
  @Getter private TipoZona tipoZona;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "zona_id")
  @Getter private List<HistorialHC> historialHCS;

  public Zona(String nombre, TipoZona tipoZona) {
    this.nombreProvinciaLocalidad = nombre;
    this.tipoZona = tipoZona;
  }

  public Zona(){

  }

  public String getNombre() {
    return this.nombreProvinciaLocalidad;
  }

  public List<Organizacion> getOrganizacionesZona() {
    Organizaciones organizaciones = Organizaciones.instancia();
    if(tipoZona == TipoZona.PROVINCIA) {
      return organizaciones
          .getOrganizacionesList()
          .stream()
          .filter(
              unaOrganizacion -> unaOrganizacion.getDireccion().getProvincia() == this.nombreProvinciaLocalidad
          ).collect(Collectors.toList());
    } else {
      return organizaciones
          .getOrganizacionesList()
          .stream()
          .filter(
              unaOrganizacion -> unaOrganizacion.getDireccion().getLocalidad() == this.nombreProvinciaLocalidad
          ).collect(Collectors.toList());
    }
  }

  public Double calcularHCMensual(Month mes, int anio) {
    Double huella;
    List<Organizacion> organizacionesFiltradas = this.getOrganizacionesZona();
    huella = organizacionesFiltradas
        .stream()
        .mapToDouble(
            unaOrganizacion -> unaOrganizacion.getHuellaCarbonoGeneralMensual(mes, anio)
        ).sum();
    return huella;
  }

  public Double getHCActualParticular(){
    Double huella;
    List<Organizacion> organizacionesFiltradas = this.getOrganizacionesZona();
    huella = organizacionesFiltradas
        .stream()
        .mapToDouble(
            unaOrganizacion -> unaOrganizacion.getHuellaCarbonoParticularAnual(LocalDateTime.now().getYear())
        ).sum();
    return huella;
  }

  public Double getHCActualExterna(){
    Double huella;
    List<Organizacion> organizacionesFiltradas = this.getOrganizacionesZona();
    huella = organizacionesFiltradas
        .stream()
        .mapToDouble(
            unaOrganizacion -> unaOrganizacion.getHuellaCarbonoExternaAnual(LocalDateTime.now().getYear())
        ).sum();
    return huella;
  }

  public Double getHCActual(){
    return calcularHCAnual(LocalDate.now().getYear());
  }

  public Double calcularHCAnual(int anio) {
    Double huella;
    List<Organizacion> organizacionesFiltradas = this.getOrganizacionesZona();
    huella = organizacionesFiltradas
        .stream()
        .mapToDouble(
            unaOrganizacion -> unaOrganizacion.getHuellaCarbonoGeneralAnual(anio)
        ).sum();
    return huella;
  }

  public void generarHistorial() {
    LocalDate hoy = LocalDate.now();
    Double hc = this.calcularHCMensual(hoy.getMonth(), hoy.getYear());
    this.historialHCS.add(new HistorialHC(hoy, hc));
  }

  public String getNombreProvinciaLocalidad() {
    return nombreProvinciaLocalidad;
  }
}
