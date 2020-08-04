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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.util.StringUtil;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.left;

//@formatter:off
@Embeddable
@Entity
@Table(name = "HFJ_SPIDX_STRING", indexes = {
	/*
	 * Note: We previously had indexes with the following names,
	 * do not reuse these names:
	 * IDX_SP_STRING
	 */

	// This is used for sorting, and for :contains queries currently
	@Index(name = "IDX_SP_STRING_HASH_IDENT", columnList = "HASH_IDENTITY"),

	@Index(name = "IDX_SP_STRING_HASH_NRM", columnList = "HASH_NORM_PREFIX,SP_VALUE_NORMALIZED"),
	@Index(name = "IDX_SP_STRING_HASH_EXCT", columnList = "HASH_EXACT"),

	@Index(name = "IDX_SP_STRING_UPDATED", columnList = "SP_UPDATED"),
	@Index(name = "IDX_SP_STRING_RESID", columnList = "RES_ID")
})
@Indexed()
public class ResourceIndexedSearchParamString extends BaseResourceIndexedSearchParam {

	/*
	 * Note that MYSQL chokes on unique indexes for lengths > 255 so be careful here
	 */
	public static final int MAX_LENGTH = 200;
	public static final int HASH_PREFIX_LENGTH = 1;
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "SEQ_SPIDX_STRING", sequenceName = "SEQ_SPIDX_STRING")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_SPIDX_STRING")
	@Column(name = "SP_ID")
	private Long myId;

	@ManyToOne(optional = false)
	@JoinColumn(name = "RES_ID", referencedColumnName = "RES_ID", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_SPIDXSTR_RESOURCE"))
	@ContainedIn
	private ResourceTable myResourceTable;

	@Column(name = "SP_VALUE_EXACT", length = MAX_LENGTH, nullable = true)
	@Fields({
		@Field(name = "myValueText", index = org.hibernate.search.annotations.Index.YES, store = Store.YES, analyze = Analyze.YES, analyzer = @Analyzer(definition = "standardAnalyzer")),
		@Field(name = "myValueTextEdgeNGram", index = org.hibernate.search.annotations.Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer = @Analyzer(definition = "autocompleteEdgeAnalyzer")),
		@Field(name = "myValueTextNGram", index = org.hibernate.search.annotations.Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer = @Analyzer(definition = "autocompleteNGramAnalyzer")),
		@Field(name = "myValueTextPhonetic", index = org.hibernate.search.annotations.Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer = @Analyzer(definition = "autocompletePhoneticAnalyzer"))
	})
	private String myValueExact;

	@Column(name = "SP_VALUE_NORMALIZED", length = MAX_LENGTH, nullable = true)
	private String myValueNormalized;
	/**
	 * @since 3.4.0 - At some point this should be made not-null
	 */
	@Column(name = "HASH_NORM_PREFIX", nullable = true)
	private Long myHashNormalizedPrefix;
	/**
	 * @since 3.6.0 - At some point this should be made not-null
	 */
	@Column(name = "HASH_IDENTITY", nullable = true)
	private Long myHashIdentity;
	/**
	 * @since 3.4.0 - At some point this should be made not-null
	 */
	@Column(name = "HASH_EXACT", nullable = true)
	private Long myHashExact;

	public ResourceIndexedSearchParamString() {
		super();
	}

	public ResourceIndexedSearchParamString(PartitionSettings thePartitionSettings, ModelConfig theModelConfig, String theResourceType, String theParamName, String theValueNormalized, String theValueExact) {
		setPartitionSettings(thePartitionSettings);
		setModelConfig(theModelConfig);
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
	public void calculateHashes() {
		String resourceType = getResourceType();
		String paramName = getParamName();
		String valueNormalized = getValueNormalized();
		String valueExact = getValueExact();
		setHashNormalizedPrefix(calculateHashNormalized(getPartitionSettings(), getPartitionId(), getModelConfig(), resourceType, paramName, valueNormalized));
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
		b.append(getResourceType(), obj.getResourceType());
		b.append(getParamName(), obj.getParamName());
		b.append(getValueExact(), obj.getValueExact());
		b.append(getHashIdentity(), obj.getHashIdentity());
		b.append(getHashExact(), obj.getHashExact());
		b.append(getHashNormalizedPrefix(), obj.getHashNormalizedPrefix());
		return b.isEquals();
	}

	private Long getHashIdentity() {
		return myHashIdentity;
	}

	public void setHashIdentity(Long theHashIdentity) {
		myHashIdentity = theHashIdentity;
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
			throw new IllegalArgumentException("Value is too long: " + theValueExact.length());
		}
		myValueExact = theValueExact;
		return this;
	}

	public String getValueNormalized() {
		return myValueNormalized;
	}

	public ResourceIndexedSearchParamString setValueNormalized(String theValueNormalized) {
		if (defaultString(theValueNormalized).length() > MAX_LENGTH) {
			throw new IllegalArgumentException("Value is too long: " + theValueNormalized.length());
		}
		myValueNormalized = theValueNormalized;
		return this;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(getResourceType());
		b.append(getParamName());
		b.append(getValueExact());
		return b.toHashCode();
	}

	@Override
	public IQueryParameterType toQueryParameterType() {
		return new StringParam(getValueExact());
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("paramName", getParamName());
		b.append("resourceId", getResourcePid());
		b.append("hashNormalizedPrefix", getHashNormalizedPrefix());
		b.append("valueNormalized", getValueNormalized());
		return b.build();
	}

	@Override
	public boolean matches(IQueryParameterType theParam, boolean theUseOrdinalDatesForDayComparison) {
		if (!(theParam instanceof StringParam)) {
			return false;
		}
		StringParam string = (StringParam) theParam;
		String normalizedString = StringUtil.normalizeStringForSearchIndexing(defaultString(string.getValue()));
		return defaultString(getValueNormalized()).startsWith(normalizedString);
	}

	public static long calculateHashExact(PartitionSettings thePartitionSettings, RequestPartitionId theRequestPartitionId, String theResourceType, String theParamName, String theValueExact) {
		return hash(thePartitionSettings, theRequestPartitionId, theResourceType, theParamName, theValueExact);
	}

	public static long calculateHashNormalized(PartitionSettings thePartitionSettings, RequestPartitionId theRequestPartitionId, ModelConfig theModelConfig, String theResourceType, String theParamName, String theValueNormalized) {
		/*
		 * If we're not allowing contained searches, we'll add the first
		 * bit of the normalized value to the hash. This helps to
		 * make the hash even more unique, which will be good for
		 * performance.
		 */
		int hashPrefixLength = HASH_PREFIX_LENGTH;
		if (theModelConfig.isAllowContainsSearches()) {
			hashPrefixLength = 0;
		}

		return hash(thePartitionSettings, theRequestPartitionId, theResourceType, theParamName, left(theValueNormalized, hashPrefixLength));
	}
}
