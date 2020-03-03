# HAPI FHIR JPA Architecture

<img src="/hapi-fhir/docs/images/jpa_architecture.png" alt="Architecture" align="right"/>

The HAPI JPA Server has the following components:

* **Resource Providers:** A RESTful server [Resource Provider](/hapi-fhir/docs/server_plain/resource_providers.html) is provided for each resource type in a given release of FHIR. Each resource provider implements a	[@Search](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/annotation/Search.html) method implementing the complete set of search parameters defined in the FHIR specification for the given resource type.

   The resource providers also extend a superclass which implements all of the other FHIR methods, such as Read, Create, Delete, etc.
   
   Note that these resource providers are generated as a part of the HAPI build process, so they are not checked into Git. The resource providers do not actually implement any of the logic in searching, updating, etc. They simply receive the incoming HTTP calls (via the RestfulServer) and pass along the incoming requests to the DAOs.
   
* **HAPI DAOs:** The DAOs actually implement all of the database business logic relating to the storage, indexing, and retrieval of FHIR resources, using the underlying JPA API.

* **Hibernate:** The HAPI JPA Server uses the JPA library, implemented by Hibernate. No Hibernate specific features are used, so the library should also work with other providers (e.g. Eclipselink) but it is not tested regularly with them.

* **Database:** The RESTful server uses an embedded Derby database, but can be configured to talk to [any database supported by Hibernate](https://developer.jboss.org/wiki/SupportedDatabases2?_sscc=t).

# Schema

This implementation uses a fairly simple table design, with a single table being used to hold resource bodies (which are stored as CLOBs, optionally GZipped to save space) and a set of tables to hold search indexes, tags, history details, etc. This design has proven to be very scalable and flexible, and has been successfully used in large scale production architectures. That said, this design is still only one of many possible ways of designing a FHIR server so it is worth considering whether it is appropriate for the problem you are trying to solve.

