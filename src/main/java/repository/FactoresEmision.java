package repository;

import domain.FactorEmision;
import domain.Usuario;
import domain.archivocsv.Unidad;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.util.ArrayList;
import java.util.List;

public class FactoresEmision implements WithGlobalEntityManager {
  private final static FactoresEmision instance = new FactoresEmision();
  private List<FactorEmision> lineaList = new ArrayList<>();

  public static FactoresEmision getInstance() {
    return instance;
  }

  private FactoresEmision() {
  }

  public List<FactorEmision> getLineaList() {
    return entityManager()
        .createQuery("from FactorEmision", FactorEmision.class)
        .getResultList();
  }

  public void agregarFactorEmision(FactorEmision factorEmision) {
    entityManager().persist(factorEmision);
  }

  public FactorEmision obtenerApartirDe(Unidad unidad) {
    return this.getLineaList().stream().filter(factor -> factor.esUnidad(unidad)).findFirst().orElse(null);
  }
}