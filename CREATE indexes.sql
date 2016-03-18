-- the below index will sort orders by date, which will allow for
-- quick checks to see if product has been sold in a certain time
-- frame. This will speed up our third query.

CREATE INDEX orderdateindex ON orders (orderdate);


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

CLUSTER product USING retailprice;
