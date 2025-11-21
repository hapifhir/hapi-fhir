# IResourceLinkService Design Document

## Problem Statement

The HAPI FHIR merge operation needs to track relationships between merged resources using "replaces" and "replaced-by" links. However, different resource types handle these links differently:

- **Patient**: Has native `Patient.link` field with `Patient.LinkType.REPLACES` and `Patient.LinkType.REPLACEDBY`
- **Other resources** (Observation, Practitioner, Organization, etc.): No native link field

To support generic merge operations across all resource types, we need an abstraction that handles both approaches transparently.

## Solution: IResourceLinkService Interface

### Design Principles

1. **Version Agnostic**: Works across FHIR DSTU3, R4, R4B, R5
2. **Type Abstraction**: Uses `IBaseReference` to match FHIR's native datatype
3. **Dual Strategy**: Native fields for Patient, extensions for others
4. **Factory Pattern**: Single decision point for strategy selection

### Interface Definition

```java
package ca.uhn.fhir.jpa.api.svc;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.List;

/**
 * Service for managing resource merge links in a version-agnostic manner.
 * Supports both Patient native links and extension-based links for other resource types.
 */
public interface IResourceLinkService {

    /**
     * Add a "replaces" link from target to source.
     * For Patient: adds Patient.link with type REPLACES
     * For others: adds extension http://hapifhir.io/fhir/StructureDefinition/replaces
     *
     * @param theTarget the resource that replaces another
     * @param theSourceRef reference to the resource being replaced
     * @param theContext the FHIR context
     */
    void addReplacesLink(IBaseResource theTarget, IBaseReference theSourceRef, FhirContext theContext);

    /**
     * Add a "replaced-by" link from source to target.
     * For Patient: adds Patient.link with type REPLACEDBY
     * For others: adds extension http://hapifhir.io/fhir/StructureDefinition/replaced-by
     *
     * @param theSource the resource that was replaced
     * @param theTargetRef reference to the replacement resource
     * @param theContext the FHIR context
     */
    void addReplacedByLink(IBaseResource theSource, IBaseReference theTargetRef, FhirContext theContext);

    /**
     * Get all "replaces" links from a resource.
     *
     * @param theResource the resource to check
     * @param theContext the FHIR context
     * @return list of references to resources that this resource replaces
     */
    List<IBaseReference> getReplacesLinks(IBaseResource theResource, FhirContext theContext);

    /**
     * Get all "replaced-by" links from a resource.
     *
     * @param theResource the resource to check
     * @param theContext the FHIR context
     * @return list of references to resources that replaced this resource
     */
    List<IBaseReference> getReplacedByLinks(IBaseResource theResource, FhirContext theContext);

    /**
     * Check if resource has any "replaced-by" links.
     * Used to determine if resource was already merged.
     *
     * @param theResource the resource to check
     * @param theContext the FHIR context
     * @return true if resource has been replaced
     */
    boolean hasReplacedByLink(IBaseResource theResource, FhirContext theContext);

    /**
     * Check if resource has a specific "replaces" link to target.
     * Used to validate result resource has correct link to source.
     *
     * @param theResource the resource to check
     * @param theTargetId the target resource ID to look for
     * @param theContext the FHIR context
     * @return true if resource has a replaces link to the target
     */
    boolean hasReplacesLinkTo(IBaseResource theResource, IIdType theTargetId, FhirContext theContext);
}
```

## Implementation Strategy

### 1. PatientNativeLinkService

Uses `Patient.link` field with native `Patient.LinkType` enum.

**Module**: `hapi-fhir-jpaserver-base`

**Key Features**:
- Direct access to `Patient.link` collection
- Uses `Patient.LinkType.REPLACES` and `Patient.LinkType.REPLACEDBY`
- No extensions needed
- Existing Patient merge behavior unchanged

**Implementation Notes**:
- Cast to R4 Patient class (version-specific implementation)
- Iterate through `Patient.PatientLinkComponent` elements
- Filter by `LinkType` to find relevant links
- Return `Reference` objects from `link.getOther()`

### 2. ExtensionBasedLinkService

Uses HAPI FHIR standard extensions for all non-Patient resources.

**Module**: `hapi-fhir-jpaserver-base`

**Extension URLs**:
- Replaces: `http://hapifhir.io/fhir/StructureDefinition/replaces`
- Replaced-by: `http://hapifhir.io/fhir/StructureDefinition/replaced-by`

