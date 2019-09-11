package il.co.David.beans;

import java.util.List;

public class Company {
private long id;
private String compName;
private String password;
private String email;
private List <Coupon> coupons;
public Company(long _id, String password,String compName,String email) {id=_id;this.compName=compName;this.email=email;this.password=password;}
public Company() {}


public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public String getCompName() {
	return compName;
}
public void setCompName(String compName) {
	this.compName = compName;
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
public String toString () {return "Company name is "+getCompName();}
}
