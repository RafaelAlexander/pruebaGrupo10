package domain;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name="historiales_huella_carbono")
public class HistorialHC extends PersistentEntity{

  @Getter private LocalDate fecha;

  @Getter private double hcReportado;

  public HistorialHC(LocalDate localDate, double hcReportado){
    this.hcReportado = hcReportado;
    this.fecha = localDate;
  }

  public double getHcReportado() {
    return hcReportado;
  }

  public String getFechaS() {
    return fecha.toString();
  }
}
