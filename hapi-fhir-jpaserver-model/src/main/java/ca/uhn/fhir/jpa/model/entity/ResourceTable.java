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

import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.cross.IResourceLookup;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import ca.uhn.fhir.jpa.model.search.IndexNonDeletedInterceptor;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.OptimisticLock;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.defaultString;

@Indexed(interceptor = IndexNonDeletedInterceptor.class)
@Entity
@Table(name = "HFJ_RESOURCE", uniqueConstraints = {}, indexes = {
	@Index(name = "IDX_RES_DATE", columnList = "RES_UPDATED"),
	@Index(name = "IDX_RES_LANG", columnList = "RES_TYPE,RES_LANGUAGE"),
	@Index(name = "IDX_RES_PROFILE", columnList = "RES_PROFILE"),
	@Index(name = "IDX_RES_TYPE", columnList = "RES_TYPE"),
	@Index(name = "IDX_INDEXSTATUS", columnList = "SP_INDEX_STATUS")
})
public class ResourceTable extends BaseHasResource implements Serializable, IBasePersistedResource, IResourceLookup {
	public static final int RESTYPE_LEN = 40;
	private static final int MAX_LANGUAGE_LENGTH = 20;
	private static final int MAX_PROFILE_LENGTH = 200;
	private static final long serialVersionUID = 1L;

	/**
	 * Holds the narrative text only - Used for Fulltext searching but not directly stored in the DB
	 */
	@Transient()
	@Fields({
		@Field(name = "myContentText", index = org.hibernate.search.annotations.Index.YES, store = Store.YES, analyze = Analyze.YES, analyzer = @Analyzer(definition = "standardAnalyzer")),
		@Field(name = "myContentTextEdgeNGram", index = org.hibernate.search.annotations.Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer = @Analyzer(definition = "autocompleteEdgeAnalyzer")),
		@Field(name = "myContentTextNGram", index = org.hibernate.search.annotations.Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer = @Analyzer(definition = "autocompleteNGramAnalyzer")),
		@Field(name = "myContentTextPhonetic", index = org.hibernate.search.annotations.Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer = @Analyzer(definition = "autocompletePhoneticAnalyzer"))
	})
	@OptimisticLock(excluded = true)
	private String myContentText;

	@Column(name = "HASH_SHA256", length = 64, nullable = true)
	@OptimisticLock(excluded = true)
	private String myHashSha256;

	@Column(name = "SP_HAS_LINKS")
	@OptimisticLock(excluded = true)
	private boolean myHasLinks;

	@Id
	@SequenceGenerator(name = "SEQ_RESOURCE_ID", sequenceName = "SEQ_RESOURCE_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_RESOURCE_ID")
	@Column(name = "RES_ID")
	private Long myId;

	@Column(name = "SP_INDEX_STATUS", nullable = true)
	@OptimisticLock(excluded = true)
	private Long myIndexStatus;

	@Column(name = "RES_LANGUAGE", length = MAX_LANGUAGE_LENGTH, nullable = true)
	@OptimisticLock(excluded = true)
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
	@OptimisticLock(excluded = true)
	private String myNarrativeText;

	@OneToMany(mappedBy = "myResource", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = false)
	@OptimisticLock(excluded = true)
	private Collection<ResourceIndexedSearchParamCoords> myParamsCoords;

	@Column(name = "SP_COORDS_PRESENT")
	@OptimisticLock(excluded = true)
	private boolean myParamsCoordsPopulated;

	@OneToMany(mappedBy = "myResource", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = false)
	@OptimisticLock(excluded = true)
	private Collection<ResourceIndexedSearchParamDate> myParamsDate;

	@Column(name = "SP_DATE_PRESENT")
	@OptimisticLock(excluded = true)
	private boolean myParamsDatePopulated;

	@OptimisticLock(excluded = true)
	@OneToMany(mappedBy = "myResource", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = false)
	private Collection<ResourceIndexedSearchParamNumber> myParamsNumber;

