package controllers;

import domain.TipoOrganizacion;

public class WrapperTipoOrganizacion {
  private TipoOrganizacion tipoOrganizacion;
  private Double hc = 0.00;
  public WrapperTipoOrganizacion(TipoOrganizacion tipoOrganizacion){
    this.tipoOrganizacion = tipoOrganizacion;
  }

  public void setHc(Double hc) {
    this.hc += hc;
  }

  public Double getHc() {
    return hc;
  }

  public TipoOrganizacion getTipoOrganizacion() {
    return tipoOrganizacion;
  }

  public String getName() {
    return tipoOrganizacion.name();
  }
}
