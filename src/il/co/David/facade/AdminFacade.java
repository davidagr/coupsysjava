package il.co.David.facade;


import java.util.Collection;

import il.co.David.Connection.CouponSystemException;
import il.co.David.beans.ClientType;
import il.co.David.beans.Company;
import il.co.David.beans.Coupon;
import il.co.David.beans.Customer;
import il.co.David.dbDao.CompanyDaoImpl;
import il.co.David.dbDao.CouponDaoImpl;
import il.co.David.dbDao.CustomerDaoImpl;
import il.co.David.interfaces.CouponClientFacade;

public class AdminFacade implements  CouponClientFacade {


	private CompanyDaoImpl companyDao;
	private CustomerDaoImpl customerDao;
	private CouponDaoImpl couponDao;
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "1234";

	public AdminFacade(CompanyDaoImpl companyDao, CustomerDaoImpl customerDao, CouponDaoImpl couponDao) {
		this.companyDao = companyDao;
		this.customerDao = customerDao;
		this.couponDao = couponDao;
	}

	@Override
	public CouponClientFacade login(String name, String Password,ClientType type) throws CouponSystemException {
		if (!(USERNAME.equals(USERNAME) && Password.equals(PASSWORD))) {
			throw new CouponSystemException(
					"Please check your username and password and try again.");
		}

		if (!type.equals(ClientType.ADMIN)) {
			throw new CouponSystemException(
					" Failed to login!");
		}
		return this;
	
	}
	
	
	
	public void createCompany(Company company) throws CouponSystemException {
		Collection<Company> companies = companyDao.getAllCompanies();
		for (Company c : companies) {
			if (c.getCompName().equals(company.getCompName())) {
				throw new CouponSystemException(
						"The company name you've chosen is taken. Please choose another name");
			}
		}
		companyDao.createCompany(company);
	}

	public void removeCompany(Company company) throws CouponSystemException {
		Collection<Coupon> coupons = companyDao.getCouponsByComp(company);

		for (Coupon coupon : coupons) {
			
			Collection<Customer> customersOfCoupon = customerDao.getCustomersByCoupon(coupon);
			
			for (Customer customer : customersOfCoupon) {
				customerDao.removeCouponFromCustomer(customer, coupon);
			}
			
			couponDao.removeCouponFromCompany(company, coupon);
			couponDao.removeCoupon(coupon);
		}
		companyDao.removeCompany(company);
	}

	public void updateCompany(Company company) throws CouponSystemException {
		Company compFromDb = companyDao.getCompany(company.getId());

		if (!company.getCompName().equals(compFromDb.getCompName())) {
			throw new CouponSystemException(
					" Cannot change the company name.");
		}

		companyDao.updateCompany(company);
	}

	public Company getCompany(long companyId) throws CouponSystemException {
		return companyDao.getCompany(companyId);
	}

	public Collection<Company> getAllCompanies() throws CouponSystemException {
		return companyDao.getAllCompanies();
	}

	public void createCustomer(Customer customer) throws CouponSystemException {
		Collection<Customer> customers = customerDao.getAllCustomer();
		for (Customer c : customers) {
			if (c.getCustName() == customer.getCustName()) {
				throw new CouponSystemException(
						" This customer name is taken. Please choose a different name");
			}
		}
		customerDao.createCustomer(customer);
	}

	public void removeCustomer(Customer customer) throws CouponSystemException {
		Collection<Coupon> coupons = customerDao.getCoupons(customer);

		if (coupons != null) {
			for (Coupon coupon : coupons) {
				customerDao.removeCouponFromCustomer(customer, coupon);
			
			}
		}

		customerDao.removeCustomer(customer);
	}

	public void updateCustomer(Customer customer) throws CouponSystemException {
		Customer custFromDb = customerDao.getCustomer(customer.getId());

		if (!custFromDb.getCustName().equals(customer.getCustName())) {
			throw new CouponSystemException(
					"Changing the customer name is forbidden.");
		}
		customerDao.updateCustomer(customer);
	}

	public Customer getCustomer(long customerId) throws CouponSystemException {
		return customerDao.getCustomer(customerId);
	}

	public Collection<Customer> getAllCustomers() throws CouponSystemException {
		return customerDao.getAllCustomer();
	}



}
