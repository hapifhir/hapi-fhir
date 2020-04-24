# Resource Providers and Plain Providers

There are two types of providers that can be registered against a HAPI FHIR Plain Server:

* Resource Providers are POJO classes that implement operations for a specific resource type 

* Plain Providers are POJO classes that implement operations for multiple resource types, or for system-level operations.
 
<a name="resource-providers"/>

# Resource Providers

A Resource provider class must implement the [IResourceProvider](/hapi-fhir/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/IResourceProvider.html) interface, and will contain one or more methods which have been annotated with special annotations indicating which RESTful operation that method supports. Below is a simple example of a resource provider which supports the FHIR [read](http://hl7.org/fhir/http.html#read) operation (i.e. retrieve a single resource by ID) as well as the FHIR [search](http://hl7.org/fhir/http.html#search) operation (i.e. find any resources matching a given criteria) for a specific search criteria.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProvider.java|provider}}
``` 

## Adding more Methods (Search, History, Create, etc.)

You will probably wish to add more methods to your resource provider. See [REST Operations](./rest_operations.html) for lots more examples of how to add methods for various operations.

For now, we will move on to the next step though, which is creating	the actual server to hold your resource providers and deploying that. Once you have this working, you might want to come back and start adding other operations.

## Create a Server

Once your resource providers are created, your next step is to define a server class.

HAPI provides a class called [RestfulServer](/hapi-fhir/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/RestfulServer.html), which is a specialized Java Servlet. To create a server, you simply create a class which extends RestfulServer as shown in the example below.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ExampleRestfulServlet.java|servlet}}
``` 

<a name="plain-providers"/>

# Plain Providers

In addition to **Resource Providers**, which are resource-type specific, a second kind of provider known as **Plain Providers**. These providers can be used both to define resource operations that apply to multiple resource types, and to define operations that operate at the server level.

## Resource Operations

Defining one provider per resource is a good strategy to keep code readable and maintainable, but it is also possible to put methods for multiple resource types in a provider class.

Providers which do not implement the [IResourceProvider](/hapi-fhir/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/IResourceProvider.html) (and therefore are not bound to one specific resource type) are known as **Plain Providers**.

A plain provider may implement any [REST operation](./rest_operations.html), but will generally need to explicitly state what type of resource it applies to. If the method directly returns a resource or a collection of resources (as in an [instance read](./rest_operations.html#instance_read) or [type search](./rest_operations.html#type_search) operation) the resource type will be inferred automatically. If the method returns a [Bundle Resource](http://hl7.org/fhir/bundle.html), it is necessary to explicitly specify the resource type in the method annotation. The following example shows this:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ExampleProviders.java|plainProvider}}
```

In addition, some methods are not resource specific. For example, the [system history](./rest_operations.html#history) operation returns historical versions of *all resource types* on a server, so it needs to be defined in a plain provider.

Once you have defined your plain providers, they are passed to the server in a similar way to the resource providers.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ExampleProviders.java|plainProviderServer}}
```

# Common Method Parameters

Different RESTful methods will have different requirements in terms of the method parameters they require, as described in the [REST Operations](./rest_operations.html) page.

In addition, there are several parameters you may add in order to meet specific needs of your application.

## Accessing the underlying Servlet Request/Response

In some cases, it may be useful to have access to the underlying HttpServletRequest and/or HttpServletResponse objects. These may be added by simply adding one or both of these objects as method parameters.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RestfulPatientResourceProviderMore.java|underlyingReq}}
```

<a name="exceptions"/>

# REST Exception/Error Handling

Within your RESTful operations, you will generally be returning resources or bundles of resources under normal operation. During execution, you may also need to propagate errors back to the client for a variety of reasons.

## Automatic Exception Handling

By default, HAPI generates appropriate error responses for several built-in conditions. For example, if the user makes a request for a resource type that does not exist, or tries to perform a search using an invalid parameter, HAPI will automatically generate an `HTTP 400 Invalid Request`, and provide an OperationOutcome resource as response containing details about the error.

Similarly, if your method implementation throws any exceptions (checked or unchecked) instead of returning normally, the server will usually (see below) automatically generate an `HTTP 500 Internal Error` and generate an OperationOutcome with details about the exception.

## Generating Specific HTTP Error Responses

In many cases, you will want to respond to client requests with a specific HTTP error code (and possibly your own error message too). Sometimes this is a requirement of the FHIR specification. (e.g. the "validate" operation requires a response of `HTTP 422 Unprocessable Entity` if the validation fails).

Sometimes this is simply a requirement of your specific application (e.g. you want to provide application specific HTTP status codes for certain types of errors), and other times this may be a requirement of the FHIR or HTTP specifications to respond in a specific way to certain conditions.

To customize the error that is returned by HAPI's server methods, you should throw an exception which extends HAPI's [BaseServerResponseException](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/server/exceptions/BaseServerResponseException.html) class. Various exceptions which extend this class will generate a different HTTP status code.

For example, the [ResourceNotFoundException](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/server/exceptions/ResourceNotFoundException.html) causes HAPI to return an `HTTP 404 Resource Not Found`. A complete list of available exceptions is available in the [exceptions package summary](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/server/exceptions/package-summary.html).

If you wish to return an HTTP status code for which there is no pre-defined exception, you may throw the [UnclassifiedServerFailureException](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/server/exceptions/UnclassifiedServerFailureException.html), which allows you to return any status code you wish.

## Returning an OperationOutcome for Errors

By default, HAPI will automatically generate an OperationOutcome which contains details about the exception that was thrown. You may wish to provide your own OperationOutcome instead. In this case, you may pass one into the constructor of the exception you are throwing.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ServerExceptionsExample.java|returnOO}}
```

# Server Lifecycle Methods

Resource providers may optionally want to be notified when the server they are registered with is being destroyed, so that they can perform cleanup. In this case, a method annotated with the [@Destroy](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/Destroy.html) annotation can be added (this method should be public, return `void`, and take no parameters).

This method will be invoked once by the RestfulServer when it is shutting down.

