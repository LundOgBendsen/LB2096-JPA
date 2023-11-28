JPA-Solutions10-RemotingAndDetachment

Solution for lab 10.


How to run:

Make sure database is running.

client.EjbRmiClient:
1. start wildfly server
3. mvn install -P mvn-deploy
4. mvn install -P ejb-client-build
5. rightclick at pom.xml > Maven > Reload project
6. Run client.EjbRmiClient as a java app

See: https://www.baeldung.com/wildfly-ejb-jndi
