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

import org.springframework.jdbc.core.RowMapper;
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

	public void update(Product product) {
		NamedParameterJdbcTemplate jdbcTempl = new NamedParameterJdbcTemplate(
				dataSource);
		Map<String, Object> namedParameters = new HashMap<String, Object>();
		namedParameters.put("productnumber", product.getProductnumber());
		namedParameters.put("name", product.getProductName());
		namedParameters.put("price", product.getPrice());

		@SuppressWarnings("unused")
		int updateResult = jdbcTempl
				.update("UPDATE product SET name=:name, price=:price  WHERE productnumber=:productnumber",
						namedParameters);
	}

	public Product load(int productNumber) {
		NamedParameterJdbcTemplate jdbcTempl = new NamedParameterJdbcTemplate(
				dataSource);
		Map<String, Object> namedParameters = new HashMap<String, Object>();
		namedParameters.put("number", productNumber);
		Product product = jdbcTempl.queryForObject(
				"SELECT * FROM Product WHERE number =:number ",
				namedParameters, new RowMapper<Product>() {
					public Product mapRow(ResultSet resultSet, int i)
							throws SQLException {
						return new Product(resultSet.getInt("number"),
								resultSet.getString("name"), resultSet
										.getDouble("price"));
					}
				});
		return product;
	}

	public void delete(Product product) {
		NamedParameterJdbcTemplate jdbcTempl = new NamedParameterJdbcTemplate(
				dataSource);
		Map<String, Object> namedParameters = new HashMap<String, Object>();
		namedParameters.put("number", product.getProductnumber());

		@SuppressWarnings("unused")
		int updateResult = jdbcTempl.update(
				"DELETE FROM product WHERE number =:number", namedParameters);

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
