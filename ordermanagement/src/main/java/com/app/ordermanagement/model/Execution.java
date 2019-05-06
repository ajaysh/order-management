package com.app.ordermanagement.model;
/**
 * class to hold execution request data(execution quantity and execution price).
 * @author ajaysharma
 *
 */
public class Execution{

	private long executionQuantity;
	private long executionPrice;

	public Execution(long executionQuantity, long executionPrice) {
		super();
		this.executionQuantity = executionQuantity;
		this.executionPrice = executionPrice;
	}

	public Execution() {
		super();
	}
	
	
	public long getExecutionQuantity() {
		return executionQuantity;
	}

	public void setExecutionQuantity(long executionQuantity) {
		this.executionQuantity = executionQuantity;
	}

	public long getExecutionPrice() {
		return executionPrice;
	}

	public void setExecutionPrice(long executionPrice) {
		this.executionPrice = executionPrice;
	}
}
