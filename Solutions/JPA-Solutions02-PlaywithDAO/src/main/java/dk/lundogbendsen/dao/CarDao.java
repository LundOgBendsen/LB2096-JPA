package dk.lundogbendsen.dao;

import java.util.List;

import dk.lundogbendsen.model.Car;

public interface CarDao {

	public List<Car> getAllCars();
	
	public long countCars();
	
	public List<Car> getCarsByManufactor(String manufactor);

	public void createCar(Car car);
	
	public void removeCar(Car car);
}
