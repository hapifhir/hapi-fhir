# Client Interceptors

Client interceptors may be used to examine requests and responses before and after they are sent to the remote server.

# Registering Client Interceptors

Interceptors for the client are registered against individual client instances, as shown in the example below.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/Interceptors.java|registerClient}}
```
