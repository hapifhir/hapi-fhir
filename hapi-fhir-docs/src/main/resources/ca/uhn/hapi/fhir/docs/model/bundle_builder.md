# Bundle Builder

The BundleBuilder ([JavaDoc](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/util/BundleBuilder.html)) can be used to construct FHIR Bundles.

Note that this class is a work in progress! It does not yet support all transaction features. We will add more features over time, and document them here. Pull requests are welcomed.

# Transaction Resource Create

To add an update (aka PUT) operation to a transaction bundle

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/BundleBuilderExamples.java|create}}
``` 

## Conditional Create

If you want to perform a conditional create:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/BundleBuilderExamples.java|createConditional}}
``` 

# Transaction Resource Updates

To add an update (aka PUT) operation to a transaction bundle:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/BundleBuilderExamples.java|update}}
``` 

## Conditional Update

If you want to perform a conditional update:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/BundleBuilderExamples.java|updateConditional}}
``` 

# Customizing bundle

If you want to manipulate a bundle:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/BundleBuilderExamples.java|customizeBundle}}
```

