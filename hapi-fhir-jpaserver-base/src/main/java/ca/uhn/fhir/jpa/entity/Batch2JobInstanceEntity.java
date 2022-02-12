package ca.uhn.fhir.jpa.entity;

import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.StatusEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "BT2_JOB_INSTANCE", indexes = {
	@Index(name = "IDX_BT2JI_CT", columnList = "CREATE_TIME")
})
public class Batch2JobInstanceEntity implements Serializable {

	public static final int STATUS_MAX_LENGTH = 20;
	@Id
	@Column(name = "ID", length = JobDefinition.ID_MAX_LENGTH, nullable = false)
	private String myId;

	@Column(name = "CREATE_TIME", nullable = false)
	private Date myCreateTime;

	@Column(name = "START_TIME", nullable = true)
	private Date myStartTime;

	@Column(name = "END_TIME", nullable = true)
	private Date myEndTime;

	@Column(name = "DEFINITION_ID", length = JobDefinition.ID_MAX_LENGTH, nullable = false)
	private String myDefinitionId;

	@Column(name = "DEFINITION_VER", nullable = false)
	private int myDefinitionVersion;

	@Column(name = "STAT", length = STATUS_MAX_LENGTH, nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusEnum myStatus;

	@Column(name = "PARAMS_JSON", length = 2000, nullable = false)
	private String myParamsJson;

	@Column(name = "CMB_RECS_PROCESSED", nullable = true)
	private Integer myCombinedRecordsProcessed;

	@Column(name = "CMB_RECS_PER_SEC", nullable = true)
	private Double myCombinedRecordsProcessedPerSecond;

	@Column(name = "TOT_ELAPSED_MILLIS", nullable = true)
	private Integer myTotalElapsedMillis;

	@Column(name = "WORK_CHUNKS_PURGED", nullable = true)
	private boolean myWorkChunksPurged;

	@Column(name="PROGRESS_PCT")
	private double myProgress;

	public boolean isWorkChunksPurged() {
		return myWorkChunksPurged;
	}

	public Integer getTotalElapsedMillis() {
		return myTotalElapsedMillis;
	}

	public void setTotalElapsedMillis(Integer theTotalElapsedMillis) {
		myTotalElapsedMillis = theTotalElapsedMillis;
	}

	public Integer getCombinedRecordsProcessed() {
		return myCombinedRecordsProcessed;
	}

	public void setCombinedRecordsProcessed(Integer theCombinedRecordsProcessed) {
		myCombinedRecordsProcessed = theCombinedRecordsProcessed;
	}

	public Double getCombinedRecordsProcessedPerSecond() {
		return myCombinedRecordsProcessedPerSecond;
	}

	public void setCombinedRecordsProcessedPerSecond(Double theCombinedRecordsProcessedPerSecond) {
		myCombinedRecordsProcessedPerSecond = theCombinedRecordsProcessedPerSecond;
	}

	public Date getCreateTime() {
		return myCreateTime;
	}

	public void setCreateTime(Date theCreateTime) {
		myCreateTime = theCreateTime;
	}

	public Date getStartTime() {
		return myStartTime;
	}

	public void setStartTime(Date theStartTime) {
		myStartTime = theStartTime;
	}

	public Date getEndTime() {
		return myEndTime;
	}

	public void setEndTime(Date theEndTime) {
		myEndTime = theEndTime;
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

	public void setParams(String theParams) {
		myParamsJson = theParams;
	}

	public boolean getWorkChunksPurged() {
		return myWorkChunksPurged;
	}

	public void setWorkChunksPurged(boolean theWorkChunksPurged) {
		myWorkChunksPurged = theWorkChunksPurged;
	}

	public double getProgress() {
		return myProgress;
	}

	public void setProgress(double theProgress) {
		myProgress = theProgress;
	}
}
