# HAPI FHIR Server Introduction

HAPI FHIR provides several mechanisms for building FHIR servers. The appropriate choice depends on the specifics of what you are trying to accomplish.

## Plain Server / Facade

The HAPI FHIR Plain Server (often referred to as a Facade) is an implementation of a FHIR server against an arbitrary backend that you provide.

In this mode, you write code that handles resource storage and retrieval logic, and HAPI FHIR takes care of:

* HTTP Processing
* Parsing / Serialization
* FHIR REST semantics

This module was originally created at [University Health Network](https://uhn.ca) (UHN) as a mechanism for placing a common FHIR layer on top of a series of existing data sources, including an EMR, an enterprise patient scheduling system, and a series of clinical data repositories. All of these systems existed long before FHIR was adopted at UHN and HAPI FHIR was created to make the process of adopting FHIR easier.

This module has been used by many organizations to successfully create FHIR servers in a variety of use cases, including:

* **Hospitals:** Adding a FHIR data access layer to Existing Enterprise Data Warehouses and Clinical Data Repositories
* **Vendors:** Integration into existing products in order to add FHIR capabilities
* **Researchers:** Aggregate data collection and reporting platforms   

To get started with the Plain Server, jump to [Plain Server Introduction](./introduction.html).

## JPA Server

The HAPI FHIR JPA Server is a complete implementation of a FHIR server against a relational database. Unlike the Plain Server, the JPA server provides its own database schema and handles all storage and retrieval logic without any coding being required.

The JPA server has been successfully used in many use cases, including:

* **App Developers:** The JPA server has been used as a backend data storage layer for various mobile and web-based apps. The ease of development combined with the power of the FHIR specification makes developing clinical apps a very enjoyable experience. 

* **Government/Enterprise:** Many large architectures, including enterprise messaging systems, regional data repositories, telehealth solutions, etc. have been created using HAPI FHIR JPA server as a backend. These systems often scale to handle millions of patients and beyond. 

To get started with the JPA Server, jump to [JPA Server Introduction](/docs/server_jpa/introduction.html).
  
## JAX-RS Server

For users in an environment where existing services using JAX-RS have been created, it is often desirable to use JAX-RS for FHIR servers as well. HAPI FHIR provides a JAX-RS FHIR server implementation for this purpose.

To get started with the JAX-RS FHIR Server, jump to [JAX-RS FHIR Server Introduction](/docs/server_plain/jax_rs.html).  
