# Schema / Schematron Validator  

FHIR resource definitions are distributed with a set of XML schema files (XSD) as well as a set of XML Schematron (SCH) files. These two sets of files are complementary to each other, meaning that in order to claim compliance to the FHIR specification, your resources must validate against both sets.

The two sets of files are included with HAPI, and it uses them to perform validation.

<div class="helpWarningCalloutBox">
<i class='fa fa-exclamation-triangle helpWarningCalloutBoxExclamation'></i>
The Schema/Schematron validators were recommended early in the development of FHIR itself, as the official FHIR validation toolchain was still maturing. At this time, the FHIR [Instance Validator](./instance_validator.html) is very mature, and gives far more helpful error messages than the Schema/Schematron validator is able to. For this reason, the Schema/Schematron validators are not available for validating R5+ content and may be deprecated in the future for other versions of FHIR as well.
</div>

# Preparation

In order to use HAPI's Schematron support, a library called [Ph-Schematron](https://github.com/phax/ph-schematron) is used, so this library must be added to your classpath (or Maven POM file, Gradle file, etc.)

Note that this library is specified as an optional dependency by HAPI FHIR so you need to explicitly include it if you want to use this functionality.

# Validating a Resource

To validate a resource instance, a new validator instance is requested from the FHIR Context. This validator is then applied against a specific resource instance, as shown in the example below.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ValidatorExamples.java|basicValidation}}
```

## Validating a Set of Files

The following example shows how to load a set of resources from files on disk and validate each one.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ValidatorExamples.java|validateFiles}}
```

