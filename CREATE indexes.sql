-- the below index will sort the prices of objects on our website,
-- which will be useful for queries where customers only want to
-- see prices in a certain range, or want to see products by price.

CREATE INDEX priceSorting ON product (retailprice);

-- the below index will create an index for customerid, so that
-- when customers want to see their own orders, the system will be
-- able to efficiently return them. Since customers often want to
-- review past orders, this should save time vs trawling through
-- the rows in customerorder.

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
