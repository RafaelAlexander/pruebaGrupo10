package repository;

import domain.archivocsv.TipoDeConsumo;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;


import java.util.List;

public class TiposDeConsumo implements WithGlobalEntityManager {

  private final static TiposDeConsumo instancia = new TiposDeConsumo();

  private TiposDeConsumo(){
  }

  public static TiposDeConsumo instancia(){
    return instancia;
  }

  public void agregarTipoConsumo(TipoDeConsumo tipoDeConsumo) {
    entityManager().persist(tipoDeConsumo);
  }

  public List<TipoDeConsumo> getTipoConsumos() {
    return entityManager().createQuery("from tipos_consumos", TipoDeConsumo.class).getResultList();
  }

  public TipoDeConsumo getTipoConsumo(String nombre) {
    return entityManager().createQuery("from tipos_consumos  WHERE nombre= :name", TipoDeConsumo.class)
        .setParameter("name", nombre)
        .setMaxResults(1)
        .getSingleResult();
  }
  public TipoDeConsumo getTipoConsumoId(Long id) {
    return entityManager().createQuery("from tipos_consumos where id = :id", TipoDeConsumo.class)
        .setParameter("id", id)
        .getSingleResult();
  }
}
