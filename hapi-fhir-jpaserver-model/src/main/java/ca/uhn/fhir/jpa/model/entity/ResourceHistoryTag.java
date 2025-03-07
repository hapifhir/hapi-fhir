/*
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

import ca.uhn.fhir.jpa.model.dao.JpaPid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

@Entity
@Table(
		name = "HFJ_HISTORY_TAG",
		uniqueConstraints = {
			@UniqueConstraint(
					name = "IDX_RESHISTTAG_TAGID",
					columnNames = {"PARTITION_ID", "RES_VER_PID", "TAG_ID"}),
		},
		indexes = {@Index(name = "IDX_RESHISTTAG_RESID", columnList = "RES_ID")})
@IdClass(IdAndPartitionId.class)
public class ResourceHistoryTag extends BaseTag implements Serializable {

	private static final long serialVersionUID = 1L;

	@GenericGenerator(name = "SEQ_HISTORYTAG_ID", type = ca.uhn.fhir.jpa.model.dialect.HapiSequenceStyleGenerator.class)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_HISTORYTAG_ID")
	@Id
	@Column(name = "PID")
	private Long myId;

	@ManyToOne()
	@JoinColumns(
			value = {
				@JoinColumn(
						name = "RES_VER_PID",
						referencedColumnName = "PID",
						nullable = false,
						insertable = false,
						updatable = false),
				@JoinColumn(
						name = "PARTITION_ID",
						referencedColumnName = "PARTITION_ID",
						nullable = false,
						insertable = false,
						updatable = false)
			},
			foreignKey = @ForeignKey(name = "FK_HISTORYTAG_HISTORY"))
	private ResourceHistoryTable myResourceHistory;

	@Column(name = "RES_VER_PID", updatable = false, nullable = false)
	private Long myResourceHistoryPid;

	@Column(name = "RES_TYPE", length = ResourceTable.RESTYPE_LEN, nullable = false)
	private String myResourceType;

	@Column(name = "RES_ID", nullable = false)
	private Long myResourceId;

	/**
	 * Constructor
	 */
	public ResourceHistoryTag() {
		super();
	}

	/**
	 * Constructor
	 */
	public ResourceHistoryTag(
			ResourceHistoryTable theResourceHistoryTable,
			TagDefinition theTag,
			PartitionablePartitionId theRequestPartitionId) {
		this();
		setTag(theTag);
		setResource(theResourceHistoryTable);
		setResourceId(theResourceHistoryTable.getResourceId().getId());
		setResourceType(theResourceHistoryTable.getResourceType());
		setPartitionId(theRequestPartitionId);
	}

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	public Long getResourceId() {
		return myResourceId;
	}

	public void setResourceId(Long theResourceId) {
		myResourceId = theResourceId;
	}

	public ResourceHistoryTable getResourceHistory() {
		return myResourceHistory;
	}

	public void setResource(ResourceHistoryTable theResourceHistory) {
		myResourceHistory = theResourceHistory;
		myResourceHistoryPid = theResourceHistory.getId().getId();
		myPartitionIdValue = theResourceHistory.getResourceId().getPartitionId();
	}

	@PrePersist
	public void prePersist() {
		myResourceHistoryPid = myResourceHistory.getId().getId();
		myPartitionIdValue = myResourceHistory.getResourceId().getPartitionId();
	}

	public Long getId() {
		return myId;
	}

	public JpaPid getResourcePid() {
		return JpaPid.fromId(myResourceId, myPartitionIdValue);
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("id", getId());
		if (getPartitionId().getPartitionId() != null) {
			b.append("partId", getPartitionId().getPartitionId());
		}
		b.append("versionId", myResourceHistoryPid);
		b.append("resId", getResourceId());
		b.append("tag", getTagId());
		return b.build();
	}
}
