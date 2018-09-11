package ca.uhn.fhir.jpa.entity;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import org.hibernate.annotations.OptimisticLock;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@MappedSuperclass
public abstract class BaseHasResource implements IBaseResourceEntity {

	@Column(name = "RES_DELETED_AT", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date myDeleted;

	// TODO: move to resource history table
	@Column(name = "RES_VERSION", nullable = true, length = 7)
	@Enumerated(EnumType.STRING)
	@OptimisticLock(excluded = true)
	private FhirVersionEnum myFhirVersion;

	@OneToOne(optional = true, fetch = FetchType.LAZY, cascade = {}, orphanRemoval = false)
	@JoinColumn(name = "FORCED_ID_PID")
	@OptimisticLock(excluded = true)
	private ForcedId myForcedId;

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

	public abstract BaseTag addTag(TagDefinition theDef);

	@Override
	public Date getDeleted() {
		return myDeleted;
	}

	public void setDeleted(Date theDate) {
		myDeleted = theDate;
	}


	@Override
	public FhirVersionEnum getFhirVersion() {
		return myFhirVersion;
	}

	public void setFhirVersion(FhirVersionEnum theFhirVersion) {
		myFhirVersion = theFhirVersion;
	}

	public ForcedId getForcedId() {
		return myForcedId;
	}

	public void setForcedId(ForcedId theForcedId) {
		myForcedId = theForcedId;
	}

	@Override
	public abstract Long getId();

	@Override
	public abstract IdDt getIdDt();

	@Override
	public InstantDt getPublished() {
		if (myPublished != null) {
			return new InstantDt(myPublished);
		} else {
			return null;
		}
	}

	public void setPublished(Date thePublished) {
		myPublished = thePublished;
	}

	@Override
	public abstract Long getResourceId();

	@Override
	public abstract String getResourceType();

	public abstract Collection<? extends BaseTag> getTags();

	@Override
	public InstantDt getUpdated() {
		return new InstantDt(myUpdated);
	}

	public void setUpdated(Date theUpdated) {
		myUpdated = theUpdated;
	}

	@Override
	public Date getUpdatedDate() {
		return myUpdated;
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

	public void setPublished(InstantDt thePublished) {
		myPublished = thePublished.getValue();
	}

	public void setUpdated(InstantDt theUpdated) {
		myUpdated = theUpdated.getValue();
	}

}
