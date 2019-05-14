package com.app.ordermanagement.conroller.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.app.ordermanagement.conroller.OrderControllerTest;
import com.app.ordermanagement.conroller.service.impl.OrderBookServiceImplTest;
import com.app.ordermanagement.orderbook.processor.OrderBookProcessorTest;
import com.app.ordermanagement.orderbook.util.OrderBookHelperTest;

@RunWith(Suite.class)
@SuiteClasses({OrderControllerTest.class,OrderBookHelperTest.class,OrderBookProcessorTest.class})
public class AllTests {

}
