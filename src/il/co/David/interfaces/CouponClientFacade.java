package il.co.David.interfaces;


import il.co.David.Connection.CouponSystemException;
import il.co.David.beans.ClientType;

/**
 * Facade Interface: Login to the coupon system according to the Client Type (ADMIN/COMPANY/CUSTOMER)
 */

public interface CouponClientFacade {

	public CouponClientFacade login (String name, String Password, ClientType type) throws CouponSystemException;

}
