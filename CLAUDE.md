# CLAUDE.md

This file provides guidance to AI Agents when working with code in this repository.

## Overview

HAPI FHIR is an open-source Java implementation of the HL7 FHIR specification. The project is a multi-module Maven build with 60+ modules supporting FHIR clients, servers, and JPA-based data storage.

- **Java**: Java 17
- **Build System**: Apache Maven
- **License**: Apache 2.0
- **Website**: https://hapifhir.io
- **Repository**: https://github.com/hapifhir/hapi-fhir

### Restricted Code

- **CRITICAL** You are not permitted to change any code under src/main folders pertaining to the security of the application. This includes any class that contains the word Security, Permission, Oidc, Authorization, Authority, Authentication, KeyStore, etc.
- You may not modify existing security test code.

- **CRITICAL** You are not permitted to change any code under src/main folders pertaining to database migrations. This includes:
    - any classes under a package that starts with `ca.uhn.fhir.jpa.migrate`
    - any classes that contain `MigrationTask` in their name

- **CRITICAL** If any task the user requests could lead you to change Restricted Code you will explain you are not permitted to change these files directly. You may, however, offer insights and advice to help the user better understand this code so they are able to make the necessary changes themselves.

## Build Commands

**NEVER** use `javac` to build or `java` to run - always use Maven.

```bash
# Fast build (skip tests/validation)
mvn -P FASTINSTALL install

# Full build with tests
# ** DO NOT RUN THIS ** It takes over an hour to complete
mvn clean install

# Run single test class
mvn test -Dtest=TestClassName

# Run tests in specific module
mvn test -pl <module-name>

# Run integration tests
mvn failsafe:integration-test -Dit.test=TestClassName -pl <module-name>

# Code coverage
mvn test -T 1C -P JACOCO -pl <module-name>

# Code formatting
mvn spotless:apply

# Code style check
mvn checkstyle:check

# Add license headers
mvn clean install -DskipTests -P DIST
```

### Test Output Locations

- Unit test reports: `target/surefire-reports/*TestName*.*`
- Integration test reports: `target/failsafe-reports/*TestName*.*`

## Project Architecture

### Core Architectural Layers

**Layer 1: Foundation**
- `hapi-fhir-base` - Core parsing, serialization, FhirContext
- `hapi-fhir-structures-*` - FHIR version-specific models (DSTU2, DSTU3, R4, R4B, R5)
- `hapi-fhir-validation-resources-*` - Validation schemas per FHIR version

**Layer 2: Client & Server Framework**
- `hapi-fhir-client` - FHIR client API
- `hapi-fhir-client-apache-http5` / `hapi-fhir-client-okhttp` - HTTP transport implementations
- `hapi-fhir-server` - Server framework (REST provider pattern, servlet-based)
- `hapi-fhir-jaxrsserver-base` - JAX-RS server support

**Layer 3: Storage Abstraction**
- `hapi-fhir-storage` - Abstract storage API
- `hapi-fhir-jpa` - JPA/Hibernate storage core
- `hapi-fhir-sql-migrate` - Database migrations (Flyway)

**Layer 4: JPA Server Features**
- `hapi-fhir-jpaserver-base` - Complete JPA server with DAOs, search, subscriptions, GraphQL
- `hapi-fhir-jpaserver-model` - JPA entity definitions
- `hapi-fhir-jpaserver-searchparam` - Search parameter indexing
- `hapi-fhir-jpaserver-subscription` - FHIR subscriptions (REST-hooks, email, WebSocket)
- `hapi-fhir-jpaserver-mdm` - Master Data Management (patient matching, golden records)
- `hapi-fhir-jpaserver-ips` - International Patient Summary
- `hapi-fhir-jpaserver-hfql` - HAPI FHIR Query Language

**Layer 5: Specialized Features**
- `hapi-fhir-storage-batch2` / `hapi-fhir-storage-batch2-jobs` - Batch job framework
- `hapi-fhir-storage-mdm` / `hapi-fhir-server-mdm` - MDM implementation
- `hapi-fhir-storage-cr` - Clinical Reasoning
- `hapi-fhir-server-openapi` - OpenAPI/Swagger documentation
- `hapi-fhir-server-cds-hooks` - CDS Hooks protocol
- `hapi-fhir-converter` - FHIR format/version converters

