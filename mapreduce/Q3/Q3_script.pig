--load the data from HDFS and define the schema
raw = LOAD '/data2/cl03.csv' USING PigStorage(',') AS (date, type:chararray, parl:int, prov:chararray, riding:chararray, lastname:chararray, firstname:chararray, gender:chararray, occupation:chararray, party:chararray, votes:int, percent:double, elected:int);

fltrd = FILTER raw BY (type == 'Gen') AND (elected == 1);

--need to group before sum
parlgroup = GROUP fltrd BY parl;

--gets count of elected people i.e. number of members in parlgroup

parlprevious = FOREACH parlgroup GENERATE group + 1 AS num, COUNT (fltrd.elected) AS count;

parlcurrent = FOREACH parlgroup GENERATE group AS num, COUNT (fltrd.elected) AS count;

parljoined = JOIN parlprevious BY (num), parlcurrent BY (num);

parldifference = FOREACH parljoined GENERATE parlprevious::num, parlcurrent::count - parlprevious::count;

DUMP parldifference

--rmf q3
--STORE parlprevious INTO 'q3' USING PigStorage (',');
