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

import ca.uhn.fhir.jpa.model.entity.ForcedId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "HFJ_BLK_EXPORT_COLFILE")
public class BulkExportCollectionFileEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_BLKEXCOLFILE_PID")
	@SequenceGenerator(name = "SEQ_BLKEXCOLFILE_PID", sequenceName = "SEQ_BLKEXCOLFILE_PID")
	@Column(name = "PID")
	private Long myId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COLLECTION_PID", referencedColumnName = "PID", nullable = false, foreignKey = @ForeignKey(name="FK_BLKEXCOLFILE_COLLECT"))
	private BulkExportCollectionEntity myCollection;

	@Column(name = "RES_ID", length = ForcedId.MAX_FORCED_ID_LENGTH, nullable = false)
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
