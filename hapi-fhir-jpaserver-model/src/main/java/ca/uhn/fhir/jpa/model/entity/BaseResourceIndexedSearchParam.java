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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.model.search.hash.ResourceIndexHasher;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

import java.util.Date;
import java.util.Objects;

@MappedSuperclass
public abstract class BaseResourceIndexedSearchParam extends BaseResourceIndex {
	static final int MAX_SP_NAME = 100;

	private static final long serialVersionUID = 1L;

	@GenericField
	@Column(name = "SP_MISSING", nullable = false)
	private boolean myMissing = false;

	@FullTextField
	@Column(name = "SP_NAME", length = MAX_SP_NAME, nullable = false)
	private String myParamName;

	@Column(name = "RES_ID", insertable = false, updatable = false, nullable = false)
	private Long myResourcePid;

	@FullTextField
	@Column(name = "RES_TYPE", updatable = false, nullable = false, length = Constants.MAX_RESOURCE_NAME_LENGTH)
	private String myResourceType;

	@GenericField
	@Column(name = "SP_UPDATED", nullable = true) // TODO: make this false after HAPI 2.3
	@Temporal(TemporalType.TIMESTAMP)
	private Date myUpdated;

	@Column(name = "HASH_IDENTITY", nullable = true)
	private Long myHashIdentity;

	@GenericField
	@Column(name = "CONTAINED_ORD", nullable = true)
	private Integer myContainedOrd;

	@Override
	public abstract Long getId();

	public String getParamName() {
		return myParamName;
	}

	public void setParamName(String theName) {
		if (!StringUtils.equals(myParamName, theName)) {
			myParamName = theName;
			clearHashes();
		}
	}

	// MB pushed these down to the individual SP classes so we could name the FK in the join annotation
	/**
	 * Get the Resource this SP indexes
	 */
	public abstract ResourceTable getResource();

	public abstract BaseResourceIndexedSearchParam setResource(ResourceTable theResource);

	@Override
	public <T extends BaseResourceIndex> void copyMutableValuesFrom(T theSource) {
		BaseResourceIndexedSearchParam source = (BaseResourceIndexedSearchParam) theSource;
		// 1. copy hash values
		myHashIdentity = source.myHashIdentity;
		// 2. copy fields that are part of the hash next
		setResourceType(source.myResourceType);
		setParamName(source.myParamName);
		setPartitionId(source.getPartitionId());
		setContainedOrd(source.myContainedOrd);
		// 3. copy other fields that are not part of any hash
		myMissing = source.myMissing;
		myParamName = source.myParamName;
		myUpdated = source.myUpdated;
	}

	public Long getResourcePid() {
		ResourceTable entity = getResource();
		return entity != null ? entity.getResourceId() : null;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		if (!StringUtils.equals(myResourceType, theResourceType)) {
			myResourceType = theResourceType;
			clearHashes();
		}
	}

	public Date getUpdated() {
		return myUpdated;
	}

	public void setUpdated(Date theUpdated) {
		myUpdated = theUpdated;
	}

	public boolean isMissing() {
		return myMissing;
	}

	public BaseResourceIndexedSearchParam setMissing(boolean theMissing) {
		myMissing = theMissing;
		return this;
	}

	public Long getHashIdentity() {
		return myHashIdentity;
	}

	public Integer getContainedOrd() {
		return myContainedOrd;
	}

	public void setContainedOrd(Integer theContainedOrd) {
		if (!Objects.equals(theContainedOrd, myContainedOrd)) {
			myContainedOrd = theContainedOrd;
			clearHashes();
		}
	}

	public boolean isContained() {
		return myContainedOrd != null;
	}

	/**
	 * Calculates the hashes for the search parameter.
	 * This method should be overridden to compute additional hash values as needed by the specific parameter type.
	 * The current object selects which fields need to be hashed.
	 * @param theHasher the hasher which performs the hashing
	 */
	public void calculateHashes(ResourceIndexHasher theHasher) {
		if (isHashPopulated(myHashIdentity)) {
			return;
		}
		myHashIdentity = theHasher.hash(getPartitionId(), isContained(), getResourceType(), getParamName());
	}

	@Override
	public void clearHashes() {
		myHashIdentity = null;
	}

	public abstract IQueryParameterType toQueryParameterType();

	public boolean matches(IQueryParameterType theParam) {
		throw new UnsupportedOperationException(Msg.code(1526) + "No parameter matcher for " + theParam);
	}

	protected static boolean isHashPopulated(Long theHash) {
		return ObjectUtils.defaultIfNull(theHash, 0L) > 0L;
	}
}
