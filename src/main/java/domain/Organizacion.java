package domain;

import domain.archivocsv.DatoActividad;
import domain.exception.PostulanteNoInscriptoException;
import domain.exception.SectorAjenoException;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import domain.recomendaciones.Contacto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.mail.EmailException;
import repository.Organizaciones;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Organizacion extends PersistentEntity{

  @Getter private String razonSocial;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_de_organizacion")
  @Getter private TipoOrganizacion tipoOrganizacion;

  @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
  @JoinColumn(name="direccion_id")
  @Getter private Direccion direccion;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name= "organizacion_id")
  @Getter private List<Sector> sectores = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name= "organizacion_id")
  private List<Postulante> postulantes = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  @Column(name = "clasificacion")
  private ClasificacionOrganizacion clasificacionOrganizacion;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name= "organizacion_id")
  private List<DatoActividad> datosActividades = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name= "organizacion_id")
  private List<Contacto> contactos;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name= "organizacion_id")
  private List<HistorialHC> historia;

  @OneToOne(cascade = CascadeType.MERGE)
  private Usuario usuario;

  public Organizacion(String razonSocial,
                      TipoOrganizacion tipoOrganizacion,
                      Direccion direccion,
                      List<Sector> sectores,
                      ClasificacionOrganizacion clasificacionOrganizacion,
                      List<Contacto> contactos) {

    this.razonSocial = razonSocial;
    this.tipoOrganizacion = tipoOrganizacion;
    this.direccion = direccion;
    this.sectores = sectores;
    this.clasificacionOrganizacion = clasificacionOrganizacion;
    this.contactos = contactos;
  }

  public String getRazonSocial() {
    return razonSocial;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  public void agregarDatosActividades(DatoActividad datoActividad) {
    this.datosActividades.add(datoActividad);
  }

  public void aceptarPostulante(Postulante postulante) {
    if (!postulantes.contains(postulante)) {
      throw new PostulanteNoInscriptoException("Solicitud no encontrada en la organizacion");
    }
    postulante.getSector().agregarMiembro(postulante.getMiembro());
    postulantes.remove(postulante);
  }
  public void rechazarPostulante(Postulante postulante) {
    if (!postulantes.contains(postulante)) {
      throw new PostulanteNoInscriptoException("Solicitud no encontrada en la organizacion");
    }
    postulantes.remove(postulante);
  }

  public List<Sector> getSectores() {
    return sectores;
  }

  public void validarSector(Sector sector) {
    if (!sectores.contains(sector)) {
      throw new SectorAjenoException("Sector no pertenece a organizacion");
    }
  }

  public Boolean poseeEsteSectorConId(Long idSector) {
    return !sectores.stream().filter(
        unSector -> unSector.getId().equals(idSector)
    ).collect(Collectors.toList()).isEmpty();
  }

  public Sector getSectorPorId(Long idSector) {
    return sectores.stream().filter(
        unSector -> unSector.getId().equals(idSector)
        ).findFirst().orElse(null);
  }

  public void agregarPostulante(Postulante postulante) {
    this.postulantes.add(postulante);
  }

  public List<Postulante> getPostulantes() {
    return postulantes;
  }

  public Postulante getPostulantePorMiembroId(Long miembroId) {
    return postulantes.stream().filter(
        unPostulante -> unPostulante.getMiembro().getId().equals(miembroId)
    ).findFirst().orElse(null);
  }

  public Boolean postulanteExistente( Miembro miembro) {
    return this.postulantes.contains(miembro);
  }

  public ClasificacionOrganizacion getClasificacionOrganizacion() {
    return clasificacionOrganizacion;
  }

  public void agregarSectores(List<Sector> sectoresNuevos) {
    this.sectores.addAll(sectoresNuevos);
  }

  public List<DatoActividad> getDatosActividades() {
    return this.datosActividades;
  }

  public double getHuellaCarbonoGeneralMensual(Month mes, int anio) {
    return this.getHuellaCarbonoParticularMensual(mes, anio) + this.getHuellaCarbonoExternaMensual(mes, anio);
  }

  public double getHuellaCarbonoGeneralAnual(int anio) {
    return this.getHuellaCarbonoParticularAnual(anio) + this.getHuellaCarbonoExternaAnual(anio);
  }

  public double getHuellaCarbonoExternaMensual(Month mes, int anio) {
    return this.sectores
        .stream()
        .flatMap(sector -> sector.getMiembros().stream())
        .flatMap(miembro -> miembro.getTrayectos().stream())
        .collect(Collectors.toSet())
        .stream()
        .mapToDouble(trayecto-> trayecto.getHcMensual(this, mes, anio)).sum();
  }

  public double getHuellaCarbonoExternaAnual(int anio) {
   return this.sectores
        .stream()
        .flatMap(sector -> sector.getMiembros().stream())
        .flatMap(miembro -> miembro.getTrayectos().stream())
        .collect(Collectors.toSet())
        .stream()
        .mapToDouble(trayecto-> trayecto.getHcAnual(this, anio)).sum();
  }


  public double getHuellaCarbonoParticularMensual(Month mes, int anio) {
    return this.datosActividades.stream().mapToDouble(datoActividad -> datoActividad.getHCMensual(mes, anio)).sum();
  }

  public double getHuellaCarbonoParticularAnual(int anio) {
    return this.datosActividades.stream().mapToDouble(datoActividad -> datoActividad.getHCAnual(anio)).sum();
  }

  public Map<Sector, Double> getIndicePerSector() {
    Map<Sector, Double> indicePorSector = new HashMap<>();
    this.sectores
        .stream()
        .forEach(sector -> indicePorSector.put(sector, sector.getHCAnual(LocalDate.now().getYear(), this)));
    return indicePorSector;
  }

  public double getHuellaCarbonoPerCapitaMensual(Month mes, int anio) {
    int cantidadMiembros = this.sectores.stream().mapToInt(Sector::getCantidadMiembros).sum();
    if (cantidadMiembros == 0) {
      return 0.00;
    }
    return this.getHuellaCarbonoGeneralMensual(mes, anio) / cantidadMiembros;
  }

  public void enviarMensajeAContactos(String recomendacion) {
    this.contactos.stream().forEach(contacto -> {
      try {
        contacto.enviarRecomendacion(recomendacion);
      } catch (EmailException e) {
        throw new RuntimeException("Error en envio de recomendaciones: " + e);
      }
    });
  }

  public List<HistorialHC> getHistoria() {
    return this.historia;
  }

  public void generarHistorial() {
    LocalDate hoy = LocalDate.now();
    Double hc = this.getHuellaCarbonoGeneralMensual(hoy.getMonth(), hoy.getYear());
    this.historia.add(new HistorialHC(hoy, hc));
  }

  public void agregarContacto(Contacto contacto) {
    this.contactos.add(contacto);
  }

  public void eliminarContacto(Contacto contacto) {
    this.contactos.remove(contacto);
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public String getName() {
    return usuario.getNombreUsuario();
  }

  public void quitarActividadId(Long idActividad) {
    datosActividades.stream()
        .forEach(
            datoActividad -> {
              if (datoActividad.getId().equals(idActividad))
                  datosActividades.remove(datoActividad);
            }
        );
  }
}