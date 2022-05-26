package ca.uhn.fhir.jpa.model.entity;

/*
 * #%L
 * HAPI FHIR JPA Model
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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
	name = "HFJ_RES_TAG",
   indexes = {
		@Index(name = "IDX_RES_TAG_RES_TAG", columnList = "RES_ID, TAG_ID, PARTITION_ID"),
		@Index(name = "IDX_RES_TAG_TAG_RES", columnList = "TAG_ID, RES_ID, PARTITION_ID")
	},
	uniqueConstraints = { @UniqueConstraint(name = "IDX_RESTAG_TAGID", columnNames = {"RES_ID", "TAG_ID"})
})
public class ResourceTag extends BaseTag {

	private static final long serialVersionUID = 1L;

	@SequenceGenerator(name = "SEQ_RESTAG_ID", sequenceName = "SEQ_RESTAG_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_RESTAG_ID")
	@Id
	@Column(name = "PID")
	private Long myId;

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "RES_ID", referencedColumnName = "RES_ID", foreignKey = @ForeignKey(name = "FK_RESTAG_RESOURCE"))
	private ResourceTable myResource;

	@Column(name = "RES_TYPE", length = ResourceTable.RESTYPE_LEN, nullable = false)
	private String myResourceType;

	@Column(name = "RES_ID", insertable = false, updatable = false)
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
	public ResourceTag(ResourceTable theResourceTable, TagDefinition theTag, PartitionablePartitionId theRequestPartitionId) {
		setTag(theTag);
		setResource(theResourceTable);
		setResourceId(theResourceTable.getId());
		setResourceType(theResourceTable.getResourceType());
		setPartitionId(theRequestPartitionId);
	}

	public Long getResourceId() {
		return myResourceId;
	}

	public void setResourceId(Long theResourceId) {
		myResourceId = theResourceId;
	}

	public ResourceTable getResource() {
		return myResource;
	}

	public void setResource(ResourceTable theResource) {
		myResource = theResource;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
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
		if (getPartitionId() != null) {
			b.append("partition", getPartitionId().getPartitionId());
		}
		b.append("resId", getResourceId());
		b.append("tag", getTag().getId());
		return b.build();
	}

}
