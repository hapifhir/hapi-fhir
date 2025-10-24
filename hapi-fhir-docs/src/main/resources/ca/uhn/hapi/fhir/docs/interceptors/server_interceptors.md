# Server Interceptors

There are many different Pointcuts available to server developers. In general, a server can be thought of as playing two roles: Server and Storage.

In the case of a Plain Server, HAPI FHIR itself performs the role of the Server and your [Resource Provider](/docs/server_plain/resource_providers.html) classes perform the role of Storage.

In the case of a JPA Server, HAPI FHIR itself performs both roles. This means that **SERVER_xxx** Pointcuts may be intercepted by interceptors on any HAPI FHIR server. However, if you want to intercept **STORAGE_xxx** Pointcuts on a plain server, you will need to trigger them yourself.

# Registering Interceptors

How to register an interceptor differs depending on the type of pointcut it registers. This is especially important if you modify resources without going through the `RestfulServer`.

For example, if you are listening to a message queue, you can have a call:

`Message Queue -> Event listener -> DAO`

This will not trigger an interceptor registered on the RestfulServer, even if this interceptor hooks onto a **STORAGE_xxx** pointcut.

### **SERVER_xxx** pointcut
|                   | Registered on    |                     |
| ----------------- | --------------- | ------------------- |
| **Call source**   | RestfulServer   | IInterceptorService |
| RestfulServer     | ✅              | ❌                  |
| Other implementation | ❌           | ❌                  |

### **STORAGE_xxx** pointcut
|                   | Registered on    |                     |
| ----------------- | --------------- | ------------------- |
| **Call source**   | RestfulServer   | IInterceptorService |
| RestfulServer     | ✅              | ✅                  |
| Other implementation | ❌           | ✅                  |

✅: Triggered
❌: Not triggered

Note that you should not register any interceptor with storage pointcuts on both the `RestfulServer` and `IInterceptorService`. If you do so, it will be triggered twice.

# Example: Clearing Tags

The following example shows an interceptor that clears all tags, profiles, and security labels from a resource prior to storage in the JPA server.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/interceptor/TagTrimmingInterceptor.java|TagTrimmingInterceptor}}
``` 
