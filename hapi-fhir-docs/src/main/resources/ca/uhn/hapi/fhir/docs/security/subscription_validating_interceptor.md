# Subscription Validating Interceptor

During its evaluation, a subscription has access to resources that the user who defined the subscription would not necessarily have access to. For this reason, it is important to restrict write access for Subscription resources to trustworthy users. In most cases, this can be accomplished by directly restricting write access on Subscription resources. However, use cases exist where it may be necessary to control the creation of these resources at a finer level of detail. 

To support these cases, the `SubscriptionValidatingInterceptor` can be subclassed and extended to provide custom authorization rules for creating and updating `Subscriptions`.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/SubscriptionInterceptors.java|validatingInterceptor}}
``` 

The method `isUserAuthorizedToWriteSubscriptions` accepts four parameters:

| Parameter name        | Type               | Usage notes                                                                                                                                                                                 |
|-----------------------|--------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| theSubscription       | IBaseResource      | The subscription being written. It can be inspected to restrict the user to only Subscriptions having certain characteristics (e.g., white-list which URLs can be targetted by a rest hook) |
| theRequestDetails     | RequestDetails     | The user session. It allows permission to be granted based on user identity.                                                                                                                |
| theRequestPartitionId | RequestPartitionId | The target partition, if any. It allows restricting which partitions Subscriptions can be written to.                                                                                       |
| thePointcut           | Pointcut           | Identifies whether this is a create or an update operation. It has the values `STORAGE_PRESTORAGE_RESOURCE_CREATED` or `STORAGE_PRESTORAGE_RESOURCE_UPDATED`.                               |

It is recommended that the default implementation of this interceptor be unregistered before a custom interceptor is registered.

Because the system's ability to update `Subscriptions` must not be denied, these custom rules will not be invoked if the pointcut is `STORAGE_PRESTORAGE_RESOURCE_UPDATED` and the request details object is of type `SystemRequestDetails`.
