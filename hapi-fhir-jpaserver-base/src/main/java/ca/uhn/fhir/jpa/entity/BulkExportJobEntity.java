package ca.uhn.fhir.jpa.entity;

import ca.uhn.fhir.jpa.bulk.BulkJobStatusEnum;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "HFJ_BLK_EXPORT_JOB", uniqueConstraints = {
	@UniqueConstraint(name = "IDX_BLKEX_JOB_ID", columnNames = "JOB_ID")
}, indexes = {
	@Index(name = "IDX_BLKEX_EXPTIME", columnList = "EXP_TIME")
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
	private BulkJobStatusEnum myStatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "STATUS_TIME", nullable = false)
	private Date myStatusTime;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXP_TIME", nullable = false)
	private Date myExpiry;
	@OneToMany(fetch = FetchType.LAZY)
	private Collection<BulkExportCollectionEntity> myCollections;
	@Version
	@Column(name = "OPTLOCK", nullable = false)
	private int myVersion;

	public void setExpiry(Date theExpiry) {
		myExpiry = theExpiry;
	}

	public Collection<BulkExportCollectionEntity> getCollections() {
		if (myCollections == null) {
			myCollections = new ArrayList<>();
		}
		return myCollections;
	}

	public String getJobId() {
		return myJobId;
	}

	public void setJobId(String theJobId) {
		myJobId = theJobId;
	}

	public BulkJobStatusEnum getStatus() {
		return myStatus;
	}

	public void setStatus(BulkJobStatusEnum theStatus) {
		if (myStatus != theStatus) {
			myStatusTime = new Date();
			myStatus = theStatus;
		}
	}

	public Date getStatusTime() {
		return myStatusTime;
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

}
