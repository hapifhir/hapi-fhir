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

import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobJson;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobStatusEnum;
import ca.uhn.fhir.jpa.bulk.imprt.model.JobFileRowProcessingModeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;

import java.io.Serializable;
import java.util.Date;

import static ca.uhn.fhir.rest.api.Constants.UUID_LENGTH;
import static org.apache.commons.lang3.StringUtils.left;

@Entity
@Table(
		name = BulkImportJobEntity.HFJ_BLK_IMPORT_JOB,
		uniqueConstraints = {@UniqueConstraint(name = "IDX_BLKIM_JOB_ID", columnNames = "JOB_ID")})
public class BulkImportJobEntity implements Serializable {

	public static final String HFJ_BLK_IMPORT_JOB = "HFJ_BLK_IMPORT_JOB";
	public static final String JOB_ID = "JOB_ID";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_BLKIMJOB_PID")
	@SequenceGenerator(name = "SEQ_BLKIMJOB_PID", sequenceName = "SEQ_BLKIMJOB_PID")
	@Column(name = "PID")
	private Long myId;

	@Column(name = JOB_ID, length = UUID_LENGTH, nullable = false, updatable = false)
	private String myJobId;

	@Column(name = "JOB_DESC", nullable = true, length = BulkExportJobEntity.STATUS_MESSAGE_LEN)
	private String myJobDescription;

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

	@Column(name = "ROW_PROCESSING_MODE", length = 20, nullable = false, updatable = false)
	@Enumerated(EnumType.STRING)
	private JobFileRowProcessingModeEnum myRowProcessingMode;

	@Column(name = "BATCH_SIZE", nullable = false, updatable = false)
	private int myBatchSize;

	public String getJobDescription() {
		return myJobDescription;
	}

	public void setJobDescription(String theJobDescription) {
		myJobDescription = left(theJobDescription, BulkExportJobEntity.STATUS_MESSAGE_LEN);
	}

	public JobFileRowProcessingModeEnum getRowProcessingMode() {
		return myRowProcessingMode;
	}

	public void setRowProcessingMode(JobFileRowProcessingModeEnum theRowProcessingMode) {
		myRowProcessingMode = theRowProcessingMode;
	}

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
		myStatusMessage = left(theStatusMessage, BulkExportJobEntity.STATUS_MESSAGE_LEN);
	}

	public BulkImportJobJson toJson() {
		return new BulkImportJobJson()
				.setProcessingMode(getRowProcessingMode())
				.setFileCount(getFileCount())
				.setJobDescription(getJobDescription());
	}

	public int getBatchSize() {
		return myBatchSize;
	}

	public void setBatchSize(int theBatchSize) {
		myBatchSize = theBatchSize;
	}
}
