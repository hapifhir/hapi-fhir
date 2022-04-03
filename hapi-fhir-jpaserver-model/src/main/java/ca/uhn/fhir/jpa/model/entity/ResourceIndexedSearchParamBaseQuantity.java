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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ResourceIndexedSearchParamBaseQuantity extends BaseResourceIndexedSearchParam {

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
	/**
	 * @since 3.5.0 - At some point this should be made not-null
	 */
	@Column(name = "HASH_IDENTITY", nullable = true)
	private Long myHashIdentity;

	/**
	 * Constructor
	 */
	public ResourceIndexedSearchParamBaseQuantity() {
		super();
	}

	@Override
	public void clearHashes() {
		myHashIdentity = null;
		myHashIdentityAndUnits = null;
		myHashIdentitySystemAndUnits = null;
	}

	@Override
	public void calculateHashes() {
		if (myHashIdentity != null || myHashIdentityAndUnits != null || myHashIdentitySystemAndUnits != null) {
			return;
		}

		String resourceType = getResourceType();
		String paramName = getParamName();
		String units = getUnits();
		String system = getSystem();
		setHashIdentity(calculateHashIdentity(getPartitionSettings(), getPartitionId(), resourceType, paramName));
		setHashIdentityAndUnits(calculateHashUnits(getPartitionSettings(), getPartitionId(), resourceType, paramName, units));
		setHashIdentitySystemAndUnits(calculateHashSystemAndUnits(getPartitionSettings(), getPartitionId(), resourceType, paramName, system, units));
	}

	public Long getHashIdentity() {
		return myHashIdentity;
	}

	public void setHashIdentity(Long theHashIdentity) {
		myHashIdentity = theHashIdentity;
	}

	public Long getHashIdentityAndUnits() {
		return myHashIdentityAndUnits;
	}

	public void setHashIdentityAndUnits(Long theHashIdentityAndUnits) {
		myHashIdentityAndUnits = theHashIdentityAndUnits;
	}

	public Long getHashIdentitySystemAndUnits() {
		return myHashIdentitySystemAndUnits;
	}

	public void setHashIdentitySystemAndUnits(Long theHashIdentitySystemAndUnits) {
		myHashIdentitySystemAndUnits = theHashIdentitySystemAndUnits;
	}

	public String getSystem() {
		return mySystem;
	}

	public void setSystem(String theSystem) {
		mySystem = theSystem;
	}

	public String getUnits() {
		return myUnits;
	}

	public void setUnits(String theUnits) {
		myUnits = theUnits;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(getResourceType());
		b.append(getParamName());
		b.append(getHashIdentity());
		b.append(getHashIdentityAndUnits());
		b.append(getHashIdentitySystemAndUnits());
		return b.toHashCode();
	}


	public static long calculateHashSystemAndUnits(PartitionSettings thePartitionSettings, PartitionablePartitionId theRequestPartitionId, String theResourceType, String theParamName, String theSystem, String theUnits) {
		RequestPartitionId requestPartitionId = PartitionablePartitionId.toRequestPartitionId(theRequestPartitionId);
		return calculateHashSystemAndUnits(thePartitionSettings, requestPartitionId, theResourceType, theParamName, theSystem, theUnits);
	}

	public static long calculateHashSystemAndUnits(PartitionSettings thePartitionSettings, RequestPartitionId theRequestPartitionId, String theResourceType, String theParamName, String theSystem, String theUnits) {
		return hash(thePartitionSettings, theRequestPartitionId, theResourceType, theParamName, theSystem, theUnits);
	}

	public static long calculateHashUnits(PartitionSettings thePartitionSettings, PartitionablePartitionId theRequestPartitionId, String theResourceType, String theParamName, String theUnits) {
		RequestPartitionId requestPartitionId = PartitionablePartitionId.toRequestPartitionId(theRequestPartitionId);
		return calculateHashUnits(thePartitionSettings, requestPartitionId, theResourceType, theParamName, theUnits);
	}

	public static long calculateHashUnits(PartitionSettings thePartitionSettings, RequestPartitionId theRequestPartitionId, String theResourceType, String theParamName, String theUnits) {
		return hash(thePartitionSettings, theRequestPartitionId, theResourceType, theParamName, theUnits);
	}

}
