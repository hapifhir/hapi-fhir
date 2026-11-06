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

## ValueSets That Use an Unknown CodeSystem

Codes a `ValueSet` lists from an unknown `CodeSystem`, one the server does not have in any version or at the version
named, are no longer taken as valid (#8415). `$expand`, `:in` / `:not-in` searches and pre-expansion of such a `ValueSet`
now fail, and authorization rules using it deny access. To keep using it, load the `CodeSystem` or configure a remote
terminology server that has it. See
[ValueSets That Use an Unknown CodeSystem](/hapi-fhir/docs/validation/validation_support_modules.html#valuesets-that-use-an-unknown-codesystem).

## ValueSet Expansions Stored by an Earlier Release

9.0.0 fixes several `ValueSet` pre-expansion bugs (#8415, #8766, #8818). The fixes apply to new expansions: with
pre-expansion enabled, which is the default, a `ValueSet` pre-expanded before the upgrade keeps its earlier expansion.
Validation, searches and authorization rules answer from it, so they can reject valid codes or allow reads they should
deny.

After upgrading, find these `ValueSet`s and pre-expand them again:

1. Run this query against the database. It reads the database only:

   ```sql
   SELECT vs.URL, vs.VER
   FROM TRM_VALUESET vs
   WHERE vs.EXPANSION_STATUS = 'EXPANDED'
     AND (vs.TOTAL_CONCEPTS = 0
       OR EXISTS (
         SELECT 1
         FROM TRM_VALUESET_CONCEPT c
         WHERE c.VALUESET_PID = vs.PID
           AND (c.SOURCE_PID IS NULL OR c.SYSTEM_URL LIKE '%|%'))
       OR EXISTS (
         SELECT 1
         FROM HFJ_SPIDX_URI u
         WHERE u.RES_ID = vs.RES_ID
           AND u.HASH_IDENTITY = -4082773750105051027
           AND NOT EXISTS (
             SELECT 1
             FROM TRM_VALUESET_CONCEPT c
             WHERE c.VALUESET_PID = vs.PID
               AND c.SYSTEM_URL = u.SP_URI)))
   ```

   The number identifies the `reference` search parameter of `ValueSet`. The list can also include `ValueSet`s whose
   codes come from a remote terminology server, UCUM, or the built-in HL7 code systems, `ValueSet`s that are empty, and
   `ValueSet`s with an include that matches no codes. Pre-expanding these again is harmless.

2. Invoke [`$invalidate-expansion`](/hapi-fhir/docs/server_jpa/valueset_expansion.html#invalidate-expansion) on each
   `ValueSet` listed.

3. When the pre-expansions have finished,
   [find the ones that failed](/hapi-fhir/docs/server_jpa/valueset_expansion.html#finding-failed-valuesets), load
   the `CodeSystem`s they are missing, and invoke `$invalidate-expansion` on each of them again.
