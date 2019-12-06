# Parsers and Serializers

HAPI FHIR has built-in support for the FHIR [JSON](http://hl7.org/fhir/json.html) and [XML](http://hl7.org/fhir/json.html) encoding formats.

A built in parser can be used to convert HAPI FHIR Java objects into a serialized form, and to parse serialized data into Java objects. Note that unlike some other frameworks, HAPI FHIR does not have separate parsers and serializers. Both of these functions are handled by a single object called the **Parser**.

# Serializing

As with many parts of the HAPI FHIR API, parsing beginis with a [FhirContext](/apidocs/hapi-fhr-base/ca/uhn/fhir/context/FhirContext.html) object. The FhirContext can be used to request an [IParser](/apidocs/hapi-fhir-base/ca/uhn/fhir/parser/IParser.html) for your chosen encodng style that is then used to serialize.   

The following example shows a JSON Parser being used to serialize a FHIR resource. 

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/Parser.java|createParser}}
``` 
   
HapiWorkerContext
