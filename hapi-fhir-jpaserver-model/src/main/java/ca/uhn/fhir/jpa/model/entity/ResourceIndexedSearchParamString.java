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
import ca.uhn.fhir.jpa.model.search.hash.ResourceIndexHasher;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.util.StringUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
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
import org.apache.commons.lang3.builder.ToStringStyle;

import static org.apache.commons.lang3.StringUtils.defaultString;

// @formatter:off
@Embeddable
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
	public static final int MAX_LENGTH = 200;
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
			String theResourceType, String theParamName, String theValueNormalized, String theValueExact) {
		setResourceType(theResourceType);
		setParamName(theParamName);
		setValueNormalized(theValueNormalized);
		setValueExact(theValueExact);
	}

	@Override
	public <T extends BaseResourceIndex> void copyMutableValuesFrom(T theSource) {
		super.copyMutableValuesFrom(theSource);
		ResourceIndexedSearchParamString source = (ResourceIndexedSearchParamString) theSource;
		// 1. copy hash values first
		myHashExact = source.myHashExact;
		myHashNormalizedPrefix = source.myHashNormalizedPrefix;
		// 2. copy fields that are part of the hash next
		myValueExact = source.myValueExact;
		myValueNormalized = source.myValueNormalized;
	}

	@Override
	public void clearHashes() {
		super.clearHashes();
		myHashNormalizedPrefix = null;
		myHashExact = null;
	}

	@Override
	public void calculateHashes(ResourceIndexHasher theHasher) {
		super.calculateHashes(theHasher);
		if (isHashPopulated(myHashNormalizedPrefix) && isHashPopulated(myHashExact)) {
			return;
		}
		String resourceType = getResourceType();
		String paramName = getParamName();
		String valueNormalized = getValueNormalized();
		String valueExact = getValueExact();
		boolean isContained = isContained();
		PartitionablePartitionId partitionId = getPartitionId();
		myHashNormalizedPrefix =
				theHasher.hashNormalized(partitionId, isContained, resourceType, paramName, valueNormalized);
		myHashExact = theHasher.hash(partitionId, isContained, resourceType, paramName, valueExact);
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
		b.append(getResourceType(), obj.getResourceType());
		b.append(getParamName(), obj.getParamName());
		b.append(isMissing(), obj.isMissing());
		b.append(getContainedOrd(), obj.getContainedOrd());
		b.append(getPartitionId(), obj.getPartitionId());
		b.append(getValueExact(), obj.getValueExact());
		b.append(getValueNormalized(), obj.getValueNormalized());
		return b.isEquals();
	}

	public Long getHashExact() {
		return myHashExact;
	}

	public Long getHashNormalizedPrefix() {
		return myHashNormalizedPrefix;
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
		if (!StringUtils.equals(myValueExact, theValueExact)) {
			myValueExact = theValueExact;
			clearHashes();
		}
		return this;
	}

	public String getValueNormalized() {
		return myValueNormalized;
	}

	public ResourceIndexedSearchParamString setValueNormalized(String theValueNormalized) {
		if (defaultString(theValueNormalized).length() > MAX_LENGTH) {
			throw new IllegalArgumentException(Msg.code(1530) + "Value is too long: " + theValueNormalized.length());
		}
		if (!StringUtils.equals(myValueNormalized, theValueNormalized)) {
			myValueNormalized = theValueNormalized;
			clearHashes();
		}
		return this;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(getResourceType());
		b.append(getParamName());
		b.append(isMissing());
		b.append(getContainedOrd());
		b.append(getPartitionId());
		b.append(getValueExact());
		b.append(getValueNormalized());
		return b.toHashCode();
	}

	@Override
	public IQueryParameterType toQueryParameterType() {
		return new StringParam(getValueExact());
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("resourceId", getResourcePid());
		b.append("resourceType", getResourceType());
		b.append("paramName", getParamName());
		b.append("missing", isMissing());
		b.append("containedOrd", getContainedOrd());
		b.append("partitionId", getPartitionId());
		b.append("valueExact", getValueExact());
		b.append("valueNormalized", getValueNormalized());
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
