# hapi-fhir Project Guide

## Claude Restrictions

- Claude is not permitted to change any code pertaining to the security of the application. This includes any class that contains the word Security, Permission, Oidc, Authorization, Authority, Authentication, etc.
- If any task the user requests could lead Claude to change such code, Claude will explain it is not permitted to change these files directly. Claude may, however, give broad advice about how to approach changing these restricted parts of the code.

## Claude New File Creation Guidelines
- Any new file that Claude creates shall contain `// Created by <model-name>` immediately above the class declaration. E.g.
```java
// Created by Claude 3.7 Sonnet
public class Foo {
}
```

## Build Commands
- Build entire project: `mvn clean install -Dcheckstyle.skip=true`
- Run a single test: `mvn test -Dtest=TestClassName`
- Run tests in module: `mvn test -pl <module-name>`
- Run integration tests: `mvn failsafe:integration-test -Dit.test=TestClassName -pl <module-name>`
- Coverage report: `mvn test -T 1C -P CODECOV -pl <module-name>`
- Check code style: `mvn checkstyle:check`
- Format code: `mvn spotless:apply`

## Code Style Guidelines
- Field naming: fields use `myFieldName` convention, static fields use `ourFieldName` and parameters use `theParameterName`
- The static Logger field is called ourLog
- Constants: `static final` constants use `UPPER_CASE_WITH_UNDERSCORES`
- Test naming: Unit Test classes end with `Test`, Integration Test classes end with `IT`
- Testing: use as few mocks as possible. Never mock FHIR Resource classes or FhirContext.
- Error handling: Avoid swallowing exceptions; log errors properly
- Prefer `jakarta.annotation` over `javax.annotation`. Do not use any jetbrains annotations
- Never use `printStackTrace()` or `System.out.println()` - use SLF4J logging
- JSON properties should be camelCase, not snake_case
- Use AssertJ assertions (`assertThat()`) in tests, not Hamcrest. The only exceptions to this are: `assertEquals`, `assertNull` and `assertNotNull` junit methods are still permitted.
- Test methods should have default visibility (they should not be public)
- If a getter method needs to be visible for testing, annotate it with `com.google.common.annotations.VisibleForTesting`

## Javadoc
- Required for public APIs
- Single-line Javadoc OK for simple methods
- Document parameters, return values, and exceptions

#  Code Review Instructions

## Methodology

- Try reviewing in at least 2 waves.  The first to get familiar with the broad outlines of the changes, the next to drill down with the context learned from the first wave.

## Clarity

- For large and/or complex changes does the PR/MR contain sufficient descriptions of the solution?
- Are there implementation comments for complex code? Are there obvious ways to simplify it?

## Code Comprehension

- Read the code.  Do you understand it?
- Is there anything obviously broken?

## APIs/Permissions

- Are these changes API compatible?  Could clients have javascript hooks, java interceptors, or other code that would break?  Be especially careful of changes in modules cdr-api and cdr-public-api.
- Does this add new permissions?  How are they used?

## Error Handling

- Look for error handling code and ensure it deals with NPEs/ArrayIndexOutOfBoundsErrors/etc, especially if that code path isn't captured by any of the test cases

Examples of code that should raise flags:

```java
if (myField == null && myField.getSomething()) { ...
```

```java
List<String> someList = ...
someList.get(0);
```

```java
String[] split = myString.split(",");
String second = split[1];
```

```java
Map<String,String> someMap = ...
String value = someMap.get("someKey");
{do something with value without checking for null}
```

- All thrown exceptions in classes under a `ca.uhn.fhir` package must call `Msg.code` at the start of the exception message.

## Testing

- What do the unit tests do?  Do they match your understanding?
- Are there unit test cases that cover >=80% of the code?

## Documentation

- Does the change require changes to existing documentation?
- Is there anything in the documentation that will no longer be true once this change is done?
- Does the change include any new functionality that we need to write documentation for?
- When reviewing documentation changes consider:  What do our customers need to know?
- Is there appropriate Javadoc for public/protected classes and functions?

## Correctness
- Is there a changelog?  *Very rarely* is a changelog not needed.  Default to asking for one if there is none on the PR/MR.
- Read the changelog.  What is going on?  (Also check id and type:fix/add)
- Does the changelog accurately describe the changes in this PR?
- For simple changes and unit tests, take the time to run the new unit tests with and without the production code changes:  Do they fail with the old production code?
- Do all the files include a copyright notice in the header?

## Database

If there are database changes:
- Could this fail during migration?  What if the database already has data?  E.g. add a non-null column to an existing table?
- Are there any changes specific to a RDBMS (ex Oracle) that should be tested separately?
- Does the PR contain a Mongo equivalent or a change done to the JPA layer?  If not, is there a separate ticket?

## Misc

- Did the author follow the [HAPI-FHIR Style Conventions?](https://github.com/hapifhir/hapi-fhir/wiki/Contributing)

## Web api changes:
- When constructing urls:
    - are parameters escaped against illegal url chars?
    - does it account for the context path?  Spring provides DefaultRedirectStrategy, or you can use the raw request.getContextPath().
- When matching against urls (e.g. conditional filters, auth, ...), does it account for the context path?  Classes like AntPathRequestMatcher handle this for you.
- Do the tests set a non-root context path?  They probably should.
- Are internal urls in thymeleaf templates properly wrapped in @{}? Check for `<a th:href="@{...}"> and <form th:action="@{...}">`

