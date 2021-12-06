# REST Operations: Overview

This page shows the operations which can be implemented on HAPI [Plain Server](/docs/server_plain/introduction.html), as well as on the [Annotation Client](/docs/client/annotation_client.html). Most of the examples shown here show how to implement a server method, but to perform an equivalent call on an annotation client you simply put a method with the same signature in your client interface.

<a name="instance_read" />

# Instance Level - Read

The	[read](http://hl7.org/fhir/http.html#read) operation retrieves a resource by ID. It is annotated with the [@Read](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/Read.html) annotation, and has at least a single parameter annotated with the [@IdParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/IdParam.html) annotation.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|read}}
``` 

Example URL to invoke this method:

```url
http://fhir.example.com/Patient/111
```

The following snippet shows how to define a client interface to handle a read method.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|clientReadInterface}}
``` 

<a name="instance_vread" />

# Instance Level - VRead

The	**[vread](http://hl7.org/implement/standards/fhir/http.html#vread)** operation retrieves a specific version of a resource with a given ID. To support vread, simply add "version=true" to your [@Read](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/Read.html) annotation. This means that the read method will support both "Read" and "VRead". The IdType instance passed into your method may or may not have the version populated depending on the client's request.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|vread}}
``` 

Example URL to invoke this method:

```url
http://fhir.example.com/Patient/111/_history/2
```

<a name="instance_update" />

# Instance Level - Update

The **[update](http://hl7.org/implement/standards/fhir/http.html#update)** operation updates a specific resource instance (using its ID), and optionally accepts a version ID as well (which can be used to detect version conflicts).

Update methods must be annotated with the [@Update](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/Update.html) annotation, and have a parameter annotated with the [@ResourceParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/ResourceParam.html) annotation. This parameter contains the resource instance to be created. See the [@ResourceParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/ResourceParam.html) for information on the types allowed for this parameter (resource types, String, byte[]).

In addition, the method may optionally have a parameter annotated with the [@IdParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/IdParam.html) annotation, or they may obtain the ID of the resource being updated from the resource itself. Either way, this ID comes from the URL passed in.

Update methods must return an object of type [MethodOutcome](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/api/MethodOutcome.html). This object contains the identity of the created resource.

The following snippet shows how to define an update method on a server:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|update}}
``` 

Example URL to invoke this method (this would be invoked using an HTTP PUT,	with the resource in the PUT body):

```url
http://fhir.example.com/Patient
```

The following snippet shows how the corresponding client interface would look:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|updateClient}}
``` 

## Conditional Updates

If you wish to support conditional updates, you can add a parameter tagged with a [@ConditionalUrlParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/ConditionalUrlParam.html) annotation. If the request URL contains search parameters instead of a resource ID, then this parameter will be populated.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|updateConditional}}
``` 

Example URL to invoke this method (this would be invoked using an HTTP PUT,	with the resource in the PUT body):

```url
http://fhir.example.com/Patient?identifier=system%7C00001
```

<a name="raw_update_access"/>

## Accessing The Raw Resource Payload

If you wish to have access to the raw resource payload as well as the parsed value for any reason, you may also add parameters which have been annotated with the [@ResourceParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/ResourceParam.html) of type `String` (to access the raw resource body) and/or `EncodingEnum` (to determine which encoding was used).

The following example shows how to use these additional data elements.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|updateRaw}}
``` 

<a name="prefer"/>

## Prefer Header / Returning the resource body

If you want to allow clients to request that the server return the resource body as a result of the transaction, you may wish to return the updated resource in the returned MethodOutcome.

In this type of request, the client adds a header containing `Prefer: return=representation` which indicates to the server that the client would like the resource returned in the response.

 In order for the server to be able to honour this request, the	server method should add the updated resource to the MethodOutcome object being returned, as shown in the example below.
 
```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|updatePrefer}}
``` 

## Contention Aware Updating

As of FHIR DSTU2, FHIR uses the `ETag` header to provide *contention aware updating*. Under this scheme, a client may create a request that contains an ETag specifying the version, and the server will fail if the given version is not the latest version.

Such a request is shown below. In the following example, the update will only be applied if resource "Patient/123" is currently at version "3". Otherwise, it will fail with an `HTTP 409 Conflict` error.

```http
PUT [serverBase]/Patient/123
If-Match: W/"3"
Content-Type: application/fhir+json

{ ..resource body.. }
```

If a client performs a contention aware update, the ETag version will be placed in the version part of the IdDt/IdType that is passed into the method. For example:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|updateEtag}}
``` 

<a name="instance_delete" />

# Instance Level - Delete

