package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeChildResourceDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.api.dao.IJpaDao;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.dao.data.IForcedIdDao;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceProvenanceDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTagDao;
import ca.uhn.fhir.jpa.dao.expunge.ExpungeService;
import ca.uhn.fhir.jpa.dao.index.DaoSearchParamSynchronizer;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.dao.index.SearchParamWithInlineReferencesExtractor;
import ca.uhn.fhir.jpa.delete.DeleteConflictService;
import ca.uhn.fhir.jpa.entity.ResourceSearchView;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.entity.SearchTypeEnum;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.entity.*;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProviderFactory;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.searchparam.ResourceMetaParams;
import ca.uhn.fhir.jpa.searchparam.extractor.LogicalReferenceHelper;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.sp.ISearchParamPresenceSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.util.AddRemoveCount;
import ca.uhn.fhir.jpa.util.JpaInterceptorBroadcaster;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.valueset.BundleEntryTransactionMethodEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.LenientErrorHandler;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.CoverageIgnore;
import ca.uhn.fhir.util.MetaUtil;
import ca.uhn.fhir.util.XmlUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.r4.model.Bundle.HTTPVerb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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
import java.util.*;
import java.util.Map.Entry;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.left;
import static org.apache.commons.lang3.StringUtils.trim;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
	public static final String NS_JPA_PROFILE = "https://github.com/jamesagnew/hapi-fhir/ns/jpa/profile";
	public static final String OO_SEVERITY_ERROR = "error";
	public static final String OO_SEVERITY_INFO = "information";
	public static final String OO_SEVERITY_WARN = "warning";
	private static final Logger ourLog = LoggerFactory.getLogger(BaseHapiFhirDao.class);
	private static final Map<FhirVersionEnum, FhirContext> ourRetrievalContexts = new HashMap<>();
	private static final String PROCESSING_SUB_REQUEST = "BaseHapiFhirDao.processingSubRequest";
	private static boolean ourValidationDisabledForUnitTest;
	private static boolean ourDisableIncrementOnUpdateForUnitTest = false;

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;
	@Autowired
	protected IdHelperService myIdHelperService;
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
	ExpungeService myExpungeService;
	@Autowired
	private DaoConfig myConfig;
	@Autowired
	private PlatformTransactionManager myPlatformTransactionManager;
	@Autowired
	private ISearchCacheSvc mySearchCacheSvc;
	@Autowired
	private ISearchParamPresenceSvc mySearchParamPresenceSvc;
	@Autowired
	protected DaoRegistry myDaoRegistry;
	@Autowired
	private SearchParamWithInlineReferencesExtractor mySearchParamWithInlineReferencesExtractor;
	@Autowired
	private DaoSearchParamSynchronizer myDaoSearchParamSynchronizer;
	@Autowired
	private SearchBuilderFactory mySearchBuilderFactory;
	private FhirContext myContext;
	private ApplicationContext myApplicationContext;

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

	/**
	 * Returns the newly created forced ID. If the entity already had a forced ID, or if
	 * none was created, returns null.
	 */
	protected ForcedId createForcedIdIfNeeded(ResourceTable theEntity, IIdType theId, boolean theCreateForPureNumericIds) {
		ForcedId retVal = null;
		if (theId.isEmpty() == false && theId.hasIdPart() && theEntity.getForcedId() == null) {
			if (theCreateForPureNumericIds || !IdHelperService.isValidPid(theId)) {
				retVal = new ForcedId();
				retVal.setResourceType(theEntity.getResourceType());
				retVal.setForcedId(theId.getIdPart());
				retVal.setResource(theEntity);
				theEntity.setForcedId(retVal);
			}
		}

		return retVal;
	}

	private void extractTagsHapi(IResource theResource, ResourceTable theEntity, Set<ResourceTag> allDefs) {
		TagList tagList = ResourceMetadataKeyEnum.TAG_LIST.get(theResource);
		if (tagList != null) {
			for (Tag next : tagList) {
				TagDefinition def = getTagOrNull(TagTypeEnum.TAG, next.getScheme(), next.getTerm(), next.getLabel());
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
				TagDefinition def = getTagOrNull(TagTypeEnum.SECURITY_LABEL, next.getSystemElement().getValue(), next.getCodeElement().getValue(), next.getDisplayElement().getValue());
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
				TagDefinition def = getTagOrNull(TagTypeEnum.PROFILE, NS_JPA_PROFILE, next.getValue(), null);
				if (def != null) {
					ResourceTag tag = theEntity.addTag(def);
					allDefs.add(tag);
					theEntity.setHasTags(true);
				}
			}
		}
	}

	private void extractTagsRi(IAnyResource theResource, ResourceTable theEntity, Set<ResourceTag> theAllTags) {
		List<? extends IBaseCoding> tagList = theResource.getMeta().getTag();
		if (tagList != null) {
			for (IBaseCoding next : tagList) {
				TagDefinition def = getTagOrNull(TagTypeEnum.TAG, next.getSystem(), next.getCode(), next.getDisplay());
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
				TagDefinition def = getTagOrNull(TagTypeEnum.SECURITY_LABEL, next.getSystem(), next.getCode(), next.getDisplay());
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
				TagDefinition def = getTagOrNull(TagTypeEnum.PROFILE, NS_JPA_PROFILE, next.getValue(), null);
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

	public void setConfig(DaoConfig theConfig) {
		myConfig = theConfig;
	}

	@Override
	public FhirContext getContext() {
		return myContext;
	}

	@Autowired
	public void setContext(FhirContext theContext) {
		myContext = theContext;
	}

	public FhirContext getContext(FhirVersionEnum theVersion) {
		Validate.notNull(theVersion, "theVersion must not be null");
		synchronized (ourRetrievalContexts) {
			FhirContext retVal = ourRetrievalContexts.get(theVersion);
			if (retVal == null) {
				retVal = new FhirContext(theVersion);
				ourRetrievalContexts.put(theVersion, retVal);
			}
			return retVal;
		}
	}

	protected TagDefinition getTagOrNull(TagTypeEnum theTagType, String theScheme, String theTerm, String theLabel) {
		if (isBlank(theScheme) && isBlank(theTerm) && isBlank(theLabel)) {
			return null;
		}

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
		try {
			return q.getSingleResult();
		} catch (NoResultException e) {
			TagDefinition retVal = new TagDefinition(theTagType, theScheme, theTerm, theLabel);
			myEntityManager.persist(retVal);
			return retVal;
		}
	}


	protected IBundleProvider history(RequestDetails theRequest, String theResourceName, Long theId, Date theSince, Date theUntil) {

		String resourceName = defaultIfBlank(theResourceName, null);

		Search search = new Search();
		search.setDeleted(false);
		search.setCreated(new Date());
		search.setLastUpdated(theSince, theUntil);
		search.setUuid(UUID.randomUUID().toString());
		search.setResourceType(resourceName);
		search.setResourceId(theId);
		search.setSearchType(SearchTypeEnum.HISTORY);
		search.setStatus(SearchStatusEnum.FINISHED);

		if (theSince != null) {
			if (resourceName == null) {
				search.setTotalCount(myResourceHistoryTableDao.countForAllResourceTypes(theSince));
			} else if (theId == null) {
				search.setTotalCount(myResourceHistoryTableDao.countForResourceType(resourceName, theSince));
			} else {
				search.setTotalCount(myResourceHistoryTableDao.countForResourceInstance(theId, theSince));
			}
		} else {
			if (resourceName == null) {
				search.setTotalCount(myResourceHistoryTableDao.countForAllResourceTypes());
			} else if (theId == null) {
				search.setTotalCount(myResourceHistoryTableDao.countForResourceType(resourceName));
			} else {
				search.setTotalCount(myResourceHistoryTableDao.countForResourceInstance(theId));
			}
		}

		search = mySearchCacheSvc.save(search);

		return myPersistedJpaBundleProviderFactory.newInstance(theRequest, search.getUuid());
	}

	@Autowired
	private PersistedJpaBundleProviderFactory myPersistedJpaBundleProviderFactory;


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

	public void notifyInterceptors(RestOperationTypeEnum theOperationType, ActionRequestDetails theRequestDetails) {
		if (theRequestDetails.getId() != null && theRequestDetails.getId().hasResourceType() && isNotBlank(theRequestDetails.getResourceType())) {
			if (theRequestDetails.getId().getResourceType().equals(theRequestDetails.getResourceType()) == false) {
				throw new InternalErrorException(
					"Inconsistent server state - Resource types don't match: " + theRequestDetails.getId().getResourceType() + " / " + theRequestDetails.getResourceType());
			}
		}

		if (theRequestDetails.getUserData().get(PROCESSING_SUB_REQUEST) == Boolean.TRUE) {
			theRequestDetails.notifyIncomingRequestPreHandled(theOperationType);
		}
	}

	/**
	 * Returns true if the resource has changed (either the contents or the tags)
	 */
	protected EncodedResource populateResourceIntoEntity(RequestDetails theRequest, IBaseResource theResource, ResourceTable theEntity, boolean theUpdateHash) {
		if (theEntity.getResourceType() == null) {
			theEntity.setResourceType(toResourceName(theResource));
		}

		if (theResource != null) {
			List<BaseResourceReferenceDt> refs = myContext.newTerser().getAllPopulatedChildElementsOfType(theResource, BaseResourceReferenceDt.class);
			for (BaseResourceReferenceDt nextRef : refs) {
				if (nextRef.getReference().isEmpty() == false) {
					if (nextRef.getReference().hasVersionIdPart()) {
						nextRef.setReference(nextRef.getReference().toUnqualifiedVersionless());
					}
				}
			}
		}

		byte[] bytes;
		ResourceEncodingEnum encoding;
		boolean changed = false;

		if (theEntity.getDeleted() == null) {

			encoding = myConfig.getResourceEncoding();
			Set<String> excludeElements = ResourceMetaParams.EXCLUDE_ELEMENTS_IN_ENCODED;
			theEntity.setFhirVersion(myContext.getVersion().getVersion());

			bytes = encodeResource(theResource, encoding, excludeElements, myContext);

			if (theUpdateHash) {
				HashFunction sha256 = Hashing.sha256();
				String hashSha256 = sha256.hashBytes(bytes).toString();
				if (hashSha256.equals(theEntity.getHashSha256()) == false) {
					changed = true;
				}
				theEntity.setHashSha256(hashSha256);
			}

			Set<ResourceTag> allDefs = new HashSet<>();
			Set<ResourceTag> allTagsOld = getAllTagDefinitions(theEntity);

			if (theResource instanceof IResource) {
				extractTagsHapi((IResource) theResource, theEntity, allDefs);
			} else {
				extractTagsRi((IAnyResource) theResource, theEntity, allDefs);
			}

			RuntimeResourceDefinition def = myContext.getResourceDefinition(theResource);
			if (def.isStandardType() == false) {
				String profile = def.getResourceProfile("");
				if (isNotBlank(profile)) {
					TagDefinition profileDef = getTagOrNull(TagTypeEnum.PROFILE, NS_JPA_PROFILE, profile, null);
					if (def != null) {
						ResourceTag tag = theEntity.addTag(profileDef);
						allDefs.add(tag);
						theEntity.setHasTags(true);
					}
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

			if (!allTagsOld.equals(allTagsNew)) {
				changed = true;
			}
			theEntity.setHasTags(!allTagsNew.isEmpty());

		} else {
			theEntity.setHashSha256(null);
			bytes = null;
			encoding = ResourceEncodingEnum.DEL;
		}

		if (changed == false) {
			if (theEntity.getId() == null) {
				changed = true;
			} else {
				ResourceHistoryTable currentHistoryVersion = theEntity.getCurrentVersionEntity();
				if (currentHistoryVersion == null) {
					currentHistoryVersion = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(theEntity.getId(), theEntity.getVersion());
				}
				if (currentHistoryVersion == null || currentHistoryVersion.getResource() == null) {
					changed = true;
				} else {
					changed = !Arrays.equals(currentHistoryVersion.getResource(), bytes);
				}
			}
		}

		EncodedResource retVal = new EncodedResource();
		retVal.setEncoding(encoding);
		retVal.setResource(bytes);
		retVal.setChanged(changed);

		return retVal;
	}

	@SuppressWarnings("unchecked")
	private <R extends IBaseResource> R populateResourceMetadataHapi(Class<R> theResourceType, IBaseResourceEntity theEntity, Collection<? extends BaseTag> theTagList, boolean theForHistoryOperation, IResource res, Long theVersion) {
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
		IDao.RESOURCE_PID.put(res, theEntity.getId());

		Collection<? extends BaseTag> tags = theTagList;
		if (theEntity.isHasTags()) {
			TagList tagList = new TagList();
			List<IBaseCoding> securityLabels = new ArrayList<>();
			List<IdDt> profiles = new ArrayList<>();
			for (BaseTag next : tags) {
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

		return retVal;
	}

	@SuppressWarnings("unchecked")
	private <R extends IBaseResource> R populateResourceMetadataRi(Class<R> theResourceType, IBaseResourceEntity theEntity, Collection<? extends BaseTag> theTagList, boolean theForHistoryOperation, IAnyResource res, Long theVersion) {
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

		res.getMeta().getTag().clear();
		res.getMeta().getProfile().clear();
		res.getMeta().getSecurity().clear();
		res.getMeta().setLastUpdated(null);
		res.getMeta().setVersionId(null);

		updateResourceMetadata(theEntity, res);
		res.setId(res.getIdElement().withVersion(theVersion.toString()));

		res.getMeta().setLastUpdated(theEntity.getUpdatedDate());
		IDao.RESOURCE_PID.put(res, theEntity.getId());

		Collection<? extends BaseTag> tags = theTagList;

		if (theEntity.isHasTags()) {
			for (BaseTag next : tags) {
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
		throw new NotImplementedException("");
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
		ResourceEncodingEnum resourceEncoding;
		Collection<? extends BaseTag> myTagList;
		long version;
		String provenanceSourceUri = null;
		String provenanceRequestId = null;

		if (theEntity instanceof ResourceHistoryTable) {
			ResourceHistoryTable history = (ResourceHistoryTable) theEntity;
			resourceBytes = history.getResource();
			resourceEncoding = history.getEncoding();
			myTagList = history.getTags();
			version = history.getVersion();
			if (history.getProvenance() != null) {
				provenanceRequestId = history.getProvenance().getRequestId();
				provenanceSourceUri = history.getProvenance().getSourceUri();
			}
		} else if (theEntity instanceof ResourceTable) {
			ResourceTable resource = (ResourceTable) theEntity;
			version = theEntity.getVersion();
			ResourceHistoryTable history = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(theEntity.getId(), version);
			((ResourceTable) theEntity).setCurrentVersionEntity(history);

			while (history == null) {
				if (version > 1L) {
					version--;
					history = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(theEntity.getId(), version);
				} else {
					return null;
				}
			}
			resourceBytes = history.getResource();
			resourceEncoding = history.getEncoding();
			myTagList = resource.getTags();
			version = history.getVersion();
			if (history.getProvenance() != null) {
				provenanceRequestId = history.getProvenance().getRequestId();
				provenanceSourceUri = history.getProvenance().getSourceUri();
			}
		} else if (theEntity instanceof ResourceSearchView) {
			// This is the search View
			ResourceSearchView view = (ResourceSearchView) theEntity;
			resourceBytes = view.getResource();
			resourceEncoding = view.getEncoding();
			version = view.getVersion();
			provenanceRequestId = view.getProvenanceRequestId();
			provenanceSourceUri = view.getProvenanceSourceUri();
			if (theTagList == null)
				myTagList = new HashSet<>();
			else
				myTagList = theTagList;
		} else {
			// something wrong
			return null;
		}

		// 2. get The text
		String resourceText = decodeResource(resourceBytes, resourceEncoding);

		// 3. Use the appropriate custom type if one is specified in the context
		Class<R> resourceType = theResourceType;
		if (myContext.hasDefaultTypeForProfile()) {
			for (BaseTag nextTag : myTagList) {
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

		// 4. parse the text to FHIR
		R retVal;
		if (resourceEncoding != ResourceEncodingEnum.DEL) {

			LenientErrorHandler errorHandler = new LenientErrorHandler(false).setErrorOnInvalidValue(false);
			IParser parser = new TolerantJsonParser(getContext(theEntity.getFhirVersion()), errorHandler);

			try {
				retVal = parser.parseResource(resourceType, resourceText);
			} catch (Exception e) {
				StringBuilder b = new StringBuilder();
				b.append("Failed to parse database resource[");
				b.append(resourceType);
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
				throw new DataFormatException(msg, e);
			}

		} else {

			retVal = (R) myContext.getResourceDefinition(theEntity.getResourceType()).newInstance();

		}

		// 5. fill MetaData
		if (retVal instanceof IResource) {
			IResource res = (IResource) retVal;
			retVal = populateResourceMetadataHapi(resourceType, theEntity, myTagList, theForHistoryOperation, res, version);
		} else {
			IAnyResource res = (IAnyResource) retVal;
			retVal = populateResourceMetadataRi(resourceType, theEntity, myTagList, theForHistoryOperation, res, version);
		}

		// 6. Handle source (provenance)
		if (isNotBlank(provenanceRequestId) || isNotBlank(provenanceSourceUri)) {
			String sourceString = cleanProvenanceSourceUri(provenanceSourceUri)
				+ (isNotBlank(provenanceRequestId) ? "#" : "")
				+ defaultString(provenanceRequestId);

			MetaUtil.setSource(myContext, retVal, sourceString);

		}

		return retVal;
	}

	public String toResourceName(Class<? extends IBaseResource> theResourceType) {
		return myContext.getResourceDefinition(theResourceType).getName();
	}

	String toResourceName(IBaseResource theResource) {
		return myContext.getResourceDefinition(theResource).getName();
	}

	protected ResourceTable updateEntityForDelete(RequestDetails theRequest, ResourceTable entity) {
		Date updateTime = new Date();
		return updateEntity(theRequest, null, entity, updateTime, true, true, updateTime, false, true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResourceTable updateEntity(RequestDetails theRequest, final IBaseResource theResource, IBasePersistedResource
		theEntity, Date theDeletedTimestampOrNull, boolean thePerformIndexing,
												 boolean theUpdateVersion, Date theUpdateTime, boolean theForceUpdate, boolean theCreateNewHistoryEntry) {
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
			String resourceType = myContext.getResourceDefinition(theResource).getName();
			if (isNotBlank(entity.getResourceType()) && !entity.getResourceType().equals(resourceType)) {
				throw new UnprocessableEntityException(
					"Existing resource ID[" + entity.getIdDt().toUnqualifiedVersionless() + "] is of type[" + entity.getResourceType() + "] - Cannot update with [" + resourceType + "]");
			}
		}

		if (entity.getPublished() == null) {
			ourLog.debug("Entity has published time: {}", new InstantDt(theUpdateTime));

			entity.setPublished(theUpdateTime);
		}

		ResourceIndexedSearchParams existingParams = null;

		ResourceIndexedSearchParams newParams = null;

		EncodedResource changed;
		if (theDeletedTimestampOrNull != null) {
			// DELETE

			entity.setDeleted(theDeletedTimestampOrNull);
			entity.setUpdated(theDeletedTimestampOrNull);
			entity.setNarrativeTextParsedIntoWords(null);
			entity.setContentTextParsedIntoWords(null);
			entity.setHashSha256(null);
			entity.setIndexStatus(INDEX_STATUS_INDEXED);
			changed = populateResourceIntoEntity(theRequest, theResource, entity, true);

		} else {
			// CREATE or UPDATE
			existingParams = new ResourceIndexedSearchParams(entity);
			entity.setDeleted(null);

			if (thePerformIndexing) {

				newParams = new ResourceIndexedSearchParams();
				mySearchParamWithInlineReferencesExtractor.populateFromResource(newParams, theUpdateTime, entity, theResource, existingParams, theRequest);

				changed = populateResourceIntoEntity(theRequest, theResource, entity, true);
				if (changed.isChanged()) {
					entity.setUpdated(theUpdateTime);
					if (theResource instanceof IResource) {
						entity.setLanguage(((IResource) theResource).getLanguage().getValue());
					} else {
						entity.setLanguage(((IAnyResource) theResource).getLanguageElement().getValue());
					}

					newParams.setParamsOn(entity);
					entity.setIndexStatus(INDEX_STATUS_INDEXED);
					populateFullTextFields(myContext, theResource, entity);
				}
			} else {

				changed = populateResourceIntoEntity(theRequest, theResource, entity, false);

				entity.setUpdated(theUpdateTime);
				entity.setIndexStatus(null);

			}

		}

		if (!changed.isChanged() && !theForceUpdate && myConfig.isSuppressUpdatesWithNoChange()) {
			ourLog.debug("Resource {} has not changed", entity.getIdDt().toUnqualified().getValue());
			if (theResource != null) {
				updateResourceMetadata(entity, theResource);
			}
			entity.setUnchangedInCurrentOperation(true);
			return entity;
		}

		if (theUpdateVersion) {
			entity.setVersion(entity.getVersion() + 1);
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

		/*
		 * Create history entry
		 */
		if (theCreateNewHistoryEntry) {
			final ResourceHistoryTable historyEntry = entity.toHistory();
			historyEntry.setEncoding(changed.getEncoding());
			historyEntry.setResource(changed.getResource());

			ourLog.debug("Saving history entry {}", historyEntry.getIdDt());
			myResourceHistoryTableDao.save(historyEntry);

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
						.filter(t -> Constants.EXT_META_SOURCE.equals(t.getUrl()))
						.filter(t -> t.getValue() instanceof IPrimitiveType)
						.map(t -> ((IPrimitiveType) t.getValue()).getValueAsString())
						.findFirst()
						.orElse(null);
				}
			}
			boolean haveSource = isNotBlank(source) && myConfig.getStoreMetaSourceInformation().isStoreSourceUri();
			boolean haveRequestId = isNotBlank(requestId) && myConfig.getStoreMetaSourceInformation().isStoreRequestId();
			if (haveSource || haveRequestId) {
				ResourceHistoryProvenanceEntity provenance = new ResourceHistoryProvenanceEntity();
				provenance.setResourceHistoryTable(historyEntry);
				provenance.setResourceTable(entity);
				if (haveRequestId) {
					provenance.setRequestId(left(requestId, Constants.REQUEST_ID_LENGTH));
				}
				if (haveSource) {
					provenance.setSourceUri(source);
				}
				myEntityManager.persist(provenance);
			}
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
			Map<String, Boolean> presentSearchParams = new HashMap<>();
			for (String nextKey : newParams.getPopulatedResourceLinkParameters()) {
				presentSearchParams.put(nextKey, Boolean.TRUE);
			}
			Set<Entry<String, RuntimeSearchParam>> activeSearchParams = mySearchParamRegistry.getActiveSearchParams(entity.getResourceType()).entrySet();
			for (Entry<String, RuntimeSearchParam> nextSpEntry : activeSearchParams) {
				if (nextSpEntry.getValue().getParamType() == RestSearchParameterTypeEnum.REFERENCE) {
					if (!presentSearchParams.containsKey(nextSpEntry.getKey())) {
						presentSearchParams.put(nextSpEntry.getKey(), Boolean.FALSE);
					}
				}
			}
			AddRemoveCount presenceCount = mySearchParamPresenceSvc.updatePresence(entity, presentSearchParams);

			// Interceptor broadcast: JPA_PERFTRACE_INFO
			if (!presenceCount.isEmpty()) {
				if (JpaInterceptorBroadcaster.hasHooks(Pointcut.JPA_PERFTRACE_INFO, myInterceptorBroadcaster, theRequest)) {
					StorageProcessingMessage message = new StorageProcessingMessage();
					message.setMessage("For " + entity.getIdDt().toUnqualifiedVersionless().getValue() + " added " + presenceCount.getAddCount() + " and removed " + presenceCount.getRemoveCount() + " resource search parameter presence entries");
					HookParams params = new HookParams()
						.add(RequestDetails.class, theRequest)
						.addIfMatchesType(ServletRequestDetails.class, theRequest)
						.add(StorageProcessingMessage.class, message);
					JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.JPA_PERFTRACE_INFO, params);
				}
			}

		}

		/*
		 * Indexing
		 */
		if (thePerformIndexing) {
			if (newParams == null) {
				myExpungeService.deleteAllSearchParams(entity.getId());
			} else {

				// Synchronize search param indexes
				AddRemoveCount searchParamAddRemoveCount = myDaoSearchParamSynchronizer.synchronizeSearchParamsToDatabase(newParams, entity, existingParams);

				// Interceptor broadcast: JPA_PERFTRACE_INFO
				if (!searchParamAddRemoveCount.isEmpty()) {
					if (JpaInterceptorBroadcaster.hasHooks(Pointcut.JPA_PERFTRACE_INFO, myInterceptorBroadcaster, theRequest)) {
						StorageProcessingMessage message = new StorageProcessingMessage();
						message.setMessage("For " + entity.getIdDt().toUnqualifiedVersionless().getValue() + " added " + searchParamAddRemoveCount.getAddCount() + " and removed " + searchParamAddRemoveCount.getRemoveCount() + " resource search parameter index entries");
						HookParams params = new HookParams()
							.add(RequestDetails.class, theRequest)
							.addIfMatchesType(ServletRequestDetails.class, theRequest)
							.add(StorageProcessingMessage.class, message);
						JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.JPA_PERFTRACE_INFO, params);
					}
				}

				// Synchronize composite params
				mySearchParamWithInlineReferencesExtractor.storeCompositeStringUniques(newParams, entity, existingParams);
			}
		}

		if (theResource != null) {
			updateResourceMetadata(entity, theResource);
		}


		return entity;
	}

	@Override
	public ResourceTable updateInternal(RequestDetails theRequestDetails, T theResource, boolean thePerformIndexing, boolean theForceUpdateVersion,
													IBasePersistedResource theEntity2, IIdType theResourceId, IBaseResource theOldResource) {

		ResourceTable entity = (ResourceTable) theEntity2;

		// We'll update the resource ID with the correct version later but for
		// now at least set it to something useful for the interceptors
		theResource.setId(entity.getIdDt());

		// Notify interceptors
		ActionRequestDetails requestDetails;
		if (theRequestDetails != null) {
			requestDetails = new ActionRequestDetails(theRequestDetails, theResource, theResourceId.getResourceType(), theResourceId);
			notifyInterceptors(RestOperationTypeEnum.UPDATE, requestDetails);
		}

		// Notify IServerOperationInterceptors about pre-action call
		HookParams hookParams = new HookParams()
			.add(IBaseResource.class, theOldResource)
			.add(IBaseResource.class, theResource)
			.add(RequestDetails.class, theRequestDetails)
			.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
		doCallHooks(theRequestDetails, Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED, hookParams);

		// Perform update
		ResourceTable savedEntity = updateEntity(theRequestDetails, theResource, entity, null, thePerformIndexing, thePerformIndexing, new Date(), theForceUpdateVersion, thePerformIndexing);

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
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
				@Override
				public void beforeCommit(boolean readOnly) {
					HookParams hookParams = new HookParams()
						.add(IBaseResource.class, theOldResource)
						.add(IBaseResource.class, theResource)
						.add(RequestDetails.class, theRequestDetails)
						.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
					doCallHooks(theRequestDetails, Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED, hookParams);
				}
			});
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

	private void validateChildReferences(IBase theElement, String thePath) {
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
				validateChildReferences(nextChild, newPath);
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
					validTypes.add(getContext().getResourceDefinition(nextValidType).getName());
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
										throw new UnprocessableEntityException(
											"Invalid reference found at path '" + newPath + "'. Resource type '" + referencedId.getResourceType() + "' is not valid for this path");
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
				throw new UnprocessableEntityException("Resource contains " + theMetaCount + " meta entries (tag/profile/security label), maximum is " + myConfig.getResourceMetaCountHardLimit());
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
			throw new UnprocessableEntityException("Resource contains the 'subsetted' tag, and must not be stored as it may contain a subset of available data");
		}

		String resName = getContext().getResourceDefinition(theResource).getName();
		validateChildReferences(theResource, resName);

		validateMetaCount(totalMetaCount);

	}

	@PostConstruct
	public void start() {
		// nothing yet
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
		for (@SuppressWarnings("rawtypes")
			IPrimitiveType<String> nextType : childElements) {
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

	public static void clearRequestAsProcessingSubRequest(ServletRequestDetails theRequestDetails) {
		if (theRequestDetails != null) {
			theRequestDetails.getUserData().remove(PROCESSING_SUB_REQUEST);
		}
	}

	public static void markRequestAsProcessingSubRequest(ServletRequestDetails theRequestDetails) {
		if (theRequestDetails != null) {
			theRequestDetails.getUserData().put(PROCESSING_SUB_REQUEST, Boolean.TRUE);
		}
	}

	public static void populateFullTextFields(final FhirContext theContext, final IBaseResource theResource, ResourceTable theEntity) {
		if (theEntity.getDeleted() != null) {
			theEntity.setNarrativeTextParsedIntoWords(null);
			theEntity.setContentTextParsedIntoWords(null);
		} else {
			theEntity.setNarrativeTextParsedIntoWords(parseNarrativeTextIntoWords(theResource));
			theEntity.setContentTextParsedIntoWords(parseContentTextIntoWords(theContext, theResource));
		}
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

	public static byte[] encodeResource(IBaseResource theResource, ResourceEncodingEnum theEncoding, Set<String> theExcludeElements, FhirContext theContext) {
		byte[] bytes;
		IParser parser = theEncoding.newParser(theContext);
		parser.setDontEncodeElements(theExcludeElements);
		String encoded = parser.encodeResourceToString(theResource);


		switch (theEncoding) {
			case JSON:
				bytes = encoded.getBytes(Charsets.UTF_8);
				break;
			case JSONC:
				bytes = GZipUtil.compress(encoded);
				break;
			default:
			case DEL:
				bytes = new byte[0];
				break;
		}

		ourLog.debug("Encoded {} chars of resource body as {} bytes", encoded.length(), bytes.length);
		return bytes;
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
				throw new DataFormatException("Unable to convert DIV to string", e);
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
			throw new ResourceNotFoundException(
				"Resource with ID " + theEntity.getIdDt().getIdPart() + " exists but it is not of type " + theResourceName + ", found resource of type " + theEntity.getResourceType());
		}
	}

}
