package ca.uhn.fhir.jpa.entity;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobFileJson;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import static org.apache.commons.lang3.StringUtils.left;

@Entity
@Table(name = "HFJ_BLK_IMPORT_JOBFILE", indexes = {
	@Index(name = "IDX_BLKIM_JOBFILE_JOBID", columnList = "JOB_PID")
})
public class BulkImportJobFileEntity implements Serializable {

	public static final int MAX_DESCRIPTION_LENGTH = 500;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_BLKIMJOBFILE_PID")
	@SequenceGenerator(name = "SEQ_BLKIMJOBFILE_PID", sequenceName = "SEQ_BLKIMJOBFILE_PID")
	@Column(name = "PID")
	private Long myId;

	@ManyToOne
	@JoinColumn(name = "JOB_PID", referencedColumnName = "PID", nullable = false, foreignKey = @ForeignKey(name = "FK_BLKIMJOBFILE_JOB"))
	private BulkImportJobEntity myJob;

	@Column(name = "FILE_SEQ", nullable = false)
	private int myFileSequence;
	@Column(name = "FILE_DESCRIPTION", nullable = true, length = MAX_DESCRIPTION_LENGTH)
	private String myFileDescription;
	@Lob
	@Column(name = "JOB_CONTENTS", nullable = false)
	private byte[] myContents;
	@Column(name = "TENANT_NAME", nullable = true, length = PartitionEntity.MAX_NAME_LENGTH)
	private String myTenantName;

	public String getFileDescription() {
		return myFileDescription;
	}

	public void setFileDescription(String theFileDescription) {
		myFileDescription = left(theFileDescription, MAX_DESCRIPTION_LENGTH);
	}

	public BulkImportJobEntity getJob() {
		return myJob;
	}

	public void setJob(BulkImportJobEntity theJob) {
		myJob = theJob;
	}

	public int getFileSequence() {
		return myFileSequence;
	}

	public void setFileSequence(int theFileSequence) {
		myFileSequence = theFileSequence;
	}

	public String getContents() {
		return new String(myContents, StandardCharsets.UTF_8);
	}

	public void setContents(String theContents) {
		myContents = theContents.getBytes(StandardCharsets.UTF_8);
	}


	public BulkImportJobFileJson toJson() {
		return new BulkImportJobFileJson()
			.setContents(getContents())
			.setTenantName(getTenantName());
	}

	public String getTenantName() {
		return myTenantName;
	}

	public void setTenantName(String theTenantName) {
		myTenantName = theTenantName;
	}
}