The [delete](http://hl7.org/implement/standards/fhir/http.html#delete) operation retrieves a specific version of a resource with a given ID. It takes a single ID parameter annotated with an [@IdParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/IdParam.html) annotation, which supplies the ID of the resource to delete.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|delete}}
``` 

Delete methods are allowed to return the following types:

* **void**: This method may return `void`, in which case the server will return an empty response and the client will ignore any successful response from the server (failure responses will still throw an exception)
* **[MethodOutcome](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/api/MethodOutcome.html)**: This method may return a `MethodOutcome`, which is a wrapper for the FHIR OperationOutcome resource, which may optionally be returned by the server according to the FHIR specification.

Example URL to invoke this method (HTTP DELETE):

```url
http://fhir.example.com/Patient/111
```

## Conditional Deletes

The FHIR specification also allows "conditional deletes". A conditional delete uses a search style URL instead of a read style URL, and deletes a single resource if it matches the given search parameters. The following example shows how to invoke a conditional delete.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|deleteConditional}}
``` 
Example URL to perform a conditional delete (HTTP DELETE):

```url
http://fhir.example.com/Patient?identifier=system%7C0001
```

# Instance Level - Patch

HAPI FHIR includes basic support for the [patch](http://hl7.org/implement/standards/fhir/http.html#patch) operation. This support allows you to perform patches, but does not include logic to actually implement resource patching in the server framework (note that the JPA server does include a patch implementation).

The following snippet shows how to define a patch method on a server:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/PatchExamples.java|patch}}
```


<a name="type_create" />

# Type Level - Create

The [create](http://hl7.org/implement/standards/fhir/http.html#create) operation saves a new resource to the server, allowing the server to give that resource an ID and version ID.

Create methods must be annotated with the [@Create](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/Create.html) annotation, and have a single parameter annotated with the [@ResourceParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/ResourceParam.html) annotation. This parameter contains the resource instance to be created. See the [@ResourceParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/ResourceParam.html) for information on the types allowed for this parameter (resource types, String, byte[]).

Create methods must return an object of type [MethodOutcome](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/api/MethodOutcome.html). This object contains the identity of the created resource.

The following snippet shows how to define a server create method:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|create}}
``` 

Example URL to invoke this method (this would be invoked using an HTTP POST, with the resource in the POST body):

```url
http://fhir.example.com/Patient
```

The following snippet shows how the corresponding client interface would look:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|createClient}}
``` 

## Conditional Creates

The FHIR specification also allows "conditional creates". A conditional create has an additional header called `If-None-Exist` which the client will supply on the HTTP request. The client will populate this header with a search URL such as `Patient?identifier=foo`. See the FHIR specification for details on the semantics for correctly implementing conditional create.

When a conditional create is detected (i.e. when the create request contains a populated `If-None-Exist` header), if a method parameter annotated with the
[@ConditionalUrlParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/ConditionalUrlParam.html) is detected, it will be populated with the value of this header.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|createConditional}}
``` 

Example HTTP transaction to perform a conditional create:

```http
POST http://fhir.example.com/Patient
If-None-Exist: Patient?identifier=system%7C0001
Content-Type: application/fhir+json

{ ...resource body... }
```

## Prefer Header / Returning the resource body

