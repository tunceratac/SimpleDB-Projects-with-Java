This assignment asks you implement two additional relational algebra operators to SimpleDB: union and rename.

• The rename operator takes three arguments: a scan denoting the input table, the name of a field from the table, and a new name for that field. The output table is identical to the input table, except that the specified field has been renamed.

• The union operator takes two scans as input, both of which have the same schema. The output consists of the union of those records, without removing duplicates. The rename operator has several uses. An obvious, simple use is to change a field value to something more readable. For example, the following query calculates the name and major of each student. It renames the field DName to be Major, so that the schema of the output table is [SName, Major].

<img src="https://github.com/tunceratac/SimpleDB-Projects-with-Java/blob/main/Task-4/1.png" alt="alt text" width="320" height="180">

Another use is to change the name of one field to be different from another field in order to join those fields together. For example, the following query calculates the students having the same major as student #1:

<img src="https://url/to/img.png" alt="alt text" width="320" height="180">


It is not crucial that you follow this query completely, but the idea is this: First select on the Student table to find the major Id of student #1. Then project on MajorId and rename that field as MajorOne. Then join this table with the Student table using the join predicate MajorId=MajorOne, and extract the student names.

A third use of the rename operator is to change the field names from one schema to match those of another, in order to use the union operator. For example, the following query returns the names of all the professors and students in the database; the output table has a single field named PersonName:

<img src="https://url/to/img.png" alt="alt text" width="320" height="180">

Your job is to write a Scan class for each of the rename and union operators. You can test your final code using my class HW4Test. For your information, executing HW4Test on my solution produces the following output:

Here are the records that have the same B-value as record 33: 13 33 53 73 93 113 133 153 173 193 213 233 253 273 293

Here are the records that have the B-value 'b1' or 'b9':
1 21 41 61 81 101 121 141 161 181 201 221 241 261 281 9 29 49 69 89 109 129 149 169 189 209 229 249 269 289

