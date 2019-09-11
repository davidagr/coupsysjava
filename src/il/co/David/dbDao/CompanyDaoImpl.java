package il.co.David.dbDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Collection;

import java.util.List;

import il.co.David.Connection.ConnectionPool;
import il.co.David.Connection.CouponSystemException;
import il.co.David.beans.Company;
import il.co.David.beans.Coupon;
import il.co.David.beans.Coupon.CouponType;
import il.co.David.interfaces.CompanyDao;

public class CompanyDaoImpl implements CompanyDao {
	
	private static  ConnectionPool pool = ConnectionPool.getInstance();
	
	Connection con = null;
	

	public CompanyDaoImpl()
	{

	}
	@Override
	public void createCompany(Company company) throws CouponSystemException {

	

		try {
			con = pool.getConnection();
			PreparedStatement stmt = con.prepareStatement(
					"insert into Company"+" values (?, ?, ?, ?)");
			stmt.setLong(1, company.getId());
			stmt.setString(2, company.getCompName());
			stmt.setString(3, company.getPassword());
			stmt.setString(4, company.getEmail());
			stmt.execute();

		} catch (Exception e) {
			throw new CouponSystemException(
					"An error has occured while creating " + company.getCompName()
							+ ", please try again later");
		} finally {
			pool.returnConnection(con);
		}
	}


	@Override
	public void removeCompany(Company company) throws CouponSystemException {
		
		ResultSet rs = null;
		
		String query = "select * from Company where id ="+company.getId();		
		Long coup = null;
		String query1 = "delete  FROM Customer_Coupon WHERE coupon_id="  + coup;
		String query3 = "DELETE FROM Company WHERE id="  + company.getId();
		String query2 = "DELETE * FROM Company_Coupon WHERE id="  + company.getId();
		try {
			con = pool.getConnection();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try
		{
			
			rs = con.createStatement().executeQuery(query);
			
			
			while(rs.next()) {
				coup = rs.getLong("coup_id");
			con.createStatement().execute(query1);}//Delete all Coupons from Customer
			
			
			con.createStatement().executeQuery(query2); //Delete all Coupons from Company_Coupon
			
			con.createStatement().executeQuery(query3);//Delete Company
		
		}catch (SQLException e){
			e.printStackTrace();
		}

		finally {
			// always returning the connection to the Connection Pool 
			pool.returnConnection(con);
		}

	}

	@Override
	public void updateCompany(Company company) throws CouponSystemException{
		
		try {
			con = pool.getConnection();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		try {

			long newid = company.getId();
			String newname = company.getCompName();
			String newpassword = company.getPassword();
			String newemail = company.getEmail();

			// updating all the Coupon fields
			String query = "UPDATE Company SET [id]='" + newid + "', "
					+ "[comp-name]='" + newname + "', " + "[password]='" + newpassword + "', "
					+ "[email]=" + newemail + "' WHERE [id]=" + newid;

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
	public Company getCompany(long id) throws CouponSystemException{
		
		try {
			con = pool.getConnection();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		Company company = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {

			String query = "SELECT * FROM Company WHERE id=" + id;
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			if(rs.next()){
				// creating new Coupon object
				company=new Company();

				// setting the object's fields
				company.setId(rs.getLong("id"));
				company.setPassword(rs.getString("password"));
				company.setCompName(rs.getString("comp-name"));
				
				company.setEmail(rs.getString("email"));
				
			
			}

		}

		catch (SQLException e){
			e.printStackTrace();
		}

		finally {

			pool.returnConnection(con);
		}
		return company;
	}


	@Override
	public Collection<Company> getAllCompanies() throws CouponSystemException{
		
		try {
			con = pool.getConnection();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Statement stmt = null;
		ResultSet rs = null;
		List<Company> allCompanies = new ArrayList<Company>();
		Company company = null;

		try {

			String query = "select * from Company";
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next()){

				company = new Company();
				// setting the object's fields
				company.setId(rs.getLong("id"));
				company.setCompName(rs.getString("comp-name"));
				company.setPassword(rs.getString("password"));
				company.setEmail(rs.getString("email"));
			

				// adding the object to the Coupons Collection
				allCompanies.add(company);
			}


		}

		catch (SQLException e){
			e.printStackTrace();
		}

		finally {

			pool.returnConnection(con);
		}
		return allCompanies;
	}

	
	
	
	
	@Override
public Collection<Coupon> getCoupons(long compId) throws CouponSystemException{
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

			String query = "select * " + "from Company_Coupon cc join Coupon c on cc.coupon_id = c.id "
					+ "where comp_id = "+compId;
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
	public boolean login(String compName, String password) throws CouponSystemException {
		Connection conn = null;
		boolean loginSuccessful = false;

		try {
			conn = pool.getConnection();
			PreparedStatement stmt = 
					conn.prepareStatement("select * from Company where [comp-name] = ? and [password] = ?");
			stmt.setString(1, compName);
			stmt.setString(2, password);
			loginSuccessful = stmt.executeQuery().next();

		} catch (Exception e) {
			throw new CouponSystemException(
					"Unable to complete the login action. please try again");
		} finally {
			pool.returnConnection(conn);
		}

		return loginSuccessful;
	}
	
	public Collection<Coupon> getCouponsByComp(Company company) throws CouponSystemException {
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

			String query = "select * " + "from Company_Coupon cc join Coupon c on cc.coupon_id = c.id "
					+ "where comp_id = "+company.getId();
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
//	@Override
//	public Company getCompanyByName(String name) throws CouponSystemException {
//		
//		try {
//			con = pool.getConnection();
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		
//		Company company = null;
//		Statement stmt = null;
//		ResultSet rs = null;
//		try {
//
//			String query = "SELECT * FROM Company WHERE comp-name = " + name;
//			stmt = con.createStatement();
//			rs = stmt.executeQuery(query);
//
//			if(rs.next()){
//				// creating new Coupon object
//				company=new Company();
//
//				// setting the object's fields
//				company.setId(rs.getLong("id"));
//				company.setCompName(rs.getString("comp-name"));
//				company.setEmail(rs.getString("email"));
//				company.setPassword(rs.getString("password"));
//			
//			}
//
//		}
//
//		catch (SQLException e){
//			e.printStackTrace();
//		}
//
//		finally {
//
//			pool.returnConnection(con);
//		}
//		return company;
//	}

	@Override
	public Company getCompanyByName(String name) throws CouponSystemException {
		Connection conn = null;
		Company company = null;

		try {
			conn = pool.getConnection();
			PreparedStatement stmt = conn.prepareStatement("select * from Company where [comp-name] = ?");
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();

			rs.next();

			company = new Company(rs.getLong("id"), rs.getString("password"),
					rs.getString("comp-name"), rs.getString("email"));

		} catch (Exception e) {
			throw new CouponSystemException(
					"Cannot find company with this name");
		} finally {
			pool.returnConnection(conn);
		}
		return company;
	}

}
