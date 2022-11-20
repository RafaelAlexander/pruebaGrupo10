package domain;

import domain.archivocsv.Unidad;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Distancia {
  @Getter private final double valor;
  @Getter private final Unidad unidad = Unidad.km;
}
