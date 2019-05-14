package com.app.ordermanagement.model;
import java.time.LocalDateTime;

/**
 * Class to hold Order request. Order request consist of order quantity,order price for given 
 * financial instrument.
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.ordermanagement.util.OrderStatus;
import com.app.ordermanagement.util.OrderType;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * Immutable Order class containing order id, Order Quantity,Price,Entry Date and Order Type information.
 * @author ajaysharma
 *
 */
public final class Order {

	Logger log = LoggerFactory.getLogger(Order.class);
	
	private final int orderid;
	private final long orderQuantity;
	private final OrderType orderType;   

	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm") //e.g   "entryDate": "2017-01-01 20:00"
	private final LocalDateTime entryDate;
	private final Long price;
	private final long executedQuantity;
	private final OrderStatus orderStatus;
	
	public Order(int orderid, long orderQuantity, OrderType orderType, LocalDateTime entryDate, Long price, 
				long executedQuantity, OrderStatus orderStatus) {
		super();
		this.orderid = orderid;
		this.orderQuantity = orderQuantity;
		this.orderType = orderType;
		this.entryDate = entryDate;
		this.price = price;
		this.executedQuantity = executedQuantity;
		this.orderStatus = orderStatus;
	}

	public LocalDateTime getEntryDate() {
		return entryDate;
	}

	public int getOrderid() {
		return orderid;
	}


	public long getOrderQuantity() {
		return orderQuantity;
	}
	
	public Long getPrice() {
		return price;
	}
	
	public OrderType getOrderType() {
		return orderType;
	}
	
	public long getExecutedQuantity() {
		return executedQuantity;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	
	/**
	 * add to executedQuantity and remove from orderQuantity
	 * Mark order as completed if order quantity is zero.
	 * @param executeQuantity
	 * @return
	 */
	public  Order execute(long executeQuantity) {
		
		long newExecutedQuantity = this.executedQuantity + executeQuantity;
		long newOrderQuantity = this.orderQuantity - executeQuantity;
		OrderStatus newOrderStatus;
		if(newOrderQuantity == 0) {
			newOrderStatus = OrderStatus.EXECUTED;
		}else{
			newOrderStatus = OrderStatus.PARTIALLY_EXECUTED;
		}
		
		return new Order(this.orderid, newOrderQuantity, this.orderType, this.entryDate, this.price, 
				 newExecutedQuantity,  newOrderStatus);
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
