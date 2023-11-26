package dk.lundogbendsen.dao;


import dk.lundogbendsen.model.Product;

import java.util.List;

public interface ProductRepository {

	public void persist(Product product);
	
	public long countProducts();

	public Product findProduct(Integer id);
	
	public List<Product> findAllProducts(int fromIndex, int count);


}