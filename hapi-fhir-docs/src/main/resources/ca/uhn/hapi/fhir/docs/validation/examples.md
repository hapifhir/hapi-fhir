
# Validation Examples

## Generate a Snapshot profile from a Differential

The following code can be used to generate a Snapshot Profile (StructureDefinition) when all you have is a differential.

```java
// Create a validation chain that includes default validation support and a
// snapshot generator
DefaultProfileValidationSupport defaultSupport = new DefaultProfileValidationSupport();
SnapshotGeneratingValidationSupport snapshotGenerator = new SnapshotGeneratingValidationSupport(myFhirCtx, defaultSupport);
ValidationSupportChain chain = new ValidationSupportChain(defaultSupport, snapshotGenerator);

// Generate the snapshot
StructureDefinition snapshot = chain.generateSnapshot(differential, "http://foo", null, "THE BEST PROFILE");
```
		

