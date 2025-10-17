# Filter Hook Interceptors

A filter hook is a hook that wraps a supplied function (i.e. Supplier). Filter hooks allow implementers to run custom code 
around the supplied function's execution (similar to Java Servlet Filters). Implementers can specify logic that should be executed:

1. Before the supplied function call is made

2. When the supplied function call throws an exception

3. After the supplied function call is made

The example below shows how a Filter Hook Interceptor can be implemented with the `BATCH2_CHUNK_PROCESS_FILTER` pointcut: 

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/interceptor/WorkChunkProcessingInterceptor.java|interceptor}}
```
