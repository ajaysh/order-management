package com.app.ordermanagement.dto;

import java.time.LocalDateTime;

import com.app.ordermanagement.util.OrderStatus;
import com.app.ordermanagement.util.OrderType;
import com.fasterxml.jackson.annotation.JsonFormat;

public final class OrderDTO {

	private  int orderid;
	private long orderQuantity;
	private OrderType orderType;   

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm") //e.g   "entryDate": "2017-01-01 20:00"
	private LocalDateTime entryDate;
	private Long price;
	private long executedQuantity;
	private OrderStatus orderStatus;


	
	public OrderDTO(int orderid, long orderQuantity, OrderType orderType, LocalDateTime entryDate, Long price
					, long executedQuantity , OrderStatus orderStatus ) {
		super();
		this.orderid = orderid;
		this.orderQuantity = orderQuantity;
		this.entryDate = entryDate;
		this.price = price;
		this.executedQuantity  = executedQuantity;
		this.orderStatus = orderStatus;
		this.orderType = orderType;
	}

	
	
	public OrderDTO() {
		super();
	}



	public long getExecutedQuantity() {
		return executedQuantity;
	}

	public void setExecutedQuantity(long executedQuantity) {
		this.executedQuantity = executedQuantity;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}


	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}

	public void setOrderQuantity(long orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
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
	
	public void setPrice(Long price) {
		this.price = price;
		if(price != null && price > 0) {
			this.orderType = OrderType.LIMIT;
		}else
		{
			this.orderType = OrderType.MARKET_ORDERS;
		}
	}

	public OrderType getOrderType() {
		return orderType;
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
		OrderDTO other = (OrderDTO) obj;
		if (orderid != other.orderid)
			return false;
		return true;
	}

}
