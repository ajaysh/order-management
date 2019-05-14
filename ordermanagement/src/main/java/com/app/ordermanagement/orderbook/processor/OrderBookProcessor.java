package com.app.ordermanagement.orderbook.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.app.ordermanagement.exception.ExecutionQuantityExceedsTotalDemand;
import com.app.ordermanagement.exception.OrderBookOpenException;
import com.app.ordermanagement.exception.WrongExecutionPriceException;
import com.app.ordermanagement.model.Execution;
import com.app.ordermanagement.model.Order;
import com.app.ordermanagement.model.OrderBook;
import com.app.ordermanagement.util.OrderBookStatus;
import com.app.ordermanagement.util.OrderStatus;
import com.app.ordermanagement.util.OrderType;

@Component
public class OrderBookProcessor {

	Logger log = LoggerFactory.getLogger(OrderBookProcessor.class);

	public void processOrders(OrderBook orderBook, Execution execution) {
		validate(orderBook,execution);
		setExecutionPrice(orderBook,execution);
		execute(orderBook,execution);
	}

	
	/**
	 * validate orders and segregate into valid/invalid orders, based on execution price.
	 * @param execution
	 */
	public boolean validate(OrderBook orderbook, Execution execution) {
		// Throw error if order book is still in open state.
		if(orderbook.getStatus().equals(OrderBookStatus.OPEN)) {
			throw new OrderBookOpenException("Cannot Execute open order book.");
		}

		//Throw error if order book is in executed state
		if(orderbook.getStatus().equals(OrderBookStatus.EXECUTED)) {
			throw new OrderBookOpenException("Order book is executed. No new executions allowed.");
		}
			
		//subsequent execution prices should match.
		if(orderbook.isValidated() == true && orderbook.getExecutionPrice() != execution.getExecutionPrice()) {
			throw new WrongExecutionPriceException("Execution price cannot be:"+execution.getExecutionPrice()+", it should"
					+ " be:"+orderbook.getExecutionPrice() );
		}

		if(orderbook.isValidated() ) { 
			log.info("Returning as valid/invalid orders are segregated.");
			return true;
		}
		
		Predicate<Order> limitOrders =a -> (a.getOrderType().equals(OrderType.LIMIT)); 
		Predicate<Order> belowExectutionPrice = a -> (a.getPrice() < execution.getExecutionPrice());

		orderbook.setInValidOrders( orderbook.getValidOrders().stream().filter(limitOrders.and(belowExectutionPrice)).collect(Collectors.toList()));
		orderbook.removeInvalidOrders();
		//validOrders = validOrders.stream().filter(marketOrders.or(aboveExecutionPrice)).collect(Collectors.toList());
		
		log.info("Valid orders after validation");
		orderbook.getValidOrders().forEach(o-> log.info(o.toString()));

		log.info("Invalid Orders after validation");
		orderbook.getInValidOrders().forEach(o-> log.info(o.toString()));
		
		return true;
	}
	
	public void setExecutionPrice(OrderBook orderbook, Execution execution) {
		orderbook.setExecutionPrice(execution.getExecutionPrice()); // set execution price.
	}

	public void execute(OrderBook orderbook, Execution execution) {
		long totaldemand = orderbook.getValidOrders().stream().
													filter(o-> (o.getOrderStatus().equals(OrderStatus.OPEN) || o.getOrderStatus().equals(OrderStatus.PARTIALLY_EXECUTED))).
													mapToLong(o-> o.getOrderQuantity()).sum();
		
		log.info("Total demand: "+totaldemand);
		
		if(execution.getExecutionQuantity() > totaldemand) {
			throw new ExecutionQuantityExceedsTotalDemand("Execution quantity "+execution.getExecutionQuantity()+" exceeds total demand "+totaldemand);
		}
		
		while(execution.getExecutionQuantity() != 0) {

			long numberOfValidOpenOrders =
					orderbook.getValidOrders().stream().filter(o -> (o.getOrderStatus().equals(OrderStatus.OPEN) || o.getOrderStatus().equals(OrderStatus.PARTIALLY_EXECUTED))).count();
			
			log.info("Number of valid orders in order book:"+numberOfValidOpenOrders);
			if(execution.getExecutionQuantity() >= numberOfValidOpenOrders) {
				log.info("1 quantity/order executed for orders(count):"+numberOfValidOpenOrders);
				executeOneQuantityPerOrder(orderbook);

				execution.setExecutionQuantity(execution.getExecutionQuantity() - numberOfValidOpenOrders);
				orderbook.setStatus(OrderBookStatus.PARTIALLY_EXECUTED);
				
			}else {
				log.info("Applying Lucky Draw system.");
				//lottery system where execution quantity is less than valid open orders.
				Collections.shuffle(orderbook.getValidOrders());
				log.info("1 quantity/order executed for order count:"+execution.getExecutionQuantity()+" out of total orders:"+numberOfValidOpenOrders+" using lottery");
				executeOneQuantityPerOrder(orderbook, execution.getExecutionQuantity());
//				orderbook.getValidOrders().stream().filter(o -> (o.getOrderStatus().equals(OrderStatus.OPEN))).
//									limit(execution.getExecutionQuantity()).
//									map(a -> a  = a.execute(executionPerOrder)).
//									collect(Collectors.toList());
				
				execution.setExecutionQuantity(0);
				orderbook.setStatus(OrderBookStatus.PARTIALLY_EXECUTED);
				
			}
		}
		
		//Check if demand is zero
		totaldemand = orderbook.getValidOrders().stream().
				filter(o-> (o.getOrderStatus().equals(OrderStatus.OPEN) || o.getOrderStatus().equals(OrderStatus.PARTIALLY_EXECUTED))).
				mapToLong(o-> o.getOrderQuantity()).sum();
		if(totaldemand == 0) {
			orderbook.setStatus(OrderBookStatus.EXECUTED);
		}
		
	}

	private void executeOneQuantityPerOrder(OrderBook orderbook, long numberOfOrderToBeExecuted) {
		List<Order> newValidOrders = new ArrayList<Order>(orderbook.getValidOrders().size()); 
		int count = 0;
		for (Order order : orderbook.getValidOrders()) {
		
			if(count < numberOfOrderToBeExecuted && 
						(order.getOrderStatus().equals(OrderStatus.OPEN) || order.getOrderStatus().equals(OrderStatus.PARTIALLY_EXECUTED)) ) {
				newValidOrders.add(order.execute(1));
				count++;
			}else {
				newValidOrders.add(order);
			}
		}
		orderbook.setValidOrders(newValidOrders);
		
	}


	private void executeOneQuantityPerOrder(OrderBook orderbook) {
		List<Order> newValidOrders = new ArrayList<Order>(orderbook.getValidOrders().size()); 
		for (Order order : orderbook.getValidOrders()) {
			if(order.getOrderStatus().equals(OrderStatus.OPEN) || order.getOrderStatus().equals(OrderStatus.PARTIALLY_EXECUTED) ) {
				newValidOrders.add(order.execute(1));
			}else {
				newValidOrders.add(order);
			}
		}
		
		orderbook.setValidOrders(newValidOrders);
		
	}
}
