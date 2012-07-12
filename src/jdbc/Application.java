package jdbc;

import java.util.Collection;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"springconfig.xml");
		ProductDAO productDAO = context.getBean("productDAO", ProductDAO.class);
		// create Products
		Product product1 = new Product(1101, "Flatscreen TV", 445.25);
		productDAO.save(product1);
		Product product2 = new Product(1106, "DVD Recorder", 189.40);
		productDAO.save(product2);
		// show Products
		showProducts(productDAO);
		// update Product
		product2.setPrice(175.00);
		// productDAO.update(product2);
		// get Product
		Product product3 = productDAO.load(1106);
		if (product3 != null)
			System.out.println("Get Product: number = "
					+ product3.getProductnumber() + " name = "
					+ product3.getProductName() + " price = "
					+ product3.getPrice());
		// delete Product
		productDAO.delete(product1);
		// show Products
		showProducts(productDAO);
	}

	public static void showProducts(IProductDAO productDAO) {
		Collection<Product> productlist = productDAO.getAllProducts();
		System.out.println("show all Products. We have " + productlist.size()
				+ " Products");
		for (Product product : productlist) {
			System.out.println("Product number = " + product.getProductnumber()
					+ " name = " + product.getProductName() + " price = "
					+ product.getPrice());
		}
	}

}
