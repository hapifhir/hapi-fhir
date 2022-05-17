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
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.api.dao.IJpaDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.dao.data.IForcedIdDao;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceProvenanceDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTagDao;
import ca.uhn.fhir.jpa.dao.expunge.ExpungeService;
import ca.uhn.fhir.jpa.dao.index.DaoSearchParamSynchronizer;
import ca.uhn.fhir.jpa.dao.index.SearchParamWithInlineReferencesExtractor;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.delete.DeleteConflictService;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.entity.ResourceSearchView;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.entity.SearchTypeEnum;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.entity.BaseHasResource;
import ca.uhn.fhir.jpa.model.entity.BaseTag;
import ca.uhn.fhir.jpa.model.entity.IBaseResourceEntity;
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.jpa.model.entity.ResourceEncodingEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryProvenanceEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTag;
import ca.uhn.fhir.jpa.model.entity.TagDefinition;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.model.search.ExtendedLuceneIndexData;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.partition.RequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProviderFactory;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.searchparam.extractor.LogicalReferenceHelper;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryResourceMatcher;
import ca.uhn.fhir.jpa.sp.ISearchParamPresenceSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.util.AddRemoveCount;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.valueset.BundleEntryTransactionMethodEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.LenientErrorHandler;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.InterceptorInvocationTimingEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.rest.server.util.ResourceSearchParams;
import ca.uhn.fhir.util.CoverageIgnore;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.MetaUtil;
import ca.uhn.fhir.util.XmlUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
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
import org.hl7.fhir.r4.model.Bundle.HTTPVerb;
import org.jetbrains.annotations.NotNull;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.left;
import static org.apache.commons.lang3.StringUtils.trim;

import static ca.uhn.fhir.jpa.model.util.JpaConstants.ALL_PARTITIONS_NAME;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

@SuppressWarnings("WeakerAccess")
@Repository
public abstract class BaseHapiFhirDao<T extends IBaseResource> extends BaseStorageDao implements IDao, IJpaDao<T>, ApplicationContextAware {

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
	protected IIdHelperService myIdHelperService;
	@Autowired
	protected IForcedIdDao myForcedIdDao;
	@Autowired
	protected IResourceProvenanceDao myResourceProvenanceDao;
	@Autowired
	protected ISearchCoordinatorSvc mySearchCoordinatorSvc;
	@Autowired
	protected ITermReadSvc myTerminologySvc;
	@Autowired
	protected IResourceHistoryTableDao myResourceHistoryTableDao;
	@Autowired
	protected IResourceTableDao myResourceTableDao;
	@Autowired
	protected IResourceTagDao myResourceTagDao;
	@Autowired
	protected DeleteConflictService myDeleteConflictService;
	@Autowired
	protected IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	protected DaoRegistry myDaoRegistry;
	@Autowired
	protected InMemoryResourceMatcher myInMemoryResourceMatcher;
	@Autowired
	ExpungeService myExpungeService;
	@Autowired
	private HistoryBuilderFactory myHistoryBuilderFactory;
	@Autowired
	private DaoConfig myConfig;
	@Autowired
	private PlatformTransactionManager myPlatformTransactionManager;
	@Autowired
	private ISearchCacheSvc mySearchCacheSvc;
	@Autowired
	private ISearchParamPresenceSvc mySearchParamPresenceSvc;
	@Autowired
	private SearchParamWithInlineReferencesExtractor mySearchParamWithInlineReferencesExtractor;
	@Autowired
	private DaoSearchParamSynchronizer myDaoSearchParamSynchronizer;
	@Autowired
	private SearchBuilderFactory mySearchBuilderFactory;
	private FhirContext myContext;
	private ApplicationContext myApplicationContext;
	@Autowired
	private PartitionSettings myPartitionSettings;
	@Autowired
	private RequestPartitionHelperSvc myRequestPartitionHelperSvc;
	@Autowired
	private PersistedJpaBundleProviderFactory myPersistedJpaBundleProviderFactory;
	@Autowired
	private IPartitionLookupSvc myPartitionLookupSvc;
	@Autowired
	private MemoryCacheService myMemoryCacheService;
	@Autowired(required = false)
	private IFulltextSearchSvc myFulltextSearchSvc;

	@Autowired
	private PlatformTransactionManager myTransactionManager;

	@VisibleForTesting
	public void setSearchParamPresenceSvc(ISearchParamPresenceSvc theSearchParamPresenceSvc) {
		mySearchParamPresenceSvc = theSearchParamPresenceSvc;
	}

	@Override
	protected IInterceptorBroadcaster getInterceptorBroadcaster() {
		return myInterceptorBroadcaster;
	}

	protected ApplicationContext getApplicationContext() {
		return myApplicationContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext theApplicationContext) throws BeansException {
		/*
		 * We do a null check here because Smile's module system tries to
		 * initialize the application context twice if two modules depend on
		 * the persistence module. The second time sets the dependency's appctx.
		 */
		if (myApplicationContext == null) {
			myApplicationContext = theApplicationContext;
		}
	}

	private void extractTagsHapi(TransactionDetails theTransactionDetails, IResource theResource, ResourceTable theEntity, Set<ResourceTag> allDefs) {
		TagList tagList = ResourceMetadataKeyEnum.TAG_LIST.get(theResource);
		if (tagList != null) {
			for (Tag next : tagList) {
				TagDefinition def = getTagOrNull(theTransactionDetails, TagTypeEnum.TAG, next.getScheme(), next.getTerm(), next.getLabel());
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
				TagDefinition def = getTagOrNull(theTransactionDetails, TagTypeEnum.SECURITY_LABEL, next.getSystemElement().getValue(), next.getCodeElement().getValue(), next.getDisplayElement().getValue());
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
				TagDefinition def = getTagOrNull(theTransactionDetails, TagTypeEnum.PROFILE, NS_JPA_PROFILE, next.getValue(), null);
				if (def != null) {
					ResourceTag tag = theEntity.addTag(def);
					allDefs.add(tag);
					theEntity.setHasTags(true);
				}
			}
		}
	}

	private void extractTagsRi(TransactionDetails theTransactionDetails, IAnyResource theResource, ResourceTable theEntity, Set<ResourceTag> theAllTags) {
		List<? extends IBaseCoding> tagList = theResource.getMeta().getTag();
		if (tagList != null) {
			for (IBaseCoding next : tagList) {
				TagDefinition def = getTagOrNull(theTransactionDetails, TagTypeEnum.TAG, next.getSystem(), next.getCode(), next.getDisplay());
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
				TagDefinition def = getTagOrNull(theTransactionDetails, TagTypeEnum.SECURITY_LABEL, next.getSystem(), next.getCode(), next.getDisplay());
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
				TagDefinition def = getTagOrNull(theTransactionDetails, TagTypeEnum.PROFILE, NS_JPA_PROFILE, next.getValue(), null);
				if (def != null) {
					ResourceTag tag = theEntity.addTag(def);
					theAllTags.add(tag);
					theEntity.setHasTags(true);
				}
			}
		}

	}

