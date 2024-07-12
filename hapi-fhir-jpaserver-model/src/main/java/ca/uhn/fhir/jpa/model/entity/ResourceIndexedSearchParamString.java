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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.listener.IndexStorageOptimizationListener;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.util.StringUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static ca.uhn.fhir.jpa.model.util.SearchParamHash.hashSearchParam;
import static org.apache.commons.lang3.StringUtils.defaultString;

// @formatter:off
@Embeddable
@EntityListeners(IndexStorageOptimizationListener.class)
@Entity
@Table(
		name = "HFJ_SPIDX_STRING",
		indexes = {
			/*
			 * Note: We previously had indexes with the following names,
			 * do not reuse these names:
			 * IDX_SP_STRING
			 */

			// This is used for sorting, and for :contains queries currently
			@Index(name = "IDX_SP_STRING_HASH_IDENT_V2", columnList = "HASH_IDENTITY,RES_ID,PARTITION_ID"),
			@Index(
					name = "IDX_SP_STRING_HASH_NRM_V2",
					columnList = "HASH_NORM_PREFIX,SP_VALUE_NORMALIZED,RES_ID,PARTITION_ID"),
			@Index(name = "IDX_SP_STRING_HASH_EXCT_V2", columnList = "HASH_EXACT,RES_ID,PARTITION_ID"),
			@Index(name = "IDX_SP_STRING_RESID_V2", columnList = "RES_ID,HASH_NORM_PREFIX,PARTITION_ID")
		})
public class ResourceIndexedSearchParamString extends BaseResourceIndexedSearchParam {

