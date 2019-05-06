package com.app.ordermanagement.model;
/**
 * Class to hold Order request. Order request consist of order quantity,order price for given 
 * financial instrument.
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.ordermanagement.util.OrderType;

public class Order {

	Logger log = LoggerFactory.getLogger(Order.class);
	
	private int orderid;
	private long orderQuantity;
	private Long price;
	private OrderType orderType = OrderType.MARKET_ORDERS;   
	private long executedQuantity;
	private boolean completed = false;
	
	public Order(int orderid,int orderQuantity,Long price) {
		super();
		this.orderQuantity = orderQuantity;
		this.price = price;
		this.orderid = orderid;
		if(price != null) {
			orderType = OrderType.LIMIT;	
		}
	}

	public Order() {
		super();
	}
	

	@Override
	public String toString() {
		return "Order [log=" + log + ", orderid=" + orderid + ", orderQuantity=" + orderQuantity + ", price=" + price
				+ ", orderType=" + orderType + ", executedQuantity=" + executedQuantity + ", completed=" + completed
				+ "]";
	}

	public int getOrderid() {
		return orderid;
	}

	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public long getOrderQuantity() {
		return orderQuantity;
	}
	
	public void setOrderQuantity(long orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public Long getPrice() {
		return price;
	}
	
	public void setPrice(Long price) {
		this.price = price;
		this.orderType = OrderType.MARKET_ORDERS;
	}
	
	public OrderType getOrderType() {
		return orderType;
	}
	public void setOrderType() {
		if(this.price != null)
			this.orderType = OrderType.LIMIT;
		else
			this.orderType = OrderType.MARKET_ORDERS;
	}
	

	public long getExecutedQuantity() {
		return executedQuantity;
	}

	public void setExecutedQuantity(long executedQuantity) {
		this.executedQuantity = executedQuantity;
	}

	/**
	 * add to executedQuantity and remove from orderQuantity
	 * Mark order as completed if order quantity is zero.
	 * @param executeQuantity
	 * @return
	 */
	public  Order execute(long executeQuantity) {
		executedQuantity += executeQuantity;
		orderQuantity    -= executeQuantity;
		if(orderQuantity == 0) {
			completed = true;
		}
		return this;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + orderid;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (orderid != other.orderid)
			return false;
		return true;
	}
}
