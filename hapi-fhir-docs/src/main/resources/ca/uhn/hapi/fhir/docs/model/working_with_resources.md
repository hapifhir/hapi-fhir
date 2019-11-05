# Working with Resources

Every resource type defined by FHIR has a corresponding class, which contains a number of getters and setters for the basic properties of that resource.

HAPI tries to make populating objects easier, by providing lots of convenience methods. For example, the Observation resource has an "issued" property which is of the FHIR "instant" type (a system time with either seconds or milliseconds precision). There are methods to use the actual FHIR datatype, but also convenience methods which use built-in Java types.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/FhirDataModel.java|datatypes}}
```

# Navigating Structures

Most HAPI structures provide getters that automatically create child objects on access. This means it is simple to navigate complex structures without needing to worry about instantiating child objects.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/FhirDataModel.java|nonNull}}
```

## Coded/Enumerated Values

There are many places in the FHIR specification where a "coded" string is used. This means that a code must be chosen from a list of allowable values.

### Closed Valuesets / Codes

The FHIR specification defines a number of "closed" ValueSets, such as
the one used for [Patient.gender](http://hl7.org/fhir/valueset-administrative-gender.html). These valuesets must either be empty, or be populated with a value drawn from the list of allowable values defined by FHIR. HAPI provides special typesafe Enums to help in dealing with these fields.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/FhirDataModel.java|codes}}
```

# Convenience Methods

The FHIR data model is rich enough to meet common use cases, but sometimes that richness adds complexity. For example, a Patient may have multiple names (a preferred name, a nickname, etc.) and each of those names may have multiple last names, multiple prefixes, etc.

The example below shows populating a name entry for a Patient. Note the use of the StringDt type, which encapsulates a regular String, but allows for extensions to be added.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/FhirDataModel.java|namesHard}}
```

HAPI also provides for simple setters that use Java primitive types and can be chained, leading to much simpler code.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/FhirDataModel.java|namesEasy}}
```

# Examples

## Populating an Observation Resource

The following example shows how to create an observation resource containing a numeric datatype.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/FhirDataModel.java|observation}}
```
