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
import ca.uhn.fhir.rest.param.UriParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.search.annotations.Field;

import javax.persistence.*;

//@formatter:off
@Embeddable
@Entity
@Table(name = "HFJ_SPIDX_URI", indexes = {
	@Index(name = "IDX_SP_URI", columnList = "RES_TYPE,SP_NAME,SP_URI"),
	@Index(name = "IDX_SP_URI_RESTYPE_NAME", columnList = "RES_TYPE,SP_NAME"),
	@Index(name = "IDX_SP_URI_UPDATED", columnList = "SP_UPDATED"),
	@Index(name = "IDX_SP_URI_COORDS", columnList = "RES_ID")
})
//@formatter:on
public class ResourceIndexedSearchParamUri extends BaseResourceIndexedSearchParam {

	/*
	 * Note that MYSQL chokes on unique indexes for lengths > 255 so be careful here 
	 */
	public static final int MAX_LENGTH = 255;

	private static final long serialVersionUID = 1L;
	@Column(name = "SP_URI", nullable = true, length = MAX_LENGTH)
	@Field()
	public String myUri;
	@Id
	@SequenceGenerator(name = "SEQ_SPIDX_URI", sequenceName = "SEQ_SPIDX_URI")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_SPIDX_URI")
	@Column(name = "SP_ID")
	private Long myId;

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

	@Override
	protected Long getId() {
		return myId;
	}

	public String getUri() {
		return myUri;
	}

	public void setUri(String theUri) {
		myUri = StringUtils.defaultIfBlank(theUri, null);
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(getParamName());
		b.append(getResource());
		b.append(getUri());
		return b.toHashCode();
	}

	@Override
	public IQueryParameterType toQueryParameterType() {
		return new UriParam(getUri());
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this);
		b.append("id", getId());
		b.append("resourceId", getResourcePid());
		b.append("paramName", getParamName());
		b.append("uri", myUri);
		return b.toString();
	}

}
