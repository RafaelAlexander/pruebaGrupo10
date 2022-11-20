package domain;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name ="miembros")
public class Miembro extends PersistentEntity{

  @Getter public String apellido;
  @Getter public String nombre;

  @Enumerated(EnumType.STRING)
  @Getter private TipoDocumento tipoDocumento;
  @Getter private Integer nroDocumento;

  @OneToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
  @JoinColumn(name="direccion_id")
  @Getter private Direccion direccionHogar;

  @OneToMany(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
  @JoinColumn(name= "miembro_id")
  @Getter @Setter private List<Trayecto> trayectos;

  @OneToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
  private Usuario usuario;

  public Miembro(String apellido, String nombre, TipoDocumento tipoDocumento, Integer nroDocumento, Direccion direccionHogar){
    this.apellido =apellido;
    this.nombre = nombre;
    this.tipoDocumento = tipoDocumento;
    this.nroDocumento = nroDocumento;
    this.direccionHogar = direccionHogar;
    this.trayectos = new ArrayList<>();
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  public void agregarTrayecto(Trayecto trayecto) {
    trayectos.add(trayecto);
  }

  public void postularse(Organizacion organizacion, Sector sector) {
    organizacion.validarSector(sector);
    organizacion.agregarPostulante(new Postulante(this, sector));
  }

  public double getHCMensual(Month mes, int anio, Organizacion organizacion){
    return this.trayectos.stream().mapToDouble(trayecto->trayecto.getHcMensual(organizacion, mes, anio)).sum();
  }

  public double getHCAnual(int anio, Organizacion organizacion){
    return this.trayectos.stream().mapToDouble(trayecto->trayecto.getHcAnual(organizacion, anio)).sum();
  }

  public Usuario getUsuario() {
    return usuario;
  }
}