package ca.uhn.fhir.jpa.entity;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.search.annotations.Field;

@Entity
@Table(name = "HFJ_RES_LINK" , indexes= {
	@Index(name="IDX_RL_TPATHRES", columnList= "SRC_PATH,TARGET_RESOURCE_ID"), 
	@Index(name="IDX_RL_SRC", columnList= "SRC_RESOURCE_ID"), 
	@Index(name="IDX_RL_DEST", columnList= "TARGET_RESOURCE_ID")
})
public class ResourceLink implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	@Column(name = "PID")
	private Long myId;

	@Column(name = "SRC_PATH", length = 100, nullable = false)
	private String mySourcePath;

	@ManyToOne(optional = false, fetch=FetchType.LAZY)
	@JoinColumn(name = "SRC_RESOURCE_ID", referencedColumnName = "RES_ID", nullable = false)
//	@ContainedIn()
	private ResourceTable mySourceResource;

	@Column(name = "SRC_RESOURCE_ID", insertable = false, updatable = false, nullable = false)
	private Long mySourceResourcePid;

	@Column(name = "SOURCE_RESOURCE_TYPE", nullable=false, length=ResourceTable.RESTYPE_LEN)
	@ColumnDefault("''") // TODO: remove this (it's only here for simplifying upgrades of 1.3 -> 1.4)
	@Field()
	private String mySourceResourceType;

	@ManyToOne(optional = false, fetch=FetchType.LAZY)
	@JoinColumn(name = "TARGET_RESOURCE_ID", referencedColumnName = "RES_ID", nullable = false)
	private ResourceTable myTargetResource;

	@Column(name = "TARGET_RESOURCE_ID", insertable = false, updatable = false, nullable = false)
	@Field()
	private Long myTargetResourcePid;

	@Column(name = "TARGET_RESOURCE_TYPE", nullable=false, length=ResourceTable.RESTYPE_LEN)
	@ColumnDefault("''") // TODO: remove this (it's only here for simplifying upgrades of 1.3 -> 1.4)
	@Field()
	private String myTargetResourceType;

	public ResourceLink() {
		super();
	}

	public ResourceLink(String theSourcePath, ResourceTable theSourceResource, ResourceTable theTargetResource) {
		super();
		setSourcePath(theSourcePath);
		setSourceResource(theSourceResource);
		setTargetResource(theTargetResource);
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
		b.append(myTargetResourcePid, obj.myTargetResourcePid);
		return b.isEquals();
	}

	public String getSourcePath() {
		return mySourcePath;
	}

	public ResourceTable getSourceResource() {
		return mySourceResource;
	}

	public Long getSourceResourcePid() {
		return mySourceResourcePid;
	}

	public ResourceTable getTargetResource() {
		return myTargetResource;
	}

	public Long getTargetResourcePid() {
		return myTargetResourcePid;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(mySourcePath);
		b.append(mySourceResource);
		b.append(myTargetResourcePid);
		return b.toHashCode();
	}

	public void setSourcePath(String theSourcePath) {
		mySourcePath = theSourcePath;
	}

	public void setSourceResource(ResourceTable theSourceResource) {
		mySourceResource = theSourceResource;
		mySourceResourcePid = theSourceResource.getId();
		mySourceResourceType = theSourceResource.getResourceType();
	}

	public void setTargetResource(ResourceTable theTargetResource) {
		Validate.notNull(theTargetResource);
		myTargetResource = theTargetResource;
		myTargetResourcePid = theTargetResource.getId();
		myTargetResourceType = theTargetResource.getResourceType();
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("ResourceLink[");
		b.append("path=").append(mySourcePath);
		b.append(", src=").append(mySourceResourcePid);
		b.append(", target=").append(myTargetResourcePid);

		b.append("]");
		return b.toString();
	}

}
