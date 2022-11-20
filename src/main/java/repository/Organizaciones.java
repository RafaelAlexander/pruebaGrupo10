package repository;

import domain.*;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class Organizaciones implements WithGlobalEntityManager {

  private final static Organizaciones instance = new Organizaciones();

  public static Organizaciones instancia() {
    return instance;
  }

  private Organizaciones() {
  }

  public void agregar(Organizacion organizacion) {
    this.entityManager().persist(organizacion);
  }

  @SuppressWarnings("unchecked")
  public List<Organizacion> getOrganizacionesList() {
    return entityManager().createQuery("from Organizacion", Organizacion.class).getResultList();
  }

  public double hCPorClasificacionOrganizacionMensual(TipoOrganizacion tipoOrganizacion, Month month, int anio) {
    List<Organizacion> orgs = this.entityManager()
        .createQuery("from Organizacion WHERE clasificacion= :tipo", Organizacion.class)
        .setParameter("tipo", tipoOrganizacion.toString())
        .getResultList();

    return orgs
        .stream()
        .mapToDouble(organizacion -> organizacion.getHuellaCarbonoGeneralMensual(month, anio))
        .sum();
  }

  public double hCPorClasificacionOrganizacionAnual(TipoOrganizacion tipoOrganizacion, int anio) {
    List<Organizacion> orgs = this.entityManager()
        .createQuery("from Organizacion WHERE clasificacion= :tipo", Organizacion.class)
        .setParameter("tipo", tipoOrganizacion.toString())
        .getResultList();

    return orgs.stream()
        .mapToDouble(organizacion -> organizacion.getHuellaCarbonoGeneralAnual(anio))
        .sum();
  }

  public List<Organizacion> getOrganizacionesDeLasCualesNoEsMiembro(Long id) {
    return entityManager()
        .createQuery("from Organizacion where id != :id", Organizacion.class)
        .setParameter("id", id)
        .getResultList();
  }

  public List<Organizacion> getOrganizacionesDeLasCualesEsMiembro(Long id) {
    return entityManager()
        .createQuery("from Organizacion where id = :id", Organizacion.class)
        .setParameter("id", id)
        .getResultList();
  }

  public Organizacion obtnerOrganizaionPorId(Long id) {
    return entityManager()
        .createQuery("from Organizacion where id = :id", Organizacion.class)
        .setParameter("id", id)
        .getSingleResult();
  }

  public Organizacion obtenerOrganizacionPorRazonSocial(String razonSocial) {
    return entityManager()
        .createQuery("from Organizacion where razonSocial = :razonSocial", Organizacion.class)
        .setParameter("razonSocial", razonSocial)
        .getSingleResult();
  }

  public Organizacion obtenerOrganizacionPorUsuario(Long id) {
    return getOrganizacionesList().stream().filter(organizacion -> organizacion.getUsuario().getId().equals(id)).findFirst().orElse(null);
  }

  public List<Organizacion> obtenerOrganizacionesPorUsuario(Long id) {
    List<Organizacion> organizaciones = new ArrayList<>();
    getOrganizacionesList()
        .stream()
        .filter(
            organizacion -> organizacion.getUsuario().getId().equals(id)
        )
        .forEach(
            unaOrganizacion -> organizaciones.add(unaOrganizacion)
        );
    return organizaciones;
  }

  public List<Organizacion> obtenerOrganizacionesDeLasCualesNoEsMiembroPorUsuario(Long id) {
    List<Organizacion> organizaciones = new ArrayList<>();
    getOrganizacionesList()
        .stream()
        .filter(
            organizacion -> !organizacion.getUsuario().getId().equals(id)
        )
        .forEach(
            unaOrganizacion -> organizaciones.add(unaOrganizacion)
        );
    return organizaciones;
  }

  public List<Organizacion> obtenerOrganizacionesDeLasCualesNoEsMiembroPorUsuarioConEsteSector(Long id, Long sectorId) {
    List<Organizacion> organizaciones = new ArrayList<>();
    getOrganizacionesList()
        .stream()
        .filter(
            organizacion -> !organizacion.getUsuario().getId().equals(id)
        )
        .forEach(
            unaOrganizacion -> {
              if(unaOrganizacion.getSectorPorId(sectorId) != null) {
                organizaciones.add(unaOrganizacion);
              }
            }
        );
    return organizaciones;
  }

}
