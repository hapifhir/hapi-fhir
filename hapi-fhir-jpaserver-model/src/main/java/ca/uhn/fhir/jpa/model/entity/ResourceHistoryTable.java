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

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.OptimisticLock;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = ResourceHistoryTable.HFJ_RES_VER, uniqueConstraints = {
	@UniqueConstraint(name = ResourceHistoryTable.IDX_RESVER_ID_VER, columnNames = {"RES_ID", "RES_VER"})
}, indexes = {
	@Index(name = "IDX_RESVER_TYPE_DATE", columnList = "RES_TYPE,RES_UPDATED"),
	@Index(name = "IDX_RESVER_ID_DATE", columnList = "RES_ID,RES_UPDATED"),
	@Index(name = "IDX_RESVER_DATE", columnList = "RES_UPDATED")
})
public class ResourceHistoryTable extends BaseHasResource implements Serializable {
	public static final String IDX_RESVER_ID_VER = "IDX_RESVER_ID_VER";
	/**
	 * @see ResourceEncodingEnum
	 */
	// Don't reduce the visibility here, we reference this from Smile
	@SuppressWarnings("WeakerAccess")
	public static final int ENCODING_COL_LENGTH = 5;
	public static final String HFJ_RES_VER = "HFJ_RES_VER";
	public static final int RES_TEXT_VC_MAX_LENGTH = 4000;
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "SEQ_RESOURCE_HISTORY_ID", sequenceName = "SEQ_RESOURCE_HISTORY_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_RESOURCE_HISTORY_ID")
	@Column(name = "PID")
	private Long myId;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RES_ID", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_RESOURCE_HISTORY_RESOURCE"))
	private ResourceTable myResourceTable;
	@Column(name = "RES_ID", nullable = false, updatable = false, insertable = false)
	private Long myResourceId;
	@Column(name = "RES_TYPE", length = ResourceTable.RESTYPE_LEN, nullable = false)
	private String myResourceType;
	@Column(name = "RES_VER", nullable = false)
	private Long myResourceVersion;
	@OneToMany(mappedBy = "myResourceHistory", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private Collection<ResourceHistoryTag> myTags;
	@Column(name = "RES_TEXT", length = Integer.MAX_VALUE - 1, nullable = true)
	@Lob()
	@OptimisticLock(excluded = true)
	private byte[] myResource;
	@Column(name = "RES_TEXT_VC", length = RES_TEXT_VC_MAX_LENGTH, nullable = true)
	@OptimisticLock(excluded = true)
	private String myResourceTextVc;
	@Column(name = "RES_ENCODING", nullable = false, length = ENCODING_COL_LENGTH)
	@Enumerated(EnumType.STRING)
	@OptimisticLock(excluded = true)
	private ResourceEncodingEnum myEncoding;
	@OneToOne(mappedBy = "myResourceHistoryTable", cascade = {CascadeType.REMOVE})
	private ResourceHistoryProvenanceEntity myProvenance;

	/**
	 * Constructor
	 */
	public ResourceHistoryTable() {
		super();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("resourceId", myResourceId)
			.append("resourceType", myResourceType)
			.append("resourceVersion", myResourceVersion)
			.append("pid", myId)
			.toString();
	}

	public String getResourceTextVc() {
		return myResourceTextVc;
	}

	public void setResourceTextVc(String theResourceTextVc) {
		myResourceTextVc = theResourceTextVc;
	}

	public ResourceHistoryProvenanceEntity getProvenance() {
		return myProvenance;
	}

	public void addTag(ResourceTag theTag) {
		ResourceHistoryTag tag = new ResourceHistoryTag(this, theTag.getTag(), getPartitionId());
		tag.setResourceType(theTag.getResourceType());
		getTags().add(tag);
	}

	@Override
	public ResourceHistoryTag addTag(TagDefinition theTag) {
		for (ResourceHistoryTag next : getTags()) {
			if (next.getTag().equals(theTag)) {
				return next;
			}
		}
		ResourceHistoryTag historyTag = new ResourceHistoryTag(this, theTag, getPartitionId());
		getTags().add(historyTag);
		return historyTag;
	}

	public ResourceEncodingEnum getEncoding() {
		return myEncoding;
	}

	public void setEncoding(ResourceEncodingEnum theEncoding) {
		myEncoding = theEncoding;
	}

	@Override
	public Long getId() {
		return myId;
	}

	/**
	 * Do not delete, required for java bean introspection
	 */
	public Long getMyId() {
		return myId;
	}

	/**
	 * Do not delete, required for java bean introspection
	 */
	public void setMyId(Long theId) {
		myId = theId;
	}

	public byte[] getResource() {
		return myResource;
	}

	public void setResource(byte[] theResource) {
		myResource = theResource;
	}

	@Override
	public Long getResourceId() {
		return myResourceId;
	}

	public void setResourceId(Long theResourceId) {
		myResourceId = theResourceId;
	}

	@Override
	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	@Override
	public Collection<ResourceHistoryTag> getTags() {
		if (myTags == null) {
			myTags = new ArrayList<>();
		}
		return myTags;
	}

	@Override
	public long getVersion() {
		return myResourceVersion;
	}

	public void setVersion(long theVersion) {
		myResourceVersion = theVersion;
	}

	@Override
	public ResourcePersistentId getPersistentId() {
		return new ResourcePersistentId(myResourceId);
	}

	public ResourceTable getResourceTable() {
		return myResourceTable;
	}

	public void setResourceTable(ResourceTable theResourceTable) {
		myResourceTable = theResourceTable;
	}

	@Override
	public IdDt getIdDt() {
		// Avoid a join query if possible
		String resourceIdPart;
		if (getTransientForcedId() != null) {
			resourceIdPart = getTransientForcedId();
		} else {
			if (getResourceTable().getForcedId() == null) {
				Long id = getResourceId();
				resourceIdPart = id.toString();
			} else {
				resourceIdPart = getResourceTable().getForcedId().getForcedId();
			}
		}
		return new IdDt(getResourceType() + '/' + resourceIdPart + '/' + Constants.PARAM_HISTORY + '/' + getVersion());
	}

	@Override
	public ForcedId getForcedId() {
		return getResourceTable().getForcedId();
	}

	@Override
	public void setForcedId(ForcedId theForcedId) {
		getResourceTable().setForcedId(theForcedId);
	}

	/**
	 * Returns <code>true</code> if there is a populated resource text (i.e.
	 * either {@link #getResource()} or {@link #getResourceTextVc()} return a non null
	 * value.
	 */
	public boolean hasResource() {
		return myResource != null || myResourceTextVc != null;
	}
}
