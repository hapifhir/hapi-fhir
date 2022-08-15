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
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.param.TokenParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.trim;

@Embeddable
@Entity
@Table(name = "HFJ_SPIDX_TOKEN", indexes = {
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

	@Index(name = "IDX_SP_TOKEN_RESID_V2", columnList = "RES_ID,HASH_SYS_AND_VALUE,HASH_VALUE,HASH_SYS,HASH_IDENTITY,PARTITION_ID")
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
	@Column(name = "HASH_IDENTITY", nullable = true)
	private Long myHashIdentity;
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

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(foreignKey = @ForeignKey(name="FK_SP_TOKEN_RES"),
		name = "RES_ID", referencedColumnName = "RES_ID", nullable = false)
	private ResourceTable myResource;

	/**
	 * Constructor
	 */
	public ResourceIndexedSearchParamToken() {
		super();
	}

	/**
	 * Constructor
	 */
	public ResourceIndexedSearchParamToken(PartitionSettings thePartitionSettings, String theResourceType, String theParamName, String theSystem, String theValue) {
		super();
		setPartitionSettings(thePartitionSettings);
		setResourceType(theResourceType);
		setParamName(theParamName);
		setSystem(theSystem);
		setValue(theValue);
		calculateHashes();
	}

	@Override
	public <T extends BaseResourceIndex> void copyMutableValuesFrom(T theSource) {
		super.copyMutableValuesFrom(theSource);
		ResourceIndexedSearchParamToken source = (ResourceIndexedSearchParamToken) theSource;

		mySystem = source.mySystem;
		myValue = source.myValue;
		myHashSystem = source.myHashSystem;
		myHashSystemAndValue = source.getHashSystemAndValue();
		myHashValue = source.myHashValue;
		myHashIdentity = source.myHashIdentity;
	}

	@Override
	public void clearHashes() {
		myHashIdentity = null;
		myHashSystem = null;
		myHashSystemAndValue = null;
		myHashValue = null;
	}


	@Override
	public void calculateHashes() {
		if (myHashIdentity != null || myHashSystem != null || myHashValue != null || myHashSystemAndValue != null) {
			return;
		}

		String resourceType = getResourceType();
		String paramName = getParamName();
		String system = getSystem();
		String value = getValue();
		setHashIdentity(calculateHashIdentity(getPartitionSettings(), getPartitionId(), resourceType, paramName));
		setHashSystemAndValue(calculateHashSystemAndValue(getPartitionSettings(), getPartitionId(), resourceType, paramName, system, value));

		// Searches using the :of-type modifier can never be partial (system-only or value-only) so don't
		// bother saving these
		boolean calculatePartialHashes = !StringUtils.endsWith(paramName, Constants.PARAMQUALIFIER_TOKEN_OF_TYPE);
		if (calculatePartialHashes) {
			setHashSystem(calculateHashSystem(getPartitionSettings(), getPartitionId(), resourceType, paramName, system));
			setHashValue(calculateHashValue(getPartitionSettings(), getPartitionId(), resourceType, paramName, value));
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
		b.append(getHashSystem(), obj.getHashSystem());
		b.append(getHashValue(), obj.getHashValue());
		b.append(getHashSystemAndValue(), obj.getHashSystemAndValue());
		return b.isEquals();
	}

	public Long getHashSystem() {
		return myHashSystem;
	}

	private void setHashSystem(Long theHashSystem) {
		myHashSystem = theHashSystem;
	}

	private void setHashIdentity(Long theHashIdentity) {
		myHashIdentity = theHashIdentity;
	}

	public Long getHashSystemAndValue() {
		return myHashSystemAndValue;
	}

	private void setHashSystemAndValue(Long theHashSystemAndValue) {
		myHashSystemAndValue = theHashSystemAndValue;
	}

	public Long getHashValue() {
		return myHashValue;
	}

	private void setHashValue(Long theHashValue) {
		myHashValue = theHashValue;
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
		mySystem = StringUtils.defaultIfBlank(theSystem, null);
		myHashSystemAndValue = null;
	}

	public String getValue() {
		return myValue;
	}

	public ResourceIndexedSearchParamToken setValue(String theValue) {
		myValue = StringUtils.defaultIfBlank(theValue, null);
		myHashSystemAndValue = null;
		return this;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(getResourceType());
		b.append(getHashValue());
		b.append(getHashSystem());
		b.append(getHashSystemAndValue());

		return b.toHashCode();
	}

	@Override
	public IQueryParameterType toQueryParameterType() {
		return new TokenParam(getSystem(), getValue());
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		if (getPartitionId() != null) {
			b.append("partitionId", getPartitionId().getPartitionId());
		}
		b.append("resourceType", getResourceType());
		b.append("paramName", getParamName());
		if (isMissing()) {
			b.append("missing", true);
		} else {
			b.append("system", getSystem());
			b.append("value", getValue());
		}
		b.append("hashIdentity", myHashIdentity);
		b.append("hashSystem", myHashSystem);
		b.append("hashValue", myHashValue);
		b.append("hashSysAndValue", myHashSystemAndValue);
		b.append("partition", getPartitionId());
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
			if (token.getSystem().equalsIgnoreCase(getSystem()) &&
				valueString.equalsIgnoreCase(tokenValueString)) {
				retVal = true;
			}
		}
		return retVal;
	}


	public static long calculateHashSystem(PartitionSettings thePartitionSettings, PartitionablePartitionId theRequestPartitionId, String theResourceType, String theParamName, String theSystem) {
		RequestPartitionId requestPartitionId = PartitionablePartitionId.toRequestPartitionId(theRequestPartitionId);
		return calculateHashSystem(thePartitionSettings, requestPartitionId, theResourceType, theParamName, theSystem);
	}

	public static long calculateHashSystem(PartitionSettings thePartitionSettings, RequestPartitionId theRequestPartitionId, String theResourceType, String theParamName, String theSystem) {
		return hash(thePartitionSettings, theRequestPartitionId, theResourceType, theParamName, trim(theSystem));
	}

	public static long calculateHashSystemAndValue(PartitionSettings thePartitionSettings, PartitionablePartitionId theRequestPartitionId, String theResourceType, String theParamName, String theSystem, String theValue) {
		RequestPartitionId requestPartitionId = PartitionablePartitionId.toRequestPartitionId(theRequestPartitionId);
		return calculateHashSystemAndValue(thePartitionSettings, requestPartitionId, theResourceType, theParamName, theSystem, theValue);
	}

	public static long calculateHashSystemAndValue(PartitionSettings thePartitionSettings, RequestPartitionId theRequestPartitionId, String theResourceType, String theParamName, String theSystem, String theValue) {
		return hash(thePartitionSettings, theRequestPartitionId, theResourceType, theParamName, defaultString(trim(theSystem)), trim(theValue));
	}

	public static long calculateHashValue(PartitionSettings thePartitionSettings, PartitionablePartitionId theRequestPartitionId, String theResourceType, String theParamName, String theValue) {
		RequestPartitionId requestPartitionId = PartitionablePartitionId.toRequestPartitionId(theRequestPartitionId);
		return calculateHashValue(thePartitionSettings, requestPartitionId, theResourceType, theParamName, theValue);
	}

	public static long calculateHashValue(PartitionSettings thePartitionSettings, RequestPartitionId theRequestPartitionId, String theResourceType, String theParamName, String theValue) {
		String value = trim(theValue);
		return hash(thePartitionSettings, theRequestPartitionId, theResourceType, theParamName, value);
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
}
