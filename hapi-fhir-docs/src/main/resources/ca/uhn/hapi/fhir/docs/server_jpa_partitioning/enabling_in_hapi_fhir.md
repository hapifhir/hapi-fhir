# Enabling Partitioning in HAPI FHIR

Follow these steps to enable partitioning on the server:

The [PartitionSettings](/hapi-fhir/apidocs/hapi-fhir-jpaserver-model/ca/uhn/fhir/jpa/model/config/PartitionSettings.html) bean contains configuration settings related to partitioning within the server. To enable partitioning, the [setPartitioningEnabled(boolean)](/hapi-fhir/apidocs/hapi-fhir-jpaserver-model/ca/uhn/fhir/jpa/model/config/PartitionSettings.html#setPartitioningEnabled(boolean)) property should be enabled.

The following settings can be enabled:

* **Include Partition in Search Hashes** ([JavaDoc](/hapi-fhir/apidocs/hapi-fhir-jpaserver-model/ca/uhn/fhir/jpa/model/config/PartitionSettings.html#setIncludePartitionInSearchHashes(boolean))): If this feature is enabled, partition IDs will be factored into [Search Hashes](/hapi-fhir/docs/server_jpa/schema.html#search-hashes). When this flag is not set (as is the default), when a search requests a specific partition, an additional SQL WHERE predicate is added to the query to explicitly request the given partition ID. When this flag is set, this additional WHERE predicate is not necessary since the partition is factored into the hash value being searched on. Setting this flag avoids the need to manually adjust indexes against the HFJ_SPIDX tables. Note that this flag should **not be used in environments where partitioning is being used for security purposes**, since it is possible for a user to reverse engineer false hash collisions.

* **Cross-Partition Reference Mode**: ([JavaDoc](/hapi-fhir/apidocs/hapi-fhir-jpaserver-model/ca/uhn/fhir/jpa/model/config/PartitionSettings.html#setAllowReferencesAcrossPartitions(ca.uhn.fhir.jpa.model.config.PartitionSettings.CrossPartitionReferenceMode))): This setting controls whether resources in one partition should be allowed to create references to resources in other partitions.
