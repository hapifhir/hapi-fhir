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

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.search.hash.ResourceIndexHasher;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.param.TokenParam;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.trim;

@Embeddable
@Entity
@Table(
		name = "HFJ_SPIDX_TOKEN",
		indexes = {
			/*
			 * Note: We previously had indexes with the following names,
			 * do not reuse these names:
			 * IDX_SP_TOKEN
			 * IDX_SP_TOKEN_UNQUAL
			 */

			@Index(name = "IDX_SP_TOKEN_HASH_V2", columnList = "HASH_IDENTITY,SP_SYSTEM,SP_VALUE,RES_ID,PARTITION_ID"),
			@Index(name = "IDX_SP_TOKEN_HASH_S_V2", columnList = "HASH_SYS,RES_ID,PARTITION_ID"),
			@Index(name = "IDX_SP_TOKEN_HASH_SV_V2", columnList = "HASH_SYS_AND_VALUE,RES_ID,PARTITION_ID"),
			@Index(name = "IDX_SP_TOKEN_HASH_V_V2", columnList = "HASH_VALUE,RES_ID,PARTITION_ID"),
			@Index(
					name = "IDX_SP_TOKEN_RESID_V2",
					columnList = "RES_ID,HASH_SYS_AND_VALUE,HASH_VALUE,HASH_SYS,HASH_IDENTITY,PARTITION_ID")
		})
public class ResourceIndexedSearchParamToken extends BaseResourceIndexedSearchParam {

	public static final int MAX_LENGTH = 200;

	private static final long serialVersionUID = 1L;

	@FullTextField
	@Column(name = "SP_SYSTEM", nullable = true, length = MAX_LENGTH)
	public String mySystem;

	@FullTextField
	@Column(name = "SP_VALUE", nullable = true, length = MAX_LENGTH)
	private String myValue;

	@SuppressWarnings("unused")
	@Id
	@SequenceGenerator(name = "SEQ_SPIDX_TOKEN", sequenceName = "SEQ_SPIDX_TOKEN")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_SPIDX_TOKEN")
	@Column(name = "SP_ID")
	private Long myId;
	/**
	 * @since 3.4.0 - At some point this should be made not-null
	 */
	@Column(name = "HASH_SYS", nullable = true)
	private Long myHashSystem;
	/**
	 * @since 3.4.0 - At some point this should be made not-null
	 */
	@Column(name = "HASH_SYS_AND_VALUE", nullable = true)
	private Long myHashSystemAndValue;
	/**
	 * @since 3.4.0 - At some point this should be made not-null
	 */
	@Column(name = "HASH_VALUE", nullable = true)
	private Long myHashValue;

	@ManyToOne(
			optional = false,
			fetch = FetchType.LAZY,
			cascade = {})
	@JoinColumn(
			foreignKey = @ForeignKey(name = "FK_SP_TOKEN_RES"),
			name = "RES_ID",
			referencedColumnName = "RES_ID",
			nullable = false)
	private ResourceTable myResource;

	/**
	 * Constructor
	 */
	public ResourceIndexedSearchParamToken() {}

	@Deprecated(since = "7.2")
	public ResourceIndexedSearchParamToken(
			PartitionSettings thePartitionSettings,
			String theResourceType,
			String theParamName,
			String theSystem,
			String theValue) {
		setResourceType(theResourceType);
		setParamName(theParamName);
		setSystem(theSystem);
		setValue(theValue);
	}

	/**
	 * Constructor
	 */
	public ResourceIndexedSearchParamToken(
			String theResourceType, String theParamName, String theSystem, String theValue) {
		setResourceType(theResourceType);
		setParamName(theParamName);
		setSystem(theSystem);
		setValue(theValue);
	}

	/**
	 * Constructor
	 */
	public ResourceIndexedSearchParamToken(String theResourceType, String theParamName, boolean theMissing) {
		super();
		setResourceType(theResourceType);
		setParamName(theParamName);
		setMissing(theMissing);
	}

	@Override
	public <T extends BaseResourceIndex> void copyMutableValuesFrom(T theSource) {
		super.copyMutableValuesFrom(theSource);
		ResourceIndexedSearchParamToken source = (ResourceIndexedSearchParamToken) theSource;
		// 1. copy hash values first
		myHashSystem = source.myHashSystem;
		myHashSystemAndValue = source.getHashSystemAndValue();
		myHashValue = source.myHashValue;
		// 2. copy fields that are part of the hash next
		mySystem = source.mySystem;
		myValue = source.myValue;
	}

	@Override
	public void clearHashes() {
		super.clearHashes();
		myHashSystem = null;
		myHashSystemAndValue = null;
		myHashValue = null;
	}

