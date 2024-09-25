package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.rest.api.server.storage.IResourceVersionPersistentId;
import ca.uhn.hapi.fhir.sql.hibernatesvc.ConditionalIdProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ResourceHistoryTablePk implements IResourceVersionPersistentId, Serializable {

	@SequenceGenerator(name = "SEQ_RESOURCE_HISTORY_ID", sequenceName = "SEQ_RESOURCE_HISTORY_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_RESOURCE_HISTORY_ID")
	@Column(name = "PID")
	private Long myVersionId;

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
		return Objects.equals(myVersionId, that.myVersionId)
				&& Objects.equals(myPartitionIdValue, that.myPartitionIdValue);
	}

	@Override
	public int hashCode() {
		return Objects.hash(myVersionId, myPartitionIdValue);
	}

	public void setPartitionIdValue(Integer thePartitionIdValue) {
		myPartitionIdValue = thePartitionIdValue;
	}

	public Long getId() {
		return myVersionId;
	}

	public Integer getPartitionId() {
		return myPartitionIdValue;
	}

	public IdAndPartitionId asIdAndPartitionId() {
		return new IdAndPartitionId(getId(), getPartitionId());
	}
}
