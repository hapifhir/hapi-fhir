# Partitioning and Multitenancy

HAPI FHIR 5.0.0 introduced a new feature to HAPI FHIR JPA server called **Partitioning**.

Partitioning allows each resource on the server to be placed in a partition, which is essentially just an arbitrary identifier grouping a set of resources together.

Partitioning is designed to be very flexible, and can be used to achieve different outcomes. For example:

* Partitioning could be used to achieve **multitenancy**, where there are multiple logically separate pools of resources on the server. Traditionally this kind of setup is desired when each of these pools belongs to a distinct user group / organization / customer / etc. (a "tenant"), and each of these tenants should not be able to access or modify data belonging to anther tenant.

* Partitioning could also be used to **logically separate data coming from distinct sources** within an organization. For example, patient records might be placed in one partition, lab data sourced from a lab system might be placed in a second partition and patient surveys from a survey app might be placed in another. In this situation data does not need to be completely segregated (lab Observation records may have references to Patient records in the patient partition) but these partitions might be used to create security groups, retention policies, etc.

* Partitioning could be used for **geographic sharding**, keeping data in a partition that is geographically closest to where it is likely to be used.

These examples each have different properties in terms of security rules, and how data is organized and searched.

# Architecture

Partitioning involves the addition of two new columns to many tables within the HAPI FHIR JPA database schema:

* **PARTITION_ID** &ndash; This is an integer indicating the specific partition that a given resource is placed in. This column can also be *NULL*, meaning that the given resource is in the **Default Partition**.
* **PARTITION_DATE** &ndash; This is a date/time column that can be assigned an arbitrary value depending on your use case.


# Enabling Partitioning

Enabling partitioning on the server involves a set of steps.

The [PartitionConfig](/apidocs/hapi-fhir-jpaserver-model/ca/uhn/fhir/jpa/model/config/PartitionConfig.html) bean contains configuration settings related to partitioning within the server. To enable partitioning, the  


# Limitations

Partitioning is a relatively new feature in HAPI FHIR (added in HAPI FHIR 5.0.0) and has a number of known limitations. If you are intending to use partitioning for achieving a multi-tenant architecture it is important to carefully consider these limitations. 

None of the limitations listed here are considered permanent. Over time the HAPI FHIR team are hoping to make all of these features partition aware.

* **Server Capability Statement is not partition aware**: The server creates and exposes a single server capability statement, covering all partitions. This can be misleading when partitioning us used as a multitenancy strategy. 

* **Subscriptions may not be partitioned**: All subscriptions must be placed in the default partition, and subscribers will receive deliveries for any matching resources from all partitions.

* **Conformance resources may not be partitioned**: The following resources must be placed in the default partition, and will be shared for any validation activities across all partitions:
   * StructureDefinition
   * Questionnaire
   * ValueSet
   * CodeSystem
   * ConceptMap

* **Search Parameters are not partitioned**: There is only one set of SearchParameter resources for the entire system, and any search parameters will apply to resources in all partitions. All SearchParameter resources must be stored in the default partition.
   
* **Bulk Operations are not partition aware**: Bulk export operations will export data across all partitions.

