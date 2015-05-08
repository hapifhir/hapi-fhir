package ca.uhn.fhir.jpa.entity;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "HFJ_SPIDX_STRING"/*, indexes= {@Index(name="IDX_SP_STRING", columnList="SP_VALUE_NORMALIZED")}*/)
@org.hibernate.annotations.Table(appliesTo="HFJ_SPIDX_STRING",indexes= {
		@org.hibernate.annotations.Index(name="IDX_SP_STRING", columnNames= {"RES_TYPE", "SP_NAME", "SP_VALUE_NORMALIZED"})})
public class ResourceIndexedSearchParamString extends BaseResourceIndexedSearchParam {

	public static final int MAX_LENGTH = 100;

	private static final long serialVersionUID = 1L;

	@Column(name = "SP_VALUE_NORMALIZED", length = MAX_LENGTH, nullable = true)
	public String myValueNormalized;

	@Column(name="SP_VALUE_EXACT",length=100,nullable=true)
	public String myValueExact;
	
	public ResourceIndexedSearchParamString() {
	}

	public ResourceIndexedSearchParamString(String theName, String theValueNormalized, String theValueExact) {
		setParamName(theName);
		setValueNormalized(theValueNormalized);
		setValueExact(theValueExact);
	}

	public String getValueNormalized() {
		return myValueNormalized;
	}

	public void setValueNormalized(String theValueNormalized) {
		if (StringUtils.defaultString(theValueNormalized).length() > MAX_LENGTH) {
			throw new IllegalArgumentException("Value is too long: " + theValueNormalized.length());
		}
		myValueNormalized = theValueNormalized;
	}

	public String getValueExact() {
		return myValueExact;
	}

	public void setValueExact(String theValueExact) {
		if (StringUtils.defaultString(theValueExact).length() > MAX_LENGTH) {
			throw new IllegalArgumentException("Value is too long: " + theValueExact.length());
		}
		myValueExact = theValueExact;
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("paramName", getParamName());
		b.append("resourceId", getResource().getId()); // TODO: add a field so we don't need to resolve this
		b.append("value", getValueNormalized());
		return b.build();
	}

}
