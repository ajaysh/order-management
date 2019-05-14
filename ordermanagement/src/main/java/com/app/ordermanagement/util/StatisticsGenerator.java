package com.app.ordermanagement.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import com.app.ordermanagement.model.Order;
import com.app.ordermanagement.model.OrderBook;

public class StatisticsGenerator {

	Map<String, Map<String,Object>> statisticsMap = new HashMap<String, Map<String,Object>>();                                

	public Object generateStatistics(Map<String, OrderBook> orderBookMap) {
		//		STRUCTURE OF STATISTIC OBJECT
		//		<instrument_id> -> <NumberOfOrders>        -> Count
		//		                   <TotalDemand>           ->  Count
		//		                   <SmallestOrder>         -> Order Object
		//		                   <LargestOrder>          -> Order Object
		//		                   <InvalidDemand>         -> Count
		//		                   <ValidDemand>           -> Count
		//		                   <Execution Price>       -> Count
		//		                   <AccumulatedExecution>  -> Count
		//		                   <LimitBreakDown>        ->  X limit   -> Count
		//		                                               Y limit   -> Count
		//		                                               Z limit   -> Count
		//		                                   
		//
		
		  Iterator<Map.Entry<String, OrderBook>> iterator = orderBookMap.entrySet().iterator();
		    while (iterator.hasNext()) {
		        Map.Entry<String, OrderBook> entry = iterator.next();
		        processOrderbook(entry.getKey(), entry.getValue());
		    }
		    
		return statisticsMap;
	}
	
	
	private void processOrderbook(String instrumentid, OrderBook orderBook) {
		Map<String, Object> attributesMap = new HashMap<String,Object>();
		statisticsMap.put(instrumentid, attributesMap);
		
		//1. NumberOfOrders.
		attributesMap.put("NumberOfOrders", (orderBook.getValidOrders().size() + orderBook.getInValidOrders().size()));
		
		//2. TotalDemand 
        //3. InvalidDemand 
		//4. ValidDemand
		long validOrdersDemand = orderBook.getValidOrders().stream().mapToLong(o-> o.getOrderQuantity()).sum();
		long inValidOrdersDemand = orderBook.getInValidOrders().stream().mapToLong(o-> o.getOrderQuantity()).sum();
		
		attributesMap.put("TotalDemand", (validOrdersDemand+inValidOrdersDemand));
		attributesMap.put("ValidDemand", validOrdersDemand);
		attributesMap.put("InvalidDemand", inValidOrdersDemand);
		
		//5.Smallest Order
		Order minQuantityOrder = orderBook.getValidOrders().stream().min(Comparator.comparing(Order::getOrderQuantity)).orElseThrow(NoSuchElementException::new);
		attributesMap.put("SmallestOrder", minQuantityOrder);
		
		
		//6. Largest Order
		Order maxQuantityOrder = orderBook.getValidOrders().stream().max(Comparator.comparing(Order::getOrderQuantity)).orElseThrow(NoSuchElementException::new);
		attributesMap.put("LargestOrder", maxQuantityOrder);

		//7. ExecutionPrice
		attributesMap.put("LargestOrder", orderBook.getExecutionPrice());

		//8. AccumulatedExecution
		long accumulatedExecution = orderBook.getValidOrders().stream().mapToLong(o-> o.getExecutedQuantity()).sum();
		attributesMap.put("AccumulatedExeution", accumulatedExecution);
		
	}
}
