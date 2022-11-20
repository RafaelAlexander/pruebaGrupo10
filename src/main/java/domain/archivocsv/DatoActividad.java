package domain.archivocsv;

import domain.PersistentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Month;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class DatoActividad extends PersistentEntity {

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "tipo_consumo_id")
  @Getter private TipoDeConsumo tipoDeConsumo;

  @Getter private int consumo;

  @Enumerated
  @Getter private Month mes;

  @Getter private int anual;

  @Enumerated
  @Getter private Periodicidad periodicidad;

  public double getHCMensual(Month mesActual,int anioActual){
    System.out.println("Entro hc m");
    if(this.mes.equals(mesActual) && this.anual==anioActual){
      return this.consumo * this.tipoDeConsumo.getUnidadFactor();
    }
    System.out.println("Final");
    return 0;
  }

  public double getHCAnual(int anioActual){
    System.out.println("Entro hc a");
    if(this.anual==anioActual) {
      return this.consumo * this.tipoDeConsumo.getUnidadFactor();
    }
    System.out.println("Final");
    return 0;
  }
}