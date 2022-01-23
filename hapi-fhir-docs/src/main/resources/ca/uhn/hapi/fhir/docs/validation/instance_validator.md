# Instance Validator

HAPI provides a built-in and configurable mechanism for validating resources using FHIR's own conformance resources (StructureDefinition, ValueSet, CodeSystem, etc.). This mechanism is called the *Instance Validator*.

The resource validator is an extendible and modular system, and you can configure it in a number of ways in order to get the specific type of validation you want to achieve.

The validator can be manually invoked at any time by creating a validator and configuring it with one or more [IValidatorModule](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/validation/IValidatorModule.html) instances.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ValidatorExamples.java|validationIntro}}
```
<div class="doc_info_bubble">
    Note that in earlier releases of HAPI FHIR it was common to register different kinds of validator modules (such as [Schema/Schematron](./schema_validator.html)) because the FHIR Instance Validator module described below was not mature. This is no longer the case, and it is generally recommended to use the FHIR Instance Validator. 
</div>

# FHIR Conformance Concepts

There are a few key concepts worth explaining before getting into how validation is performed in HAPI FHIR.

Conformance Resources:

* [StructureDefinition](http://hl7.org/fhir/structuredefinition.html) &ndash; Contains definitions of the valid fields in a given resource, including details about their datatypes, min/max cardinalities, valid values, and other rules about what content is valid and what is not. StructureDefinition resources are also used to express derivative profiles (e.g. a description of a constraint on a FHIR resource for a specfic purpose) as well as to describe extensions. 

* [CodeSystem](http://hl7.org/fhir/codesystem.html) &ndash; Contains definiitions of codes and vocabularies that can be used in FHIR resources, or even outside of FHIR resources.

* [ValueSet](http://hl7.org/fhir/valueset.html) &ndash; Contains lists of codes drawn from one or more CodeSystems that are suitable for use in a specific field in a FHIR resource.  


# FHIR Instance Validator

<div class="doc_info_bubble">
    <b>Note on HAPI FHIR 5.0.0+:</b> Many of the classes described here have changed in HAPI FHIR 5.0.0 and
    existing users of HAPI FHIR may need to migrate existing validation code in order to successfully use the validator
    in HAPI FHIR 5.0.0 and beyond. See <a href="#migrating-to-5x">Migrating to 5.x</a> for information.
</div>

HAPI has very complete support for validation against FHIR conformance resources.

This functionality is proviided by the HAPI FHIR "reference validator", which is able
to check a resource for conformance to FHIR profiles.

The FHIR instance validator is very powerful. It will use terminology services to validate codes, StructureDefinitions to validate semantics, and uses a customized XML/JSON parser in order to provide descriptive error messages.

It is always worth considering the performance implications of using the Instance Validator at runtime in a production system. While efforts are made to keep the Instance Validator and its supporting infrastructure performant, the act of performing deep semantic validation is never going to be without some performance cost.    

The FHIR instance validator can be used to validate a resource against the
official structure definitions (produced by HL7) as well as against custom
definitions provided either by HL7 or by the user.

# Running the Validator

To execute the validator, you create a [validation support chain](./validation_support_modules.html) and pass this to an instance of [FhirInstanceValidator](/hapi-fhir/apidocs/hapi-fhir-validation/org/hl7/fhir/common/hapi/validation/validator/FhirInstanceValidator.html). The FhirInstanceValidator is then used as a module for the HAPI FHIR validation framework.

Note that the example below uses the official FHIR StructureDefintions and ValueSets
to validate the resource. It will not work unless you include the
**hapi-fhir-validation-resources-[version].jar** module/JAR on your classpath.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ValidatorExamples.java|instanceValidator}}
```

<a name="packages"/>

# Validating Using Packages

HAPI FHIR supports the use of FHIR NPM Packages for supplying validation artifacts.

When using the HAPI FHIR [JPA Server](../server_jpa/) you can simply upload your packages into the JPA Server package registry and the contents will be made available to the validator.

If you are using the validator as a standalone service (i.e. you are invoking it via a Java call) you will need to explicitly make your packages available to the validation support chain.

The following example shows the use of [NpmPackageValidationSupport](./validation_support_modules.html#npmpackagevalidationsupport) to load a package and use it to validate a resource.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ValidatorExamples.java|npm}}
```


<a name="migrating-to-5x"></a>

# Migrating to HAPI FHIR 5.x

HAPI FHIR 5.x contained a significant rewrite of the IValidationSupport interface and the entire validation support module infrastructure.

Users wishing to upgrade an existing application from HAPI FHIR 4.2 or earlier may need to consider the following points (note that the HAPI FHIR JPA server has already been adapted to use the new infrastructure, so most users of the JPA server will not need to make any changes in this regard).

* The `IContextValidationSupport` interface has been renamed to `IValidationSupport`. Previous versions of HAPI had a number of sub-interfaces of IContextValidationSupport that were all named IValidationSupport (but were FHIR version-specific and were located in distinct packages). These previous interfaces named IValidationSupport have all been removed.

* The `IValidationSupport` interface has been reworked significantly in order to simplify extension (these points apply only to users who have created custom implementations of this interface):

   * A method called `getFhirContext()` has been added, meaning that all validation support modules must be able to report their FhirContext object. This is used to ensure that all modules in the chain are consistent with each other in terms of supported FHIR version, etc.

   * All other methods in the interface now have a default implementation which returns a null response. This means that custom implementations of this interface only need to implement the methods they care about.

   * The `validateCode(...)` methods previously passed in a special constant to `theCodeSystem` (the code system URL) parameter in cases where the system URL was implied and not explicit (e.g. when validating the `Patient.gender` field, where the resource body does not contain the code system URL). This constant was confusing to implementors and has been replaced with a new parameter of type [ConceptValidationOptions](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/context/support/ConceptValidationOptions.html) that supplies details about the validation.

* Many classes were previously duplicated across different FHIR versions. For example, there were previously 5 classes named `DefaultProfileValidationSupport` spanning the different versions of FHIR that were supported by the validator. As of HAPI FHIR 5.x, a single [DefaultProfileValidationSupport](/hapi-fhir/apidocs/hapi-fhir-base/undefined/ca/uhn/fhir/context/support/DefaultProfileValidationSupport.html) class exists. Users if this class (and several other implementations of the IValidationSupport interface may need to change their package declarations.

* The DefaultProfileValidationSupport module previously contained code to perform terminology/code validation based on the CodeSystems it contained. This functionality has been relocated to a new module called [(InMemoryTerminologyServerValidationSupport)](/hapi-fhir/apidocs/hapi-fhir-validation/org/hl7/fhir/common/hapi/validation/InMemoryTerminologyServerValidationSupport.html), so this module should generally be added to the chain.




