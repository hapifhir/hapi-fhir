# Parser Error Handler

Parser Error Handler validation is enabled by calling [IParser#setParserErrorHandler(IParserErrorHandler)](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/parser/IParser.html#setParserErrorHandler(ca.uhn.fhir.parser.IParserErrorHandler)) on either the FhirContext or on individual parser instances. This method takes an [IParserErrorHandler](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/parser/IParserErrorHandler.html), which is a callback that will be invoked any time a parse issue is detected.

There are two implementations of IParserErrorHandler that come built into HAPI FHIR. You can also supply your own implementation if you want.

* [**LenientErrorHandler**](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/parser/LenientErrorHandler.html) logs any errors but does not abort parsing. By default this handler is used, and it logs errors at "warning" level. It can also be configured to silently ignore issues. LenientErrorHandler is the default.

* [**StrictErrorHandler**](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/parser/StrictErrorHandler.html) throws a [DataFormatException](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/parser/DataFormatException.html) if any errors are detected.

The following example shows how to configure a parser to use strict validation.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ValidatorExamples.java|parserValidation}}
```

You can also configure the error handler at the FhirContext level, which is useful for clients.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ValidatorExamples.java|clientValidation}}
```

FhirContext level validators can also be useful on servers.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ValidatorExamples.java|serverValidation}}
```

