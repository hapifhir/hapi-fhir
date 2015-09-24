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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

//@formatter:off
@Entity
@Table(name = "HFJ_SPIDX_URI" /* , indexes = { @Index(name = "IDX_SP_TOKEN", columnList = "SP_SYSTEM,SP_VALUE") } */)
@org.hibernate.annotations.Table(appliesTo = "HFJ_SPIDX_URI", indexes = { 
	@org.hibernate.annotations.Index(name = "IDX_SP_URI", columnNames = { "RES_TYPE", "SP_NAME", "SP_URI" }) 
})
//@formatter:on
public class ResourceIndexedSearchParamUri extends BaseResourceIndexedSearchParam {

	public static final int MAX_LENGTH = 256;

	private static final long serialVersionUID = 1L;

	@Column(name = "SP_URI", nullable = true, length = MAX_LENGTH)
	public String myUri;

	public ResourceIndexedSearchParamUri() {
	}

	public ResourceIndexedSearchParamUri(String theName, String theUri) {
		setParamName(theName);
		setUri(theUri);
	}

	@Override
	public boolean equals(Object theObj) {
		if (this == theObj) {
			return true;
		}
		if (theObj == null) {
			return false;
		}
		if (!(theObj instanceof ResourceIndexedSearchParamUri)) {
			return false;
		}
		ResourceIndexedSearchParamUri obj = (ResourceIndexedSearchParamUri) theObj;
		EqualsBuilder b = new EqualsBuilder();
		b.append(getParamName(), obj.getParamName());
		b.append(getResource(), obj.getResource());
		b.append(getUri(), obj.getUri());
		return b.isEquals();
	}

	public String getUri() {
		return myUri;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(getParamName());
		b.append(getResource());
		b.append(getUri());
		return b.toHashCode();
	}

	public void setUri(String theUri) {
		myUri = StringUtils.defaultIfBlank(theUri, null);
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", getId());
		builder.append("paramName", getParamName());
		builder.append("uri", myUri);
		return builder.toString();
	}

}
