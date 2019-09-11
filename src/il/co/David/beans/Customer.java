package il.co.David.beans;

import java.util.List;

public class Customer {
	private long id;
	private String custName;

	
	private String email;
	private List<Coupon> coupons;
	private String password;
	
	public String getPassword() {
		return password;
	}

	
	public void setPassword(String password) {
		this.password = password;
	}

	public Customer() {}
	public Customer(long id, String name, String password, String email) {
		this.id=id;this.password = password; custName=name;
		this.email=email;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<Coupon> getCoupons() {
		return coupons;
	}
	public void setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
	}
	public String toString() {return "The " + getCustName() + "'s coupons are "+ getCoupons();}
	

}
