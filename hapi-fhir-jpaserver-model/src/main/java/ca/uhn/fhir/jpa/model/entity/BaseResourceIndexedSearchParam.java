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
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.util.SearchParamHash;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

import java.util.Date;
import java.util.List;

@MappedSuperclass
public abstract class BaseResourceIndexedSearchParam extends BaseResourceIndex {
	static final int MAX_SP_NAME = 100;
	private static final long serialVersionUID = 1L;

	@GenericField
	@Column(name = "SP_MISSING", nullable = false)
	private boolean myMissing = false;

	@FullTextField
	@Column(name = "SP_NAME", length = MAX_SP_NAME)
	private String myParamName;

	@Column(name = "RES_ID", insertable = false, updatable = false, nullable = false)
	private Long myResourcePid;

	@FullTextField
	@Column(name = "RES_TYPE", length = Constants.MAX_RESOURCE_NAME_LENGTH)
	private String myResourceType;

	/**
	 * Composite of resourceType, paramName, and partition info if configured.
	 * Combined with the various date fields for a query.
	 * Nullable to allow optimized storage.
	 */
	@Column(name = "HASH_IDENTITY", nullable = true)
	protected Long myHashIdentity;

	@GenericField
	@Column(name = "SP_UPDATED", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date myUpdated;

	@Transient
	private transient PartitionSettings myPartitionSettings;

	@Transient
	private transient StorageSettings myStorageSettings;

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

	/**
	 * Restore SP_NAME without clearing hashes
	 */
	public void restoreParamName(String theParamName) {
		if (myParamName == null) {
			myParamName = theParamName;
		}
	}

	/**
	 * Set SP_NAME, RES_TYPE, SP_UPDATED to null without clearing hashes
	 */
	public void optimizeIndexStorage() {
		myParamName = null;
		myResourceType = null;
		myUpdated = null;
	}

	public boolean isIndexStorageOptimized() {
		return myParamName == null || myResourceType == null || myUpdated == null;
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
		myMissing = source.myMissing;
		myParamName = source.myParamName;
		myResourceType = source.myResourceType;
		myUpdated = source.myUpdated;
		myStorageSettings = source.myStorageSettings;
		myPartitionSettings = source.myPartitionSettings;
		setPartitionId(source.getPartitionId());
	}

	public Long getResourcePid() {
		return myResourcePid;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	public void setHashIdentity(Long theHashIdentity) {
		myHashIdentity = theHashIdentity;
	}

	public Long getHashIdentity() {
		return myHashIdentity;
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

	public abstract IQueryParameterType toQueryParameterType();

	public boolean matches(IQueryParameterType theParam) {
		throw new UnsupportedOperationException(Msg.code(1526) + "No parameter matcher for " + theParam);
	}

	public PartitionSettings getPartitionSettings() {
		return myPartitionSettings;
	}

	public BaseResourceIndexedSearchParam setPartitionSettings(PartitionSettings thePartitionSettings) {
		myPartitionSettings = thePartitionSettings;
		return this;
	}

	public StorageSettings getStorageSettings() {
		return myStorageSettings;
	}

	public BaseResourceIndexedSearchParam setStorageSettings(StorageSettings theStorageSettings) {
		myStorageSettings = theStorageSettings;
		return this;
	}

	public static long calculateHashIdentity(
			PartitionSettings thePartitionSettings,
			PartitionablePartitionId theRequestPartitionId,
			String theResourceType,
			String theParamName) {
		RequestPartitionId requestPartitionId = PartitionablePartitionId.toRequestPartitionId(theRequestPartitionId);
		return calculateHashIdentity(thePartitionSettings, requestPartitionId, theResourceType, theParamName);
	}

	public static long calculateHashIdentity(
			PartitionSettings thePartitionSettings,
			RequestPartitionId theRequestPartitionId,
			String theResourceType,
			String theParamName) {
		return SearchParamHash.hashSearchParam(
				thePartitionSettings, theRequestPartitionId, theResourceType, theParamName);
	}

	public static long calculateHashIdentity(
			PartitionSettings thePartitionSettings,
			RequestPartitionId theRequestPartitionId,
			String theResourceType,
			String theParamName,
			List<String> theAdditionalValues) {
		String[] values = new String[theAdditionalValues.size() + 2];
		values[0] = theResourceType;
		values[1] = theParamName;
		for (int i = 0; i < theAdditionalValues.size(); i++) {
			values[i + 2] = theAdditionalValues.get(i);
		}

		return SearchParamHash.hashSearchParam(thePartitionSettings, theRequestPartitionId, values);
	}
}
