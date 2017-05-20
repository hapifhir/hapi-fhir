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
import static org.apache.commons.lang3.StringUtils.defaultString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterFilterFactory;
import org.apache.lucene.analysis.ngram.EdgeNGramFilterFactory;
import org.apache.lucene.analysis.ngram.NGramFilterFactory;
import org.apache.lucene.analysis.pattern.PatternTokenizerFactory;
import org.apache.lucene.analysis.phonetic.PhoneticFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.AnalyzerDefs;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;

import ca.uhn.fhir.jpa.search.IndexNonDeletedInterceptor;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;

//@formatter:off
@Indexed(interceptor=IndexNonDeletedInterceptor.class)	
@Entity
@Table(name = "HFJ_RESOURCE", uniqueConstraints = {}, indexes= {
	@Index(name = "IDX_RES_DATE", columnList="RES_UPDATED"), 
	@Index(name = "IDX_RES_LANG", columnList="RES_TYPE,RES_LANGUAGE"), 
	@Index(name = "IDX_RES_PROFILE", columnList="RES_PROFILE"),
	@Index(name = "IDX_RES_TYPE", columnList="RES_TYPE"),
	@Index(name = "IDX_INDEXSTATUS", columnList="SP_INDEX_STATUS")
})
@AnalyzerDefs({
	@AnalyzerDef(name = "autocompleteEdgeAnalyzer",
		tokenizer = @TokenizerDef(factory = PatternTokenizerFactory.class, params= {
			@Parameter(name="pattern", value="(.*)"),
			@Parameter(name="group", value="1")
		}),
		filters = {
			@TokenFilterDef(factory = LowerCaseFilterFactory.class),
			@TokenFilterDef(factory = StopFilterFactory.class),
			@TokenFilterDef(factory = EdgeNGramFilterFactory.class, params = {
				@Parameter(name = "minGramSize", value = "3"),
				@Parameter(name = "maxGramSize", value = "50") 
			}), 
		}),
	@AnalyzerDef(name = "autocompletePhoneticAnalyzer",
		tokenizer = @TokenizerDef(factory=StandardTokenizerFactory.class),
		filters = {
			@TokenFilterDef(factory=StandardFilterFactory.class),
			@TokenFilterDef(factory=StopFilterFactory.class),
			@TokenFilterDef(factory=PhoneticFilterFactory.class, params = {
				@Parameter(name="encoder", value="DoubleMetaphone")
			}),
			@TokenFilterDef(factory=SnowballPorterFilterFactory.class, params = {
				@Parameter(name="language", value="English") 
			})
		}),
	@AnalyzerDef(name = "autocompleteNGramAnalyzer",
		tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
		filters = {
			@TokenFilterDef(factory = WordDelimiterFilterFactory.class),
			@TokenFilterDef(factory = LowerCaseFilterFactory.class),
			@TokenFilterDef(factory = NGramFilterFactory.class, params = {
				@Parameter(name = "minGramSize", value = "3"),
				@Parameter(name = "maxGramSize", value = "20") 
			}),
		}),
	@AnalyzerDef(name = "standardAnalyzer",
		tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
		filters = {
			@TokenFilterDef(factory = LowerCaseFilterFactory.class),
		}),
	@AnalyzerDef(name = "exactAnalyzer",
		tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
		filters = {
		})
	}
)
//@formatter:on
public class ResourceTable extends BaseHasResource implements Serializable {
	private static final int MAX_LANGUAGE_LENGTH = 20;
	private static final int MAX_PROFILE_LENGTH = 200;

	static final int RESTYPE_LEN = 30;

	private static final long serialVersionUID = 1L;

	/**
	 * Holds the narrative text only - Used for Fulltext searching but not directly stored in the DB
	 */
	//@formatter:off
	@Transient()
	@Fields({
		@Field(name = "myContentText", index = org.hibernate.search.annotations.Index.YES, store = Store.YES, analyze = Analyze.YES, analyzer = @Analyzer(definition = "standardAnalyzer")),
		@Field(name = "myContentTextEdgeNGram", index = org.hibernate.search.annotations.Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer = @Analyzer(definition = "autocompleteEdgeAnalyzer")),
		@Field(name = "myContentTextNGram", index = org.hibernate.search.annotations.Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer = @Analyzer(definition = "autocompleteNGramAnalyzer")),
		@Field(name = "myContentTextPhonetic", index = org.hibernate.search.annotations.Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer = @Analyzer(definition = "autocompletePhoneticAnalyzer"))
	})
	//@formatter:on
	private String myContentText;

	@Column(name = "HASH_SHA256", length=64, nullable=true)
	private String myHashSha256;

	@Column(name = "SP_HAS_LINKS")
	private boolean myHasLinks;

