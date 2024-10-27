/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeChildResourceDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.api.dao.IJpaDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTagDao;
import ca.uhn.fhir.jpa.dao.expunge.ExpungeService;
import ca.uhn.fhir.jpa.dao.index.DaoSearchParamSynchronizer;
import ca.uhn.fhir.jpa.dao.index.SearchParamWithInlineReferencesExtractor;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.delete.DeleteConflictService;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.esr.ExternallyStoredResourceAddress;
import ca.uhn.fhir.jpa.esr.ExternallyStoredResourceAddressMetadataKey;
import ca.uhn.fhir.jpa.esr.ExternallyStoredResourceServiceRegistry;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.cross.IResourceLookup;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.BaseHasResource;
import ca.uhn.fhir.jpa.model.entity.BaseTag;
import ca.uhn.fhir.jpa.model.entity.ResourceEncodingEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryProvenanceEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTag;
import ca.uhn.fhir.jpa.model.entity.TagDefinition;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.model.search.ExtendedHSearchIndexData;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.searchparam.extractor.LogicalReferenceHelper;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryResourceMatcher;
import ca.uhn.fhir.jpa.sp.ISearchParamPresenceSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.util.AddRemoveCount;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.jpa.util.QueryChunker;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.InterceptorInvocationTimingEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.util.CoverageIgnore;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.MetaUtil;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.XmlUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import com.google.common.hash.HashCode;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IDomainResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.isEqualCollection;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.left;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * TODO: JA - This class has only one subclass now. Historically it was a common
 * ancestor for BaseHapiFhirSystemDao and BaseHapiFhirResourceDao but I've untangled
 * the former from this hierarchy in order to simplify moving common functionality
 * for resource DAOs into the hapi-fhir-storage project. This class should be merged
 * into BaseHapiFhirResourceDao, but that should be done in its own dedicated PR
 * since it'll be a noisy change.
 */
