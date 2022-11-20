package domain;

import domain.archivocsv.TipoDeConsumo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import repository.FactoresEmision;
import service.impl.ServiceLocator;
import javax.persistence.*;

@AllArgsConstructor
@Entity
@Table(name="vehiculos_particulares")
public class VehiculoParticular extends Transporte {

  @ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
  @JoinColumn(name="direccion_origen_id", nullable=false)
  @Getter private Direccion direccionInicio;

  @ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
  @JoinColumn(name="direccion_llegada_id", nullable=false)
  @Getter private Direccion direccionLlegada;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_vehiculo")
  @Getter private TipoVehiculo tipoVehiculo;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_combustible")
  @Getter private TipoCombustible tipoCombustible;

  @ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
  @JoinColumn(name="tipo_consumo_id", nullable=false)
  @Getter private TipoDeConsumo tipoDeConsumo;

  @Override
  public double calcularDistancia() {
    return
        ServiceLocator.obtenerInstancia()
            .obtenerCalculadorDeDistancia()
            .obtenerDistancia(this.direccionInicio,this.direccionLlegada).getValor();
  }

  @Override
  public double getHuellaDeCarbono() {
    FactorEmision factorEmision = FactoresEmision.getInstance().obtenerApartirDe(tipoDeConsumo.getUnidad());
    return tipoDeConsumo.getUnidadFactor()*calcularDistancia();
  }

}