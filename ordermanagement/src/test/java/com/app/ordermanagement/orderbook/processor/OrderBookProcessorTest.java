package com.app.ordermanagement.orderbook.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.app.ordermanagement.exception.ExecutionQuantityExceedsTotalDemand;
import com.app.ordermanagement.exception.OrderBookOpenException;
import com.app.ordermanagement.exception.WrongExecutionPriceException;
import com.app.ordermanagement.model.Execution;
import com.app.ordermanagement.model.Order;
import com.app.ordermanagement.model.OrderBook;
import com.app.ordermanagement.util.OrderBookStatus;
import com.app.ordermanagement.util.OrderStatus;
import com.app.ordermanagement.util.OrderType;

@RunWith(SpringRunner.class)

public class OrderBookProcessorTest {

	OrderBookProcessor orderBookProcessor;
	OrderBook orderbook;
	Execution execution;
	
	@Before
	public void beforeTestCase() {
		orderBookProcessor = new OrderBookProcessor();
		orderbook = new OrderBook();
		execution = new Execution();
	}
	

//	@Test
//	public void validate_Success(){
//		orderbook.setStatus(OrderBookStatus.CLOSED);
//		orderBookProcessor.validate(orderbook, execution);
//	}
	
	@Test(expected=OrderBookOpenException.class)
	public void validate_Failure_OrderBookOpenFailure(){
		
		orderbook.setStatus(OrderBookStatus.OPEN);
		orderBookProcessor.validate(orderbook, execution);
		
	}
	
	@Test(expected=OrderBookOpenException.class)
	public void validate_Failure_OrderBookOpenFailureDueToExecuted(){
		
		orderbook.setStatus(OrderBookStatus.EXECUTED);
		orderBookProcessor.validate(orderbook, execution);
		
	}
	
	@Test(expected=WrongExecutionPriceException.class)
	public void validate_Failure_WrongExecutionPrice(){
		
		orderbook.setStatus(OrderBookStatus.CLOSED);
		orderbook.setValidated(true);
		orderbook.setExecutionPrice(100l);
		execution.setExecutionPrice(110l);
		
		orderBookProcessor.validate(orderbook, execution);
		
	}

	@Test
	public void validate_Sucess_AlreadyValidated(){
		orderbook.setStatus(OrderBookStatus.CLOSED);
		orderbook.setValidated(true);
		orderbook.setExecutionPrice(100l);
		execution.setExecutionPrice(100l);
		boolean result  = orderBookProcessor.validate(orderbook, execution);
		assertTrue(result);
	}
	
	
	@Test
	public void exeucte_Sucess(){
		
		execution.setExecutionPrice(100);
		execution.setExecutionQuantity(10);
		
		Order order1 = new Order(1, 10, OrderType.LIMIT, null, 100l, 0, OrderStatus.OPEN);
		Order order2 = new Order(2, 10, OrderType.LIMIT, null, 110l, 0, OrderStatus.OPEN);
		Order order3 = new Order(3, 5, OrderType.MARKET_ORDERS, null, null, 0, OrderStatus.OPEN);
		Order order4 = new Order(4, 10, OrderType.LIMIT, null, 120l, 0, OrderStatus.OPEN);
		java.util.List<Order> orderList = new ArrayList<Order>(4);
		orderList.add(order1);
		orderList.add(order2);
		orderList.add(order3);
		orderList.add(order4);
		orderbook.setValidOrders(orderList);
		
		orderBookProcessor.execute(orderbook, execution);
		long accumulatedExecution = orderbook.getValidOrders().stream().mapToLong(o-> o.getExecutedQuantity()).sum();
		assertEquals(10, accumulatedExecution);
	}


	@Test
	public void validate_Sucess_ValidInvalidList(){
		
		execution.setExecutionPrice(100);
		execution.setExecutionQuantity(35);
		orderbook.setStatus(OrderBookStatus.CLOSED);
		
		Order order1 = new Order(1, 10, OrderType.LIMIT, null, 100l, 0, OrderStatus.OPEN);
		Order order2 = new Order(2, 10, OrderType.LIMIT, null, 90l, 0, OrderStatus.OPEN);
		Order order3 = new Order(3, 5, OrderType.MARKET_ORDERS, null, null, 0, OrderStatus.OPEN);
		Order order4 = new Order(4, 10, OrderType.LIMIT, null, 80l, 0, OrderStatus.OPEN);
		java.util.List<Order> orderList = new ArrayList<Order>(4);
		orderList.add(order1);
		orderList.add(order2);
		orderList.add(order3);
		orderList.add(order4);
		orderbook.setValidOrders(orderList);
		
		orderBookProcessor.validate(orderbook, execution);
		long invalidOrderCount = orderbook.getInValidOrders().size();
		
		
		assertEquals(2, invalidOrderCount);
	}

