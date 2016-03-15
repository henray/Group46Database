-- We have a playoff customer lottery.

-- During playoffs, if more than target amount of people purchase 
-- products related to the most valuable player of the game,
-- then all those customers are entered into a draw to win a prize.  

-- This will take the mvp's player team and jersey number, a
-- randomly generated int, the target amount of product buyers,
-- and the gamen number (for identification)find the winning 
-- customer, and update a table with the winner, and the playoff 
-- game the draw was for. 

-- Function: public.lotterywinnerfinderx(integer, character varying, integer, integer, integer)

-- DROP FUNCTION public.lotterywinnerfinderx(integer, character varying, integer, integer, integer);

CREATE OR REPLACE FUNCTION public.lotterywinnerfinderx(
    playernum integer,
    playerteam character varying,
    rngvalue integer,
    target integer,
    gameid integer)
  RETURNS void AS
$BODY$DECLARE
	customerCount int;
	customer int;
BEGIN	
	customerCount := 	(SELECT count(*) 
				FROM (
					SELECT DISTINCT customerid
					FROM CustomerOrder
					JOIN (	SELECT orderId -- orders of player products within last two days
						FROM Orders
						JOIN ( 	SELECT DISTINCT orderId AS playerOrderId -- orders with player products
							FROM ProductOrderWarehouse
							JOIN ( 	SELECT productId AS playerId -- product ids associated with player
								FROM PlayerMerchandisePlayer
								WHERE playernumber = playerNum AND teamname = playerTeam) AS playerProducts
							ON ProductOrderWarehouse.productId = playerProducts.playerId) AS playerOrdersA
						ON Orders.orderId = playerOrdersA.playerOrderId
						WHERE AGE(Orderdate) < '1000 days') AS playerOrdersB
					ON Customerorder.orderId = playerOrdersB.orderId) AS a);

	IF customerCount >= target THEN
		rngValue := rngValue % customerCount;
		FOR customer IN (SELECT DISTINCT customerid
				FROM CustomerOrder
				JOIN (	SELECT orderId -- orders of player products within last two days
					FROM Orders
					JOIN ( 	SELECT DISTINCT orderId AS playerOrderId -- orders with player products
						FROM ProductOrderWarehouse
						JOIN ( 	SELECT productId AS playerId -- product ids associated with player
							FROM PlayerMerchandisePlayer
							WHERE playernumber = playerNum AND teamname = playerTeam) AS playerProducts
						ON ProductOrderWarehouse.productId = playerProducts.playerId) AS playerOrdersA
					ON Orders.orderId = playerOrdersA.playerOrderId
					WHERE AGE(Orderdate) < '1000 days') AS playerOrdersB
				ON Customerorder.orderId = playerOrdersB.orderId) LOOP
			IF rngValue = 0 THEN
				CREATE TABLE IF NOT EXISTS lotteryWinners (
					gameId		integer NOT NULL,
					winnerId 	integer,
					PRIMARY KEY(gameId)
					
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
			winnerId 	integer,
			PRIMARY KEY(gameId)
		);
		
		INSERT INTO lotteryWinners
			VALUES (gameId, NULL);
	END IF;

END$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION public.lotterywinnerfinderx(integer, character varying, integer, integer, integer)
  OWNER TO postgres;