	@Id
	@SequenceGenerator(name = "SEQ_RESOURCE_ID", sequenceName = "SEQ_RESOURCE_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_RESOURCE_ID")
	@Column(name = "RES_ID")
	private Long myId;

	@OneToMany(mappedBy = "myTargetResource", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = false)
	private Collection<ResourceLink> myIncomingResourceLinks;

	@Column(name = "SP_INDEX_STATUS", nullable = true)
	private Long myIndexStatus;

	@Column(name = "RES_LANGUAGE", length = MAX_LANGUAGE_LENGTH, nullable = true)
	private String myLanguage;

	/**
	 * Holds the narrative text only - Used for Fulltext searching but not directly stored in the DB
	 */
	@Transient()
	@Fields({
		@Field(name = "myNarrativeText", index = org.hibernate.search.annotations.Index.YES, store = Store.YES, analyze = Analyze.YES, analyzer = @Analyzer(definition = "standardAnalyzer")),
		@Field(name = "myNarrativeTextEdgeNGram", index = org.hibernate.search.annotations.Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer = @Analyzer(definition = "autocompleteEdgeAnalyzer")),
		@Field(name = "myNarrativeTextNGram", index = org.hibernate.search.annotations.Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer = @Analyzer(definition = "autocompleteNGramAnalyzer")),
		@Field(name = "myNarrativeTextPhonetic", index = org.hibernate.search.annotations.Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer = @Analyzer(definition = "autocompletePhoneticAnalyzer"))
	})
	private String myNarrativeText;

	@OneToMany(mappedBy = "myResource", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = false)
	private Collection<ResourceIndexedSearchParamCoords> myParamsCoords;

	@Column(name = "SP_COORDS_PRESENT")
	private boolean myParamsCoordsPopulated;

	@OneToMany(mappedBy = "myResource", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = false)
	private Collection<ResourceIndexedSearchParamDate> myParamsDate;

	@Column(name = "SP_DATE_PRESENT")
	private boolean myParamsDatePopulated;

	@OneToMany(mappedBy = "myResource", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = false)
	private Collection<ResourceIndexedSearchParamNumber> myParamsNumber;

	@Column(name = "SP_NUMBER_PRESENT")
	private boolean myParamsNumberPopulated;

	@OneToMany(mappedBy = "myResource", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = false)
	private Collection<ResourceIndexedSearchParamQuantity> myParamsQuantity;

	@Column(name = "SP_QUANTITY_PRESENT")
	private boolean myParamsQuantityPopulated;

	@OneToMany(mappedBy = "myResource", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = false)
	private Collection<ResourceIndexedSearchParamString> myParamsString;

	@Column(name = "SP_STRING_PRESENT")
	private boolean myParamsStringPopulated;

	@OneToMany(mappedBy = "myResource", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = false)
	private Collection<ResourceIndexedSearchParamToken> myParamsToken;

	@Column(name = "SP_TOKEN_PRESENT")
	private boolean myParamsTokenPopulated;

	@OneToMany(mappedBy = "myResource", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = false)
	private Collection<ResourceIndexedSearchParamUri> myParamsUri;

	@Column(name = "SP_URI_PRESENT")
	private boolean myParamsUriPopulated;

	@Column(name = "RES_PROFILE", length = MAX_PROFILE_LENGTH, nullable = true)
	private String myProfile;

	@OneToMany(mappedBy = "mySourceResource", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = false)
	@IndexedEmbedded()
	private Collection<ResourceLink> myResourceLinks;

	@Column(name = "RES_TYPE", length = RESTYPE_LEN)
	@Field
	private String myResourceType;

