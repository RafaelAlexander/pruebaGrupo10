package extras;


import javax.persistence.EntityManager;

public interface WithEntityManager {

  EntityManager entityManager();
}