	@Test
	public void validate_Sucess_ValidInvalidList2(){
		
		execution.setExecutionPrice(100);
		execution.setExecutionQuantity(35);
		orderbook.setStatus(OrderBookStatus.CLOSED);
		
		Order order1 = new Order(1, 10, OrderType.LIMIT, null, 100l, 0, OrderStatus.OPEN);
		Order order2 = new Order(2, 10, OrderType.LIMIT, null, 100l, 0, OrderStatus.OPEN);
		Order order3 = new Order(3, 5, OrderType.MARKET_ORDERS, null, null, 0, OrderStatus.OPEN);
		Order order4 = new Order(4, 10, OrderType.LIMIT, null, 80l, 0, OrderStatus.OPEN);
		java.util.List<Order> orderList = new ArrayList<Order>(4);
		orderList.add(order1);
		orderList.add(order2);
		orderList.add(order3);
		orderList.add(order4);
		orderbook.setValidOrders(orderList);
		
		orderBookProcessor.validate(orderbook, execution);
		long validOrderCount = orderbook.getValidOrders().size();
		
		
		assertEquals(3, validOrderCount);
	}

	@Test
	public void exeucte_Sucess_AllOrdersExecuted(){
		
		execution.setExecutionPrice(100);
		execution.setExecutionQuantity(35);
		
		Order order1 = new Order(1, 10, OrderType.LIMIT, null, 100l, 0, OrderStatus.OPEN);
		Order order2 = new Order(2, 10, OrderType.LIMIT, null, 110l, 0, OrderStatus.OPEN);
		Order order3 = new Order(3, 5, OrderType.MARKET_ORDERS, null, null, 0, OrderStatus.OPEN);
		Order order4 = new Order(4, 10, OrderType.LIMIT, null, 120l, 0, OrderStatus.OPEN);
		java.util.List<Order> orderList = new ArrayList<Order>(4);
		orderList.add(order1);
		orderList.add(order2);
		orderList.add(order3);
		orderList.add(order4);
		orderbook.setValidOrders(orderList);
		
		orderBookProcessor.execute(orderbook, execution);
		long completedCount = orderbook.getValidOrders().stream().
									filter(o-> o.getOrderStatus().equals(OrderStatus.EXECUTED)).count();
		
		assertEquals(4, completedCount);
	}

	@Test
	public void exeucte_Sucess_AllOrdersPartiallyExecuted(){
		
		execution.setExecutionPrice(100);
		execution.setExecutionQuantity(4);
		
		Order order1 = new Order(1, 10, OrderType.LIMIT, null, 100l, 0, OrderStatus.OPEN);
		Order order2 = new Order(2, 10, OrderType.LIMIT, null, 110l, 0, OrderStatus.OPEN);
		Order order3 = new Order(3, 5, OrderType.MARKET_ORDERS, null, null, 0, OrderStatus.OPEN);
		Order order4 = new Order(4, 10, OrderType.LIMIT, null, 120l, 0, OrderStatus.OPEN);
		java.util.List<Order> orderList = new ArrayList<Order>(4);
		orderList.add(order1);
		orderList.add(order2);
		orderList.add(order3);
		orderList.add(order4);
		orderbook.setValidOrders(orderList);
		
		orderBookProcessor.execute(orderbook, execution);
		long completedCount = orderbook.getValidOrders().stream().
									filter(o-> o.getOrderStatus().equals(OrderStatus.PARTIALLY_EXECUTED)).count();
		
		assertEquals(4, completedCount);
	}
	
	@Test
	public void exeucte_Sucess_RemainingDemandCheck(){
		
		execution.setExecutionPrice(100);
		execution.setExecutionQuantity(20);
		
		Order order1 = new Order(1, 10, OrderType.LIMIT, null, 100l, 0, OrderStatus.OPEN);
		Order order2 = new Order(2, 10, OrderType.LIMIT, null, 110l, 0, OrderStatus.OPEN);
		Order order3 = new Order(3, 5, OrderType.MARKET_ORDERS, null, null, 0, OrderStatus.OPEN);
		Order order4 = new Order(4, 10, OrderType.LIMIT, null, 120l, 0, OrderStatus.OPEN);
		java.util.List<Order> orderList = new ArrayList<Order>(4);
		orderList.add(order1);
		orderList.add(order2);
		orderList.add(order3);
		orderList.add(order4);
		orderbook.setValidOrders(orderList);
		
		orderBookProcessor.execute(orderbook, execution);
		long remainingDemand = orderbook.getValidOrders().stream().mapToLong(o-> o.getOrderQuantity()).sum();
				
		assertEquals(15, remainingDemand);
	}
	
	@Test(expected=ExecutionQuantityExceedsTotalDemand.class)
	public void exeucte_Failure_ExecutionQuantityExceedsTotalDemand(){
		
		execution.setExecutionPrice(100);
		execution.setExecutionQuantity(40);
		
		Order order1 = new Order(1, 10, OrderType.LIMIT, null, 100l, 0, OrderStatus.OPEN);
		Order order2 = new Order(2, 10, OrderType.LIMIT, null, 110l, 0, OrderStatus.OPEN);
		Order order3 = new Order(3, 5, OrderType.MARKET_ORDERS, null, null, 0, OrderStatus.OPEN);
		Order order4 = new Order(4, 10, OrderType.LIMIT, null, 120l, 0, OrderStatus.OPEN);
		java.util.List<Order> orderList = new ArrayList<Order>(4);
		orderList.add(order1);
		orderList.add(order2);
		orderList.add(order3);
		orderList.add(order4);
		orderbook.setValidOrders(orderList);
		
		orderBookProcessor.execute(orderbook, execution);
	}
	
	

	
	
	@After
	public void afterTestCase() {
	}

}
