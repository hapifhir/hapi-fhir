# REST Server Security: Introduction

Security is a complex topic which goes far beyond the scope of FHIR or HAPI. Every system and architecture operates in a different set of rules, and has different security requirements.

As such, HAPI FHIR does not provide a single one-size-fits-all security layer. Instead, it provides a number of useful tools and building blocks that can be built around as a part of your overall security architecture.

Because HAPI FHIR's REST server is based on the Servlet API, you may use any security mechanism which works in that environment. Some servlet containers may provide security layers you can plug into. The rest of this page does not explore that method, but rather looks at HAPI FHIR hooks that can be used to implement FHIR specific security.

# Authentication vs Authorization

Background reading: [Wikipedia - Authentication](https://en.wikipedia.org/wiki/Authentication)

Server security is divided into three topics:

* **Authentication (AuthN):** Is verifying that the user is who they say they are. This is typically accomplished by testing a username/password in the request, or by checking a "bearer token" in the request.

* **Authorization (AuthZ):** Is verifying that the user is allowed to perform the given action. For example, in a FHIR application you might use AuthN to test that the user making a request to the FHIR server is allowed to access the server, but that test might determine that the requesting user is not permitted to perform write operations and therefore block a FHIR <code>create</code> operation. This is AuthN and AuthZ in action.

* **Consent and Audit:** Is verifying that a user has rights to see/modify the specific resources they are requesting, applying any directives to mask data being returned to the client (either partially or completely), and creating a record that the event occurred.

# Authentication Interceptors

The [Server Interceptor](/docs/interceptors/server_interceptors.html) framework can provide an easy way to test for credentials. The following example shows a simple custom interceptor which tests for HTTP Basic Auth.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/SecurityInterceptors.java|basicAuthInterceptorExample}}
``` 

## HTTP Basic Auth

Note that if you are implementing HTTP Basic Auth, you may want to return a <code>WWW-Authenticate</code> header with the response. The following snippet shows how to add such a header with a custom realm:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/SecurityInterceptors.java|basicAuthInterceptorRealm}}
``` 

