package dk.lundogbendsen.examples;

import java.util.Date;
import java.util.Set;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import org.apache.log4j.Level;

import dk.lundogbendsen.jpa.util.JPAResourceHandler;
import dk.lundogbendsen.jpa.util.JpaUtil;
import dk.lundogbendsen.model.CourseInstance;
import dk.lundogbendsen.model.Student;

public class BeanValidationStandaloneClient {

	public static void main(String[] args) {
		
		JpaUtil.HibernateLoggingConfig loggingConfig = new JpaUtil.HibernateLoggingConfig();
		loggingConfig.setHibernateRootLogLevel(Level.OFF);
		loggingConfig.setLogDdlEnabled(true);
		loggingConfig.setLogSqlEnabled(false);
		loggingConfig.setLogParameterBindingEnabled(false);
		JpaUtil.configureHibernateLogging(loggingConfig);

		EntityManagerFactory emf = null;
		EntityManager em = null;
		try{
			// Create new JPA entity manager (similar to JDBC Connection)
			emf = Persistence.createEntityManagerFactory("MyPersistenceUnit");
			em = emf.createEntityManager();
			exerciseWithJPA(loggingConfig, emf, em);
		}finally{
			// ///////////////////////////////////////////////////////////////////////
			// Free resources used by the JPA entity manager and entity manager factory
			JPAResourceHandler.close(em);
			// Necessary with JPA 2.1 / Hibernate 5.2.2
			JPAResourceHandler.close(emf);
		}		
	}
	
	public static void exerciseWithJPA(JpaUtil.HibernateLoggingConfig loggingConfig, 
			EntityManagerFactory emf, EntityManager em){
				
		persist(em, new Student());
		persist(em, new Student("Kasper Sørensen", null, null));
		persist(em, new Student("Kasper Sørensen", "kasper@lundogbendsen.com",
				null));
		persist(em, new Student("Kasper Sørensen", "kasper@lundogbendsen.dk",
				null));

		CourseInstance course = new CourseInstance(null, new Date());
		persist(em, course);
		persist(em, new Student("Kasper Sørensen", "kasper@lundogbendsen.dk",
				course));
	}

	private static void persist(EntityManager em, Object o) {
		System.out.println("******* Gemmer: " + o + " *******");
		try {
			em.getTransaction().begin();
			em.persist(o);
			em.getTransaction().commit();
			System.out.println("  Det gik godt!");
		} catch (ConstraintViolationException e) {
			System.out.println("  Der skete en fejl: " + e.getMessage());
			Set<ConstraintViolation<?>> constraintViolations = e
					.getConstraintViolations();
			for (ConstraintViolation<?> constraintViolation : constraintViolations) {
				System.out.println("   - "
						+ constraintViolation.getPropertyPath() + ": "
						+ constraintViolation.getMessage());
			}

			em.getTransaction().rollback();
		}
		System.out.println();
	}
}
