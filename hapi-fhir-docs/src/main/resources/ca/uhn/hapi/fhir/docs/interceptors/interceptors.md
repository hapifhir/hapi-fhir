# Interceptors: Overview

HAPI FHIR 3.8.0 introduced a new interceptor framework that is used across the entire library. In previous versions of HAPI FHIR, a "Server Interceptor" framework existed and a separate "Client Interceptor" framework existed. These have now been combined into a single unified (and much more powerful) framework.

Interceptor classes may "hook into" various points in the processing chain in both the client and the server. The interceptor framework has been designed to be flexible enough to hook into almost every part of the library. When trying to figure out "how would I make HAPI FHIR do X", the answer is very often to create an interceptor.

The following example shows a very simple interceptor example. 

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/Interceptors.java|sampleClass}}
```

## Interceptors Glossary

The HAPI FHIR interceptor framework uses a couple of key terms that are important to understand as you read the rest of this documentation:

* **Interceptor** &ndash; A class such as the example above that has one or more *Hook* methods on it. An optional marker annotation called [@Interceptor](/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Interceptor.html) exists and can be used, but is not required.

* **Hook** &ndash; An individual interceptor method that is invoked in response to a specific action taking place in the HAPI FHIR framework. Hook methods must be annotated with the [@Hook](/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Hook.html) annotation.

* **Pointcut** &ndash; A pointcut is a specific point in the HAPI FHIR processing pipeline that is being intercepted. Each Hook Method must declare which pointcut it is intercepting. All available pointcuts are defined by HAPI FHIR in the [Pointcut](/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html) enum.

* **Hook Params** &ndash; Every Pointcut defines a list of parameters that may be passed to a Hook Method for a given Pointcut. For example, the definition of the [SERVER_INCOMING_REQUEST_PRE_HANDLED](/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#SERVER_INCOMING_REQUEST_PRE_HANDLED) pointcut used in the example above defines 4 parameter types. A hook method for that parameter type can include any/all of these parameter types in its parameter list. The example above is only using one.

# Creating Interceptors

Creating your own interceptors is easy. Custom interceptor classes do not need to extend any other class or implement any particular interface. 

* They must have at least one method annotated with the [@Hook](/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html) annotation.

* The method must have an appropriate return value for the chosen [Pointcut](/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html).

* The method may have any of the parameters specified for the given [Pointcut](/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html). 

* The method must be public.

The following example shows a simple request counter interceptor.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RequestCounterInterceptor.java|interceptor}}
```

The following example shows an exception handling interceptor which overrides the built-in exception handling by providing a custom response.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/RequestExceptionInterceptor.java|interceptor}}
```

