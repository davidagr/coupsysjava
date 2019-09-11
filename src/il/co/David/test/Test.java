package il.co.David.test;




import java.sql.Date;

import il.co.David.Connection.ConnectionPool;
import il.co.David.Connection.CouponSystemException;
import il.co.David.beans.ClientType;
import il.co.David.beans.Company;
import il.co.David.beans.Coupon;

import il.co.David.beans.Customer;

import il.co.David.facade.AdminFacade;
import il.co.David.facade.CompanyFacade;
import il.co.David.facade.CouponSystemSingleton;
import il.co.David.facade.CustomerFacade;


public class Test {

	public static void main(String[] args) throws InterruptedException, CouponSystemException {

		CouponSystemSingleton system = CouponSystemSingleton.getInstance();

		try {
			AdminFacade admin = (AdminFacade) system.login("admin", "1234", ClientType.ADMIN);

		Customer some = new Customer(15, "SomeCostumer2", "12345670","cost@mail.ru");
            System.out.println(some);
			admin.createCustomer(some);

			CustomerFacade someFacade = (CustomerFacade) system.login("SomeCostumer2", "12345670",
					ClientType.CUSTOMER);
			System.out.println("Customer " + some.getCustName() + " has been created successfully.");

			Company someCompany = new Company(2, "00345681", "PizzaHut","phut@mail.com");
			System.out.println(someCompany);
//
			admin.createCompany(someCompany);

			CompanyFacade someCompanyFacade = (CompanyFacade) system.login("PizzaHut","00345681",
					ClientType.COMPANY);
			System.out.println("Company  has been logged successfully.");

			Coupon rest = new Coupon(1, "MorePizza",new Date(2018,02,17),  new Date(2020,10,10) , 5, il.co.David.beans.Coupon.CouponType.Food, "Tasty!", 20, "pizzaImage");

			someCompanyFacade.createCoupon(rest);
			System.out.println("The coupon \"" + rest.getTitle() + "\" has been created successfully by "
					+ someCompany.getCompName() + ".");

			someFacade.purchaseCoupon(rest);

			Coupon rest1 = someCompanyFacade.getCoupon(1);
			System.out.println(
					"The coupon \"" + rest1.getTitle() + "\" has been purchased  ");
//			
			System.out.println("PRINT ALREADY SOMETHING!!!!!!!!!!!!!");
//			
		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

}
	}


