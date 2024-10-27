This release strictly attempts to remove as much usage of the LOB table as possible in postgres. Specifically, in the JPA server, several database columns related to Batch2 jobs and searching
have been reworked so that they no will longer use LOB datatypes going forward. This
is a significant advantage on Postgresql databases as it removes a significant use
of the inefficient `pg_largeobject` table, and should yield performance boosts for
MSSQL as well.

