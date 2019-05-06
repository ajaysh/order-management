package com.app.ordermanagement.conroller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.app.ordermanagement.service.OrderBookService;


@RunWith(SpringRunner.class)
@WebMvcTest(value = OrderController.class)
public class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrderBookService orderBookService;
	
	OrderBook mockOrderBook = new OrderBook("test");
	
	@Test
	public void createOrderBook() throws Exception{
		
		Mockito.when(
				orderBookService.createOrderBook(Mockito.anyString()
						)).thenReturn(mockOrderBook);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/orderbook/test").accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println(result.getResponse().getContentAsString());
		String expected = "Order book created successsfully!";
		
		Assert.assertEquals("Order book creation failed",result.getResponse().getContentAsString(),expected);

	}

	@Test
	public void closeOrderBook() throws Exception{
		
		Mockito.when(
				orderBookService.closeOrderBook(Mockito.anyString()
						)).thenReturn(mockOrderBook);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/orderbook/test/close").accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println(result.getResponse().getContentAsString());
		String expected = "Order book closed!";
		
		Assert.assertEquals("Order book close failed",result.getResponse().getContentAsString(),expected);

	}

	
}
