# Generic (Fluent) Client

Creating a generic client simply requires you to create an instance of `FhirContext` and use that to instantiate a client.

The following example shows how to create a client, and a few operations which can be performed.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|simple}}
``` 

<p class="doc_info_bubble">
    <b>Performance Tip:</b> Note that FhirContext is an expensive object to create, so you should try to keep an instance around for the lifetime of your application. It is thread-safe so it can be passed as needed. Client instances, on the other hand, are very inexpensive to create so you can create a new one for each request if needed (although there is no requirement to do so, clients are reusable and thread-safe as well).
</p>

# Fluent Calls

The generic client supports queries using a fluent interface which is originally inspired by the excellent [.NET FHIR API](http://firely-team.github.io/fhir-net-api/client-search.html).

The fluent interface allows you to construct powerful queries by chaining method calls together, leading to highly readable code. It also allows you to take advantage of intellisense/code completion in your favourite IDE.

Note that most fluent operations end with an `execute()` statement which actually performs the invocation. You may also invoke several configuration operations just prior to the execute() statement, such as `encodedJson()` or `encodedXml()`.

# Search

Searching is a very powerful part of the FHIR API specification itself, and HAPI FHIR aims to provide a complete implementation of the FHIR API search specification via the generic client API.

## Search - By Type

Searching for resources is probably the most common initial scenario for client applications, so we'll start the demonstration there. The FHIR search operation generally uses a URL with a set of predefined search parameters, and returns a Bundle containing zero-or-more resources which matched the given search criteria.

Search is a very powerful mechanism, with advanced features such as paging, including linked resources, etc. See the FHIR [search specification](http://hl7.org/fhir/search.html) for more information.

The following example shows how to query using the generic client:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|search}}
``` 

## Search - Multi-valued Parameters (ANY/OR)

To search for a set of possible values where *ANY* should be matched, you can provide multiple values to a parameter, as shown in the example below.

This leads to a URL resembling `http://base/Patient?family=Smith,Smyth`.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|searchOr}}
``` 

## Search - Multi-valued Parameters (ALL/AND)

To search for a set of possible values where *ALL* should be matched, you can provide multiple instances of a parameter, as shown in the example below.

This leads to a URL resembling `http://base/Patient?address=Toronto&address=Ontario&address=Canada`.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|searchAnd}}
``` 

## Search - Paging

If the server supports paging results, the client has a page method which can be used to load subsequent pages.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|searchPaging}}
``` 

## Search - Composite Parameters

If a composite parameter is being searched on, the parameter takes a "left" and "right" operand, each of which is a parameter from the resource being searched. The following example shows the syntax.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|searchComposite}}
``` 

## Search - By plain URL

You can also perform a search using a String URL, instead of using the fluent method calls to build the URL.

This can be useful if you have a URL you retrieved from somewhere else that you want to use as a search. This can also be useful if you need to make a search that isn't presently supported by one of the existing fluent methods (e.g. reverse chaining with `_has`).

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|searchUrl}}
``` 

## Search - Other Query Options

The fluent search also has methods for sorting, limiting, specifying JSON encoding, _include, _revinclude, _lastUpdated, _tag, etc.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|searchAdv}}
``` 

## Search - Using HTTP POST

The FHIR specification allows the use of an HTTP POST to transmit a search to a server instead of using
an HTTP GET. With this style of search, the search parameters are included in the request body instead
of the request URL, which can be useful if you need to transmit a search with a large number of parameters.

The [`usingStyle(SearchStyleEnum)`](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/gclient/IQuery.html#usingStyle(ca.uhn.fhir.rest.api.SearchStyleEnum)) method controls which style to use. By default, GET style is used unless the client detects that the request would result in a very long URL (over 8000 chars) in which case the client automatically switches to POST.

If you wish to force the use of HTTP POST, you can do that as well.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|searchPost}}
``` 

## Search - Compartments

To search a [resource compartment](http://www.hl7.org/implement/standards/fhir/extras.html#compartment), simply use the [`withIdAndCompartment()`](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/gclient/IQuery.html#withIdAndCompartment(java.lang.String,java.lang.String)) method in your search.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|searchCompartment}}
```

## Search - Subsetting (_summary and _elements)

Sometimes you may want to only ask the server to include some parts of returned resources (instead of the whole resource). Typically this is for performance or optimization reasons, but there may also be privacy reasons for doing this.

To request that the server return only "summary" elements (those elements defined in the specification with the "Σ" flag), you can use the [`summaryMode(SummaryEnum)`](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/gclient/IClientExecutable.html#summaryMode(ca.uhn.fhir.rest.api.SummaryEnum)) qualifier:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|searchSubsetSummary}}
```

To request that the server return only elements from a custom list provided by the client, you can use the [`elementsSubset(String...)`](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/gclient/IClientExecutable.html#elementsSubset(java.lang.String...)) qualifier:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|searchSubsetElements}}
```

# Create - Type

The following example shows how to perform a create operation using the generic client:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|create}}
```

## Conditional Creates

FHIR also specifies a type of update called "conditional create", where a set of search parameters are provided and a new resource is only created if no existing resource matches those parameters. See the FHIR specification for more information on conditional creation.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|createConditional}}
```

# Read/VRead - Instance

Given a resource name and ID, it is simple to retrieve the latest version of that resource (a 'read').

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|read}}
```

