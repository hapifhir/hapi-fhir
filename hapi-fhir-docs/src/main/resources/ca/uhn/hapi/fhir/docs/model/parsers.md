# Parsers and Serializers

HAPI FHIR has built-in support for the FHIR [JSON](http://hl7.org/fhir/json.html) and [XML](http://hl7.org/fhir/json.html) encoding formats.

A built in parser can be used to convert HAPI FHIR Java objects into a serialized form, and to parse serialized data into Java objects. Note that unlike some other frameworks, HAPI FHIR does not have separate parsers and serializers. Both of these functions are handled by a single object called the **Parser**.

# Parsing (aka Deserializing)

As with many parts of the HAPI FHIR API, parsing begins with a [FhirContext](/apidocs/hapi-fhir-base/ca/uhn/fhir/context/FhirContext.html) object. The FhirContext can be used to request an [IParser](/apidocs/hapi-fhir-base/ca/uhn/fhir/parser/IParser.html) for your chosen encoding style that is then used to parse.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/Parser.java|parsing}}
``` 
   
# Encoding (aka Serializing)

As with many parts of the HAPI FHIR API, parsing begins with a [FhirContext](/apidocs/hapi-fhir-base/ca/uhn/fhir/context/FhirContext.html) object. The FhirContext can be used to request an [IParser](/apidocs/hapi-fhir-base/ca/uhn/fhir/parser/IParser.html) for your chosen encoding style that is then used to serialize.   

The following example shows a JSON Parser being used to serialize a FHIR resource. 

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/Parser.java|encoding}}
``` 
   
## Pretty Printing

By default, the parser will output in condensed form, with no newlines or indenting. This is good for machine-to-machine communication since it reduces the amount of data to be transferred but it is harder to read. To enable pretty printed output:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/Parser.java|encodingPretty}}
``` 

## Encoding Configuration

There are plenty of other options too that can be used to control the output by the parser. A few examples are shown below. See the [IParser](/apidocs/hapi-fhir-base/ca/uhn/fhir/parser/IParser.html) JavaDoc for more information.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/Parser.java|encodingConfig}}
``` 

 