	@Override
	public void calculateHashes(ResourceIndexHasher theHasher) {
		super.calculateHashes(theHasher);
		if (isHashPopulated(myHashSystemAndValue)) {
			return;
		}
		String resourceType = getResourceType();
		String paramName = getParamName();
		String system = defaultString(trim(getSystem()));
		String value = getValue();
		PartitionablePartitionId partitionId = getPartitionId();
		boolean isContained = isContained();
		myHashSystemAndValue = theHasher.hash(partitionId, isContained, resourceType, paramName, system, value);
		// Searches using the :of-type modifier can never be partial (system-only or value-only) so don't
		// bother saving these
		boolean calculatePartialHashes = !StringUtils.endsWith(paramName, Constants.PARAMQUALIFIER_TOKEN_OF_TYPE);
		if (calculatePartialHashes) {
			if (isHashPopulated(myHashSystem) && isHashPopulated(myHashValue)) {
				return;
			}
			myHashSystem = theHasher.hash(partitionId, isContained, resourceType, paramName, system);
			myHashValue = theHasher.hash(partitionId, isContained, resourceType, paramName, value);
		}
	}

	@Override
	public boolean equals(Object theObj) {
		if (this == theObj) {
			return true;
		}
		if (theObj == null) {
			return false;
		}
		if (!(theObj instanceof ResourceIndexedSearchParamToken)) {
			return false;
		}
		ResourceIndexedSearchParamToken obj = (ResourceIndexedSearchParamToken) theObj;
		EqualsBuilder b = new EqualsBuilder();
		b.append(getResourceType(), obj.getResourceType());
		b.append(getParamName(), obj.getParamName());
		b.append(isMissing(), obj.isMissing());
		b.append(getContainedOrd(), obj.getContainedOrd());
		b.append(getPartitionId(), obj.getPartitionId());
		b.append(getSystem(), obj.getSystem());
		b.append(getValue(), obj.getValue());
		return b.isEquals();
	}

	public Long getHashSystem() {
		return myHashSystem;
	}

	public Long getHashSystemAndValue() {
		return myHashSystemAndValue;
	}

	public Long getHashValue() {
		return myHashValue;
	}

	@Override
	public Long getId() {
		return myId;
	}

	@Override
	public void setId(Long theId) {
		myId = theId;
	}

	public String getSystem() {
		return mySystem;
	}

	public void setSystem(String theSystem) {
		if (!StringUtils.equals(mySystem, theSystem)) {
			mySystem = StringUtils.defaultIfBlank(theSystem, null);
			clearHashes();
		}
	}

	public String getValue() {
		return myValue;
	}

	public ResourceIndexedSearchParamToken setValue(String theValue) {
		if (!StringUtils.equals(myValue, theValue)) {
			myValue = StringUtils.defaultIfBlank(theValue, null);
			myHashSystemAndValue = null;
			return this;
		}
		return this;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(getResourceType());
		b.append(getParamName());
		b.append(isMissing());
		b.append(getContainedOrd());
		b.append(getPartitionId());
		b.append(getSystem());
		b.append(getValue());
		return b.toHashCode();
	}

	@Override
	public IQueryParameterType toQueryParameterType() {
		return new TokenParam(getSystem(), getValue());
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("id", getId());
		b.append("resourceType", getResourceType());
		b.append("paramName", getParamName());
		b.append("missing", isMissing());
		b.append("containedOrd", getContainedOrd());
		b.append("partitionId", getPartitionId());
		b.append("system", getSystem());
		b.append("value", getValue());
		return b.build();
	}

	@Override
	public boolean matches(IQueryParameterType theParam) {
		if (!(theParam instanceof TokenParam)) {
			return false;
		}
		TokenParam token = (TokenParam) theParam;
		boolean retVal = false;
		String valueString = defaultString(getValue());
		String tokenValueString = defaultString(token.getValue());

		// Only match on system if it wasn't specified
		if (token.getSystem() == null || token.getSystem().isEmpty()) {
			if (valueString.equalsIgnoreCase(tokenValueString)) {
				retVal = true;
			}
		} else if (tokenValueString == null || tokenValueString.isEmpty()) {
			if (token.getSystem().equalsIgnoreCase(getSystem())) {
				retVal = true;
			}
		} else {
			if (token.getSystem().equalsIgnoreCase(getSystem()) && valueString.equalsIgnoreCase(tokenValueString)) {
				retVal = true;
			}
		}
		return retVal;
	}

	@Override
	public ResourceTable getResource() {
		return myResource;
	}

	@Override
	public BaseResourceIndexedSearchParam setResource(ResourceTable theResource) {
		myResource = theResource;
		setResourceType(theResource.getResourceType());
		return this;
	}

	/**
	 * We truncate the fields at the last moment because the tables have limited size.
	 * We don't truncate earlier in the flow because the index hashes MUST be calculated on the full string.
	 */
	@PrePersist
	public void truncateFieldsForDB() {
		mySystem = StringUtils.truncate(mySystem, MAX_LENGTH);
		myValue = StringUtils.truncate(myValue, MAX_LENGTH);
	}
}
