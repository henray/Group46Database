-- Function: public."lotteryWinnerFinder"(integer, integer, integer, integer)

-- DROP FUNCTION public."lotteryWinnerFinder"(integer, integer, integer, integer);

CREATE OR REPLACE FUNCTION public."lotteryWinnerFinder"(
    mvp integer,
    "rngValue" integer,
    target integer,
    "gameId" integer)
  RETURNS void AS
$BODY$DECLARE
	customerCount int;
	customer int;
	mvp ALIAS FOR $1;
	rngValue ALIAS FOR $2;
	target ALIAS FOR $3;
	gameId ALIAS FOR $4;
BEGIN	
	customerCount := 	(SELECT count(*) 
				FROM (
					SELECT DISTINCT customerid
					FROM CustomerOrder
					JOIN (	SELECT orderId -- orders of mvp products within last two days
						FROM Orders
						JOIN ( 	SELECT DISTINCT orderId AS mvpOrderId -- orders with mvp products
							FROM ProductOrderWarehouse
							JOIN ( 	SELECT productId AS mvpId -- product ids associated with mvp
								FROM PlayerMerchandisePlayer
								WHERE playernumber = mvp) AS mvpProducts
							ON ProductOrderWarehouse.productId = mvpProducts.mvpId) AS mvpOrdersA
						ON Orders.orderId = mvpOrdersA.mvpOrderId
						WHERE AGE(Orderdate) < '1000 days') AS mvpOrdersB
					ON Customerorder.orderId = mvpOrdersB.orderId) AS a);

	rngValue := rngValue % customerCount;

	IF customerCount >= target THEN
		FOR customer IN (SELECT DISTINCT customerid
				FROM CustomerOrder
				JOIN (	SELECT orderId -- orders of mvp products within last two days
					FROM Orders
					JOIN ( 	SELECT DISTINCT orderId AS mvpOrderId -- orders with mvp products
						FROM ProductOrderWarehouse
						JOIN ( 	SELECT productId AS mvpId -- product ids associated with mvp
							FROM PlayerMerchandisePlayer
							WHERE playernumber = mvp) AS mvpProducts
						ON ProductOrderWarehouse.productId = mvpProducts.mvpId) AS mvpOrdersA
					ON Orders.orderId = mvpOrdersA.mvpOrderId
					WHERE AGE(Orderdate) < '1000 days') AS mvpOrdersB
				ON Customerorder.orderId = mvpOrdersB.orderId) LOOP
			IF rngValue = 0 THEN
				CREATE TABLE IF NOT EXISTS lotteryWinners (
					gameId		integer NOT NULL,
					winnerId 	integer
				);
				INSERT INTO lotteryWinners
					VALUES (gameId, customer);
				EXIT;
			ELSE
				rngValue := rngValue - 1;
			END IF;
		END LOOP;
	ELSE
		CREATE TABLE IF NOT EXISTS lotteryWinners (
			gameId		integer NOT NULL,
			winnerId 	integer
		);
		
		INSERT INTO lotteryWinners
			VALUES (gameId, NULL);
	END IF;

END$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION public."lotteryWinnerFinder"(integer, integer, integer, integer)
  OWNER TO postgres;
