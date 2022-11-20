package extras.transaction;

import org.uqbarproject.jpa.java8.extras.WithEntityManager;

import javax.persistence.EntityTransaction;
import java.util.function.Supplier;

/**
 * @author flbulgarelli
 * @author gprieto
 */
public interface TransactionalOps extends WithEntityManager {

   default void withTransaction(Runnable action) {
      withTransaction(() -> {
         action.run();
         return null;
      });
   }

   /**
    * Runs an action within a transaction, commiting it if action succeeds, or
    * rollbacking it otherwise
    * 
    * @param action
    *           the action to execute
    * @return the suppliers result
    * @throws RuntimeException
    *            if actions fails with a RuntimeException
    */
   default <A> A withTransaction(Supplier<A> action) {
      beginTransaction();
      try {
         A result = action.get();
         commitTransaction();
         return result;
      } catch (Throwable e) {
         rollbackTransaction();
         throw e;
      }
   }

   default EntityTransaction getTransaction() {
      return entityManager().getTransaction();
   }

   /**
    * Begins a transaction if there is no active current transaction yet.
    * 
    * Unlike {@link EntityTransaction#begin()}, this method never fails with
    * {@link IllegalStateException}
    * 
    * @see EntityTransaction#begin()
    * @return the current active transaction
    */
   default EntityTransaction beginTransaction() {
      EntityTransaction tx = getTransaction();
      if (!tx.isActive()) {
         tx.begin();
      }
      return tx;
   }

   default void commitTransaction() {
      EntityTransaction tx = getTransaction();
      if (tx.isActive()) {
         tx.commit();
      }
   }

   default void rollbackTransaction() {
      EntityTransaction tx = getTransaction();
      if (tx.isActive()) {
         tx.rollback();
      }
   }
}
