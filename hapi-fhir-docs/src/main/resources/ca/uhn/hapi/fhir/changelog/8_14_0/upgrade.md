# Upgrade Notes

## Jackson 3

HAPI FHIR now depends on Jackson 3 instead of Jackson 2. Jackson packages move from
`com.fasterxml.jackson` to `tools.jackson`, and several Jackson defaults changed. See the
[Jackson 3.0 release notes](https://github.com/FasterXML/jackson/wiki/Jackson-Release-3.0).

## MySQL Support Removed

MySQL is no longer a supported database platform, and the MySQL JDBC driver is no longer bundled with the HAPI FHIR CLI. MySQL had been deprecated because of its poor performance with HAPI FHIR. The `migrate-database` command can no longer connect to a MySQL database unless the driver is added to the classpath manually, and MySQL users should migrate to a supported platform such as PostgreSQL before upgrading.