@SuppressWarnings("WeakerAccess")
@Repository
public abstract class BaseHapiFhirDao<T extends IBaseResource> extends BaseStorageResourceDao<T>
		implements IDao, IJpaDao<T>, ApplicationContextAware {

	public static final long INDEX_STATUS_INDEXED = 1L;
	public static final long INDEX_STATUS_INDEXING_FAILED = 2L;
	public static final String NS_JPA_PROFILE = "https://github.com/hapifhir/hapi-fhir/ns/jpa/profile";
	// total attempts to do a tag transaction
	private static final int TOTAL_TAG_READ_ATTEMPTS = 10;
	private static final Logger ourLog = LoggerFactory.getLogger(BaseHapiFhirDao.class);
	private static boolean ourValidationDisabledForUnitTest;
	private static boolean ourDisableIncrementOnUpdateForUnitTest = false;

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;

	@Autowired
	protected IIdHelperService<JpaPid> myIdHelperService;

	@Autowired
	protected ISearchCoordinatorSvc<JpaPid> mySearchCoordinatorSvc;

	@Autowired
	protected ITermReadSvc myTerminologySvc;

	@Autowired
	protected IResourceHistoryTableDao myResourceHistoryTableDao;

	@Autowired
	protected IResourceTableDao myResourceTableDao;

	@Autowired
	protected IResourceLinkDao myResourceLinkDao;

	@Autowired
	protected IResourceTagDao myResourceTagDao;

	@Autowired
	protected DeleteConflictService myDeleteConflictService;

	@Autowired
	protected IInterceptorBroadcaster myInterceptorBroadcaster;

	@Autowired
	protected InMemoryResourceMatcher myInMemoryResourceMatcher;

	@Autowired
	protected IJpaStorageResourceParser myJpaStorageResourceParser;

	@Autowired
	protected PartitionSettings myPartitionSettings;

	@Autowired
	ExpungeService myExpungeService;

	@Autowired
	private ExternallyStoredResourceServiceRegistry myExternallyStoredResourceServiceRegistry;

	@Autowired
	private ISearchParamPresenceSvc mySearchParamPresenceSvc;

	@Autowired
	private SearchParamWithInlineReferencesExtractor mySearchParamWithInlineReferencesExtractor;

	@Autowired
	private DaoSearchParamSynchronizer myDaoSearchParamSynchronizer;

	private FhirContext myContext;
	private ApplicationContext myApplicationContext;

	@Autowired
	private IPartitionLookupSvc myPartitionLookupSvc;

	@Autowired
	private MemoryCacheService myMemoryCacheService;

	@Autowired(required = false)
	private IFulltextSearchSvc myFulltextSearchSvc;

	@Autowired
	private PlatformTransactionManager myTransactionManager;

	@Autowired
	protected ResourceHistoryCalculator myResourceHistoryCalculator;

	protected final CodingSpy myCodingSpy = new CodingSpy();

	@VisibleForTesting
	public void setExternallyStoredResourceServiceRegistryForUnitTest(
			ExternallyStoredResourceServiceRegistry theExternallyStoredResourceServiceRegistry) {
		myExternallyStoredResourceServiceRegistry = theExternallyStoredResourceServiceRegistry;
	}

	@VisibleForTesting
	public void setSearchParamPresenceSvc(ISearchParamPresenceSvc theSearchParamPresenceSvc) {
		mySearchParamPresenceSvc = theSearchParamPresenceSvc;
	}

	@VisibleForTesting
	public void setResourceHistoryCalculator(ResourceHistoryCalculator theResourceHistoryCalculator) {
		myResourceHistoryCalculator = theResourceHistoryCalculator;
	}

	@Override
	protected IInterceptorBroadcaster getInterceptorBroadcaster() {
		return myInterceptorBroadcaster;
	}

	protected ApplicationContext getApplicationContext() {
		return myApplicationContext;
	}

	@Override
	public void setApplicationContext(@Nonnull ApplicationContext theApplicationContext) throws BeansException {
		/*
		 * We do a null check here because Smile's module system tries to
		 * initialize the application context twice if two modules depend on
		 * the persistence module. The second time sets the dependency's appctx.
		 */
		if (myApplicationContext == null) {
			myApplicationContext = theApplicationContext;
		}
	}

	private void extractHapiTags(
			TransactionDetails theTransactionDetails,
			IResource theResource,
			ResourceTable theEntity,
			Set<ResourceTag> allDefs) {
		TagList tagList = ResourceMetadataKeyEnum.TAG_LIST.get(theResource);
		if (tagList != null) {
			for (Tag next : tagList) {
				TagDefinition def = getTagOrNull(
						theTransactionDetails,
						TagTypeEnum.TAG,
						next.getScheme(),
						next.getTerm(),
						next.getLabel(),
						next.getVersion(),
						myCodingSpy.getBooleanObject(next));
				if (def != null) {
					ResourceTag tag = theEntity.addTag(def);
					allDefs.add(tag);
					theEntity.setHasTags(true);
				}
			}
		}

		List<BaseCodingDt> securityLabels = ResourceMetadataKeyEnum.SECURITY_LABELS.get(theResource);
		if (securityLabels != null) {
			for (BaseCodingDt next : securityLabels) {
				TagDefinition def = getTagOrNull(
						theTransactionDetails,
						TagTypeEnum.SECURITY_LABEL,
						next.getSystemElement().getValue(),
						next.getCodeElement().getValue(),
						next.getDisplayElement().getValue(),
						next.getVersionElement().getValue(),
						next.getUserSelectedElement().getValue());
				if (def != null) {
					ResourceTag tag = theEntity.addTag(def);
					allDefs.add(tag);
					theEntity.setHasTags(true);
				}
			}
		}

		List<IdDt> profiles = ResourceMetadataKeyEnum.PROFILES.get(theResource);
		if (profiles != null) {
			for (IIdType next : profiles) {
				TagDefinition def = getTagOrNull(
						theTransactionDetails, TagTypeEnum.PROFILE, NS_JPA_PROFILE, next.getValue(), null, null, null);
				if (def != null) {
					ResourceTag tag = theEntity.addTag(def);
					allDefs.add(tag);
					theEntity.setHasTags(true);
				}
			}
		}
	}

	private void extractRiTags(
			TransactionDetails theTransactionDetails,
			IAnyResource theResource,
			ResourceTable theEntity,
			Set<ResourceTag> theAllTags) {
		List<? extends IBaseCoding> tagList = theResource.getMeta().getTag();
		if (tagList != null) {
			for (IBaseCoding next : tagList) {
				TagDefinition def = getTagOrNull(
						theTransactionDetails,
						TagTypeEnum.TAG,
						next.getSystem(),
						next.getCode(),
						next.getDisplay(),
						next.getVersion(),
						myCodingSpy.getBooleanObject(next));
				if (def != null) {
					ResourceTag tag = theEntity.addTag(def);
					theAllTags.add(tag);
					theEntity.setHasTags(true);
				}
			}
		}

		List<? extends IBaseCoding> securityLabels = theResource.getMeta().getSecurity();
		if (securityLabels != null) {
			for (IBaseCoding next : securityLabels) {
				TagDefinition def = getTagOrNull(
						theTransactionDetails,
						TagTypeEnum.SECURITY_LABEL,
						next.getSystem(),
						next.getCode(),
						next.getDisplay(),
						next.getVersion(),
						myCodingSpy.getBooleanObject(next));
				if (def != null) {
					ResourceTag tag = theEntity.addTag(def);
					theAllTags.add(tag);
					theEntity.setHasTags(true);
				}
			}
		}

		List<? extends IPrimitiveType<String>> profiles = theResource.getMeta().getProfile();
		if (profiles != null) {
			for (IPrimitiveType<String> next : profiles) {
				TagDefinition def = getTagOrNull(
						theTransactionDetails, TagTypeEnum.PROFILE, NS_JPA_PROFILE, next.getValue(), null, null, null);
				if (def != null) {
					ResourceTag tag = theEntity.addTag(def);
					theAllTags.add(tag);
					theEntity.setHasTags(true);
				}
			}
		}
	}

	private void extractProfileTags(
			TransactionDetails theTransactionDetails,
			IBaseResource theResource,
			ResourceTable theEntity,
			Set<ResourceTag> theAllTags) {
		RuntimeResourceDefinition def = myContext.getResourceDefinition(theResource);
		if (!def.isStandardType()) {
			String profile = def.getResourceProfile("");
			if (isNotBlank(profile)) {
				TagDefinition profileDef = getTagOrNull(
						theTransactionDetails, TagTypeEnum.PROFILE, NS_JPA_PROFILE, profile, null, null, null);

				ResourceTag tag = theEntity.addTag(profileDef);
				theAllTags.add(tag);
				theEntity.setHasTags(true);
			}
		}
	}

	private Set<ResourceTag> getAllTagDefinitions(ResourceTable theEntity) {
		HashSet<ResourceTag> retVal = Sets.newHashSet();
		if (theEntity.isHasTags()) {
			retVal.addAll(theEntity.getTags());
		}
		return retVal;
	}

	@Override
	public JpaStorageSettings getStorageSettings() {
		return myStorageSettings;
	}

	@Override
	public FhirContext getContext() {
		return myContext;
	}

	@Autowired
	public void setContext(FhirContext theContext) {
		super.myFhirContext = theContext;
		myContext = theContext;
	}

	/**
	 * <code>null</code> will only be returned if the scheme and tag are both blank
	 */
	protected TagDefinition getTagOrNull(
			TransactionDetails theTransactionDetails,
			TagTypeEnum theTagType,
			String theScheme,
			String theTerm,
			String theLabel,
			String theVersion,
			Boolean theUserSelected) {
		if (isBlank(theScheme) && isBlank(theTerm) && isBlank(theLabel)) {
			return null;
		}

		MemoryCacheService.TagDefinitionCacheKey key =
				toTagDefinitionMemoryCacheKey(theTagType, theScheme, theTerm, theVersion, theUserSelected);

		TagDefinition retVal = myMemoryCacheService.getIfPresent(MemoryCacheService.CacheEnum.TAG_DEFINITION, key);
		if (retVal == null) {
			HashMap<MemoryCacheService.TagDefinitionCacheKey, TagDefinition> resolvedTagDefinitions =
					theTransactionDetails.getOrCreateUserData(
							HapiTransactionService.XACT_USERDATA_KEY_RESOLVED_TAG_DEFINITIONS, HashMap::new);

			retVal = resolvedTagDefinitions.get(key);

			if (retVal == null) {
				// actual DB hit(s) happen here
				retVal = getOrCreateTag(theTagType, theScheme, theTerm, theLabel, theVersion, theUserSelected);

				TransactionSynchronization sync = new AddTagDefinitionToCacheAfterCommitSynchronization(key, retVal);
				TransactionSynchronizationManager.registerSynchronization(sync);

				resolvedTagDefinitions.put(key, retVal);
			}
		}

		return retVal;
	}

	/**
	 * Gets the tag defined by the fed in values, or saves it if it does not
	 * exist.
	 * <p>
	 * Can also throw an InternalErrorException if something bad happens.
	 */
	private TagDefinition getOrCreateTag(
			TagTypeEnum theTagType,
			String theScheme,
			String theTerm,
			String theLabel,
			String theVersion,
			Boolean theUserSelected) {

		TypedQuery<TagDefinition> q = buildTagQuery(theTagType, theScheme, theTerm, theVersion, theUserSelected);
		q.setMaxResults(1);

		TransactionTemplate template = new TransactionTemplate(myTransactionManager);
		template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

		// this transaction will attempt to get or create the tag,
		// repeating (on any failure) 10 times.
		// if it fails more than this, we will throw exceptions
		TagDefinition retVal;
		int count = 0;
		HashSet<Throwable> throwables = new HashSet<>();
		do {
			try {
				retVal = template.execute(new TransactionCallback<TagDefinition>() {

					// do the actual DB call(s) to read and/or write the values
					private TagDefinition readOrCreate() {
						TagDefinition val;
						try {
							val = q.getSingleResult();
						} catch (NoResultException e) {
							val = new TagDefinition(theTagType, theScheme, theTerm, theLabel);
							val.setVersion(theVersion);
							val.setUserSelected(theUserSelected);
							myEntityManager.persist(val);
						}
						return val;
					}

					@Override
					public TagDefinition doInTransaction(TransactionStatus status) {
						TagDefinition tag = null;

						try {
							tag = readOrCreate();
						} catch (Exception ex) {
							// log any exceptions - just in case
							// they may be signs of things to come...
							ourLog.warn(
									"Tag read/write failed: "
											+ ex.getMessage() + ". "
											+ "This is not a failure on its own, "
											+ "but could be useful information in the result of an actual failure.",
									ex);
							throwables.add(ex);
						}

						return tag;
					}
				});
			} catch (Exception ex) {
				// transaction template can fail if connections to db are exhausted and/or timeout
				ourLog.warn(
						"Transaction failed with: {}. Transaction will rollback and be reattempted.", ex.getMessage());
				retVal = null;
			}
			count++;
		} while (retVal == null && count < TOTAL_TAG_READ_ATTEMPTS);

		if (retVal == null) {
			// if tag is still null,
			// something bad must be happening
			// - throw
			String msg = throwables.stream().map(Throwable::getMessage).collect(Collectors.joining(", "));
			throw new InternalErrorException(Msg.code(2023)
					+ "Tag get/create failed after "
					+ TOTAL_TAG_READ_ATTEMPTS
					+ " attempts with error(s): "
					+ msg);
		}

		return retVal;
	}

	private TypedQuery<TagDefinition> buildTagQuery(
			TagTypeEnum theTagType, String theScheme, String theTerm, String theVersion, Boolean theUserSelected) {
		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<TagDefinition> cq = builder.createQuery(TagDefinition.class);
		Root<TagDefinition> from = cq.from(TagDefinition.class);

		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.and(
				builder.equal(from.get("myTagType"), theTagType), builder.equal(from.get("myCode"), theTerm)));

		predicates.add(
				isBlank(theScheme)
						? builder.isNull(from.get("mySystem"))
						: builder.equal(from.get("mySystem"), theScheme));

		predicates.add(
				isBlank(theVersion)
						? builder.isNull(from.get("myVersion"))
						: builder.equal(from.get("myVersion"), theVersion));

		predicates.add(
				isNull(theUserSelected)
						? builder.isNull(from.get("myUserSelected"))
						: builder.equal(from.get("myUserSelected"), theUserSelected));

		cq.where(predicates.toArray(new Predicate[0]));
		return myEntityManager.createQuery(cq);
	}

	void incrementId(T theResource, ResourceTable theSavedEntity, IIdType theResourceId) {
		if (theResourceId == null || theResourceId.getVersionIdPart() == null) {
			theSavedEntity.initializeVersion();
		} else {
			theSavedEntity.markVersionUpdatedInCurrentTransaction();
		}

		assert theResourceId != null;
		String newVersion = Long.toString(theSavedEntity.getVersion());
		IIdType newId = theResourceId.withVersion(newVersion);
		theResource.getIdElement().setValue(newId.getValue());
	}

	public boolean isLogicalReference(IIdType theId) {
		return LogicalReferenceHelper.isLogicalReference(myStorageSettings, theId);
	}

	/**
	 * Returns {@literal true} if the resource has changed (either the contents or the tags)
	 */
	protected EncodedResource populateResourceIntoEntity(
			TransactionDetails theTransactionDetails,
			RequestDetails theRequest,
			IBaseResource theResource,
			ResourceTable theEntity,
			boolean thePerformIndexing) {
		if (theEntity.getResourceType() == null) {
			theEntity.setResourceType(toResourceName(theResource));
		}

		byte[] resourceBinary;
		String resourceText;
		ResourceEncodingEnum encoding;
		boolean changed = false;

		if (theEntity.getDeleted() == null) {

			if (thePerformIndexing) {

				ExternallyStoredResourceAddress address = null;
				if (myExternallyStoredResourceServiceRegistry.hasProviders()) {
					address = ExternallyStoredResourceAddressMetadataKey.INSTANCE.get(theResource);
				}

				if (address != null) {

					encoding = ResourceEncodingEnum.ESR;
					resourceBinary = null;
					resourceText = address.getProviderId() + ":" + address.getLocation();
					changed = true;

				} else {

					encoding = myStorageSettings.getResourceEncoding();

					String resourceType = theEntity.getResourceType();

					List<String> excludeElements = new ArrayList<>(8);
					IBaseMetaType meta = theResource.getMeta();

					IBaseExtension<?, ?> sourceExtension = getExcludedElements(resourceType, excludeElements, meta);

					theEntity.setFhirVersion(myContext.getVersion().getVersion());

					// TODO:  LD: Once 2024-02 it out the door we should consider further refactoring here to move
					// more of this logic within the calculator and eliminate more local variables
					final ResourceHistoryState calculate = myResourceHistoryCalculator.calculateResourceHistoryState(
							theResource, encoding, excludeElements);

					resourceText = calculate.getResourceText();
					resourceBinary = calculate.getResourceBinary();
					encoding = calculate.getEncoding(); // This may be a no-op
					final HashCode hashCode = calculate.getHashCode();

					String hashSha256 = hashCode.toString();
					if (!hashSha256.equals(theEntity.getHashSha256())) {
						changed = true;
					}
					theEntity.setHashSha256(hashSha256);

					if (sourceExtension != null) {
						IBaseExtension<?, ?> newSourceExtension = ((IBaseHasExtensions) meta).addExtension();
						newSourceExtension.setUrl(sourceExtension.getUrl());
						newSourceExtension.setValue(sourceExtension.getValue());
					}
				}

			} else {

				encoding = null;
				resourceBinary = null;
				resourceText = null;
			}

			boolean skipUpdatingTags = myStorageSettings.isMassIngestionMode() && theEntity.isHasTags();
			skipUpdatingTags |= myStorageSettings.getTagStorageMode() == JpaStorageSettings.TagStorageModeEnum.INLINE;

			if (!skipUpdatingTags) {
				changed |= updateTags(theTransactionDetails, theRequest, theResource, theEntity);
			}

		} else {

			if (nonNull(theEntity.getHashSha256())) {
				theEntity.setHashSha256(null);
				changed = true;
			}

			resourceBinary = null;
			resourceText = null;
			encoding = ResourceEncodingEnum.DEL;
		}

		if (thePerformIndexing && !changed) {
			if (theEntity.getId() == null) {
				changed = true;
			} else if (myStorageSettings.isMassIngestionMode()) {

				// Don't check existing - We'll rely on the SHA256 hash only

			} else if (theEntity.getVersion() == 1L && theEntity.getCurrentVersionEntity() == null) {

				// No previous version if this is the first version

			} else {
				ResourceHistoryTable currentHistoryVersion = theEntity.getCurrentVersionEntity();
				if (currentHistoryVersion == null) {
					currentHistoryVersion = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(
							theEntity.getId(), theEntity.getVersion());
				}
				if (currentHistoryVersion == null || !currentHistoryVersion.hasResource()) {
					changed = true;
				} else {
					// TODO:  LD: Once 2024-02 it out the door we should consider further refactoring here to move
					// more of this logic within the calculator and eliminate more local variables
					changed = myResourceHistoryCalculator.isResourceHistoryChanged(
							currentHistoryVersion, resourceBinary, resourceText);
				}
			}
		}

		EncodedResource retVal = new EncodedResource();
		retVal.setEncoding(encoding);
		retVal.setResourceBinary(resourceBinary);
		retVal.setResourceText(resourceText);
		retVal.setChanged(changed);

		return retVal;
	}

	/**
	 * helper to format the meta element for serialization of the resource.
	 *
	 * @param theResourceType    the resource type of the resource
	 * @param theExcludeElements list of extensions in the meta element to exclude from serialization
	 * @param theMeta            the meta element of the resource
	 * @return source extension if present in the meta element
	 */
	private IBaseExtension<?, ?> getExcludedElements(
			String theResourceType, List<String> theExcludeElements, IBaseMetaType theMeta) {
		boolean hasExtensions = false;
		IBaseExtension<?, ?> sourceExtension = null;
		if (theMeta instanceof IBaseHasExtensions) {
			List<? extends IBaseExtension<?, ?>> extensions = ((IBaseHasExtensions) theMeta).getExtension();
			if (!extensions.isEmpty()) {
				hasExtensions = true;

				/*
				 * FHIR DSTU3 did not have the Resource.meta.source field, so we use a
				 * custom HAPI FHIR extension in Resource.meta to store that field. However,
				 * we put the value for that field in a separate table, so we don't want to serialize
				 * it into the stored BLOB. Therefore: remove it from the resource temporarily
				 * and restore it afterward.
				 */
				if (myFhirContext.getVersion().getVersion().equals(FhirVersionEnum.DSTU3)) {
					for (int i = 0; i < extensions.size(); i++) {
						if (extensions.get(i).getUrl().equals(HapiExtensions.EXT_META_SOURCE)) {
							sourceExtension = extensions.remove(i);
							i--;
						}
					}
				}
				boolean allExtensionsRemoved = extensions.isEmpty();
				if (allExtensionsRemoved) {
					hasExtensions = false;
				}
			}
		}

		theExcludeElements.add("id");
		boolean inlineTagMode =
				getStorageSettings().getTagStorageMode() == JpaStorageSettings.TagStorageModeEnum.INLINE;
		if (hasExtensions || inlineTagMode) {
			if (!inlineTagMode) {
				theExcludeElements.add(theResourceType + ".meta.profile");
				theExcludeElements.add(theResourceType + ".meta.tag");
				theExcludeElements.add(theResourceType + ".meta.security");
			}
			theExcludeElements.add(theResourceType + ".meta.versionId");
			theExcludeElements.add(theResourceType + ".meta.lastUpdated");
			theExcludeElements.add(theResourceType + ".meta.source");
		} else {
			/*
			 * If there are no extensions in the meta element, we can just exclude the
			 * whole meta element, which avoids adding an empty "meta":{}
			 * from showing up in the serialized JSON.
			 */
			theExcludeElements.add(theResourceType + ".meta");
		}
		return sourceExtension;
	}

	private boolean updateTags(
			TransactionDetails theTransactionDetails,
			RequestDetails theRequest,
			IBaseResource theResource,
			ResourceTable theEntity) {
		Set<ResourceTag> allResourceTagsFromTheResource = new HashSet<>();
		Set<ResourceTag> allOriginalResourceTagsFromTheEntity = getAllTagDefinitions(theEntity);

		if (theResource instanceof IResource) {
			extractHapiTags(theTransactionDetails, (IResource) theResource, theEntity, allResourceTagsFromTheResource);
		} else {
			extractRiTags(theTransactionDetails, (IAnyResource) theResource, theEntity, allResourceTagsFromTheResource);
		}

		extractProfileTags(theTransactionDetails, theResource, theEntity, allResourceTagsFromTheResource);

		// the extract[Hapi|Ri|Profile]Tags methods above will have populated the allResourceTagsFromTheResource Set
		// AND
		// added all tags from theResource.meta.tags to theEntity.meta.tags.  the next steps are to:
		// 1- remove duplicates;
		// 2- remove tags from theEntity that are not present in theResource if header HEADER_META_SNAPSHOT_MODE
		// is present in the request;
		//
		Set<ResourceTag> allResourceTagsNewAndOldFromTheEntity = getAllTagDefinitions(theEntity);
		Set<TagDefinition> allTagDefinitionsPresent = new HashSet<>();

		allResourceTagsNewAndOldFromTheEntity.forEach(tag -> {

			// Don't keep duplicate tags
			if (!allTagDefinitionsPresent.add(tag.getTag())) {
				theEntity.getTags().remove(tag);
			}

			// Drop any tags that have been removed
			if (!allResourceTagsFromTheResource.contains(tag)) {
				if (shouldDroppedTagBeRemovedOnUpdate(theRequest, tag)) {
					theEntity.getTags().remove(tag);
				} else if (HapiExtensions.EXT_SUBSCRIPTION_MATCHING_STRATEGY.equals(
						tag.getTag().getSystem())) {
					theEntity.getTags().remove(tag);
				}
			}
		});

		// at this point, theEntity.meta.tags will be up to date:
		// 1- it was stripped from tags that needed removing;
		// 2- it has new tags from a resource update through theResource;
		// 3- it has tags from the previous version;
		//
		// Since tags are merged on updates, we add tags from theEntity that theResource does not have
		Set<ResourceTag> allUpdatedResourceTagsNewAndOldMinusRemovalsFromTheEntity = getAllTagDefinitions(theEntity);

		allUpdatedResourceTagsNewAndOldMinusRemovalsFromTheEntity.forEach(aResourcetag -> {
			if (!allResourceTagsFromTheResource.contains(aResourcetag)) {
				IBaseCoding iBaseCoding = theResource
						.getMeta()
						.addTag()
						.setCode(aResourcetag.getTag().getCode())
						.setSystem(aResourcetag.getTag().getSystem())
						.setVersion(aResourcetag.getTag().getVersion());

				allResourceTagsFromTheResource.add(aResourcetag);

				if (aResourcetag.getTag().getUserSelected() != null) {
					iBaseCoding.setUserSelected(aResourcetag.getTag().getUserSelected());
				}
			}
		});

		theEntity.setHasTags(!allUpdatedResourceTagsNewAndOldMinusRemovalsFromTheEntity.isEmpty());
		return !isEqualCollection(allOriginalResourceTagsFromTheEntity, allResourceTagsFromTheResource);
	}

	/**
	 * Subclasses may override to provide behaviour. Called when a pre-existing resource has been updated in the database
	 *
	 * @param theEntity The resource
	 */
	protected void postDelete(ResourceTable theEntity) {
		// nothing
	}

	/**
	 * Subclasses may override to provide behaviour. Called when a resource has been inserted into the database for the first time.
	 *
	 * @param theEntity         The entity being updated (Do not modify the entity! Undefined behaviour will occur!)
	 * @param theResource       The resource being persisted
	 * @param theRequestDetails The request details, needed for partition support
	 */
	protected void postPersist(ResourceTable theEntity, T theResource, RequestDetails theRequestDetails) {
		// nothing
	}

	/**
	 * Subclasses may override to provide behaviour. Called when a pre-existing resource has been updated in the database
	 *
	 * @param theEntity         The resource
	 * @param theResource       The resource being persisted
	 * @param theRequestDetails The request details, needed for partition support
	 */
	protected void postUpdate(ResourceTable theEntity, T theResource, RequestDetails theRequestDetails) {
		// nothing
	}

	@Override
	@CoverageIgnore
	public BaseHasResource readEntity(IIdType theValueId, RequestDetails theRequest) {
		throw new NotImplementedException(Msg.code(927) + "");
	}

	/**
	 * This method is called when an update to an existing resource detects that the resource supplied for update is missing a tag/profile/security label that the currently persisted resource holds.
	 * <p>
	 * The default implementation removes any profile declarations, but leaves tags and security labels in place. Subclasses may choose to override and change this behaviour.
	 * </p>
	 * <p>
	 * See <a href="http://hl7.org/fhir/resource.html#tag-updates">Updates to Tags, Profiles, and Security Labels</a> for a description of the logic that the default behaviour follows.
	 * </p>
	 *
	 * @param theTag The tag
	 * @return Returns <code>true</code> if the tag should be removed
	 */
	protected boolean shouldDroppedTagBeRemovedOnUpdate(RequestDetails theRequest, ResourceTag theTag) {

		Set<TagTypeEnum> metaSnapshotModeTokens = null;

		if (theRequest != null) {
			List<String> metaSnapshotMode = theRequest.getHeaders(JpaConstants.HEADER_META_SNAPSHOT_MODE);
			if (metaSnapshotMode != null && !metaSnapshotMode.isEmpty()) {
				metaSnapshotModeTokens = new HashSet<>();
				for (String nextHeaderValue : metaSnapshotMode) {
					StringTokenizer tok = new StringTokenizer(nextHeaderValue, ",");
					while (tok.hasMoreTokens()) {
						switch (trim(tok.nextToken())) {
							case "TAG":
								metaSnapshotModeTokens.add(TagTypeEnum.TAG);
								break;
							case "PROFILE":
								metaSnapshotModeTokens.add(TagTypeEnum.PROFILE);
								break;
							case "SECURITY_LABEL":
								metaSnapshotModeTokens.add(TagTypeEnum.SECURITY_LABEL);
								break;
						}
					}
				}
			}
		}

		if (metaSnapshotModeTokens == null) {
			metaSnapshotModeTokens = Collections.singleton(TagTypeEnum.PROFILE);
		}

		return metaSnapshotModeTokens.contains(theTag.getTag().getTagType());
	}

	String toResourceName(IBaseResource theResource) {
		return myContext.getResourceType(theResource);
	}

	@VisibleForTesting
	public void setEntityManager(EntityManager theEntityManager) {
		myEntityManager = theEntityManager;
	}

	@VisibleForTesting
	public void setSearchParamWithInlineReferencesExtractor(
			SearchParamWithInlineReferencesExtractor theSearchParamWithInlineReferencesExtractor) {
		mySearchParamWithInlineReferencesExtractor = theSearchParamWithInlineReferencesExtractor;
	}

	@VisibleForTesting
	public void setResourceHistoryTableDao(IResourceHistoryTableDao theResourceHistoryTableDao) {
		myResourceHistoryTableDao = theResourceHistoryTableDao;
	}

	@VisibleForTesting
	public void setDaoSearchParamSynchronizer(DaoSearchParamSynchronizer theDaoSearchParamSynchronizer) {
		myDaoSearchParamSynchronizer = theDaoSearchParamSynchronizer;
	}

	private void verifyMatchUrlForConditionalCreateOrUpdate(
			CreateOrUpdateByMatch theCreateOrUpdate,
			IBaseResource theResource,
			String theIfNoneExist,
			ResourceIndexedSearchParams theParams,
			RequestDetails theRequestDetails) {
		// Make sure that the match URL was actually appropriate for the supplied resource
		InMemoryMatchResult outcome =
				myInMemoryResourceMatcher.match(theIfNoneExist, theResource, theParams, theRequestDetails);

		if (outcome.supported() && !outcome.matched()) {
			String errorMsg = getConditionalCreateOrUpdateErrorMsg(theCreateOrUpdate);
			throw new InvalidRequestException(Msg.code(929) + errorMsg);
		}
	}

	private String getConditionalCreateOrUpdateErrorMsg(CreateOrUpdateByMatch theCreateOrUpdate) {
		return String.format(
				"Failed to process conditional %s. " + "The supplied resource did not satisfy the conditional URL.",
				theCreateOrUpdate.name().toLowerCase());
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResourceTable updateEntity(
			RequestDetails theRequest,
			final IBaseResource theResource,
			IBasePersistedResource theEntity,
			Date theDeletedTimestampOrNull,
			boolean thePerformIndexing,
			boolean theUpdateVersion,
			TransactionDetails theTransactionDetails,
			boolean theForceUpdate,
			boolean theCreateNewHistoryEntry) {
		Validate.notNull(theEntity);
		Validate.isTrue(
				theDeletedTimestampOrNull != null || theResource != null,
				"Must have either a resource[%s] or a deleted timestamp[%s] for resource PID[%s]",
				theDeletedTimestampOrNull != null,
				theResource != null,
				theEntity.getPersistentId());

		ourLog.debug("Starting entity update");

		ResourceTable entity = (ResourceTable) theEntity;

		/*
		 * This should be the very first thing..
		 */
		if (theResource != null) {
			if (thePerformIndexing && theDeletedTimestampOrNull == null) {
				if (!ourValidationDisabledForUnitTest) {
					validateResourceForStorage((T) theResource, entity);
				}
			}
			if (!StringUtils.isBlank(entity.getResourceType())) {
				validateIncomingResourceTypeMatchesExisting(theResource, entity);
			}
		}

		if (entity.getPublished() == null) {
			ourLog.debug("Entity has published time: {}", theTransactionDetails.getTransactionDate());
			entity.setPublished(theTransactionDetails.getTransactionDate());
		}

		ResourceIndexedSearchParams existingParams = null;

		ResourceIndexedSearchParams newParams = null;

		EncodedResource changed;
		if (theDeletedTimestampOrNull != null) {
			// DELETE

			entity.setDeleted(theDeletedTimestampOrNull);
			entity.setUpdated(theDeletedTimestampOrNull);
			entity.setNarrativeText(null);
			entity.setContentText(null);
			entity.setIndexStatus(INDEX_STATUS_INDEXED);
			changed = populateResourceIntoEntity(theTransactionDetails, theRequest, theResource, entity, true);

		} else {

			// CREATE or UPDATE

			IdentityHashMap<ResourceTable, ResourceIndexedSearchParams> existingSearchParams =
					theTransactionDetails.getOrCreateUserData(
							HapiTransactionService.XACT_USERDATA_KEY_EXISTING_SEARCH_PARAMS,
							() -> new IdentityHashMap<>());
			existingParams = existingSearchParams.get(entity);
			if (existingParams == null) {
				existingParams = ResourceIndexedSearchParams.withLists(entity);
				/*
				 * If we have lots of resource links, this proactively fetches the targets so
				 * that we don't look them up one-by-one when comparing the new set to the
				 * old set later on
				 */
				if (existingParams.getResourceLinks().size() >= 10) {
					List<Long> pids = existingParams.getResourceLinks().stream()
							.map(t -> t.getId())
							.collect(Collectors.toList());
					new QueryChunker<Long>().chunk(pids, t -> {
						List<ResourceLink> targets = myResourceLinkDao.findByPidAndFetchTargetDetails(t);
						ourLog.trace("Prefetched targets: {}", targets);
					});
				}
				existingSearchParams.put(entity, existingParams);
			}
			entity.setDeleted(null);

			// TODO: is this IF statement always true? Try removing it
			if (thePerformIndexing || theEntity.getVersion() == 1) {

				newParams = ResourceIndexedSearchParams.withSets();

				RequestPartitionId requestPartitionId;
				if (!myPartitionSettings.isPartitioningEnabled()) {
					requestPartitionId = RequestPartitionId.allPartitions();
				} else if (entity.getPartitionId() != null) {
					requestPartitionId = entity.getPartitionId().toPartitionId();
				} else {
					requestPartitionId = RequestPartitionId.defaultPartition();
				}

				failIfPartitionMismatch(theRequest, entity);

				// Extract search params for resource
				mySearchParamWithInlineReferencesExtractor.populateFromResource(
						requestPartitionId,
						newParams,
						theTransactionDetails,
						entity,
						theResource,
						existingParams,
						theRequest,
						thePerformIndexing);

				// Actually persist the ResourceTable and ResourceHistoryTable entities
				changed = populateResourceIntoEntity(theTransactionDetails, theRequest, theResource, entity, true);

				if (theForceUpdate) {
					changed.setChanged(true);
				}

				if (changed.isChanged()) {
					checkConditionalMatch(
							entity, theUpdateVersion, theResource, thePerformIndexing, newParams, theRequest);

					if (CURRENTLY_REINDEXING.get(theResource) != Boolean.TRUE) {
						entity.setUpdated(theTransactionDetails.getTransactionDate());
					}
					newParams.populateResourceTableSearchParamsPresentFlags(entity);
					entity.setIndexStatus(INDEX_STATUS_INDEXED);
				}

				if (myFulltextSearchSvc != null && !myFulltextSearchSvc.isDisabled()) {
					populateFullTextFields(myContext, theResource, entity, newParams);
				}

			} else {

				entity.setUpdated(theTransactionDetails.getTransactionDate());
				entity.setIndexStatus(null);

				changed = populateResourceIntoEntity(theTransactionDetails, theRequest, theResource, entity, false);
			}
		}

		if (thePerformIndexing
				&& changed != null
				&& !changed.isChanged()
				&& !theForceUpdate
				&& myStorageSettings.isSuppressUpdatesWithNoChange()
				&& (entity.getVersion() > 1 || theUpdateVersion)) {
			ourLog.debug(
					"Resource {} has not changed",
					entity.getIdDt().toUnqualified().getValue());
			if (theResource != null) {
				myJpaStorageResourceParser.updateResourceMetadata(entity, theResource);
			}
			entity.setUnchangedInCurrentOperation(true);
			return entity;
		}

		if (entity.getId() != null && theUpdateVersion) {
			entity.markVersionUpdatedInCurrentTransaction();
		}

		/*
		 * Save the resource itself
		 */
		if (entity.getId() == null) {
			myEntityManager.persist(entity);

			postPersist(entity, (T) theResource, theRequest);

		} else if (entity.getDeleted() != null) {
			entity = myEntityManager.merge(entity);

			postDelete(entity);

		} else {
			entity = myEntityManager.merge(entity);

			postUpdate(entity, (T) theResource, theRequest);
		}

		if (theCreateNewHistoryEntry) {
			createHistoryEntry(theRequest, theResource, entity, changed);
		}

		/*
		 * Update the "search param present" table which is used for the
		 * ?foo:missing=true queries
		 *
		 * Note that we're only populating this for reference params
		 * because the index tables for all other types have a MISSING column
		 * right on them for handling the :missing queries. We can't use the
		 * index table for resource links (reference indexes) because we index
		 * those by path and not by parameter name.
		 */
		if (thePerformIndexing && newParams != null) {
			AddRemoveCount presenceCount =
					mySearchParamPresenceSvc.updatePresence(entity, newParams.mySearchParamPresentEntities);

			// Interceptor broadcast: JPA_PERFTRACE_INFO
			if (!presenceCount.isEmpty()) {
				if (CompositeInterceptorBroadcaster.hasHooks(
						Pointcut.JPA_PERFTRACE_INFO, myInterceptorBroadcaster, theRequest)) {
					StorageProcessingMessage message = new StorageProcessingMessage();
					message.setMessage(
							"For " + entity.getIdDt().toUnqualifiedVersionless().getValue() + " added "
									+ presenceCount.getAddCount() + " and removed " + presenceCount.getRemoveCount()
									+ " resource search parameter presence entries");
					HookParams params = new HookParams()
							.add(RequestDetails.class, theRequest)
							.addIfMatchesType(ServletRequestDetails.class, theRequest)
							.add(StorageProcessingMessage.class, message);
					CompositeInterceptorBroadcaster.doCallHooks(
							myInterceptorBroadcaster, theRequest, Pointcut.JPA_PERFTRACE_INFO, params);
				}
			}
		}

		/*
		 * Indexing
		 */
		if (thePerformIndexing) {
			if (newParams == null) {
				myExpungeService.deleteAllSearchParams(JpaPid.fromId(entity.getId()));
				entity.clearAllParamsPopulated();
			} else {

				// Synchronize search param indexes
				AddRemoveCount searchParamAddRemoveCount =
						myDaoSearchParamSynchronizer.synchronizeSearchParamsToDatabase(
								newParams, entity, existingParams);

				newParams.populateResourceTableParamCollections(entity);

				// Interceptor broadcast: JPA_PERFTRACE_INFO
				if (!searchParamAddRemoveCount.isEmpty()) {
					if (CompositeInterceptorBroadcaster.hasHooks(
							Pointcut.JPA_PERFTRACE_INFO, myInterceptorBroadcaster, theRequest)) {
						StorageProcessingMessage message = new StorageProcessingMessage();
						message.setMessage("For "
								+ entity.getIdDt().toUnqualifiedVersionless().getValue() + " added "
								+ searchParamAddRemoveCount.getAddCount() + " and removed "
								+ searchParamAddRemoveCount.getRemoveCount()
								+ " resource search parameter index entries");
						HookParams params = new HookParams()
								.add(RequestDetails.class, theRequest)
								.addIfMatchesType(ServletRequestDetails.class, theRequest)
								.add(StorageProcessingMessage.class, message);
						CompositeInterceptorBroadcaster.doCallHooks(
								myInterceptorBroadcaster, theRequest, Pointcut.JPA_PERFTRACE_INFO, params);
					}
				}
			}
		}

		if (theResource != null) {
			myJpaStorageResourceParser.updateResourceMetadata(entity, theResource);
		}

		return entity;
	}

	/**
	 * Make sure that the match URL was actually appropriate for the supplied
	 * resource, if so configured, or do it only for first version, since technically it
	 * is possible (and legal) for someone to be using a conditional update
	 * to match a resource and then update it in a way that it no longer
	 * matches.
	 */
	private void checkConditionalMatch(
			ResourceTable theEntity,
			boolean theUpdateVersion,
			IBaseResource theResource,
			boolean thePerformIndexing,
			ResourceIndexedSearchParams theNewParams,
			RequestDetails theRequest) {

		if (!thePerformIndexing) {
			return;
		}

		if (theEntity.getCreatedByMatchUrl() == null && theEntity.getUpdatedByMatchUrl() == null) {
			return;
		}

		// version is not updated at this point, but could be pending for update, which we consider here
		long pendingVersion = theEntity.getVersion();
		if (theUpdateVersion && !theEntity.isVersionUpdatedInCurrentTransaction()) {
			pendingVersion++;
		}

		if (myStorageSettings.isPreventInvalidatingConditionalMatchCriteria() || pendingVersion <= 1L) {
			String createOrUpdateUrl;
			CreateOrUpdateByMatch createOrUpdate;

			if (theEntity.getCreatedByMatchUrl() != null) {
				createOrUpdateUrl = theEntity.getCreatedByMatchUrl();
				createOrUpdate = CreateOrUpdateByMatch.CREATE;
			} else {
				createOrUpdateUrl = theEntity.getUpdatedByMatchUrl();
				createOrUpdate = CreateOrUpdateByMatch.UPDATE;
			}

			verifyMatchUrlForConditionalCreateOrUpdate(
					createOrUpdate, theResource, createOrUpdateUrl, theNewParams, theRequest);
		}
	}

	public IBasePersistedResource updateHistoryEntity(
			RequestDetails theRequest,
			T theResource,
			IBasePersistedResource theEntity,
			IBasePersistedResource theHistoryEntity,
			IIdType theResourceId,
			TransactionDetails theTransactionDetails,
			boolean isUpdatingCurrent) {
		Validate.notNull(theEntity);
		Validate.isTrue(
				theResource != null,
				"Must have either a resource[%s] for resource PID[%s]",
				theResource != null,
				theEntity.getPersistentId());

		ourLog.debug("Starting history entity update");
		EncodedResource encodedResource = new EncodedResource();
		ResourceHistoryTable historyEntity;

		if (isUpdatingCurrent) {
			ResourceTable entity = (ResourceTable) theEntity;

			IBaseResource oldResource;
			if (getStorageSettings().isMassIngestionMode()) {
				oldResource = null;
			} else {
				oldResource = myJpaStorageResourceParser.toResource(entity, false);
			}

			notifyInterceptors(theRequest, theResource, oldResource, theTransactionDetails, true);

			ResourceTable savedEntity = updateEntity(
					theRequest, theResource, entity, null, true, false, theTransactionDetails, false, false);
			// Have to call populate again for the encodedResource, since using createHistoryEntry() will cause version
			// constraint failure, ie updating the same resource at the same time
			encodedResource = populateResourceIntoEntity(theTransactionDetails, theRequest, theResource, entity, true);
			// For some reason the current version entity is not attached until after using updateEntity
			historyEntity = ((ResourceTable) readEntity(theResourceId, theRequest)).getCurrentVersionEntity();

			// Update version/lastUpdated so that interceptors see the correct version
			myJpaStorageResourceParser.updateResourceMetadata(savedEntity, theResource);
			// Populate the PID in the resource, so it is available to hooks
			addPidToResource(savedEntity, theResource);

			if (!savedEntity.isUnchangedInCurrentOperation()) {
				notifyInterceptors(theRequest, theResource, oldResource, theTransactionDetails, false);
			}
		} else {
			historyEntity = (ResourceHistoryTable) theHistoryEntity;
			if (!StringUtils.isBlank(historyEntity.getResourceType())) {
				validateIncomingResourceTypeMatchesExisting(theResource, historyEntity);
			}

			historyEntity.setDeleted(null);

			// Check if resource is the same
			ResourceEncodingEnum encoding = myStorageSettings.getResourceEncoding();
			List<String> excludeElements = new ArrayList<>(8);
			getExcludedElements(historyEntity.getResourceType(), excludeElements, theResource.getMeta());
			String encodedResourceString =
					myResourceHistoryCalculator.encodeResource(theResource, encoding, excludeElements);
			byte[] resourceBinary = ResourceHistoryCalculator.getResourceBinary(encoding, encodedResourceString);
			final boolean changed = myResourceHistoryCalculator.isResourceHistoryChanged(
					historyEntity, resourceBinary, encodedResourceString);

			historyEntity.setUpdated(theTransactionDetails.getTransactionDate());

			if (!changed && myStorageSettings.isSuppressUpdatesWithNoChange() && (historyEntity.getVersion() > 1)) {
				ourLog.debug(
						"Resource {} has not changed",
						historyEntity.getIdDt().toUnqualified().getValue());
				myJpaStorageResourceParser.updateResourceMetadata(historyEntity, theResource);
				return historyEntity;
			}

			myResourceHistoryCalculator.populateEncodedResource(
					encodedResource, encodedResourceString, resourceBinary, encoding);
		}
		/*
		 * Save the resource itself to the resourceHistoryTable
		 */
		historyEntity = myEntityManager.merge(historyEntity);
		historyEntity.setEncoding(encodedResource.getEncoding());
		historyEntity.setResource(encodedResource.getResourceBinary());
		historyEntity.setResourceTextVc(encodedResource.getResourceText());
		myResourceHistoryTableDao.save(historyEntity);

		myJpaStorageResourceParser.updateResourceMetadata(historyEntity, theResource);

		return historyEntity;
	}

	private void populateEncodedResource(
			EncodedResource encodedResource,
			String encodedResourceString,
			byte[] theResourceBinary,
			ResourceEncodingEnum theEncoding) {
		encodedResource.setResourceText(encodedResourceString);
		encodedResource.setResourceBinary(theResourceBinary);
		encodedResource.setEncoding(theEncoding);
	}

	/**
	 * TODO eventually consider refactoring this to be part of an interceptor.
	 * <p>
	 * Throws an exception if the partition of the request, and the partition of the existing entity do not match.
	 *
	 * @param theRequest the request.
	 * @param entity     the existing entity.
	 */
	private void failIfPartitionMismatch(RequestDetails theRequest, ResourceTable entity) {
		if (myPartitionSettings.isPartitioningEnabled()
				&& theRequest != null
				&& theRequest.getTenantId() != null
				&& entity.getPartitionId() != null) {
			PartitionEntity partitionEntity = myPartitionLookupSvc.getPartitionByName(theRequest.getTenantId());
			// partitionEntity should never be null
			if (partitionEntity != null
					&& !partitionEntity.getId().equals(entity.getPartitionId().getPartitionId())) {
				throw new InvalidRequestException(Msg.code(2079) + "Resource " + entity.getResourceType() + "/"
						+ entity.getId() + " is not known");
			}
		}
	}

	private void createHistoryEntry(
			RequestDetails theRequest, IBaseResource theResource, ResourceTable theEntity, EncodedResource theChanged) {
		boolean versionedTags =
				getStorageSettings().getTagStorageMode() == JpaStorageSettings.TagStorageModeEnum.VERSIONED;

		ResourceHistoryTable historyEntry = null;
		long resourceVersion = theEntity.getVersion();
		boolean reusingHistoryEntity = false;
		if (!myStorageSettings.isResourceDbHistoryEnabled() && resourceVersion > 1L) {
			/*
			 * If we're not storing history, then just pull the current history
			 * table row and update it. Note that there is always a chance that
			 * this could return null if the current resourceVersion has been expunged
			 * in which case we'll still create a new one
			 */
			historyEntry = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(
					theEntity.getResourceId(), resourceVersion - 1);
			if (historyEntry != null) {
				reusingHistoryEntity = true;
				theEntity.populateHistoryEntityVersionAndDates(historyEntry);
				if (versionedTags && theEntity.isHasTags()) {
					for (ResourceTag next : theEntity.getTags()) {
						historyEntry.addTag(next.getTag());
					}
				}
			}
		}

		/*
		 * This should basically always be null unless resource history
		 * is disabled on this server. In that case, we'll just be reusing
		 * the previous version entity.
		 */
		if (historyEntry == null) {
			historyEntry = theEntity.toHistory(versionedTags);
		}

		historyEntry.setEncoding(theChanged.getEncoding());
		historyEntry.setResource(theChanged.getResourceBinary());
		historyEntry.setResourceTextVc(theChanged.getResourceText());

		ourLog.debug("Saving history entry ID[{}] for RES_ID[{}]", historyEntry.getId(), historyEntry.getResourceId());
		myResourceHistoryTableDao.save(historyEntry);
		theEntity.setCurrentVersionEntity(historyEntry);

		// Save resource source
		String source = null;

		if (theResource != null) {
			if (myContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.R4)) {
				IBaseMetaType meta = theResource.getMeta();
				source = MetaUtil.getSource(myContext, meta);
			}
			if (myContext.getVersion().getVersion().equals(FhirVersionEnum.DSTU3)) {
				source = ((IBaseHasExtensions) theResource.getMeta())
						.getExtension().stream()
								.filter(t -> HapiExtensions.EXT_META_SOURCE.equals(t.getUrl()))
								.filter(t -> t.getValue() instanceof IPrimitiveType)
								.map(t -> ((IPrimitiveType<?>) t.getValue()).getValueAsString())
								.findFirst()
								.orElse(null);
			}
		}

		String requestId = getRequestId(theRequest, source);
		source = MetaUtil.cleanProvenanceSourceUriOrEmpty(source);

		boolean shouldStoreSource =
				myStorageSettings.getStoreMetaSourceInformation().isStoreSourceUri();
		boolean shouldStoreRequestId =
				myStorageSettings.getStoreMetaSourceInformation().isStoreRequestId();
		boolean haveSource = isNotBlank(source) && shouldStoreSource;
		boolean haveRequestId = isNotBlank(requestId) && shouldStoreRequestId;
		if (haveSource || haveRequestId) {
			ResourceHistoryProvenanceEntity provenance = null;
			if (reusingHistoryEntity) {
				/*
				 * If version history is disabled, then we may be reusing
				 * a previous history entity. If that's the case, let's try
				 * to reuse the previous provenance entity too.
				 */
				provenance = historyEntry.getProvenance();
			}
			if (provenance == null) {
				provenance = historyEntry.toProvenance();
			}
			provenance.setResourceHistoryTable(historyEntry);
			provenance.setResourceTable(theEntity);
			provenance.setPartitionId(theEntity.getPartitionId());
			if (haveRequestId) {
				String persistedRequestId = left(requestId, Constants.REQUEST_ID_LENGTH);
				provenance.setRequestId(persistedRequestId);
				historyEntry.setRequestId(persistedRequestId);
			}
			if (haveSource) {
				String persistedSource = left(source, ResourceHistoryTable.SOURCE_URI_LENGTH);
				provenance.setSourceUri(persistedSource);
				historyEntry.setSourceUri(persistedSource);
			}
			if (theResource != null) {
				MetaUtil.populateResourceSource(
						myFhirContext,
						shouldStoreSource ? source : null,
						shouldStoreRequestId ? requestId : null,
						theResource);
			}

			myEntityManager.persist(provenance);
		}
	}

	private String getRequestId(RequestDetails theRequest, String theSource) {
		if (myStorageSettings.isPreserveRequestIdInResourceBody()) {
			return StringUtils.substringAfter(theSource, "#");
		}
		return theRequest != null ? theRequest.getRequestId() : null;
	}

	private void validateIncomingResourceTypeMatchesExisting(IBaseResource theResource, BaseHasResource entity) {
		String resourceType = myContext.getResourceType(theResource);
		if (!resourceType.equals(entity.getResourceType())) {
			throw new UnprocessableEntityException(Msg.code(930) + "Existing resource ID["
					+ entity.getIdDt().toUnqualifiedVersionless() + "] is of type[" + entity.getResourceType()
					+ "] - Cannot update with [" + resourceType + "]");
		}
	}

	@Override
	public DaoMethodOutcome updateInternal(
			RequestDetails theRequestDetails,
			T theResource,
			String theMatchUrl,
			boolean thePerformIndexing,
			boolean theForceUpdateVersion,
			IBasePersistedResource theEntity,
			IIdType theResourceId,
			@Nullable IBaseResource theOldResource,
			RestOperationTypeEnum theOperationType,
			TransactionDetails theTransactionDetails) {

		ResourceTable entity = (ResourceTable) theEntity;

		// We'll update the resource ID with the correct version later but for
		// now at least set it to something useful for the interceptors
		theResource.setId(entity.getIdDt());

		// Notify IServerOperationInterceptors about pre-action call
		notifyInterceptors(theRequestDetails, theResource, theOldResource, theTransactionDetails, true);

		entity.setUpdatedByMatchUrl(theMatchUrl);

		// Perform update
		ResourceTable savedEntity = updateEntity(
				theRequestDetails,
				theResource,
				entity,
				null,
				thePerformIndexing,
				thePerformIndexing,
				theTransactionDetails,
				theForceUpdateVersion,
				thePerformIndexing);

		/*
		 * If we aren't indexing (meaning we're probably executing a sub-operation within a transaction),
		 * we'll manually increase the version. This is important because we want the updated version number
		 * to be reflected in the resource shared with interceptors
		 */
		if (!thePerformIndexing
				&& !savedEntity.isUnchangedInCurrentOperation()
				&& !ourDisableIncrementOnUpdateForUnitTest) {
			if (!theResourceId.hasVersionIdPart()) {
				theResourceId = theResourceId.withVersion(Long.toString(savedEntity.getVersion()));
			}
			incrementId(theResource, savedEntity, theResourceId);
		}

		// Update version/lastUpdated so that interceptors see the correct version
		myJpaStorageResourceParser.updateResourceMetadata(savedEntity, theResource);

		// Populate the PID in the resource so it is available to hooks
		addPidToResource(savedEntity, theResource);

		// Notify interceptors
		if (!savedEntity.isUnchangedInCurrentOperation()) {
			notifyInterceptors(theRequestDetails, theResource, theOldResource, theTransactionDetails, false);
		}

		Collection<? extends BaseTag> tagList = Collections.emptyList();
		if (entity.isHasTags()) {
			tagList = entity.getTags();
		}
		long version = entity.getVersion();
		myJpaStorageResourceParser.populateResourceMetadata(entity, false, tagList, version, theResource);

		boolean wasDeleted = false;
		if (theOldResource != null) {
			wasDeleted = theOldResource.isDeleted();
		}

		DaoMethodOutcome outcome = toMethodOutcome(
						theRequestDetails, savedEntity, theResource, theMatchUrl, theOperationType)
				.setCreated(wasDeleted);

		if (!thePerformIndexing) {
			IIdType id = getContext().getVersion().newIdType();
			id.setValue(entity.getIdDt().getValue());
			outcome.setId(id);
		}

		// Only include a task timer if we're not in a sub-request (i.e. a transaction)
		// since individual item times don't actually make much sense in the context
		// of a transaction
		StopWatch w = null;
		if (theRequestDetails != null && !theRequestDetails.isSubRequest()) {
			if (theTransactionDetails != null && !theTransactionDetails.isFhirTransaction()) {
				w = new StopWatch(theTransactionDetails.getTransactionDate());
			}
		}

		populateOperationOutcomeForUpdate(w, outcome, theMatchUrl, outcome.getOperationType());

		return outcome;
	}

	private void notifyInterceptors(
			RequestDetails theRequestDetails,
			T theResource,
			IBaseResource theOldResource,
			TransactionDetails theTransactionDetails,
			boolean isUnchanged) {
		Pointcut interceptorPointcut = Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED;

		HookParams hookParams = new HookParams()
				.add(IBaseResource.class, theOldResource)
				.add(IBaseResource.class, theResource)
				.add(RequestDetails.class, theRequestDetails)
				.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
				.add(TransactionDetails.class, theTransactionDetails);

		if (!isUnchanged) {
			hookParams.add(
					InterceptorInvocationTimingEnum.class,
					theTransactionDetails.getInvocationTiming(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED));
			interceptorPointcut = Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED;
		}

		doCallHooks(theTransactionDetails, theRequestDetails, interceptorPointcut, hookParams);
	}

	protected void addPidToResource(IResourceLookup<JpaPid> theEntity, IBaseResource theResource) {
		if (theResource instanceof IAnyResource) {
			IDao.RESOURCE_PID.put(theResource, theEntity.getPersistentId().getId());
		} else if (theResource instanceof IResource) {
			IDao.RESOURCE_PID.put(theResource, theEntity.getPersistentId().getId());
		}
	}

	private void validateChildReferenceTargetTypes(IBase theElement, String thePath) {
		if (theElement == null) {
			return;
		}
		BaseRuntimeElementDefinition<?> def = myContext.getElementDefinition(theElement.getClass());
		if (!(def instanceof BaseRuntimeElementCompositeDefinition)) {
			return;
		}

		BaseRuntimeElementCompositeDefinition<?> cdef = (BaseRuntimeElementCompositeDefinition<?>) def;
		for (BaseRuntimeChildDefinition nextChildDef : cdef.getChildren()) {

			List<IBase> values = nextChildDef.getAccessor().getValues(theElement);
			if (values == null || values.isEmpty()) {
				continue;
			}

			String newPath = thePath + "." + nextChildDef.getElementName();

			for (IBase nextChild : values) {
				validateChildReferenceTargetTypes(nextChild, newPath);
			}

			if (nextChildDef instanceof RuntimeChildResourceDefinition) {
				RuntimeChildResourceDefinition nextChildDefRes = (RuntimeChildResourceDefinition) nextChildDef;
				Set<String> validTypes = new HashSet<>();
				boolean allowAny = false;
				for (Class<? extends IBaseResource> nextValidType : nextChildDefRes.getResourceTypes()) {
					if (nextValidType.isInterface()) {
						allowAny = true;
						break;
					}
					validTypes.add(getContext().getResourceType(nextValidType));
				}

				if (allowAny) {
					continue;
				}

				if (getStorageSettings().isEnforceReferenceTargetTypes()) {
					for (IBase nextChild : values) {
						IBaseReference nextRef = (IBaseReference) nextChild;
						IIdType referencedId = nextRef.getReferenceElement();
						if (!isBlank(referencedId.getResourceType())) {
							if (!isLogicalReference(referencedId)) {
								if (!referencedId.getValue().contains("?")) {
									if (!validTypes.contains(referencedId.getResourceType())) {
										throw new UnprocessableEntityException(Msg.code(931)
												+ "Invalid reference found at path '" + newPath + "'. Resource type '"
												+ referencedId.getResourceType() + "' is not valid for this path");
									}
								}
							}
						}
					}
				}
			}
		}
	}

	protected void validateMetaCount(int theMetaCount) {
		if (myStorageSettings.getResourceMetaCountHardLimit() != null) {
			if (theMetaCount > myStorageSettings.getResourceMetaCountHardLimit()) {
				throw new UnprocessableEntityException(Msg.code(932) + "Resource contains " + theMetaCount
						+ " meta entries (tag/profile/security label), maximum is "
						+ myStorageSettings.getResourceMetaCountHardLimit());
			}
		}
	}

	/**
	 * This method is invoked immediately before storing a new resource, or an update to an existing resource to allow the DAO to ensure that it is valid for persistence. By default, checks for the
	 * "subsetted" tag and rejects resources which have it. Subclasses should call the superclass implementation to preserve this check.
	 *
	 * @param theResource     The resource that is about to be persisted
	 * @param theEntityToSave TODO
	 */
	protected void validateResourceForStorage(T theResource, ResourceTable theEntityToSave) {
		Object tag = null;

		int totalMetaCount = 0;

		if (theResource instanceof IResource) {
			IResource res = (IResource) theResource;
			TagList tagList = ResourceMetadataKeyEnum.TAG_LIST.get(res);
			if (tagList != null) {
				tag = tagList.getTag(Constants.TAG_SUBSETTED_SYSTEM_DSTU3, Constants.TAG_SUBSETTED_CODE);
				totalMetaCount += tagList.size();
			}
			List<IdDt> profileList = ResourceMetadataKeyEnum.PROFILES.get(res);
			if (profileList != null) {
				totalMetaCount += profileList.size();
			}
		} else {
			IAnyResource res = (IAnyResource) theResource;
			tag = res.getMeta().getTag(Constants.TAG_SUBSETTED_SYSTEM_DSTU3, Constants.TAG_SUBSETTED_CODE);
			totalMetaCount += res.getMeta().getTag().size();
			totalMetaCount += res.getMeta().getProfile().size();
			totalMetaCount += res.getMeta().getSecurity().size();
		}

		if (tag != null) {
			throw new UnprocessableEntityException(
					Msg.code(933)
							+ "Resource contains the 'subsetted' tag, and must not be stored as it may contain a subset of available data");
		}

		if (getStorageSettings().isEnforceReferenceTargetTypes()) {
			String resName = getContext().getResourceType(theResource);
			validateChildReferenceTargetTypes(theResource, resName);
		}

		validateMetaCount(totalMetaCount);
	}

	@PostConstruct
	public void start() {
		// nothing yet
	}

	@VisibleForTesting
	public void setStorageSettingsForUnitTest(JpaStorageSettings theStorageSettings) {
		myStorageSettings = theStorageSettings;
	}

	public void populateFullTextFields(
			final FhirContext theContext,
			final IBaseResource theResource,
			ResourceTable theEntity,
			ResourceIndexedSearchParams theNewParams) {
		if (theEntity.getDeleted() != null) {
			theEntity.setNarrativeText(null);
			theEntity.setContentText(null);
		} else {
			theEntity.setNarrativeText(parseNarrativeTextIntoWords(theResource));
			theEntity.setContentText(parseContentTextIntoWords(theContext, theResource));
			if (myStorageSettings.isAdvancedHSearchIndexing()) {
				ExtendedHSearchIndexData hSearchIndexData =
						myFulltextSearchSvc.extractLuceneIndexData(theResource, theNewParams);
				theEntity.setLuceneIndexData(hSearchIndexData);
			}
		}
	}

	@VisibleForTesting
	public void setPartitionSettingsForUnitTest(PartitionSettings thePartitionSettings) {
		myPartitionSettings = thePartitionSettings;
	}

	/**
	 * Do not call this method outside of unit tests
	 */
	@VisibleForTesting
	public void setJpaStorageResourceParserForUnitTest(IJpaStorageResourceParser theJpaStorageResourceParser) {
		myJpaStorageResourceParser = theJpaStorageResourceParser;
	}

	private class AddTagDefinitionToCacheAfterCommitSynchronization implements TransactionSynchronization {

		private final TagDefinition myTagDefinition;
		private final MemoryCacheService.TagDefinitionCacheKey myKey;

		public AddTagDefinitionToCacheAfterCommitSynchronization(
				MemoryCacheService.TagDefinitionCacheKey theKey, TagDefinition theTagDefinition) {
			myTagDefinition = theTagDefinition;
			myKey = theKey;
		}

		@Override
		public void afterCommit() {
			myMemoryCacheService.put(MemoryCacheService.CacheEnum.TAG_DEFINITION, myKey, myTagDefinition);
		}
	}

	@Nonnull
	public static MemoryCacheService.TagDefinitionCacheKey toTagDefinitionMemoryCacheKey(
			TagTypeEnum theTagType, String theScheme, String theTerm, String theVersion, Boolean theUserSelected) {
		return new MemoryCacheService.TagDefinitionCacheKey(
				theTagType, theScheme, theTerm, theVersion, theUserSelected);
	}

	@SuppressWarnings("unchecked")
	public static String parseContentTextIntoWords(FhirContext theContext, IBaseResource theResource) {

		Class<IPrimitiveType<String>> stringType = (Class<IPrimitiveType<String>>)
				theContext.getElementDefinition("string").getImplementingClass();

		StringBuilder retVal = new StringBuilder();
		List<IPrimitiveType<String>> childElements =
				theContext.newTerser().getAllPopulatedChildElementsOfType(theResource, stringType);
		for (IPrimitiveType<String> nextType : childElements) {
			if (stringType.equals(nextType.getClass())) {
				String nextValue = nextType.getValueAsString();
				if (isNotBlank(nextValue)) {
					retVal.append(nextValue.replace("\n", " ").replace("\r", " "));
					retVal.append("\n");
				}
			}
		}
		return retVal.toString();
	}

	public static String decodeResource(byte[] theResourceBytes, ResourceEncodingEnum theResourceEncoding) {
		String resourceText = null;
		switch (theResourceEncoding) {
			case JSON:
				resourceText = new String(theResourceBytes, Charsets.UTF_8);
				break;
			case JSONC:
				resourceText = GZipUtil.decompress(theResourceBytes);
				break;
			case DEL:
			case ESR:
				break;
		}
		return resourceText;
	}

	private static String parseNarrativeTextIntoWords(IBaseResource theResource) {

		StringBuilder b = new StringBuilder();
		if (theResource instanceof IResource) {
			IResource resource = (IResource) theResource;
			List<XMLEvent> xmlEvents = XmlUtil.parse(resource.getText().getDiv().getValue());
			if (xmlEvents != null) {
				for (XMLEvent next : xmlEvents) {
					if (next.isCharacters()) {
						Characters characters = next.asCharacters();
						b.append(characters.getData()).append(" ");
					}
				}
			}
		} else if (theResource instanceof IDomainResource) {
			IDomainResource resource = (IDomainResource) theResource;
			try {
				String divAsString = resource.getText().getDivAsString();
				List<XMLEvent> xmlEvents = XmlUtil.parse(divAsString);
				if (xmlEvents != null) {
					for (XMLEvent next : xmlEvents) {
						if (next.isCharacters()) {
							Characters characters = next.asCharacters();
							b.append(characters.getData()).append(" ");
						}
					}
				}
			} catch (Exception e) {
				throw new DataFormatException(Msg.code(934) + "Unable to convert DIV to string", e);
			}
		}
		return b.toString();
	}

	@VisibleForTesting
	public static void setDisableIncrementOnUpdateForUnitTest(boolean theDisableIncrementOnUpdateForUnitTest) {
		ourDisableIncrementOnUpdateForUnitTest = theDisableIncrementOnUpdateForUnitTest;
	}

	/**
	 * Do not call this method outside of unit tests
	 */
	@VisibleForTesting
	public static void setValidationDisabledForUnitTest(boolean theValidationDisabledForUnitTest) {
		ourValidationDisabledForUnitTest = theValidationDisabledForUnitTest;
	}

	private enum CreateOrUpdateByMatch {
		CREATE,
		UPDATE
	}
}
