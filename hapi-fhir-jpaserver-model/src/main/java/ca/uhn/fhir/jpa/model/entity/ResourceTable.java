package ca.uhn.fhir.jpa.model.entity;

/*
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.cross.IResourceLookup;
import ca.uhn.fhir.jpa.model.search.ExtendedLuceneIndexData;
import ca.uhn.fhir.jpa.model.search.ResourceTableRoutingBinder;
import ca.uhn.fhir.jpa.model.search.SearchParamTextPropertyBinder;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.OptimisticLock;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.PropertyBinderRef;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.RoutingBinderRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ObjectPath;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.PropertyBinding;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.PropertyValue;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Indexed(routingBinder= @RoutingBinderRef(type = ResourceTableRoutingBinder.class))
@Entity
@Table(name = "HFJ_RESOURCE", uniqueConstraints = {}, indexes = {
	// Do not reuse previously used index name: IDX_INDEXSTATUS, IDX_RES_TYPE
	@Index(name = "IDX_RES_DATE", columnList = "RES_UPDATED"),
	@Index(name = "IDX_RES_TYPE_DEL_UPDATED", columnList = "RES_TYPE,RES_DELETED_AT,RES_UPDATED,PARTITION_ID,RES_ID"),
})
@NamedEntityGraph(name = "Resource.noJoins")
public class ResourceTable extends BaseHasResource implements Serializable, IBasePersistedResource, IResourceLookup {
	public static final int RESTYPE_LEN = 40;
	private static final int MAX_LANGUAGE_LENGTH = 20;
	private static final long serialVersionUID = 1L;

	/**
	 * Holds the narrative text only - Used for Fulltext searching but not directly stored in the DB
	 * Note the extra config needed in HS6 for indexing transient props:
	 * https://docs.jboss.org/hibernate/search/6.0/migration/html_single/#indexed-transient-requires-configuration
	 *
	 * Note that we depend on `myVersion` updated for this field to be indexed.
	 */
	@Transient
	@FullTextField(name = "myContentText", searchable = Searchable.YES, projectable = Projectable.YES, analyzer = "standardAnalyzer")
	@FullTextField(name = "myContentTextEdgeNGram", searchable= Searchable.YES, projectable= Projectable.NO, analyzer =  "autocompleteEdgeAnalyzer")
	@FullTextField(name = "myContentTextNGram", searchable= Searchable.YES, projectable= Projectable.NO, analyzer =  "autocompleteNGramAnalyzer")
	@FullTextField(name = "myContentTextPhonetic", searchable= Searchable.YES, projectable= Projectable.NO, analyzer =  "autocompletePhoneticAnalyzer")
	@OptimisticLock(excluded = true)
	@IndexingDependency(derivedFrom = @ObjectPath(@PropertyValue(propertyName = "myVersion")))
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
	@GenericField(projectable = Projectable.YES)
	private Long myId;

	@Column(name = "SP_INDEX_STATUS", nullable = true)
	@OptimisticLock(excluded = true)
	private Long myIndexStatus;

	// TODO: Removed in 5.5.0. Drop in a future release.
	@Column(name = "RES_LANGUAGE", length = MAX_LANGUAGE_LENGTH, nullable = true)
	@OptimisticLock(excluded = true)
	private String myLanguage;

	/**
	 * Holds the narrative text only - Used for Fulltext searching but not directly stored in the DB
	 */
	@Transient()
	@FullTextField(name = "myNarrativeText", searchable = Searchable.YES, projectable = Projectable.YES, analyzer = "standardAnalyzer")
	@FullTextField(name = "myNarrativeTextEdgeNGram", searchable= Searchable.YES, projectable= Projectable.NO, analyzer =  "autocompleteEdgeAnalyzer")
	@FullTextField(name = "myNarrativeTextNGram", searchable= Searchable.YES, projectable= Projectable.NO, analyzer =  "autocompleteNGramAnalyzer")
	@FullTextField(name = "myNarrativeTextPhonetic", searchable= Searchable.YES, projectable= Projectable.NO, analyzer =  "autocompletePhoneticAnalyzer")
	@OptimisticLock(excluded = true)
	@IndexingDependency(derivedFrom = @ObjectPath(@PropertyValue(propertyName = "myVersion")))
	private String myNarrativeText;

	@Transient
	@IndexingDependency(derivedFrom = @ObjectPath(@PropertyValue(propertyName = "myVersion")))
	@PropertyBinding(binder = @PropertyBinderRef(type = SearchParamTextPropertyBinder.class))
	private ExtendedLuceneIndexData myLuceneIndexData;

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
	
	/**
	 * Added to support UCUM conversion
	 * since 5.3.0
	 */
	@OneToMany(mappedBy = "myResource", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = false)
	@OptimisticLock(excluded = true)
	private Collection<ResourceIndexedSearchParamQuantityNormalized> myParamsQuantityNormalized;
	
	/**
	 * Added to support UCUM conversion, 
	 * NOTE : use Boolean class instead of boolean primitive, in order to set the existing rows to null
	 * since 5.3.0
	 */
	@Column(name = "SP_QUANTITY_NRML_PRESENT")
	@OptimisticLock(excluded = true)
	private Boolean myParamsQuantityNormalizedPopulated = Boolean.FALSE;

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

	// Added in 3.0.0 - Should make this a primitive Boolean at some point
	@OptimisticLock(excluded = true)
	@Column(name = "SP_CMPSTR_UNIQ_PRESENT")
	private Boolean myParamsComboStringUniquePresent = false;

	@OneToMany(mappedBy = "myResource", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = false)
	@OptimisticLock(excluded = true)
	private Collection<ResourceIndexedComboStringUnique> myParamsComboStringUnique;

	// Added in 5.5.0 - Should make this a primitive Boolean at some point
	@OptimisticLock(excluded = true)
	@Column(name = "SP_CMPTOKS_PRESENT")
	private Boolean myParamsComboTokensNonUniquePresent = false;

	@OneToMany(mappedBy = "myResource", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = false)
	@OptimisticLock(excluded = true)
	private Collection<ResourceIndexedComboTokenNonUnique> myParamsComboTokensNonUnique;

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
	 * <p>
	 * This field is used by FulltextSearchSvcImpl
	 * <p>
	 * You can test that any changes don't cause extra queries by running
	 * FhirResourceDaoR4QueryCountTest
	 */
	@FullTextField
	@Transient
	@IndexingDependency(derivedFrom = @ObjectPath(@PropertyValue(propertyName = "myResourceLinks")))
	private String myResourceLinksField;

	@OneToMany(mappedBy = "myTargetResource", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = false)
	@OptimisticLock(excluded = true)
	private Collection<ResourceLink> myResourceLinksAsTarget;

	@Column(name = "RES_TYPE", length = RESTYPE_LEN, nullable = false)
	@FullTextField
	@OptimisticLock(excluded = true)
	private String myResourceType;

	@OneToMany(mappedBy = "myResource", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@OptimisticLock(excluded = true)
	private Collection<SearchParamPresentEntity> mySearchParamPresents;

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
	@OptimisticLock(excluded = true)
	private ForcedId myForcedId;

	@Transient
	private volatile String myCreatedByMatchUrl;

	/**
	 * Constructor
	 */
	public ResourceTable() {
		super();
	}

	@Override
	public ResourceTag addTag(TagDefinition theTag) {
		for (ResourceTag next : getTags()) {
			if (next.getTag().equals(theTag)) {
				return next;
			}
		}
		ResourceTag tag = new ResourceTag(this, theTag, getPartitionId());
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

	public Collection<ResourceIndexedComboStringUnique> getParamsComboStringUnique() {
		if (myParamsComboStringUnique == null) {
			myParamsComboStringUnique = new ArrayList<>();
		}
		return myParamsComboStringUnique;
	}

	public Collection<ResourceIndexedComboTokenNonUnique> getmyParamsComboTokensNonUnique() {
		if (myParamsComboTokensNonUnique == null) {
			myParamsComboTokensNonUnique = new ArrayList<>();
		}
		return myParamsComboTokensNonUnique;
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

	public Collection<ResourceIndexedSearchParamQuantityNormalized> getParamsQuantityNormalized() {
		if (myParamsQuantityNormalized == null) {
			myParamsQuantityNormalized = new ArrayList<>();
		}
		return myParamsQuantityNormalized;
	}

	public void setParamsQuantityNormalized(Collection<ResourceIndexedSearchParamQuantityNormalized> theQuantityNormalizedParams) {
		if (!isParamsQuantityNormalizedPopulated() && theQuantityNormalizedParams.isEmpty()) {
			return;
		}
		getParamsQuantityNormalized().clear();
		getParamsQuantityNormalized().addAll(theQuantityNormalizedParams);
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

	public boolean isParamsComboStringUniquePresent() {
		if (myParamsComboStringUniquePresent == null) {
			return false;
		}
		return myParamsComboStringUniquePresent;
	}

	public void setParamsComboStringUniquePresent(boolean theParamsComboStringUniquePresent) {
		myParamsComboStringUniquePresent = theParamsComboStringUniquePresent;
	}

	public boolean isParamsComboTokensNonUniquePresent() {
		if (myParamsComboTokensNonUniquePresent == null) {
			return false;
		}
		return myParamsComboTokensNonUniquePresent;
	}

	public void setParamsComboTokensNonUniquePresent(boolean theParamsComboTokensNonUniquePresent) {
		myParamsComboStringUniquePresent = theParamsComboTokensNonUniquePresent;
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
	
	public Boolean isParamsQuantityNormalizedPopulated() {
		if (myParamsQuantityNormalizedPopulated == null)
			return Boolean.FALSE;
		else
			return myParamsQuantityNormalizedPopulated;
	}

	public void setParamsQuantityNormalizedPopulated(Boolean theParamsQuantityNormalizedPopulated) {
		if (theParamsQuantityNormalizedPopulated == null)
			myParamsQuantityNormalizedPopulated = Boolean.FALSE;
		else
			myParamsQuantityNormalizedPopulated = theParamsQuantityNormalizedPopulated;
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

	public void setContentText(String theContentText) {
		myContentText = theContentText;
	}

	public String getContentText() {
		return myContentText;
	}

	public void setNarrativeText(String theNarrativeText) {
		myNarrativeText = theNarrativeText;
	}

	public ResourceHistoryTable toHistory(boolean theCreateVersionTags) {
		ResourceHistoryTable retVal = new ResourceHistoryTable();

		retVal.setResourceId(myId);
		retVal.setResourceType(myResourceType);
		retVal.setVersion(myVersion);
		retVal.setTransientForcedId(getTransientForcedId());

		retVal.setPublished(getPublishedDate());
		retVal.setUpdated(getUpdatedDate());
		retVal.setFhirVersion(getFhirVersion());
		retVal.setDeleted(getDeleted());
		retVal.setResourceTable(this);
		retVal.setForcedId(getForcedId());
		retVal.setPartitionId(getPartitionId());

		retVal.getTags().clear();

		retVal.setHasTags(isHasTags());
		if (isHasTags() && theCreateVersionTags) {
			for (ResourceTag next : getTags()) {
				retVal.addTag(next);
			}
		}

		return retVal;
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("pid", myId);
		b.append("resourceType", myResourceType);
		b.append("version", myVersion);
		if (getPartitionId() != null) {
			b.append("partitionId", getPartitionId().getPartitionId());
		}
		b.append("lastUpdated", getUpdated().getValueAsString());
		if (getDeleted() != null) {
			b.append("deleted");
		}
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
		IdDt retVal = new IdDt();
		populateId(retVal);
		return retVal;
	}


	public IIdType getIdType(FhirContext theContext) {
		IIdType retVal = theContext.getVersion().newIdType();
		populateId(retVal);
		return retVal;
	}

	private void populateId(IIdType retVal) {
		if (getTransientForcedId() != null) {
			// Avoid a join query if possible
			retVal.setValue(getResourceType() + '/' + getTransientForcedId() + '/' + Constants.PARAM_HISTORY + '/' + getVersion());
		} else if (getForcedId() == null) {
			Long id = this.getResourceId();
			retVal.setValue(getResourceType() + '/' + id + '/' + Constants.PARAM_HISTORY + '/' + getVersion());
		} else {
			String forcedId = getForcedId().getForcedId();
			retVal.setValue(getResourceType() + '/' + forcedId + '/' + Constants.PARAM_HISTORY + '/' + getVersion());
		}
	}

	public void setCreatedByMatchUrl(String theCreatedByMatchUrl) {
		myCreatedByMatchUrl = theCreatedByMatchUrl;
	}

	public String getCreatedByMatchUrl() {
		return myCreatedByMatchUrl;
	}

	public void setLuceneIndexData(ExtendedLuceneIndexData theLuceneIndexData) {
		myLuceneIndexData = theLuceneIndexData;
	}

	public Collection<SearchParamPresentEntity> getSearchParamPresents() {
		if (mySearchParamPresents == null) {
			mySearchParamPresents = new ArrayList<>();
		}
		return mySearchParamPresents;
	}
}
