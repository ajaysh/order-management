package com.app.ordermanagement.orderbook.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import com.app.ordermanagement.exception.OrderBookNotFoundException;
import com.app.ordermanagement.exception.OrderDoesNotExist;
import com.app.ordermanagement.model.Order;
import com.app.ordermanagement.model.OrderBook;
import com.app.ordermanagement.util.OrderStatus;
import com.app.ordermanagement.util.OrderType;

@RunWith(SpringRunner.class)
public class OrderBookHelperTest {


	@Mock
	Map<String, OrderBook> orderBookMap;

	OrderBookHelper orderBookHelper;
	
	String instrumentid;
	
	@Before
	public void beforeTestCase() {
		orderBookHelper = new OrderBookHelper();
		instrumentid = "testInstrument";
	}
	
	@Test
	public void doesOrderBookExist_Success(){
		Mockito.when(orderBookMap.get("testInstrument")).thenReturn(new OrderBook());
		
		boolean result = orderBookHelper.doesOrderBookExist(orderBookMap, instrumentid);
		assertTrue(result);
	}

	@Test(expected = OrderBookNotFoundException.class)
	public void doesOrderBookExist_Failure(){
		
		orderBookHelper.doesOrderBookExist(orderBookMap, instrumentid);
	}

	@Test
	public void canAddOrderToOrderBook_Success(){
		
		String instrumentid = "testInstrument";
		
		Mockito.when(orderBookMap.get("testInstrument")).thenReturn(new OrderBook());
		
		boolean result = orderBookHelper.canAddOrderToOrderBook(orderBookMap, instrumentid);

		assertTrue(result);
	}

	@Test(expected = OrderBookNotFoundException.class)
	public void canAddOrderToOrderBook_Failure(){
		String instrumentid = "testInstrument";
		
		boolean result = orderBookHelper.canAddOrderToOrderBook(orderBookMap, instrumentid);
		
	}

	@Test
	public void getOrderDetails_Success() {
		int orderid = 1;
		Order order = new Order(1, 10L, OrderType.LIMIT, null, 200L, 10L, OrderStatus.OPEN);

		OrderBook orderBook = new OrderBook();
		orderBook.getValidOrders().add(order);
		Order resultOrder = orderBookHelper.getOrderDetails(orderBook, orderid);
		
		assertEquals(order, resultOrder);
	}
	
	@Test
	public void getOrderDetails_notFound() {
		int orderid = 1;
		OrderBook orderBook = new OrderBook();
		Order resultOrder = orderBookHelper.getOrderDetails(orderBook, orderid);
		assertEquals(null, resultOrder);
	}
	
	
	@Test
	public void getOrder_Success() {
		int orderid = 1;
		Order order = new Order(1, 10L, OrderType.LIMIT, null, 200L, 10L, OrderStatus.OPEN);

		OrderBook orderBook = new OrderBook();
		orderBook.getValidOrders().add(order);

		Mockito.when(orderBookMap.get("testInstrument")).thenReturn(orderBook);

		Order returnedOrder = orderBookHelper.getOrder(orderBookMap, instrumentid, orderid);
		assertEquals(order, returnedOrder);
	}
	
	@Test(expected=OrderDoesNotExist.class)
	public void getOrder_Failure_OrderDoesNotExist() {
		int orderid = 1;
		OrderBook orderBook = new OrderBook();
		Mockito.when(orderBookMap.get("testInstrument")).thenReturn(orderBook);

		orderBookHelper.getOrder(orderBookMap, instrumentid, orderid);
	}

	@Test(expected=OrderBookNotFoundException.class)
	public void getOrder_Failure_OrderBookNotFound() {
		int orderid = 1;
		Mockito.when(orderBookMap.get("testInstrument")).thenReturn(null);
		orderBookHelper.getOrder(orderBookMap, instrumentid, orderid);
	}

	
	@After
	public void afterTestCase() {
		orderBookHelper = null;
		instrumentid = null;
	}

	
}
