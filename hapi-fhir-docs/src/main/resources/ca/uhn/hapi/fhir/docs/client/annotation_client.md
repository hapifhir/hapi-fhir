# Annotation Client

HAPI also provides a second style of client, called the *annotation-driven* client. If you are using the 
[Generic (Fluent) Client](./generic_client.html) do not necessarily need to read this page.

The design of the annotation-driven client is intended to be similar to that of JAX-WS, so users of that specification should be comfortable with this one. It uses a user-defined interface containing special annotated methods which HAPI binds to calls against a server.

The annotation-driven client is particularly useful if you have a server that exposes a set of specific operations (search parameter combinations, named queries, etc.) and you want to let developers have a strongly/statically typed interface to that server.

There is no difference in terms of capability between the two styles of client. There is simply a difference in programming style and complexity. It is probably safe to say that the generic client is easier to use and leads to more readable code, at the expense of not giving any visibility into the specific capabilities of the server you are interacting with.

## Defining A Restful Client Interface

The first step in creating an annotation-driven client is to define a restful client interface.

A restful client interface class must extend the [IRestfulClient](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/client/api/IRestfulClient.html) interface, and will contain one or more methods which have been annotated with special annotations indicating which REST operation
that method supports. 

Below is a simple example of a resource provider which supports the [read](http://hl7.org/implement/standards/fhir/http.html#read) operation (i.e. retrieve a single resource by ID) as well as the [search](http://hl7.org/implement/standards/fhir/http.html#search) operation (i.e. find any resources matching a given criteria) for a specific search criteria.

You may notice that this interface looks a lot like the Resource Provider which is defined for use by the RESTful server. In fact, it supports all of the same annotations and is essentially identical, other than the fact that for a client you must use an interface but for a server you must use a concrete class with method implementations.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/IRestfulClient.java|provider}}
```

You will probably want to add more methods to your client interface.

See the [REST Operations](/docs/server_plain/rest_operations.html) page in the server documentation section to see examples of how these methods should look.

## Instantiating the Client

Once your client interface is created, all that is left is to create a FhirContext and instantiate the client and you are ready to start using it.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ExampleRestfulClient.java|client}}
```

# Configuring Encoding (JSON/XML)

Restful client interfaces that you create will also extend the interface [IRestfulClient](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/client/api/IRestfulClient.html), which comes with some helpful methods for configuring the way that the client will interact with the server.

The following snippet shows how to configure the client to explicitly request JSON or XML responses, and how to request "pretty printed" responses on servers that support this (HAPI based servers currently).

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ClientExamples.java|clientConfig}}
```

## A Complete Example

The following is a complete example showing a RESTful client using HAPI FHIR.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/CompleteExampleClient.java|client}}
```

