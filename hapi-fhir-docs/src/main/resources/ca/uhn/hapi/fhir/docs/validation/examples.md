
# Validation Examples

## Generate a Snapshot profile from a Differential

The following code can be used to generate a Snapshot Profile (StructureDefinition) when all you have is a differential.

```java
// Create a validation support chain that includes default validation support 
// and a snapshot generator
DefaultProfileValidationSupport defaultSupport = new DefaultProfileValidationSupport();
SnapshotGeneratingValidationSupport snapshotGenerator = new SnapshotGeneratingValidationSupport(myFhirCtx, defaultSupport);
ValidationSupportChain chain = new ValidationSupportChain(defaultSupport, snapshotGenerator);

// Generate the snapshot
StructureDefinition snapshot = chain.generateSnapshot(differential, "http://foo", null, "THE BEST PROFILE");
```

## Validate a Resource with Cross Version Extensions

The following code can be used to validate a resource using FHIR [Cross Version Extensions](http://hl7.org/fhir/versions.html#extensions).

Note that you must have the [hl7.fhir.xver-extensions-0.0.11.tgz](http://fhir.org/packages/hl7.fhir.xver-extensions/0.0.11/package.tgz) package available in your classpath. 

```java
// Create a validation support chain that includes default validation support
// and support from the hl7.fhir.xver-extensions NPM pacakage. 
NpmPackageValidationSupport npmPackageSupport = new NpmPackageValidationSupport(myFhirCtx);
npmPackageSupport.loadPackageFromClasspath("classpath:package/hl7.fhir.xver-extensions-0.0.11.tgz");

myFhirCtx.setValidationSupport(new ValidationSupportChain(
        new DefaultProfileValidationSupport(myFhirCtx),
        npmPackageSupport
   ));
    
FhirInstanceValidator instanceValidator = new FhirInstanceValidator(myFhirCtx);
	
FhirValidator validator = myFhirCtx.newValidator();
validator.registerValidatorModule(instanceValidator);

// Validate theResource
ValidationResult validationResult = validator.validateWithResult(theResource);
```
