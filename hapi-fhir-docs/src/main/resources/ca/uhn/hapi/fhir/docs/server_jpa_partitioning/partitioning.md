# Partitioning and Multitenancy

HAPI FHIR 5.0.0 introduced a new feature to HAPI FHIR JPA server called **Partitioning**.

Partitioning allows each resource on the server to be placed in a partition, which is essentially just an arbitrary identifier grouping a set of resources together.

Partitioning is designed to be very flexible, and can be used to achieve different outcomes. For example:

* Partitioning could be used to achieve **multitenancy**, where there are multiple logically separate pools of resources on the server. Traditionally this kind of setup is desired when each of these pools belongs to a distinct user group / organization / customer / etc. (a "tenant"), and each of these tenants should not be able to access or modify data belonging to another tenant.

* Partitioning could also be used to **logically separate data coming from distinct sources** within an organization. For example, patient records might be placed in one partition, lab data sourced from a lab system might be placed in a second partition and patient surveys from a survey app might be placed in another. In this situation data does not need to be completely segregated (lab Observation records may have references to Patient records in the patient partition) but these partitions might be used to support security groups, retention policies, etc.

* Partitioning could be used for **geographic sharding**, keeping data in a partition that is geographically closest to where it is likely to be used.

These examples each have different properties in terms of security rules, and how data is organized and searched.

# Architecture

## Conceptual Architecture

Partitioning in HAPI FHIR JPA means that every resource has a partition identity. This identity consists of the following attributes:

* **Partition Name**: This is a short textual identifier for the partition that the resource belongs to. This might be a customer ID, a description of the type of data in the partition, or something else. There is no restriction on the text used aside from a maximum length of 200, but generally it makes sense to limit the text to URL-friendly characters.

* **Partition ID**: This is an integer ID that corresponds 1:1 with the partition Name. It is used in the database as the partition identifier.   

* **Partition Date**: This is an additional partition discriminator that can be used to implement partitioning strategies using a date axis.

Mappings between the **Partition Name** and the **Partition ID** are maintained using the [Partition Management Operations](./partitioning_management_operations.html).

## Logical Architecture

At the database level, partitioning involves the use of two dedicated columns to many tables within the HAPI FHIR JPA [database schema](/hapi-fhir/docs/server_jpa/schema.html):

* **PARTITION_ID** &ndash; This is an integer indicating the specific partition that a given resource is placed in. This column can also be *NULL*, meaning that the given resource is in the **Default Partition**.
* **PARTITION_DATE** &ndash; This is a date/time column that can be assigned an arbitrary value depending on your use case. Typically, this would be used for use cases where data should be automatically dropped after a certain time period using native database partition drops. 

