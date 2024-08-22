
Possible solutions:

Use IdClass
:Hibernate 6 IdClass can have no NULL parts!  See AbstractNonAggregatedIdentifierMappingInitializer#233

Use EmbeddedId
:Same problem with EmbeddedId - null partition id means can't query/read!

What next?
- @PartitionKey !!!!
- try user-type wrapper around nullable partition id?
- Try overriding IdClass with persistence.xml?

