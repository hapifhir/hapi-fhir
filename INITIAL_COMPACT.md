# Generic Resource Merge - Quick Reference

## Goal
Extend HAPI FHIR's Patient `$merge` operation to support any FHIR resource type (Observation, Practitioner, Organization, etc.) while maintaining backward compatibility.

## Core Problem
Current merge operation is tightly coupled to Patient resource type. Key challenge: Patient has native `link` field for merge tracking, but other resources don't.

## Solution
- **Patient resources**: Continue using native `Patient.link` field
- **Other resources**: Use FHIR extensions (`HapiExtensions.EXTENSION_REPLACES` / `EXTENSION_REPLACED_BY`)
- **Generic infrastructure**: Use Terser for dynamic field access (identifier, active) and type-agnostic operations

## Architecture Layers
1. **REST Provider**: Generic operation in `BaseJpaResourceProvider<T>` (available on ALL resource types)
2. **Service Layer**: `ResourceMergeService` (generic orchestration)
3. **Link Abstraction**: `IResourceLinkService` (handles Patient.link vs extensions)
4. **Helper/Validation**: Generic resource manipulation and validation
5. **Batch2**: Async execution support

## Implementation Phases
1. **Phase 1-2**: Link abstraction layer (Patient.link vs extensions)
2. **Phase 2**: Generic resource helper (identifier/active field handling)
3. **Phase 3**: Generic validation service
4. **Phase 4**: Generic service and REST provider
5. **Phase 5**: Async/Batch2 support
6. **Phase 6**: Undo-merge support
7. **Phase 7**: Documentation

## Key Technical Decisions
- Use `FhirTerser` for dynamic field access (identifier, active)
- Hybrid approach: Patient uses native link, others use extensions
- Single REST operation in `BaseJpaResourceProvider` instead of per-resource providers
- Replace-references operation already generic (no changes needed)
- Template Method pattern for test infrastructure (AbstractMergeTestScenario)

## Test Strategy
- Generic test framework: `AbstractGenericMergeR4Test<T>`
- Resource-specific scenarios: `PractitionerMergeTestScenario`, `ObservationMergeTestScenario`
- Test both resources WITH active field (Practitioner) and WITHOUT (Observation)
- Comprehensive coverage: validation, sync merge, async merge, undo, error cases

## Current Status (2025-11-21)
- ✅ Test infrastructure simplified (removed unnecessary interface layer)
- ✅ 87 generic merge tests passing (42 Practitioner + 45 Observation)
- ✅ AbstractMergeTestScenario uses Template Method pattern
- 🚧 Generic merge implementation in progress

## Key Files Modified
- Service: `ResourceMergeService.java`
- Helper: `MergeResourceHelper.java`
- Validation: `MergeValidationService.java`
- Provider: `BaseJpaResourceProvider.java` (generic merge operation)
- Batch2: `MergeUpdateTaskReducerStep.java`
- Tests: `AbstractGenericMergeR4Test.java`, scenario classes

## Risks & Mitigation
- **Performance**: Terser/reflection overhead acceptable for infrequent merge operations
- **Type safety**: Runtime validation required for `IBaseResource`
- **Compatibility**: Extensions work across FHIR versions (DSTU3, R4, R5)
- **Backward compat**: Patient merge unchanged, existing code unaffected

## Quick Commands
```bash
# Run Practitioner merge tests
mvn test -Dtest=PractitionerMergeR4Test -pl hapi-fhir-jpaserver-test-r4

# Run Observation merge tests
mvn test -Dtest=ObservationMergeR4Test -pl hapi-fhir-jpaserver-test-r4

# Run all generic merge tests
mvn test -Dtest=*MergeR4Test -pl hapi-fhir-jpaserver-test-r4
```

## Reference
See `INITIAL.md` for full implementation plan (1947 lines, detailed architecture, phase breakdowns, code examples).
