package dk.lundogbendsen.dao;

import dk.lundogbendsen.model.Supplier;

import java.util.List;


public interface SupplierRepository {

	public void persist(Supplier supplier);

	public Supplier findSupplier(Integer id);

	public List<Supplier> findAllWithName(String name);

	public long countSuppliers();

	public List<Supplier> findAllSuppliers(int fromIndex, int count);

	public void removeSupplier(Supplier supplier);
}