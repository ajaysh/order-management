package com.app.ordermanagement.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.app.ordermanagement.controller.OrderController;
import com.app.ordermanagement.exception.DuplicateOrderBookException;
import com.app.ordermanagement.exception.OrderBookClosedException;
import com.app.ordermanagement.exception.OrderBookNotFoundException;
import com.app.ordermanagement.exception.OrderDoesNotExist;
import com.app.ordermanagement.model.Execution;
import com.app.ordermanagement.model.Order;
import com.app.ordermanagement.model.OrderBook;

@Component
public class OrderBookService {
	
	Logger log = LoggerFactory.getLogger(OrderController.class);
	
	private static Map<String, OrderBook> orderBookMap = new HashMap<String, OrderBook>();
	
	
	public OrderBook createOrderBook(String instrumentid) {
		log.info("Creating order book for instrumentid:"+instrumentid);
		OrderBook orderBook = orderBookMap.get(instrumentid);
		if(orderBook != null) {
			throw new DuplicateOrderBookException("Order book for instrument id"+instrumentid+" is already created!");
		}
		OrderBook orderbook = new OrderBook(instrumentid);
		orderBookMap.put(instrumentid, orderbook);
		return orderbook;
	}


	public OrderBook closeOrderBook(String instrumentid) {
		log.info("Closing order book for instrumentid:"+instrumentid);
		OrderBook orderBook = orderBookMap.get(instrumentid);
		if(orderBook == null) {
			throw new OrderBookNotFoundException("Order book for instrument id"+instrumentid+" doesn't exist!");
		}
		orderBook.setOpen(false);
		return orderBook;
	}

	public Order addOrderToOrderBook(String instrumentid,Order order) {
		log.info("Adding order to order book:"+order);
		
		OrderBook orderBook = orderBookMap.get(instrumentid);
		if(orderBook == null) {
			throw new OrderBookNotFoundException("Order book for instrument id"+instrumentid+" doesn't exist!");
		}
		if(!orderBook.isOpen()) {
			throw new OrderBookClosedException("Cannot add Orders book because order book is closed!");
		}
		order.setOrderid(orderBook.getValidOrders().size()+1);
		order.setOrderType();
		orderBook.getValidOrders().add(order);
		return order;
	}

	public List<Order> getOrders(String instrumentid) {
		OrderBook orderBook = orderBookMap.get(instrumentid);
		if(orderBook == null) {
			throw new OrderBookNotFoundException("Order book for instrument id"+instrumentid+" doesn't exist");
		}
		List<Order> totalOrders = new ArrayList<Order>(orderBook.getValidOrders());
		totalOrders.addAll(orderBook.getInValidOrders());
		totalOrders.sort(new Comparator<Order>() {
			@Override
			public int compare(Order o1, Order o2) {
				return o1.getOrderid() - o2.getOrderid();
			}
		});	
		return totalOrders;
		
	}

	public void execute(String instrumentid, Execution execution) {
		//validateOrders
		OrderBook orderBook = orderBookMap.get(instrumentid);
		if(orderBook == null) {
			throw new OrderBookNotFoundException("Order book for instrument id"+instrumentid+" doesn't exist");
		}

		orderBook.processExecution(execution);
		
	}

	public Order getOrderDetails(String instrumentid, int orderid) {
		OrderBook orderBook = orderBookMap.get(instrumentid);
		if(orderBook == null) {
			throw new OrderBookNotFoundException("Order book for instrument id"+instrumentid+" doesn't exist");
		}

		Order order = orderBook.getOrderDetails(orderid);
		if(order == null) {
			throw new OrderDoesNotExist("Order with orderid:"+orderid+" does not exist!");
		}
		return order;
	}
}