When partitioning is used, these two columns will be populated with the same value for a given resource on all resource-specific tables (this includes [HFJ_RESOURCE](/hapi-fhir/docs/server_jpa/schema.html#HFJ_RESOURCE) and all tables that have a foreign key relationship to it including [HFJ_RES_VER](/hapi-fhir/docs/server_jpa/schema.html#HFJ_RES_VER), [HFJ_RESLINK](/hapi-fhir/docs/server_jpa/schema.html#HFJ_RES_LINK), [HFJ_SPIDX_*](/hapi-fhir/docs/server_jpa/schema.html#search-indexes), etc.)

When a new resource is **created**, an [interceptor hook](#partition-interceptors) is invoked to request the partition ID and date to be assigned to the resource.

When a resource is **updated**, the partition ID and date from the previous version will be used.

When a **read operation** is being performed (e.g. a read, search, history, etc.), a separate [interceptor hook](#partition-interceptors) is invoked in order to determine whether the operation should target a specific partition. The outcome of this hook determines how the partitioning manifests itself to the end user: 

* The system can be configured to operate as a **multitenant** solution by configuring the partition interceptor to scope all read operations to read data only from the partition that request has access to.
* The system can be configured to operate with logical segments by configuring the partition interceptor to scope read operations to access all partitions.


# Partitioning and Resource IDs

In a partitioned repository, it is important to understand that only a single pool of resource IDs exists. In other words, only one resource with the ID `Patient/1` can exist across all partitions, and it must be in a single partition.

This fact can have security implications:

* A client might be blocked from creating `Patient/ABC` in the partition they have access to because this ID is already
  in use in another partition.

* In a server using the default configuration of
  SEQUENTIAL_NUMERIC [Server ID Strategy](/hapi-fhir/apidocs/hapi-fhir-storage/ca/uhn/fhir/jpa/api/config/DaoConfig.html#setResourceServerIdStrategy(ca.uhn.fhir.jpa.api.config.DaoConfig.IdStrategyEnum))
  a client may be able to infer the IDs of resources in other partitions based on the ID they were assigned.

These considerations can be addressed by using UUID Server ID Strategy, and disallowing client-assigned IDs.  


# Partition Interceptors

In order to implement partitioning, an interceptor must be registered against the interceptor registry (either the REST Server registry, or the JPA Server registry will work).

This interceptor can implement the hooks shown below.

## Identify Partition for Create (Required)

A hook against the [`Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE`](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#STORAGE_PARTITION_IDENTIFY_CREATE) pointcut must be registered, and this hook method will be invoked every time a resource is created in order to determine the partition the resource is assigned to.

The criteria for determining the partition will depend on your use case. For example:
 
* If you are implementing multi-tenancy the partition might be determined by using the [Request Tenant ID](/docs/server_plain/multitenancy.html). It could also be determined by looking at request headers, or the authorized user/session context, etc.

* If you are implementing segmented data partitioning, the partition might be determined by examining the actual resource being created, by the identity of the sending system, etc.    

## Identify Partition for Read (Optional)

A hook against the [`Pointcut.STORAGE_PARTITION_IDENTIFY_READ`](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#STORAGE_PARTITION_IDENTIFY_READ) pointcut must be registered, and this hook method will be invoked every time a resource is created in order to determine the partition to assign the resource to.

As of HAPI FHIR 5.3.0, the *Identify Partition for Read* hook method may return multiple partition names or IDs. If more than one partition is identified, the server will search in all identified partitions.  

## Non-Partitionable Resources

Some resource types can not be placed in any partition other than the DEFAULT partition. When a resource of one of these types is being created, the *STORAGE_PARTITION_IDENTIFY_CREATE* pointcut is invoked, but the hook method must return [defaultPartition()](https://hapifhir.io/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/model/RequestPartitionId.html#defaultPartition()). A partition date may optionally be included.

The following resource types may not be placed in any partition except the default partition:

* CapabilityStatement
* CodeSystem
* CompartmentDefinition
* ConceptMap
* Library
* NamingSystem
* OperationDefinition
* Questionnaire
* SearchParameter
* StructureDefinition
* StructureMap
* ValueSet

## Examples

See [Partition Interceptor Examples](./partition_interceptor_examples.html) for various samples of how partitioning interceptors can be set up.

# Complete Example: Using Request Tenants

In order to achieve a multitenant configuration, the following configuration steps must be taken:

* Partitioning must be enabled.
* A [Tenant Identification Strategy](/docs/server_plain/multitenancy.html) must be enabled on the RestfulServer.
* A [RequestTenantPartitionInterceptor](/docs/interceptors/built_in_server_interceptors.html#request-tenant-partition-interceptor) instance must be registered as an interceptor.

Additionally, indexes will likely need to be tuned in order to support the partition-aware queries.

The following snippet shows a server with this configuration.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/PartitionExamples.java|multitenantServer}}
```

Once enabled, HTTP Requests to the FHIR server must include the name of the partition in the request, for identification purposes. With no multitenancy, a request to create a Patient could look like this:

```HTTP
POST www.example.com/fhir/Patient
```

With partitioning enabled, if we were to now create a patient in the `DEFAULT` paritition, the request would now look like this:


```
POST www.example.com/fhir/DEFAULT/Patient
```

Failure to add a partition name to the request path will result in an error when multitenancy is enabled.

# Limitations

Partitioning is a relatively new feature in HAPI FHIR (added in HAPI FHIR 5.0.0) and has a number of known limitations. If you are intending to use partitioning for achieving a multi-tenant architecture it is important to consider these limitations. 

None of the limitations listed here are considered permanent. Over time the HAPI FHIR team is hoping to make all of these features partition aware.

* **The following ResourceTypes may not be partitioned**: The following resources must be placed in the default partition:
   * CapabilityStatement
   * CodeSystem
   * CompartmentDefinition
   * ConceptMap
   * Library
   * NamingSystem
   * OperationDefinition
   * Questionnaire
   * SearchParameter
   * StructureDefinition
   * StructureMap
   * ValueSet

* **Server Capability Statement is not partition aware**: The server creates and exposes a single server capability statement, covering all partitions. This can be misleading when partitioning us used as a multitenancy strategy.

* **Conformance resources may not be partitioned**: Conformance resources must be placed in the default partition, and will be shared for any validation activities across all partitions.

* **Search Parameters are not partitioned**: There is only one set of SearchParameter resources for the entire system, and any search parameters will apply to resources in all partitions. All SearchParameter resources must be stored in the default partition.

* **Cross-partition History Operations are not supported**: It is not possible to perform a `_history` operation that spans all partitions (`_history` does work when applied to a single partition however). 
   
* **Bulk Operations are not partition aware**: Bulk export operations will export data across all partitions.

* **Package Operations are not partition aware**: Package operations will only create, update and query resources in the default partition.

* **Advanced Elasticsearch indexing is not partition optimized**: The results are correctly partitioned, but the extended indexing is not optimized to account for partitions. 

* **Subscriptions are partition aware**: Subscriptions can be placed on any partition and will deliver matching resources from the same partition. A subscription on the default can deliver resource from all partition if it is placed in the default partition with the cross-partition extension and the server allows cross-partition subscriptions.
