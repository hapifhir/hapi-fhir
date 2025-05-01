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
import jakarta.persistence.FetchType;
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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(
		name = "HFJ_RES_TAG",
		indexes = {
			@Index(name = "IDX_RES_TAG_RES_TAG", columnList = "RES_ID, TAG_ID, PARTITION_ID"),
			@Index(name = "IDX_RES_TAG_TAG_RES", columnList = "TAG_ID, RES_ID, PARTITION_ID")
		},
		uniqueConstraints = {
			@UniqueConstraint(
					name = "IDX_RESTAG_TAGID",
					columnNames = {"PARTITION_ID", "RES_ID", "TAG_ID"})
		})
@IdClass(IdAndPartitionId.class)
public class ResourceTag extends BaseTag {

	private static final long serialVersionUID = 1L;

	@GenericGenerator(name = "SEQ_RESTAG_ID", type = ca.uhn.fhir.jpa.model.dialect.HapiSequenceStyleGenerator.class)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_RESTAG_ID")
	@Id
	@Column(name = "PID")
	private Long myId;

	@ManyToOne(
			cascade = {},
			fetch = FetchType.LAZY)
	@JoinColumns(
			value = {
				@JoinColumn(
						name = "RES_ID",
						referencedColumnName = "RES_ID",
						insertable = false,
						updatable = false,
						nullable = true),
				@JoinColumn(
						name = "PARTITION_ID",
						referencedColumnName = "PARTITION_ID",
						insertable = false,
						updatable = false,
						nullable = true)
			},
			foreignKey = @ForeignKey(name = "FK_RESTAG_RESOURCE"))
	private ResourceTable myResource;

	@Column(name = "RES_TYPE", length = ResourceTable.RESTYPE_LEN, nullable = false)
	private String myResourceType;

	@Column(name = "RES_ID", updatable = false, nullable = true)
	private Long myResourceId;

	/**
	 * Constructor
	 */
	public ResourceTag() {
		super();
	}

	/**
	 * Constructor
	 */
	public ResourceTag(
			ResourceTable theResourceTable, TagDefinition theTag, PartitionablePartitionId theRequestPartitionId) {
		setTag(theTag);
		setResource(theResourceTable);
		setResourceId(theResourceTable.getId().getId());
		setResourceType(theResourceTable.getResourceType());
		setPartitionId(theRequestPartitionId);
	}

	public JpaPid getResourceId() {
		return JpaPid.fromId(myResourceId, myPartitionIdValue);
	}

	public void setResourceId(Long theResourceId) {
		myResourceId = theResourceId;
	}

	public ResourceTable getResource() {
		return myResource;
	}

	public void setResource(ResourceTable theResource) {
		myResource = theResource;
		myResourceId = theResource.getId().getId();
		myPartitionIdValue = theResource.getPartitionId().getPartitionId();
	}

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	@PrePersist
	public void prePersist() {
		myResourceId = myResource.getId().getId();
		myPartitionIdValue = myResource.getId().getPartitionId();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ResourceTag)) {
			return false;
		}
		ResourceTag other = (ResourceTag) obj;
		EqualsBuilder b = new EqualsBuilder();
		b.append(getResourceId(), other.getResourceId());
		b.append(getTag(), other.getTag());
		return b.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(getResourceId());
		b.append(getTag());
		return b.toHashCode();
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		if (getPartitionId().getPartitionId() != null) {
			b.append("partition", getPartitionId().getPartitionId());
		}
		b.append("resId", getResourceId());
		b.append("tag", getTagId());
		return b.build();
	}
}
