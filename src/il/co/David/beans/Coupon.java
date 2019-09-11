package il.co.David.beans;

import java.sql.Date;


public class Coupon {
	public enum CouponType{Restaurants,
		Electricity,
		Food,
		Health,
		Sports,
		Camping,
		Travelling,
		Books,
		Entertainment}
	
	private long id;
	private String title;
	private Date startDate;
	private Date endDate;
	private int amount;
	private CouponType type;
	private double price;
	private String image;
	private String message;
	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	//_____________________________________
	public Coupon(long id,  String title,Date startDate, Date endDate) {
		this.id=id;this.title=title;this.startDate=startDate;this.endDate=endDate;
	}//C-tor
	
	public Coupon() {};//C-tor
	
	public Coupon(long id, String title, Date startDate, Date enDate, int amount, CouponType type, String message,
			double price, String image) {
		this.id=id;this.title=title;this.startDate=startDate;endDate=enDate;this.amount=amount;this.type=type;this.message=message;
		this.price=price;this.image=image;
	}//C-tor

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public CouponType getType() {
		return type;
	}
	public void setType(CouponType type) {
		this.type = type;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String toString() {return "The coupon with id "+ getId() + " costs "+getPrice();}//toString

}
