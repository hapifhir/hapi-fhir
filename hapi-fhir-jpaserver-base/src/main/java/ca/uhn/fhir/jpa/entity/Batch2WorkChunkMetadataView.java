package ca.uhn.fhir.jpa.entity;


import ca.uhn.fhir.batch2.model.WorkChunkMetadata;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import java.io.Serializable;

import static ca.uhn.fhir.batch2.model.JobDefinition.ID_MAX_LENGTH;

@Entity
@Immutable
@Subselect(
	"SELECT e.id as id, "
	+ " e.seq as seq,"
	+ " e.stat as status, "
	+ " e.instance_id as instance_id, "
	+ " e.definition_id as job_definition_id, "
	+ " e.definition_ver as job_definition_version, "
	+ " e.tgt_step_id as target_step_id "
	+ "FROM BT2_WORK_CHUNK e WHERE (1=0) = false"
)
public class Batch2WorkChunkMetadataView implements Serializable {

	@Id
	@Column(name = "ID", length = ID_MAX_LENGTH)
	private String myId;

	@Column(name = "SEQ", nullable = false)
	private int mySequence;

	@Column(name = "STATUS", length = ID_MAX_LENGTH, nullable = false)
	@Enumerated(EnumType.STRING)
	private WorkChunkStatusEnum myStatus;

	@Column(name = "INSTANCE_ID", length = ID_MAX_LENGTH, nullable = false)
	private String myInstanceId;

	@Column(name = "JOB_DEFINITION_ID", length = ID_MAX_LENGTH, nullable = false)
	private String myJobDefinitionId;

	@Column(name = "JOB_DEFINITION_VERSION", nullable = false)
	private int myJobDefinitionVersion;

	@Column(name = "TARGET_STEP_ID", length = ID_MAX_LENGTH, nullable = false)
	private String myTargetStepId;

	public String getId() {
		return myId;
	}

	public void setId(String theId) {
		myId = theId;
	}

	public int getSequence() {
		return mySequence;
	}

	public void setSequence(int theSequence) {
		mySequence = theSequence;
	}

	public WorkChunkStatusEnum getStatus() {
		return myStatus;
	}

	public void setStatus(WorkChunkStatusEnum theStatus) {
		myStatus = theStatus;
	}

	public String getInstanceId() {
		return myInstanceId;
	}

	public void setInstanceId(String theInstanceId) {
		myInstanceId = theInstanceId;
	}

	public String getJobDefinitionId() {
		return myJobDefinitionId;
	}

	public void setJobDefinitionId(String theJobDefinitionId) {
		myJobDefinitionId = theJobDefinitionId;
	}

	public int getJobDefinitionVersion() {
		return myJobDefinitionVersion;
	}

	public void setJobDefinitionVersion(int theJobDefinitionVersion) {
		myJobDefinitionVersion = theJobDefinitionVersion;
	}

	public String getTargetStepId() {
		return myTargetStepId;
	}

	public void setTargetStepId(String theTargetStepId) {
		myTargetStepId = theTargetStepId;
	}

	public WorkChunkMetadata toChunkMetadata() {
		WorkChunkMetadata metadata = new WorkChunkMetadata();
		metadata.setId(getId());
		metadata.setInstanceId(getInstanceId());
		metadata.setSequence(getSequence());
		metadata.setStatus(getStatus());
		metadata.setJobDefinitionId(getJobDefinitionId());
		metadata.setJobDefinitionVersion(getJobDefinitionVersion());
		metadata.setTargetStepId(getTargetStepId());
		return metadata;
	}
}
