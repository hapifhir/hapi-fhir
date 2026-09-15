# Database Support

HAPI FHIR JPA Server maintains active support for several databases.

The supported databases are regularly tested for ongoing compliance and performance, and HAPI FHIR has specific performance optimizations for each platform. Make sure to use the HAPI FHIR dialect class as opposed to the default hibernate dialect class.

| Database                                                                    | Status        | Hibernate Dialect Class                                  | Notes                                                                                                                             |
|-----------------------------------------------------------------------------|---------------|----------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------|
| [MS SQL Server](https://www.microsoft.com/en-us/sql-server/sql-server-2019) | **Supported** | `ca.uhn.fhir.jpa.model.dialect.HapiFhirSQLServerDialect` | See [Microsoft SQL Server](#microsoft-sql-server) below for an important JDBC driver setting.                                     |
| [PostgreSQL](https://www.postgresql.org/)                                   | **Supported** | `ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect`  |                                                                                                                                   |
| [Oracle](https://www.oracle.com/ca-en/database/12c-database/)               | **Supported** | `ca.uhn.fhir.jpa.model.dialect.HapiFhirOracleDialect`    |                                                                                                                                   |
| [Cockroach DB](https://www.cockroachlabs.com/)                              | Experimental  | `ca.uhn.fhir.jpa.model.dialect.HapiFhirCockroachDialect` | A CockroachDB dialect was contributed by a HAPI FHIR community member. This dialect is not regularly tested, use with caution.    |
| MySQL                                                                       | Deprecated    | `ca.uhn.fhir.jpa.model.dialect.HapiFhirMySQLDialect`     | MySQL and MariaDB exhibit poor performance with HAPI FHIR and have therefore been deprecated. These databases should not be used. |
| MariaDB                                                                     | Deprecated    | `ca.uhn.fhir.jpa.model.dialect.HapiFhirMariaDBDialect`   | MySQL and MariaDB exhibit poor performance with HAPI FHIR and have therefore been deprecated. These databases should not be used. |

# Large ID Lists

Some searches constrain a column to a list of resource IDs: a search on `_id` with many values, or a reference parameter such as `subject=` with many values. This happens in particular when a request is narrowed to the compartments a user is permitted to see, since every permitted compartment contributes one value.

Sending one bind parameter per ID puts a ceiling on how large such a list can be, because databases limit the number of bind parameters in a single statement - PostgreSQL accepts 65,535, SQL Server 2,100, and Oracle rejects `IN` lists longer than 1,000 expressions.

Any list holding more than `StorageSettings#setLargeIdListJsonThreshold(int)` IDs is therefore bound as a single JSON array parameter which the database unpacks with its own JSON function: `jsonb_array_elements_text` on PostgreSQL, `JSON_TABLE` on Oracle (bound as a CLOB), and `OPENJSON` on SQL Server (see [Compatibility Level](#compatibility-level) below). Lists at or under the threshold are unchanged, as are all lists on MySQL, MariaDB and H2, which keep one bind parameter per ID and therefore keep the engine's limit.

See [Large ID List JSON Binding](performance.html#large-id-list-json-binding) for how to tune or disable this behaviour.

# Microsoft SQL Server

The HAPI FHIR JPA schema uses plain `VARCHAR` columns (not `NVARCHAR`). The Microsoft SQL Server JDBC driver sends string parameters as Unicode (`NVARCHAR`) by default (`sendStringParametersAsUnicode=true`), so every string-parameter comparison forces an implicit `NVARCHAR`-to-`VARCHAR` conversion on the server side. This prevents SQL Server from using indexes on those columns and can severely degrade query performance on large datasets.

When using SQL Server, always add `sendStringParametersAsUnicode=false` to the JDBC connection URL, e.g.:

```
jdbc:sqlserver://localhost:1433;databaseName=hapi;sendStringParametersAsUnicode=false
```

For more information, see [Microsoft JDBC driver documentation](https://learn.microsoft.com/en-us/sql/connect/jdbc/setting-the-connection-properties).

## Compatibility Level

`OPENJSON`, used to bind [large ID lists](#large-id-lists) above, requires a database compatibility level of 130 (SQL Server 2016) or higher. A database restored from an older version can still be running at a lower level. Check it first:

```sql
SELECT compatibility_level FROM sys.databases WHERE name = DB_NAME()
```

If, and only if, the reported level is below 130, raise it - to the highest level your SQL Server version supports (150 for SQL Server 2019, 160 for SQL Server 2022/Azure SQL), not to 130 itself:

```sql
ALTER DATABASE [hapi] SET COMPATIBILITY_LEVEL = 150
```

HAPI FHIR checks the compatibility level the first time it needs to bind a large ID list, and caches the answer. On a database below 130, the ID list is sent as one bind parameter per ID instead - the behaviour of earlier versions - and a warning naming this requirement is logged once. The server never fails to start because of this. If the check itself cannot be completed - for example because the database user may not read `sys.databases` - HAPI FHIR retries on later searches, up to three times, before falling back the same way and logging that failure once.

# Experimental Support

HAPI FHIR uses the Hibernate ORM to provide database abstraction. This means that HAPI FHIR could theoretically also work on other databases supported by Hibernate.
For example, although we do not regularly test or validate on other platforms, community members have reported successfully running HAPI FHIR on:
 
- DB2
- Cache
- Firebird

