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

import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.StringParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import javax.persistence.Index;

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

	// This one us used only for sorting
	@Index(name = "IDX_SP_STRING_HASH_IDENT", columnList = "HASH_IDENTITY"),

	@Index(name = "IDX_SP_STRING_HASH_NRM", columnList = "HASH_NORM_PREFIX,SP_VALUE_NORMALIZED"),
	@Index(name = "IDX_SP_STRING_HASH_EXCT", columnList = "HASH_EXACT"),

	@Index(name = "IDX_SP_STRING_UPDATED", columnList = "SP_UPDATED"),
	@Index(name = "IDX_SP_STRING_RESID", columnList = "RES_ID")
})
@Indexed()
//@AnalyzerDefs({
//	@AnalyzerDef(name = "autocompleteEdgeAnalyzer",
//		tokenizer = @TokenizerDef(factory = PatternTokenizerFactory.class, params= {
//			@Parameter(name="pattern", value="(.*)"),
//			@Parameter(name="group", value="1")
//		}),
//		filters = {
//			@TokenFilterDef(factory = LowerCaseFilterFactory.class),
//			@TokenFilterDef(factory = StopFilterFactory.class),
//			@TokenFilterDef(factory = EdgeNGramFilterFactory.class, params = {
//				@Parameter(name = "minGramSize", value = "3"),
//				@Parameter(name = "maxGramSize", value = "50") 
//			}), 
//		}),
//	@AnalyzerDef(name = "autocompletePhoneticAnalyzer",
//		tokenizer = @TokenizerDef(factory=StandardTokenizerFactory.class),
//		filters = {
//			@TokenFilterDef(factory=StandardFilterFactory.class),
//			@TokenFilterDef(factory=StopFilterFactory.class),
//			@TokenFilterDef(factory=PhoneticFilterFactory.class, params = {
//				@Parameter(name="encoder", value="DoubleMetaphone")
//			}),
//			@TokenFilterDef(factory=SnowballPorterFilterFactory.class, params = {
//				@Parameter(name="language", value="English") 
//			})
//		}),
//	@AnalyzerDef(name = "autocompleteNGramAnalyzer",
//		tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
//		filters = {
//			@TokenFilterDef(factory = WordDelimiterFilterFactory.class),
//			@TokenFilterDef(factory = LowerCaseFilterFactory.class),
//			@TokenFilterDef(factory = NGramFilterFactory.class, params = {
//				@Parameter(name = "minGramSize", value = "3"),
//				@Parameter(name = "maxGramSize", value = "20") 
//			}),
//		}),
//	@AnalyzerDef(name = "standardAnalyzer",
//		tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
//		filters = {
//			@TokenFilterDef(factory = LowerCaseFilterFactory.class),
//		}) // Def
//	}
//)
//@formatter:on
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
	@Transient
	private transient DaoConfig myDaoConfig;
	public ResourceIndexedSearchParamString() {
		super();
	}

	public ResourceIndexedSearchParamString(DaoConfig theDaoConfig, String theName, String theValueNormalized, String theValueExact) {
		setDaoConfig(theDaoConfig);
		setParamName(theName);
		setValueNormalized(theValueNormalized);
		setValueExact(theValueExact);
	}

	public void setHashIdentity(Long theHashIdentity) {
		myHashIdentity = theHashIdentity;
	}

	@Override
	@PrePersist
	public void calculateHashes() {
		if (myHashNormalizedPrefix == null && myDaoConfig != null) {
			String resourceType = getResourceType();
			String paramName = getParamName();
			String valueNormalized = getValueNormalized();
			String valueExact = getValueExact();
			setHashNormalizedPrefix(calculateHashNormalized(myDaoConfig, resourceType, paramName, valueNormalized));
			setHashExact(calculateHashExact(resourceType, paramName, valueExact));
			setHashIdentity(calculateHashIdentity(resourceType, paramName));
		}
	}

	@Override
	protected void clearHashes() {
		myHashNormalizedPrefix = null;
		myHashExact = null;
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
		b.append(getParamName(), obj.getParamName());
		b.append(getResource(), obj.getResource());
		b.append(getValueExact(), obj.getValueExact());
		b.append(getHashExact(), obj.getHashExact());
		b.append(getHashNormalizedPrefix(), obj.getHashNormalizedPrefix());
		return b.isEquals();
	}

	public Long getHashExact() {
		calculateHashes();
		return myHashExact;
	}

	public void setHashExact(Long theHashExact) {
		myHashExact = theHashExact;
	}

	public Long getHashNormalizedPrefix() {
		calculateHashes();
		return myHashNormalizedPrefix;
	}

	public void setHashNormalizedPrefix(Long theHashNormalizedPrefix) {
		myHashNormalizedPrefix = theHashNormalizedPrefix;
	}

	@Override
	protected Long getId() {
		return myId;
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

	public String getValueNormalized() {
		return myValueNormalized;
	}

	public void setValueNormalized(String theValueNormalized) {
		if (StringUtils.defaultString(theValueNormalized).length() > MAX_LENGTH) {
			throw new IllegalArgumentException("Value is too long: " + theValueNormalized.length());
		}
		myValueNormalized = theValueNormalized;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(getParamName());
		b.append(getResource());
		b.append(getValueExact());
		return b.toHashCode();
	}

	public BaseResourceIndexedSearchParam setDaoConfig(DaoConfig theDaoConfig) {
		myDaoConfig = theDaoConfig;
		return this;
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
		b.append("value", getValueNormalized());
		return b.build();
	}

	public static long calculateHashExact(String theResourceType, String theParamName, String theValueExact) {
		return hash(theResourceType, theParamName, theValueExact);
	}

	public static long calculateHashNormalized(DaoConfig theDaoConfig, String theResourceType, String theParamName, String theValueNormalized) {
		/*
		 * If we're not allowing contained searches, we'll add the first
		 * bit of the normalized value to the hash. This helps to
		 * make the hash even more unique, which will be good for
		 * performance.
		 */
		int hashPrefixLength = HASH_PREFIX_LENGTH;
		if (theDaoConfig.isAllowContainsSearches()) {
			hashPrefixLength = 0;
		}

		return hash(theResourceType, theParamName, left(theValueNormalized, hashPrefixLength));
	}

	@Override
	public boolean matches(IQueryParameterType theParam) {
		if (!(theParam instanceof StringParam)) {
			return false;
		}
		StringParam string = (StringParam)theParam;
		String normalizedString = BaseHapiFhirDao.normalizeString(string.getValue());
		return getValueNormalized().startsWith(normalizedString);
	}
}
