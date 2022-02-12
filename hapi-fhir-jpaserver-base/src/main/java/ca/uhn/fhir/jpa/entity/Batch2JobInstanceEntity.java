package ca.uhn.fhir.jpa.entity;

import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.StatusEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "BT2_JOB_INSTANCE")
public class Batch2JobInstanceEntity implements Serializable {

	public static final int STATUS_MAX_LENGTH = 20;
	@Id
	@Column(name = "ID", length = JobDefinition.ID_MAX_LENGTH, nullable = false)
	private String myId;

	@Version
	@Column(name = "UPDATED", nullable = false)
	private Date myUpdated;

	@Column(name = "DEFINITION_ID", length = JobDefinition.ID_MAX_LENGTH, nullable = false)
	private String myDefinitionId;

	@Column(name = "DEFINITION_VER", nullable = false)
	private int myDefinitionVersion;

	@Column(name = "STAT", length = STATUS_MAX_LENGTH, nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusEnum myStatus;

	@Column(name = "PARAMS_JSON", length = 2000, nullable = false)
	private String myParamsJson;

	public void setParams(String theParams) {
		myParamsJson = theParams;
	}

	public String getId() {
		return myId;
	}

	public void setId(String theId) {
		myId = theId;
	}

	public String getDefinitionId() {
		return myDefinitionId;
	}

	public void setDefinitionId(String theDefinitionId) {
		myDefinitionId = theDefinitionId;
	}

	public int getDefinitionVersion() {
		return myDefinitionVersion;
	}

	public void setDefinitionVersion(int theDefinitionVersion) {
		myDefinitionVersion = theDefinitionVersion;
	}

	public StatusEnum getStatus() {
		return myStatus;
	}

	public void setStatus(StatusEnum theStatus) {
		myStatus = theStatus;
	}

	public String getParams() {
		return myParamsJson;
	}
}
