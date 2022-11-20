package extras.export;

import org.hibernate.cfg.Configuration;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import java.util.HashMap;

/**
 * Tool for exporting JPA schema. This class depends on Hibernate classes.
 * <p>
 * Run with args: <code>db schema.sql true true</code>
 */
public class JpaSchemaExport {

  public static void main(String[] args) throws Exception {
    execute("db", "squema.sql", true, true);
  }

  public static void execute(String unused, String destination, boolean create, boolean format)
      throws Exception {
    System.out.println("Starting schema export");

    new HibernatePersistenceProvider() {
      {
        EntityManagerFactoryBuilderImpl emfb = (EntityManagerFactoryBuilderImpl) this
            .getEntityManagerFactoryBuilderOrNull(unused, new HashMap<>());
        Configuration hbmcfg = emfb.buildHibernateConfiguration(emfb.buildServiceRegistry());
        SchemaExport schemaExport = new SchemaExport(hbmcfg);
        schemaExport.setOutputFile(destination);
        schemaExport.setFormat(format);
        schemaExport.execute(true, false, false, create);
        System.out.println("Schema exported to " + destination);
      }
    };

  }
}