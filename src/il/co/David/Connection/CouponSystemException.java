package il.co.David.Connection;



public class CouponSystemException extends Exception {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CouponSystemException(String arg0) {
		super(arg0);
		
		System.out.println("Something's went wrong, sorry.Try again");
		super.printStackTrace();
	}
}