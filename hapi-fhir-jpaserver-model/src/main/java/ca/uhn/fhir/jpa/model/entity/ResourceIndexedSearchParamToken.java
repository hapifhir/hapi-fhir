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

import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.TokenParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.search.annotations.Field;

import javax.persistence.*;

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
	@Index(name = "IDX_SP_TOKEN_HASH", columnList = "HASH_IDENTITY"),
	@Index(name = "IDX_SP_TOKEN_HASH_S", columnList = "HASH_SYS"),
	@Index(name = "IDX_SP_TOKEN_HASH_SV", columnList = "HASH_SYS_AND_VALUE"),
	@Index(name = "IDX_SP_TOKEN_HASH_V", columnList = "HASH_VALUE"),

	@Index(name = "IDX_SP_TOKEN_UPDATED", columnList = "SP_UPDATED"),
	@Index(name = "IDX_SP_TOKEN_RESID", columnList = "RES_ID")
})
public class ResourceIndexedSearchParamToken extends BaseResourceIndexedSearchParam {

	public static final int MAX_LENGTH = 200;

	private static final long serialVersionUID = 1L;

	@Field()
	@Column(name = "SP_SYSTEM", nullable = true, length = MAX_LENGTH)
	public String mySystem;
	@Field()
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
	public ResourceIndexedSearchParamToken(String theResourceType, String theParamName, String theSystem, String theValue) {
		super();
		setResourceType(theResourceType);
		setParamName(theParamName);
		setSystem(theSystem);
		setValue(theValue);
	}

	@Override
	@PrePersist
	public void calculateHashes() {
		if (myHashSystem == null) {
			String resourceType = getResourceType();
			String paramName = getParamName();
			String system = getSystem();
			String value = getValue();
			setHashIdentity(calculateHashIdentity(resourceType, paramName));
			setHashSystem(calculateHashSystem(resourceType, paramName, system));
			setHashSystemAndValue(calculateHashSystemAndValue(resourceType, paramName, system, value));
			setHashValue(calculateHashValue(resourceType, paramName, value));
		}
	}

	@Override
	protected void clearHashes() {
		myHashSystem = null;
		myHashSystemAndValue = null;
		myHashValue = null;
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
		b.append(getSystem(), obj.getSystem());
		b.append(getValue(), obj.getValue());
		return b.isEquals();
	}

	Long getHashSystem() {
		calculateHashes();
		return myHashSystem;
	}

	private void setHashSystem(Long theHashSystem) {
		myHashSystem = theHashSystem;
	}

	private void setHashIdentity(Long theHashIdentity) {
		myHashIdentity = theHashIdentity;
	}

	Long getHashSystemAndValue() {
		calculateHashes();
		return myHashSystemAndValue;
	}

	private void setHashSystemAndValue(Long theHashSystemAndValue) {
		calculateHashes();
		myHashSystemAndValue = theHashSystemAndValue;
	}

	Long getHashValue() {
		calculateHashes();
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
		myId =theId;
	}

	public String getSystem() {
		return mySystem;
	}

	public void setSystem(String theSystem) {
		clearHashes();
		mySystem = StringUtils.defaultIfBlank(theSystem, null);
	}

	public String getValue() {
		return myValue;
	}

	public ResourceIndexedSearchParamToken setValue(String theValue) {
		clearHashes();
		myValue = StringUtils.defaultIfBlank(theValue, null);
		return this;
	}

	@Override
	public int hashCode() {
		calculateHashes();
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(getResourceType());
		b.append(getParamName());
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
		b.append("resourceType", getResourceType());
		b.append("paramName", getParamName());
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
		boolean retval = false;
		String valueString = defaultString(getValue());
		String tokenValueString = defaultString(token.getValue());

		// Only match on system if it wasn't specified
		if (token.getSystem() == null || token.getSystem().isEmpty()) {
			if (valueString.equalsIgnoreCase(tokenValueString)) {
				retval = true;
			}
		} else if (tokenValueString == null || tokenValueString.isEmpty()) {
			if (token.getSystem().equalsIgnoreCase(getSystem())) {
				retval = true;
			}
		} else {
			if (token.getSystem().equalsIgnoreCase(getSystem()) &&
				valueString.equalsIgnoreCase(tokenValueString)) {
				retval = true;
			}
		}
		return retval;
	}

	public static long calculateHashSystem(String theResourceType, String theParamName, String theSystem) {
		return hash(theResourceType, theParamName, trim(theSystem));
	}

	public static long calculateHashSystemAndValue(String theResourceType, String theParamName, String theSystem, String theValue) {
		return hash(theResourceType, theParamName, defaultString(trim(theSystem)), trim(theValue));
	}

	public static long calculateHashValue(String theResourceType, String theParamName, String theValue) {
		String value = trim(theValue);
		return hash(theResourceType, theParamName, value);
	}


}
