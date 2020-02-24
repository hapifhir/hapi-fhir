package ca.uhn.fhir.jpa.entity;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.jpa.bulk.BulkJobStatusEnum;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.r5.model.InstantType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Entity
@Table(name = "HFJ_BLK_EXPORT_JOB", uniqueConstraints = {
	@UniqueConstraint(name = "IDX_BLKEX_JOB_ID", columnNames = "JOB_ID")
}, indexes = {
	@Index(name = "IDX_BLKEX_EXPTIME", columnList = "EXP_TIME")
})
public class BulkExportJobEntity implements Serializable {

	public static final int REQUEST_LENGTH = 500;
	public static final int STATUS_MESSAGE_LEN = 500;
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
	@Column(name = "CREATED_TIME", nullable = false)
	private Date myCreated;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "STATUS_TIME", nullable = false)
	private Date myStatusTime;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXP_TIME", nullable = false)
	private Date myExpiry;
	@Column(name = "REQUEST", nullable = false, length = REQUEST_LENGTH)
	private String myRequest;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "myJob", orphanRemoval = false)
	private Collection<BulkExportCollectionEntity> myCollections;
	@Version
	@Column(name = "OPTLOCK", nullable = false)
	private int myVersion;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXP_SINCE", nullable = true)
	private Date mySince;
	@Column(name = "STATUS_MESSAGE", nullable = true, length = STATUS_MESSAGE_LEN)
	private String myStatusMessage;

	public Date getCreated() {
		return myCreated;
	}

	public void setCreated(Date theCreated) {
		myCreated = theCreated;
	}

	public String getStatusMessage() {
		return myStatusMessage;
	}

	public void setStatusMessage(String theStatusMessage) {
		myStatusMessage = theStatusMessage;
	}

	public String getRequest() {
		return myRequest;
	}

	public void setRequest(String theRequest) {
		myRequest = theRequest;
	}

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

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("pid", myId);
		if (isNotBlank(myJobId)) {
			b.append("jobId", myJobId);
		}
		if (myStatus != null) {
			b.append("status", myStatus + " " + new InstantType(myStatusTime).getValueAsString());
		}
		b.append("created", new InstantType(myExpiry).getValueAsString());
		b.append("expiry", new InstantType(myExpiry).getValueAsString());
		b.append("request", myRequest);
		b.append("since", mySince);
		if (isNotBlank(myStatusMessage)) {
			b.append("statusMessage", myStatusMessage);
		}
		return b.toString();
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

	public Date getSince() {
		if (mySince != null) {
			return new Date(mySince.getTime());
		}
		return null;
	}

	public void setSince(Date theSince) {
		mySince = theSince;
	}
}
