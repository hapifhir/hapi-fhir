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

import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.io.Serializable;

/*
 * These classes are no longer needed.
 * Metadata on the job is contained in the job itself
 * (no separate storage required).
 *
 * See the BulkExportAppCtx for job details
 */
@Entity
@Table(name = "HFJ_BLK_EXPORT_COLFILE")
@Deprecated
public class BulkExportCollectionFileEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_BLKEXCOLFILE_PID")
	@SequenceGenerator(name = "SEQ_BLKEXCOLFILE_PID", sequenceName = "SEQ_BLKEXCOLFILE_PID")
	@Column(name = "PID")
	private Long myId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "COLLECTION_PID",
			referencedColumnName = "PID",
			nullable = false,
			foreignKey = @ForeignKey(name = "FK_BLKEXCOLFILE_COLLECT"))
	private BulkExportCollectionEntity myCollection;

	@Column(name = "RES_ID", length = ResourceTable.MAX_FORCED_ID_LENGTH, nullable = false)
	private String myResourceId;

	public void setCollection(BulkExportCollectionEntity theCollection) {
		myCollection = theCollection;
	}

	public void setResource(String theResourceId) {
		myResourceId = theResourceId;
	}

	public String getResourceId() {
		return myResourceId;
	}

	public Long getId() {
		return myId;
	}
}
