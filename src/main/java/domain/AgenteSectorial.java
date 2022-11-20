package domain;

import domain.exception.AgenteNoPerteneceZonaException;
import lombok.Getter;
import javax.persistence.*;
import java.util.Objects;

/**
 * Me planteo (por interfaz) que una vez creado un usuario en el sistema y logueado,
 * decido inicializarme como un Agente Sectorial y asociarme a cierta Zona
 */
@Entity
@Table(name="agentes_sectoriales")
public class AgenteSectorial extends PersistentEntity {

  @Getter private final String apellido;

  @Getter private final String nombre;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_documento")
  @Getter private final TipoDocumento tipoDocumento;

  @Getter private final Integer nroDocumento;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name="direccion_id")
  @Getter private final Direccion direccionHogar;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name="zona_id")
  @Getter private Zona zona;

  @OneToOne(cascade = CascadeType.MERGE)
  private Usuario usuario;

  public AgenteSectorial(
      String apellido,
      String nombre,
      TipoDocumento tipoDocumento,
      Integer nroDocumento,
      Direccion direccionHogar
  ) {
    this.apellido = apellido;
    this.nombre = nombre;
    this.tipoDocumento = tipoDocumento;
    this.nroDocumento = nroDocumento;
    this.direccionHogar = direccionHogar;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  public void asociarZona(Zona zona) {
    if(this.pertenezco(zona)) {
      this.zona = zona;
    } else {
      throw new AgenteNoPerteneceZonaException(
          "El Agente que se quiere asociar no pertenece a esa Zona."
      );
    }
  }

  public Boolean pertenezco(Zona zona) {
    return (
        Objects.equals(zona.getTipoZona(),TipoZona.PROVINCIA) & Objects.equals(zona.getNombre(), direccionHogar.getProvincia()) |
            Objects.equals(zona.getTipoZona(),TipoZona.PROVINCIA) & Objects.equals(zona.getNombre(), direccionHogar.getLocalidad())
    );
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public Zona getZona() {
    return zona;
  }
}