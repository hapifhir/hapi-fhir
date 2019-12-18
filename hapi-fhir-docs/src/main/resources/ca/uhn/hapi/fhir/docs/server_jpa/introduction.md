# JPA Server Introduction

The HAPI FHIR [Plain Server](/hapi-fhir/docs/server_plain/introduction.html) module can be used to create a FHIR server endpoint against an arbitrary data source, which could be a database of your own design, an existing clinical system, a set of files, or anything else you come up with.

HAPI also provides a persistence module which can be used to provide a complete RESTful server implementation, backed by a database of your choosing. This module uses the [JPA 2.0](http://en.wikipedia.org/wiki/Java_Persistence_API) API to store data in a database without depending on any specific database technology.

