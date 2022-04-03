# Authorization Interceptor

HAPI FHIR 1.5 introduced a new interceptor: [AuthorizationInterceptor](/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/interceptor/auth/AuthorizationInterceptor.html).

This interceptor can help with the complicated task of determining whether a user has the appropriate permission to perform a given task on a FHIR server. This is done by declaring a set of rules that can selectively allow (whitelist) and/or selectively block (blacklist) requests.

* [AuthorizationInterceptor JavaDoc](/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/interceptor/auth/AuthorizationInterceptor.html)
* [AuthorizationInterceptor Source](https://github.com/hapifhir/hapi-fhir/blob/master/hapi-fhir-server/src/main/java/ca/uhn/fhir/rest/server/interceptor/auth/AuthorizationInterceptor.java)

<p class="helpInfoCalloutBox">
    AuthorizationInterceptor has been well tested, but it is impossible to predict every scenario and environment in which HAPI FHIR will be used. Use with caution, and do lots of testing! We welcome feedback and suggestions on this feature. Please get in touch if you'd like to help test, have suggestions, etc.
</p>

The AuthorizationInterceptor works by allowing you to declare permissions based on an individual request coming in. In other words, you could have code that examines an incoming request and determines that it is being made by a Patient with ID 123. You could then declare that the requesting user has access to read and write any resource in compartment "Patient/123", which corresponds to any Observation, MedicationOrder etc with a subject of "`Patient/123`". On the other hand, another request might be determined to belong to an administrator user, and could be declared to be allowed to do anything.

The AuthorizationInterceptor is used by subclassing it and then registering your subclass with the `RestfulServer`. The following example shows a subclassed interceptor implementing some basic rules:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/AuthorizationInterceptors.java|patientAndAdmin}}
``` 

## Using AuthorizationInterceptor in a REST Server

The AuthorizationInterceptor works by examining the client request in order to determine whether "write" operations are legal, and looks at the response from the server in order to determine whether "read" operations are legal.

# Authorizing Read Operations

When authorizing a read operation, the AuthorizationInterceptor always allows client code to execute and generate a response. It then examines the response that would be returned before actually returning it to the client, and if rules do not permit that data to be shown to the client the interceptor aborts the request.

Note that there are performance implications to this mechanism, since an unauthorized user can still cause the server to fetch data even if they won't get to see it. This mechanism should be comprehensive however, since it will prevent clients from using various features in FHIR (e.g. <code>_include</code> or <code>_revinclude</code>) to "trick" the server into showing them data they shouldn't be allowed to see.

See the following diagram for an example of how this works.

<img src="/hapi-fhir/docs/images/hapi_authorizationinterceptor_read_normal.svg" alt="Write Authorization"/>

# Authorizing Write Operations

Write operations (create, update, etc.) are typically authorized by the interceptor by examining the parsed URL and making a decision about whether to authorize the operation before allowing Resource Provider code to proceed. This means that client code will not have a chance to execute and create resources that the client does not have permissions to create.

See the following diagram for an example of how this works.

<img src="/hapi-fhir/docs/images/hapi_authorizationinterceptor_write_normal.svg" alt="Write Authorization"/>


<a name="authorizing-sub-operations"/>

# Authorizing Sub-Operations

There are a number of situations where the REST framework doesn't actually know exactly what operation is going to be performed by the implementing server code. For example, if your server implements a <code>conditional update</code> operation, the server might not know which resource is actually being updated until the server code is executed.

Because client code is actually determining which resources are being modified, the server can not automatically apply security rules against these modifications without being provided hints from client code.

In this type of situation, it is important to manually notify the interceptor chain about the "sub-operation" being performed. The following snippet shows how to notify interceptors about a conditional create.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/AuthorizationInterceptors.java|conditionalUpdate}}
``` 

# Authorizing Patch Operations

The FHIR [patch](http://hl7.org/fhir/http.html#patch) operation presents a challenge for authorization, as the incoming request often contains very little detail about what is being modified.

In order to properly enforce authorization on a server that allows the patch operation, a rule may be added that allows all patch requests, as shown below.

This should be combined with server support for [Authorizing Sub-Operations](#authorizing-sub-operations) as shown above.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/AuthorizationInterceptors.java|patchAll}}
``` 

# Authorizing Multitenant Servers

The AuthorizationInterceptor has the ability to direct individual rules as only applying to a single tenant in a multitenant server. The following example shows such a rule.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/AuthorizationInterceptors.java|authorizeTenantAction}}
```

# Authorizing Bulk Export Operations

AuthorizationInterceptor can be used to provide nuanced control over the kinds of Bulk Export operations that a user can initiate when using the JPA Server.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/AuthorizationInterceptors.java|bulkExport}}
```

# Advanced Compartment authorization

AuthorizationInterceptor can be used to provide fine-grained control over compartment reads and writes as well. There is a strict FHIR definition
of which resources and related search parameters fall into a given compartment. However, sometimes the defaults do not suffice. The following is an example 
of an R4 ruleset which allows `device.patient` to be considered in the Patient compartment, on top of all the standard search parameters. 


```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/AuthorizationInterceptors.java|advancedCompartment}}
```
