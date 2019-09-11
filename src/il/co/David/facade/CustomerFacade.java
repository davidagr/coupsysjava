package il.co.David.facade;


import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;


import il.co.David.Connection.CouponSystemException;
import il.co.David.beans.ClientType;
import il.co.David.beans.Coupon;
import il.co.David.beans.Coupon.CouponType;
import il.co.David.beans.Customer;
import il.co.David.dbDao.CouponDaoImpl;
import il.co.David.dbDao.CustomerDaoImpl;
import il.co.David.interfaces.CouponClientFacade;

public class CustomerFacade implements CouponClientFacade {


		private CustomerDaoImpl customerDao;
		private CouponDaoImpl couponDao;
		private Customer customer;

		public CustomerFacade(Customer customer, CustomerDaoImpl customerDao, CouponDaoImpl couponDao) {
			this.customerDao = customerDao;
			this.couponDao = couponDao;
			this.customer = customer;
		}

		@Override
		public CouponClientFacade login(String name, String Password,ClientType type) throws CouponSystemException {
			if (!customerDao.login(name, Password)) {
				throw new CouponSystemException(
						"The username or password is incorrect. Please try again");
			}
			if (!type.equals(ClientType.CUSTOMER)) {
				throw new CouponSystemException( "Failed to login ");
			}
			
			return this;}
		
		
		
		public void purchaseCoupon(Coupon coupon) throws CouponSystemException {
			Coupon coupFromDB = couponDao.getCoupon(coupon.getId());
			Date today = new Date(System.currentTimeMillis());

			if (coupFromDB.getAmount() <= 0) {
				throw new CouponSystemException(
						"There is no such a coupon in the DataBase");

			} else if (coupFromDB.getEndDate().before(today)) {
				throw new CouponSystemException(
						"Coupon you've tried to purchase has expired.");
			}
			for (Coupon c : customerDao.getCoupons(customer.getId())) {
				if (c.getId() == coupon.getId()) {
					throw new CouponSystemException(
							"A coupon can not be purchased twice.");
				}
			}

			customerDao.purchaseCoupon(customer, coupon);
			
		}

		public void deleteCoupon(Coupon coupon) throws CouponSystemException {
			customerDao.removeCouponFromCustomer(customer, coupon);
			
		}

		public Collection<Coupon> getAllPurchasedCoupons() throws CouponSystemException {
			return customerDao.getCoupons(customer.getId());
		}

		public Collection<Coupon> getAllPurchasedCouponsByType(CouponType type) throws CouponSystemException {
			Collection<Coupon> coupons = getAllPurchasedCoupons();
			Collection<Coupon> couponsByType = new ArrayList<>();

			for (Coupon coupon : coupons) {
				if (coupon.getType() == type) {
					couponsByType.add(coupon);
				}
			}
			return couponsByType;
		}

		public Collection<Coupon> getAllPurchasedCouponsByPrice(double price) throws CouponSystemException {
			Collection<Coupon> coupons = getAllPurchasedCoupons();
			Collection<Coupon> couponsByPrice = new ArrayList<>();

			for (Coupon coupon : coupons) {
				if (coupon.getPrice() <= price) {
					couponsByPrice.add(coupon);
				}
			}

			return couponsByPrice;
		}

		

}



