/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.r5.model.InstantType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static ca.uhn.fhir.rest.api.Constants.UUID_LENGTH;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.left;

/*
 * These classes are no longer needed.
 * Metadata on the job is contained in the job itself
 * (no separate storage required).
 *
 * See the BulkExportAppCtx for job details
 */
@Entity
@Table(
		name = BulkExportJobEntity.HFJ_BLK_EXPORT_JOB,
		uniqueConstraints = {@UniqueConstraint(name = "IDX_BLKEX_JOB_ID", columnNames = "JOB_ID")},
		indexes = {@Index(name = "IDX_BLKEX_EXPTIME", columnList = "EXP_TIME")})
@Deprecated
public class BulkExportJobEntity implements Serializable {

	public static final int REQUEST_LENGTH = 1024;
	public static final int STATUS_MESSAGE_LEN = 500;
	public static final String JOB_ID = "JOB_ID";
	public static final String HFJ_BLK_EXPORT_JOB = "HFJ_BLK_EXPORT_JOB";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_BLKEXJOB_PID")
	@SequenceGenerator(name = "SEQ_BLKEXJOB_PID", sequenceName = "SEQ_BLKEXJOB_PID")
	@Column(name = "PID")
	private Long myId;

	@Column(name = JOB_ID, length = UUID_LENGTH, nullable = false)
	private String myJobId;

	@Column(name = "JOB_STATUS", length = 10, nullable = false)
	private String myStatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_TIME", nullable = false)
	private Date myCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "STATUS_TIME", nullable = false)
	private Date myStatusTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXP_TIME", nullable = true)
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
		myStatusMessage = left(theStatusMessage, STATUS_MESSAGE_LEN);
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
		b.append("created", new InstantType(myCreated).getValueAsString());
		b.append("expiry", new InstantType(myExpiry).getValueAsString());
		b.append("request", myRequest);
		b.append("since", mySince);
		if (isNotBlank(myStatusMessage)) {
			b.append("statusMessage", myStatusMessage);
		}
		return b.toString();
	}

	public String getStatus() {
		return myStatus;
	}

	public void setStatus(String theStatus) {
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
