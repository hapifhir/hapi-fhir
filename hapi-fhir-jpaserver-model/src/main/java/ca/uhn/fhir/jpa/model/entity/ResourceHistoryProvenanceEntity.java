/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.rest.api.Constants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable.SOURCE_URI_LENGTH;

/**
 * This entity is deprecated - It stores the source URI and Request ID
 * fields so that they can be indexed and searched discretely. In
 * HAPI FHIR 6.8.0 we added equivalent columns to {@link ResourceHistoryTable}
 * and started populating both those columns and the ones in this table.
 * As of HAPI FHIR 8.0.0 we are no longer using this table unless
 * the "AccessMetaSourceInformationFromProvenanceTable" on JpaStorageSettings
 * is enabled (it's disabled by default). In the future we will remove
 * this table entirely.
 */
@Table(
		name = "HFJ_RES_VER_PROV",
		indexes = {
			@Index(name = "IDX_RESVERPROV_SOURCEURI", columnList = "SOURCE_URI"),
			@Index(name = "IDX_RESVERPROV_REQUESTID", columnList = "REQUEST_ID"),
			@Index(name = "IDX_RESVERPROV_RES_PID", columnList = "RES_PID")
		})
@Entity
@IdClass(IdAndPartitionId.class)
public class ResourceHistoryProvenanceEntity extends BasePartitionable {

	@Id
	@Column(name = "RES_VER_PID")
	private Long myId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(
			value = {
				@JoinColumn(
						name = "RES_PID",
						referencedColumnName = "RES_ID",
						insertable = false,
						updatable = false,
						nullable = false),
				@JoinColumn(
						name = "PARTITION_ID",
						referencedColumnName = "PARTITION_ID",
						insertable = false,
						updatable = false,
						nullable = false)
			},
			foreignKey = @ForeignKey(name = "FK_RESVERPROV_RES_PID"))
	private ResourceTable myResourceTable;

	@Column(name = "RES_PID", nullable = false)
	private Long myResourceId;

	@Column(name = "SOURCE_URI", length = SOURCE_URI_LENGTH, nullable = true)
	private String mySourceUri;

	@Column(name = "REQUEST_ID", length = Constants.REQUEST_ID_LENGTH, nullable = true)
	private String myRequestId;

	/**
	 * Constructor
	 */
	public ResourceHistoryProvenanceEntity() {
		super();
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("resourceId", myResourceTable.getId());
		b.append("sourceUri", mySourceUri);
		b.append("requestId", myRequestId);
		return b.toString();
	}

	public void setResourceTable(ResourceTable theResourceTable) {
		myResourceTable = theResourceTable;
		myResourceId = theResourceTable.getId().getId();
		myPartitionIdValue = theResourceTable.getPartitionId().getPartitionId();
	}

	public void setResourceHistoryTable(ResourceHistoryTable theResourceHistoryTable) {
		myId = theResourceHistoryTable.getId().getId();
		assert myId != null;
	}

	public String getSourceUri() {
		return mySourceUri;
	}

	public void setSourceUri(String theSourceUri) {
		mySourceUri = theSourceUri;
	}

	public String getRequestId() {
		return myRequestId;
	}

	public void setRequestId(String theRequestId) {
		myRequestId = theRequestId;
	}

	public Long getId() {
		return myId;
	}
}
