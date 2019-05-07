package com.app.ordermanagement.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.ordermanagement.controller.OrderController;
import com.app.ordermanagement.exception.DuplicateOrderBookException;
import com.app.ordermanagement.exception.OrderBookNotFoundException;
import com.app.ordermanagement.model.Execution;
import com.app.ordermanagement.model.Order;
import com.app.ordermanagement.model.OrderBook;
import com.app.ordermanagement.orderbook.util.OrderBookHelper;

@Component
public class OrderBookService {
	
	Logger log = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired(required=false)
	private Map<String, OrderBook> orderBookMap;	

	@Autowired
	private OrderBookHelper orderBookHelper;
	
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
		
		if(orderBookHelper.canAddOrderToOrderBook(orderBookMap,instrumentid)){
			OrderBook orderBook = orderBookMap.get(instrumentid);
			order.setOrderid(orderBook.getValidOrders().size()+1);
			order.setOrderType();
			orderBook.getValidOrders().add(order);
		}
		return order;
	}

	public List<Order> getOrders(String instrumentid) {
		OrderBook orderBook = orderBookMap.get(instrumentid);
		if(orderBook == null) {
			throw new OrderBookNotFoundException("Order book for instrument id"+instrumentid+" doesn't exist");
		}
		
		return orderBookHelper.getTotalOrders(orderBook);
	}

	public void execute(String instrumentid, Execution execution) {
		//validateOrders
		if(orderBookHelper.doesOrderBookExist(orderBookMap, instrumentid)) {
			OrderBook orderBook = orderBookMap.get(instrumentid);
			orderBook.processExecution(execution);
		}
		
	}

	public Order getOrderDetails(String instrumentid, int orderid) {
		return orderBookHelper.getOrder(orderBookMap, instrumentid, orderid);
	}
}
