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

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.search.annotations.Field;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "HFJ_RES_LINK", indexes = {
	@Index(name = "IDX_RL_TPATHRES", columnList = "SRC_PATH,TARGET_RESOURCE_ID"),
	@Index(name = "IDX_RL_SRC", columnList = "SRC_RESOURCE_ID"),
	@Index(name = "IDX_RL_DEST", columnList = "TARGET_RESOURCE_ID")
})
public class ResourceLink extends BaseResourceIndex {

	public static final int SRC_PATH_LENGTH = 200;
	private static final long serialVersionUID = 1L;
	@SequenceGenerator(name = "SEQ_RESLINK_ID", sequenceName = "SEQ_RESLINK_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_RESLINK_ID")
	@Id
	@Column(name = "PID")
	private Long myId;

	@Column(name = "SRC_PATH", length = SRC_PATH_LENGTH, nullable = false)
	private String mySourcePath;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "SRC_RESOURCE_ID", referencedColumnName = "RES_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_RESLINK_SOURCE"))
	private ResourceTable mySourceResource;

	@Column(name = "SRC_RESOURCE_ID", insertable = false, updatable = false, nullable = false)
	private Long mySourceResourcePid;

	@Column(name = "SOURCE_RESOURCE_TYPE", nullable = false, length = ResourceTable.RESTYPE_LEN)
	@Field()
	private String mySourceResourceType;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "TARGET_RESOURCE_ID", referencedColumnName = "RES_ID", nullable = true, insertable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_RESLINK_TARGET"))
	private ResourceTable myTargetResource;

	@Column(name = "TARGET_RESOURCE_ID", insertable = true, updatable = true, nullable = true)
	@Field()
	private Long myTargetResourcePid;
	@Column(name = "TARGET_RESOURCE_TYPE", nullable = false, length = ResourceTable.RESTYPE_LEN)
	@Field()
	private String myTargetResourceType;
	@Column(name = "TARGET_RESOURCE_URL", length = 200, nullable = true)
	@Field()
	private String myTargetResourceUrl;
	@Field()
	@Column(name = "SP_UPDATED", nullable = true) // TODO: make this false after HAPI 2.3
	@Temporal(TemporalType.TIMESTAMP)
	private Date myUpdated;
	@Transient
	private transient String myTargetResourceId;

	public ResourceLink() {
		super();
	}

	public String getTargetResourceId() {
		if (myTargetResourceId == null && myTargetResource != null) {
			myTargetResourceId = getTargetResource().getIdDt().getIdPart();
		}
		return myTargetResourceId;
	}

	public String getTargetResourceType() {
		return myTargetResourceType;
	}

	@Override
	public boolean equals(Object theObj) {
		if (this == theObj) {
			return true;
		}
		if (theObj == null) {
			return false;
		}
		if (!(theObj instanceof ResourceLink)) {
			return false;
		}
		ResourceLink obj = (ResourceLink) theObj;
		EqualsBuilder b = new EqualsBuilder();
		b.append(mySourcePath, obj.mySourcePath);
		b.append(mySourceResource, obj.mySourceResource);
		b.append(myTargetResourceUrl, obj.myTargetResourceUrl);
		b.append(myTargetResourceType, obj.myTargetResourceType);
		b.append(getTargetResourceId(), obj.getTargetResourceId());
		return b.isEquals();
	}

	public String getSourcePath() {
		return mySourcePath;
	}

	public void setSourcePath(String theSourcePath) {
		mySourcePath = theSourcePath;
	}

	public ResourceTable getSourceResource() {
		return mySourceResource;
	}

	public void setSourceResource(ResourceTable theSourceResource) {
		mySourceResource = theSourceResource;
		mySourceResourcePid = theSourceResource.getId();
		mySourceResourceType = theSourceResource.getResourceType();
	}

	public void setTargetResource(String theResourceType, Long theResourcePid, String theTargetResourceId) {
		Validate.notBlank(theResourceType);

		myTargetResourceType = theResourceType;
		myTargetResourcePid = theResourcePid;
		myTargetResourceId = theTargetResourceId;
	}

	public Long getTargetResourcePid() {
		return myTargetResourcePid;
	}

	public void setTargetResourceUrl(IIdType theTargetResourceUrl) {
		Validate.isTrue(theTargetResourceUrl.hasBaseUrl());
		Validate.isTrue(theTargetResourceUrl.hasResourceType());

//		if (theTargetResourceUrl.hasIdPart()) {
		// do nothing
//		} else {
		// Must have set an url like http://example.org/something
		// We treat 'something' as the resource type because of fix for #659. Prior to #659 fix, 'something' was
		// treated as the id and 'example.org' was treated as the resource type
		// TODO: log a warning?
//		}

		myTargetResourceType = theTargetResourceUrl.getResourceType();
		myTargetResourceUrl = theTargetResourceUrl.getValue();
	}

	public void setTargetResourceUrlCanonical(String theTargetResourceUrl) {
		Validate.notBlank(theTargetResourceUrl);

		myTargetResourceType = "(unknown)";
		myTargetResourceUrl = theTargetResourceUrl;
	}

	public Date getUpdated() {
		return myUpdated;
	}

	public void setUpdated(Date theUpdated) {
		myUpdated = theUpdated;
	}

	@Override
	public Long getId() {
		return myId;
	}

	@Override
	public void setId(Long theId) {
		myId = theId;
	}

	@Override
	public void calculateHashes() {
		// nothing right now
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(mySourcePath);
		b.append(mySourceResource);
		b.append(myTargetResourceUrl);
		b.append(getTargetResourceType());
		b.append(getTargetResourceId());
		return b.toHashCode();
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("ResourceLink[");
		b.append("path=").append(mySourcePath);
		b.append(", src=").append(mySourceResourcePid);
		b.append(", target=").append(myTargetResourcePid);
		b.append(", targetType=").append(myTargetResourceType);
		b.append(", targetUrl=").append(myTargetResourceUrl);

		b.append("]");
		return b.toString();
	}

	public ResourceTable getTargetResource() {
		return myTargetResource;
	}

	public static ResourceLink forAbsoluteReference(String theSourcePath, ResourceTable theSourceResource, IIdType theTargetResourceUrl, Date theUpdated) {
		ResourceLink retVal = new ResourceLink();
		retVal.setSourcePath(theSourcePath);
		retVal.setSourceResource(theSourceResource);
		retVal.setTargetResourceUrl(theTargetResourceUrl);
		retVal.setUpdated(theUpdated);
		return retVal;
	}

	/**
	 * Factory for canonical URL
	 */
	public static ResourceLink forLogicalReference(String theSourcePath, ResourceTable theSourceResource, String theTargetResourceUrl, Date theUpdated) {
		ResourceLink retVal = new ResourceLink();
		retVal.setSourcePath(theSourcePath);
		retVal.setSourceResource(theSourceResource);
		retVal.setTargetResourceUrlCanonical(theTargetResourceUrl);
		retVal.setUpdated(theUpdated);
		return retVal;
	}

	public static ResourceLink forLocalReference(String theSourcePath, ResourceTable theSourceResource, String theTargetResourceType, Long theTargetResourcePid, String theTargetResourceId, Date theUpdated) {
		ResourceLink retVal = new ResourceLink();
		retVal.setSourcePath(theSourcePath);
		retVal.setSourceResource(theSourceResource);
		retVal.setTargetResource(theTargetResourceType, theTargetResourcePid, theTargetResourceId);
		retVal.setUpdated(theUpdated);
		return retVal;
	}

}
