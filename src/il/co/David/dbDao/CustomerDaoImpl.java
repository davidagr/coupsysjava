package il.co.David.dbDao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import il.co.David.Connection.ConnectionPool;
import il.co.David.Connection.CouponSystemException;

import il.co.David.beans.Coupon;
import il.co.David.beans.Customer;
import il.co.David.beans.Coupon.CouponType;
import il.co.David.interfaces.CustomerDao;

public class CustomerDaoImpl implements CustomerDao {
	private static  ConnectionPool pool = ConnectionPool.getInstance();
	
	Connection con = null;
	

	public CustomerDaoImpl()
	{

	}

	@Override
	public void createCustomer(Customer customer) throws CouponSystemException {
		Connection conn = null;

		try {
			conn = pool.getConnection();
			PreparedStatement stmt = conn.prepareStatement(
					"insert into Customer (id, cust_name, password,email) " + "values (?, ?, ?, ?)");
			stmt.setLong(1, customer.getId());
			stmt.setString(2, customer.getCustName());
			stmt.setString(3, customer.getPassword());
			stmt.setString(4, customer.getEmail());
			stmt.execute();
		} catch (Exception e) {
			throw new CouponSystemException(
					"Something has occured while creating customer " + customer.getCustName()
							+ ". Please try again");
		} finally {
			pool.returnConnection(conn);
		}
	}

