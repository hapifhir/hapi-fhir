# JAX-RS Server

The HAPI FHIR Plain Server ([RestfulServer](/hapi-fhir/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/RestfulServer.html)) is implemented as a standard JEE Servlet, meaning that it can be deployed in any compliant JEE web container. 

For users who already have an existing JAX-RS infrastructure, and who would like to use that technology for their FHIR stack as well, a module exists which implements the server using [JAX-RS technology](https://jax-rs-spec.java.net/nonav/2.0/apidocs/index.html). 

<div class="doc_info_bubble">
    The JAX-RS module is a community-supported module that was not developed by the core HAPI FHIR team. Before deciding to use the HAPI FHIR JAX-RS module, please be aware that it does not have as complete of support for the full FHIR REST specification as the Plain Server. If you need a feature that is missing, please consider adding it and making a pull request! 
</div>

## Features

The server currently supports:
 
* Automatic [Capability Statement Generation](./introduction.html#capabilities)
* [@Read](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/Read.html)
* [@RSearch](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/Search.html)
* [@Create](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/Create.html)
* [@Update](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/Update.html)
* [@Delete](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/Delete.html)
* [@Operation](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/Operation.html)

The primary intention for this project is to ensure that other web technologies (JAX-RS in this case) can be used together with the base-server functionality. 
An example server can be found in the Git repo [here](https://github.com/hapifhir/hapi-fhir/tree/master/hapi-fhir-jaxrsserver-example).

# JAX-RS Implementation specifics

The set-up of a JAX-RS server goes beyond the scope of this documentation. The implementation of the server follows the same pattern as the standard server. It is required to put the correct [annotation](./rest_operations.html) on the methods in the [Resource Providers](./resource_providers.html) in order to be able to call them. 

Implementing a JAX-RS Resource Provider requires some JAX-RS annotations. The [@Path](https://docs.oracle.com/javaee/6/api/javax/ws/rs/Path.html) annotation needs to define the resource path. The <code><a href="https://docs.oracle.com/javaee/6/api/javax/ws/rs/Produces.html">@Produces</a></code> annotation needs to declare the produced formats. The constructor needs to pass the class of the object explicitly in order to avoid problems with proxy classes in a Java EE environment.

It is necessary to extend the abstract class [AbstractJaxRsResourceProvide](/hapi-fhir/apidocs/hapi-fhir-jaxrsserver-base/ca/uhn/fhir/jaxrs/server/AbstractJaxRsResourceProvider.html).

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/JaxRsPatientRestProvider.java|jax-rs-provider-construction}}
```

## Extended Operations

[Extended Operations](./rest_operations_operations.html) require the correct JAX-RS (
[@Path](https://docs.oracle.com/javaee/6/api/javax/ws/rs/Path.html), [@GET](https://docs.oracle.com/javaee/6/api/javax/ws/rs/GET.html) or [@POST](https://docs.oracle.com/javaee/6/api/javax/ws/rs/POST.html) annotations. The body of the method needs to call the method [AbstractJaxRsResourceProvider#customOperation](/hapi-fhir/apidocs/hapi-fhir-jaxrsserver-base/ca/uhn/fhir/jaxrs/server/AbstractJaxRsResourceProvider.html#customOperation(java.lang.String,ca.uhn.fhir.rest.api.RequestTypeEnum,java.lang.String,java.lang.String,ca.uhn.fhir.rest.api.RestOperationTypeEnum) with the correct parameters. The server will then call the method with corresponding name.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/JaxRsPatientRestProvider.java|jax-rs-provider-operation}}
```

In order to create the conformance profile, a conformance provider class needs to be deployed which exports the provider's conformance statements. These providers need to be returned  as the result of the method [AbstractJaxRsConformanceProvider#getProviders](/hapi-fhir/apidocs/hapi-fhir-jaxrsserver-base/ca/uhn/fhir/jaxrs/server/AbstractJaxRsConformanceProvider.html#getProviders()). This method is called once, during [PostConstruct](https://docs.oracle.com/javaee/6/api/javax/annotation/PostConstruct.html).

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/JaxRsConformanceProvider.java|jax-rs-conformance}}
```

# A Complete Example

A complete example showing how to implement a JAX-RS RESTful server can be found in our Git repo here:

* [hapi-fhir-jaxrsserver-example](https://github.com/hapifhir/hapi-fhir/tree/master/hapi-fhir-jaxrsserver-example)
