# Partitioned ID Mode Introduction --EXPERIMENTAL--

HAPI FHIR 5.0.0 introduced partitioning to the JPA server. In the current partitioning design, each resource-specific table (i.e. each table in the database schema where each row pertains to a single FHIR resource) has two new columns: `PARTITION_ID`, and `PARTITION_DATE`.

The `PARTITION_DATE` column may be set by user code, but it otherwise untouched by HAPI FHIR. The `PARTITION_ID` column is the main partitioning mechanism. It uses an arbitrary Integer value to demote the partition associated with an individual resource.

### Limitations of the Original Design

The Partition ID was added as a column to the end of many of the default HAPI FHIR database indexes, meaning that searches could take advantage of partition IDs if they were present, but would also perform well if partitioning was not being used. 

The original partitioning design also introduced the concept of the _Default Partition_, which is a partition used to store infrastructure resources that are not inherently partitionable. The ID fot eh default partition is configurable, but is set by default to _NULL_.

Although this new partitioning mode unlocked a number of great use cases for HAPI FHIR users (including multitenancy, better scalability, data segmentation, etc.) several fundamental limitations in the design have become apparent over time:

* The Partition ID is not actually a part of the row identities (ie. the Primary Key and Foreign Key )

# Partitioned ID Mode

The HAPI FHIR 8.0.0 JPA server introduces a new experimental mode called Partitioned ID Mode.

