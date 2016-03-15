-- Function: playersalecount(integer, character varying)

-- DROP FUNCTION playersalecount(integer, character varying);

CREATE OR REPLACE FUNCTION playersalecount(
    playernum integer,
    playerteam character varying)
  RETURNS integer AS
$BODY$
DECLARE
	customerCount int;
BEGIN	
	customerCount := 	(SELECT sum(quantity) -- orders with player products
				FROM ProductOrderWarehouse
				JOIN ( 	SELECT productId AS playerId -- product ids associated with player
					FROM PlayerMerchandisePlayer
					WHERE playernumber = playerNum AND teamname = playerTeam) AS playerProducts
				ON ProductOrderWarehouse.productId = playerProducts.playerId);

	RETURN customerCount;

END$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION public.playersalecount(integer, character varying)
  OWNER TO postgres;