	@Override
	public void removeCustomer(Customer customer)throws CouponSystemException {
		try {
			con = pool.getConnection();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		try
		{
			//delete the coupon from teh coupon table and the mapping tables			
			String query = "DELETE FROM Customer WHERE id="  + customer.getId();
			

			con.createStatement().executeQuery(query);
		
		}catch (SQLException e){
			e.printStackTrace();
		}

		finally {
			// always returning the connection to the Connection Pool 
			pool.returnConnection(con);
		}

	}


	@Override
	public void updateCustomer(Customer customer)throws CouponSystemException {
		
		
		try {
			con = pool.getConnection();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {

			long newid = customer.getId();
			String newname = customer.getCustName();
			String newpassword = customer.getPassword();
			String newemail = customer.getEmail();

			// updating all the Coupon fields
			String query = "UPDATE Customer SET [id]=" + newid + ", "
					+ "[cust_name]='" + newname + "', " + "[password]='" + newpassword + "', "
					+ "[email]='" + newemail + "' WHERE [id]=" + newid;

			con.createStatement().execute(query);
		}

		catch (SQLException e){

			e.printStackTrace();
		}
		finally {

			pool.returnConnection(con);
		}
	}
	
	@Override
	public void purchaseCoupon(Customer customer, Coupon coupon) throws CouponSystemException {
		
		

		
		try {
			con = pool.getConnection();
			PreparedStatement stmt = con
					.prepareStatement("insert into Customer_Coupon (cust_id, coupon_id) values (?,?)");
			stmt.setLong(1, customer.getId());
			stmt.setLong(2, coupon.getId());

			stmt.execute();
		} catch (Exception e) {
			throw new CouponSystemException(
					"An error has occured while purchasing this coupon.");
		} finally {
			pool.returnConnection(con);
		}
	}
	
	
	@Override
	public void removeCouponFromCustomer(Customer customer, Coupon coupon) throws CouponSystemException {
		Connection conn = null;
		try {
			conn = pool.getConnection();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			conn = pool.getConnection();
			PreparedStatement stmt = conn.prepareStatement("delete from Customer_Coupon where cust_id = ? and coupon_id = ?");
			stmt.setLong(1, customer.getId());
			stmt.setLong(2, coupon.getId());

			stmt.execute();
		} catch (Exception e) {
			throw new CouponSystemException(
					" Some problem occured during this task.Please,try again.");
		} finally {
			pool.returnConnection(conn);
		}
	}

	@Override
	public Customer getCustomer(long id) throws CouponSystemException {
		
		try {
			con = pool.getConnection();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Customer customer = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {

			String query = "SELECT * FROM Customer WHERE id=" + id;
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			if(rs.next()){
				// creating new Coupon object
				customer=new Customer();

				// setting the object's fields
				customer.setId(rs.getLong("id"));
				customer.setCustName(rs.getString("cust-name"));
				customer.setEmail(rs.getString("email"));
				customer.setPassword(rs.getString("password"));
			
			}

		}

		catch (SQLException e){
			e.printStackTrace();
		}

		finally {

			pool.returnConnection(con);
		}
		return customer;
	}




	@Override
	public Collection<Coupon> getCoupons(Customer customer) throws CouponSystemException {
		try {
			con = pool.getConnection();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Statement stmt = null;
		ResultSet rs = null;
		List<Coupon> allCoupons = new ArrayList<Coupon>();
		Coupon coupon = null;

		try {

			String query = "SELECT coupon_id FROM Customer_Coupon WHERE cust_id"+customer.getId();
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next()){

				coupon = new Coupon();
				// setting the object's fields
				coupon.setId(rs.getLong("id"));
				coupon.setTitle(rs.getString("title"));
				coupon.setStartDate(rs.getDate("Start_Date"));
				coupon.setEndDate(rs.getDate("end_date"));
				coupon.setAmount(rs.getInt("amount"));
				
				CouponType couponType = CouponType.valueOf(rs.getString("type"));
				coupon.setType(couponType);
				coupon.setMessage(rs.getString("message"));
				coupon.setPrice(rs.getDouble("price"));
				coupon.setImage(rs.getString("image"));
			

				// adding the object to the Coupons Collection
				allCoupons.add(coupon);
			}


		}

		catch (SQLException e){
			e.printStackTrace();
		}

		finally {

			pool.returnConnection(con);
		}
		return allCoupons;
	}

	@Override
	public boolean login(String custName, String password) throws CouponSystemException {
		Connection conn = null;
		boolean loginSuccessful = false;

		try {
			conn = pool.getConnection();
			PreparedStatement stmt = conn
					.prepareStatement("select * from Customer where cust_name = ? and password = ?");
			stmt.setString(1, custName);
			stmt.setString(2, password);
			loginSuccessful = stmt.executeQuery().next();

		} catch (Exception e) {
			throw new CouponSystemException(
					"Failed to login");
		} finally {
			pool.returnConnection(conn);
		}
		return loginSuccessful;
	}

	
	
	@Override
	public Collection<Customer> getAllCustomer() throws CouponSystemException {
		Connection conn = null;
		try {
			conn = pool.getConnection();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		Collection<Customer> customers = new ArrayList<>();
		ResultSet rows = null;

		try {
			conn = pool.getConnection();
			PreparedStatement stmt = conn.prepareStatement("select * from Customer");
			rows = stmt.executeQuery();

			while (rows.next()) {
				customers.add(new Customer(rows.getLong("id"), rows.getString("cust_name"),
						rows.getString("password"), rows.getString("email")));
			}
		} catch (Exception e) {
			throw new CouponSystemException(
					" We're unable to find customers. Please try again");
		} finally {
			pool.returnConnection(conn);
		}

		return customers;
	}
	
	
	@Override
	public Collection<Customer> getCustomersByCoupon(Coupon coupon) throws CouponSystemException {
		
		
		Collection<Customer> customers = new ArrayList<>();
		Connection conn = null;
		ResultSet rs = null;

		try {
			conn = pool.getConnection();
			PreparedStatement stmt = conn.prepareStatement(
					"select * from Customer_Coupon cc join Customer c on cc.cust_id = c.id where coupon_id = ?");
			stmt.setLong(1, coupon.getId());

			rs = stmt.executeQuery();

			while (rs.next()) {
				customers.add(new Customer(rs.getLong("cust_id"), rs.getString("cust_name"),
						rs.getString("password"), rs.getString("email")));
			}
		} catch (Exception e) {
			throw new CouponSystemException(
					"Something  went wrong while searching for customers that have purchased this coupon. Please try again later");
		} finally {
			pool.returnConnection(conn);
		}
		return customers;
	}

	public Collection<Coupon> getCoupons(long custid) throws CouponSystemException {
		Connection conn = null;
		Collection<Coupon> coupons = new ArrayList<>();
		ResultSet rows = null;

		try {
			conn = pool.getConnection();
			PreparedStatement stmt = conn.prepareStatement("select * " + "from Coupon c join Customer_Coupon cc "
					+ "on c.id = cc.coupon_id " + "where cc.cust_id = ?");
			stmt.setLong(1, custid);
			rows = stmt.executeQuery();

			while (rows.next()) {
				coupons.add(new Coupon(rows.getLong("id"), rows.getString("title"),
						new Date(rows.getDate("Start_Date").getTime()), new Date(rows.getDate("end_date").getTime()),
						rows.getInt("amount"), CouponType.valueOf(rows.getString("type")), rows.getString("message"),
						rows.getFloat("price"), rows.getString("image")));
			}
		} catch (Exception e) {
			throw new CouponSystemException(
					"Some problemm occured during the operation.Coupons cannot be taken from DataBase");
		} finally {
			pool.returnConnection(conn);
		}

		return coupons;
	}


	@Override
	public Customer getCustomerByName(String name) throws CouponSystemException {
		Connection conn = null;
		Customer customer = null;
		try {
			conn = pool.getConnection();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			conn = pool.getConnection();
			PreparedStatement stmt = conn.prepareStatement("select * from Customer where cust_name = ?");
			stmt.setString(1, name);
			ResultSet customerRow = stmt.executeQuery();
			customerRow.next();

			customer = new Customer(customerRow.getLong("id"), customerRow.getString("cust_name"),
					customerRow.getString("password"),customerRow.getString("email"));

		} catch (Exception e) {
			throw new CouponSystemException(
					"Customer called " + name
							+ "cannot be fined");
		} finally {
			pool.returnConnection(conn);
		}

		return customer;
	}

}
