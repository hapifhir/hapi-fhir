# REST Operations: Extended Operations

The FHIR specification defines a special kind of operations that have an RPC-like functionality. These are called "Execute Operations", or simply "Operations" throughout the FHIR specification.

A good introduction to this capability can be found on the [Operations Page](http://hl7.org/fhir/operations.html) of the FHIR Specification.

FHIR extended operations are a special type of RPC-style invocation you can perform against a FHIR server, type, or resource instance. These invocations are named using the convention `$name` (i.e. the name is prefixed with $) and will generally take a [Parameters](/hapi-fhir/apidocs/hapi-fhir-structures-r4/org/hl7/fhir/r4/model/Parameters.html) resource as input and output. There are some cases where the input and/or output will be a different resource type however.

## Providers

To define an operation, a method should be placed in a [Resource Provider class](./resource_providers.html#resource-providers) if the operation works against a resource type/instance (e.g. `Patient/$everything`), or on a [Plain Provider class](./resource_providers.html#plain-providers) if the operation works against the server (i.e. it is global and not resource specific).

# Type-Level Operations

To implement a type-specific operation, the method should be annotated with the [@Operation](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/Operation.html) tag, and should have an [@OperationParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/OperationParam.html) tag for each named parameter that the input Parameters resource may be populated with. The following example shows how to implement the [`Patient/$everything`](http://hl7.org/fhir/operation-patient-everything.html) method, defined in the FHIR specification.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ServerOperations.java|patientTypeOperation}}
``` 

Example URL to invoke this operation:

```url
http://fhir.example.com/Patient/$everything
```

# Instance-Level Operations

To create an instance-specific operation (an operation which takes the ID of a specific resource instance as a part of its request URL), you can add a parameter annotated with the [@IdParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/IdParam.html) annotation, of type [IdType](/hapi-fhir/apidocs/hapi-fhir-structures-r4/org/hl7/fhir/r4/model/IdType.html). The following example shows how to implement the `Patient/[id]/$everything` operation.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ServerOperations.java|patientInstanceOperation}}
``` 

Example URL to invoke this operation:

```url
http://fhir.example.com/Patient/123/$everything
```

# Server-Level Operations

Server-level operations do not operate on a specific resource type or instance, but rather operate globally on the server itself. The following example shows how to implement a server-level operation. Note that the `concept` parameter in the example has a cardinality of `0..*`, so a ``List<Coding>` is used as the parameter type.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ServerOperations.java|serverOperation}}
``` 

Example URL to invoke this operation (HTTP request body is Parameters resource):

```url
http://fhir.example.com/$closure
```

# Using Search Parameter Types

FHIR allows operation parameters to be of a [Search parameter type](http://hl7.org/fhir/search.html#ptypes) (e.g. token) instead of a FHIR datatype (e.g. Coding).

To use a search parameter type, any of the search parameter types listed in [Rest Operations: Search](./rest_operations_search.html) may be used. For example, the following is a simple operation method declaration using search parameters:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ServerOperations.java|searchParamBasic}}
``` 

Example URL to invoke this operation (HTTP request body is Parameters resource):

```url
http://fhir.example.com/$find-matches?date=2011-01-02&code=http://system%7Cvalue
```

It is also fine to use collection types for search parameter types if you want to be able to accept multiple values. For example, a [`List<TokenParam>`](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/param/TokenParam.html) could be used if you want to allow multiple repetitions of a given token parameter (this is analogous to the "AND" semantics in a search).

A [`TokenOrListParam`](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/param/TokenOrListParam.html) could be used if you want to allow multiple values within a single repetition, separated by comma (this is analogous to "OR" semantics in a search).

For example:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ServerOperations.java|searchParamAdvanced}}
``` 

# Returning Multiple OUT Parameters

In all of the Operation examples above, the return type specified for the operation is a single Resource instance. This is a common pattern in FHIR defined operations. However, it is also possible for an extended operation to be defined with multiple and/or repeating OUT parameters. In this case, you can return a [Parameters](/hapi-fhir/apidocs/hapi-fhir-structures-r4/org/hl7/fhir/r4/model/Parameters.html) resource directly.

# Accepting HTTP GET

The FHIR specification allows for operations to be invoked using an HTTP GET instead of an HTTP POST **only** if the following two conditions are met:

* All parameters have primitive datatype values

* The operation is marked as "affectsState = false". Note that early releases of the FHIR specification referred to an operation that did not affect state as "idempotent = true". It was subsequently determined that *idempotency* was the wrong term for the concept being expressed, but the term does persist in some HAPI FHIR documentation and code.

If you are implementing an operation which should allow HTTP GET, you should mark your operation with
`idempotent=true` in the [@Operation](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/Operation.html). The default value for this flag is `false`, meaning that operations will not support <code>HTTP GET</code> by default.

Note that the HTTP GET form is only supported if the operation has only primitive parameters (no complex parameters or resource parameters). If a client makes a request containing a complex parameter, the server will respond with an <code>HTTP 405 Method Not Supported</code>.

# Manually handing Request/Response

For some operations you may wish to bypass the HAPI FHIR standard request parsing and/or response generation. In this case you may use the `manualRequest = true` and/or `manualResponse = true` attributes on the [@Operation](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/Operation.html) annotation.

The following example shows an operation that parses the request and generates a response (by echoing back the request).

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ServerOperations.java|manualInputAndOutput}}
``` 
