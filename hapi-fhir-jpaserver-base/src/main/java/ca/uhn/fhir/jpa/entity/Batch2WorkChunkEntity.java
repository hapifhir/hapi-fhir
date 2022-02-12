package ca.uhn.fhir.jpa.entity;

import ca.uhn.fhir.batch2.model.StatusEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Date;

import static ca.uhn.fhir.batch2.model.JobDefinition.ID_MAX_LENGTH;
import static ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity.STATUS_MAX_LENGTH;

@Entity
@Table(name = "BT2_WORK_CHUNK")
public class Batch2WorkChunkEntity implements Serializable {

	private static final long serialVersionUID = -6202771941965780558L;

	@Id
	@Column(name = "ID", length = ID_MAX_LENGTH)
	private String myId;

	@Version
	@Column(name = "UPDATED", nullable = false)
	private Date myUpdated;

	@Column(name = "DEFINITION_ID", length = ID_MAX_LENGTH, nullable = false)
	private String myJobDefinitionId;

	@Column(name = "DEFINITION_VER", length = ID_MAX_LENGTH, nullable = false)
	private int myJobDefinitionVersion;

	@Column(name = "TGT_STEP_ID", length = ID_MAX_LENGTH, nullable = false)
	private String myTargetStepId;

	@Lob
	@Column(name = "DATA", nullable = false, length = Integer.MAX_VALUE - 1)
	private String mySerializedData;

	@Column(name = "STAT", length = STATUS_MAX_LENGTH, nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusEnum myStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "INSTANCE_ID", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_BT2WC_INSTANCE"))
	private Batch2JobInstanceEntity myInstance;

	@Column(name = "INSTANCE_ID", length = ID_MAX_LENGTH, nullable = false)
	private String myInstanceId;

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

	public String getSerializedData() {
		return mySerializedData;
	}

	public void setSerializedData(String theSerializedData) {
		mySerializedData = theSerializedData;
	}

	public StatusEnum getStatus() {
		return myStatus;
	}

	public void setStatus(StatusEnum theStatus) {
		myStatus = theStatus;
	}

	public void setId(String theId) {
		myId = theId;
	}

	public String getId() {
		return myId;
	}

	public void setInstanceId(String theInstanceId) {
		myInstanceId = theInstanceId;
	}

	public String getInstanceId() {
		return myInstanceId;
	}
}
