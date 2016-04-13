raw = LOAD '/data2/emp.csv' USING PigStorage(',') AS (empid:int, fname:chararray, lname:chararray, deptname:chararray, isManager:chararray, mgrid:int, salary:int);

managers = FILTER raw BY ((deptname == 'Finance') AND (isManager == 'Y'));

DESCRIBE managers;

employees = FILTER raw BY isManager == 'N';

DESCRIBE employees;

grouped = GROUP employees BY mgrid;

DESCRIBE grouped;

grouped_count = FOREACH grouped GENERATE ($0) AS mgrid, COUNT (employees.mgrid) AS count;

DESCRIBE grouped_count;

joined = JOIN employees BY mgrid, managers BY empid;

DESCRIBE joined;

joined2 = JOIN joined BY employees::mgrid, grouped_count BY mgrid;

DESCRIBE joined2;

result = GROUP joined2 BY employees::mgrid;

DESCRIBE result;

final = FOREACH result GENERATE joined2.grouped_count::mgrid AS managerId, joined2.joined::managers::lname AS lastname, joined2.grouped_count::count AS employees;

DUMP final;


