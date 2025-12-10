# Refactoring Plan: MethodUtil.getResourceParameters

## Overview

Refactor the 318-line "god method" `getResourceParameters` in `ca.uhn.fhir.rest.server.method.MethodUtil` into a chain of compact, focused private methods while maintaining 100% functional equivalence through comprehensive testing.

## Critical Context

- **File**: `hapi-fhir-server/src/main/java/ca/uhn/fhir/rest/server/method/MethodUtil.java`
- **Method**: `getResourceParameters` (lines 98-417, 318 lines)
- **Risk Level**: CRITICAL - Used by all FHIR server method bindings; failures break entire server
- **Integration Point**: Called by `BaseMethodBinding` constructor (line 89) - sole integration point
- **Test File**: Must create from scratch (mentioned backup file doesn't exist)

## Implementation Phases

### Phase 1: Test Creation (Est. 2-3 days)

**Goal**: Establish comprehensive test coverage before any refactoring

#### Step 1: Create Test Infrastructure
- Create `MethodUtilTest.java` with nested test class structure
- Set up FhirContext (R4) in @BeforeEach
- Create test provider classes with annotated methods

#### Step 2: Write ~100 Comprehensive Tests

Organize tests into nested classes by parameter category:

1. **TypeBasedParametersTest** (~20 tests)
   - ServletRequest, ServletResponse, RequestDetails, IInterceptorBroadcaster
   - SummaryEnum, PatchTypeEnum, SearchContainedModeEnum, SearchTotalModeEnum
   - TagList (NullParameter special case)

2. **SearchParametersTest** (~15 tests)
   - @RequiredParam and @OptionalParam with various types
   - Chain whitelists/blacklists, declared types, composite types
   - Description extraction, collections

3. **IncludeParameterTest** (~8 tests)
   - String type vs Collection<Include> type
   - Validation errors for invalid types

4. **ResourceParameterTest** (~10 tests)
   - All modes: RESOURCE, BODY, BODY_BYTE_ARRAY, ENCODING
   - @Operation and @Patch method detection
   - Error cases for invalid types

5. **OperationParameterTest** (~15 tests)
   - Basic operation params
   - TypeName resolution and validation
   - Cardinality (min/max), descriptions, examples

6. **ValidationParameterTest** (~10 tests)
   - @Validate.Mode with ValidationModeEnum
   - @Validate.Profile with String
   - Custom converters testing

7. **CollectionHandlingTest** (~10 tests)
   - Single-level collections, nested collections
   - Synthetic class handling (lambdas/proxies)
   - Triple-nested collection errors
   - IPrimitiveType<Date> and IPrimitiveType<String> resolution

8. **ErrorCasesTest** (~10 tests)
   - Unrecognized annotations
   - Invalid type configurations
   - All ConfigurationException scenarios

#### Testing Approach
- **Minimal Mocking**: Use real FhirContext, real annotations, real Method reflection
- **AssertJ Assertions**: Use `assertThat()` and `assertThatThrownBy()`
- **Test Provider Pattern**: Inner static classes with annotated methods

#### Validation Criteria
- Run: `mvn test -Dtest=MethodUtilTest`
- **Success**: 100% pass rate, 80%+ code coverage
- Generate coverage: `mvn test -P JACOCO -Dtest=MethodUtilTest`

---

### Phase 2: Refactoring (Est. 3-4 days)

**Goal**: Extract private methods while maintaining test success

#### Extraction Strategy: Safe → Risky Order

**Step 1: Create Supporting Classes**
- Add `CollectionTypeInfo` static inner class to encapsulate collection metadata
- Fields: effectiveParameterType, declaredParameterType, innerCollectionType, outerCollectionType

**Step 2: Extract Simple Annotations** (Low Risk)
- Method: `createSimpleAnnotationParameter()`
- Handles: RawParam, IdParam, ServerBase, Elements, Since, At, Count, Offset, GraphQLQueryUrl, GraphQLQueryBody, Sort, TransactionParam, ConditionalUrlParam
- Returns null if annotation not recognized

**Step 3: Extract Type-Based Parameters** (Low Risk)
- Method: `createTypeBasedParameter()`
- Handles: ServletRequest, ServletResponse, RequestDetails, IInterceptorBroadcaster, enum parameters
- Simple type checking, no complex logic

**Step 4: Extract Search Parameters** (Medium Risk)
- Method: `createSearchParameter()`
- Handles both @RequiredParam and @OptionalParam uniformly
- Extracts name, types, chains, descriptions

**Step 5: Extract Include Parameters** (High Risk)
- Method: `createIncludeParameter()`
- Complex collection validation logic
- Handles String vs Collection<Include> distinction

**Step 6: Extract Resource Parameters** (High Risk)
- Method: `createResourceParameter()`
- Mode detection based on type
- Checks for @Operation and @Patch on method

**Step 7: Extract Operation Parameters** (Very High Risk)
- Methods: `createOperationParameter()`, `createValidationModeParameter()`, `createValidationProfileParameter()`
- Complex type resolution via FhirContext
- Custom converter logic for validation parameters

**Step 8: Extract Collection Detection** (Very High Risk)
- Method: `detectCollectionTypes()`
- Complex reflection, synthetic class handling
- Nested collection detection with validation

**Step 9: Extract Primitive Resolution** (High Risk)
- Method: `resolvePrimitiveType()`
- IPrimitiveType<?> to concrete type mapping
- FhirContext element definition resolution

**Step 10: Create Annotation Dispatcher** (Coordinator)
- Method: `createAnnotationBasedParameter()`
- Dispatches to appropriate handler
- Preserves break-on-first-match behavior

#### After Each Extraction
1. Run tests: `mvn test -Dtest=MethodUtilTest`
2. Verify: 100% pass rate (no regressions)
3. Format: `mvn spotless:apply`
4. Check style: `mvn checkstyle:check`
5. Commit: `git commit -m "Extract [method_name]"`

#### Final Main Method Structure (~60 lines)
```java
public static List<IParameter> getResourceParameters(...) {
    // For each parameter:
    // 1. Handle TagList special case
    // 2. Detect collection types → CollectionTypeInfo
    // 3. Resolve IPrimitiveType → concrete type
    // 4. Try type-based parameter creation
    // 5. Try annotation-based parameter creation
    // 6. Validate parameter was created
    // 7. Initialize types and add to list
}
```

---

### Phase 3: Validation (Est. 1 day)

**Goal**: Ensure no integration issues or regressions

#### Module-Level Testing
```bash
# Run all server module tests
mvn test -pl hapi-fhir-server

# Run critical test classes
mvn test -Dtest=SearchMethodBindingTest -pl hapi-fhir-server
mvn test -Dtest=ConformanceMethodBindingTest -pl hapi-fhir-server
mvn test -Dtest=ReadMethodBindingTest -pl hapi-fhir-server
mvn test -Dtest=ResourceBindingTest -pl hapi-fhir-server
```

**Success Criteria**:
- No new test failures compared to baseline
- All critical method binding tests pass
- Test execution time similar to baseline

#### Integration Testing
```bash
# Run integration tests (if available)
mvn failsafe:integration-test -pl hapi-fhir-server
```

**Success Criteria**:
- Integration tests pass
- Server starts without ConfigurationException
- FHIR operations work correctly (Search, Create, Operation, Include)

#### Final Code Review
- [ ] All extracted methods have complete Javadoc
- [ ] No behavior changes (functional equivalence)
- [ ] All error messages preserved (Msg.code values)
- [ ] Test coverage ≥ 80%
- [ ] No checkstyle violations
- [ ] Clean git history with incremental commits

---

## Extracted Methods Summary

| Method | Lines | Risk | Handles |
|--------|-------|------|---------|
| CollectionTypeInfo | Support class | Low | Collection metadata holder |
| createSimpleAnnotationParameter | ~40 | Low | 13 simple annotations |
| createTypeBasedParameter | ~20 | Low | 8 type-based parameters |
| createSearchParameter | ~25 | Medium | RequiredParam, OptionalParam |
| createIncludeParameter | ~20 | High | IncludeParam validation |
| createResourceParameter | ~25 | High | ResourceParam mode detection |
| createOperationParameter | ~30 | Very High | OperationParam type resolution |
| createValidationModeParameter | ~25 | High | Validate.Mode with converter |
| createValidationProfileParameter | ~20 | High | Validate.Profile with converter |
| detectCollectionTypes | ~40 | Very High | Collection unwrapping, synthetic classes |
| resolvePrimitiveType | ~15 | High | IPrimitiveType resolution |
| createAnnotationBasedParameter | ~30 | Medium | Annotation dispatcher |

**Result**: 318 lines → ~60 lines main method + ~290 lines in 12 focused methods

---

## Risk Mitigation

### Critical Risks
1. **Breaking BaseMethodBinding**: Keep public signature identical, test with real bindings
2. **Collection detection changes**: Comprehensive collection tests, extract last
3. **State mutation bugs**: Use immutable CollectionTypeInfo, explicit parameters
4. **Annotation processing order**: Preserve break-on-first-match behavior exactly

### Mitigation Strategies
- Incremental extraction (safest first)
- Test-first approach (TDD)
- Run tests after every extraction
- Git commit after each successful extraction (easy rollback)
- Preserve all error messages and Msg.code() values

---

## Critical Files

### Main Implementation
- `hapi-fhir-server/src/main/java/ca/uhn/fhir/rest/server/method/MethodUtil.java` - Refactor target

### Test Files
- `hapi-fhir-server/src/test/java/ca/uhn/fhir/rest/server/method/MethodUtilTest.java` - Create comprehensive tests
- `hapi-fhir-server/src/test/java/ca/uhn/fhir/rest/server/method/SearchMethodBindingTest.java` - Reference for test patterns

### Integration Point
- `hapi-fhir-server/src/main/java/ca/uhn/fhir/rest/server/method/BaseMethodBinding.java:89` - Calls getResourceParameters

---

## Success Metrics

| Metric | Before | After | Target |
|--------|--------|-------|--------|
| Method length | 318 lines | ~60 lines | 81% reduction |
| Test coverage | 0% | 80%+ | Comprehensive |
| Number of methods | 1 god method | 12 focused methods | Better modularity |
| Test suite | 0 tests | ~100 tests | Full coverage |
| All tests pass | N/A | ✓ | No regressions |

---

## Time Estimate

- **Phase 1**: 2-3 days (test creation)
- **Phase 2**: 3-4 days (refactoring)
- **Phase 3**: 1 day (validation)
- **Total**: 6-8 days (~50 hours)
