package il.co.David.interfaces;


import java.util.Collection;

import il.co.David.Connection.CouponSystemException;

import il.co.David.beans.Coupon;
import il.co.David.beans.Customer;
;



public interface CustomerDao {
	/**
	 * Create a new Customer in the Customer table in the Database
	 */
	public void createCustomer(Customer customer)throws CouponSystemException; 
	/**
	 * Remove a Customer from Database
	 */
	public void removeCustomer(Customer customer)throws CouponSystemException;
	/**
	 * Update a Customer in the Customer table in the Database
	 */
	public void updateCustomer(Customer customer)throws CouponSystemException;
	/**
	 * Get a Customer from the Customer table in the Database BY ID
	 */
	public Customer getCustomer(long id)throws CouponSystemException;
	/**
	 * Get all the Customers from the Database 
	 */
	public Collection<Customer> getAllCustomer()throws CouponSystemException;
	public Collection<Customer> getCustomersByCoupon(Coupon coupon) throws CouponSystemException;
	
	
	
	
	
	
	/**
	 * Get ALL the given Company's COUPONS from the customer_coupon join table in the Database 
	 */
	public Collection<Coupon>  getCoupons(Customer customer)throws CouponSystemException;
	/**
	 * Login: check the name and the password of the Customer in conjunction with the Customer table in the Database 
	 */
	
	
	
	
	public boolean login(String custName,String password)throws CouponSystemException;
	
	public void removeCouponFromCustomer(Customer customer, Coupon coupon) throws CouponSystemException ;
	void purchaseCoupon(Customer customer, Coupon coupon) throws CouponSystemException;
	Customer getCustomerByName(String name) throws CouponSystemException;
}
