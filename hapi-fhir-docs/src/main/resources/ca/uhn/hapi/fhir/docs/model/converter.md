# HL7 FHIR Converter

Beginning in HAPI FHIR 2.3, a new module called `hapi-fhir-converter` has been added to the project. This is an <b>experimental feature</b> so use it with caution!

This feature allows automated conversion from earlier versions of the FHIR structures to a later version.

The following page shows some basic examples. Please get in touch if you are able to contribute better examples!

## Importing the Module

To use the `hapi-fhir-converter` module, import the following dependency into your project pom.xml (or equivalent)

```xml
<dependency>
	<groupId>ca.uhn.hapi.fhir</groupId>
	<artifactId>hapi-fhir-converter</artifactId>
	<version>${project.version}</version>
</dependency>
```

## Converting from DSTU2 to DSTU3

The following example shows a conversion from a	`hapi-fhir-structures-hl7org-dstu2` structure to a `hapi-fhir-structures-dstu3` structure.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ConverterExamples.java|1020}}
``` 

## Converting from DSTU2.1 to DSTU3

The following example shows a conversion from a	`hapi-fhir-structures-dstu2.1` structure to a `hapi-fhir-structures-dstu3` structure.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ConverterExamples.java|1420}}
``` 

