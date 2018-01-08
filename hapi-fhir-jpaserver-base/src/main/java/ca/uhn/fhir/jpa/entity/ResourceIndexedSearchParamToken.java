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

//@formatter:off
@Embeddable
@Entity
@Table(name = "HFJ_SPIDX_TOKEN", indexes = {
	@Index(name = "IDX_SP_TOKEN", columnList = "RES_TYPE,SP_NAME,SP_SYSTEM,SP_VALUE"),
	@Index(name = "IDX_SP_TOKEN_UNQUAL", columnList = "RES_TYPE,SP_NAME,SP_VALUE"),
	@Index(name = "IDX_SP_TOKEN_UPDATED", columnList = "SP_UPDATED"),
	@Index(name = "IDX_SP_TOKEN_RESID", columnList = "RES_ID")
})
//@formatter:on
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

	public ResourceIndexedSearchParamToken() {
	}

	public ResourceIndexedSearchParamToken(String theName, String theSystem, String theValue) {
		setParamName(theName);
		setSystem(theSystem);
		setValue(theValue);
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
		return b.isEquals();
	}

	@Override
	protected Long getId() {
		return myId;
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

	public void setValue(String theValue) {
		myValue = StringUtils.defaultIfBlank(theValue, null);
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(getParamName());
		b.append(getResource());
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
		b.append("paramName", getParamName());
		b.append("resourceId", getResourcePid());
		b.append("system", getSystem());
		b.append("value", getValue());
		return b.build();
	}
}
