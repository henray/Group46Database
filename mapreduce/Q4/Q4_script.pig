(date, type:chararray, parl:int, prov:chararray, riding:chararray, lastname:chararray, firstname:chararray, gender:chararray, occupation:chararray, party:chararray, votes:int, percent:double, elected:int);

Parliament number, the party name and the number of MPs from that party, along with the total number of MPs in that Parliament. 

parl, party, COUNT( mp in the party), COUNT(MP in the parl)



--load the data from S3 and define the schema
raw = LOAD '/user/hadoop/data2.csv' USING PigStorage(',') AS  (date, type:chararray, parl:int, prov:chararray, riding:chararray, lastname:chararray, firstname:chararray,gender:chararray, occupation:chararray, party:chararray,votes:int, percent:double, elected:int);

fltrd = FILTER raw BY elected == 1;

parl_group = GROUP fltrd BY parl;

parl_count = FOREACH parl_group GENERATE ($0) AS parl, COUNT($1) AS parl_count;

party_group = GROUP raw BY (parl, party);

party_count = FOREACH party_group GENERATE FLATTEN($0) AS (parl, party),  COUNT($1) AS party_count;

joined = JOIN party_count BY parl, parl_count BY parl;

results = FOREACH joined GENERATE parl_count::parl AS parl, party_count::party AS party, party_count::party_count AS party_count, parl_count::parl_count AS parl_count;

STORE results INTO '/user/hadoop/q4output.csv' USING PigStorage('\t','-schema');