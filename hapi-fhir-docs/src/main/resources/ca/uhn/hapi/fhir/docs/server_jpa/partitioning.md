# Partitioning

# Limitations

Partitioning is a relatively new feature in HAPI FHIR and has a number of known limitations. If you are intending to use partitioning for achieving a multi-tenant architecture it is important to carefully consider these limitations. 

None of the limitations listed here are considered permanent. Over time the HAPI FHIR team are hoping to make all of these features partition aware.

* **Subscriptions may not be partitioned**: All subscriptions must be placed in the default partition, and subscribers will receive deliveries for any matching resources from all partitions.

* **Conformance resources may not be partitioned**: The following resources must be placed in the default partition, and will be shared for any validation activities across all partitions:
   * StructureDefinition
   * Questionnaire
   * ValueSet
   * CodeSystem
   * ConceptMap
   
* **Bulk Operations are not partition aware**: Bulk export operations will export data across all partitions.
