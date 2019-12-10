# Getting Started with HAPI FHIR JPA Server

The recommended way to get started with the HAPI FHIR JPA server module is
to begin with the starter project. This project can be found at the following link: [https://github.com/hapifhir/hapi-fhir-jpaserver-starter](https://github.com/hapifhir/hapi-fhir-jpaserver-starter)

This project is a fully contained FHIR server, supporting all standard operations (read/create/delete/etc). It bundles an embedded instance of the [H2 Java Database](http://h2database.com) so that the server can run without depending on any external database, but it can also be configured to use an installation of Oracle, Postgres, etc.
