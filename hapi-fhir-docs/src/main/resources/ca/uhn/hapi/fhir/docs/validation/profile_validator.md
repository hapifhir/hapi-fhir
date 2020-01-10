# Profile Validator

# Validator Modules

HAPI provides a built-in and configurable mechanism for validating resources. This mechanism is called the *Resource Validator*.

The resource validator is an extendible and modular system, and you can configure it in a number of ways in order to get the specific type of validation you want to achieve.

The validator can be manually invoked at any time by creating a validator and configuring it with one or more [IValidatorModule](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/validation/IValidatorModule.html) instances.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ValidatorExamples.java|validationIntro}}
```

<div class="doc_info_bubble">
    Note that in earlier releases of HAPI FHIR it was common to register different kinds of validator modules (such as [Schema/Schematron](./schema_validator.html)) because the FHIR Instance Validator module described below was not mature. This is no longer the case, and it is generally recommended to use the FHIR Instance Validator. 
</div>

# FHIR Conformance Packages

There are a few key concepts worth explaining before getting into how validation is performed in HAPI FHIR.

Conformance Resources:

* [StructureDefinition](http://hl7.org/fhir/structuredefinition.html) &ndash; Contains definitions of the valid fields in a given resource, including details about their datatypes, min/max cardinalities, valid values, and other rules about what content is valid and what is not. StructureDefinition resources are also used to express derivative profiles (e.g. a description of a constraint on a FHIR resource for a specfic purpose) as well as to describe extensions. 

* [CodeSystem](http://hl7.org/fhir/codesystem.html) &ndash; Contains definiitions of codes and vocabularies that can be used in FHIR resources, or even outside of FHIR resources.

* [ValueSet](http://hl7.org/fhir/valueset.html) &ndash; Contains lists of codes drawn from one or more CodeSystems that are suitable for use in a specific field in a FHIR resource.  


# FHIR Instance Validator

HAPI has very complete support for validation against FHIR conformance resources. 

This functionality is proviided by the HAPI FHIR "reference validator", which is able
to check a resource for conformance to FHIR profiles.

The FHIR instance validator is very powerful. It will use terminology services to validate codes, StructureDefinitions to validate semantics, and uses a customized XML/JSON parser in order to provide descriptive error messages.

It is always worth considering the performance implications of using the Instance Validator at runtime in a production system. While efforts are made to keep the Instance Validator and its supporting infrastructure performant, the act of performing deep semantic validation is never going to be without some performance cost.    

The FHIR instance validator can be used to validate a resource against the
official structure definitions (produced by HL7) as well as against custom
definitions provided either by HL7 or by the user.

# Running the Validator

<div class="doc_info_bubble">
    <b>Note on FHIR Versions:</b> Many of the classes described on this page have
    multiple versions, and you should use the version of the class the is appropriate
    for the version of FHIR you are looking to validate. For example, the
    examples and links below are using the
    <code>org.hl7.fhir.r4.hapi.validation.FhirInstanceValidator</code> class to
    validate FHIR R4 resources, but you would want to use the class
    <code>org.hl7.fhir.dstu3.hapi.validation.FhirInstanceValidator</code>
    if you need to validate DSTU3 content.
</div>

To execute the validator, you simply create an instance of [FhirInstanceValidator](/hapi-fhir/apidocs/hapi-fhir-validation/org/hl7/fhir/r4/hapi/validation/FhirInstanceValidator.html) and register it to new validator, as shown in the example below.

Note that the example below uses the official FHIR StructureDefintions and ValueSets
to validate the resource. It will not work unless you include the
**hapi-fhir-validation-resources-[version].jar** module/JAR on your classpath.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ValidatorExamples.java|instanceValidator}}
```

# Supplying Your Own Definitions 

The FhirInstanceValidator relies on an implementation of an interface called [IValidationSupport](/hapi-fhir/apidocs/hapi-fhir-structures-r4/org/hl7/fhir/r4/hapi/ctx/IValidationSupport.html) interface to load StructureDefinitions, validate codes, etx.

By default, an implementation of this interface called [DefaultProfileValidationSupport](/hapi-fhir/apidocs/hapi-fhir-structures-r4/org/hl7/fhir/r4/hapi/ctx/DefaultProfileValidationSupport.html) is used. This implementation simply uses the built-in official FHIR definitions to validate against (and in many cases, this is good enough).
 
However, if you have needs beyond simply validating against the core FHIR specification, you may wish to use something more.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ValidatorExamples.java|validateSupplyProfiles}}
```

# Built-In Validation Support Classes

There are a several implementations of the [IValidationSupport](/hapi-fhir/apidocs/hapi-fhir-structures-r4/org/hl7/fhir/r4/hapi/ctx/IValidationSupport.html) interface built into HAPI FHIR that can be used, typically in a chain.

* [**DefaultProfileValidationSupport**](/hapi-fhir/apidocs/hapi-fhir-structures-r4/org/hl7/fhir/r4/hapi/ctx/DefaultProfileValidationSupport.html) - Supplies the built-in FHIR core structure definitions, including both structures and vocabulary.

* [**ValidationSupportChain**](/hapi-fhir/apidocs/hapi-fhir-validation/org/hl7/fhir/r4/hapi/validation/ValidationSupportChain.html) - Can be used to chain multiple implementations together so that for every request, each support class instance in the chain is tried in sequence.

* [**PrePopulatedValidationSupport**](/hapi-fhir/apidocs/hapi-fhir-validation/org/hl7/fhir/r4/hapi/validation/PrePopulatedValidationSupport.html) - Contains a series of HashMaps that store loaded conformance resources in memory. Typically this is initialized at startup in order to add custom conformance resources into the chain.

* [**PrePopulatedValidationSupport**](/hapi-fhir/apidocs/hapi-fhir-validation/org/hl7/fhir/r4/hapi/validation/PrePopulatedValidationSupport.html) - Contains a series of HashMaps that store loaded conformance resources in memory. Typically this is initialized at startup in order to add custom conformance resources into the chain.

* [**CachingValidationSupport**](/hapi-fhir/apidocs/hapi-fhir-validation/org/hl7/fhir/r4/hapi/validation/CachingValidationSupport.html) - Caches results of calls to a wrapped service implementation for a period of time. This class can be a significant help in terms of performance if you are loading conformance resources or performing terminology operations from a database or disk.

* [**SnapshotGeneratingValidationSupport**](/hapi-fhir/apidocs/hapi-fhir-validation/org/hl7/fhir/r4/hapi/validation/SnapshotGeneratingValidationSupport.html) - Generates StructureDefinition snapshots as needed. This should be added to your chain if you are working wiith differential StructueDefinitions that do not include the snapshot view.






 
