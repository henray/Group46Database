raw = LOAD '/data2/emp.csv' USING PigStorage(',') AS (empid:int, fname:chararray, lname:chararray, deptname:chararray, isManager:chararray, mgrid:int, salary:int);

managers = FILTER raw BY ((deptname == 'Finance') AND (isManager == 'Y'));

employees = FILTER raw BY isManager == 'N';

grouped = GROUP employees BY mgrid;

grouped_count = FOREACH grouped GENERATE ($0) AS mgrid, COUNT (employees.mgrid) AS count;

joined = JOIN employees BY mgrid, managers BY empid;

joined2 = JOIN joined BY employees::mgrid, grouped_count BY mgrid;

result = GROUP joined2 BY employees::mgrid;

final = FOREACH result GENERATE FLATTEN(joined2.grouped_count::mgrid) AS managerId, FLATTEN(joined2.joined::managers::lname) AS lastname, FLATTEN(joined2.grouped_count::count) AS employees;

final_results = DISTINCT final;

DUMP final_results;

rmf q5

STORE final_results INTO 'q5';

EXPLAIN final_results;