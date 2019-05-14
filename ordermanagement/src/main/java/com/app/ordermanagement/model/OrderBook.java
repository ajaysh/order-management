package com.app.ordermanagement.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.app.ordermanagement.exception.ExecutionQuantityExceedsTotalDemand;
import com.app.ordermanagement.exception.OrderBookOpenException;
import com.app.ordermanagement.exception.WrongExecutionPriceException;
import com.app.ordermanagement.util.OrderBookStatus;
import com.app.ordermanagement.util.OrderStatus;
import com.app.ordermanagement.util.OrderType;

/**
 * class to hold order book for given financial instrument. It hold valid and invalid orders.
 * @author ajaysharma
 *
 */

@Component
@Scope("prototype")
public class OrderBook {

	Logger log = LoggerFactory.getLogger(OrderBook.class);

	private String isinNumber;
	private OrderBookStatus status = OrderBookStatus.OPEN;  
	private long executionPrice = -1;
	private List<Order> validOrders = new ArrayList<Order>(50);

	private List<Order> inValidOrders = new ArrayList<Order>(50);
	private List<Execution> listOfExecutions = new ArrayList<Execution>(10);
	private boolean validated = false;

	public OrderBook() {
		super();
	}
	

	public boolean isValidated() {
		return validated;
	}


	public void setValidated(boolean validated) {
		this.validated = validated;
	}

	public void removeInvalidOrders() {
		validOrders.removeIf(o-> inValidOrders.contains(o));
	}

	public List<Execution> getExecutions() {
		return listOfExecutions;
	}

	public List<Order> getValidOrders() {
		return validOrders;
	}

	public List<Order> getInValidOrders() {
		return inValidOrders;
	}

	public OrderBook(String isinNumber) {
		super();
		this.isinNumber = isinNumber;
	}

	public String getIsinNumber() {
		return isinNumber;
	}
	
	public void setIsinNumber(String isinNumber) {
		this.isinNumber = isinNumber;
	}
	
	public long getExecutionPrice() {
		return executionPrice;
	}
	
	public void setExecutionPrice(long executionPrice) {
		this.executionPrice = executionPrice;
	}

	public OrderBookStatus getStatus() {
		return status;
	}

	public void setValidOrders(List<Order> validOrders) {
		this.validOrders = validOrders;
	}


	public void setInValidOrders(List<Order> inValidOrders) {
		this.inValidOrders = inValidOrders;
	}

	public void setStatus(OrderBookStatus status) {
		this.status = status;
	}
	
}