	@OneToMany(mappedBy = "myResource", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private Collection<SearchParamPresent> mySearchParamPresents;

	@OneToMany(mappedBy = "myResource", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<ResourceTag> myTags;

	@Transient
	private transient boolean myUnchangedInCurrentOperation;
	
	@Column(name = "RES_VER")
	private long myVersion;

	@Override
	public ResourceTag addTag(TagDefinition theTag) {
		ResourceTag tag = new ResourceTag(this, theTag);
		getTags().add(tag);
		return tag;
	}

	public String getHashSha256() {
		return myHashSha256;
	}

	@Override
	public Long getId() {
		return myId;
	}

	@Override
	public IdDt getIdDt() {
		if (getForcedId() == null) {
			Long id = myId;
			return new IdDt(myResourceType + '/' + id + '/' + Constants.PARAM_HISTORY + '/' + myVersion);
		} else {
			return new IdDt(getForcedId().getResourceType() + '/' + getForcedId().getForcedId() + '/' + Constants.PARAM_HISTORY + '/' + myVersion);
		}
	}

	public Long getIndexStatus() {
		return myIndexStatus;
	}

	public String getLanguage() {
		return myLanguage;
	}

	public Collection<ResourceIndexedSearchParamCoords> getParamsCoords() {
		if (myParamsCoords == null) {
			myParamsCoords = new ArrayList<ResourceIndexedSearchParamCoords>();
		}
		return myParamsCoords;
	}

	public Collection<ResourceIndexedSearchParamDate> getParamsDate() {
		if (myParamsDate == null) {
			myParamsDate = new ArrayList<ResourceIndexedSearchParamDate>();
		}
		return myParamsDate;
	}

	public Collection<ResourceIndexedSearchParamNumber> getParamsNumber() {
		if (myParamsNumber == null) {
			myParamsNumber = new ArrayList<ResourceIndexedSearchParamNumber>();
		}
		return myParamsNumber;
	}

	public Collection<ResourceIndexedSearchParamQuantity> getParamsQuantity() {
		if (myParamsQuantity == null) {
			myParamsQuantity = new ArrayList<ResourceIndexedSearchParamQuantity>();
		}
		return myParamsQuantity;
	}

	public Collection<ResourceIndexedSearchParamString> getParamsString() {
		if (myParamsString == null) {
			myParamsString = new ArrayList<ResourceIndexedSearchParamString>();
		}
		return myParamsString;
	}

	public Collection<ResourceIndexedSearchParamToken> getParamsToken() {
		if (myParamsToken == null) {
			myParamsToken = new ArrayList<ResourceIndexedSearchParamToken>();
		}
		return myParamsToken;
	}

	public Collection<ResourceIndexedSearchParamUri> getParamsUri() {
		if (myParamsUri == null) {
			myParamsUri = new ArrayList<ResourceIndexedSearchParamUri>();
		}
		return myParamsUri;
	}

	public String getProfile() {
		return myProfile;
	}

	public Collection<ResourceLink> getResourceLinks() {
		if (myResourceLinks == null) {
			myResourceLinks = new ArrayList<ResourceLink>();
		}
		return myResourceLinks;
	}

	@Override
	public String getResourceType() {
		return myResourceType;
	}

	@Override
	public Collection<ResourceTag> getTags() {
		if (myTags == null) {
			myTags = new HashSet<ResourceTag>();
		}
		return myTags;
	}

	@Override
	public long getVersion() {
		return myVersion;
	}

	public boolean hasTag(System theSystem, String theTerm) {
		for (ResourceTag next : getTags()) {
			if (next.getTag().getSystem().equals(theSystem) && next.getTag().getCode().equals(theTerm)) {
				return true;
			}
		}
		return false;
	}

	public boolean isHasLinks() {
		return myHasLinks;
	}

	public boolean isParamsCoordsPopulated() {
		return myParamsCoordsPopulated;
	}

	public boolean isParamsDatePopulated() {
		return myParamsDatePopulated;
	}

	public boolean isParamsNumberPopulated() {
		return myParamsNumberPopulated;
	}

	public boolean isParamsQuantityPopulated() {
		return myParamsQuantityPopulated;
	}

	public boolean isParamsStringPopulated() {
		return myParamsStringPopulated;
	}

	public boolean isParamsTokenPopulated() {
		return myParamsTokenPopulated;
	}

	public boolean isParamsUriPopulated() {
		return myParamsUriPopulated;
	}

	/**
	 * Transient (not saved in DB) flag indicating that this resource was found to be unchanged by the current operation
	 * and was not re-saved in the database
	 */
	public boolean isUnchangedInCurrentOperation() {
		return myUnchangedInCurrentOperation;
	}

	public void setContentTextParsedIntoWords(String theContentText) {
		myContentText = theContentText;
	}

	public void setHashSha256(String theHashSha256) {
		myHashSha256 = theHashSha256;
	}

	public void setHasLinks(boolean theHasLinks) {
		myHasLinks = theHasLinks;
	}

	public void setId(Long theId) {
		myId = theId;
	}

	public void setIndexStatus(Long theIndexStatus) {
		myIndexStatus = theIndexStatus;
	}

	public void setLanguage(String theLanguage) {
		if (defaultString(theLanguage).length() > MAX_LANGUAGE_LENGTH) {
			throw new UnprocessableEntityException("Language exceeds maximum length of " + MAX_LANGUAGE_LENGTH + " chars: " + theLanguage);
		}
		myLanguage = theLanguage;
	}

	public void setNarrativeTextParsedIntoWords(String theNarrativeText) {
		myNarrativeText = theNarrativeText;
	}

	public void setParamsCoords(Collection<ResourceIndexedSearchParamCoords> theParamsCoords) {
		if (!isParamsTokenPopulated() && theParamsCoords.isEmpty()) {
			return;
		}
		getParamsCoords().clear();
		getParamsCoords().addAll(theParamsCoords);
	}

	public void setParamsCoordsPopulated(boolean theParamsCoordsPopulated) {
		myParamsCoordsPopulated = theParamsCoordsPopulated;
	}

	public void setParamsDate(Collection<ResourceIndexedSearchParamDate> theParamsDate) {
		if (!isParamsDatePopulated() && theParamsDate.isEmpty()) {
			return;
		}
		getParamsDate().clear();
		getParamsDate().addAll(theParamsDate);
	}

	public void setParamsDatePopulated(boolean theParamsDatePopulated) {
		myParamsDatePopulated = theParamsDatePopulated;
	}

	public void setParamsNumber(Collection<ResourceIndexedSearchParamNumber> theNumberParams) {
		if (!isParamsNumberPopulated() && theNumberParams.isEmpty()) {
			return;
		}
		getParamsNumber().clear();
		getParamsNumber().addAll(theNumberParams);
	}

	public void setParamsNumberPopulated(boolean theParamsNumberPopulated) {
		myParamsNumberPopulated = theParamsNumberPopulated;
	}

	public void setParamsQuantity(Collection<ResourceIndexedSearchParamQuantity> theQuantityParams) {
		if (!isParamsQuantityPopulated() && theQuantityParams.isEmpty()) {
			return;
		}
		getParamsQuantity().clear();
		getParamsQuantity().addAll(theQuantityParams);
	}

	public void setParamsQuantityPopulated(boolean theParamsQuantityPopulated) {
		myParamsQuantityPopulated = theParamsQuantityPopulated;
	}

	public void setParamsString(Collection<ResourceIndexedSearchParamString> theParamsString) {
		if (!isParamsStringPopulated() && theParamsString.isEmpty()) {
			return;
		}
		getParamsString().clear();
		getParamsString().addAll(theParamsString);
	}

	public void setParamsStringPopulated(boolean theParamsStringPopulated) {
		myParamsStringPopulated = theParamsStringPopulated;
	}

	public void setParamsToken(Collection<ResourceIndexedSearchParamToken> theParamsToken) {
		if (!isParamsTokenPopulated() && theParamsToken.isEmpty()) {
			return;
		}
		getParamsToken().clear();
		getParamsToken().addAll(theParamsToken);
	}

	public void setParamsTokenPopulated(boolean theParamsTokenPopulated) {
		myParamsTokenPopulated = theParamsTokenPopulated;
	}

	public void setParamsUri(Collection<ResourceIndexedSearchParamUri> theParamsUri) {
		if (!isParamsTokenPopulated() && theParamsUri.isEmpty()) {
			return;
		}
		getParamsUri().clear();
		getParamsUri().addAll(theParamsUri);
	}

	public void setParamsUriPopulated(boolean theParamsUriPopulated) {
		myParamsUriPopulated = theParamsUriPopulated;
	}

	public void setProfile(String theProfile) {
		if (defaultString(theProfile).length() > MAX_PROFILE_LENGTH) {
			throw new UnprocessableEntityException("Profile name exceeds maximum length of " + MAX_PROFILE_LENGTH + " chars: " + theProfile);
		}
		myProfile = theProfile;
	}

	public void setResourceLinks(Collection<ResourceLink> theLinks) {
		if (!isHasLinks() && theLinks.isEmpty()) {
			return;
		}
		getResourceLinks().clear();
		getResourceLinks().addAll(theLinks);
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	/**
	 * Transient (not saved in DB) flag indicating that this resource was found to be unchanged by the current operation
	 * and was not re-saved in the database
	 */
	public void setUnchangedInCurrentOperation(boolean theUnchangedInCurrentOperation) {
		myUnchangedInCurrentOperation = theUnchangedInCurrentOperation;
	}

	public void setVersion(long theVersion) {
		myVersion = theVersion;
	}

	public ResourceHistoryTable toHistory(ResourceHistoryTable theResourceHistoryTable) {
		ResourceHistoryTable retVal = theResourceHistoryTable != null ? theResourceHistoryTable : new ResourceHistoryTable();

		retVal.setResourceId(myId);
		retVal.setResourceType(myResourceType);
		retVal.setVersion(myVersion);

		retVal.setTitle(getTitle());
		retVal.setPublished(getPublished());
		retVal.setUpdated(getUpdated());
		retVal.setEncoding(getEncoding());
		retVal.setFhirVersion(getFhirVersion());
		retVal.setResource(getResource());
		retVal.setDeleted(getDeleted());
		retVal.setForcedId(getForcedId());

		retVal.getTags().clear();

		retVal.setHasTags(isHasTags());
		if (isHasTags()) {
			for (ResourceTag next : getTags()) {
				retVal.addTag(next);
			}
		}

		return retVal;
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("resourceType", myResourceType);
		b.append("pid", myId);
		return b.build();
	}

}
