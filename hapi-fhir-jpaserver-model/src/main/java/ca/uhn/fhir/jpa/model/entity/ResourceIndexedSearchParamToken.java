package ca.uhn.fhir.jpa.model.entity;

/*
 * #%L
 * HAPI FHIR Model
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
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

	// TODO PERF Recommend to drop this index:
	@Index(name = "IDX_SP_TOKEN_HASH", columnList = "HASH_IDENTITY"),
	@Index(name = "IDX_SP_TOKEN_HASH_S", columnList = "HASH_SYS"),
	@Index(name = "IDX_SP_TOKEN_HASH_SV", columnList = "HASH_SYS_AND_VALUE"),
	// TODO PERF change this to:
	//	@Index(name = "IDX_SP_TOKEN_HASH_V", columnList = "HASH_VALUE,RES_ID"),
	@Index(name = "IDX_SP_TOKEN_HASH_V", columnList = "HASH_VALUE"),

	@Index(name = "IDX_SP_TOKEN_UPDATED", columnList = "SP_UPDATED"),
	@Index(name = "IDX_SP_TOKEN_RESID", columnList = "RES_ID")
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
	public void calculateHashes() {
		String resourceType = getResourceType();
		String paramName = getParamName();
		String system = getSystem();
		String value = getValue();
		setHashIdentity(calculateHashIdentity(getPartitionSettings(), getPartitionId(), resourceType, paramName));
		setHashSystem(calculateHashSystem(getPartitionSettings(), getPartitionId(), resourceType, paramName, system));
		setHashSystemAndValue(calculateHashSystemAndValue(getPartitionSettings(), getPartitionId(), resourceType, paramName, system, value));
		setHashValue(calculateHashValue(getPartitionSettings(), getPartitionId(), resourceType, paramName, value));
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

	Long getHashSystem() {
		return myHashSystem;
	}

	private void setHashSystem(Long theHashSystem) {
		myHashSystem = theHashSystem;
	}

	private void setHashIdentity(Long theHashIdentity) {
		myHashIdentity = theHashIdentity;
	}

	Long getHashSystemAndValue() {
		return myHashSystemAndValue;
	}

	private void setHashSystemAndValue(Long theHashSystemAndValue) {
		myHashSystemAndValue = theHashSystemAndValue;
	}

	Long getHashValue() {
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
	}

	public String getValue() {
		return myValue;
	}

	public ResourceIndexedSearchParamToken setValue(String theValue) {
		myValue = StringUtils.defaultIfBlank(theValue, null);
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


}
