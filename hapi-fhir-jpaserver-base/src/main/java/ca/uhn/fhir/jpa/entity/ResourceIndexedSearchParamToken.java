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

import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.TokenParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.search.annotations.Field;

import javax.persistence.*;

@Embeddable
@Entity
@Table(name = "HFJ_SPIDX_TOKEN", indexes = {
	@Index(name = "IDX_SP_TOKEN", columnList = "RES_TYPE,SP_NAME,SP_SYSTEM,SP_VALUE"),
	@Index(name = "IDX_SP_TOKEN_UNQUAL", columnList = "RES_TYPE,SP_NAME,SP_VALUE"),
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
	public String myValue;
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


	/**
	 * Constructor
	 */
	public ResourceIndexedSearchParamToken() {
		super();
	}

	/**
	 * Constructor
	 */
	public ResourceIndexedSearchParamToken(String theName, String theSystem, String theValue) {
		super();
		setParamName(theName);
		setSystem(theSystem);
		setValue(theValue);
	}


	@PrePersist
	public void calculateHashes() {
		if (myHashSystem == null) {
			setHashSystem(hash(getResourceType(), getParamName(), getSystem()));
			setHashSystemAndValue(hash(getResourceType(), getParamName(), getSystem(), getValue()));
			setHashValue(hash(getResourceType(), getParamName(), getValue()));
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
		b.append(getParamName(), obj.getParamName());
		b.append(getResource(), obj.getResource());
		b.append(getSystem(), obj.getSystem());
		b.append(getValue(), obj.getValue());
		b.append(getHashSystem(), obj.getHashSystem());
		b.append(getHashSystemAndValue(), obj.getHashSystemAndValue());
		b.append(getHashValue(), obj.getHashValue());
		return b.isEquals();
	}

	public Long getHashSystem() {
		calculateHashes();
		return myHashSystem;
	}

	public void setHashSystem(Long theHashSystem) {
		myHashSystem = theHashSystem;
	}

	public Long getHashSystemAndValue() {
		calculateHashes();
		return myHashSystemAndValue;
	}

	public void setHashSystemAndValue(Long theHashSystemAndValue) {
		calculateHashes();
		myHashSystemAndValue = theHashSystemAndValue;
	}

	public Long getHashValue() {
		calculateHashes();
		return myHashValue;
	}

	public void setHashValue(Long theHashValue) {
		myHashValue = theHashValue;
	}

	@Override
	protected Long getId() {
		return myId;
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

	public void setValue(String theValue) {
		clearHashes();
		myValue = StringUtils.defaultIfBlank(theValue, null);
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(getParamName());
		b.append(getResource());
		b.append(getSystem());
		b.append(getValue());
		b.append(getHashSystem());
		b.append(getHashSystemAndValue());
		b.append(getHashValue());
		return b.toHashCode();
	}


	@Override
	public IQueryParameterType toQueryParameterType() {
		return new TokenParam(getSystem(), getValue());
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("paramName", getParamName());
		b.append("resourceId", getResourcePid());
		b.append("system", getSystem());
		b.append("value", getValue());
		return b.build();
	}
}
