package dk.lundogbendsen.dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import dk.lundogbendsen.model.Car;

public class CarDaoImpl implements CarDao {

	private EntityManager entityManager;
	

	public long countCars() {
		TypedQuery<Long> q = entityManager.createQuery("select count(c) from Car c", Long.class);
		return q.getSingleResult();
	}

	public List<Car> getAllCars() {
		TypedQuery<Car> q = entityManager.createQuery("select c from Car c", Car.class);
		return q.getResultList();
	}

	public List<Car> getCarsByManufactor(String manufactor) {
		TypedQuery<Car> q = entityManager.createQuery("select c from Car c where c.make = :make", Car.class);
		q.setParameter("make", manufactor);
		return q.getResultList();
	}

	public void createCar(Car car) {
		entityManager.persist(car);
	}

	public void removeCar(Car car) {
		entityManager.remove(car);
	}

	public void setEntityManager(EntityManager manager){
		this.entityManager = manager;
	}
}