	/*
	 * Note that MYSQL chokes on unique indexes for lengths > 255 so be careful here
	 */
	public static final int MAX_LENGTH = 768;
	public static final int HASH_PREFIX_LENGTH = 1;
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "SEQ_SPIDX_STRING", sequenceName = "SEQ_SPIDX_STRING")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_SPIDX_STRING")
	@Column(name = "SP_ID")
	private Long myId;

	@ManyToOne(optional = false)
	@JoinColumn(
			name = "RES_ID",
			referencedColumnName = "RES_ID",
			nullable = false,
			foreignKey = @ForeignKey(name = "FK_SPIDXSTR_RESOURCE"))
	private ResourceTable myResource;

	@Column(name = "SP_VALUE_EXACT", length = MAX_LENGTH, nullable = true)
	private String myValueExact;

	@Column(name = "SP_VALUE_NORMALIZED", length = MAX_LENGTH, nullable = true)
	private String myValueNormalized;
	/**
	 * @since 3.4.0 - At some point this should be made not-null
	 */
	@Column(name = "HASH_NORM_PREFIX", nullable = true)
	private Long myHashNormalizedPrefix;
	/**
	 * @since 3.4.0 - At some point this should be made not-null
	 */
	@Column(name = "HASH_EXACT", nullable = true)
	private Long myHashExact;

	public ResourceIndexedSearchParamString() {
		super();
	}

	public ResourceIndexedSearchParamString(
			PartitionSettings thePartitionSettings,
			StorageSettings theStorageSettings,
			String theResourceType,
			String theParamName,
			String theValueNormalized,
			String theValueExact) {
		setPartitionSettings(thePartitionSettings);
		setStorageSettings(theStorageSettings);
		setResourceType(theResourceType);
		setParamName(theParamName);
		setValueNormalized(theValueNormalized);
		setValueExact(theValueExact);
		calculateHashes();
	}

	@Override
	public <T extends BaseResourceIndex> void copyMutableValuesFrom(T theSource) {
		super.copyMutableValuesFrom(theSource);
		ResourceIndexedSearchParamString source = (ResourceIndexedSearchParamString) theSource;
		myValueExact = source.myValueExact;
		myValueNormalized = source.myValueNormalized;
		myHashExact = source.myHashExact;
		myHashIdentity = source.myHashIdentity;
		myHashNormalizedPrefix = source.myHashNormalizedPrefix;
	}

	@Override
	public void clearHashes() {
		myHashIdentity = null;
		myHashNormalizedPrefix = null;
		myHashExact = null;
	}

	@Override
	public void calculateHashes() {
		if (myHashIdentity != null || myHashExact != null || myHashNormalizedPrefix != null) {
			return;
		}

		String resourceType = getResourceType();
		String paramName = getParamName();
		String valueNormalized = getValueNormalized();
		String valueExact = getValueExact();
		setHashNormalizedPrefix(calculateHashNormalized(
				getPartitionSettings(),
				getPartitionId(),
				getStorageSettings(),
				resourceType,
				paramName,
				valueNormalized));
		setHashExact(calculateHashExact(getPartitionSettings(), getPartitionId(), resourceType, paramName, valueExact));
		setHashIdentity(calculateHashIdentity(getPartitionSettings(), getPartitionId(), resourceType, paramName));
	}

	@Override
	public boolean equals(Object theObj) {
		if (this == theObj) {
			return true;
		}
		if (theObj == null) {
			return false;
		}
		if (!(theObj instanceof ResourceIndexedSearchParamString)) {
			return false;
		}
		ResourceIndexedSearchParamString obj = (ResourceIndexedSearchParamString) theObj;
		EqualsBuilder b = new EqualsBuilder();
		b.append(getValueExact(), obj.getValueExact());
		b.append(getHashIdentity(), obj.getHashIdentity());
		b.append(getHashExact(), obj.getHashExact());
		b.append(getHashNormalizedPrefix(), obj.getHashNormalizedPrefix());
		b.append(getValueNormalized(), obj.getValueNormalized());
		b.append(isMissing(), obj.isMissing());
		return b.isEquals();
	}

	public Long getHashExact() {
		return myHashExact;
	}

	public void setHashExact(Long theHashExact) {
		myHashExact = theHashExact;
	}

	public Long getHashNormalizedPrefix() {
		return myHashNormalizedPrefix;
	}

	public void setHashNormalizedPrefix(Long theHashNormalizedPrefix) {
		myHashNormalizedPrefix = theHashNormalizedPrefix;
	}

	@Override
	public Long getId() {
		return myId;
	}

	@Override
	public void setId(Long theId) {
		myId = theId;
	}

	public String getValueExact() {
		return myValueExact;
	}

	public ResourceIndexedSearchParamString setValueExact(String theValueExact) {
		if (defaultString(theValueExact).length() > MAX_LENGTH) {
			throw new IllegalArgumentException(Msg.code(1529) + "Value is too long: " + theValueExact.length());
		}
		myValueExact = theValueExact;
		return this;
	}

	public String getValueNormalized() {
		return myValueNormalized;
	}

	public ResourceIndexedSearchParamString setValueNormalized(String theValueNormalized) {
		if (defaultString(theValueNormalized).length() > MAX_LENGTH) {
			throw new IllegalArgumentException(Msg.code(1530) + "Value is too long: " + theValueNormalized.length());
		}
		myValueNormalized = theValueNormalized;
		return this;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(getValueExact());
		b.append(getHashIdentity());
		b.append(getHashExact());
		b.append(getHashNormalizedPrefix());
		b.append(getValueNormalized());
		b.append(isMissing());
		return b.toHashCode();
	}

	@Override
	public IQueryParameterType toQueryParameterType() {
		return new StringParam(getValueExact());
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("resourceType", getResourceType());
		b.append("paramName", getParamName());
		b.append("resourceId", getResourcePid());
		b.append("hashIdentity", getHashIdentity());
		b.append("hashNormalizedPrefix", getHashNormalizedPrefix());
		b.append("valueNormalized", getValueNormalized());
		b.append("partitionId", getPartitionId());
		return b.build();
	}

	@Override
	public boolean matches(IQueryParameterType theParam) {
		if (!(theParam instanceof StringParam)) {
			return false;
		}
		StringParam string = (StringParam) theParam;
		String normalizedString = StringUtil.normalizeStringForSearchIndexing(defaultString(string.getValue()));
		return defaultString(getValueNormalized()).startsWith(normalizedString);
	}

	public static long calculateHashExact(
			PartitionSettings thePartitionSettings,
			PartitionablePartitionId theRequestPartitionId,
			String theResourceType,
			String theParamName,
			String theValueExact) {
		RequestPartitionId requestPartitionId = PartitionablePartitionId.toRequestPartitionId(theRequestPartitionId);
		return calculateHashExact(
				thePartitionSettings, requestPartitionId, theResourceType, theParamName, theValueExact);
	}

	public static long calculateHashExact(
			PartitionSettings thePartitionSettings,
			RequestPartitionId theRequestPartitionId,
			String theResourceType,
			String theParamName,
			String theValueExact) {
		return hashSearchParam(
				thePartitionSettings, theRequestPartitionId, theResourceType, theParamName, theValueExact);
	}

	public static long calculateHashNormalized(
			PartitionSettings thePartitionSettings,
			PartitionablePartitionId theRequestPartitionId,
			StorageSettings theStorageSettings,
			String theResourceType,
			String theParamName,
			String theValueNormalized) {
		RequestPartitionId requestPartitionId = PartitionablePartitionId.toRequestPartitionId(theRequestPartitionId);
		return calculateHashNormalized(
				thePartitionSettings,
				requestPartitionId,
				theStorageSettings,
				theResourceType,
				theParamName,
				theValueNormalized);
	}

	public static long calculateHashNormalized(
			PartitionSettings thePartitionSettings,
			RequestPartitionId theRequestPartitionId,
			StorageSettings theStorageSettings,
			String theResourceType,
			String theParamName,
			String theValueNormalized) {
		/*
		 * If we're not allowing contained searches, we'll add the first
		 * bit of the normalized value to the hash. This helps to
		 * make the hash even more unique, which will be good for
		 * performance.
		 */
		int hashPrefixLength = HASH_PREFIX_LENGTH;
		if (theStorageSettings.isAllowContainsSearches()) {
			hashPrefixLength = 0;
		}

		String value = StringUtil.left(theValueNormalized, hashPrefixLength);
		return hashSearchParam(thePartitionSettings, theRequestPartitionId, theResourceType, theParamName, value);
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