If you wish to allow your server to honour the `Prefer` header, the same mechanism shown above for [Prefer Header for Updates](#prefer) should be used.

## Accessing The Raw Resource Payload

The create operation also supports access to the raw payload, using the same semantics as raw payload access [for the update operation](#raw_update_access).

<a name="type_search" />

# Type Level - Search

The [search](http://hl7.org/implement/standards/fhir/http.html#search) operation returns a bundle with zero-to-many resources of a given type, matching a given set of parameters.

Searching is a very powerful and potentially very complicated operation to implement, with many possible parameters and combinations of parameters. See [REST Operations: Search](./rest_operations_search.html) for details on how to create search methods.


<a name="type_validate" />

# Type Level - Validate

The [validate](http://hl7.org/implement/standards/fhir/http.html#validate) operation tests whether a resource passes business validation, and would be acceptable for saving to a server (e.g. by a create or update method).

Validate methods must be annotated with the [@Validate](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/Validate.html) annotation, and have a parameter annotated with the [@ResourceParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/ResourceParam.html) annotation. This parameter contains the resource instance to be created.

Validate methods may optionally also have a parameter of type IdType annotated with the [@IdParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/IdParam.html) annotation. This parameter contains the resource ID (see the [FHIR specification](http://hl7.org/implement/standards/fhir/http.html#validation) for details on how this is used).

Validate methods must return normally if the resource validates successfully, or throw an [UnprocessableEntityException](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/server/exceptions/UnprocessableEntityException.html) or [InvalidRequestException](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/server/exceptions/InvalidRequestException.html) if the validation fails.

Validate methods must return either:

* **void** &ndash; The method should throw an exception for a validation failure, or return normally.

* An object of type [MethodOutcome](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/api/MethodOutcome.html). The MethodOutcome may optionally be populated with an OperationOutcome resource, which will be returned to the client if it exists.

The following snippet shows how to define a server validate method:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|validate}}
``` 
In the example above, only the [@ResourceParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/ResourceParam.html) parameter is technically required, but you may also add the following parameters:

* **[@Validate.Mode](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/Validate.Mode.html) ValidationModeEnum theMode** - This is the validation mode (see the FHIR specification for information on this)

* **[@Validate.Profile](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/Validate.Profile.html) String profile** - This is the profile to validate against (see the FHIR specification for more information on this)

Example URL to invoke this method (this would be invoked using an HTTP POST, with a Parameters resource in the POST body):

```url
http://fhir.example.com/Patient/$validate
```

<a name="system_capabilities" />

# System Level - Capabilities

FHIR defines that a FHIR Server must be able to export a Capability Statement (formerly called a Conformance Statement), which is an instance of the [CapabilityStatement](http://hl7.org/implement/standards/fhir/CapabilityStatement.html) resource describing the server itself.

The HAPI FHIR RESTful server will automatically export such a capability statement. See the [Server Capability Statement](./introduction.html#capabilities) documentation for more information.

If you wish to override this default behaviour by creating your own capability statement provider, you simply need to define a class with a method annotated using the [@Metadata](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/Metadata.html) annotation.

An example provider is shown below.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|metadataProvider}}
``` 

To create a Client which can retrieve a Server's conformance statement is simple. First, define your Client Interface, using the [@Metadata](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/Metadata.html) annotation:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|metadataClient}}
``` 

You can then use the standard [Annotation Client](/docs/client/annotation_client.html) mechanism for instantiating a client:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|metadataClientUsage}}
``` 

<a name="system_transaction" />

# System Level - Transaction

The [transaction](http://hl7.org/implement/standards/fhir/http.html#transaction) action is among the most challenging parts of the FHIR specification to implement. It allows the user to submit a bundle containing a number of resources to be created/updated/deleted as a single atomic transaction.

HAPI provides a skeleton for implementing this action, although most of the effort will depend on the underlying implementation. The following example shows how to define a <i>transaction</i> method.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|transaction}}
``` 

Transaction methods require one parameter annotated with [@TransactionParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/TransactionParam.html), and that parameter may be of type [`List<IBaseResource>`](/hapi-fhir/apidocs/hapi-fhir-base/org/hl7/fhir/instance/model/api/IBaseResource.html) or [`Bundle`](/hapi-fhir/apidocs/hapi-fhir-structures-r4/org/hl7/fhir/r4/model/Bundle.html)`.

In terms of actually implementing the method, unfortunately there is only so much help HAPI will give you. One might expect HAPI to automatically delegate the individual operations in the transaction to other methods on the server but at this point it does not do that. There is a lot that transaction needs to handle (making everything atomic, replacing placeholder IDs across multiple resources which may even be circular, handling operations in the right order) and so far we have not found a way for the framework to do this in a generic way.

What it comes down to is the fact that transaction is a tricky thing to implement. For what it's worth, you could look at the HAPI FHIR JPA Server [TransactionProcessor](/hapi-fhir/apidocs/hapi-fhir-jpaserver-base/ca/uhn/fhir/jpa/dao/TransactionProcessor.html) class for inspiration on how to build a transaction processor of your own (note that this class is tightly coupled with the rest of the JPA Server so it is unlikely that it can be used directly outside of that context). 

Example URL to invoke this method (note that the URL is the base URL for the server, and the request body is a Bundle resource):

```http
POST http://fhir.example.com/
Content-Type: application/fhir+json

{
   "resourceType": "Bundle",
   "type": "transaction",
   "entry": [ ...entries... ]
}
```

<a name="system_search" />

# System Level - Search

Not yet implemented - Get in touch if you would like to help!

<a name="history" />

# History (Instance, Type, Server)

The [history](http://hl7.org/implement/standards/fhir/http.html#history) operation retrieves a historical collection of all versions of a single resource *(instance history)*, all resources of a given type *(type history)*, or all resources of any type on a server *(server history)*.

History methods are annotated with the [@History](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/History.html) annotation, and will have additional requirements depending on the kind of history method intended:

* For an **Instance History** method, the method must have a parameter annotated with the [@IdParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/IdParam.html) annotation, indicating the ID of the resource for which to return history. The method must either be defined in a [resource provider](./resource_providers.html#resource-providers), or must have a `type()` value in the [@History](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/History.html) annotation if it is defined in a [plain provider](./resource_providers.html#plain-providers).

* For a **Type History** method, the method must not have any [@IdParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/IdParam.html) parameter. The method must either be defined in a [resource provider](./resource_providers.html#resource-providers), or must have a `type()` value in the [@History](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/History.html) annotation if it is defined in a [plain provider](./resource_providers.html#plain-providers).

* For a **Server History** method, the method must not have any [@IdParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/IdParam.html) parameter, and must not have a `type()` value specified in the [@History](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/History.html) annotation. The method must be defined in a [plain provider](./resource_providers.html#plain-providers).


The following snippet shows how to define a history method on a server. Note that the following parameters are both optional, but may be useful in implementing the history operation:

* The [@Since](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/Since.html) method argument implements the `_since` parameter and should be of type [DateTimeType](/hapi-fhir/apidocs/hapi-fhir-structures-r4/org/hl7/fhir/r4/model/DateTimeType.html).

* The [@At](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/At.html) method argument implements the `_at` parameter and may be of type [DateRangeParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/param/DateRangeParam.html) or [DateTimeType](/hapi-fhir/apidocs/hapi-fhir-structures-r4/org/hl7/fhir/r4/model/DateTimeType.html).

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|history}}
``` 

The following snippet shows how to define various history methods in a client.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|historyClient}}
``` 

# Exceptions

When implementing a server operation, there are a number of failure conditions specified. For example, an [Instance Read](#instance_read) request might specify an unknown resource ID, or a [Type Create](#type_create) request might contain an invalid resource which can not be created.

See [REST Exception Handling](./resource_providers.html#exceptions) for information on available exceptions.


<a name="tags" />

# Tags

FHIR RESTful servers may support a feature known as tagging. Tags are a set of named flags called which use a FHIR Coding datatype (meaning that they have a system, value, and display just like any other coded field).

Tags have very specific semantics, which may not be obvious simply by using the HAPI API. It is important to review the specification [Tags Documentation](http://hl7.org/implement/standards/fhir/http.html#tags) before attempting to implement tagging in your own applications.

## Accessing Tags in a Read / VRead / Search Method

Tags are stored within a resource object, in the Resource.meta element. It is important to note that changing a resource's tags will not cause a version update to that resource.

In a server implementation, you may populate your tags into the returned resource(s) and HAPI will automatically place these tags into the response headers (for read/vread) or the bundle category tags (for search). The following example illustrates how to return tags from a server method. This example shows how to supply tags in a read method, but the same approach applies to vread and search operations as well.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|readTags}}
``` 

In a client operation, you simply call the read/vread/search method as you normally would (as described above), and if any tags have been returned by the server, these may be accessed from the resource metadata.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|clientReadTags}}
``` 

## Setting Tags in a Create/Update Method

Within a [Type Create](#type_create) or [Instance Update](#instance_update) method, it is possible for the client to specify a set of tags to be stored along with the saved resource instance.

Note that FHIR specifies that in an update method, any tags supplied by the client are copied to the newly saved version, as well as any tags the existing version had.

To work with tags in a create/update method, the pattern used in the read examples above is simply reversed. In a server, the resource which is passed in will be populated with any tags that the client supplied:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|createTags}}
``` 

## Removing Tags

In order to remove a tag, it does not suffice to remove it from the resource. Tags can be removed using the [Resource Operation Meta Delete](https://www.hl7.org/fhir/resource-operation-meta-delete.html), which takes a Parameter 
definining which tags to delete. 

# Handling _summary and _elements

The `_summary` and `_elements` parameters are automatically handled by the server, so no coding is required to make this work. 

However, if you wish to add parameters to manually handle these fields, the following example shows how to access these. This can be useful if you have an architecture where it is more work for the database/storage engine to load all fields.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|summaryAndElements}}
``` 

<a name="compartments" />

# Compartments

FHIR defines a mechanism for logically grouping resources together called [compartments](http://www.hl7.org/implement/standards/fhir/extras.html#compartment).

To define a search by compartment, you simply need to add the `compartmentName()` attribute to the [@Search](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/Search.html) annotation, and add an [@IdParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/IdParam.html) parameter.

The following example shows a search method in a resource provider which returns a compartment. Note that you may also add [@RequiredParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/RequiredParam.html) and [@OptionalParam](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/OptionalParam.html) parameters to your compartment search method.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|searchCompartment}}
``` 

Example URL to invoke this method:

```url
http://fhir.example.com/Patient/123/Condition
```

