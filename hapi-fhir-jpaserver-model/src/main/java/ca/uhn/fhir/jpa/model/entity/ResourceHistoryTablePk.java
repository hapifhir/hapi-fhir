package ca.uhn.fhir.jpa.model.entity;


import ca.uhn.hapi.fhir.sql.hibernatesvc.ConditionalIdProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ResourceHistoryTablePk implements Serializable {

	@Id
	@SequenceGenerator(name = "SEQ_RESOURCE_HISTORY_ID", sequenceName = "SEQ_RESOURCE_HISTORY_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_RESOURCE_HISTORY_ID")
	@Column(name = "PID")
	private Long myId;

	@ConditionalIdProperty
	@Column(name = PartitionablePartitionId.PARTITION_ID)
	private Integer myPartitionIdValue;

	@Override
	public boolean equals(Object theO) {
		if (this == theO) {
			return true;
		}
		if (!(theO instanceof ResourceHistoryTablePk)) {
			return false;
		}
		ResourceHistoryTablePk that = (ResourceHistoryTablePk) theO;
		return Objects.equals(myId, that.myId) && Objects.equals(myPartitionIdValue, that.myPartitionIdValue);
	}

	@Override
	public int hashCode() {
		return Objects.hash(myId, myPartitionIdValue);
	}

	public void setPartitionIdValue(Integer thePartitionIdValue) {
		myPartitionIdValue = thePartitionIdValue;
	}

	public Long getId() {
		return myId;
	}
}
