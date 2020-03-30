package ca.uhn.fhir.jpa.model.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public class BasePartitionable implements Serializable {

	@Embedded
	private PartitionId myPartitionId;

	/**
	 * This is here to support queries only, do not set this field directly
	 */
	@SuppressWarnings("unused")
	@Column(name = PartitionId.PARTITION_ID, insertable = false, updatable = false, nullable = true)
	private Integer myPartitionIdValue;

	public PartitionId getPartitionId() {
		return myPartitionId;
	}

	public void setPartitionId(PartitionId thePartitionId) {
		myPartitionId = thePartitionId;
	}


}
