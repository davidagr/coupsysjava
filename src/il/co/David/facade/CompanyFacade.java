package il.co.David.facade;


import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

import il.co.David.Connection.CouponSystemException;
import il.co.David.beans.ClientType;
import il.co.David.beans.Company;
import il.co.David.beans.Coupon;
import il.co.David.beans.Coupon.CouponType;
import il.co.David.beans.Customer;
import il.co.David.dbDao.CompanyDaoImpl;
import il.co.David.dbDao.CouponDaoImpl;
import il.co.David.dbDao.CustomerDaoImpl;
import il.co.David.interfaces.CouponClientFacade;

public class CompanyFacade implements CouponClientFacade {

	@Override
	public CouponClientFacade login(String name, String Password,ClientType type) throws CouponSystemException {
		if (!type.equals(ClientType.COMPANY)) {
			throw new CouponSystemException(" Failed to login.Try again " 
					);
		}
		if (!companyDao.login(name, Password)) {
			throw new CouponSystemException(
					" Failed to log in. Please check your username and password and try again.");
		}
		return this;
	}
	
	private CouponDaoImpl couponDao;
	private CompanyDaoImpl companyDao;
	private CustomerDaoImpl customerDao;
	private Company company;

	public CompanyFacade( Company company, CouponDaoImpl couponDao, CompanyDaoImpl companyDao, CustomerDaoImpl customerDao) {
		this.couponDao = couponDao;
		this.companyDao = companyDao;
		this.customerDao = customerDao;
		this.company = company;
	}

	public void createCoupon(Coupon coupon) throws CouponSystemException {
		Collection<Coupon> coupons = couponDao.getAllCoupons();
		for (Coupon c : coupons) {
			if (c.getTitle().equals(coupon.getTitle())) {
				throw new CouponSystemException(
						"Sorry, coupon with the same title already existing.");
			}
		}

		couponDao.createCoupon(coupon);
		couponDao.addCouponToCompany(company, coupon);
	}

	public void removeCoupon(Coupon coupon) throws CouponSystemException {
		Collection<Customer> customers = customerDao.getCustomersByCoupon(coupon);

		if (customers != null) {
			for (Customer customer : customers) {
				customerDao.removeCouponFromCustomer(customer, coupon);
			}
		}

		couponDao.removeCouponFromCompany(company, coupon);
		couponDao.removeCoupon(coupon);
	}

	public void updateCoupon(Coupon coupon) throws CouponSystemException {

		if (couponDao.getCoupon(coupon.getId()) == null) {
			throw new CouponSystemException(
					"Coupon with such id doesn't exists.");
		}

		couponDao.updateCoupon(coupon);
	}

	public Coupon getCoupon(long couponId) throws CouponSystemException {
		return couponDao.getCoupon(couponId);
	}

	public Collection<Coupon> getAllCoupons() throws CouponSystemException {
		return companyDao.getCouponsByComp(company);
	}

	public Collection<Coupon> getCouponByType(CouponType couponType) throws CouponSystemException {
		Collection<Coupon> coupons = new ArrayList<>();
		Collection<Coupon> everyonesCoupons = couponDao.getCouponByType(couponType);

		for (Coupon coupon : everyonesCoupons) {
			if (couponDao.getCompanyId(coupon) == company.getId()) {
				coupons.add(coupon);
			}
		}

		return coupons;
	}



	public Collection<Coupon> getCouponsBeforeDate(Date date) throws CouponSystemException {
		Collection<Coupon> couponsBeforeDate = new ArrayList<>();
		Collection<Coupon> allCoupons = companyDao.getCoupons(company.getId());

		for (Coupon coupon : allCoupons) {
			if (coupon.getEndDate().compareTo(date) <= 0) {
				couponsBeforeDate.add(coupon);
			}
		}
		return couponsBeforeDate;
	}



}
