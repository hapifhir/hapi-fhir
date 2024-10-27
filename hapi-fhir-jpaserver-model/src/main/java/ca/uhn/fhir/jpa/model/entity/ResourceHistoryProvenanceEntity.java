/*-
 * #%L
 * HAPI FHIR JPA Model
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
package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.rest.api.Constants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable.SOURCE_URI_LENGTH;

@Table(
		name = "HFJ_RES_VER_PROV",
		indexes = {
			@Index(name = "IDX_RESVERPROV_SOURCEURI", columnList = "SOURCE_URI"),
			@Index(name = "IDX_RESVERPROV_REQUESTID", columnList = "REQUEST_ID"),
			@Index(name = "IDX_RESVERPROV_RES_PID", columnList = "RES_PID")
		})
@Entity
public class ResourceHistoryProvenanceEntity extends BasePartitionable {

	@Id
	@Column(name = "RES_VER_PID")
	private Long myId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "RES_VER_PID",
			referencedColumnName = "PID",
			foreignKey = @ForeignKey(name = "FK_RESVERPROV_RESVER_PID"),
			nullable = false,
			insertable = false,
			updatable = false)
	@MapsId
	private ResourceHistoryTable myResourceHistoryTable;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "RES_PID",
			referencedColumnName = "RES_ID",
			foreignKey = @ForeignKey(name = "FK_RESVERPROV_RES_PID"),
			nullable = false)
	private ResourceTable myResourceTable;

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
	}

	public void setResourceHistoryTable(ResourceHistoryTable theResourceHistoryTable) {
		myResourceHistoryTable = theResourceHistoryTable;
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
