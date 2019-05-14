package com.app.ordermanagement.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.app.ordermanagement.dto.OrderDTO;
import com.app.ordermanagement.model.Order;

@Component
public class OrderMapper {

	public Order getOrderFromOrderDTO(OrderDTO orderDto, int orderid) {
		orderDto.setOrderid(orderid);
		orderDto.setOrderStatus(OrderStatus.OPEN);
		if(orderDto.getPrice() != null && orderDto.getPrice() > 0) {
			orderDto.setOrderType(OrderType.LIMIT);
		}else
		{
			orderDto.setOrderType(OrderType.MARKET_ORDERS);
		}

		Order order  = new Order(orderDto.getOrderid(),
								orderDto.getOrderQuantity(),
								orderDto.getOrderType(),
								orderDto.getEntryDate(),
								orderDto.getPrice(),
								orderDto.getExecutedQuantity(),
								orderDto.getOrderStatus());
		
		return order;
	}
	
	public OrderDTO getOrderDTOFromOrder(Order order) {
		OrderDTO orderDto  = new OrderDTO(order.getOrderid(),
										 order.getOrderQuantity(),
										 order.getOrderType(),
										 order.getEntryDate(),
										 order.getPrice(),
										 order.getExecutedQuantity(),
										 order.getOrderStatus());
		
		return orderDto;
	}
	
	public List<OrderDTO> getOrderDTOFromOrdersLlist(List<Order> orders) {
		List<OrderDTO> orderDTOs = new ArrayList<OrderDTO>(orders.size()); 
		for (Order order : orders) {
			OrderDTO orderDTO = this.getOrderDTOFromOrder(order);
			orderDTOs.add(orderDTO);
		}
		return orderDTOs;
	}
	
	

}