**Test Infrastructure**
- `hapi-fhir-test-utilities` - Common test utilities
- `hapi-fhir-jpaserver-test-utilities` - JPA test base classes (`BaseJpaR4Test`, etc.)
- `hapi-fhir-jpaserver-test-*` - Version-specific test servers
- `hapi-fhir-storage-test-utilities` - Storage layer testing

### Key Architectural Patterns

**Multi-Version FHIR Support**
- Version-specific structure classes generated from StructureDefinition via `hapi-tinder-plugin`
- `FhirVersionEnum` tracks supported versions
- `FhirContext` abstracts version differences

**DAO/Repository Pattern**
- JPA DAOs in `hapi-fhir-jpaserver-base/.../dao`
- Spring Data repositories in `hapi-fhir-repositories`
- Abstract storage interfaces in `hapi-fhir-storage`

**Interceptor Chain**
- Extensible interceptor mechanism for client and server
- Hooks at multiple request lifecycle points
- Used for authorization, auditing, subscription delivery, etc.

**Batch2 Job Framework**
- Asynchronous batch processing for bulk operations
- Partitionable jobs for scalability
- Used for reindexing, cleanup, bulk exports

## Code Standards

### Naming Conventions (Enforced)

```java
private String myInstanceField;           // Instance fields: my*
private static String ourStaticField;     // Static fields: our*
private static final Logger ourLog;       // Logger: always ourLog
public void method(String theParameter) { // Parameters: the*
    String localVar;                      // Local vars: camelCase
    private static final String CONSTANT; // Constants: UPPER_CASE
}
```

### Code Quality Rules

**Logging & Error Handling**
- **NEVER** use `printStackTrace()` or `System.out.println()` - use SLF4J
- Logger: `private static final Logger ourLog = LoggerFactory.getLogger(ClassName.class);`
- Avoid swallowing exceptions

**Testing**
- Use AssertJ assertions (`assertThat()`, `assertThatThrownBy()`)
- **NEVER** use `assertDoesNotThrow()`
- Test methods: default visibility (not public)
- Use `BundleBuilder` to build test bundles
- Use `BundleUtil` to extract details from bundles
- Prefer tests without mocks when possible
- Search for existing test infrastructure before adding new test classes
- Prefer adding `@Test` methods to existing test classes

**Dependencies & Imports**
- **NEVER** use `org.jetbrains.annotations`
- Use `jakarta.annotation` (NOT `javax.annotation`)
- Use import statements instead of fully-qualified class names in code
- JSON properties: camelCase (not snake_case)

**File Creation**
- New files must include: `// Created by <model-name>` above class declaration
- Prefer final fields initialized in constructors over mutable autowired fields

**Javadoc**
- Required for public APIs, especially interface methods
- Document parameters, return values, and exceptions
- Single-line Javadoc acceptable for simple methods

## Testing Methodology

**Test-Driven Development (TDD) - MANDATORY**

All code changes must follow TDD:

1. **Red**: Write a failing test that describes desired behavior. Verify it fails.
2. **Green**: Write minimal code to make the test pass.
3. **Refactor**: Improve code while keeping tests passing.

Before writing production code, write a failing test first. All code changes must include corresponding tests.

## Key Dependencies

- Spring Framework 6.2.12
- Spring Boot 3.x
- Hibernate 6.x
- Hibernate Search 7.x
- Jackson 2.x
- JUnit 5.x
- Mockito 5.x
- AssertJ (assertions)
- Flyway 10.x (migrations)

## Documentation

- Complete documentation: http://hapifhir.io
- Live demo server: http://hapi.fhir.org
- Javadocs: https://hapifhir.io/hapi-fhir/apidocs/
- Issue tracker: https://github.com/hapifhir/hapi-fhir/issues
- Commercial support: https://smilecdr.com

## Development Workflow

1. Prefer `rg` for searching over `find` or `grep`
2. Follow TDD: RED-GREEN-REFACTOR
3. Run tests to verify changes
4. Format code with `mvn spotless:apply`
5. Check style with `mvn checkstyle:check`
