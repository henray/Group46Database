--load the data from HDFS and define the schema
raw = LOAD '/data2/cl03.csv' USING PigStorage(',') AS  (date, type:chararray, parl:int, prov:chararray, riding:chararray, lastname:chararray, firstname:chararray, gender:chararray, occupation:chararray, party:chararray, votes:int, percent:double, elected:int);

--need to group before sum
parliament = GROUP raw by parl;

--gets count of elected people i.e. number of members in parliament
parlcount = FOREACH parliament GENERATE group, SUM(raw.elected);

parldif = FOREACH parlcount GENERATE group, 

dump parlcount;

--rmf q3
--STORE parlcount INTO 'q3' USING PigStorage (',');