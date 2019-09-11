package il.co.David.interfaces;

import java.util.Collection;

import il.co.David.Connection.CouponSystemException;
import il.co.David.beans.*;
import il.co.David.beans.Coupon.CouponType;


public interface CouponDao {
	
	/**
	 * Create a new Coupon in the Coupon table in the Database
	 */
	public void createCoupon(Coupon coupon)throws CouponSystemException;
	/**
	 * Remove a Coupon from Database
	 */
	public void removeCoupon(Coupon coupon)throws CouponSystemException;
	/**
	 * Update a Coupon in the Coupon table in the Database
	 */
	public void updateCoupon(Coupon coupon)throws CouponSystemException;
	/**
	 * Get a coupon from the Coupon table in the Database BY ID
	 */
	public Coupon getCoupon(long id)throws CouponSystemException;
	/**
	 * Get all the Coupons from the Coupon table in the Database  
	 */
	public Collection<Coupon> getAllCoupons()throws CouponSystemException;
	/**
	 * Get all the Coupons of the given TYPE from the Coupon table in the Database
	 */
	public long getCompanyId(Coupon coupon) throws CouponSystemException;
	public Collection<Coupon> getCouponByType(CouponType couponType) throws CouponSystemException;
	public Collection<Coupon> getCouponsByType(CouponType couponType) throws CouponSystemException;
	public void addCouponToCompany(Company company, Coupon coupon) throws CouponSystemException;
	public void removeCouponFromCompany(Company company, Coupon coupon) throws CouponSystemException;
}
