# Interceptors: Overview

HAPI FHIR 3.8.0 introduced a new interceptor framework that is used across the entire library. In previous versions of HAPI FHIR, a "Server Interceptor" framework existed and a separate "Client Interceptor" framework existed. These have now been combined into a single unified (and very powerful) framework.

Interceptor classes may "hook into" various points in the processing chain in both the client and the server.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/MyInterceptor.java|sampleClass}}
```

