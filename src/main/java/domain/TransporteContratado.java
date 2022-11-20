package domain;

import domain.archivocsv.TipoDeConsumo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import service.impl.ServiceLocator;
import javax.persistence.*;

@AllArgsConstructor
@Entity
@Table(name ="transportes_contratados")
public class TransporteContratado extends Transporte {

  @ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
  @JoinColumn(name="direccion_origen_id", nullable=false)
  @Getter private Direccion direccionInicio;

  @ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
  @JoinColumn(name="direccion_llegada_id", nullable=false)
  @Getter private Direccion direccionllegada;

  @ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
  @JoinColumn(name="servicio_contratado_id", nullable=false)
  @Getter private ServicioContratado servicioContratado;

  @ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
  @JoinColumn(name="tipo_consumo_id", nullable=false)
  @Getter private TipoDeConsumo tipoDeConsumo;
  
  @Override
  public double calcularDistancia() {
    return ServiceLocator.obtenerInstancia()
        .obtenerCalculadorDeDistancia()
        .obtenerDistancia(direccionInicio,direccionllegada)
        .getValor();
  }

  @Override
  public double getHuellaDeCarbono() {
    return tipoDeConsumo.getUnidadFactor()*calcularDistancia();
  }

}
