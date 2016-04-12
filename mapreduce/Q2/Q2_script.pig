--load the data from HDFS and define the schema
raw = LOAD '/data2/cl03.csv' USING PigStorage(',') AS  (date, type:chararray, parl:int, prov:chararray, riding:chararray, lastname:chararray, firstname:chararray, gender:chararray, occupation:chararray, party:chararray, votes:int, percent:double, elected:int);

--filter by votes > 100
fltrd = FILTER raw by votes > 100;

--split relation into winners and losers
SPLIT fltrd INTO winner IF elected == 1, loser IF elected == 0;

--join on first 5 fields
results = join winner by ($0, $1, $2, $3 , $4), loser by ($0, $1, $2, $3 , $4);

--projects for lastnames and difference in votes
gen = foreach results generate winner::lastname, loser::lastname, winner::votes-loser::votes as difference;

final = FILTER gen by difference < 10;

STORE final INTO 'q2' USING PigStorage (',');