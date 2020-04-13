package ca.uhn.fhir.jpa.partition;

import ca.uhn.fhir.jpa.entity.PartitionEntity;

public interface IPartitionConfigSvc {

	/**
	 * This is mostly here for unit test purposes. Regular code is not expected to call this method directly.
	 */
	void start();

	/**
	 * @throws IllegalArgumentException If the name is not known
	 */
	PartitionEntity getPartitionByName(String theName) throws IllegalArgumentException;

	PartitionEntity getPartitionById(Integer theId);

	void clearCaches();

	PartitionEntity createPartition(PartitionEntity thePartition);

	PartitionEntity updatePartition(PartitionEntity thePartition);

	void deletePartition(Integer thePartitionId);

}
