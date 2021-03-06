package com.app.ordermanagement.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.ordermanagement.dto.OrderDTO;
import com.app.ordermanagement.model.Execution;
import com.app.ordermanagement.service.OrderBookService;

@RestController
public class OrderController {

	Logger log = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
	@Qualifier("OrderBookServiceImpl")
	private OrderBookService orderBookService;
	
	@RequestMapping(value = "/orderbook/{instrumentid}", method = RequestMethod.GET)
	public ResponseEntity<Object> createOrderBook(@PathVariable("instrumentid") String instrumentid) { 
		  orderBookService.createOrderBook(instrumentid);
	      return new ResponseEntity<>("Order book created successsfully!", HttpStatus.OK);
	   }   

	@RequestMapping(value = "/orderbook/{instrumentid}/close", method = RequestMethod.GET)
	public ResponseEntity<Object> closeOrderBook(@PathVariable("instrumentid") String instrumentid) { 
		  orderBookService.closeOrderBook(instrumentid);
	      return new ResponseEntity<>("Order book closed!", HttpStatus.OK);
	}   
	
	@RequestMapping(value = "/orderbook/{instrumentid}/createOrder", method = RequestMethod.POST)
	public ResponseEntity<OrderDTO> addOrder(@PathVariable("instrumentid") String instrumentid, 
										 @RequestBody OrderDTO orderDto) {
		OrderDTO orderobj = orderBookService.addOrderToOrderBook(instrumentid,orderDto);
		return new ResponseEntity<>(orderobj, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/orderbook/{instrumentid}/getOrders", method = RequestMethod.GET)
	public ResponseEntity<List<OrderDTO>> getOrders(@PathVariable("instrumentid") String instrumentid) { 
		return new ResponseEntity<>(orderBookService.getOrders(instrumentid), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/orderbook/{instrumentid}/getOrderDetails/{orderid}", method = RequestMethod.GET)
	public ResponseEntity<OrderDTO> getOrders(@PathVariable("instrumentid") String instrumentid,
										  @PathVariable("orderid") int orderid) { 
		return new ResponseEntity<>(orderBookService.getOrderDetails(instrumentid,orderid), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/orderbook/{instrumentid}/execute", method = RequestMethod.POST)
	public ResponseEntity<Object> execute(@PathVariable("instrumentid") String instrumentid, 
										 @RequestBody Execution execution) {
		orderBookService.execute(instrumentid,execution);
		return new ResponseEntity<>("Execution Completed!", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/getStatistics", method = RequestMethod.GET)
	public ResponseEntity<Object> getStatistics() { 
		return new ResponseEntity<>(orderBookService.getStatistics(), HttpStatus.OK);
	}
	
	
}
