package domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Entity
@Table(name="trayectos")
@NoArgsConstructor
@AllArgsConstructor
public class Trayecto extends PersistentEntity{

  @Getter @Setter private String nombre;

  @OneToMany(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
  @JoinColumn(name = "trayecto_id")
  @Getter @Setter private List<Tramo> tramos;

  @Getter private LocalDate fecha;

  public double calcularDistancia() {
    return this.tramos.stream().mapToDouble(Tramo::calcularDistancia).sum();
  }

  public double getHcMensual(Organizacion organizacion, Month mes, int anio){
    return fecha.getMonth().equals(mes) && this.fecha.getYear() == anio?
        this.tramos.stream().mapToDouble(tramo->tramo.getHC(organizacion)).sum() : 0;
  }

  public double getHcAnual(Organizacion organizacion, int anio){
    return this.fecha.getYear() == anio?
        this.tramos.stream().mapToDouble(tramo->tramo.getHC(organizacion)).sum() : 0;
  }

}
