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

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.api.Constants;
import org.hibernate.annotations.OptimisticLock;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@MappedSuperclass
public abstract class BaseHasResource implements IBaseResourceEntity, IBasePersistedResource {

	@Column(name = "RES_DELETED_AT", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date myDeleted;

	// TODO: move to resource history table
	@Column(name = "RES_VERSION", nullable = true, length = 7)
	@Enumerated(EnumType.STRING)
	@OptimisticLock(excluded = true)
	private FhirVersionEnum myFhirVersion;

	@Column(name = "HAS_TAGS", nullable = false)
	@OptimisticLock(excluded = true)
	private boolean myHasTags;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RES_PUBLISHED", nullable = false)
	@OptimisticLock(excluded = true)
	private Date myPublished;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RES_UPDATED", nullable = false)
	@OptimisticLock(excluded = true)
	private Date myUpdated;

	/**
	 * This is stored as an optimization to avoid neeind to query for this
	 * after an update
	 */
	@Transient
	private transient String myTransientForcedId;

	public String getTransientForcedId() {
		return myTransientForcedId;
	}

	public void setTransientForcedId(String theTransientForcedId) {
		myTransientForcedId = theTransientForcedId;
	}


	public abstract BaseTag addTag(TagDefinition theDef);

	@Override
	public Date getDeleted() {
		return cloneDate(myDeleted);
	}

	@Override
	public FhirVersionEnum getFhirVersion() {
		return myFhirVersion;
	}

	public void setFhirVersion(FhirVersionEnum theFhirVersion) {
		myFhirVersion = theFhirVersion;
	}

	abstract public ForcedId getForcedId();

	abstract public void setForcedId(ForcedId theForcedId);

	@Override
	public abstract Long getId();

	@Override
	public boolean isDeleted() {
		return myDeleted != null;
	}

	public void setDeleted(Date theDate) {
		myDeleted = theDate;
	}

	@Override
	public InstantDt getPublished() {
		if (myPublished != null) {
			return new InstantDt(cloneDate(myPublished));
		} else {
			return null;
		}
	}

	public void setPublished(Date thePublished) {
		myPublished = thePublished;
	}

	public void setPublished(InstantDt thePublished) {
		myPublished = thePublished.getValue();
	}

	@Override
	public abstract Long getResourceId();

	@Override
	public abstract String getResourceType();

	public abstract Collection<? extends BaseTag> getTags();

	@Override
	public InstantDt getUpdated() {
		return new InstantDt(cloneDate(myUpdated));
	}

	public void setUpdated(Date theUpdated) {
		myUpdated = theUpdated;
	}

	public void setUpdated(InstantDt theUpdated) {
		myUpdated = theUpdated.getValue();
	}

	@Override
	public Date getUpdatedDate() {
		return cloneDate(myUpdated);
	}

	@Override
	public abstract long getVersion();

	@Override
	public boolean isHasTags() {
		return myHasTags;
	}

	public void setHasTags(boolean theHasTags) {
		myHasTags = theHasTags;
	}

	static Date cloneDate(Date theDate) {
		Date retVal = theDate;
		if (retVal != null) {
			retVal = new Date(retVal.getTime());
		}
		return retVal;
	}

}