	private Set<ResourceTag> getAllTagDefinitions(ResourceTable theEntity) {
		HashSet<ResourceTag> retVal = Sets.newHashSet();
		if (theEntity.isHasTags()) {
			for (ResourceTag next : theEntity.getTags()) {
				retVal.add(next);
			}
		}
		return retVal;
	}

	@Override
	public DaoConfig getConfig() {
		return myConfig;
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

	public FhirContext getContext(FhirVersionEnum theVersion) {
		Validate.notNull(theVersion, "theVersion must not be null");
		if (theVersion == myFhirContext.getVersion().getVersion()) {
			return myFhirContext;
		}
		return FhirContext.forCached(theVersion);
	}

	/**
	 * <code>null</code> will only be returned if the scheme and tag are both blank
	 */
	protected TagDefinition getTagOrNull(TransactionDetails theTransactionDetails, TagTypeEnum theTagType, String theScheme, String theTerm, String theLabel) {
		if (isBlank(theScheme) && isBlank(theTerm) && isBlank(theLabel)) {
			return null;
		}

		MemoryCacheService.TagDefinitionCacheKey key = toTagDefinitionMemoryCacheKey(theTagType, theScheme, theTerm);

		TagDefinition retVal = myMemoryCacheService.getIfPresent(MemoryCacheService.CacheEnum.TAG_DEFINITION, key);

		if (retVal == null) {
			HashMap<MemoryCacheService.TagDefinitionCacheKey, TagDefinition> resolvedTagDefinitions = theTransactionDetails.getOrCreateUserData(HapiTransactionService.XACT_USERDATA_KEY_RESOLVED_TAG_DEFINITIONS, () -> new HashMap<>());
			retVal = resolvedTagDefinitions.get(key);

			if (retVal == null) {
				// actual DB hit(s) happen here
				retVal = getOrCreateTag(theTagType, theScheme, theTerm, theLabel);

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
	private TagDefinition getOrCreateTag(TagTypeEnum theTagType, String theScheme, String theTerm, String theLabel) {
		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<TagDefinition> cq = builder.createQuery(TagDefinition.class);
		Root<TagDefinition> from = cq.from(TagDefinition.class);

		if (isNotBlank(theScheme)) {
			cq.where(
				builder.and(
					builder.equal(from.get("myTagType"), theTagType),
					builder.equal(from.get("mySystem"), theScheme),
					builder.equal(from.get("myCode"), theTerm)));
		} else {
			cq.where(
				builder.and(
					builder.equal(from.get("myTagType"), theTagType),
					builder.isNull(from.get("mySystem")),
					builder.equal(from.get("myCode"), theTerm)));
		}

		TypedQuery<TagDefinition> q = myEntityManager.createQuery(cq);

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
									+ "but could be useful information in the result of an actual failure."
							);
							throwables.add(ex);
						}

						return tag;
					}
				});
			} catch (Exception ex) {
				// transaction template can fail if connections to db are exhausted
				// and/or timeout
				ourLog.warn("Transaction failed with: "
					+ ex.getMessage() + ". "
					+ "Transaction will rollback and be reattempted."
				);
				retVal = null;
			}
			count++;
		} while (retVal == null && count < TOTAL_TAG_READ_ATTEMPTS);

		if (retVal == null) {
			// if tag is still null,
			// something bad must be happening
			// - throw
			String msg = throwables.stream()
				.map(Throwable::getMessage)
				.collect(Collectors.joining(", "));
			throw new InternalErrorException(
				Msg.code(2023)
					+ "Tag get/create failed after "
					+ TOTAL_TAG_READ_ATTEMPTS
					+ " attempts with error(s): "
					+ msg
			);
		}

		return retVal;
	}

	protected IBundleProvider history(RequestDetails theRequest, String theResourceType, Long theResourcePid, Date theRangeStartInclusive, Date theRangeEndInclusive, Integer theOffset) {

		String resourceName = defaultIfBlank(theResourceType, null);

		Search search = new Search();
		search.setOffset(theOffset);
		search.setDeleted(false);
		search.setCreated(new Date());
		search.setLastUpdated(theRangeStartInclusive, theRangeEndInclusive);
		search.setUuid(UUID.randomUUID().toString());
		search.setResourceType(resourceName);
		search.setResourceId(theResourcePid);
		search.setSearchType(SearchTypeEnum.HISTORY);
		search.setStatus(SearchStatusEnum.FINISHED);

		return myPersistedJpaBundleProviderFactory.newInstance(theRequest, search);
	}

	void incrementId(T theResource, ResourceTable theSavedEntity, IIdType theResourceId) {
		String newVersion;
		long newVersionLong;
		if (theResourceId == null || theResourceId.getVersionIdPart() == null) {
			newVersion = "1";
			newVersionLong = 1;
		} else {
			newVersionLong = theResourceId.getVersionIdPartAsLong() + 1;
			newVersion = Long.toString(newVersionLong);
		}

		assert theResourceId != null;
		IIdType newId = theResourceId.withVersion(newVersion);
		theResource.getIdElement().setValue(newId.getValue());
		theSavedEntity.setVersion(newVersionLong);
	}

	public boolean isLogicalReference(IIdType theId) {
		return LogicalReferenceHelper.isLogicalReference(myConfig.getModelConfig(), theId);
	}

	/**
	 * Returns true if the resource has changed (either the contents or the tags)
	 */
	protected EncodedResource populateResourceIntoEntity(TransactionDetails theTransactionDetails, RequestDetails theRequest, IBaseResource theResource, ResourceTable theEntity, boolean thePerformIndexing) {
		if (theEntity.getResourceType() == null) {
			theEntity.setResourceType(toResourceName(theResource));
		}

		byte[] resourceBinary;
		String resourceText;
		ResourceEncodingEnum encoding;
		boolean changed = false;

		if (theEntity.getDeleted() == null) {

			if (thePerformIndexing) {

				encoding = myConfig.getResourceEncoding();

				String resourceType = theEntity.getResourceType();

				List<String> excludeElements = new ArrayList<>(8);
				excludeElements.add("id");

				IBaseMetaType meta = theResource.getMeta();
				boolean hasExtensions = false;
				IBaseExtension<?, ?> sourceExtension = null;
				if (meta instanceof IBaseHasExtensions) {
					List<? extends IBaseExtension<?, ?>> extensions = ((IBaseHasExtensions) meta).getExtension();
					if (!extensions.isEmpty()) {
						hasExtensions = true;

						/*
						 * FHIR DSTU3 did not have the Resource.meta.source field, so we use a
						 * custom HAPI FHIR extension in Resource.meta to store that field. However,
						 * we put the value for that field in a separate table so we don't want to serialize
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

					}
				}

				boolean inlineTagMode = getConfig().getTagStorageMode() == DaoConfig.TagStorageModeEnum.INLINE;
				if (hasExtensions || inlineTagMode) {
					if (!inlineTagMode) {
						excludeElements.add(resourceType + ".meta.profile");
						excludeElements.add(resourceType + ".meta.tag");
						excludeElements.add(resourceType + ".meta.security");
					}
					excludeElements.add(resourceType + ".meta.versionId");
					excludeElements.add(resourceType + ".meta.lastUpdated");
					excludeElements.add(resourceType + ".meta.source");
				} else {
					/*
					 * If there are no extensions in the meta element, we can just exclude the
					 * whole meta element, which avoids adding an empty "meta":{}
					 * from showing up in the serialized JSON.
					 */
					excludeElements.add(resourceType + ".meta");
				}

				theEntity.setFhirVersion(myContext.getVersion().getVersion());

				HashFunction sha256 = Hashing.sha256();
				HashCode hashCode;
				String encodedResource = encodeResource(theResource, encoding, excludeElements, myContext);
				if (getConfig().getInlineResourceTextBelowSize() > 0 && encodedResource.length() < getConfig().getInlineResourceTextBelowSize()) {
					resourceText = encodedResource;
					resourceBinary = null;
					encoding = ResourceEncodingEnum.JSON;
					hashCode = sha256.hashUnencodedChars(encodedResource);
				} else {
					resourceText = null;
					switch (encoding) {
						case JSON:
							resourceBinary = encodedResource.getBytes(Charsets.UTF_8);
							break;
						case JSONC:
							resourceBinary = GZipUtil.compress(encodedResource);
							break;
						default:
						case DEL:
							resourceBinary = new byte[0];
							break;
					}
					hashCode = sha256.hashBytes(resourceBinary);
				}

				String hashSha256 = hashCode.toString();
				if (hashSha256.equals(theEntity.getHashSha256()) == false) {
					changed = true;
				}
				theEntity.setHashSha256(hashSha256);


				if (sourceExtension != null) {
					IBaseExtension<?, ?> newSourceExtension = ((IBaseHasExtensions) meta).addExtension();
					newSourceExtension.setUrl(sourceExtension.getUrl());
					newSourceExtension.setValue(sourceExtension.getValue());
				}

			} else {

				encoding = null;
				resourceBinary = null;
				resourceText = null;

			}

			boolean skipUpdatingTags = myConfig.isMassIngestionMode() && theEntity.isHasTags();
			skipUpdatingTags |= myConfig.getTagStorageMode() == DaoConfig.TagStorageModeEnum.INLINE;

			if (!skipUpdatingTags) {
				changed |= updateTags(theTransactionDetails, theRequest, theResource, theEntity);
			}

		} else {

			theEntity.setHashSha256(null);
			resourceBinary = null;
			resourceText = null;
			encoding = ResourceEncodingEnum.DEL;

		}

		if (thePerformIndexing && !changed) {
			if (theEntity.getId() == null) {
				changed = true;
			} else if (myConfig.isMassIngestionMode()) {

				// Don't check existing - We'll rely on the SHA256 hash only

			} else if (theEntity.getVersion() == 1L && theEntity.getCurrentVersionEntity() == null) {

				// No previous version if this is the first version

			} else {
				ResourceHistoryTable currentHistoryVersion = theEntity.getCurrentVersionEntity();
				if (currentHistoryVersion == null) {
					currentHistoryVersion = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(theEntity.getId(), theEntity.getVersion());
				}
				if (currentHistoryVersion == null || !currentHistoryVersion.hasResource()) {
					changed = true;
				} else {
					changed = !Arrays.equals(currentHistoryVersion.getResource(), resourceBinary);
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

	private boolean updateTags(TransactionDetails theTransactionDetails, RequestDetails theRequest, IBaseResource theResource, ResourceTable theEntity) {
		Set<ResourceTag> allDefs = new HashSet<>();
		Set<ResourceTag> allTagsOld = getAllTagDefinitions(theEntity);

		if (theResource instanceof IResource) {
			extractTagsHapi(theTransactionDetails, (IResource) theResource, theEntity, allDefs);
		} else {
			extractTagsRi(theTransactionDetails, (IAnyResource) theResource, theEntity, allDefs);
		}

		RuntimeResourceDefinition def = myContext.getResourceDefinition(theResource);
		if (def.isStandardType() == false) {
			String profile = def.getResourceProfile("");
			if (isNotBlank(profile)) {
				TagDefinition profileDef = getTagOrNull(theTransactionDetails, TagTypeEnum.PROFILE, NS_JPA_PROFILE, profile, null);

				ResourceTag tag = theEntity.addTag(profileDef);
				allDefs.add(tag);
				theEntity.setHasTags(true);
			}
		}

		Set<ResourceTag> allTagsNew = getAllTagDefinitions(theEntity);
		Set<TagDefinition> allDefsPresent = new HashSet<>();
		allTagsNew.forEach(tag -> {

			// Don't keep duplicate tags
			if (!allDefsPresent.add(tag.getTag())) {
				theEntity.getTags().remove(tag);
			}

			// Drop any tags that have been removed
			if (!allDefs.contains(tag)) {
				if (shouldDroppedTagBeRemovedOnUpdate(theRequest, tag)) {
					theEntity.getTags().remove(tag);
				}
			}

		});

		theEntity.setHasTags(!allTagsNew.isEmpty());
		return !allTagsOld.equals(allTagsNew);
	}

	@SuppressWarnings("unchecked")
	private <R extends IBaseResource> R populateResourceMetadataHapi(Class<R> theResourceType, IBaseResourceEntity theEntity, @Nullable Collection<? extends BaseTag> theTagList, boolean theForHistoryOperation, IResource res, Long theVersion) {
		R retVal = (R) res;
		if (theEntity.getDeleted() != null) {
			res = (IResource) myContext.getResourceDefinition(theResourceType).newInstance();
			retVal = (R) res;
			ResourceMetadataKeyEnum.DELETED_AT.put(res, new InstantDt(theEntity.getDeleted()));
			if (theForHistoryOperation) {
				ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.put(res, BundleEntryTransactionMethodEnum.DELETE);
			}
		} else if (theForHistoryOperation) {
			/*
			 * If the create and update times match, this was when the resource was created so we should mark it as a POST. Otherwise, it's a PUT.
			 */
			Date published = theEntity.getPublished().getValue();
			Date updated = theEntity.getUpdated().getValue();
			if (published.equals(updated)) {
				ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.put(res, BundleEntryTransactionMethodEnum.POST);
			} else {
				ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.put(res, BundleEntryTransactionMethodEnum.PUT);
			}
		}

		res.setId(theEntity.getIdDt().withVersion(theVersion.toString()));

		ResourceMetadataKeyEnum.VERSION.put(res, Long.toString(theEntity.getVersion()));
		ResourceMetadataKeyEnum.PUBLISHED.put(res, theEntity.getPublished());
		ResourceMetadataKeyEnum.UPDATED.put(res, theEntity.getUpdated());
		IDao.RESOURCE_PID.put(res, theEntity.getResourceId());

		if (theTagList != null) {
			if (theEntity.isHasTags()) {
				TagList tagList = new TagList();
				List<IBaseCoding> securityLabels = new ArrayList<>();
				List<IdDt> profiles = new ArrayList<>();
				for (BaseTag next : theTagList) {
					switch (next.getTag().getTagType()) {
						case PROFILE:
							profiles.add(new IdDt(next.getTag().getCode()));
							break;
						case SECURITY_LABEL:
							IBaseCoding secLabel = (IBaseCoding) myContext.getVersion().newCodingDt();
							secLabel.setSystem(next.getTag().getSystem());
							secLabel.setCode(next.getTag().getCode());
							secLabel.setDisplay(next.getTag().getDisplay());
							securityLabels.add(secLabel);
							break;
						case TAG:
							tagList.add(new Tag(next.getTag().getSystem(), next.getTag().getCode(), next.getTag().getDisplay()));
							break;
					}
				}
				if (tagList.size() > 0) {
					ResourceMetadataKeyEnum.TAG_LIST.put(res, tagList);
				}
				if (securityLabels.size() > 0) {
					ResourceMetadataKeyEnum.SECURITY_LABELS.put(res, toBaseCodingList(securityLabels));
				}
				if (profiles.size() > 0) {
					ResourceMetadataKeyEnum.PROFILES.put(res, profiles);
				}
			}
		}

		return retVal;
	}

	@SuppressWarnings("unchecked")
	private <R extends IBaseResource> R populateResourceMetadataRi(Class<R> theResourceType, IBaseResourceEntity theEntity, @Nullable Collection<? extends BaseTag> theTagList, boolean theForHistoryOperation, IAnyResource res, Long theVersion) {
		R retVal = (R) res;
		if (theEntity.getDeleted() != null) {
			res = (IAnyResource) myContext.getResourceDefinition(theResourceType).newInstance();
			retVal = (R) res;
			ResourceMetadataKeyEnum.DELETED_AT.put(res, new InstantDt(theEntity.getDeleted()));
			if (theForHistoryOperation) {
				ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.put(res, HTTPVerb.DELETE.toCode());
			}
		} else if (theForHistoryOperation) {
			/*
			 * If the create and update times match, this was when the resource was created so we should mark it as a POST. Otherwise, it's a PUT.
			 */
			Date published = theEntity.getPublished().getValue();
			Date updated = theEntity.getUpdated().getValue();
			if (published.equals(updated)) {
				ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.put(res, HTTPVerb.POST.toCode());
			} else {
				ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.put(res, HTTPVerb.PUT.toCode());
			}
		}

		res.getMeta().setLastUpdated(null);
		res.getMeta().setVersionId(null);

		updateResourceMetadata(theEntity, res);
		res.setId(res.getIdElement().withVersion(theVersion.toString()));

		res.getMeta().setLastUpdated(theEntity.getUpdatedDate());
		IDao.RESOURCE_PID.put(res, theEntity.getResourceId());

		if (theTagList != null) {
			res.getMeta().getTag().clear();
			res.getMeta().getProfile().clear();
			res.getMeta().getSecurity().clear();
			for (BaseTag next : theTagList) {
				switch (next.getTag().getTagType()) {
					case PROFILE:
						res.getMeta().addProfile(next.getTag().getCode());
						break;
					case SECURITY_LABEL:
						IBaseCoding sec = res.getMeta().addSecurity();
						sec.setSystem(next.getTag().getSystem());
						sec.setCode(next.getTag().getCode());
						sec.setDisplay(next.getTag().getDisplay());
						break;
					case TAG:
						IBaseCoding tag = res.getMeta().addTag();
						tag.setSystem(next.getTag().getSystem());
						tag.setCode(next.getTag().getCode());
						tag.setDisplay(next.getTag().getDisplay());
						break;
				}
			}
		}

		return retVal;
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
	 * @param theEntity   The entity being updated (Do not modify the entity! Undefined behaviour will occur!)
	 * @param theResource The resource being persisted
	 */
	protected void postPersist(ResourceTable theEntity, T theResource) {
		// nothing
	}

	/**
	 * Subclasses may override to provide behaviour. Called when a pre-existing resource has been updated in the database
	 *
	 * @param theEntity   The resource
	 * @param theResource The resource being persisted
	 */
	protected void postUpdate(ResourceTable theEntity, T theResource) {
		// nothing
	}

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
	 * See <a href="http://hl7.org/fhir/resource.html#tag-updates">Updates to Tags, Profiles, and Security Labels</a> for a description of the logic that the default behaviour folows.
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

	@Override
	public IBaseResource toResource(BaseHasResource theEntity, boolean theForHistoryOperation) {
		RuntimeResourceDefinition type = myContext.getResourceDefinition(theEntity.getResourceType());
		Class<? extends IBaseResource> resourceType = type.getImplementingClass();
		return toResource(resourceType, theEntity, null, theForHistoryOperation);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R extends IBaseResource> R toResource(Class<R> theResourceType, IBaseResourceEntity theEntity, Collection<ResourceTag> theTagList, boolean theForHistoryOperation) {

		// 1. get resource, it's encoding and the tags if any
		byte[] resourceBytes;
		String resourceText;
		ResourceEncodingEnum resourceEncoding;
		@Nullable
		Collection<? extends BaseTag> tagList = Collections.emptyList();
		long version;
		String provenanceSourceUri = null;
		String provenanceRequestId = null;

		if (theEntity instanceof ResourceHistoryTable) {
			ResourceHistoryTable history = (ResourceHistoryTable) theEntity;
			resourceBytes = history.getResource();
			resourceText = history.getResourceTextVc();
			resourceEncoding = history.getEncoding();
			switch (getConfig().getTagStorageMode()) {
				case VERSIONED:
				default:
					if (history.isHasTags()) {
						tagList = history.getTags();
					}
					break;
				case NON_VERSIONED:
					if (history.getResourceTable().isHasTags()) {
						tagList = history.getResourceTable().getTags();
					}
					break;
				case INLINE:
					tagList = null;
			}
			version = history.getVersion();
			if (history.getProvenance() != null) {
				provenanceRequestId = history.getProvenance().getRequestId();
				provenanceSourceUri = history.getProvenance().getSourceUri();
			}
		} else if (theEntity instanceof ResourceTable) {
			ResourceTable resource = (ResourceTable) theEntity;
			ResourceHistoryTable history;
			if (resource.getCurrentVersionEntity() != null) {
				history = resource.getCurrentVersionEntity();
			} else {
				version = theEntity.getVersion();
				history = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(theEntity.getId(), version);
				((ResourceTable) theEntity).setCurrentVersionEntity(history);

				while (history == null) {
					if (version > 1L) {
						version--;
						history = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(theEntity.getId(), version);
					} else {
						return null;
					}
				}
			}

			resourceBytes = history.getResource();
			resourceEncoding = history.getEncoding();
			resourceText = history.getResourceTextVc();
			switch (getConfig().getTagStorageMode()) {
				case VERSIONED:
				case NON_VERSIONED:
					if (resource.isHasTags()) {
						tagList = resource.getTags();
					} else {
						tagList = Collections.emptyList();
					}
					break;
				case INLINE:
					tagList = null;
					break;
			}
			version = history.getVersion();
			if (history.getProvenance() != null) {
				provenanceRequestId = history.getProvenance().getRequestId();
				provenanceSourceUri = history.getProvenance().getSourceUri();
			}
		} else if (theEntity instanceof ResourceSearchView) {
			// This is the search View
			ResourceSearchView view = (ResourceSearchView) theEntity;
			resourceBytes = view.getResource();
			resourceText = view.getResourceTextVc();
			resourceEncoding = view.getEncoding();
			version = view.getVersion();
			provenanceRequestId = view.getProvenanceRequestId();
			provenanceSourceUri = view.getProvenanceSourceUri();
			switch (getConfig().getTagStorageMode()) {
				case VERSIONED:
				case NON_VERSIONED:
					if (theTagList != null) {
						tagList = theTagList;
					} else {
						tagList = Collections.emptyList();
					}
					break;
				case INLINE:
					tagList = null;
					break;
			}
		} else {
			// something wrong
			return null;
		}

		// 2. get The text
		String decodedResourceText;
		if (resourceText != null) {
			decodedResourceText = resourceText;
		} else {
			decodedResourceText = decodeResource(resourceBytes, resourceEncoding);
		}

		// 3. Use the appropriate custom type if one is specified in the context
		Class<R> resourceType = theResourceType;
		if (tagList != null) {
			if (myContext.hasDefaultTypeForProfile()) {
				for (BaseTag nextTag : tagList) {
					if (nextTag.getTag().getTagType() == TagTypeEnum.PROFILE) {
						String profile = nextTag.getTag().getCode();
						if (isNotBlank(profile)) {
							Class<? extends IBaseResource> newType = myContext.getDefaultTypeForProfile(profile);
							if (newType != null && theResourceType.isAssignableFrom(newType)) {
								ourLog.debug("Using custom type {} for profile: {}", newType.getName(), profile);
								resourceType = (Class<R>) newType;
								break;
							}
						}
					}
				}
			}
		}

		// 4. parse the text to FHIR
		R retVal;
		if (resourceEncoding != ResourceEncodingEnum.DEL) {

			LenientErrorHandler errorHandler = new LenientErrorHandler(false).setErrorOnInvalidValue(false);
			IParser parser = new TolerantJsonParser(getContext(theEntity.getFhirVersion()), errorHandler, theEntity.getId());

			try {
				retVal = parser.parseResource(resourceType, decodedResourceText);
			} catch (Exception e) {
				StringBuilder b = new StringBuilder();
				b.append("Failed to parse database resource[");
				b.append(myFhirContext.getResourceType(resourceType));
				b.append("/");
				b.append(theEntity.getIdDt().getIdPart());
				b.append(" (pid ");
				b.append(theEntity.getId());
				b.append(", version ");
				b.append(theEntity.getFhirVersion().name());
				b.append("): ");
				b.append(e.getMessage());
				String msg = b.toString();
				ourLog.error(msg, e);
				throw new DataFormatException(Msg.code(928) + msg, e);
			}

		} else {

			retVal = (R) myContext.getResourceDefinition(theEntity.getResourceType()).newInstance();

		}

		// 5. fill MetaData
		if (retVal instanceof IResource) {
			IResource res = (IResource) retVal;
			retVal = populateResourceMetadataHapi(resourceType, theEntity, tagList, theForHistoryOperation, res, version);
		} else {
			IAnyResource res = (IAnyResource) retVal;
			retVal = populateResourceMetadataRi(resourceType, theEntity, tagList, theForHistoryOperation, res, version);
		}

		// 6. Handle source (provenance)
		if (isNotBlank(provenanceRequestId) || isNotBlank(provenanceSourceUri)) {
			String sourceString = cleanProvenanceSourceUri(provenanceSourceUri)
				+ (isNotBlank(provenanceRequestId) ? "#" : "")
				+ defaultString(provenanceRequestId);

			MetaUtil.setSource(myContext, retVal, sourceString);

		}

		// 7. Add partition information
		if (myPartitionSettings.isPartitioningEnabled()) {
			PartitionablePartitionId partitionId = theEntity.getPartitionId();
			if (partitionId != null && partitionId.getPartitionId() != null) {
				PartitionEntity persistedPartition = myPartitionLookupSvc.getPartitionById(partitionId.getPartitionId());
				retVal.setUserData(Constants.RESOURCE_PARTITION_ID, persistedPartition.toRequestPartitionId());
			} else {
				retVal.setUserData(Constants.RESOURCE_PARTITION_ID, null);
			}
		}

		return retVal;
	}

	public String toResourceName(Class<? extends IBaseResource> theResourceType) {
		return myContext.getResourceType(theResourceType);
	}

	String toResourceName(IBaseResource theResource) {
		return myContext.getResourceType(theResource);
	}

	protected ResourceTable updateEntityForDelete(RequestDetails theRequest, TransactionDetails theTransactionDetails, ResourceTable entity) {
		Date updateTime = new Date();
		return updateEntity(theRequest, null, entity, updateTime, true, true, theTransactionDetails, false, true);
	}

	@VisibleForTesting
	public void setEntityManager(EntityManager theEntityManager) {
		myEntityManager = theEntityManager;
	}

	@VisibleForTesting
	public void setSearchParamWithInlineReferencesExtractor(SearchParamWithInlineReferencesExtractor theSearchParamWithInlineReferencesExtractor) {
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

	private void verifyMatchUrlForConditionalCreate(IBaseResource theResource, String theIfNoneExist, ResourceTable entity, ResourceIndexedSearchParams theParams) {
		// Make sure that the match URL was actually appropriate for the supplied resource
		InMemoryMatchResult outcome = myInMemoryResourceMatcher.match(theIfNoneExist, theResource, theParams);
		if (outcome.supported() && !outcome.matched()) {
			throw new InvalidRequestException(Msg.code(929) + "Failed to process conditional create. The supplied resource did not satisfy the conditional URL.");
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public ResourceTable updateEntity(RequestDetails theRequest, final IBaseResource theResource, IBasePersistedResource
		theEntity, Date theDeletedTimestampOrNull, boolean thePerformIndexing,
												 boolean theUpdateVersion, TransactionDetails theTransactionDetails, boolean theForceUpdate, boolean theCreateNewHistoryEntry) {
		Validate.notNull(theEntity);
		Validate.isTrue(theDeletedTimestampOrNull != null || theResource != null, "Must have either a resource[%s] or a deleted timestamp[%s] for resource PID[%s]", theDeletedTimestampOrNull != null, theResource != null, theEntity.getPersistentId());

		ourLog.debug("Starting entity update");

		ResourceTable entity = (ResourceTable) theEntity;

		/*
		 * This should be the very first thing..
		 */
		if (theResource != null) {
			if (thePerformIndexing) {
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
			entity.setHashSha256(null);
			entity.setIndexStatus(INDEX_STATUS_INDEXED);
			changed = populateResourceIntoEntity(theTransactionDetails, theRequest, theResource, entity, true);

		} else {

			// CREATE or UPDATE

			IdentityHashMap<ResourceTable, ResourceIndexedSearchParams> existingSearchParams = theTransactionDetails.getOrCreateUserData(HapiTransactionService.XACT_USERDATA_KEY_EXISTING_SEARCH_PARAMS, () -> new IdentityHashMap<>());
			existingParams = existingSearchParams.get(entity);
			if (existingParams == null) {
				existingParams = new ResourceIndexedSearchParams(entity);
				existingSearchParams.put(entity, existingParams);
			}
			entity.setDeleted(null);

			// TODO: is this IF statement always true? Try removing it
			if (thePerformIndexing || ((ResourceTable) theEntity).getVersion() == 1) {

				newParams = new ResourceIndexedSearchParams();

				RequestPartitionId requestPartitionId;
				if (!myPartitionSettings.isPartitioningEnabled()) {
					requestPartitionId = RequestPartitionId.allPartitions();
				} else if (entity.getPartitionId() != null) {
					requestPartitionId = entity.getPartitionId().toPartitionId();
				} else {
					requestPartitionId = RequestPartitionId.defaultPartition();
				}

				failIfPartitionMismatch(theRequest, entity);
				mySearchParamWithInlineReferencesExtractor.populateFromResource(requestPartitionId, newParams, theTransactionDetails, entity, theResource, existingParams, theRequest, thePerformIndexing);

				changed = populateResourceIntoEntity(theTransactionDetails, theRequest, theResource, entity, true);

				if (theForceUpdate) {
					changed.setChanged(true);
				}

				if (changed.isChanged()) {

					// Make sure that the match URL was actually appropriate for the supplied
					// resource. We only do this for version 1 right now since technically it
					// is possible (and legal) for someone to be using a conditional update
					// to match a resource and then update it in a way that it no longer
					// matches. We could certainly make this configurable though in the
					// future.
					if (entity.getVersion() <= 1L && entity.getCreatedByMatchUrl() != null && thePerformIndexing) {
						verifyMatchUrlForConditionalCreate(theResource, entity.getCreatedByMatchUrl(), entity, newParams);
					}

					entity.setUpdated(theTransactionDetails.getTransactionDate());
					newParams.populateResourceTableSearchParamsPresentFlags(entity);
					entity.setIndexStatus(INDEX_STATUS_INDEXED);
				}

				if (myFulltextSearchSvc != null && !myFulltextSearchSvc.isDisabled()) {
					populateFullTextFields(myContext, theResource, entity, newParams);
				}

			} else {

				changed = populateResourceIntoEntity(theTransactionDetails, theRequest, theResource, entity, false);

				entity.setUpdated(theTransactionDetails.getTransactionDate());
				entity.setIndexStatus(null);

			}

		}

		if (thePerformIndexing && changed != null && !changed.isChanged() && !theForceUpdate && myConfig.isSuppressUpdatesWithNoChange() && (entity.getVersion() > 1 || theUpdateVersion)) {
			ourLog.debug("Resource {} has not changed", entity.getIdDt().toUnqualified().getValue());
			if (theResource != null) {
				updateResourceMetadata(entity, theResource);
			}
			entity.setUnchangedInCurrentOperation(true);
			return entity;
		}

		if (theUpdateVersion) {
			long newVersion = entity.getVersion() + 1;
			entity.setVersion(newVersion);
		}

		/*
		 * Save the resource itself
		 */
		if (entity.getId() == null) {
			myEntityManager.persist(entity);

			if (entity.getForcedId() != null) {
				myEntityManager.persist(entity.getForcedId());
			}

			postPersist(entity, (T) theResource);

		} else if (entity.getDeleted() != null) {
			entity = myEntityManager.merge(entity);

			postDelete(entity);

		} else {
			entity = myEntityManager.merge(entity);

			postUpdate(entity, (T) theResource);
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
			Map<String, Boolean> searchParamPresenceMap = getSearchParamPresenceMap(entity, newParams);

			AddRemoveCount presenceCount = mySearchParamPresenceSvc.updatePresence(entity, searchParamPresenceMap);

			// Interceptor broadcast: JPA_PERFTRACE_INFO
			if (!presenceCount.isEmpty()) {
				if (CompositeInterceptorBroadcaster.hasHooks(Pointcut.JPA_PERFTRACE_INFO, myInterceptorBroadcaster, theRequest)) {
					StorageProcessingMessage message = new StorageProcessingMessage();
					message.setMessage("For " + entity.getIdDt().toUnqualifiedVersionless().getValue() + " added " + presenceCount.getAddCount() + " and removed " + presenceCount.getRemoveCount() + " resource search parameter presence entries");
					HookParams params = new HookParams()
						.add(RequestDetails.class, theRequest)
						.addIfMatchesType(ServletRequestDetails.class, theRequest)
						.add(StorageProcessingMessage.class, message);
					CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.JPA_PERFTRACE_INFO, params);
				}
			}

		}

		/*
		 * Indexing
		 */
		if (thePerformIndexing) {
			if (newParams == null) {
				myExpungeService.deleteAllSearchParams(new ResourcePersistentId(entity.getId()));
			} else {

				// Synchronize search param indexes
				AddRemoveCount searchParamAddRemoveCount = myDaoSearchParamSynchronizer.synchronizeSearchParamsToDatabase(newParams, entity, existingParams);

				newParams.populateResourceTableParamCollections(entity);

				// Interceptor broadcast: JPA_PERFTRACE_INFO
				if (!searchParamAddRemoveCount.isEmpty()) {
					if (CompositeInterceptorBroadcaster.hasHooks(Pointcut.JPA_PERFTRACE_INFO, myInterceptorBroadcaster, theRequest)) {
						StorageProcessingMessage message = new StorageProcessingMessage();
						message.setMessage("For " + entity.getIdDt().toUnqualifiedVersionless().getValue() + " added " + searchParamAddRemoveCount.getAddCount() + " and removed " + searchParamAddRemoveCount.getRemoveCount() + " resource search parameter index entries");
						HookParams params = new HookParams()
							.add(RequestDetails.class, theRequest)
							.addIfMatchesType(ServletRequestDetails.class, theRequest)
							.add(StorageProcessingMessage.class, message);
						CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.JPA_PERFTRACE_INFO, params);
					}
				}

				// Synchronize composite params
				mySearchParamWithInlineReferencesExtractor.storeUniqueComboParameters(newParams, entity, existingParams);
			}
		}

		if (theResource != null) {
			updateResourceMetadata(entity, theResource);
		}


		return entity;
	}

	@NotNull
	private Map<String, Boolean> getSearchParamPresenceMap(ResourceTable entity, ResourceIndexedSearchParams newParams) {
		Map<String, Boolean> retval = new HashMap<>();

		for (String nextKey : newParams.getPopulatedResourceLinkParameters()) {
			retval.put(nextKey, Boolean.TRUE);
		}

		ResourceSearchParams activeSearchParams = mySearchParamRegistry.getActiveSearchParams(entity.getResourceType());
		activeSearchParams.getReferenceSearchParamNames().forEach(key -> {
			if (!retval.containsKey(key)) {
				retval.put(key, Boolean.FALSE);
			}
		});
		return retval;
	}

	/**
	 * TODO eventually consider refactoring this to be part of an interceptor.
	 *
	 * Throws an exception if the partition of the request, and the partition of the existing entity do not match.
	 * @param theRequest the request.
	 * @param entity the existing entity.
	 */
	private void failIfPartitionMismatch(RequestDetails theRequest, ResourceTable entity) {
		if (myPartitionSettings.isPartitioningEnabled() && theRequest != null && theRequest.getTenantId() != null && entity.getPartitionId() != null &&
				theRequest.getTenantId() != ALL_PARTITIONS_NAME) {
			PartitionEntity partitionEntity = myPartitionLookupSvc.getPartitionByName(theRequest.getTenantId());
			//partitionEntity should never be null
			if (partitionEntity != null && !partitionEntity.getId().equals(entity.getPartitionId().getPartitionId())) {
				throw new InvalidRequestException(Msg.code(2079) + "Resource " + entity.getResourceType() + "/" + entity.getId() + " is not known");
			}
		}
	}

	private void createHistoryEntry(RequestDetails theRequest, IBaseResource theResource, ResourceTable theEntity, EncodedResource theChanged) {
		boolean versionedTags = getConfig().getTagStorageMode() == DaoConfig.TagStorageModeEnum.VERSIONED;
		final ResourceHistoryTable historyEntry = theEntity.toHistory(versionedTags);
		historyEntry.setEncoding(theChanged.getEncoding());
		historyEntry.setResource(theChanged.getResourceBinary());
		historyEntry.setResourceTextVc(theChanged.getResourceText());

		ourLog.debug("Saving history entry {}", historyEntry.getIdDt());
		myResourceHistoryTableDao.save(historyEntry);
		theEntity.setCurrentVersionEntity(historyEntry);

		// Save resource source
		String source = null;
		String requestId = theRequest != null ? theRequest.getRequestId() : null;
		if (theResource != null) {
			if (myContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.R4)) {
				IBaseMetaType meta = theResource.getMeta();
				source = MetaUtil.getSource(myContext, meta);
			}
			if (myContext.getVersion().getVersion().equals(FhirVersionEnum.DSTU3)) {
				source = ((IBaseHasExtensions) theResource.getMeta())
					.getExtension()
					.stream()
					.filter(t -> HapiExtensions.EXT_META_SOURCE.equals(t.getUrl()))
					.filter(t -> t.getValue() instanceof IPrimitiveType)
					.map(t -> ((IPrimitiveType<?>) t.getValue()).getValueAsString())
					.findFirst()
					.orElse(null);
			}
		}

		boolean haveSource = isNotBlank(source) && myConfig.getStoreMetaSourceInformation().isStoreSourceUri();
		boolean haveRequestId = isNotBlank(requestId) && myConfig.getStoreMetaSourceInformation().isStoreRequestId();
		if (haveSource || haveRequestId) {
			ResourceHistoryProvenanceEntity provenance = new ResourceHistoryProvenanceEntity();
			provenance.setResourceHistoryTable(historyEntry);
			provenance.setResourceTable(theEntity);
			provenance.setPartitionId(theEntity.getPartitionId());
			if (haveRequestId) {
				provenance.setRequestId(left(requestId, Constants.REQUEST_ID_LENGTH));
			}
			if (haveSource) {
				provenance.setSourceUri(source);
			}
			myEntityManager.persist(provenance);
		}
	}

	private void validateIncomingResourceTypeMatchesExisting(IBaseResource theResource, ResourceTable entity) {
		String resourceType = myContext.getResourceType(theResource);
		if (!resourceType.equals(entity.getResourceType())) {
			throw new UnprocessableEntityException(Msg.code(930) + "Existing resource ID[" + entity.getIdDt().toUnqualifiedVersionless() + "] is of type[" + entity.getResourceType() + "] - Cannot update with [" + resourceType + "]");
		}
	}

	@Override
	public ResourceTable updateInternal(RequestDetails theRequestDetails, T theResource, boolean thePerformIndexing, boolean theForceUpdateVersion,
													IBasePersistedResource theEntity, IIdType theResourceId, IBaseResource theOldResource, TransactionDetails theTransactionDetails) {

		ResourceTable entity = (ResourceTable) theEntity;

		// We'll update the resource ID with the correct version later but for
		// now at least set it to something useful for the interceptors
		theResource.setId(entity.getIdDt());

		// Notify interceptors
		ActionRequestDetails requestDetails;
		if (theRequestDetails != null && theRequestDetails.getServer() != null) {
			requestDetails = new ActionRequestDetails(theRequestDetails, theResource, theResourceId.getResourceType(), theResourceId);
			notifyInterceptors(RestOperationTypeEnum.UPDATE, requestDetails);
		}

		// Notify IServerOperationInterceptors about pre-action call
		HookParams hookParams = new HookParams()
			.add(IBaseResource.class, theOldResource)
			.add(IBaseResource.class, theResource)
			.add(RequestDetails.class, theRequestDetails)
			.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
			.add(TransactionDetails.class, theTransactionDetails);
		doCallHooks(theTransactionDetails, theRequestDetails, Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED, hookParams);

		// Perform update
		ResourceTable savedEntity = updateEntity(theRequestDetails, theResource, entity, null, thePerformIndexing, thePerformIndexing, theTransactionDetails, theForceUpdateVersion, thePerformIndexing);

		/*
		 * If we aren't indexing (meaning we're probably executing a sub-operation within a transaction),
		 * we'll manually increase the version. This is important because we want the updated version number
		 * to be reflected in the resource shared with interceptors
		 */
		if (!thePerformIndexing && !savedEntity.isUnchangedInCurrentOperation() && !ourDisableIncrementOnUpdateForUnitTest) {
			if (theResourceId.hasVersionIdPart() == false) {
				theResourceId = theResourceId.withVersion(Long.toString(savedEntity.getVersion()));
			}
			incrementId(theResource, savedEntity, theResourceId);
		}

		// Update version/lastUpdated so that interceptors see the correct version
		updateResourceMetadata(savedEntity, theResource);

		// Populate the PID in the resource so it is available to hooks
		addPidToResource(savedEntity, theResource);

		// Notify interceptors
		if (!savedEntity.isUnchangedInCurrentOperation()) {
			hookParams = new HookParams()
				.add(IBaseResource.class, theOldResource)
				.add(IBaseResource.class, theResource)
				.add(RequestDetails.class, theRequestDetails)
				.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
				.add(TransactionDetails.class, theTransactionDetails)
				.add(InterceptorInvocationTimingEnum.class, theTransactionDetails.getInvocationTiming(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED));
			doCallHooks(theTransactionDetails, theRequestDetails, Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED, hookParams);
		}

		return savedEntity;
	}

	protected void addPidToResource(IBasePersistedResource theEntity, IBaseResource theResource) {
		if (theResource instanceof IAnyResource) {
			IDao.RESOURCE_PID.put((IAnyResource) theResource, theEntity.getPersistentId().getIdAsLong());
		} else if (theResource instanceof IResource) {
			IDao.RESOURCE_PID.put((IResource) theResource, theEntity.getPersistentId().getIdAsLong());
		}
	}

	protected void updateResourceMetadata(IBaseResourceEntity theEntity, IBaseResource theResource) {
		IIdType id = theEntity.getIdDt();
		if (getContext().getVersion().getVersion().isRi()) {
			id = getContext().getVersion().newIdType().setValue(id.getValue());
		}

		if (id.hasResourceType() == false) {
			id = id.withResourceType(theEntity.getResourceType());
		}

		theResource.setId(id);
		if (theResource instanceof IResource) {
			ResourceMetadataKeyEnum.VERSION.put((IResource) theResource, id.getVersionIdPart());
			ResourceMetadataKeyEnum.UPDATED.put((IResource) theResource, theEntity.getUpdated());
		} else {
			IBaseMetaType meta = theResource.getMeta();
			meta.setVersionId(id.getVersionIdPart());
			meta.setLastUpdated(theEntity.getUpdatedDate());
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

				if (getConfig().isEnforceReferenceTargetTypes()) {
					for (IBase nextChild : values) {
						IBaseReference nextRef = (IBaseReference) nextChild;
						IIdType referencedId = nextRef.getReferenceElement();
						if (!isBlank(referencedId.getResourceType())) {
							if (!isLogicalReference(referencedId)) {
								if (!referencedId.getValue().contains("?")) {
									if (!validTypes.contains(referencedId.getResourceType())) {
										throw new UnprocessableEntityException(Msg.code(931) + "Invalid reference found at path '" + newPath + "'. Resource type '" + referencedId.getResourceType() + "' is not valid for this path");
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
		if (myConfig.getResourceMetaCountHardLimit() != null) {
			if (theMetaCount > myConfig.getResourceMetaCountHardLimit()) {
				throw new UnprocessableEntityException(Msg.code(932) + "Resource contains " + theMetaCount + " meta entries (tag/profile/security label), maximum is " + myConfig.getResourceMetaCountHardLimit());
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
			throw new UnprocessableEntityException(Msg.code(933) + "Resource contains the 'subsetted' tag, and must not be stored as it may contain a subset of available data");
		}

		if (getConfig().isEnforceReferenceTargetTypes()) {
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
	public void setDaoConfigForUnitTest(DaoConfig theDaoConfig) {
		myConfig = theDaoConfig;
	}

	public void populateFullTextFields(final FhirContext theContext, final IBaseResource theResource, ResourceTable theEntity, ResourceIndexedSearchParams theNewParams) {
		if (theEntity.getDeleted() != null) {
			theEntity.setNarrativeText(null);
			theEntity.setContentText(null);
		} else {
			theEntity.setNarrativeText(parseNarrativeTextIntoWords(theResource));
			theEntity.setContentText(parseContentTextIntoWords(theContext, theResource));
			if (myDaoConfig.isAdvancedLuceneIndexing()) {
				ExtendedLuceneIndexData luceneIndexData = myFulltextSearchSvc.extractLuceneIndexData(theResource, theNewParams);
				theEntity.setLuceneIndexData(luceneIndexData);
			}
		}
	}

	@VisibleForTesting
	public void setPartitionSettingsForUnitTest(PartitionSettings thePartitionSettings) {
		myPartitionSettings = thePartitionSettings;
	}

	@Nonnull
	public static MemoryCacheService.TagDefinitionCacheKey toTagDefinitionMemoryCacheKey(TagTypeEnum theTagType, String theScheme, String theTerm) {
		return new MemoryCacheService.TagDefinitionCacheKey(theTagType, theScheme, theTerm);
	}

	static String cleanProvenanceSourceUri(String theProvenanceSourceUri) {
		if (isNotBlank(theProvenanceSourceUri)) {
			int hashIndex = theProvenanceSourceUri.indexOf('#');
			if (hashIndex != -1) {
				theProvenanceSourceUri = theProvenanceSourceUri.substring(0, hashIndex);
			}
		}
		return defaultString(theProvenanceSourceUri);
	}

	@SuppressWarnings("unchecked")
	public static String parseContentTextIntoWords(FhirContext theContext, IBaseResource theResource) {

		Class<IPrimitiveType<String>> stringType = (Class<IPrimitiveType<String>>) theContext.getElementDefinition("string").getImplementingClass();

		StringBuilder retVal = new StringBuilder();
		List<IPrimitiveType<String>> childElements = theContext.newTerser().getAllPopulatedChildElementsOfType(theResource, stringType);
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
				break;
		}
		return resourceText;
	}

	public static String encodeResource(IBaseResource theResource, ResourceEncodingEnum theEncoding, List<String> theExcludeElements, FhirContext theContext) {
		IParser parser = theEncoding.newParser(theContext);
		parser.setDontEncodeElements(theExcludeElements);
		return parser.encodeResourceToString(theResource);
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

	private static List<BaseCodingDt> toBaseCodingList(List<IBaseCoding> theSecurityLabels) {
		ArrayList<BaseCodingDt> retVal = new ArrayList<>(theSecurityLabels.size());
		for (IBaseCoding next : theSecurityLabels) {
			retVal.add((BaseCodingDt) next);
		}
		return retVal;
	}

	public static void validateResourceType(BaseHasResource theEntity, String theResourceName) {
		if (!theResourceName.equals(theEntity.getResourceType())) {
			throw new ResourceNotFoundException(Msg.code(935) + "Resource with ID " + theEntity.getIdDt().getIdPart() + " exists but it is not of type " + theResourceName + ", found resource of type " + theEntity.getResourceType());
		}
	}

	private class AddTagDefinitionToCacheAfterCommitSynchronization implements TransactionSynchronization {

		private final TagDefinition myTagDefinition;
		private final MemoryCacheService.TagDefinitionCacheKey myKey;

		public AddTagDefinitionToCacheAfterCommitSynchronization(MemoryCacheService.TagDefinitionCacheKey theKey, TagDefinition theTagDefinition) {
			myTagDefinition = theTagDefinition;
			myKey = theKey;
		}

		@Override
		public void afterCommit() {
			myMemoryCacheService.put(MemoryCacheService.CacheEnum.TAG_DEFINITION, myKey, myTagDefinition);
		}
	}

}
