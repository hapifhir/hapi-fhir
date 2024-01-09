# Database Support

HAPI FHIR JPA Server maintains active support for several databases.

The supported databases are regularly tested for ongoing compliance and performance, and HAPI FHIR has specific performance optimizations for each platform. Make sure to use the HAPI FHIR dialect class as opposed to the default hibernate dialect class.

| Database                                                                    | Status        | Hibernate Dialect Class                                  | Notes                                                                                                                             |
|-----------------------------------------------------------------------------|---------------|----------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------|
| [MS SQL Server](https://www.microsoft.com/en-us/sql-server/sql-server-2019) | **Supported** | `ca.uhn.fhir.jpa.model.dialect.HapiFhirH2Dialect`        |                                                                                                                                   |
| [PostgreSQL](https://www.postgresql.org/)                                   | **Supported** | `ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect`  |                                                                                                                                   |
| [Oracle](https://www.oracle.com/ca-en/database/12c-database/)               | **Supported** | `ca.uhn.fhir.jpa.model.dialect.HapiFhirOracleDialect`    |                                                                                                                                   |
| [Cockroach DB](https://www.cockroachlabs.com/)                              | Experimental  | `ca.uhn.fhir.jpa.model.dialect.HapiFhirCockroachDialect` | A CockroachDB dialect was contributed by a HAPI FHIR community member. This dialect is not regularly tested, use with caution.    |
| MySQL                                                                       | Deprecated    | `ca.uhn.fhir.jpa.model.dialect.HapiFhirMySQLDialect`     | MySQL and MariaDB exhibit poor performance with HAPI FHIR and have therefore been deprecated. These databases should not be used. |
| MariaDB                                                                     | Deprecated    | `ca.uhn.fhir.jpa.model.dialect.HapiFhirMariaDBDialect`   | MySQL and MariaDB exhibit poor performance with HAPI FHIR and have therefore been deprecated. These databases should not be used. |

# Experimental Support

HAPI FHIR uses the Hibernate ORM to provide database abstraction. This means that HAPI FHIR could theoretically also work on other databases supported by Hibernate.
For example, although we do not regularly test or validate on other platforms, community members have reported successfully running HAPI FHIR on:
 
- DB2
- Cache
- Firebird

