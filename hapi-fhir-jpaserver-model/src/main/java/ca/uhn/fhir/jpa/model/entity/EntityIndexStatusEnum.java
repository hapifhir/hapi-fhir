package ca.uhn.fhir.jpa.model.entity;

public enum EntityIndexStatusEnum {
	/**
	 * Only indexed in the relational database
	 */
	INDEXED_RDBMS_ONLY,

	/**
	 * Indexed in relational and fulltext databases
	 */
	INDEXED_ALL,

	/**
	 * Indexing failed - This should only happen if a resource is being reindexed and the reindexing fails
	 */
	INDEXING_FAILED;
}
