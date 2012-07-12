package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class ProductDAO implements IProductDAO {
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public ProductDAO() {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException e) {
			System.out
					.println("ClassNotFoundException in ProductDAO constructor :"
							+ e.getStackTrace());
		}
	}

	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(
				"jdbc:hsqldb:hsql://localhost/trainingdb", "sa", "");
	}

	public void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.err.println("SQLCloseException in closeConnection(): "
						+ e);
			}
		}
	}

	public void save(Product product) {
		NamedParameterJdbcTemplate jdbcTempl = new NamedParameterJdbcTemplate(
				dataSource);
		Map<String, Object> namedParameters = new HashMap<String, Object>();
		namedParameters.put("productnumber", product.getProductnumber());
		namedParameters.put("name", product.getProductName());
		namedParameters.put("price", product.getPrice());

		@SuppressWarnings("unused")
		int updateResult = jdbcTempl.update(
				"INSERT INTO product VALUES(:productnumber,:name,:price)",
				namedParameters);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jdbc.IProductDAO#update(jdbc.Product)
	 */
	@Override
	public void update(Product product) {
		Connection conn = null;
		PreparedStatement prepareUpdateProduct = null;
		try {
			conn = getConnection();
			prepareUpdateProduct = conn
					.prepareStatement("UPDATE product SET name= ?, price= ? WHERE number=?");
			prepareUpdateProduct.setString(1, product.getProductName());
			prepareUpdateProduct.setDouble(2, product.getPrice());
			prepareUpdateProduct.setInt(3, product.getProductnumber());

			int updateresult = prepareUpdateProduct.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException in ProductDAO update() :" + e);
		} finally {
			try {
				prepareUpdateProduct.close();
				closeConnection(conn);
			} catch (SQLException e1) {
				// no action needed
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jdbc.IProductDAO#load(int)
	 */
	@Override
	public Product load(int productNumber) {
		Product employee = null;
		Connection conn = null;
		PreparedStatement prepareGetProduct = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			prepareGetProduct = conn
					.prepareStatement("SELECT * FROM Product WHERE number = ?");
			prepareGetProduct.setLong(1, productNumber);

			rs = prepareGetProduct.executeQuery();
			if (rs.next()) {
				String name = rs.getString("name");
				double price = rs.getDouble("price");
				rs.close();
				employee = new Product(productNumber, name, price);
			}
		} catch (SQLException e) {
			System.out.println("SQLException in ProductDAO load() :" + e);
		} finally {
			try {
				rs.close();
				prepareGetProduct.close();
				closeConnection(conn);
			} catch (SQLException e1) {
				// no action needed
			}
		}
		return employee;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jdbc.IProductDAO#delete(jdbc.Product)
	 */
	@Override
	public void delete(Product product) {
		Connection conn = null;
		PreparedStatement prepareDeleteProduct = null;

		try {
			conn = getConnection();
			prepareDeleteProduct = conn
					.prepareStatement("DELETE FROM product WHERE number = ?");
			prepareDeleteProduct.setInt(1, product.getProductnumber());

			int updateresult = prepareDeleteProduct.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException in ProductDAO delete() :" + e);
		} finally {
			try {
				prepareDeleteProduct.close();
				closeConnection(conn);
			} catch (SQLException e1) {
				// no action needed
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jdbc.IProductDAO#getAllProducts()
	 */
	@Override
	public Collection<Product> getAllProducts() {
		Collection<Product> productList = new ArrayList<Product>();
		Connection conn = null;
		PreparedStatement prepareGetAllProducts = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			prepareGetAllProducts = conn
					.prepareStatement("SELECT * FROM Product");

			rs = prepareGetAllProducts.executeQuery();
			while (rs.next()) {
				int number = rs.getInt("number");
				String name = rs.getString("name");
				double price = rs.getDouble("price");
				Product product = new Product(number, name, price);
				productList.add(product);
			}
		} catch (SQLException e) {
			System.out.println("SQLException in ProductDAO getAllProducts() :"
					+ e);
		} finally {
			try {
				rs.close();
				prepareGetAllProducts.close();
				closeConnection(conn);
			} catch (SQLException e1) {
				// no action needed
			}
		}
		return productList;
	}

}
