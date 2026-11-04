# Upgrade Notes

## Spring Boot 4 / Spring Framework 7 / Hibernate 7 / Jakarta EE 11

HAPI FHIR now builds on Spring Boot 4.0 and the platform it brings with it. This is a **breaking
change**: applications embedding HAPI FHIR must upgrade to these baselines as well, since the two
lines are not binary-compatible.

| Component | Previous | This release |
| --------- | -------- | ------------ |
| Spring Boot | 3.5.x | 4.0.x |
| Spring Framework | 6.2.x | 7.0.x |
| Spring Data | 2024.0.x | 2025.1.x |
| Hibernate ORM | 6.6.x | 7.2.x |
| Hibernate Search | 7.2.x | 8.2.x |
| Jakarta Servlet | 6.0 | 6.1 |
| Jakarta Persistence | 3.1 | 3.2 |
| Jakarta REST (JAX-RS) | 3.1 | 4.0 |
| JUnit (tests) | 5.x | 6.x |

Notable points for downstream projects:

- **Jackson 3.** HAPI FHIR uses Jackson 3 (`tools.jackson`), which is also Spring Boot 4's default; see
  the Jackson 3 section below.
- **The generated database schema is unchanged.** Hibernate 7 would otherwise alter some column types
  (Oracle `binary_double`, higher timestamp precision, unbounded CockroachDB strings); HAPI's dialects
  pin these back to the previous mappings, so **no database migration is required** when upgrading.
- **Test infrastructure:** if you run HAPI's test utilities, note the move to JUnit 6, and that Spring
  Framework 7 defaults `@Nested` test classes to a test-method scoped `ExtensionContext`. HAPI sets
  `spring.test.extension.context.scope=test_class` for its own build to preserve the previous
  behaviour.
- **`@MockBean` / `@SpyBean`** (removed in Spring Boot 4) are replaced throughout by Spring's
  `@MockitoBean` / `@MockitoSpyBean`.

## Jackson 3

HAPI FHIR now depends on Jackson 3 instead of Jackson 2. Jackson packages move from
`com.fasterxml.jackson` to `tools.jackson`, and several Jackson defaults changed. See the
[Jackson 3.0 release notes](https://github.com/FasterXML/jackson/wiki/Jackson-Release-3.0).

## MySQL Support Removed

MySQL is no longer a supported database platform, and the MySQL JDBC driver is no longer bundled with the HAPI FHIR CLI. MySQL had been deprecated because of its poor performance with HAPI FHIR. The `migrate-database` command can no longer connect to a MySQL database unless the driver is added to the classpath manually, and MySQL users should migrate to a supported platform such as PostgreSQL before upgrading.

## ValueSet Expansions Stored by an Earlier Release

Upgrading does not change expansions that are already stored. Two kinds stored by earlier releases are wrong:

- Codes a `ValueSet` lists from a `CodeSystem`, or `CodeSystem` version, the server does not have. Before 9.0.0
  these were stored as members; pre-expansion now fails for such a `ValueSet`.
- Versions joined onto the code system (`url|version`), stored by 8.12 and 8.13 with full-text indexing disabled
  for an include or exclude that names a `CodeSystem` version.

Storing a `CodeSystem` again is not enough to replace these expansions. This query lists the `ValueSet`s whose stored
expansion has either kind. It reads the database only:

```sql
SELECT vs.URL, vs.VER
FROM TRM_VALUESET vs
WHERE vs.EXPANSION_STATUS = 'EXPANDED'
  AND EXISTS (
    SELECT 1
    FROM TRM_VALUESET_CONCEPT c
    WHERE c.VALUESET_PID = vs.PID
      AND (c.SOURCE_PID IS NULL OR c.SYSTEM_URL LIKE '%|%'))
```

The list can also include `ValueSet`s whose codes come from a remote terminology server, UCUM, or the built-in
HL7 code systems. Invoke
[`$invalidate-expansion`](/hapi-fhir/docs/server_jpa/valueset_expansion.html#invalidate-expansion) on each
`ValueSet` listed; invalidating one that was correct pre-expands it again. Then
[find the ones that failed](/hapi-fhir/docs/server_jpa/valueset_expansion.html#finding-failed-valuesets).
