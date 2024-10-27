/*
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.model.primitive.InstantDt;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.hibernate.annotations.OptimisticLock;

import java.util.Collection;
import java.util.Date;

@MappedSuperclass
public abstract class BaseHasResource extends BasePartitionable
		implements IBaseResourceEntity, IBasePersistedResource<JpaPid> {

	public static final String RES_PUBLISHED = "RES_PUBLISHED";
	public static final String RES_UPDATED = "RES_UPDATED";

	@Column(name = "RES_DELETED_AT", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date myDeleted;

	@Column(name = "RES_VERSION", nullable = true, length = 7)
	@Enumerated(EnumType.STRING)
	@OptimisticLock(excluded = true)
	private FhirVersionEnum myFhirVersion;

	@Column(name = "HAS_TAGS", nullable = false)
	@OptimisticLock(excluded = true)
	private boolean myHasTags;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = RES_PUBLISHED, nullable = false)
	@OptimisticLock(excluded = true)
	private Date myPublished;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = RES_UPDATED, nullable = false)
	@OptimisticLock(excluded = true)
	private Date myUpdated;

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

	public void setDeleted(Date theDate) {
		myDeleted = theDate;
	}

	@Override
	public InstantDt getPublished() {
		if (myPublished != null) {
			return new InstantDt(getPublishedDate());
		} else {
			return null;
		}
	}

	public Date getPublishedDate() {
		return cloneDate(myPublished);
	}

	public void setPublished(Date thePublished) {
		myPublished = thePublished;
	}

	public void setPublished(InstantDt thePublished) {
		myPublished = thePublished.getValue();
	}

	public abstract Collection<? extends BaseTag> getTags();

	@Override
	public InstantDt getUpdated() {
		return new InstantDt(getUpdatedDate());
	}

	@Override
	public Date getUpdatedDate() {
		return cloneDate(myUpdated);
	}

	public void setUpdated(Date theUpdated) {
		myUpdated = theUpdated;
	}

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
