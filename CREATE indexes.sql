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