	@Column(name = "SP_NUMBER_PRESENT")
	@OptimisticLock(excluded = true)
	private boolean myParamsNumberPopulated;

	@OneToMany(mappedBy = "myResource", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = false)
	@OptimisticLock(excluded = true)
	private Collection<ResourceIndexedSearchParamQuantity> myParamsQuantity;

	@Column(name = "SP_QUANTITY_PRESENT")
	@OptimisticLock(excluded = true)
	private boolean myParamsQuantityPopulated;

	@OneToMany(mappedBy = "myResource", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = false)
	@OptimisticLock(excluded = true)
	private Collection<ResourceIndexedSearchParamString> myParamsString;

	@Column(name = "SP_STRING_PRESENT")
	@OptimisticLock(excluded = true)
	private boolean myParamsStringPopulated;

	@OneToMany(mappedBy = "myResource", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = false)
	@OptimisticLock(excluded = true)
	private Collection<ResourceIndexedSearchParamToken> myParamsToken;

	@Column(name = "SP_TOKEN_PRESENT")
	@OptimisticLock(excluded = true)
	private boolean myParamsTokenPopulated;

	@OneToMany(mappedBy = "myResource", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = false)
	@OptimisticLock(excluded = true)
	private Collection<ResourceIndexedSearchParamUri> myParamsUri;

	@Column(name = "SP_URI_PRESENT")
	@OptimisticLock(excluded = true)
	private boolean myParamsUriPopulated;

	@Column(name = "RES_PROFILE", length = MAX_PROFILE_LENGTH, nullable = true)
	@OptimisticLock(excluded = true)
	private String myProfile;

	// Added in 3.0.0 - Should make this a primitive Boolean at some point
	@OptimisticLock(excluded = true)
	@Column(name = "SP_CMPSTR_UNIQ_PRESENT")
	private Boolean myParamsCompositeStringUniquePresent = false;

	@OneToMany(mappedBy = "myResource", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = false)
	@OptimisticLock(excluded = true)
	private Collection<ResourceIndexedCompositeStringUnique> myParamsCompositeStringUnique;

	@OneToMany(mappedBy = "mySourceResource", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = false)
	@OptimisticLock(excluded = true)
	private Collection<ResourceLink> myResourceLinks;

	/**
	 * This is a clone of {@link #myResourceLinks} but without the hibernate annotations.
	 * Before we persist we copy the contents of {@link #myResourceLinks} into this field. We
	 * have this separate because that way we can only populate this field if
	 * {@link #myHasLinks} is true, meaning that there are actually resource links present
	 * right now. This avoids Hibernate Search triggering a select on the resource link
	 * table.
	 *
	 * This field is used by FulltextSearchSvcImpl
	 *
	 * You can test that any changes don't cause extra queries by running
	 * FhirResourceDaoR4QueryCountTest
	 */
	@Field
	@Transient
	private String myResourceLinksField;

	@OneToMany(mappedBy = "myTargetResource", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = false)
	@OptimisticLock(excluded = true)
	private Collection<ResourceLink> myResourceLinksAsTarget;

	@Column(name = "RES_TYPE", length = RESTYPE_LEN, nullable = false)
	@Field
	@OptimisticLock(excluded = true)
	private String myResourceType;

