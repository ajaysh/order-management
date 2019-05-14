package com.app.ordermanagement.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.ordermanagement.controller.OrderController;
import com.app.ordermanagement.dto.OrderDTO;
import com.app.ordermanagement.exception.DuplicateOrderBookException;
import com.app.ordermanagement.exception.OrderBookNotFoundException;
import com.app.ordermanagement.model.Execution;
import com.app.ordermanagement.model.Order;
import com.app.ordermanagement.model.OrderBook;
import com.app.ordermanagement.orderbook.processor.OrderBookProcessor;
import com.app.ordermanagement.orderbook.util.OrderBookHelper;
import com.app.ordermanagement.service.OrderBookService;
import com.app.ordermanagement.util.OrderBookStatus;
import com.app.ordermanagement.util.OrderMapper;
import com.app.ordermanagement.util.StatisticsGenerator;

@Component("OrderBookServiceImpl")
public class OrderBookServiceImpl implements OrderBookService{
	
	Logger log = LoggerFactory.getLogger(OrderController.class);

	
	StatisticsGenerator statisticsGenerator = new StatisticsGenerator();

	
	private Map<String, OrderBook> orderBookMap = new HashMap<String, OrderBook>();	

	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private OrderBookHelper orderBookHelper;

	@Autowired
	private OrderBookProcessor orderBookProcessor;

	public String createOrderBook(String instrumentid) {
		log.info("Creating order book for instrumentid:"+instrumentid);
		OrderBook orderBook = orderBookMap.get(instrumentid);
		if(orderBook != null) {
			throw new DuplicateOrderBookException("Order book for instrument id"+instrumentid+" is already created!");
		}
		OrderBook orderbook = new OrderBook(instrumentid);
		orderBookMap.put(instrumentid, orderbook);
		return "Order Book created.";
	}


	public String closeOrderBook(String instrumentid) {
		log.info("Closing order book for instrumentid:"+instrumentid);
		OrderBook orderBook = orderBookMap.get(instrumentid);
		if(orderBook == null) {
			throw new OrderBookNotFoundException("Order book for instrument id"+instrumentid+" doesn't exist!");
		}
		orderBook.setStatus(OrderBookStatus.CLOSED);
		return "Order Book closed!";
	}

	public OrderDTO addOrderToOrderBook(String instrumentid,OrderDTO orderdto) {
		Order order;
		if(orderBookHelper.canAddOrderToOrderBook(orderBookMap,instrumentid)){
			OrderBook orderBook = orderBookMap.get(instrumentid);
			order = orderMapper.getOrderFromOrderDTO(orderdto, orderBook.getValidOrders().size()+1);
			log.info("Adding order to order book:"+orderdto);
			orderBook.getValidOrders().add(order);
		}
		
		return orderdto;
	}

	public List<OrderDTO> getOrders(String instrumentid) {
		OrderBook orderBook = orderBookMap.get(instrumentid);
		if(orderBook == null) {
			throw new OrderBookNotFoundException("Order book for instrument id"+instrumentid+" doesn't exist");
		}
		
		return orderMapper.getOrderDTOFromOrdersLlist(orderBookHelper.getTotalOrders(orderBook));
	}

	public void execute(String instrumentid, Execution execution) {
		//validateOrders
		if(orderBookHelper.doesOrderBookExist(orderBookMap, instrumentid)) {
			OrderBook orderBook = orderBookMap.get(instrumentid);
			orderBookProcessor.processOrders(orderBook, execution);
		}
		
	}

	public OrderDTO getOrderDetails(String instrumentid, int orderid) {
		return orderMapper.getOrderDTOFromOrder(orderBookHelper.getOrder(orderBookMap, instrumentid, orderid));
	}


	@Override
	public Object getStatistics() {
		return statisticsGenerator.generateStatistics(orderBookMap);
	}
	
}
