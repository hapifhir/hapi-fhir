# Review instructions

Rules for reviewing HAPI FHIR changes. They add to `CLAUDE.md`, whose rules apply to reviews as well.

## What Important means here

HAPI FHIR is a published library: downstream projects compile against it and run against later releases without recompiling.

- A removed or renamed public or protected method, class or field is Important. So is reduced visibility, or a changed parameter list, parameter type or return type. Deprecate the old signature first and remove it in a later release.
- A change to the default behaviour of an existing API without a configuration flag or deprecation path is Important.
- A removed or renamed JSON field, or an optional operation parameter made mandatory, is Important.
- Any change to restricted security or database-migration production code (the "Restricted Code" section of `CLAUDE.md`) is Important, whatever it changes.
- Style, naming and refactoring suggestions are Nit at most.

## Cap the nits

Report at most five Nits per review, and roll the rest into one line.

## Do not report

- Anything checkstyle enforces (`hapi-fhir-checkstyle`): `Msg.code()` on `throw new` (`HapiErrorCodeCheck`), `javax` and `org.jetbrains` nullability annotations, Hamcrest, `System.out.println`, `javax.transaction`.
- Formatting that spotless applies, and license headers; the build adds them.
- The `// Created by <model>` marker on a new file.
- Defensive null or bounds checks in test code; `list.get(0)` in a test is fine.
- Problems in lines the change did not touch.

## Always check

- A thrown exception that checkstyle does not see, such as a rethrow or an exception built outside a `throw new` statement, starts its message with `Msg.code(...)`.
- A new error code is recorded on the [Bumping-Error-Code](https://github.com/hapifhir/hapi-fhir/wiki/Bumping-Error-Code) wiki page, so no two errors share a code.
- The change has a changelog entry under `hapi-fhir-docs/src/main/resources/ca/uhn/hapi/fhir/changelog/`, with a `type` and `issue` that match the change and a `title` that describes the user-visible effect.
- Public methods and constructors annotate reference-type parameters and return values with `@Nullable` or `@Nonnull` from `jakarta.annotation`. A `Nullable` or `Nonnull` imported from anywhere else must become `jakarta.annotation`.
- A changed test assertion is at least as strong as the one it replaces, and a new test would fail without the production change.
- `@Transactional` on a private method, or called from the same class, starts no transaction. An exception caught inside a transactional method and not rethrown suppresses the rollback the caller expects.
- A `SystemRequestDetails` built for work started by a request carries that request's `RequestPartitionId`.
- Mutable instance state on a singleton (interceptor, resource provider, Spring bean) is shared by concurrent requests.
