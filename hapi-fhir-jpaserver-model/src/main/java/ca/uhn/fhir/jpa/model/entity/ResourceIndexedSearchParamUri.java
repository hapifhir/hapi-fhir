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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.listener.IndexStorageOptimizationListener;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.UriParam;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

import static ca.uhn.fhir.jpa.model.util.SearchParamHash.hashSearchParam;
import static org.apache.commons.lang3.StringUtils.defaultString;

@Embeddable
@EntityListeners(IndexStorageOptimizationListener.class)
@Entity
@Table(
		name = "HFJ_SPIDX_URI",
		indexes = {
			// for queries
			@Index(name = "IDX_SP_URI_HASH_URI_V2", columnList = "HASH_URI,RES_ID,PARTITION_ID"),
			// for sorting
			@Index(name = "IDX_SP_URI_HASH_IDENTITY_V2", columnList = "HASH_IDENTITY,SP_URI,RES_ID,PARTITION_ID"),
			// for index create/delete
			@Index(name = "IDX_SP_URI_COORDS", columnList = "RES_ID")
		})
public class ResourceIndexedSearchParamUri extends BaseResourceIndexedSearchParam {

	/*
	 * Be careful when modifying this value
	 * MySQL chokes on indexes with combined column length greater than 3052 bytes (768 chars)
	 * https://dev.mysql.com/doc/refman/8.0/en/innodb-limits.html
	 */
	public static final int MAX_LENGTH = 500;

	private static final long serialVersionUID = 1L;

	@Column(name = "SP_URI", nullable = true, length = MAX_LENGTH)
	@FullTextField
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

	@ManyToOne(
			optional = false,
			fetch = FetchType.LAZY,
			cascade = {})
	@JoinColumn(
			foreignKey = @ForeignKey(name = "FKGXSREUTYMMFJUWDSWV3Y887DO"),
			name = "RES_ID",
			referencedColumnName = "RES_ID",
			nullable = false)
	private ResourceTable myResource;

	/**
	 * Constructor
	 */
	public ResourceIndexedSearchParamUri() {
		super();
	}

	/**
	 * Constructor
	 */
	public ResourceIndexedSearchParamUri(
			PartitionSettings thePartitionSettings, String theResourceType, String theParamName, String theUri) {
		setPartitionSettings(thePartitionSettings);
		setResourceType(theResourceType);
		setParamName(theParamName);
		setUri(theUri);
		calculateHashes();
	}

	@Override
	public <T extends BaseResourceIndex> void copyMutableValuesFrom(T theSource) {
		super.copyMutableValuesFrom(theSource);
		ResourceIndexedSearchParamUri source = (ResourceIndexedSearchParamUri) theSource;
		myUri = source.myUri;
		myHashUri = source.myHashUri;
		myHashIdentity = source.myHashIdentity;
	}

	@Override
	public void clearHashes() {
		myHashIdentity = null;
		myHashUri = null;
	}

	@Override
	public void calculateHashes() {
		if (myHashIdentity != null || myHashUri != null) {
			return;
		}

		String resourceType = getResourceType();
		String paramName = getParamName();
		String uri = getUri();
		setHashIdentity(calculateHashIdentity(getPartitionSettings(), getPartitionId(), resourceType, paramName));
		setHashUri(calculateHashUri(getPartitionSettings(), getPartitionId(), resourceType, paramName, uri));
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
		b.append(getUri(), obj.getUri());
		b.append(getHashUri(), obj.getHashUri());
		b.append(getHashIdentity(), obj.getHashIdentity());
		b.append(isMissing(), obj.isMissing());
		return b.isEquals();
	}

	public Long getHashUri() {
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
		myId = theId;
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
		b.append(getUri());
		b.append(getHashUri());
		b.append(getHashIdentity());
		b.append(isMissing());
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
		b.append("hashUri", myHashUri);
		b.append("hashIdentity", myHashIdentity);
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

	public static long calculateHashUri(
			PartitionSettings thePartitionSettings,
			PartitionablePartitionId theRequestPartitionId,
			String theResourceType,
			String theParamName,
			String theUri) {
		RequestPartitionId requestPartitionId = PartitionablePartitionId.toRequestPartitionId(theRequestPartitionId);
		return calculateHashUri(thePartitionSettings, requestPartitionId, theResourceType, theParamName, theUri);
	}

	public static long calculateHashUri(
			PartitionSettings thePartitionSettings,
			RequestPartitionId theRequestPartitionId,
			String theResourceType,
			String theParamName,
			String theUri) {
		return hashSearchParam(thePartitionSettings, theRequestPartitionId, theResourceType, theParamName, theUri);
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
