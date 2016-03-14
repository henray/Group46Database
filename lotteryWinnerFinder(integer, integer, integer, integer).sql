-- Function: public."lotteryWinnerFinder"(integer, integer, integer, integer)

-- DROP FUNCTION public."lotteryWinnerFinder"(integer, integer, integer, integer);

CREATE OR REPLACE FUNCTION public."lotteryWinnerFinder"(
    pId integer,
    "rngValue" integer,
    target integer,
    "gameId" integer)
  RETURNS void AS
$BODY$DECLARE
	customerCount int;
	customer int;
	pId ALIAS FOR $1;
	rngValue ALIAS FOR $2;
	target ALIAS FOR $3;
	gameId ALIAS FOR $4;
BEGIN	
	customerCount := 	(SELECT count(*) 
				FROM (
					SELECT DISTINCT customerid
					FROM CustomerOrder
					JOIN (	SELECT orderId -- orders of pId products within last two days
						FROM Orders
						JOIN ( 	SELECT DISTINCT orderId AS pIdOrderId -- orders with pId products
							FROM ProductOrderWarehouse
							JOIN ( 	SELECT productId AS pIdId -- product ids associated with pId
								FROM PlayerMerchandisePlayer
								WHERE playernumber = pId) AS pIdProducts
							ON ProductOrderWarehouse.productId = pIdProducts.pIdId) AS pIdOrdersA
						ON Orders.orderId = pIdOrdersA.pIdOrderId
						WHERE AGE(Orderdate) < '1000 days') AS pIdOrdersB
					ON Customerorder.orderId = pIdOrdersB.orderId) AS a);

	rngValue := rngValue % customerCount;

	IF customerCount >= target THEN
		FOR customer IN (SELECT DISTINCT customerid
				FROM CustomerOrder
				JOIN (	SELECT orderId -- orders of pId products within last two days
					FROM Orders
					JOIN ( 	SELECT DISTINCT orderId AS pIdOrderId -- orders with pId products
						FROM ProductOrderWarehouse
						JOIN ( 	SELECT productId AS pIdId -- product ids associated with pId
							FROM PlayerMerchandisePlayer
							WHERE playernumber = pId) AS pIdProducts
						ON ProductOrderWarehouse.productId = pIdProducts.pIdId) AS pIdOrdersA
					ON Orders.orderId = pIdOrdersA.pIdOrderId
					WHERE AGE(Orderdate) < '1000 days') AS pIdOrdersB
				ON Customerorder.orderId = pIdOrdersB.orderId) LOOP
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
