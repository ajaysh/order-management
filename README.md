# order-management Service
Order management service exposes following restfull end points to do order management of any financial instrument.
Service is a spring boot restfull service which gets deployed and executed in embedded Tomcat server. Currently it 
uses in-memory data store.

```
1. Create order book -> GET http://localhost:8080/orderbook/{instrumentid}.
2. Close order book. -> GET http://localhost:8080/orderbook/{instrumentid}/close.
3. Add order to order boook. -> POST http://localhost:8080/orderbook/{instrumentid}/createOrder
	                              REQUEST BODY: {
                                              "orderQuantity": "10",
                                              "price": "200"
	                                            }
4. Get orders list. -> GET http://localhost:8080/orderbook/{instrumentid}/getOrders
5. Get order details. -> GET http://localhost:8080/orderbook/{instrumentid}/getOrderDetails/{orderid}
6. Execute order. -> POST http://localhost:8080/orderbook/inst1234/execute
	                              REQUEST BODY: {
                                             "executionQuantity": "38",
                                             "executionPrice":200
	                                            }

```

## Validations ##
Following are the list of validations added to the service.
1. Order cannot be executed on open book.
2. Orders cannot be added on closed book.
3. Once submitted, orders cannot be modified.
4. Subsequent execution price should be same as first execution price.
5. Invalid orders are not executed (Invalid orders are limit price orders with price less than execution price).
6. Executions cannot be submitted if enitre order demand is executed.
7. Executions are distributed evenly across orders. 

## TESTING ##
Please refer TestCasesUsingPostmanClient_pdf.pdf, it covers testing of all the rest end points and different
validation scenarios.

## To Do Features ##
1. Better unit test coverage.
2. Service to print statistics/matrix.
2. Persisting data to database.
3. Horizontal scaling of application to accomodate hight load.


