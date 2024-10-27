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

import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobFileJson;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import org.hibernate.Length;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import static org.apache.commons.lang3.StringUtils.left;

@Entity
@Table(
		name = "HFJ_BLK_IMPORT_JOBFILE",
		indexes = {@Index(name = "IDX_BLKIM_JOBFILE_JOBID", columnList = "JOB_PID")})
public class BulkImportJobFileEntity implements Serializable {

	public static final int MAX_DESCRIPTION_LENGTH = 500;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_BLKIMJOBFILE_PID")
	@SequenceGenerator(name = "SEQ_BLKIMJOBFILE_PID", sequenceName = "SEQ_BLKIMJOBFILE_PID")
	@Column(name = "PID")
	private Long myId;

	@ManyToOne
	@JoinColumn(
			name = "JOB_PID",
			referencedColumnName = "PID",
			nullable = false,
			foreignKey = @ForeignKey(name = "FK_BLKIMJOBFILE_JOB"))
	private BulkImportJobEntity myJob;

	@Column(name = "FILE_SEQ", nullable = false)
	private int myFileSequence;

	@Column(name = "FILE_DESCRIPTION", nullable = true, length = MAX_DESCRIPTION_LENGTH)
	private String myFileDescription;

	@Lob // TODO: VC column added in 7.2.0 - Remove non-VC column later
	@Column(name = "JOB_CONTENTS", nullable = true)
	private byte[] myContents;

	@Column(name = "JOB_CONTENTS_VC", nullable = true, length = Length.LONG32)
	private String myContentsVc;

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
		if (myContentsVc != null) {
			return myContentsVc;
		} else {
			return new String(myContents, StandardCharsets.UTF_8);
		}
	}

	public void setContents(String theContents) {
		myContentsVc = theContents;
		myContents = null;
	}

	public BulkImportJobFileJson toJson() {
		return new BulkImportJobFileJson().setContents(getContents()).setTenantName(getTenantName());
	}

	public String getTenantName() {
		return myTenantName;
	}

	public void setTenantName(String theTenantName) {
		myTenantName = theTenantName;
	}
}
