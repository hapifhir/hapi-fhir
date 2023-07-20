/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.model.entity.ResourceTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

/*
 * These classes are no longer needed.
 * Metadata on the job is contained in the job itself
 * (no separate storage required).
 *
 * See the BulkExportAppCtx for job details
 */
@Entity
@Table(name = "HFJ_BLK_EXPORT_COLLECTION")
@Deprecated
public class BulkExportCollectionEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_BLKEXCOL_PID")
	@SequenceGenerator(name = "SEQ_BLKEXCOL_PID", sequenceName = "SEQ_BLKEXCOL_PID")
	@Column(name = "PID")
	private Long myId;

	@ManyToOne()
	@JoinColumn(
			name = "JOB_PID",
			referencedColumnName = "PID",
			nullable = false,
			foreignKey = @ForeignKey(name = "FK_BLKEXCOL_JOB"))
	private BulkExportJobEntity myJob;

	@Column(name = "RES_TYPE", length = ResourceTable.RESTYPE_LEN, nullable = false)
	private String myResourceType;

	@Column(name = "TYPE_FILTER", length = 1000, nullable = true)
	private String myFilter;

	@Version
	@Column(name = "OPTLOCK", nullable = false)
	private int myVersion;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "myCollection")
	private Collection<BulkExportCollectionFileEntity> myFiles;

	public void setJob(BulkExportJobEntity theJob) {
		myJob = theJob;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	public String getFilter() {
		return myFilter;
	}

	public void setFilter(String theFilter) {
		myFilter = theFilter;
	}

	public int getVersion() {
		return myVersion;
	}

	public void setVersion(int theVersion) {
		myVersion = theVersion;
	}

	public Collection<BulkExportCollectionFileEntity> getFiles() {
		if (myFiles == null) {
			myFiles = new ArrayList<>();
		}
		return myFiles;
	}

	public Long getId() {
		return myId;
	}
}
