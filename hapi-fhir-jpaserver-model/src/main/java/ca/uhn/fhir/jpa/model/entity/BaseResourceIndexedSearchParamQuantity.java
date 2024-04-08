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

import ca.uhn.fhir.jpa.model.search.hash.ResourceIndexHasher;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.trim;

@MappedSuperclass
public abstract class BaseResourceIndexedSearchParamQuantity extends BaseResourceIndexedSearchParam {

	private static final int MAX_LENGTH = 200;

	private static final long serialVersionUID = 1L;

	@Column(name = "SP_SYSTEM", nullable = true, length = MAX_LENGTH)
	@FullTextField
	public String mySystem;

	@Column(name = "SP_UNITS", nullable = true, length = MAX_LENGTH)
	@FullTextField
	public String myUnits;

	/**
	 * @since 3.5.0 - At some point this should be made not-null
	 */
	@Column(name = "HASH_IDENTITY_AND_UNITS", nullable = true)
	private Long myHashIdentityAndUnits;
	/**
	 * @since 3.5.0 - At some point this should be made not-null
	 */
	@Column(name = "HASH_IDENTITY_SYS_UNITS", nullable = true)
	private Long myHashIdentitySystemAndUnits;

	@Override
	public <T extends BaseResourceIndex> void copyMutableValuesFrom(T theSource) {
		super.copyMutableValuesFrom(theSource);
		BaseResourceIndexedSearchParamQuantity source = (BaseResourceIndexedSearchParamQuantity) theSource;
		// 1. copy hash values
		myHashIdentityAndUnits = source.getHashIdentityAndUnits();
		myHashIdentitySystemAndUnits = source.getHashIdentitySystemAndUnits();
		// 2. copy fields that are part of the hash
		setSystem(source.mySystem);
		setUnits(source.myUnits);
	}

	@Override
	public void clearHashes() {
		super.clearHashes();
		myHashIdentityAndUnits = null;
		myHashIdentitySystemAndUnits = null;
	}

	@Override
	public void calculateHashes(ResourceIndexHasher theHasher) {
		super.calculateHashes(theHasher);
		if (isHashPopulated(myHashIdentityAndUnits) && isHashPopulated(myHashIdentitySystemAndUnits)) {
			return;
		}
		String resourceType = getResourceType();
		String paramName = getParamName();
		String units = getUnits();
		String system = defaultString(trim(getSystem()));
		boolean isContained = isContained();
		PartitionablePartitionId partitionId = getPartitionId();
		myHashIdentityAndUnits = theHasher.hash(partitionId, isContained, resourceType, paramName, units);
		myHashIdentitySystemAndUnits = theHasher.hash(partitionId, isContained, resourceType, paramName, system, units);
	}

	public Long getHashIdentityAndUnits() {
		return myHashIdentityAndUnits;
	}

	public Long getHashIdentitySystemAndUnits() {
		return myHashIdentitySystemAndUnits;
	}

	public String getSystem() {
		return mySystem;
	}

	public BaseResourceIndexedSearchParam setSystem(String theSystem) {
		if (!StringUtils.equals(mySystem, theSystem)) {
			mySystem = theSystem;
			clearHashes();
		}
		return this;
	}

	public String getUnits() {
		return myUnits;
	}

	public BaseResourceIndexedSearchParam setUnits(String theUnits) {
		if (!StringUtils.equals(myUnits, theUnits)) {
			myUnits = theUnits;
			clearHashes();
		}
		return this;
	}
}
