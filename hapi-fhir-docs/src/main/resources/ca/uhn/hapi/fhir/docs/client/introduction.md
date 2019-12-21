# Client Introduction

HAPI FHIR provides a built-in mechanism for connecting to FHIR REST servers.

The HAPI RESTful client is designed to be easy to set up and to allow strong compile-time type checking wherever possible.

There are two types of REST clients provided by HAPI:

* The [Generic (Fluent) client](./generic_client.html) is designed to be very flexible, yet easy to use. It allows you to build up FHIR REST invocations using a fluent API that is consistent and powerful. If you are getting started with HAPI FHIR and aren't sure which client to use, this is the client to start with.   

* The [Annotation Client](./annotation_client.html) client relies on static binding to specific operations to give better compile-time checking against servers with a specific set of capabilities
exposed. This second model takes more effort to use, but can be useful if the person defining the specific methods to invoke is not the same person who is using those methods.