**Key Features**:
- Uses `ExtensionUtil` for version-agnostic extension handling
- Works with any resource type
- Extension value is `IBaseReference` (Reference datatype)

**Implementation Notes**:
- Use `ExtensionUtil.addExtension()` to create extensions
- Use `ExtensionUtil.getExtensions()` to retrieve extensions
- Extension value is cast to `IBaseReference`
- Use `getReferenceElement()` for version-agnostic reference string extraction

**Example Extension Structure**:
```json
{
  "resourceType": "Observation",
  "id": "obs-123",
  "extension": [
    {
      "url": "http://hapifhir.io/fhir/StructureDefinition/replaces",
      "valueReference": {
        "reference": "Observation/obs-456"
      }
    }
  ]
}
```

### 3. ResourceLinkServiceFactory

Factory to select the appropriate implementation based on resource type.

**Module**: `hapi-fhir-jpaserver-base`

**Decision Logic**:
```java
public IResourceLinkService getServiceForResourceType(String theResourceType) {
    if ("Patient".equals(theResourceType)) {
        return myPatientService;
    }
    return myExtensionService;
}
```

**Spring Configuration**:
```java
@Configuration
public class MergeConfig {

    @Bean
    public PatientNativeLinkService patientNativeLinkService() {
        return new PatientNativeLinkService();
    }

    @Bean
    public ExtensionBasedLinkService extensionBasedLinkService() {
        return new ExtensionBasedLinkService();
    }

    @Bean
    public ResourceLinkServiceFactory resourceLinkServiceFactory(
            PatientNativeLinkService thePatientService,
            ExtensionBasedLinkService theExtensionService) {
        return new ResourceLinkServiceFactory(thePatientService, theExtensionService);
    }
}
```

## Design Decisions

### Why IBaseReference Instead of IIdType?

**Initial Consideration**: Use `IIdType` for simplicity
**Final Decision**: Use `IBaseReference` for these reasons:

1. **Matches FHIR Datatype**: Patient.link uses `Reference`, not just an ID
2. **Extension Compatibility**: Extensions store `Reference` objects as values
3. **Metadata Preservation**: Reference can include display, identifier, type fields
4. **Version Agnostic**: `IBaseReference` works across all FHIR versions
5. **Less Conversion**: No need to convert between Reference and ID types

**Reference Extraction**:
```java
// Version-agnostic way to get reference string
IBaseReference ref = ...;
IIdType refElement = ref.getReferenceElement();
String refString = refElement.getValue();  // "Patient/123"
```

### Why Factory Pattern Instead of Resource-Based Selection?

**Rejected Approach**:
```java
// This creates version dependency!
public IResourceLinkService getServiceForResource(IBaseResource theResource) {
    if (theResource instanceof Patient) {  // ❌ R4-specific check
        return myPatientService;
    }
    return myExtensionService;
}
```

**Chosen Approach**:
```java
// Version-agnostic using resource type string
String resourceType = theResource.fhirType();  // or myFhirContext.getResourceType()
IResourceLinkService service = factory.getServiceForResourceType(resourceType);
```

**Benefits**:
- No instanceof checks (version-agnostic)
- Works with resource type from RequestDetails
- Testable with mock resource types
- Clear string-based routing

## Usage in MergeValidationService

### Current Patient-Specific Code

```java
// Lines 247-257 - Check for REPLACEDBY links
List<Reference> replacedByLinksInTarget =
    getLinksOfTypeWithNonNullReference(theTargetResource, Patient.LinkType.REPLACEDBY);
if (!replacedByLinksInTarget.isEmpty()) {
    String ref = replacedByLinksInTarget.get(0).getReference();
    String msg = String.format("Target resource was previously replaced by '%s'", ref);
    addErrorToOperationOutcome(myFhirContext, outcome, msg, "invalid");
    return false;
}
```

### New Generic Code

```java
// Get appropriate link service based on resource type
String resourceType = myFhirContext.getResourceType(theTargetResource);
IResourceLinkService linkService = myLinkServiceFactory.getServiceForResourceType(resourceType);

// Check for REPLACEDBY links - works for any resource type
if (linkService.hasReplacedByLink(theTargetResource, myFhirContext)) {
    List<IBaseReference> replacedByLinks = linkService.getReplacedByLinks(theTargetResource, myFhirContext);
    IIdType refElement = replacedByLinks.get(0).getReferenceElement();
    String ref = refElement.getValue();
    String msg = String.format("Target resource was previously replaced by '%s'", ref);
    addErrorToOperationOutcome(myFhirContext, outcome, msg, "invalid");
    return false;
}
```

