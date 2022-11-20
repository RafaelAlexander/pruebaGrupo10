package domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name="usuarios")
public class Usuario extends PersistentEntity{
  @Column(name = "nombre_usuario")
  private String nombreUsuario;
  private String contrasenia;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_usuario")
  private TipoUsuario tipoUsuario;

  public Usuario() {
  }

  public void setNombreUsuario(String nombreUsuario) {
    this.nombreUsuario = nombreUsuario;
  }

  public String getNombreUsuario() {
    return nombreUsuario;
  }

  public void setContrasenia(String contrasenia) {
    this.contrasenia = contrasenia;
  }

  public TipoUsuario getTipoUsuario() {
    return tipoUsuario;
  }

  public void setTipoUsuario(TipoUsuario tipoUsuario) {
    this.tipoUsuario = tipoUsuario;
  }

  public String getContrasenia() {
    return contrasenia;
  }
}
