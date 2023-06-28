# Binary Security Interceptor

The Binary resource has an element called `Binary.securityContext` that can be used to declare a security context for a given resource instance.

The **BinarySecurityContextInterceptor** can be used to verify whether a calling user/client should have access to a Binary resource they are trying to access.

Note that this interceptor can currently only enforce identifier values found in `Binary.securityContext.identifier`. Reference values found in `Binary.securityContext.reference` are not examined by this interceptor at this time, although this may be added in the future.

This interceptor is intended to be subclassed. A simple example is shown below:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/SecurityInterceptors.java}}
``` 
