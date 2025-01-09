# Database Partition Mode Introduction

HAPI FHIR 5.0.0 introduced partitioning to the JPA server. In the current partitioning design, each resource-specific table (i.e. each table in the database schema where each row pertains to a single FHIR resource) has two new columns: `PARTITION_ID`, and `PARTITION_DATE`.

The `PARTITION_DATE` column may be set by user code, but is otherwise untouched by HAPI FHIR. The `PARTITION_ID` column is the main partitioning mechanism. It uses an arbitrary Integer value to denote the partition associated with an individual resource.

<a name="original-limitations"/>

### Limitations of the Original Design

When Partition support was added to HAPI FHIR, it was designed with a goal of not breaking existing deployments of HAPI FHIR. This meant that tables could not change in ways that were not backwards compatible, and columns could not be added if they required non-null values.

This decision still led to a useful partitioning capability, and HAPI FHIR users have used it to build useful solutions, but it has also limited the ability of the system to truly leverage underlying partitioning/sharding capabilities provided by many RDBMS vendors. 

In the legacy partitioning scheme, the *Partition ID* was added as a column to the end of many of the default HAPI FHIR database indexes, meaning that searches could take advantage of partition IDs if they were present, but would also perform well if partitioning was not being used. The original partitioning design also introduced the concept of the _Default Partition_, which is a partition used to store infrastructure resources that are not inherently partitionable. The ID for the default partition is configurable, but is set by default to _NULL_.

Although this new partitioning mode unlocked a number of great use cases for HAPI FHIR users (including multitenancy, better scalability, data segmentation, etc.) several fundamental limitations in the design have become apparent over time:

* The Partition ID is not actually a part of the row identities (ie. the Primary Key and Foreign Key constraints) meaning that some RDBMS platforms could not use it as a partition key without significant manual schema modifications.
* The Partition ID is not included in SQL ON statements when performing table joins, making these joins inefficient for partitioned databases.

# Database Partitioning

The HAPI FHIR 8.0.0 JPA server introduces a new experimental mode called Database Partitioning Mode. This new mode addresses the [limitations](#original-limitations) described above by making changes to the JPA database schema and the SQL which interacts with it that are incompatible with the existing schema.

In Database Partitioning Mode:

* All tables containing resource-specific data (ie. each row contains information about a single resource) will have the partition ID incorporated in their primary key.
* All SQL emitted by HAPI FHIR JPA will include a partition ID in the _WHERE_ clause for selects against partitioned tables (where possible) and in the _ON_ clause for joins across partitioned tables (always).
* The default partition must be set to a non-null value (e.g. 0)

# Initializing and Upgrading your Database

On first startup (assuming an empty database schema) the system will automatically create all needed tables. The system must be configured to be in Database Partition Mode before this initial startup in order for the system to select the correct DDL to use when initializing the database.

It is not yet possible to migrate an existing database schema between these two modes (this may come in a future release), and the system will detect and fail to start if the server is in the incorrect mode for an existing database schema.

The CLI [Migrator](../server_jpa/upgrading.md) can be used to upgrade the database schema when upgrading HAPI FHIR. It must be configured to use the Database Partition Mode schema by using the `--flags` argument as [described here](../server_jpa/upgrading.md#database-partition-mode).

