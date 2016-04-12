--load the data from HDFS and define the schema
raw = LOAD '/data2/cl03.csv' USING PigStorage(',') AS  (date, type:chararray, parl:int, prov:chararray, riding:chararray, lastname:chararray, firstname:chararray, gender:chararray, occupation:chararray, party:chararray, votes:int, percent:double, elected:int);

fltrd = FILTER raw BY (type == 'Gen') AND (elected == 1);

--need to group before sum
parlgroup = GROUP fltrd BY parl;

--gets count of elected people i.e. number of members in parliament

parlcount = FOREACH parliament GENERATE group as num, COUNT (fltrd.elected) as count;

parljank = FOREACH parliament GENERATE group - 1 as num, COUNT (fltrd.elected) as count;

parldif = join parlcount by (num), parljank by (num);

parlfinal = FOREACH parldif GENERATE parlcount::num, parljank::count - parlcount::count;

dump parlfinal;

--rmf q3
--STORE parlcount INTO 'q3' USING PigStorage (',');