By adding a version string, it is also possible to retrieve a specific version (a 'vread').


```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|vread}}
```

It is also possible to retrieve a resource given its absolute URL (this will override the base URL set on the client).

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|readabsolute}}
```

**See Also:** See the description of [Read/VRead ETags](#read_etags) below for information on specifying a matching version in the client request.

# Delete - Instance

The following example shows how to perform a delete operation using the generic client:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|delete}}
```

## Conditional Deletes

Conditional deletions are also possible, which is a form where instead of deleting a resource using its logical ID, you specify a set of search criteria and a single resource is deleted if it matches that criteria. Note that this is not a mechanism for bulk deletion; see the FHIR specification for information on conditional deletes and how they are used.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|deleteConditional}}
```

## Cascading Delete

The following snippet shows now to request a cascading delete. Note that this is a HAPI FHIR specific feature and is not supported on all servers.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|deleteCascade}}
```

# Update - Instance

Updating a resource is similar to creating one, except that an ID must be supplied since you are updating a previously existing resource instance.

The following example shows how to perform an update operation using the generic client:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|update}}
```

## Conditional Updates

FHIR also specifies a type of update called "conditional updates", where instead of using the logical ID of a resource to update, a set of search parameters is provided. If a single resource matches that set of parameters, that resource is updated. See the FHIR specification for information on how conditional updates work.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|updateConditional}}
```

**See Also:** See the description of [Update ETags](#update_etags) below for information on specifying a matching version in the client request.

# Patch - Instance

The PATCH operation can be used to modify a resource in place by supplying a delta

The following example shows how to perform a patch using a [FHIR Patch](http://hl7.org/fhir/fhirpatch.html)

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|patchFhir}}
```

The following example shows how to perform a patch using a [JSON Patch](https://tools.ietf.org/html/rfc6902.)

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|patchJson}}
```

# History - Server/Type/Instance

To retrieve the version history of all resources, or all resources of a given type, or of a specific instance of a resource, you call the [`history()`](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/client/api/IGenericClient.html#history()) method.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|historyDstu2}}
```

You can also optionally request that only resource versions later than a given date, and/or only up to a given count (number) of resource versions be returned.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|historyFeatures}}
```

# Transaction - Server

The following example shows how to execute a transaction using the generic client:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|transaction}}
```

# Capability Statement (metadata) - Server

To retrieve the server's capability statement, simply call the [`capabilities()`](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/client/api/IGenericClient.html#capabilities()) method as shown below.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|conformance}}
```

# Extended Operations

FHIR also supports a set of *extended operations*, which are operations beyond the basic CRUD operations defined in the specification. These operations are an RPC style of invocation, with a set of named input parameters passed to the server and a set of named output parameters returned back.

To invoke an operation using the client, you simply need to create the input [Parameters](/hapi-fhir/apidocs/hapi-fhir-structures-r4/org/hl7/fhir/r4/model/Parameters.html) resource, then pass that to the [`operation()`](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/client/api/IGenericClient.html#operation()) fluent method.

The example below shows a simple operation call.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|operation}}
```

Note that if the operation does not require any input parameters, you may also invoke the operation using the following form. Note that the `withNoParameters` form still requires you to provide the type of the Parameters resource so that it can return the correct type in the response.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|operationNoIn}}
```

## Using the HTTP GET Form

By default, the client will invoke operations using the HTTP POST form. The FHIR specification also allows requests to use the HTTP GET verb if the operation does not affect state and has no composite/resource parameters. Use the following form to invoke operation with HTTP GET.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|operationHttpGet}}
```

## Built-In Operations - Validate

The `$validate` operation asks the server to test a given resource to see if it would be acceptable as a create/update on that server. The client has built-in support for this operation.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|validate}}
```

# Built-In Operations - Process-Message

The `$process-message` operation asks the server to accept a FHIR message bundle for processing.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ClientExamples.java|processMessage}}
```

# Additional Properties

This section contains ways of customizing the request sent by the client.

## Cache-Control

The `Cache-Control` header can be used by the client in a request to signal to the server (or any cache in front of it) that the client wants specific behaviour from the cache, or wants the cache to not act on the request altogether. Naturally, not all servers will honour this header.

To add a cache control directive in a request:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ClientExamples.java|cacheControl}}
```

<a name="etags"/>

# ETags

ETag features are added simply by adding fluent method calls to the client method chain, as shown in the following examples.

<a name="read_etags"/>

## Read / VRead ETags

To notify the server that it should return an `HTTP 304 Not Modified` if the content has not changed, add an [`ifVersionMatches()`](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/gclient/IReadExecutable.html#ifVersionMatches(java.lang.String)) invocation.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|etagread}}
```

This method will add the following header to the request: 

```http
If-None-Match: "W/001"
```

<a name="update_etags"/>

## Update ETags

To implement version aware updates, specify a version in the request. This will notify the server that it should only update the resource on the server if the version matches the given version. This is useful to prevent two clients from attempting to modify the resource at the same time, and having one client's updates overwrite the other's.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|etagupdate}}
```

The following header will be added to the request as a part of this	interaction:

```http
If-Match: "W/001"
```
