package jdbc;

public class Product {
	private int productnumber;
	private String productName;
	private double price;

	public Product(int productnumber, String productName, double price) {
		this.productnumber = productnumber;
		this.productName = productName;
		this.price = price;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getProductnumber() {
		return productnumber;
	}

	public void setProductnumber(int productnumber) {
		this.productnumber = productnumber;
	}
}
