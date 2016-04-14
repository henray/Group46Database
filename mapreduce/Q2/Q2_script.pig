--load the data from HDFS and define the schema
raw = LOAD '/data2/cl03.csv' USING PigStorage(',') AS  (date, type:chararray, parl:int, prov:chararray, riding:chararray, lastname:chararray, firstname:chararray, gender:chararray, occupation:chararray, party:chararray, votes:int, percent:double, elected:int);

--filter BY votes >= 100
fltrd = FILTER raw BY votes >= 100;

--split relation into winners and losers
SPLIT fltrd INTO winner IF (elected == 1), loser IF (elected == 0);

--join on first 5 fields
results = JOIN winner BY ($0, $1, $2, $3 , $4), loser BY ($0, $1, $2, $3 , $4) PARALLEL 4;

--projects for lastnames and difference in votes
gen = FOREACH results generate winner::lastname, loser::lastname, winner::votes-loser::votes as difference;

final = FILTER gen BY difference < 10;

rmf q2

STORE final INTO 'q2';
