package com.app.ordermanagement.service;

import java.util.List;

import com.app.ordermanagement.dto.OrderDTO;
import com.app.ordermanagement.model.Execution;

public interface OrderBookService {

	public String createOrderBook(String instrumentid);
	public String closeOrderBook(String instrumentid);
	public OrderDTO addOrderToOrderBook(String instrumentid,OrderDTO order);
	public List<OrderDTO> getOrders(String instrumentid);
	public OrderDTO getOrderDetails(String instrumentid, int orderid);
	public void execute(String instrumentid, Execution execution);
	public Object getStatistics();
	
}
