package dk.lundogbendsen.jpa.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class JPAResourceHandler {

	public static void close(EntityManager entityManager){
		if(entityManager != null){
			entityManager.close();
		}
	}

	public static void close(EntityManagerFactory entityManagerFactory){
		if(entityManagerFactory != null){
			entityManagerFactory.close();			
		}
	}
	
}
