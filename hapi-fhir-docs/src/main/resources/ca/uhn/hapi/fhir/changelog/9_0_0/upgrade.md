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

This applies when upgrading from a release before 9.0.0 with `ValueSet` pre-expansion enabled, which is the default.

9.0.0 corrects two errors in `ValueSet` pre-expansion, but a `ValueSet` pre-expanded before the upgrade keeps its
earlier expansion:

- Before 9.0.0, codes a `ValueSet` lists from a `CodeSystem`, or `CodeSystem` version, the server does not have were
  stored as members (#8415). Validation, `:in` searches and `withCodeInValueSet` authorization rules accept these
  codes. Pre-expansion of such a `ValueSet` now fails.
- 8.12 with full-text indexing disabled stored the code system of an include or exclude that names a `CodeSystem`
  version as `url|version` (#8766). Validation against these `ValueSet`s rejects valid codes.

Storing the `CodeSystem` again does not replace these expansions. After upgrading, find these `ValueSet`s and
pre-expand them again:

1. Run this query against the database. It reads the database only:

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

   The list can also include `ValueSet`s whose codes come from a remote terminology server, UCUM, or the built-in HL7
   code systems. Pre-expanding these again is harmless.

2. Invoke [`$invalidate-expansion`](/hapi-fhir/docs/server_jpa/valueset_expansion.html#invalidate-expansion) on each
   `ValueSet` listed.

3. When the pre-expansions have finished,
   [find the ones that failed](/hapi-fhir/docs/server_jpa/valueset_expansion.html#finding-failed-valuesets) and load
   the `CodeSystem`s they are missing. Storing a missing `CodeSystem` resource pre-expands them again.
