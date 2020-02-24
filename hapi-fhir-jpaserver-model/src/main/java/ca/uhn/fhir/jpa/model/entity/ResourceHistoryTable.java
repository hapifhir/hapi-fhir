package ca.uhn.fhir.jpa.model.entity;

/*
 * #%L
 * HAPI FHIR Model
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.Constants;
import org.hibernate.annotations.OptimisticLock;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "HFJ_RES_VER", uniqueConstraints = {
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

	@Column(name = "RES_ENCODING", nullable = false, length = ENCODING_COL_LENGTH)
	@Enumerated(EnumType.STRING)
	@OptimisticLock(excluded = true)
	private ResourceEncodingEnum myEncoding;

	@OneToOne(mappedBy = "myResourceHistoryTable", cascade = {CascadeType.REMOVE})
	private ResourceHistoryProvenanceEntity myProvenance;

	public ResourceHistoryTable() {
		super();
	}

	public ResourceHistoryProvenanceEntity getProvenance() {
		return myProvenance;
	}

	public void addTag(ResourceTag theTag) {
		ResourceHistoryTag tag = new ResourceHistoryTag(this, theTag.getTag());
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
		ResourceHistoryTag historyTag = new ResourceHistoryTag(this, theTag);
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
		if (getResourceTable().getForcedId() == null) {
			Long id = getResourceId();
			return new IdDt(getResourceType() + '/' + id + '/' + Constants.PARAM_HISTORY + '/' + getVersion());
		} else {
			// Avoid a join query if possible
			String forcedId = getTransientForcedId() != null ? getTransientForcedId() : getResourceTable().getForcedId().getForcedId();
			return new IdDt(getResourceType() + '/' + forcedId + '/' + Constants.PARAM_HISTORY + '/' + getVersion());
		}
	}

	@Override
	public ForcedId getForcedId() {
		return getResourceTable().getForcedId();
	}

	@Override
	public void setForcedId(ForcedId theForcedId) {
		getResourceTable().setForcedId(theForcedId);
	}

}
