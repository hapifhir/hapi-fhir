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
import ca.uhn.fhir.rest.param.UriParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.search.annotations.Field;

import javax.persistence.*;

import static org.apache.commons.lang3.StringUtils.defaultString;

@Embeddable
@Entity
@Table(name = "HFJ_SPIDX_URI", indexes = {
	@Index(name = "IDX_SP_URI", columnList = "RES_TYPE,SP_NAME,SP_URI"),
	@Index(name = "IDX_SP_URI_HASH_IDENTITY", columnList = "HASH_IDENTITY,SP_URI"),
	@Index(name = "IDX_SP_URI_HASH_URI", columnList = "HASH_URI"),
	@Index(name = "IDX_SP_URI_RESTYPE_NAME", columnList = "RES_TYPE,SP_NAME"),
	@Index(name = "IDX_SP_URI_UPDATED", columnList = "SP_UPDATED"),
	@Index(name = "IDX_SP_URI_COORDS", columnList = "RES_ID")
})
public class ResourceIndexedSearchParamUri extends BaseResourceIndexedSearchParam {

	/*
	 * Note that MYSQL chokes on unique indexes for lengths > 255 so be careful here
	 */
	public static final int MAX_LENGTH = 254;

	private static final long serialVersionUID = 1L;
	@Column(name = "SP_URI", nullable = true, length = MAX_LENGTH)
	@Field()
	public String myUri;

	@Id
	@SequenceGenerator(name = "SEQ_SPIDX_URI", sequenceName = "SEQ_SPIDX_URI")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_SPIDX_URI")
	@Column(name = "SP_ID")
	private Long myId;
	/**
	 * @since 3.4.0 - At some point this should be made not-null
	 */
	@Column(name = "HASH_URI", nullable = true)
	private Long myHashUri;
	/**
	 * @since 3.5.0 - At some point this should be made not-null
	 */
	@Column(name = "HASH_IDENTITY", nullable = true)
	private Long myHashIdentity;

	/**
	 * Constructor
	 */
	public ResourceIndexedSearchParamUri() {
		super();
	}

	/**
	 * Constructor
	 */
	public ResourceIndexedSearchParamUri(String theResourceType, String theParamName, String theUri) {
		setResourceType(theResourceType);
		setParamName(theParamName);
		setUri(theUri);
	}

	@Override
	@PrePersist
	public void calculateHashes() {
		if (myHashUri == null) {
			String resourceType = getResourceType();
			String paramName = getParamName();
			String uri = getUri();
			setHashIdentity(calculateHashIdentity(resourceType, paramName));
			setHashUri(calculateHashUri(resourceType, paramName, uri));
		}
	}

	@Override
	protected void clearHashes() {
		myHashUri = null;
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
		b.append(getResourceType(), obj.getResourceType());
		b.append(getParamName(), obj.getParamName());
		b.append(getUri(), obj.getUri());
		b.append(getHashUri(), obj.getHashUri());
		b.append(getHashIdentity(), obj.getHashIdentity());
		return b.isEquals();
	}

	private Long getHashIdentity() {
		calculateHashes();
		return myHashIdentity;
	}

	private void setHashIdentity(long theHashIdentity) {
		myHashIdentity = theHashIdentity;
	}

	public Long getHashUri() {
		calculateHashes();
		return myHashUri;
	}

	public void setHashUri(Long theHashUri) {
		myHashUri = theHashUri;
	}

	@Override
	public Long getId() {
		return myId;
	}

	@Override
	public void setId(Long theId) {
		myId =theId;
	}


	public String getUri() {
		return myUri;
	}

	public ResourceIndexedSearchParamUri setUri(String theUri) {
		myUri = StringUtils.defaultIfBlank(theUri, null);
		return this;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(getResourceType());
		b.append(getParamName());
		b.append(getUri());
		b.append(getHashUri());
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

	@Override
	public boolean matches(IQueryParameterType theParam) {
		if (!(theParam instanceof UriParam)) {
			return false;
		}
		UriParam uri = (UriParam) theParam;
		return defaultString(getUri()).equalsIgnoreCase(uri.getValueNotNull());
	}

	public static long calculateHashUri(String theResourceType, String theParamName, String theUri) {
		return hash(theResourceType, theParamName, theUri);
	}


}
