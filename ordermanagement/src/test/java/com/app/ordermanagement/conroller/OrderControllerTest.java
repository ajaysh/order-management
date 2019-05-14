package com.app.ordermanagement.conroller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.app.ordermanagement.controller.OrderController;
import com.app.ordermanagement.model.OrderBook;
import com.app.ordermanagement.service.impl.OrderBookServiceImpl;


@RunWith(SpringRunner.class)
@WebMvcTest(value = OrderController.class)
public class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	@Qualifier("OrderBookServiceImpl")
	private OrderBookServiceImpl orderBookService;
	
	OrderBook mockOrderBook = new OrderBook("test");
	
	@Test
	public void createOrderBook() throws Exception{
		
		Mockito.when(
				orderBookService.createOrderBook(Mockito.anyString()
						)).thenReturn("Order Book created!");

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/orderbook/test").accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		String expected = "Order book created successsfully!";
		
		Assert.assertEquals("Order book creation failed",result.getResponse().getContentAsString(),expected);

	}

	@Test
	public void closeOrderBook() throws Exception{
		
		Mockito.when(
				orderBookService.closeOrderBook(Mockito.anyString()
						)).thenReturn("Order Book closed.");

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/orderbook/test/close").accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		String expected = "Order book closed!";
		
		Assert.assertEquals("Order book close failed",result.getResponse().getContentAsString(),expected);

	}

	
}
