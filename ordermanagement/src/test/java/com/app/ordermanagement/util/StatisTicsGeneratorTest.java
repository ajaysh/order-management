package com.app.ordermanagement.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.app.ordermanagement.model.Order;
import com.app.ordermanagement.model.OrderBook;

@RunWith(SpringRunner.class)
public class StatisTicsGeneratorTest {
	
	StatisticsGenerator statisticsGenerator;
	OrderBook orderbook;
	Map<String, OrderBook> orderBookMap;
	
	@Before
	public void beforeTestCase() {
		statisticsGenerator = new StatisticsGenerator();
		orderbook = new OrderBook();
		orderBookMap = new HashMap<String, OrderBook>();
	}

	
	@Test
	public void geneateStatistics_success(){
		Order order1 = new Order(1, 10, OrderType.LIMIT, null, 100l, 0, OrderStatus.OPEN);
		Order order2 = new Order(2, 30, OrderType.LIMIT, null, 110l, 0, OrderStatus.OPEN);
		Order order3 = new Order(3, 5, OrderType.MARKET_ORDERS, null, null, 0, OrderStatus.OPEN);
		Order order4 = new Order(4, 10, OrderType.LIMIT, null, 120l, 0, OrderStatus.OPEN);
		Order order5 = new Order(5, 10, OrderType.LIMIT, null, 90l, 0, OrderStatus.OPEN);
		Order order6 = new Order(6, 20, OrderType.LIMIT, null, 80l, 0, OrderStatus.OPEN);

		java.util.List<Order> orderList = new ArrayList<Order>(4);
		orderList.add(order1);
		orderList.add(order2);
		orderList.add(order3);
		orderList.add(order4);
		java.util.List<Order> invalidOrderList = new ArrayList<Order>(2);
		invalidOrderList.add(order5);
		invalidOrderList.add(order6);
		
		orderbook.setValidOrders(orderList);
		orderbook.setInValidOrders(invalidOrderList);
		orderBookMap.put("instrument1", orderbook);		
		
		Map<String, Map<String,Object>> statisticsMap = (Map<String, Map<String,Object>>)statisticsGenerator.generateStatistics(orderBookMap);

		assertEquals(6, statisticsMap.get("instrument1").get("NumberOfOrders"));
		assertEquals(85L, statisticsMap.get("instrument1").get("TotalDemand"));
		assertEquals(5, ((Order)statisticsMap.get("instrument1").get("SmallestOrder")).getOrderQuantity());
		assertEquals(30, ((Order)statisticsMap.get("instrument1").get("LargestOrder")).getOrderQuantity());
		
	}
	
	
	@After
	public void afterTestCase() {
		statisticsGenerator =null;
		orderbook = null;

	}
	
	

}
