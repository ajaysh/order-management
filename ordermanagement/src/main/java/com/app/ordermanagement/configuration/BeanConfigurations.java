package com.app.ordermanagement.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.app.ordermanagement.model.OrderBook;

@Configuration
public class BeanConfigurations {
	@Autowired private OrderBook orderbook;
	
	@Bean public Map<String, OrderBook> orderBookMap() {
        Map<String, OrderBook> map = new HashMap<String,OrderBook>();
        return map;
    }
}
