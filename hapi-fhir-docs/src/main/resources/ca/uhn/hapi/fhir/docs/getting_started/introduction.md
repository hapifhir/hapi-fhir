
# Introduction to HAPI FHIR

The HAPI FHIR library is an implementation of the [HL7 FHIR specification](http://hl7.org/fhir/) for Java. Explaining what FHIR is would be beyond the scope of this documentation, so if you have not previously worked with FHIR, the specification is a good place to start. This is often not actually the case when discussing messaging protocols, but in this case it is so: The FHIR specification is designed to be readable and implementable, and is filled with good information.

Part of the key to why FHIR is a good specification is the fact that its design is based on the design of other successful APIs (in particular, the FHIR designers often reference the Highrise API as a key influence in the design of the spec.)

HAPI FHIR is based on the same principle, but applied to the Java implementation: We have based the design of this API on the JAXB and JAX-WS APIs, which we consider to be very well thought-out, and very usable APIs. This does <b>not</b> mean that HAPI-FHIR actually uses these two APIs however, or that HAPI-FHIR is in any way compliant with JAXB ([JSR222](https://jcp.org/en/jsr/detail?id=222)) or JAX-WS ([JSR224](https://jcp.org/en/jsr/detail?id=222)), only that we have tried to emulate the easy-to-use, but flexible design of these specifications.

# Getting Started

To get started with HAPI FHIR, first download a copy and add it	to your project. See [Downloading and Importing](./downloading_and_importing.html) for instructions.

## A Note on FHIR Versions

Before discussing HAPI itself, a quick word about FHIR versions. FHIR is not yet a finalized "1.0" standard. It is currently in the DSTU phase, which means that it is changing in subtle and non-subtle ways between releases. Before trying to use FHIR, you will need to determine which version of FHIR you want to support in your application. Typically this would be the latest version, but if you are looking to interact with an application which already exists, you will probably want to implement the same version implemented by that application.

## Introducing the FHIR Context

HAPI defines model classes for every resource type and datatype defined by the FHIR specification. For example, here is the [Patient](/hapi-fhir/apidocs/hapi-fhir-structures-r4/org/hl7/fhir/r4/model/Patient.html) resource specification. If you browse the JavaDoc you will see getters and setters for the various properties that make up a Patient resource.

We will come back to how to interact with these objects in a moment, but first we need to create a [FhirContext](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/context/FhirContext.html). FhirContext is the starting point to using HAPI, and acts as a factory for most other parts of the API as well as a runtime cache of information that HAPI needs to operate. Users of the JAXB API may find this class to be similar in purpose to	the	[JAXBContext](http://docs.oracle.com/javaee/5/api/javax/xml/bind/JAXBContext.html) class from that API.

Creating a FhirContext is as simple as instantiating one. A FhirContext instance is	specific to a given version of the FHIR specification, so it is recommended that you use one of the factory methods indicating the FHIR version you wish to support in your application, as shown in the following snippet:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/FhirContextIntro.java|creatingContext}}
```

## Parsing a resource from a String

This [Parser instance](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/parser/IParser.html) can then be used to parse messages. Note that you may use the context to create as many parsers are you want.

**Performance tip:** The FhirContext is an expensive object to create, so you should try to create it once and keep it around during the life of your application. Parsers, on the other hand, are very lightweight and do not need to be reused.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/FhirContextIntro.java|parseMsg}}
``` 

## Encoding a Resource to a String

The parser can also be used to encode a resource (which you can populate with your own values) just as easily.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/FhirContextIntro.java|encodeMsg}}
``` 

<!--/* ****** The section below on fluent references the snippet above ***** */-->
<!--/* ****** so be careful about any reordering!                      ***** */-->

This code gives the following output:

```xml
<Patient xmlns="http://hl7.org/fhir">
   <identifier>
      <system value="http://example.com/fictitious-mrns"/>
      <value value="MRN001"/>
   </identifier>
   <name>
      <use value="official"/>
      <family value="Tester"/>
      <given value="John"/>
      <given value="Q"/>
   </name>
</Patient>
```

## Fluent Programming

Much of the HAPI FHIR API is designed using a fluent style, where method calls can be chained in a natural way. This leads to tighter and easier-to-read code.


The following snippet is functionally identical to the example above:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/FhirContextIntro.java|encodeMsgFluent}}
```

# JSON Encoding

JSON parsing/encoding is also supported.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/FhirContextIntro.java|encodeMsgJson}}
```

This code gives the following output:

```json
{
    "resourceType":"Patient",
    "identifier":[
        {
            "system":"http://example.com/fictitious-mrns",
            "value":"MRN001"
        }
    ],
    "name":[
        {
            "use":"official",
            "family":[
                "Tester"
            ],
            "given":[
                "John",
                "Q"
            ]
        }
    ]
}
```
