package dk.lundogbendsen.client;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import org.apache.log4j.Level;

import dk.lundogbendsen.model.Car;
import dk.lundogbendsen.model.Person;
import dk.lundogbendsen.jpa.util.JPAResourceHandler;
import dk.lundogbendsen.jpa.util.JpaUtil;
import dk.lundogbendsen.string.util.StringUtil;

public class StandaloneJpaClient {

    public static void main(String[] args) {

        JpaUtil.HibernateLoggingConfig loggingConfig = new JpaUtil.HibernateLoggingConfig();
        loggingConfig.setHibernateRootLogLevel(Level.WARN);
        loggingConfig.setLogDdlEnabled(true);
        loggingConfig.setLogSqlEnabled(false);
        loggingConfig.setLogParameterBindingEnabled(false);
        JpaUtil.configureHibernateLogging(loggingConfig);

        try (EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("StandaloneJpaTestPersistenceUnit");
             EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            // Create new JPA entity manager (similar to JDBC Connection)

            exerciseWithJPA(loggingConfig, entityManagerFactory, entityManager);
        }
    }

    public static void exerciseWithJPA(JpaUtil.HibernateLoggingConfig loggingConfig,
                                       EntityManagerFactory entityManagerFactory, EntityManager entityManager) {

        // ///////////////////////////////////////////////////////////////////////
        StringUtil.prettyPrintHeadline("Create and persist some persons");
        // Begin a new JTA transaction
        entityManager.getTransaction().begin();
        // Persist is used to save an entity instance
        Person person = new Person("Tomas");
        // At this moment Tomas is in-memory only (will dissappear at shutdown)
        entityManager.persist(person);
        // Now Tomas is made persistent, which means, that he will stay in the
        // database after the Java program has shutdown.
        // Let's make more person entities and save them to the database.
        entityManager.persist(new Person("Jens"));
        entityManager.persist(new Person("Ole"));
        entityManager.persist(new Person("Lotte"));
        entityManager.persist(new Person("Lise"));
        // Try to commit the current transaction (the commit may fail which will
        // result in the changes made in the transaction will be rollback and an
        // exception will be thrown)
        entityManager.getTransaction().commit();
        // Clear cached entities from the entityManager (called managed entities
        // in JPA). We will talk more about what "managed" means later on...
        entityManager.clear();

        // ///////////////////////////////////////////////////////////////////////
        StringUtil.prettyPrintHeadline("Find all persistent persons");
        entityManager.getTransaction().begin();
        // Notice the similarity to SQL. Altso notice that we can select the
        // full entity object, and not just its fields one by one (as in SQL).
        String jpaql = "select p from Person p";
        // A Query is similar to a Statements in JDBC
        TypedQuery<Person> query = entityManager.createQuery(jpaql, Person.class);
        // Excecute the query and get the result.
        // The returned list is like getting ResultSet in JDBC.
        // Note that we can cast the non-generic list to a generic list!
        List<Person> resultList = (List<Person>) query.getResultList();
        JpaUtil.prettyPrintQueryResult(jpaql, resultList);
        entityManager.getTransaction().commit();
        entityManager.clear();

        // ///////////////////////////////////////////////////////////////////////
        StringUtil.prettyPrintHeadline("Use JPA QL count aggregate function");
        entityManager.getTransaction().begin();
        // Notice the use of the count-function
        jpaql = "select count(p) from Person p";
        TypedQuery<Long> queryCount = entityManager.createQuery(jpaql, Long.class);
        Long personCount = queryCount.getSingleResult();
        JpaUtil.prettyPrintQueryResult(jpaql, personCount);
        entityManager.getTransaction().commit();
        entityManager.clear();

        // ///////////////////////////////////////////////////////////////////////
        StringUtil.prettyPrintHeadline("Use like in select");
        entityManager.getTransaction().begin();
        // % is any number of chars and _ is a single char (as in SQL)
        jpaql = "select p from Person p where p.name like 'L%e'";
        query = entityManager.createQuery(jpaql, Person.class);
        resultList = query.getResultList();
        JpaUtil.prettyPrintQueryResult(jpaql, resultList);
        entityManager.getTransaction().commit();
        entityManager.clear();

        // ///////////////////////////////////////////////////////////////////////
        StringUtil.prettyPrintHeadline("Use named parameter in JPA QL");
        entityManager.getTransaction().begin();
        // Use colon to define a named parameter
        jpaql = "select p from Person p where p.name like :namePattern";
        query = entityManager.createQuery(jpaql, Person.class);
        // Set the value of the named parameter (don't include the colon)
        query.setParameter("namePattern", "%s%");
        System.out.println("Has set JPA QL parameter [:namePattern] to [%s%]");
        resultList = query.getResultList();
        JpaUtil.prettyPrintQueryResult(jpaql, resultList);
        entityManager.getTransaction().commit();
        entityManager.clear();

        // ///////////////////////////////////////////////////////////////////////
        StringUtil.prettyPrintHeadline("Find by entity type and entity primary key");
        entityManager.getTransaction().begin();
        // We can find an antity without any JPA QL if we know its type
        // (e.g. Person.class) and the value of its primary key (here 2 using
        // autoboxing to convert from int to Integer which is the pk type of
        // Person ).
        Person personJens = entityManager.find(Person.class, 2);
        JpaUtil.prettyPrintQueryResult("Using find(entityType, entityPrimaryKey)", personJens);
        entityManager.getTransaction().commit();
        entityManager.clear();

        // ///////////////////////////////////////////////////////////////////////
        StringUtil.prettyPrintHeadline("Removing an entity");
        entityManager.getTransaction().begin();
        Person personToDelete = entityManager.find(Person.class, 2);
        // The remove method is one way of deleting entity instances from the db
        entityManager.remove(personToDelete);
        System.out.println("Has removed Jens");
        jpaql = "select p from Person p";
        query = entityManager.createQuery(jpaql, Person.class);
        resultList = query.getResultList();
        JpaUtil.prettyPrintQueryResult(jpaql, resultList);
        entityManager.getTransaction().commit();
        entityManager.clear();

        // ///////////////////////////////////////////////////////////////////////
        StringUtil.prettyPrintHeadline("Updating an entity");
        entityManager.getTransaction().begin();
        Person personBeforeEdit = entityManager.find(Person.class, 1);
        JpaUtil.prettyPrintQueryResult("Person before edit", personBeforeEdit);
        // One way of editing an entity is to load it from the database
        // and then edit the object directly. JPA will automatically
        // detect the change and write the changed entity state to the
        // database at commit time. This only works if the entity is
        // managed (we will talk about that later on).
        personBeforeEdit.setName("Thomas");
        Person personAfterEdit = entityManager.find(Person.class, 1);
        JpaUtil.prettyPrintQueryResult("Person after edit", personAfterEdit);
        entityManager.getTransaction().commit();
        entityManager.clear();

        // ///////////////////////////////////////////////////////////////////////
        StringUtil.prettyPrintHeadline("Use paging to go through huge result chunk by chunk");
        entityManager.getTransaction().begin();
        // Make more persons to make paging more fun!
        entityManager.persist(new Person("Charlotte"));
        entityManager.persist(new Person("Thomas"));
        entityManager.persist(new Person("Jørgen"));
        entityManager.persist(new Person("Sidsel"));
        entityManager.getTransaction().commit();
        entityManager.clear();

        // How many persons exist in the database?
        entityManager.getTransaction().begin();
        jpaql = "select count(p) from Person p";
        queryCount = entityManager.createQuery(jpaql, Long.class);
        personCount = queryCount.getSingleResult();
        entityManager.getTransaction().commit();
        entityManager.clear();

        // How many pages should we split the result in?
        int totalSize = personCount.intValue();
        int pageSize = 3;
        double noOfPagesAsDouble = ((double) totalSize) / pageSize;
        int noOfPages = (int) Math.ceil(noOfPagesAsDouble);
        // Go through all the persons chunk by chunk
        jpaql = "select p from Person p";
        query = entityManager.createQuery(jpaql, Person.class);
        // We only want 3 persons at a time
        query.setMaxResults(pageSize);
        for (int n = 0; n < noOfPages; n++) {
            entityManager.getTransaction().begin();
            // The index of the first of the 3 persons we want
            query.setFirstResult(n * pageSize);
            resultList = (List<Person>) query.getResultList();
            System.out.println("Page[" + n + "] -> " + resultList);
            entityManager.getTransaction().commit();
            entityManager.clear();
        }


        // Lab01 Persist some cars and retrieve em again
        StringUtil.prettyPrintHeadline("Creating 2 cars");
        entityManager.getTransaction().begin();

        Car c1 = new Car("Austin", "Morris Mascot", 25000);
        entityManager.persist(c1);

        Car c2 = new Car("BMW", "Morris Mini Cooper", 300000);
        entityManager.persist(c2);

        entityManager.getTransaction().commit();

        StringUtil.prettyPrintHeadline("Retrieving cars from the database");
        jpaql = "select c from Car c";
        TypedQuery<Car> carQuery = entityManager.createQuery(jpaql, Car.class);
        List<Car> carList = carQuery.getResultList();

        for (Car car : carList) {
            System.out.println(car);
        }
    }
}