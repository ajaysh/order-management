package com.app.ordermanagement.conroller.service.impl;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.app.ordermanagement.model.OrderBook;
import com.app.ordermanagement.orderbook.util.OrderBookHelper;
import com.app.ordermanagement.service.OrderBookService;
import com.app.ordermanagement.service.impl.OrderBookServiceImpl;
import com.app.ordermanagement.util.OrderMapper;

@RunWith(SpringRunner.class)
public class OrderBookServiceImplTest {

	
	@TestConfiguration
    static class orderBookServiceImplTestContextConfiguration {
  
        @Bean("TestOrderBookServiceImpl")
        public OrderBookService orderBookService() {
            return new OrderBookServiceImpl();
        }
        
        @Bean("TestorderMapper")
        public OrderMapper orderMapper() {
            return new OrderMapper();
        }

        @Bean("TestOrderBookHelper")
        public OrderBookHelper orderBookHelper() {
            return new OrderBookHelper();
        }
 
    }
	
	@MockBean
	private Map<String, OrderBook> orderBookMap;
	
	@Autowired
	@Qualifier("TestOrderBookServiceImpl")
	private OrderBookService orderBookService;
	
	@Captor
    ArgumentCaptor<String> keyCaptor;
 
    @Captor
    ArgumentCaptor<OrderBook> valueCaptor;
    
	@Test
	public void createOrderBook_Success() throws Exception{
//		orderBookService.createOrderBook("1234");
//		
//	 Mockito.verify(orderBookMap, Mockito.times(1)).put(keyCaptor.capture(), valueCaptor.capture());
//	 List<String> keys = keyCaptor.getAllValues();
//	 assertEquals("1234", keys.get(0));
	}

}

final class MyEntry<K, V> implements Map.Entry<K, V> {
    private final K key;
    private V value;

    public MyEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        return old;
    }
}