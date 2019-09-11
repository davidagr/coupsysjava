package il.co.David.interfaces;

import java.util.Collection;

import il.co.David.beans.*;

import il.co.David.Connection.CouponSystemException;
public interface CompanyDao {
	
	/**
	 * Create a new Company in the Company table in the Database
	 */
	public void createCompany(Company company)throws CouponSystemException;
	/**
	 * Remove a Company from Database, including all the company's coupons
	 */
	public void removeCompany(Company company)throws  CouponSystemException;
	/**
	 * Update a Company in the Company table in the Database
	 */
	public void updateCompany(Company company)throws CouponSystemException;
	/**
	 * Get a Company from the Company table in the Database BY ID
	 */
	public Company getCompany(long id)throws CouponSystemException;
	/**
	 * Get all the Companies from the Company table in the Database 
	 */
	public Collection<Company> getAllCompanies()throws CouponSystemException;
	/**
	 * Get ALL the given Company's COUPONS from the company_coupon join table in the Database 
	 */
	public Collection<Coupon> getCoupons(long companyId)throws  CouponSystemException;
	/**
	 * Login: check the name and the password of the company in conjunction with the company table in the Database 
	 */
	public boolean login(String compName, String password)throws  CouponSystemException;
	Company getCompanyByName(String name) throws CouponSystemException;
	
}
