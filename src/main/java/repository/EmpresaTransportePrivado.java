package repository;

import domain.ServicioContratado;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import java.util.List;

public class EmpresaTransportePrivado implements WithGlobalEntityManager {

  public List<ServicioContratado> getServicioContratadoList() {
    return entityManager()
        .createQuery("from servicios_contratados")
        .getResultList();
  }

  public void agregar(ServicioContratado servicioContratado) {
    this.entityManager().persist(servicioContratado);
  }
}
