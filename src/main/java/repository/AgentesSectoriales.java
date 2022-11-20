package repository;

import domain.AgenteSectorial;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.util.List;

public class AgentesSectoriales  implements WithGlobalEntityManager {
  private final static AgentesSectoriales instance = new AgentesSectoriales();

  public static AgentesSectoriales getInstance() {
    return instance;
  }

  public List<AgenteSectorial> getAgentesList() {
    return entityManager().createQuery("from AgenteSectorial").getResultList();
  }

  public AgenteSectorial getAgenteSectorialByUsuario(Long id) {
    return getAgentesList().stream().filter( aS -> aS.getUsuario().getId().equals(id)).findFirst().orElse(null);
  }
}
