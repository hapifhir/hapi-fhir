# HFQL Syntax Examples

This page contains example HFQL queries.

# Find Resources with Repeating Elements

You can select and return only the resources that have multiple repetitions of an element by using a _HAVING_ clause. Note that this query loads each Patient resource from the database in order to test whether to include it in the result set, so it can potentially be very slow if there are many resources that only have a single repetition.   

```sql
SELECT id, name[0], name[1]
FROM Patient
HAVING name.count() > 1
```
