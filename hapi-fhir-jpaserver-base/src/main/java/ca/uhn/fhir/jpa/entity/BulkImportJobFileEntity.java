package ca.uhn.fhir.jpa.entity;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.bulk.imp.model.BulkImportJobFileJson;
import ca.uhn.fhir.jpa.bulk.imp.model.JobFileRowProcessingModeEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

@Entity
@Table(name = "HFJ_BLK_IMPORT_JOBFILE", indexes = {
		  @Index(name = "IDX_BLKIM_JOBFILE_JOBID", columnList = "JOB_PID")
})
public class BulkImportJobFileEntity implements Serializable {

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

	@Lob
	@Column(name = "JOB_CONTENTS", nullable = false)
	private byte[] myContents;

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
			.setContents(getContents());
	}
}
