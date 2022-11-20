package domain.archivocsv;

import domain.PersistentEntity;
import repository.FactoresEmision;
import repository.TiposDeConsumo;

import javax.persistence.*;

@Entity(name = "tipos_consumos")
@Table(name="tipos_consumos")
public class TipoDeConsumo extends PersistentEntity {

  @Column(name = "nombre")
  private String nombre;

  @Enumerated
  private Unidad unidad;

  @Enumerated
  private Actividad actividad;

  @Column(name = "alcance")
  private int alcance;

   public TipoDeConsumo(String nombre,
                        Unidad unidad,
                        Actividad actividad,
                        int alcance) {
     this.nombre = nombre;
     this.unidad = unidad;
     this.alcance = alcance;
     this.actividad = actividad;
     TiposDeConsumo.instancia().agregarTipoConsumo(this);
   }

   public TipoDeConsumo(){

   }

  public String getNombre() {
    return nombre;
  }

  public int getAlcance() {
    return alcance;
  }

  public Actividad getActividad() {
    return actividad;
  }

  public Unidad getUnidad() {
    return unidad;
  }

  public float getUnidadFactor(){
    return FactoresEmision.getInstance().obtenerApartirDe(this.getUnidad()).getFactor();
  }
}
