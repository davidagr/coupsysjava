package il.co.David.facade;


import il.co.David.Connection.ConnectionPool;
import il.co.David.Connection.CouponSystemException;
import il.co.David.beans.Company;
import il.co.David.beans.Customer;
import il.co.David.dbDao.CompanyDaoImpl;
import il.co.David.dbDao.CouponDaoImpl;
import il.co.David.dbDao.CustomerDaoImpl;
import il.co.David.interfaces.CouponClientFacade;

/**
 * The Project (Coupon System) Entry and End Point: 
 * Enter the system by singleton Login instance, Define the client type, Start the daily task and Shut down the system
 */

public class CouponSystemSingleton extends Thread
{
	private static CouponSystemSingleton instance = new CouponSystemSingleton();
	
	// loading the DAO Objects
	CompanyDaoImpl companyDBDAO = new CompanyDaoImpl();
	CustomerDaoImpl customerDBDAO = new CustomerDaoImpl();
	CouponDaoImpl couponDBDAO = new CouponDaoImpl();
	
	// a private constructor
	private CouponSystemSingleton() {
				
		// starting the Daily Task Thread
		DailyCouponExpirationTask dailyTask = new DailyCouponExpirationTask(couponDBDAO, companyDBDAO, customerDBDAO);
		Thread dailyTaskThread = new Thread(dailyTask);
		dailyTaskThread.start();
		}
	
	// create a Single Instance of the LOGIN to the SYSTEM
	public static final CouponSystemSingleton getInstance(){
		if (instance == null){
			instance = new CouponSystemSingleton();
		}
		return instance;
	}
	
	// LOGIN - applying to the right facade by the client TYPE
	
	public CouponClientFacade login (String name, String password, il.co.David.beans.ClientType clientType) throws CouponSystemException {
		CouponClientFacade facade;
		
		// applying to the right facade by the client TYPE
		if (clientType.name().equals("ADMIN")){
			facade = new AdminFacade(companyDBDAO, customerDBDAO, couponDBDAO);		
			return facade.login(name, password, clientType);
		}

		else if (clientType.name().equals("COMPANY")){
			Company company = companyDBDAO.getCompanyByName(name);
			facade = new CompanyFacade(company, couponDBDAO, companyDBDAO, customerDBDAO);
			return facade.login(name, password, clientType);
		}
		
		else if (clientType.name().equals("CUSTOMER")){
			Customer customer = customerDBDAO.getCustomerByName(name);
			facade = new CustomerFacade(customer, customerDBDAO, couponDBDAO);
			return facade.login(name, password, clientType);
		}
		else {
			facade = null;
			System.out.println("System: The CLIENT TYPE must be: ADMIN or COMPANY or CUSTOMER only!");
		}
		
		
		System.out.println("System: FACADE = " + clientType);
		return facade;
	}

	public void shutdown()throws CouponSystemException {
		// stopping the Daily Task
		DailyCouponExpirationTask.stopTask();
		
		
		// closing all connections
		ConnectionPool.closeAllConnections();
		
	}
	

}