	@OneToMany(mappedBy = "myResource", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@OptimisticLock(excluded = true)
	private Collection<SearchParamPresent> mySearchParamPresents;

	@OneToMany(mappedBy = "myResource", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@OptimisticLock(excluded = true)
	private Set<ResourceTag> myTags;

	@Transient
	private transient boolean myUnchangedInCurrentOperation;

	@Version
	@Column(name = "RES_VER")
	private long myVersion;

	@OneToMany(mappedBy = "myResourceTable", fetch = FetchType.LAZY)
	private Collection<ResourceHistoryProvenanceEntity> myProvenance;

	@Transient
	private transient ResourceHistoryTable myCurrentVersionEntity;

	@OneToOne(optional = true, fetch = FetchType.EAGER, cascade = {}, orphanRemoval = false, mappedBy = "myResource")
	private ForcedId myForcedId;

	@Override
	public ResourceTag addTag(TagDefinition theTag) {
		for (ResourceTag next : getTags()) {
			if (next.getTag().equals(theTag)) {
				return next;
			}
		}
		ResourceTag tag = new ResourceTag(this, theTag);
		getTags().add(tag);
		return tag;
	}


	public String getHashSha256() {
		return myHashSha256;
	}

	public void setHashSha256(String theHashSha256) {
		myHashSha256 = theHashSha256;
	}

	@Override
	public Long getId() {
		return myId;
	}

	public void setId(Long theId) {
		myId = theId;
	}

	public Long getIndexStatus() {
		return myIndexStatus;
	}

	public void setIndexStatus(Long theIndexStatus) {
		myIndexStatus = theIndexStatus;
	}

	public String getLanguage() {
		return myLanguage;
	}

	public void setLanguage(String theLanguage) {
		if (defaultString(theLanguage).length() > MAX_LANGUAGE_LENGTH) {
			throw new UnprocessableEntityException("Language exceeds maximum length of " + MAX_LANGUAGE_LENGTH + " chars: " + theLanguage);
		}
		myLanguage = theLanguage;
	}

	public Collection<ResourceIndexedCompositeStringUnique> getParamsCompositeStringUnique() {
		if (myParamsCompositeStringUnique == null) {
			myParamsCompositeStringUnique = new ArrayList<>();
		}
		return myParamsCompositeStringUnique;
	}

	public Collection<ResourceIndexedSearchParamCoords> getParamsCoords() {
		if (myParamsCoords == null) {
			myParamsCoords = new ArrayList<>();
		}
		return myParamsCoords;
	}

	public void setParamsCoords(Collection<ResourceIndexedSearchParamCoords> theParamsCoords) {
		if (!isParamsTokenPopulated() && theParamsCoords.isEmpty()) {
			return;
		}
		getParamsCoords().clear();
		getParamsCoords().addAll(theParamsCoords);
	}

	public Collection<ResourceIndexedSearchParamDate> getParamsDate() {
		if (myParamsDate == null) {
			myParamsDate = new ArrayList<>();
		}
		return myParamsDate;
	}

	public void setParamsDate(Collection<ResourceIndexedSearchParamDate> theParamsDate) {
		if (!isParamsDatePopulated() && theParamsDate.isEmpty()) {
			return;
		}
		getParamsDate().clear();
		getParamsDate().addAll(theParamsDate);
	}

	public Collection<ResourceIndexedSearchParamNumber> getParamsNumber() {
		if (myParamsNumber == null) {
			myParamsNumber = new ArrayList<>();
		}
		return myParamsNumber;
	}

	public void setParamsNumber(Collection<ResourceIndexedSearchParamNumber> theNumberParams) {
		if (!isParamsNumberPopulated() && theNumberParams.isEmpty()) {
			return;
		}
		getParamsNumber().clear();
		getParamsNumber().addAll(theNumberParams);
	}

	public Collection<ResourceIndexedSearchParamQuantity> getParamsQuantity() {
		if (myParamsQuantity == null) {
			myParamsQuantity = new ArrayList<>();
		}
		return myParamsQuantity;
	}

	public void setParamsQuantity(Collection<ResourceIndexedSearchParamQuantity> theQuantityParams) {
		if (!isParamsQuantityPopulated() && theQuantityParams.isEmpty()) {
			return;
		}
		getParamsQuantity().clear();
		getParamsQuantity().addAll(theQuantityParams);
	}

	public Collection<ResourceIndexedSearchParamString> getParamsString() {
		if (myParamsString == null) {
			myParamsString = new ArrayList<>();
		}
		return myParamsString;
	}

	public void setParamsString(Collection<ResourceIndexedSearchParamString> theParamsString) {
		if (!isParamsStringPopulated() && theParamsString.isEmpty()) {
			return;
		}
		getParamsString().clear();
		getParamsString().addAll(theParamsString);
	}

	public Collection<ResourceIndexedSearchParamToken> getParamsToken() {
		if (myParamsToken == null) {
			myParamsToken = new ArrayList<>();
		}
		return myParamsToken;
	}

	public void setParamsToken(Collection<ResourceIndexedSearchParamToken> theParamsToken) {
		if (!isParamsTokenPopulated() && theParamsToken.isEmpty()) {
			return;
		}
		getParamsToken().clear();
		getParamsToken().addAll(theParamsToken);
	}

	public Collection<ResourceIndexedSearchParamUri> getParamsUri() {
		if (myParamsUri == null) {
			myParamsUri = new ArrayList<>();
		}
		return myParamsUri;
	}

	public void setParamsUri(Collection<ResourceIndexedSearchParamUri> theParamsUri) {
		if (!isParamsTokenPopulated() && theParamsUri.isEmpty()) {
			return;
		}
		getParamsUri().clear();
		getParamsUri().addAll(theParamsUri);
	}

	public String getProfile() {
		return myProfile;
	}

	public void setProfile(String theProfile) {
		if (defaultString(theProfile).length() > MAX_PROFILE_LENGTH) {
			throw new UnprocessableEntityException("Profile name exceeds maximum length of " + MAX_PROFILE_LENGTH + " chars: " + theProfile);
		}
		myProfile = theProfile;
	}

	@Override
	public Long getResourceId() {
		return getId();
	}

	public Collection<ResourceLink> getResourceLinks() {
		if (myResourceLinks == null) {
			myResourceLinks = new ArrayList<>();
		}
		return myResourceLinks;
	}

	public void setResourceLinks(Collection<ResourceLink> theLinks) {
		if (!isHasLinks() && theLinks.isEmpty()) {
			return;
		}
		getResourceLinks().clear();
		getResourceLinks().addAll(theLinks);
	}

	@Override
	public String getResourceType() {
		return myResourceType;
	}

	public ResourceTable setResourceType(String theResourceType) {
		myResourceType = theResourceType;
		return this;
	}

	@Override
	public Collection<ResourceTag> getTags() {
		if (myTags == null) {
			myTags = new HashSet<>();
		}
		return myTags;
	}

	@Override
	public long getVersion() {
		return myVersion;
	}

	public void setVersion(long theVersion) {
		myVersion = theVersion;
	}

	public boolean isHasLinks() {
		return myHasLinks;
	}

	public void setHasLinks(boolean theHasLinks) {
		myHasLinks = theHasLinks;
	}

	public boolean isParamsCompositeStringUniquePresent() {
		if (myParamsCompositeStringUniquePresent == null) {
			return false;
		}
		return myParamsCompositeStringUniquePresent;
	}

	public void setParamsCompositeStringUniquePresent(boolean theParamsCompositeStringUniquePresent) {
		myParamsCompositeStringUniquePresent = theParamsCompositeStringUniquePresent;
	}

	public boolean isParamsCoordsPopulated() {
		return myParamsCoordsPopulated;
	}

	public void setParamsCoordsPopulated(boolean theParamsCoordsPopulated) {
		myParamsCoordsPopulated = theParamsCoordsPopulated;
	}

	public boolean isParamsDatePopulated() {
		return myParamsDatePopulated;
	}

	public void setParamsDatePopulated(boolean theParamsDatePopulated) {
		myParamsDatePopulated = theParamsDatePopulated;
	}

	public boolean isParamsNumberPopulated() {
		return myParamsNumberPopulated;
	}

	public void setParamsNumberPopulated(boolean theParamsNumberPopulated) {
		myParamsNumberPopulated = theParamsNumberPopulated;
	}

	public boolean isParamsQuantityPopulated() {
		return myParamsQuantityPopulated;
	}

	public void setParamsQuantityPopulated(boolean theParamsQuantityPopulated) {
		myParamsQuantityPopulated = theParamsQuantityPopulated;
	}

	public boolean isParamsStringPopulated() {
		return myParamsStringPopulated;
	}

	public void setParamsStringPopulated(boolean theParamsStringPopulated) {
		myParamsStringPopulated = theParamsStringPopulated;
	}

	public boolean isParamsTokenPopulated() {
		return myParamsTokenPopulated;
	}

	public void setParamsTokenPopulated(boolean theParamsTokenPopulated) {
		myParamsTokenPopulated = theParamsTokenPopulated;
	}

	public boolean isParamsUriPopulated() {
		return myParamsUriPopulated;
	}

	public void setParamsUriPopulated(boolean theParamsUriPopulated) {
		myParamsUriPopulated = theParamsUriPopulated;
	}

	/**
	 * Transient (not saved in DB) flag indicating that this resource was found to be unchanged by the current operation
	 * and was not re-saved in the database
	 */
	public boolean isUnchangedInCurrentOperation() {
		return myUnchangedInCurrentOperation;
	}

	/**
	 * Transient (not saved in DB) flag indicating that this resource was found to be unchanged by the current operation
	 * and was not re-saved in the database
	 */
	public void setUnchangedInCurrentOperation(boolean theUnchangedInCurrentOperation) {
		myUnchangedInCurrentOperation = theUnchangedInCurrentOperation;
	}

	public void setContentTextParsedIntoWords(String theContentText) {
		myContentText = theContentText;
	}

	public void setNarrativeTextParsedIntoWords(String theNarrativeText) {
		myNarrativeText = theNarrativeText;
	}

	public ResourceHistoryTable toHistory() {
		ResourceHistoryTable retVal = new ResourceHistoryTable();

		retVal.setResourceId(myId);
		retVal.setResourceType(myResourceType);
		retVal.setVersion(myVersion);
		retVal.setTransientForcedId(getTransientForcedId());

		retVal.setPublished(getPublished());
		retVal.setUpdated(getUpdated());
		retVal.setFhirVersion(getFhirVersion());
		retVal.setDeleted(getDeleted());
		retVal.setResourceTable(this);

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

	@PrePersist
	@PreUpdate
	public void preSave() {
		if (myHasLinks && myResourceLinks != null) {
			myResourceLinksField = getResourceLinks()
				.stream()
				.map(ResourceLink::getTargetResourcePid)
				.filter(Objects::nonNull)
				.map(Object::toString)
				.collect(Collectors.joining(" "));
		} else {
			myResourceLinksField = null;
		}
	}

	/**
	 * This is a convenience to avoid loading the version a second time within a single transaction. It is
	 * not persisted.
	 */
	public void setCurrentVersionEntity(ResourceHistoryTable theCurrentVersionEntity) {
		myCurrentVersionEntity = theCurrentVersionEntity;
	}

	/**
	 * This is a convenience to avoid loading the version a second time within a single transaction. It is
	 * not persisted.
	 */
	public ResourceHistoryTable getCurrentVersionEntity() {
		return myCurrentVersionEntity;
	}

	@Override
	public ResourcePersistentId getPersistentId() {
		return new ResourcePersistentId(getId());
	}

	@Override
	public ForcedId getForcedId() {
		return myForcedId;
	}

	@Override
	public void setForcedId(ForcedId theForcedId) {
		myForcedId = theForcedId;
	}

	@Override
	public IdDt getIdDt() {
		if (getForcedId() == null) {
			Long id = this.getResourceId();
			return new IdDt(getResourceType() + '/' + id + '/' + Constants.PARAM_HISTORY + '/' + getVersion());
		} else {
			// Avoid a join query if possible
			String forcedId = getTransientForcedId() != null ? getTransientForcedId() : getForcedId().getForcedId();
			return new IdDt(getResourceType() + '/' + forcedId + '/' + Constants.PARAM_HISTORY + '/' + getVersion());
		}
	}


}
