package il.co.David.dbDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.sql.Date;


import il.co.David.beans.*;
import il.co.David.beans.Coupon.CouponType;
import il.co.David.interfaces.CouponDao;
import il.co.David.Connection.*;


public class CouponDaoImpl implements CouponDao {

	private static  ConnectionPool pool = ConnectionPool.getInstance();
	Connection con = null;
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public CouponDaoImpl()
	{

	}

	@Override
	public void createCoupon(Coupon coupon) throws CouponSystemException {

		try {
			con = pool.getConnection();
			PreparedStatement stmt = con
					.prepareStatement("insert into Coupon (id, title, Start_Date, end_date, "
							+ "amount, type, message, price, image) " + "values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			stmt.setLong(1, coupon.getId());
			stmt.setString(2, coupon.getTitle());
			stmt.setDate(3,new Date(coupon.getStartDate().getTime()));
			stmt.setDate(4,new Date(coupon.getEndDate().getTime()));
			stmt.setInt(5, coupon.getAmount());
			stmt.setString(6, coupon.getType().name());
			stmt.setString(7, coupon.getMessage());
			stmt.setDouble(8, coupon.getPrice());
			stmt.setString(9, coupon.getImage());

			stmt.execute();

		} catch (Exception e) {
			throw new CouponSystemException(
					" Some error  occured during creating the coupon.");
		} finally {
			pool.returnConnection(con);
		}
	}


	@Override
	public long getCompanyId(Coupon coupon) throws CouponSystemException {
		
		ResultSet resultSet = null;
		long companyId;

		try {
			con = pool.getConnection();
			PreparedStatement stmt = con.prepareStatement("select * from Company_Coupon where coupon_id = ?");
			stmt.setLong(1, coupon.getId());
			resultSet = stmt.executeQuery();
			resultSet.next();

			companyId = resultSet.getLong("comp_id");

		} catch (Exception e) {
						throw new CouponSystemException(
					"Could not find the company that created this coupon. Please try again");
		} finally {
			pool.returnConnection(con);
		}
		return companyId;
	}

	@Override
	public void removeCoupon(Coupon coupon) throws CouponSystemException{
		
		try {
			con = pool.getConnection();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		try
		{
			//delete the coupon from teh coupon table and the mapping tables			
			String query1 = "DELETE FROM Company_Coupon WHERE coupon_id="  + coupon.getId();
			String query2 = "DELETE FROM Customer_Coupon WHERE coupon_id=" + coupon.getId();
			String query3 = "DELETE FROM Coupon WHERE id=" + coupon.getId();

			con.createStatement().executeQuery(query1);
			con.createStatement().executeQuery(query2);
			con.createStatement().executeQuery(query3);

		}catch (SQLException e){
			e.printStackTrace();
		}

		finally {
			// always returning the connection to the Connection Pool 
			pool.returnConnection(con);
		}

	}
	@Override
	public void updateCoupon(Coupon coupon)throws CouponSystemException {
		try {
			con = pool.getConnection();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {

			long id = coupon.getId();
			String newTitle = coupon.getTitle();
			Date newStartDate = coupon.getStartDate();
			Date newEndDate = coupon.getEndDate();
			int newAmount = coupon.getAmount();
			CouponType newType = coupon.getType();
			String newMessage = coupon.getMessage();
			double newPrice = coupon.getPrice();
			String newImage = coupon.getImage();

			// updating all the Coupon fields
			String query = "UPDATE Coupon SET [title]='" + newTitle + "', "
					+ "[start_date]='" + dateFormat.format(newStartDate) + "', " + "[end_date]='" + dateFormat.format(newEndDate) + "', "
					+ "[amount]=" + newAmount + ", " + "[type]='" + newType.name() + "', "
					+ "[message]='" + newMessage + "', " + "[price]=" + newPrice + ", "
					+ "[image]='" + newImage + "' WHERE [id]=" + id;

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
	public Collection<Coupon> getCouponByType(CouponType couponType) throws CouponSystemException {
		Collection<Coupon> coupons = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Coupon coupon = null;
		try {
			conn = pool.getConnection();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {

			String query = "SELECT * FROM Coupon WHERE type="+couponType;
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next()){

				coupon = new Coupon();
				// setting the object's fields
				coupon.setId(rs.getLong("id"));
				coupon.setTitle(rs.getString("title"));
				coupon.setStartDate(rs.getDate("Start_Date"));
				coupon.setEndDate(rs.getDate("end_date"));
				coupon.setAmount(rs.getInt("amount"));
				
				coupon.setType(couponType);
				coupon.setMessage(rs.getString("message"));
				coupon.setPrice(rs.getDouble("price"));
				coupon.setImage(rs.getString("image"));

				// adding the object to the Coupons Collection
				coupons.add(coupon);
			}


		
		} catch (Exception e) {
			throw new CouponSystemException(
					"We're unable to load the coupons you've requested. Please try again later");
		} finally {
			pool.returnConnection(conn);
		}

		return coupons;
	}



	@Override
	public void addCouponToCompany(Company company, Coupon coupon) throws CouponSystemException {
		Connection conn = null;

		try {
			conn = pool.getConnection();
			PreparedStatement stmt = conn
					.prepareStatement("insert into Company_Coupon (comp_id, coupon_id) values (?, ?)");
			stmt.setLong(1, company.getId());
			stmt.setLong(2, coupon.getId());

			stmt.execute();

		} catch (Exception e) {
			throw new CouponSystemException(
					"Something must have went wrong while adding your coupons. Please try again");
		} finally {
			pool.returnConnection(conn);
		}
	}

	@Override
	public void removeCouponFromCompany(Company company, Coupon coupon) throws CouponSystemException {
		Connection conn = null;

		try {
			conn = pool.getConnection();
			PreparedStatement stmt = conn
					.prepareStatement("delete from Company_Coupon where comp_id = ? and coupon_id = ?");
			stmt.setLong(1, company.getId());
			stmt.setLong(2, coupon.getId());

			stmt.execute();
		} catch (Exception e) {
			throw new CouponSystemException(
					" An error has occured during removing your coupon. Please try again");
		} finally {
			pool.returnConnection(conn);
		}
	}

	@Override
	public Coupon getCoupon(long id) throws CouponSystemException {
		Coupon coupon = null;
		try {
			con = pool.getConnection();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		Statement stmt = null;
		ResultSet rs = null;
		try {

			String query = "SELECT * FROM Coupon WHERE id=" + id;
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			if(rs.next()){
				// creating new Coupon object
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
			}

		}

		catch (SQLException e){
			e.printStackTrace();
		}

		finally {

			pool.returnConnection(con);
		}
		return coupon;
	}

	@Override
	public Collection<Coupon> getAllCoupons() throws CouponSystemException {

		Collection<Coupon> coupons = new ArrayList<>();
		Connection conn = null;
		ResultSet resultSet = null;

		try {
			conn = pool.getConnection();
			PreparedStatement stmt = conn.prepareStatement("select * from Coupon");
			resultSet = stmt.executeQuery();

			while (resultSet.next()) {
				coupons.add(new Coupon(resultSet.getLong("id"), resultSet.getString("title"),
						resultSet.getDate("Start_Date"), resultSet.getDate("end_date"), resultSet.getInt("amount"),
						CouponType.valueOf(resultSet.getString("type")), resultSet.getString("message"),
						resultSet.getFloat("price"), resultSet.getString("image")));
			}

		} catch (Exception e) {
			throw new CouponSystemException(
					" Something went wrong while loading your coupons. Please try again");
		} finally {
			pool.returnConnection(conn);
		}
		return coupons;
	}

//	@Override
//	public List<Coupon> getAllCoupons() throws CouponSystemException {
//System.out.println("getAllCoupons");
//		
//		 
//		List<Coupon> allCoupons = new ArrayList<Coupon>();
//		Coupon coupon ;
//
//		try {
//
//			String query = "SELECT * FROM Coupon";
//			Statement stmt = con.createStatement();
//			ResultSet rs = stmt.executeQuery(query);
//			while(rs.next()){
//
//				coupon = new Coupon();
//				// setting the object's fields
//				coupon.setId(rs.getLong("id"));
//				coupon.setTitle(rs.getString("title"));
//				coupon.setStartDate(rs.getDate("start_date"));
//				coupon.setEndDate(rs.getDate("end_date"));
//				coupon.setAmount(rs.getInt("amount"));
//				CouponType couponType = CouponType.valueOf(rs.getString("type"));
//				coupon.setType(couponType);
//				coupon.setMessage(rs.getString("message"));
//				coupon.setPrice(rs.getDouble("price"));
//				coupon.setImage(rs.getString("image"));
//
//				// adding the object to the Coupons Collection
//				allCoupons.add(coupon);
//			}
//
//
//		}
//
//		catch (SQLException e){
//			throw new CouponSystemException(
//					"We're unable to load the coupons you've requested. ");
//		}
//
//		finally {
//
//			pool.returnConnectionToPool(con);
//		}
//		return allCoupons;
//	}

	
	
	
	
	
	@Override
	public Collection<Coupon> getCouponsByType(CouponType couponType) throws CouponSystemException {
		Collection<Coupon> coupons = new ArrayList<>();
		Connection conn = null;
		ResultSet resultset = null;

		try {
			conn = pool.getConnection();
			PreparedStatement stmt = conn.prepareStatement("select * from Coupon where type = ?");
			stmt.setString(1, couponType.name());
			resultset = stmt.executeQuery();

			while (resultset.next()) {
				coupons.add(new Coupon(resultset.getLong("id"), resultset.getString("title"),
						resultset.getDate("Start_Date"), resultset.getDate("end_date"), resultset.getInt("amount"),
						CouponType.valueOf(resultset.getString("type")), resultset.getString("message"),
						resultset.getDouble("price"), resultset.getString("image")));
			}
		} catch (Exception e) {
			throw new CouponSystemException(
					"We're unable to load the coupons you've requested. ");
		} finally {
			pool.returnConnection(conn);
		}

		return coupons;
	}

}
