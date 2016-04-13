raw = LOAD '/data2/cl03.csv' USING PigStorage(',') AS (date, type:chararray, parl:int, prov:chararray, riding:chararray, lastname:chararray, firstname:chararray, gender:chararray, occupation:chararray, party:chararray, votes:int, percent:double, elected:int);

filtrd = FILTER raw BY elected == 1;

parl_group = GROUP filtrd BY parl;

parl_count = FOREACH parl_group GENERATE group AS parl, COUNT(filtrd.elected) AS members;

party_group = GROUP filtrd BY (parl, party);

party_count = FOREACH party_group GENERATE FLATTEN($0) as (parl, party),  COUNT($1) as party_count;

joined = JOIN parl_count BY parl, party_count BY parl;

results = FOREACH joined GENERATE parl_count::parl AS parl, party_count::party AS party, party_count::party_count AS party_count, parl_count::members AS members;

store results into 'q4' using PigStorage('\t','-schema');
