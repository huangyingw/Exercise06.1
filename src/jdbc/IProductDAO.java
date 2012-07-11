package jdbc;

import java.util.Collection;

public interface IProductDAO {

	public abstract void save(Product product);

	public abstract void update(Product product);

	public abstract Product load(int productNumber);

	public abstract void delete(Product product);

	public abstract Collection<Product> getAllProducts();

}