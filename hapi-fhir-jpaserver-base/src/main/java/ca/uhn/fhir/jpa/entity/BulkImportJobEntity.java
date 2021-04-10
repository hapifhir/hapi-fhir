package ca.uhn.fhir.jpa.entity;

import ca.uhn.fhir.jpa.bulk.imp.model.BulkImportJobStatusEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "HFJ_BLK_IMPORT_JOB", uniqueConstraints = {
		  @UniqueConstraint(name = "IDX_BLKIM_JOB_ID", columnNames = "JOB_ID")
})
public class BulkImportJobEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_BLKIMJOB_PID")
	@SequenceGenerator(name = "SEQ_BLKIMJOB_PID", sequenceName = "SEQ_BLKIMJOB_PID")
	@Column(name = "PID")
	private Long myId;

	@Column(name = "JOB_ID", length = Search.UUID_COLUMN_LENGTH, nullable = false)
	private String myJobId;

	@Enumerated(EnumType.STRING)
	@Column(name = "JOB_STATUS", length = 10, nullable = false)
	private BulkImportJobStatusEnum myStatus;

	@Version
	@Column(name = "OPTLOCK", nullable = false)
	private int myVersion;

	@Column(name = "FILE_COUNT", nullable = false)
	private int myFileCount;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "STATUS_TIME", nullable = false)
	private Date myStatusTime;
	@Column(name = "STATUS_MESSAGE", nullable = true, length = BulkExportJobEntity.STATUS_MESSAGE_LEN)
	private String myStatusMessage;

	public Date getStatusTime() {
		return myStatusTime;
	}

	public void setStatusTime(Date theStatusTime) {
		myStatusTime = theStatusTime;
	}

	public int getFileCount() {
		return myFileCount;
	}

	public void setFileCount(int theFileCount) {
		myFileCount = theFileCount;
	}

	public String getJobId() {
		return myJobId;
	}

	public void setJobId(String theJobId) {
		myJobId = theJobId;
	}

	public BulkImportJobStatusEnum getStatus() {
		return myStatus;
	}

	/**
	 * Sets the status, updates the status time, and clears the status message
	 */
	public void setStatus(BulkImportJobStatusEnum theStatus) {
		if (myStatus != theStatus) {
			myStatus = theStatus;
			setStatusTime(new Date());
			setStatusMessage(null);
		}
	}

	public String getStatusMessage() {
		return myStatusMessage;
	}

	public void setStatusMessage(String theStatusMessage) {
		myStatusMessage = theStatusMessage;
	}
}