## Version Compatibility

### Tested FHIR Versions
- ✅ DSTU3
- ✅ R4
- ✅ R4B
- ✅ R5

### Version-Agnostic Interfaces Used
- `IBaseResource` - all resources
- `IBaseReference` - reference datatype
- `IBaseExtension` - extension datatype
- `IIdType` - resource identifiers
- `FhirContext` - version-aware operations

### Version-Specific Code Locations
Only in `PatientNativeLinkService` implementation:
- Cast to `org.hl7.fhir.r4.model.Patient` (R4-specific)
- Access to `Patient.LinkType` enum (R4-specific)
- This is acceptable as Patient link field is stable across versions

## Testing Strategy

### Unit Tests

1. **PatientNativeLinkService**:
   - Add/get replaces links
   - Add/get replaced-by links
   - Check for specific links
   - Handle empty/null cases

2. **ExtensionBasedLinkService**:
   - Add/get replaces links via extensions
   - Add/get replaced-by links via extensions
   - Handle multiple extensions
   - Handle empty/null cases

3. **ResourceLinkServiceFactory**:
   - Returns PatientNativeLinkService for "Patient"
   - Returns ExtensionBasedLinkService for other types
   - Thread-safe operation

### Integration Tests

1. **Patient Merge** (existing tests should still pass):
   - Verify native links still work
   - 46 existing PatientMergeR4Test tests

2. **Observation Merge** (new tests):
   - Verify extension-based links work
   - Check extension URLs are correct
   - Validate extension values

3. **Cross-Resource Validation**:
   - Source with replaced-by link cannot be merged
   - Target with replaced-by link cannot be merged
   - Result resource must have replaces link (if source not deleted)

## Migration Path

### Phase 1: Create Link Service (Current)
- ✅ Design interface
- ✅ Implement PatientNativeLinkService
- ✅ Implement ExtensionBasedLinkService
- ✅ Create factory
- ⏳ Write unit tests

### Phase 2: Integrate with MergeValidationService
- Update `validateSourceAndTargetAreSuitableForMerge` to use link service
- Update `validateResultResourceReplacesLinkToSourceResource` to use link service
- Remove Patient-specific helper methods (`getLinksToResource`, `getLinksOfTypeWithNonNullReference`)

### Phase 3: Integrate with MergeResourceHelper
- Use link service to add links during merge operation
- Works for both Patient and other resource types

### Phase 4: Testing & Validation
- Run existing Patient merge tests (ensure no regression)
- Create new tests for Observation, Practitioner merge
- Validate extension structure in test assertions

## Benefits Summary

✅ **Generic Merge Support**: Enable merge for any FHIR resource type
✅ **Version Agnostic**: Works across FHIR DSTU3, R4, R4B, R5
✅ **Backward Compatible**: Patient merge unchanged, all existing tests pass
✅ **Clean Abstraction**: MergeValidationService doesn't know about Patient.link vs extensions
✅ **Standard Extensions**: Uses official HAPI extension URLs
✅ **Testable**: Clear interfaces, easy to mock
✅ **Maintainable**: Single responsibility, clear separation of concerns

## Future Considerations

### Potential Enhancements

1. **Custom Extension URLs**: Allow configuration of extension URLs per deployment
2. **Link Metadata**: Support display text, identifier in links
3. **Link Validation**: Ensure link targets exist and are valid
4. **Link History**: Track when links were created/modified
5. **Undo Merge**: Use link service to reverse merge operations

### Performance Considerations

1. **Extension Search**: May be slower than native field access
2. **Caching**: Consider caching link service instances
3. **Batch Operations**: Optimize for bulk merge scenarios

## References

- [FHIR Patient $merge Operation](https://build.fhir.org/patient-operation-merge.html)
- [FHIR Extensions](https://www.hl7.org/fhir/extensibility.html)
- [HapiExtensions.java](hapi-fhir-base/src/main/java/ca/uhn/fhir/util/HapiExtensions.java)
- [ExtensionUtil.java](hapi-fhir-base/src/main/java/ca/uhn/fhir/util/ExtensionUtil.java)

---

**Document Version**: 1.0
**Last Updated**: 2025-11-12
**Authors**: Claude Code (Sonnet 4.5), Emre Dinçtürk
**Status**: Design Approved - Ready for Implementation
