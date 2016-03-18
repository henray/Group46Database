-- the below index will sort the prices of objects on our website,
-- which will be useful for queries where customers only want to
-- see prices in a certain range, e.g.
--      SELECT retailprice, productname
--      FROM product
--      WHERE retailprice < 20

CREATE INDEX priceSorting ON product (retailprice);


-- the below index will create an index for customerid, so that
-- when customers want to see their own orders, the system will be
-- able to efficiently return them. It will also speed up our
-- stored procedure customersFromProduct, as it has to compare
-- customerID. This index will allow those lookups to be quick.

CREATE INDEX indexCustomerOrder ON customerorder (customerid);

-- i don't think clustering is necessary, as things are always
-- changing. new orders are always being added, so clustering on
-- order id will only be useful until the customer next purchases
-- an item. It is neither useful for prices on items, as new
-- players are always being added, and there aren't even enough
-- products that the physical chunking of things by price for
-- caching will speed things up. However, should you want to do
-- that, the below could be run. 
-- This index will also help our 4th query from deliveralbe 2, as shown below:
  /* Fourth query will grab the customers who have paid with VISA on products that have come from Adidas*/
  /*SELECT Customer.firstName, Customer.lastName FROM Customer 
  WHERE Customer.customerID IN 
  (SELECT DISTINCT customerID FROM CustomerOrder, Orders, Productorderwarehouse
  WHERE CustomerOrder.orderID = Orders.orderID 
  AND Orders.orderID = Productorderwarehouse.orderID 
  AND Orders.payment = 'visa'
  AND Productorderwarehouse.productID 
  IN 
  (
  	SELECT productID 
  	FROM ShipmentProduct, ShipmentSupplier
  	WHERE ShipmentProduct.shipmentID = ShipmentSupplier.shipmentID 
  	AND ShipmentSupplier.supplierName = 'Adidas'
  ) 
  ORDER BY customerID);*/

CLUSTER product USING retailprice;
