# Test Container Optimization Requirements

## Problem Statement

The current test architecture using `HapiEmbeddedDatabasesExtension` creates significant performance and resource issues:

1. **Double Container Boot**: Both `HapiSchemaMigrationTest` and `BaseDatabaseVerificationIT` use the same extension, causing each of the 4 database containers (H2, PostgreSQL, MS SQL Server, Oracle) to boot twice
2. **Memory Pressure**: All 4 containers boot simultaneously, causing CI memory issues
3. **Slow Test Execution**: Container startup time is multiplied by redundant boots

## Solution Goals

Transform the test execution model from:
```
Current: [H2, PostgreSQL, MSSQL, Oracle] x [SchemaTest, VerificationTest] = 8 container boots
```

To:
```
Target: H2 → [SchemaTest, VerificationTest] → shutdown
        PostgreSQL → [SchemaTest, VerificationTest] → shutdown  
        MSSQL → [SchemaTest, VerificationTest] → shutdown
        Oracle → [SchemaTest, VerificationTest] → shutdown
```

Expected output format:
```
Oracle 
 | ---- Schema Migration
 | ---- Database Verification
Postgres
 | ---- Schema Migration
 | ---- Database Verification
H2
 | ---- Schema Migration  
 | ---- Database Verification
MS SQL Server
 | ---- Schema Migration
 | ---- Database Verification
```

## Current Architecture Analysis

### HapiEmbeddedDatabasesExtension
**Location**: `hapi-fhir-jpaserver-test-utilities/src/main/java/ca/uhn/fhir/jpa/embedded/HapiEmbeddedDatabasesExtension.java`

**Key Features**:
- Manages 4 embedded databases (H2, PostgreSQL, MS SQL Server, Oracle)
- Implements `AfterAllCallback` for cleanup
- Provides `DatabaseVendorProvider` for parameterized tests
- Handles database initialization and schema setup
- Supports different HAPI FHIR versions for data insertion

**Current Lifecycle**:
1. `@ExtendWith(HapiEmbeddedDatabasesExtension.class)` boots all 4 containers
2. Parameterized tests iterate over all databases
3. Extension cleans up after all tests complete

### HapiSchemaMigrationTest
**Location**: `hapi-fhir-jpaserver-test-utilities/src/test/java/ca/uhn/fhir/jpa/embedded/HapiSchemaMigrationTest.java`

**Requirements**:
- **CRITICAL**: Needs empty database to start (no pre-existing schema)
- Tests migration from `FIRST_TESTED_VERSION` (V5_1_0) through all current versions
- Uses manual `SchemaMigrator` with precise migration control
- Verifies specific table structures and data integrity per database vendor
- Must NOT have any automatic schema migration happening

