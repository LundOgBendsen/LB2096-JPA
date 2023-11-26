JPA-Ex04-SameDAOinEJBandJavaSE.

Demonstrate how the same DAO can be used in a standalone Java app and an EJB.

The client.StandaloneJpaClient has a main method that uses the dao.PersonRepositoryImpl by injecting an EntityManager.

Notice that the persistence.xml defines a resource local (jdbc) persistence unit and a jta unit (for appservers).

The dao.PersonRepository is a Remote EJB interface that's implemented by PersonRepositoryImpl.
The same goes for PersonService and PersonServiceImpl.

How to run:

client.EjbRmiClient:
1. start wildfly server
2. Remove StandaloneJpaTestPersistenceUnit from persistence.xml (comment it out using <!-- ... -->)
3. mvn install -P mvn-deploy
4. mvn install -P ejb-client-build
5. rightclick at pom.xml > Maven > Reload project
6. Run client.EjbRmiClient as a java app

client.StandaloneJpaClient:
1. Include StandaloneJpaTestPersistenceUnit in persistence.xml (comment it in)
2. mvn install
3. right click at pom.xml > Maven > Reload project
4. Run client.StandaloneJpaClient as a java app

See: https://www.baeldung.com/wildfly-ejb-jndi
