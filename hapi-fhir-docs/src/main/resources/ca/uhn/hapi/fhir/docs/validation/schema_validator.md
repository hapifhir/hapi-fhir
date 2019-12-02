# Schema / Schematron Validator  

FHIR resource definitions are distributed with a set of XML schema files (XSD) as well as a set of XML Schematron (SCH) files. These two sets of files are complimentary to each other, meaning that in order to claim compliance to the FHIR specification, your resources must validate against both sets.

The two sets of files are included with HAPI, and it uses them to perform validation.

# Preparation

In order to use HAPI's Schematron support, a libaray called [Ph-Schematron](https://github.com/phax/ph-schematron) is used, so this library must be added to your classpath (or Maven POM file, Gradle file, etc.)

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

