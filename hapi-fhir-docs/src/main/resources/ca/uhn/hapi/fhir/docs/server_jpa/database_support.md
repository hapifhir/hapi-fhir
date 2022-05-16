# Database Support

HAPI FHIR JPA Server maintains active support for several databases:

- [MS SQL Server](https://www.microsoft.com/en-us/sql-server/sql-server-2019)
- [PostgreSQL](https://www.postgresql.org/)
- [Oracle](https://www.oracle.com/ca-en/database/12c-database/)

Use of any of the above databases is fully supported by HAPI-FHIR, and code is actively written to work with them.

# Experimental Support

HAPI FHIR currently provides experimental for the following databases, but does not actively support them, or write code specifically to work with them:

- [Cockroach DB](https://www.cockroachlabs.com/)
 
HAPI FHIR uses the Hibernate ORM to provide database abstraction. This means that HAPI FHIR could theoretically also work on other databases supported by Hibernate.
For example, although we do not regularly test or validate on other platforms, community members have reported successfully running HAPI FHIR on:
 
- DB2
- Cache
- Firebird

# Deprecated Support

These databases were previously supported by HAPI FHIR JPA Server, but have since been deprecated, and should not be used. 

- [MySQL](https://www.mysql.com/)

