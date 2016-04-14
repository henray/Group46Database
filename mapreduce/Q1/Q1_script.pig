--load the data from HDFS and define the schema
raw = LOAD '/data2/cl03.csv' USING PigStorage(',') AS  (date, type:chararray, parl:int, prov:chararray, riding:chararray, lastname:chararray, firstname:chararray, gender:chararray, occupation:chararray, party:chararray, votes:int, percent:double, elected:int);

--some data entries use the middle name AS well, so this way we will catch all of them
fltrd = FILTER raw BY percent >= 60.0;

--project only the needed fields
gen = FOREACH fltrd GENERATE CONCAT(firstname, CONCAT(' ', lastname));

--choose only the smallest date
results = DISTINCT gen;

--print the result tuple to the screen
DUMP results;

rmf q1

STORE results INTO 'q1';
