package com.app.ordermanagement.testsuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.app.ordermanagement.conroller.OrderControllerTest;
import com.app.ordermanagement.orderbook.processor.OrderBookProcessorTest;
import com.app.ordermanagement.orderbook.util.OrderBookHelperTest;
import com.app.ordermanagement.util.StatisTicsGeneratorTest;

@RunWith(Suite.class)
@SuiteClasses({OrderControllerTest.class,OrderBookHelperTest.class,OrderBookProcessorTest.class,StatisTicsGeneratorTest.class})
public class AllTests {

}
