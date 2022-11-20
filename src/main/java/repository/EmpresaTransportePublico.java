package repository;

import domain.Linea;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.util.ArrayList;
import java.util.List;

public class EmpresaTransportePublico implements WithGlobalEntityManager {

  public List<Linea> getLineaList() {
    return entityManager()
        .createQuery("from transportes_publicos")
        .getResultList();
  }

  public void agregarLinea(Linea linea) {
    entityManager().persist(linea);
  }
}
