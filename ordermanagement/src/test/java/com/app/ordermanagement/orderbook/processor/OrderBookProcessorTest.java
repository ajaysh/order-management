package com.app.ordermanagement.orderbook.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.app.ordermanagement.exception.OrderBookOpenException;
import com.app.ordermanagement.exception.WrongExecutionPriceException;
import com.app.ordermanagement.model.Execution;
import com.app.ordermanagement.model.OrderBook;
import com.app.ordermanagement.util.OrderBookStatus;

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
		System.out.println("result is:"+result);
		assertTrue(result);
	}	
	
	
	@After
	public void afterTestCase() {
	}

}
