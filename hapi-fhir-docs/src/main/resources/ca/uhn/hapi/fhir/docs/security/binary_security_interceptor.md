# Binary Security Interceptor

The Binary resource has an element called `Binary.securityContext` that can be used to declare a security context for a given resource instance.

The **BinarySecurityContextInterceptor** can be used to verify whether a calling user/client should have access to a Binary resource they are trying to access.

Note that this interceptor can currently only enforce identifier values found in `Binary.securityContext.identifier`. Reference values found in `Binary.securityContext.reference` are not examined by this interceptor at this time, although this may be added in the future.

This interceptor is intended to be subclassed. A simple example is shown below:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/interceptor/HeaderBasedBinarySecurityContextInterceptor.java}}
```

## Combining with Bulk Export

The `setBinarySecurityContextIdentifierSystem(..)` and `setBinarySecurityContextIdentifierValue(..)` properties on the `BulkExportJobParameters` object can be used to automatically populate the security context on Binary resources created by Bulk Export jobs with values that can be verified by this interceptor.
An interceptor on the `STORAGE_PRE_INITIATE_BULK_EXPORT` pointcut is the recommended way to set these properties when a new Bulk Export job is being kicked off.

NB: Previous versions recommended using the `STORAGE_INITIATE_BULK_EXPORT` pointcut, but this is no longer the recommended way.
`STORAGE_PRE_INITIATE_BULK_EXPORT` pointcut is called before `STORAGE_INITIATE_BULK_EXPORT` and is thus guaranteed to be called before
any AuthorizationInterceptors.
