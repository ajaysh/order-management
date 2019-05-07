package com.app.ordermanagement.orderbook.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.app.ordermanagement.exception.OrderBookClosedException;
import com.app.ordermanagement.exception.OrderBookNotFoundException;
import com.app.ordermanagement.exception.OrderDoesNotExist;
import com.app.ordermanagement.model.Order;
import com.app.ordermanagement.model.OrderBook;

@Component
public class OrderBookHelper {

	public List<Order> getTotalOrders(OrderBook orderBook) {
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
	
	public boolean canAddOrderToOrderBook(Map<String, OrderBook> orderBookMap,String instrumentid) {
		OrderBook orderBook = orderBookMap.get(instrumentid);
		if(orderBook == null) {
			throw new OrderBookNotFoundException("Order book for instrument id"+instrumentid+" doesn't exist!");
		}
		if(!orderBook.isOpen()) {
			throw new OrderBookClosedException("Cannot add Orders book because order book is closed!");
		}
		return true;
	}
	
	public boolean doesOrderBookExist(Map<String, OrderBook> orderBookMap,String instrumentid) {
		OrderBook orderBook = orderBookMap.get(instrumentid);
		if(orderBook == null) {
			throw new OrderBookNotFoundException("Order book for instrument id"+instrumentid+" doesn't exist");
		}
		
		return true;
	}
	
	public Order getOrder(Map<String, OrderBook> orderBookMap,String instrumentid,int orderid) {
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
