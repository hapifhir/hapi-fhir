package ca.uhn.fhir.jpa.entity;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import org.apache.commons.lang3.StringUtils;
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

//@formatter:off
@Embeddable
@Entity
@Table(name = "HFJ_SPIDX_STRING", indexes = { 
	@Index(name = "IDX_SP_STRING", columnList = "RES_TYPE,SP_NAME,SP_VALUE_NORMALIZED"), 
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

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SEQ_SPIDX_STRING", sequenceName="SEQ_SPIDX_STRING")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_SPIDX_STRING")
	@Column(name = "SP_ID")
	private Long myId;

	@ManyToOne(optional = false)
	@JoinColumn(name = "RES_ID", referencedColumnName="RES_ID", insertable=false, updatable=false, foreignKey=@ForeignKey(name="FK_SPIDXSTR_RESOURCE"))
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

	public ResourceIndexedSearchParamString() {
	}


	public ResourceIndexedSearchParamString(String theName, String theValueNormalized, String theValueExact) {
		setParamName(theName);
		setValueNormalized(theValueNormalized);
		setValueExact(theValueExact);
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
		return b.isEquals();
	}

	@Override
	protected Long getId() {
		return myId;
	}

	public String getValueExact() {
		return myValueExact;
	}

	public String getValueNormalized() {
		return myValueNormalized;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(getParamName());
		b.append(getResource());
		b.append(getValueExact());
		return b.toHashCode();
	}

	public void setValueExact(String theValueExact) {
		if (StringUtils.defaultString(theValueExact).length() > MAX_LENGTH) {
			throw new IllegalArgumentException("Value is too long: " + theValueExact.length());
		}
		myValueExact = theValueExact;
	}

	public void setValueNormalized(String theValueNormalized) {
		if (StringUtils.defaultString(theValueNormalized).length() > MAX_LENGTH) {
			throw new IllegalArgumentException("Value is too long: " + theValueNormalized.length());
		}
		myValueNormalized = theValueNormalized;
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