**Key Test Methods** (MUST NOT CHANGE):
- `testMigration()` - Full migration cycle test
- `testCreateMigrationTableIfRequired()` - Migration table creation
- `verifyHfjResSearchUrlMigration()` - Partition support verification
- `verifyTrm_Concept_Desig()` - Terminology concept table verification
- `verifyForcedIdMigration()` - FHIR ID migration (issue #5546)
- `verifyHfjResourceFhirIdCollation()` - MS SQL Server collation

### BaseDatabaseVerificationIT and Children
**Location**: `hapi-fhir-jpaserver-test-r5/src/test/java/ca/uhn/fhir/jpa/dao/r5/database/BaseDatabaseVerificationIT.java`

**Requirements**:
- **CRITICAL**: Needs fully migrated database with complete schema
- Spring-based test with full application context
- Uses `DataSource dataSource()` bean with automatic migration
- Tests full FHIR R5 operations (CRUD, search, includes, $everything)

**Key Test Methods** (MUST NOT CHANGE):
- `testCreateRead()` - Basic CRUD with variable data sizes
- `testDelete()` - Resource deletion and exception handling
- `testEverything()` - Patient `$everything` operation
- `testSearchWithInclude()` - Search with resource includes
- `testSyntaxForVariousQueries()` - Query syntax validation

**Child Classes**:
- All inherit from `BaseDatabaseVerificationIT`
- Use same database infrastructure
- Must continue to work without modification

## Critical Conflict Resolution

### DataSource Bean Configuration Conflict
**Problem**: `BaseDatabaseVerificationIT.TestConfig` has:
```java
@Bean
public DataSource dataSource() {
    // Automatic migration setup
    SchemaMigrator schemaMigrator = new SchemaMigrator(...);
    schemaMigrator.migrate();  // ← RUNS MIGRATIONS AUTOMATICALLY
    return dataSource;
}
```

**Conflict**: This automatic migration CANNOT happen during `HapiSchemaMigrationTest` as that test is specifically testing the migration process itself.

**Solution Requirements**:
1. Schema migration test must get clean, empty database
2. Database verification test must get fully migrated database
3. Both tests must use the same physical container instance
4. Migration must only happen once per database, between the two test phases

## Implementation Strategy

### Option 1: Custom JUnit 5 Extension (Recommended)
Create new extension that:
1. Boots one database container at a time
2. Runs schema migration test first (empty database)
3. Runs database verification test second (migrated database)
4. Shuts down container before moving to next database

### Option 2: TestContainers Singleton Pattern
Use TestContainers singleton pattern with:
1. `@Container` annotations with static containers
2. Custom test ordering annotations
3. Shared database state management
4. Manual container lifecycle control

### Option 3: Spring Test Context Hierarchy
Create shared test context configuration:
1. Parent context with container management
2. Child contexts for specific test types
3. Context caching to reuse containers
4. Custom TestExecutionListener for ordering

## Technical Implementation Plan

### Phase 1: Container Lifecycle Management
1. **Create new extension**: `HapiSequentialDatabaseTestExtension`
   - Replaces `HapiEmbeddedDatabasesExtension` for affected tests
   - Manages single container lifecycle
   - Provides database-specific configuration

2. **Modify test annotations**:
   - Remove `@ExtendWith(HapiEmbeddedDatabasesExtension.class)`
   - Add `@ExtendWith(HapiSequentialDatabaseTestExtension.class)`
   - Add database-specific parameters

### Phase 2: Test Execution Sequencing
1. **Create test suite runner**:
   - JUnit 5 `@Suite` with custom execution order
   - Database-specific test grouping
   - Container startup/shutdown coordination

2. **Modify test classes**:
   - Remove parameterization from individual tests
   - Add database-specific configuration injection
   - Maintain existing test method signatures

### Phase 3: DataSource Configuration Management
1. **Create conditional DataSource beans**:
   - Migration-aware DataSource for schema tests
   - Pre-migrated DataSource for verification tests
   - Same physical container, different configuration

2. **Test phase detection**:
   - Environment variables or system properties
   - Spring profiles for test phases
   - Bean condition annotations

### Phase 4: Maven Integration
1. **Test execution coordination**:
   - Custom Maven Surefire configuration
   - Test class ordering
   - Container lifecycle management across test phases

2. **CI/CD optimization**:
   - Parallel database execution (if resources allow)
   - Container reuse between Maven modules
   - Memory usage monitoring

## Validation Requirements

### Functional Validation
1. **All existing tests must pass unchanged**
2. **Test execution time must improve significantly**
3. **Memory usage must decrease in CI environment**
4. **Test output must match expected format**

### Technical Validation
1. **Container boot count**: Each database boots exactly once
2. **Migration integrity**: Schema migration test runs on empty database
3. **Verification integrity**: Database verification test runs on migrated database
4. **Container isolation**: No cross-contamination between database types

## Risk Assessment

### High Risk
- **Test behavior changes**: Any modification to actual test logic
- **Spring context conflicts**: DataSource bean configuration conflicts
- **Container lifecycle bugs**: Premature shutdown or startup failures

### Medium Risk
- **Test execution ordering**: Dependency on specific execution sequence
- **Resource cleanup**: Incomplete container shutdown
- **Maven integration**: Test discovery and execution issues

### Low Risk
- **Performance regression**: Slower execution than expected
- **Output formatting**: Test result presentation issues
- **Documentation**: Missing implementation details

## Success Criteria

1. **Performance**: Total test execution time reduced by 50%+ 
2. **Memory**: CI memory usage reduced to sustainable levels
3. **Reliability**: All existing tests pass without modification
4. **Maintainability**: Clear separation of concerns between test phases
5. **Scalability**: Easy to add new database types or test classes

## Implementation Notes

### TestContainers Documentation References
- **Container Reuse**: https://logarithmicwhale.com/posts/faster-tests-by-resuing-testcontainers-in-spring-boot/
- **Official Docs**: https://java.testcontainers.org/
- **Singleton Pattern**: https://www.testcontainers.org/test_framework_integration/manual_lifecycle_control/

### Spring Test Context Management
- Use `@DirtiesContext` strategically to control context lifecycle
- Leverage `TestExecutionListener` for custom test phase management
- Consider `@TestMethodOrder` for deterministic execution

### Database-Specific Considerations
- **Oracle**: May require special licensing/container setup
- **PostgreSQL**: Foreign key index validation requirements
- **MS SQL Server**: Collation settings verification
- **H2**: Different behavior in embedded vs server mode

## Questions for Clarification

1. **Test Execution Environment**: Are tests run in parallel Maven modules, or sequentially?
2. **Container Registry**: Are there specific Docker registry requirements for database images?
3. **Memory Constraints**: What are the specific memory limits in CI environment?
4. **Database Versions**: Are there specific database version requirements for each vendor?
5. **Test Isolation**: Are there any cross-test dependencies that need to be preserved?