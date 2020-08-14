# Transaction Builder

The TransactionBuilder ([JavaDoc](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/util/TransactionBuilder.html)) can be used to construct FHIR Transaction Bundles.

Note that this class is a work in progress! It does not yet support all transaction features. We will add more features over time, and document them here. Pull requests are welcomed.

# Resource Updates

To add an update (aka PUT) operation to a transaction bundle

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/TransactionBuilderExamples.java|update}}
``` 

## Conditional Update

If you want to perform a conditional update:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/TransactionBuilderExamples.java|updateConditional}}
``` 

