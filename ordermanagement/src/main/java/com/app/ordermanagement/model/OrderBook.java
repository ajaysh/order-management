package com.app.ordermanagement.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.ordermanagement.exception.ExecutionQuantityExceedsTotalDemand;
import com.app.ordermanagement.exception.OrderBookOpenException;
import com.app.ordermanagement.exception.WrongExecutionPriceException;
import com.app.ordermanagement.util.OrderType;

/**
 * class to hold order book for given financial instrument. It hold valid and invalid orders.
 * @author ajaysharma
 *
 */
public class OrderBook {

	Logger log = LoggerFactory.getLogger(OrderBook.class);
	private String isinNumber;
	private boolean open;
	private long executionPrice = -1;
	private List<Order> validOrders = new ArrayList<Order>(50);
	private List<Order> inValidOrders = new ArrayList<Order>(50);
	private List<Execution> executions = new ArrayList<Execution>(10);
	
	public List<Execution> getExecutions() {
		return executions;
	}

	public List<Order> getValidOrders() {
		return validOrders;
	}

	public List<Order> getInValidOrders() {
		return inValidOrders;
	}

	public OrderBook() {
		super();
	}
	
	public OrderBook(String isinNumber) {
		super();
		this.isinNumber = isinNumber;
		this.open = true;
	}


	public String getIsinNumber() {
		return isinNumber;
	}
	
	public void setIsinNumber(String isinNumber) {
		this.isinNumber = isinNumber;
	}
	
	public boolean isOpen() {
		return open;
	}
	
	public void setOpen(boolean open) {
		this.open = open;
	}
	
	public long getExecutionPrice() {
		return executionPrice;
	}
	
	public void setExecutionPrice(long executionPrice) {
		this.executionPrice = executionPrice;
	}

	/**
	 * validate orders and segregate into valid/invalid orders, based on execution price.
	 * @param execution
	 */
	private void validate(Execution execution) {
		// Check if order book is closed.
		if(this.isOpen()) {
			throw new OrderBookOpenException("Cannot Execute open order book.");
		}

		//subsequent execution prices should match.
		if(this.executionPrice >= 0 && this.executionPrice != execution.getExecutionPrice()) {
			throw new WrongExecutionPriceException("Execution price cannot be:"+execution.getExecutionPrice()+", it should"
					+ " be:"+this.executionPrice);
		}

		log.info("Validating Orders.");
		if(this.getExecutionPrice() > 0) { 
			log.info("orders already executed and validated once.");
			return;
		}
		
		Predicate<Order> limitOrders =a -> (a.getOrderType().equals(OrderType.LIMIT)); 
//		Predicate<Order> marketOrders =a -> (a.getOrderType().equals(OrderType.MARKET_ORDERS)); 
//		Predicate<Order> aboveExecutionPrice = a -> (a.getPrice() >= execution.getExecutionPrice());
		Predicate<Order> belowExectutionPrice = a -> (a.getPrice() < execution.getExecutionPrice());

		inValidOrders = validOrders.stream().filter(limitOrders.and(belowExectutionPrice)).collect(Collectors.toList());
		validOrders.removeIf(o-> inValidOrders.contains(o));
		//validOrders = validOrders.stream().filter(marketOrders.or(aboveExecutionPrice)).collect(Collectors.toList());
		
		log.info("Valid orders after validation");
		validOrders.forEach(System.out::println);

		log.info("Invalid Orders after validation");
		inValidOrders.forEach(System.out::println);
	}
	
	private void setExecutionPrice(Execution execution) {
		this.executionPrice = execution.getExecutionPrice(); // set execution price.
	}

	private void execute(Execution execution) {
		long executionPerOrder = 1;
		long totaldemand = validOrders.stream().filter(o-> (o.isCompleted() == false)).mapToLong(o-> o.getOrderQuantity()).sum();
		
		log.info("Total demand: "+totaldemand);
		
		if(execution.getExecutionQuantity() > totaldemand) {
			throw new ExecutionQuantityExceedsTotalDemand("Execution quantity "+execution.getExecutionQuantity()+" exceeds total demand "+totaldemand);
		}
		while(execution.getExecutionQuantity() != 0) {

			long numberOfValidOpenOrders =
				     validOrders.stream().filter(o -> (o.isCompleted()==false)).count();
			
			log.info("Number of valid orders in order book:"+numberOfValidOpenOrders);
			if(execution.getExecutionQuantity() >= numberOfValidOpenOrders) {
				log.info("1 quantity/order executed for orders(count):"+numberOfValidOpenOrders);
				//executionPerOrder = execution.getExecutionQuantity() % numberOfValidOpenOrders;
				validOrders.stream().filter(o -> (o.isCompleted()==false)).
				 					 map(a -> a.execute(executionPerOrder)).
				 					 collect(Collectors.toList());
				execution.setExecutionQuantity(execution.getExecutionQuantity() - numberOfValidOpenOrders);
				
			}else {
				log.info("Applying Lucky Draw system.");
				//lottery system where execution quantity is less than valid open orders.
				Collections.shuffle(validOrders);
				log.info("1 quantity/order executed for order count:"+execution.getExecutionQuantity()+" out of total orders:"+numberOfValidOpenOrders+" using lottery");
				validOrders.stream().filter(o -> (o.isCompleted()==false)).
									limit(execution.getExecutionQuantity()).
									map(a -> a.execute(executionPerOrder)).
									collect(Collectors.toList());
				
				execution.setExecutionQuantity(0);
				
			}
		}
		
	}

	/**
	 * Three steps execution process. 1. validate. 2. set price. 3. execute
	 * @param execution
	 */
	public void processExecution(Execution execution) {
		this.validate(execution);
		this.setExecutionPrice(execution);
		this.execute(execution);
	}

	public Order getOrderDetails(int orderid) {
		Optional<Order> result  = validOrders.stream().
											 filter(o -> (o.getOrderid()==orderid)).
											 findFirst();
		if(!result.isPresent()) {
			result  = inValidOrders.stream().
									filter(o -> (o.getOrderid()==orderid)).
									findFirst();	
			if(!result.isPresent()) {
				return null;
			}
		}
		return result.get();
	}
	
}
