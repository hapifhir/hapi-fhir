package ca.uhn.fhir.jpa.entity;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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
import java.util.Date;

import javax.persistence.*;

import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;

@MappedSuperclass
public abstract class BaseResourceIndexedSearchParam implements Serializable {

	static final int MAX_SP_NAME = 100;

	private static final long serialVersionUID = 1L;

	// TODO: make this nullable=false and a primitive (written may 2017)
	@Field()
	@Column(name = "SP_MISSING", nullable = true)
	private Boolean myMissing = Boolean.FALSE;

	@Field
	@Column(name = "SP_NAME", length = MAX_SP_NAME, nullable = false)
	private String myParamName;

	@ManyToOne(optional = false)
	@JoinColumn(name = "RES_ID", referencedColumnName = "RES_ID")
	@ContainedIn
	private ResourceTable myResource;

	@Column(name = "RES_ID", insertable = false, updatable = false)
	private Long myResourcePid;

	@Field()
	@Column(name = "RES_TYPE", nullable = false)
	private String myResourceType;

	@Field()
	@Column(name = "SP_UPDATED", nullable = true) // TODO: make this false after HAPI 2.3
	@Temporal(TemporalType.TIMESTAMP)
	private Date myUpdated;

	protected abstract Long getId();

	public String getParamName() {
		return myParamName;
	}

	public ResourceTable getResource() {
		return myResource;
	}

	public Long getResourcePid() {
		return myResourcePid;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public Date getUpdated() {
		return myUpdated;
	}

	public boolean isMissing() {
		return Boolean.TRUE.equals(myMissing);
	}

	public void setMissing(boolean theMissing) {
		myMissing = theMissing;
	}

	public void setParamName(String theName) {
		myParamName = theName;
	}

	public void setResource(ResourceTable theResource) {
		myResource = theResource;
		myResourceType = theResource.getResourceType();
	}

	public void setUpdated(Date theUpdated) {
		myUpdated = theUpdated;
	}

}
