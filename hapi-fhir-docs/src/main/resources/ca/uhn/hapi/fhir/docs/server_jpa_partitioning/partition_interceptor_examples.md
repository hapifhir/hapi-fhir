# Partition Interceptor Examples

This page shows examples of partition interceptors.

# Example: Partitioning based on Tenant ID

The [RequestTenantPartitionInterceptor](/docs/interceptors/built_in_server_interceptors.html#request-tenant-partition-interceptor) uses the request tenant ID to determine the partition name. A simplified version of its source is shown below:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/PartitionExamples.java|partitionInterceptorRequestPartition}}
```

# Example: Partitioning based on headers

If requests are coming from a trusted system, that system might be relied on to determine the partition for reads and writes.

The following example shows a simple partition interceptor that determines the partition name by looking at a custom HTTP header:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/PartitionExamples.java|partitionInterceptorHeaders}}
```

# Example: Using Resource Contents

When creating resources, the contents of the resource can also be factored into the decision on which tenant to use. The following example shows a very simple algorithm, placing resources into one of three partitions based on the resource type. Other contents in the resource could also be used instead.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/PartitionExamples.java|partitionInterceptorResourceContents}}
```

# Example: Always Read All Partitions

This is an example of a simple interceptor that causes read/search/history requests to always use all partitions. This would be useful if partitioning is being used for use cases that do not involve data segregation for end users.

This interceptor only provides the [`STORAGE_PARTITION_IDENTIFY_READ`](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#STORAGE_PARTITION_IDENTIFY_READ) pointcut, so a separate interceptor would have to be added to provide the [`STORAGE_PARTITION_IDENTIFY_CREATE`](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#STORAGE_PARTITION_IDENTIFY_CREATE) pointcut in order to be able to write data to the server.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/PartitionExamples.java|partitionInterceptorReadAllPartitions}}
```

# Example: Smile CDR SMART Scopes

When deploying a partitioned server in Smile CDR using SMART on FHIR security, it may be desirable to use OAuth2 scope approval as a mechanism for determining which partitions a user should have access to. 

This interceptor looks for approved scopes named `partition-ABC` where "ABC" represents the tenant name that the user should have access to. 

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/PartitionExamples.java|partitionInterceptorReadBasedOnScopes}}
```
