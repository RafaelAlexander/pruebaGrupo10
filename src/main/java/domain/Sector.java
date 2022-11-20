package domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
public class Sector extends PersistentEntity{

  @Getter public String name;

  @ManyToMany(cascade = CascadeType.MERGE)
  @Getter private List<Miembro> miembros;

  public Sector(String name) {
    this.name = name;
    this.miembros = new ArrayList<>();
  }

  public void agregarMiembro(Miembro miembro) {
    miembros.add(miembro);
  }

  public int getCantidadMiembros(){
    return miembros.size();
  }

  public double getHCMensual(Month mes, int anio, Organizacion organizacion){
    return this.miembros.stream().mapToDouble(miembro->miembro.getHCMensual(mes, anio,organizacion)).sum();
  }

  public double getHCAnual(int anio, Organizacion organizacion){
    return this.miembros.stream().mapToDouble(miembro->miembro.getHCAnual(anio, organizacion)).sum();
  }
}