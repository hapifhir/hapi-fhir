package ca.uhn.fhir.jpa.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "HFJ_BLK_EXPORT_JOB", uniqueConstraints = {
	@UniqueConstraint(name = "IDX_BLKEX_JOB_ID", columnNames = "JOB_ID")
})
public class BulkExportJobEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_BLKEXJOB_PID")
	@SequenceGenerator(name = "SEQ_BLKEXJOB_PID", sequenceName = "SEQ_BLKEXJOB_PID")
	@Column(name = "PID")
	private Long myId;

	@Column(name = "JOB_ID", length = Search.UUID_COLUMN_LENGTH, nullable = false)
	private String myJobId;

	@Enumerated(EnumType.STRING)
	@Column(name = "JOB_STATUS", length = 10, nullable = false)
	private StatusEnum myStatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "STATUS_TIME", nullable = false)
	private Date myStatusTime;
	@OneToMany(fetch = FetchType.LAZY)
	private Collection<BulkExportCollectionEntity> myCollections;
	@Version
	@Column(name = "OPTLOCK", nullable = false)
	private int myVersion;

	public Collection<BulkExportCollectionEntity> getCollections() {
		return myCollections;
	}

	public String getJobId() {
		return myJobId;
	}

	public void setJobId(String theJobId) {
		myJobId = theJobId;
	}

	public StatusEnum getStatus() {
		return myStatus;
	}

	public void setStatus(StatusEnum theStatus) {
		myStatus = theStatus;
	}

	public Date getStatusTime() {
		return myStatusTime;
	}

	public void setStatusTime(Date theStatusTime) {
		myStatusTime = theStatusTime;
	}

	public int getVersion() {
		return myVersion;
	}

	public void setVersion(int theVersion) {
		myVersion = theVersion;
	}

	public Long getId() {
		return myId;
	}

	public enum StatusEnum {

		SUBMITTED,
		BUILDING,
		COMPLETE,
		ERROR

	}

}
