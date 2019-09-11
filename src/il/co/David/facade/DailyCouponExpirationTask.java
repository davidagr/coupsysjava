package il.co.David.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import il.co.David.Connection.CouponSystemException;
import il.co.David.beans.Company;
import il.co.David.beans.Coupon;
import il.co.David.beans.Customer;
import il.co.David.dbDao.CompanyDaoImpl;
import il.co.David.dbDao.CouponDaoImpl;
import il.co.David.dbDao.CustomerDaoImpl;

public class DailyCouponExpirationTask implements Runnable{

	private CouponDaoImpl couponDao;
	private CompanyDaoImpl companyDao;
	private CustomerDaoImpl customerDao;
	private static boolean running;

	public DailyCouponExpirationTask(CouponDaoImpl couponDao, CompanyDaoImpl companyDao, CustomerDaoImpl customerDao) {
		this.couponDao = couponDao;
		this.companyDao = companyDao;
		this.customerDao = customerDao;
		running = false;
	}
@Override
	public void run() {
		running = true;

		try {
			Collection<Coupon> coupons = couponDao.getAllCoupons();
			Date today = new Date();

			while (running) {

				for (Coupon coupon : coupons) {

					if (coupon.getEndDate().before(today)) {



						couponDao.removeCoupon(coupon);
					}
				}

				Thread.sleep(24 * 60 * 60 * 1000);
			}

		} catch (CouponSystemException | InterruptedException e) {
			System.out.println("There is no one coupon here");
		}
	}

	

	public static void stopTask() {
		running=false;
		
	}


}
