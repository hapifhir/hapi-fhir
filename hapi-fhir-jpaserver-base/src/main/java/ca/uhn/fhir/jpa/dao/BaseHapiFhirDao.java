package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.base.Function;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeChildResourceDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.entity.BaseHasResource;
import ca.uhn.fhir.jpa.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.entity.BaseTag;
import ca.uhn.fhir.jpa.entity.ForcedId;
import ca.uhn.fhir.jpa.entity.ResourceEncodingEnum;
import ca.uhn.fhir.jpa.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.entity.ResourceHistoryTag;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamCoords;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.jpa.entity.ResourceLink;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.ResourceTag;
import ca.uhn.fhir.jpa.entity.TagDefinition;
import ca.uhn.fhir.jpa.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.util.StopWatch;
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.BaseResource;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.valueset.BundleEntryTransactionMethodEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.MethodUtil;
import ca.uhn.fhir.rest.method.QualifiedParamList;
import ca.uhn.fhir.rest.method.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriAndListParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.util.FhirTerser;
import net.sourceforge.cobertura.CoverageIgnore;

public abstract class BaseHapiFhirDao<T extends IBaseResource> implements IDao {

	/**
	 * These are parameters which are supported by {@link BaseHapiFhirResourceDao#searchForIds(Map)}
	 */
	protected static final Map<String, Class<? extends IQueryParameterType>> RESOURCE_META_PARAMS;
	/**
	 * These are parameters which are supported by {@link BaseHapiFhirResourceDao#searchForIds(Map)}
	 */
	protected static final Map<String, Class<? extends IQueryParameterAnd<?>>> RESOURCE_META_AND_PARAMS;

	static {
		Map<String, Class<? extends IQueryParameterType>> resourceMetaParams = new HashMap<String, Class<? extends IQueryParameterType>>();
		Map<String, Class<? extends IQueryParameterAnd<?>>> resourceMetaAndParams = new HashMap<String, Class<? extends IQueryParameterAnd<?>>>();
		resourceMetaParams.put(BaseResource.SP_RES_ID, StringParam.class);
		resourceMetaAndParams.put(BaseResource.SP_RES_ID, StringAndListParam.class);
		resourceMetaParams.put(BaseResource.SP_RES_LANGUAGE, StringParam.class);
		resourceMetaAndParams.put(BaseResource.SP_RES_LANGUAGE, StringAndListParam.class);
		resourceMetaParams.put(Constants.PARAM_TAG, TokenParam.class);
		resourceMetaAndParams.put(Constants.PARAM_TAG, TokenAndListParam.class);
		resourceMetaParams.put(Constants.PARAM_PROFILE, UriParam.class);
		resourceMetaAndParams.put(Constants.PARAM_PROFILE, UriAndListParam.class);
		resourceMetaParams.put(Constants.PARAM_SECURITY, TokenParam.class);
		resourceMetaAndParams.put(Constants.PARAM_SECURITY, TokenAndListParam.class);
		RESOURCE_META_PARAMS = Collections.unmodifiableMap(resourceMetaParams);
		RESOURCE_META_AND_PARAMS = Collections.unmodifiableMap(resourceMetaAndParams);
	}

	public static final long INDEX_STATUS_INDEXED = Long.valueOf(1L);
	public static final long INDEX_STATUS_INDEXING_FAILED = Long.valueOf(2L);
	public static final String NS_JPA_PROFILE = "https://github.com/jamesagnew/hapi-fhir/ns/jpa/profile";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseHapiFhirDao.class);

	private static final Map<FhirVersionEnum, FhirContext> ourRetrievalContexts = new HashMap<FhirVersionEnum, FhirContext>();
	public static final String UCUM_NS = "http://unitsofmeasure.org";

	@Autowired(required = true)
	private DaoConfig myConfig;

	private FhirContext myContext;

	// @PersistenceContext(name = "FHIR_UT", type = PersistenceContextType.TRANSACTION, unitName = "FHIR_UT")
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager myEntityManager;

	private List<IDaoListener> myListeners = new ArrayList<IDaoListener>();
	@Autowired
	private PlatformTransactionManager myPlatformTransactionManager;

	@Autowired
	private List<IFhirResourceDao<?>> myResourceDaos;

	private Map<Class<? extends IBaseResource>, IFhirResourceDao<?>> myResourceTypeToDao;

	private ISearchParamExtractor mySearchParamExtractor;

	protected void createForcedIdIfNeeded(ResourceTable entity, IIdType id) {
		if (id.isEmpty() == false && id.hasIdPart()) {
			if (isValidPid(id)) {
				return;
			}
			ForcedId fid = new ForcedId();
			fid.setForcedId(id.getIdPart());
			fid.setResource(entity);
			entity.setForcedId(fid);
		}
	}

	InstantDt createHistoryToTimestamp() {
		// final InstantDt end = new InstantDt(DateUtils.addSeconds(DateUtils.truncate(new Date(), Calendar.SECOND),
		// -1));
		return InstantDt.withCurrentTime();
	}

	protected Set<ResourceLink> extractResourceLinks(ResourceTable theEntity, IResource theResource) {
		Set<ResourceLink> retVal = new HashSet<ResourceLink>();

		RuntimeResourceDefinition def = getContext().getResourceDefinition(theResource);
		for (RuntimeSearchParam nextSpDef : def.getSearchParams()) {

			if (nextSpDef.getParamType() != RestSearchParameterTypeEnum.REFERENCE) {
				continue;
			}

			String nextPathsUnsplit = nextSpDef.getPath();
			if (isBlank(nextPathsUnsplit)) {
				continue;
			}

			boolean multiType = false;
			if (nextPathsUnsplit.endsWith("[x]")) {
				multiType = true;
			}

			String[] nextPathsSplit = nextPathsUnsplit.split("\\|");
			for (String nextPath : nextPathsSplit) {
				nextPath = nextPath.trim();

				List<Class<? extends IBaseResource>> allowedTypesInField = null;
				for (Object nextObject : extractValues(nextPath, theResource)) {
					if (nextObject == null) {
						continue;
					}

					ResourceLink nextEntity;
					if (nextObject instanceof BaseResourceReferenceDt) {
						BaseResourceReferenceDt nextValue = (BaseResourceReferenceDt) nextObject;
						if (nextValue.isEmpty()) {
							continue;
						}
						if (nextValue.getReference().isEmpty() || nextValue.getReference().getValue().startsWith("#")) {
							// This is a blank or contained resource reference
							continue;
						}

						String typeString = nextValue.getReference().getResourceType();
						if (isBlank(typeString)) {
							throw new InvalidRequestException("Invalid resource reference found at path[" + nextPathsUnsplit + "] - Does not contain resource type - " + nextValue.getReference().getValue());
						}
						RuntimeResourceDefinition resourceDefinition;
						try {
							resourceDefinition = getContext().getResourceDefinition(typeString);
						} catch (DataFormatException e) {
							throw new InvalidRequestException(
									"Invalid resource reference found at path[" + nextPathsUnsplit + "] - Resource type is unknown or not supported on this server - " + nextValue.getReference().getValue());
						}

						Class<? extends IBaseResource> type = resourceDefinition.getImplementingClass();
						String id = nextValue.getReference().getIdPart();
						if (StringUtils.isBlank(id)) {
							throw new InvalidRequestException("Invalid resource reference found at path[" + nextPathsUnsplit + "] - Does not contain resource ID - " + nextValue.getReference().getValue());
						}

						IFhirResourceDao<?> dao = getDao(type);
						if (dao == null) {
							StringBuilder b = new StringBuilder();
							b.append("This server (version ");
							b.append(myContext.getVersion().getVersion());
							b.append(") is not able to handle resources of type[");
							b.append(nextValue.getReference().getResourceType());
							b.append("] - Valid resource types for this server: ");
							b.append(myResourceTypeToDao.keySet().toString());

							throw new InvalidRequestException(b.toString());
						}
						Long valueOf;
						try {
							valueOf = translateForcedIdToPid(nextValue.getReference());
						} catch (ResourceNotFoundException e) {
							String resName = getContext().getResourceDefinition(type).getName();
							throw new InvalidRequestException("Resource " + resName + "/" + id + " not found, specified in path: " + nextPathsUnsplit);
						}
						ResourceTable target = myEntityManager.find(ResourceTable.class, valueOf);
						RuntimeResourceDefinition targetResourceDef = getContext().getResourceDefinition(type);
						if (target == null) {
							String resName = targetResourceDef.getName();
							throw new InvalidRequestException("Resource " + resName + "/" + id + " not found, specified in path: " + nextPathsUnsplit);
						}

						if (!typeString.equals(target.getResourceType())) {
							throw new UnprocessableEntityException("Resource contains reference to " + nextValue.getReference().getValue() + " but resource with ID " + nextValue.getReference().getIdPart()
									+ " is actually of type " + target.getResourceType());
						}

						/*
						 * Is the target type an allowable type of resource for the path where it is referenced?
						 */

						if (allowedTypesInField == null) {
							BaseRuntimeChildDefinition childDef = getContext().newTerser().getDefinition(theResource.getClass(), nextPath);
							if (childDef instanceof RuntimeChildResourceDefinition) {
								RuntimeChildResourceDefinition resRefDef = (RuntimeChildResourceDefinition) childDef;
								allowedTypesInField = resRefDef.getResourceTypes();
							} else {
								allowedTypesInField = new ArrayList<Class<? extends IBaseResource>>();
								allowedTypesInField.add(IBaseResource.class);
							}
						}

						boolean acceptableLink = false;
						for (Class<? extends IBaseResource> next : allowedTypesInField) {
							if (next.isAssignableFrom(targetResourceDef.getImplementingClass())) {
								acceptableLink = true;
								break;
							}
						}

						if (!acceptableLink) {
							throw new UnprocessableEntityException("Invalid reference found at path '" + nextPath + "'. Resource type '" + targetResourceDef.getName() + "' is not valid for this path");
						}

						nextEntity = new ResourceLink(nextPath, theEntity, target);
					} else {
						if (!multiType) {
							throw new ConfigurationException("Search param " + nextSpDef.getName() + " is of unexpected datatype: " + nextObject.getClass());
						} else {
							continue;
						}
					}
					if (nextEntity != null) {
						retVal.add(nextEntity);
					}
				}
			}
		}

		theEntity.setHasLinks(retVal.size() > 0);

		return retVal;
	}

	protected Set<ResourceIndexedSearchParamDate> extractSearchParamDates(ResourceTable theEntity, IResource theResource) {
		return mySearchParamExtractor.extractSearchParamDates(theEntity, theResource);
	}

	protected Set<ResourceIndexedSearchParamNumber> extractSearchParamNumber(ResourceTable theEntity, IResource theResource) {
		return mySearchParamExtractor.extractSearchParamNumber(theEntity, theResource);
	}

	protected Set<ResourceIndexedSearchParamUri> extractSearchParamUri(ResourceTable theEntity, IResource theResource) {
		return mySearchParamExtractor.extractSearchParamUri(theEntity, theResource);
	}

	protected Set<ResourceIndexedSearchParamCoords> extractSearchParamCoords(ResourceTable theEntity, IResource theResource) {
		return mySearchParamExtractor.extractSearchParamCoords(theEntity, theResource);
	}

	protected Set<ResourceIndexedSearchParamQuantity> extractSearchParamQuantity(ResourceTable theEntity, IResource theResource) {
		return mySearchParamExtractor.extractSearchParamQuantity(theEntity, theResource);
	}

	protected Set<ResourceIndexedSearchParamString> extractSearchParamStrings(ResourceTable theEntity, IResource theResource) {
		return mySearchParamExtractor.extractSearchParamStrings(theEntity, theResource);
	}

	protected Set<BaseResourceIndexedSearchParam> extractSearchParamTokens(ResourceTable theEntity, IResource theResource) {
		return mySearchParamExtractor.extractSearchParamTokens(theEntity, theResource);
	}

	private List<Object> extractValues(String thePath, IResource theResource) {
		List<Object> values = new ArrayList<Object>();
		FhirTerser t = getContext().newTerser();
		String nextPathTrimmed = thePath.trim();
		try {
			values.addAll(t.getValues(theResource, nextPathTrimmed));
		} catch (Exception e) {
			RuntimeResourceDefinition def = myContext.getResourceDefinition(theResource);
			ourLog.warn("Failed to index values from path[{}] in resource type[{}]: ", new Object[] { nextPathTrimmed, def.getName(), e.toString() });
		}
		return values;
	}

	private void findMatchingTagIds(String theResourceName, IIdType theResourceId, Set<Long> tagIds, Class<? extends BaseTag> entityClass) {
		{
			CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
			CriteriaQuery<Tuple> cq = builder.createTupleQuery();
			Root<? extends BaseTag> from = cq.from(entityClass);
			cq.multiselect(from.get("myTagId").as(Long.class)).distinct(true);

			if (theResourceName != null) {
				Predicate typePredicate = builder.equal(from.get("myResourceType"), theResourceName);
				if (theResourceId != null) {
					cq.where(typePredicate, builder.equal(from.get("myResourceId"), translateForcedIdToPid(theResourceId)));
				} else {
					cq.where(typePredicate);
				}
			}

			TypedQuery<Tuple> query = myEntityManager.createQuery(cq);
			for (Tuple next : query.getResultList()) {
				tagIds.add(next.get(0, Long.class));
			}
		}
	}

	protected void notifyInterceptors(RestOperationTypeEnum operationType, ActionRequestDetails requestDetails) {
		if (requestDetails.getId() != null && requestDetails.getId().hasResourceType() && isNotBlank(requestDetails.getResourceType())) {
			if (requestDetails.getId().getResourceType().equals(requestDetails.getResourceType()) == false) {
				throw new InternalErrorException("Inconsistent server state - Resource types don't match: " + requestDetails.getId().getResourceType() + " / " + requestDetails.getResourceType());
			}
		}
		List<IServerInterceptor> interceptors = getConfig().getInterceptors();
		if (interceptors == null) {
			return;
		}
		for (IServerInterceptor next : interceptors) {
			next.incomingRequestPreHandled(operationType, requestDetails);
		}
	}

	protected DaoConfig getConfig() {
		return myConfig;
	}

	public FhirContext getContext() {
		return myContext;
	}

	public FhirContext getContext(FhirVersionEnum theVersion) {
		FhirVersionEnum ver = theVersion != null ? theVersion : FhirVersionEnum.DSTU1;
		synchronized (ourRetrievalContexts) {
			FhirContext retVal = ourRetrievalContexts.get(ver);
			if (retVal == null) {
				retVal = new FhirContext(ver);
				ourRetrievalContexts.put(ver, retVal);
			}
			return retVal;
		}
	}

	@SuppressWarnings("unchecked")
	protected <R extends IBaseResource> IFhirResourceDao<R> getDao(Class<R> theType) {
		if (myResourceTypeToDao == null) {
			myResourceTypeToDao = new HashMap<Class<? extends IBaseResource>, IFhirResourceDao<?>>();
			for (IFhirResourceDao<?> next : myResourceDaos) {
				myResourceTypeToDao.put(next.getResourceType(), next);
			}

			if (this instanceof IFhirResourceDao<?>) {
				IFhirResourceDao<?> thiz = (IFhirResourceDao<?>) this;
				myResourceTypeToDao.put(thiz.getResourceType(), thiz);
			}

		}

		return (IFhirResourceDao<R>) myResourceTypeToDao.get(theType);
	}

	protected TagDefinition getTag(TagTypeEnum theTagType, String theScheme, String theTerm, String theLabel) {
		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<TagDefinition> cq = builder.createQuery(TagDefinition.class);
		Root<TagDefinition> from = cq.from(TagDefinition.class);

		//@formatter:off
		if (isNotBlank(theScheme)) {
			cq.where(
				builder.and(
					builder.equal(from.get("myTagType"), theTagType), 
					builder.equal(from.get("mySystem"), theScheme), 
					builder.equal(from.get("myCode"), theTerm))
				);
		} else {
			cq.where(
				builder.and(
					builder.equal(from.get("myTagType"), theTagType), 
					builder.isNull(from.get("mySystem")), 
					builder.equal(from.get("myCode"), theTerm))
				);
		}
		//@formatter:on

		TypedQuery<TagDefinition> q = myEntityManager.createQuery(cq);
		try {
			return q.getSingleResult();
		} catch (NoResultException e) {
			TagDefinition retVal = new TagDefinition(theTagType, theScheme, theTerm, theLabel);
			myEntityManager.persist(retVal);
			return retVal;
		}
	}

	protected TagList getTags(Class<? extends IResource> theResourceType, IIdType theResourceId) {
		String resourceName = null;
		if (theResourceType != null) {
			resourceName = toResourceName(theResourceType);
			if (theResourceId != null && theResourceId.hasVersionIdPart()) {
				IFhirResourceDao<? extends IResource> dao = getDao(theResourceType);
				BaseHasResource entity = dao.readEntity(theResourceId);
				TagList retVal = new TagList();
				for (BaseTag next : entity.getTags()) {
					retVal.add(next.getTag().toTag());
				}
				return retVal;
			}
		}

		Set<Long> tagIds = new HashSet<Long>();
		findMatchingTagIds(resourceName, theResourceId, tagIds, ResourceTag.class);
		findMatchingTagIds(resourceName, theResourceId, tagIds, ResourceHistoryTag.class);
		if (tagIds.isEmpty()) {
			return new TagList();
		}
		{
			CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
			CriteriaQuery<TagDefinition> cq = builder.createQuery(TagDefinition.class);
			Root<TagDefinition> from = cq.from(TagDefinition.class);
			cq.where(from.get("myId").in(tagIds));
			cq.orderBy(builder.asc(from.get("mySystem")), builder.asc(from.get("myCode")));
			TypedQuery<TagDefinition> q = myEntityManager.createQuery(cq);
			q.setMaxResults(getConfig().getHardTagListLimit());

			TagList retVal = new TagList();
			for (TagDefinition next : q.getResultList()) {
				retVal.add(next.toTag());
			}

			return retVal;
		}
	}

	protected IBundleProvider history(String theResourceName, Long theId, Date theSince) {
		final List<HistoryTuple> tuples = new ArrayList<HistoryTuple>();

		final InstantDt end = createHistoryToTimestamp();

		StopWatch timer = new StopWatch();

		int limit = 10000;

		// Get list of IDs
		searchHistoryCurrentVersion(theResourceName, theId, theSince, end.getValue(), limit, tuples);
		ourLog.info("Retrieved {} history IDs from current versions in {} ms", tuples.size(), timer.getMillisAndRestart());

		searchHistoryHistory(theResourceName, theId, theSince, end.getValue(), limit, tuples);
		ourLog.info("Retrieved {} history IDs from previous versions in {} ms", tuples.size(), timer.getMillisAndRestart());

		// Sort merged list
		Collections.sort(tuples, Collections.reverseOrder());
		assert tuples.size() < 2 || !tuples.get(tuples.size() - 2).getUpdated().before(tuples.get(tuples.size() - 1).getUpdated()) : tuples.toString();

		return new IBundleProvider() {

			@Override
			public InstantDt getPublished() {
				return end;
			}

			@Override
			public List<IBaseResource> getResources(final int theFromIndex, final int theToIndex) {
				final StopWatch timer = new StopWatch();
				TransactionTemplate template = new TransactionTemplate(myPlatformTransactionManager);
				return template.execute(new TransactionCallback<List<IBaseResource>>() {
					@Override
					public List<IBaseResource> doInTransaction(TransactionStatus theStatus) {
						List<BaseHasResource> resEntities = Lists.newArrayList();

						List<HistoryTuple> tupleSubList = tuples.subList(theFromIndex, theToIndex);
						searchHistoryCurrentVersion(tupleSubList, resEntities);
						ourLog.info("Loaded history from current versions in {} ms", timer.getMillisAndRestart());

						searchHistoryHistory(tupleSubList, resEntities);
						ourLog.info("Loaded history from previous versions in {} ms", timer.getMillisAndRestart());

						Collections.sort(resEntities, new Comparator<BaseHasResource>() {
							@Override
							public int compare(BaseHasResource theO1, BaseHasResource theO2) {
								return theO2.getUpdated().getValue().compareTo(theO1.getUpdated().getValue());
							}
						});

						int limit = theToIndex - theFromIndex;
						if (resEntities.size() > limit) {
							resEntities = resEntities.subList(0, limit);
						}

						ArrayList<IBaseResource> retVal = new ArrayList<IBaseResource>();
						for (BaseHasResource next : resEntities) {
							RuntimeResourceDefinition type;
							try {
								type = myContext.getResourceDefinition(next.getResourceType());
							} catch (DataFormatException e) {
								if (next.getFhirVersion() != getContext().getVersion().getVersion()) {
									ourLog.info("Ignoring history resource of type[{}] because it is not compatible with version[{}]", next.getResourceType(), getContext().getVersion().getVersion());
									continue;
								}
								throw e;
							}
							IResource resource = (IResource) toResource(type.getImplementingClass(), next, true);
							retVal.add(resource);
						}
						return retVal;
					}
				});
			}

			@Override
			public int size() {
				return tuples.size();
			}

			@Override
			public Integer preferredPageSize() {
				return null;
			}
		};
	}

	protected boolean isValidPid(IIdType theId) {
		if (theId == null || theId.getIdPart() == null) {
			return false;
		}
		String idPart = theId.getIdPart();
		for (int i = 0; i < idPart.length(); i++) {
			char nextChar = idPart.charAt(i);
			if (nextChar < '0' || nextChar > '9') {
				return false;
			}
		}
		return true;
	}

	protected void notifyWriteCompleted() {
		for (IDaoListener next : myListeners) {
			next.writeCompleted();
		}
	}

	protected void populateResourceIntoEntity(IResource theResource, ResourceTable theEntity) {
		theEntity.setResourceType(toResourceName(theResource));

		List<BaseResourceReferenceDt> refs = myContext.newTerser().getAllPopulatedChildElementsOfType(theResource, BaseResourceReferenceDt.class);
		for (BaseResourceReferenceDt nextRef : refs) {
			if (nextRef.getReference().isEmpty() == false) {
				if (nextRef.getReference().hasVersionIdPart()) {
					nextRef.setReference(nextRef.getReference().toUnqualifiedVersionless());
				}
			}
		}

		String encoded = myConfig.getResourceEncoding().newParser(myContext).encodeResourceToString(theResource);
		ResourceEncodingEnum encoding = myConfig.getResourceEncoding();
		theEntity.setEncoding(encoding);
		theEntity.setFhirVersion(myContext.getVersion().getVersion());
		try {
			switch (encoding) {
			case JSON:
				theEntity.setResource(encoded.getBytes("UTF-8"));
				break;
			case JSONC:
				theEntity.setResource(GZipUtil.compress(encoded));
				break;
			}
		} catch (UnsupportedEncodingException e) {
			throw new InternalErrorException(e);
		}

		Set<TagDefinition> allDefs = new HashSet<TagDefinition>();

		TagList tagList = ResourceMetadataKeyEnum.TAG_LIST.get(theResource);
		if (tagList != null) {
			for (Tag next : tagList) {
				TagDefinition tag = getTag(TagTypeEnum.TAG, next.getScheme(), next.getTerm(), next.getLabel());
				allDefs.add(tag);
				theEntity.addTag(tag);
				theEntity.setHasTags(true);
			}
		}

		List<BaseCodingDt> securityLabels = ResourceMetadataKeyEnum.SECURITY_LABELS.get(theResource);
		if (securityLabels != null) {
			for (BaseCodingDt next : securityLabels) {
				TagDefinition tag = getTag(TagTypeEnum.SECURITY_LABEL, next.getSystemElement().getValue(), next.getCodeElement().getValue(), next.getDisplayElement().getValue());
				allDefs.add(tag);
				theEntity.addTag(tag);
				theEntity.setHasTags(true);
			}
		}

		List<IdDt> profiles = ResourceMetadataKeyEnum.PROFILES.get(theResource);
		if (profiles != null) {
			for (IIdType next : profiles) {
				TagDefinition tag = getTag(TagTypeEnum.PROFILE, NS_JPA_PROFILE, next.getValue(), null);
				allDefs.add(tag);
				theEntity.addTag(tag);
				theEntity.setHasTags(true);
			}
		}

		for (ResourceTag next : new ArrayList<ResourceTag>(theEntity.getTags())) {
			TagDefinition nextDef = next.getTag();
			if (!allDefs.contains(nextDef)) {
				if (shouldDroppedTagBeRemovedOnUpdate(theEntity, next)) {
					theEntity.getTags().remove(next);
				}
			}
		}
		if (theEntity.getTags().size() == 0) {
			theEntity.setHasTags(false);
		}

		String title = ResourceMetadataKeyEnum.TITLE.get(theResource);
		if (title != null && title.length() > BaseHasResource.MAX_TITLE_LENGTH) {
			title = title.substring(0, BaseHasResource.MAX_TITLE_LENGTH);
		}
		theEntity.setTitle(title);

	}

	/**
	 * This method is called when an update to an existing resource detects that the resource supplied for update is missing a tag/profile/security label that the currently persisted resource holds.
	 * <p>
	 * The default implementation removes any profile declarations, but leaves tags and security labels in place. Subclasses may choose to override and change this behaviour.
	 * </p>
	 * 
	 * @param theEntity
	 *           The entity being updated (Do not modify the entity! Undefined behaviour will occur!)
	 * @param theTag
	 *           The tag
	 * @return Retturns <code>true</code> if the tag should be removed
	 * @see <a href="http://hl7.org/fhir/2015Sep/resource.html#1.11.3.7">Updates to Tags, Profiles, and Security Labels</a> for a description of the logic that the default behaviour folows.
	 */
	protected boolean shouldDroppedTagBeRemovedOnUpdate(ResourceTable theEntity, ResourceTag theTag) {
		if (theTag.getTag().getTagType() == TagTypeEnum.PROFILE) {
			return true;
		}
		return false;
	}

	@CoverageIgnore
	protected static IQueryParameterAnd<?> newInstanceAnd(String chain) {
		IQueryParameterAnd<?> type;
		Class<? extends IQueryParameterAnd<?>> clazz = RESOURCE_META_AND_PARAMS.get(chain);
		try {
			type = clazz.newInstance();
		} catch (Exception e) {
			throw new InternalErrorException("Failure creating instance of " + clazz, e);
		}
		return type;
	}

	@CoverageIgnore
	protected static IQueryParameterType newInstanceType(String chain) {
		IQueryParameterType type;
		Class<? extends IQueryParameterType> clazz = RESOURCE_META_PARAMS.get(chain);
		try {
			type = clazz.newInstance();
		} catch (Exception e) {
			throw new InternalErrorException("Failure creating instance of " + clazz, e);
		}
		return type;
	}

	protected <R extends IResource> Set<Long> processMatchUrl(String theMatchUrl, Class<R> theResourceType) {
		RuntimeResourceDefinition resourceDef = getContext().getResourceDefinition(theResourceType);

		SearchParameterMap paramMap = translateMatchUrl(theMatchUrl, resourceDef);
		
		if (paramMap.isEmpty()) {
			throw new InvalidRequestException("Invalid match URL[" + theMatchUrl + "] - URL has no search parameters");
		}

		IFhirResourceDao<R> dao = getDao(theResourceType);
		Set<Long> ids = dao.searchForIdsWithAndOr(paramMap);

		return ids;
	}

	public static SearchParameterMap translateMatchUrl(String theMatchUrl, RuntimeResourceDefinition resourceDef) {
		SearchParameterMap paramMap = new SearchParameterMap();
		List<NameValuePair> parameters;
		String matchUrl = theMatchUrl;
		int questionMarkIndex = matchUrl.indexOf('?');
		if (questionMarkIndex != -1) {
			matchUrl = matchUrl.substring(questionMarkIndex + 1);
		}
		matchUrl = matchUrl.replace("|", "%7C");
		matchUrl = matchUrl.replace("=>=", "=%3E%3D");
		matchUrl = matchUrl.replace("=<=", "=%3C%3D");
		matchUrl = matchUrl.replace("=>", "=%3E");
		matchUrl = matchUrl.replace("=<", "=%3C");
		if (matchUrl.contains(" ")) {
			throw new InvalidRequestException("Failed to parse match URL[" + theMatchUrl + "] - URL is invalid (must not contain spaces)");
		}

		parameters = URLEncodedUtils.parse((matchUrl), Constants.CHARSET_UTF8, '&');

		ArrayListMultimap<String, QualifiedParamList> nameToParamLists = ArrayListMultimap.create();
		for (NameValuePair next : parameters) {
			if (isBlank(next.getValue())) {
				continue;
			}

			String paramName = next.getName();
			String qualifier = null;
			for (int i = 0; i < paramName.length(); i++) {
				switch (paramName.charAt(i)) {
				case '.':
				case ':':
					qualifier = paramName.substring(i);
					paramName = paramName.substring(0, i);
					i = Integer.MAX_VALUE - 1;
					break;
				}
			}

			QualifiedParamList paramList = QualifiedParamList.splitQueryStringByCommasIgnoreEscape(qualifier, next.getValue());
			nameToParamLists.put(paramName, paramList);
		}

		for (String nextParamName : nameToParamLists.keySet()) {
			List<QualifiedParamList> paramList = nameToParamLists.get(nextParamName);
			if (Constants.PARAM_LASTUPDATED.equals(nextParamName)) {
				if (paramList != null && paramList.size() > 0) {
					if (paramList.size() > 2) {
						throw new InvalidRequestException("Failed to parse match URL[" + theMatchUrl + "] - Can not have more than 2 " + Constants.PARAM_LASTUPDATED + " parameter repetitions");
					} else {
						DateRangeParam p1 = new DateRangeParam();
						p1.setValuesAsQueryTokens(paramList);
						paramMap.setLastUpdated(p1);
					}
				}
				continue;
			}

			if (Constants.PARAM_COUNT.equals(nextParamName)) {
				if (paramList.size() > 0 && paramList.get(0).size() > 0) {
					String intString = paramList.get(0).get(0);
					try {
						paramMap.setCount(Integer.parseInt(intString));
					} catch (NumberFormatException e) {
						throw new InvalidRequestException("Invalid " + Constants.PARAM_COUNT + " value: " + intString);
					}
				}
				continue;
			}

			if (RESOURCE_META_PARAMS.containsKey(nextParamName)) {
				if (isNotBlank(paramList.get(0).getQualifier()) && paramList.get(0).getQualifier().startsWith(".")) {
					throw new InvalidRequestException("Invalid parameter chain: " + nextParamName + paramList.get(0).getQualifier()); 
				}
				IQueryParameterAnd<?> type = newInstanceAnd(nextParamName);
				type.setValuesAsQueryTokens((paramList));
				paramMap.add(nextParamName, type);
			} else if (nextParamName.startsWith("_")) {
				// ignore these since they aren't search params (e.g. _sort)
			} else {
				RuntimeSearchParam paramDef = resourceDef.getSearchParam(nextParamName);
				if (paramDef == null) {
					throw new InvalidRequestException("Failed to parse match URL[" + theMatchUrl + "] - Resource type " + resourceDef.getName() + " does not have a parameter with name: " + nextParamName);
				}
	
				IQueryParameterAnd<?> param = MethodUtil.parseQueryParams(paramDef, nextParamName, paramList);
				paramMap.add(nextParamName, param);
			}
		}
		return paramMap;
	}

	@Override
	public void registerDaoListener(IDaoListener theListener) {
		Validate.notNull(theListener, "theListener");
		myListeners.add(theListener);
	}

	private void searchHistoryCurrentVersion(List<HistoryTuple> theTuples, List<BaseHasResource> theRetVal) {
		Collection<HistoryTuple> tuples = Collections2.filter(theTuples, new com.google.common.base.Predicate<HistoryTuple>() {
			@Override
			public boolean apply(HistoryTuple theInput) {
				return theInput.isHistory() == false;
			}
		});
		Collection<Long> ids = Collections2.transform(tuples, new Function<HistoryTuple, Long>() {
			@Override
			public Long apply(HistoryTuple theInput) {
				return theInput.getId();
			}
		});
		if (ids.isEmpty()) {
			return;
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<ResourceTable> cq = builder.createQuery(ResourceTable.class);
		Root<ResourceTable> from = cq.from(ResourceTable.class);
		cq.where(from.get("myId").in(ids));

		cq.orderBy(builder.desc(from.get("myUpdated")));
		TypedQuery<ResourceTable> q = myEntityManager.createQuery(cq);
		for (ResourceTable next : q.getResultList()) {
			theRetVal.add(next);
		}
	}

	private void searchHistoryCurrentVersion(String theResourceName, Long theId, Date theSince, Date theEnd, Integer theLimit, List<HistoryTuple> tuples) {
		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = builder.createTupleQuery();
		Root<?> from = cq.from(ResourceTable.class);
		cq.multiselect(from.get("myId").as(Long.class), from.get("myUpdated").as(Date.class));

		List<Predicate> predicates = new ArrayList<Predicate>();
		if (theSince != null) {
			Predicate low = builder.greaterThanOrEqualTo(from.<Date> get("myUpdated"), theSince);
			predicates.add(low);
		}

		Predicate high = builder.lessThan(from.<Date> get("myUpdated"), theEnd);
		predicates.add(high);

		if (theResourceName != null) {
			predicates.add(builder.equal(from.get("myResourceType"), theResourceName));
		}
		if (theId != null) {
			predicates.add(builder.equal(from.get("myId"), theId));
		}

		cq.where(builder.and(predicates.toArray(new Predicate[0])));

		cq.orderBy(builder.desc(from.get("myUpdated")));
		TypedQuery<Tuple> q = myEntityManager.createQuery(cq);
		if (theLimit != null && theLimit < myConfig.getHardSearchLimit()) {
			q.setMaxResults(theLimit);
		} else {
			q.setMaxResults(myConfig.getHardSearchLimit());
		}
		for (Tuple next : q.getResultList()) {
			long id = next.get(0, Long.class);
			Date updated = next.get(1, Date.class);
			tuples.add(new HistoryTuple(false, updated, id));
		}
	}

	private void searchHistoryHistory(List<HistoryTuple> theTuples, List<BaseHasResource> theRetVal) {
		Collection<HistoryTuple> tuples = Collections2.filter(theTuples, new com.google.common.base.Predicate<HistoryTuple>() {
			@Override
			public boolean apply(HistoryTuple theInput) {
				return theInput.isHistory() == true;
			}
		});
		Collection<Long> ids = Collections2.transform(tuples, new Function<HistoryTuple, Long>() {
			@Override
			public Long apply(HistoryTuple theInput) {
				return (Long) theInput.getId();
			}
		});
		if (ids.isEmpty()) {
			return;
		}

		ourLog.info("Retrieving {} history elements from ResourceHistoryTable", ids.size());

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<ResourceHistoryTable> cq = builder.createQuery(ResourceHistoryTable.class);
		Root<ResourceHistoryTable> from = cq.from(ResourceHistoryTable.class);
		cq.where(from.get("myId").in(ids));

		cq.orderBy(builder.desc(from.get("myUpdated")));
		TypedQuery<ResourceHistoryTable> q = myEntityManager.createQuery(cq);
		for (ResourceHistoryTable next : q.getResultList()) {
			theRetVal.add(next);
		}
	}

	private void searchHistoryHistory(String theResourceName, Long theResourceId, Date theSince, Date theEnd, Integer theLimit, List<HistoryTuple> tuples) {
		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = builder.createTupleQuery();
		Root<?> from = cq.from(ResourceHistoryTable.class);
		cq.multiselect(from.get("myId").as(Long.class), from.get("myUpdated").as(Date.class));

		List<Predicate> predicates = new ArrayList<Predicate>();
		if (theSince != null) {
			Predicate low = builder.greaterThanOrEqualTo(from.<Date> get("myUpdated"), theSince);
			predicates.add(low);
		}

		Predicate high = builder.lessThan(from.<Date> get("myUpdated"), theEnd);
		predicates.add(high);

		if (theResourceName != null) {
			predicates.add(builder.equal(from.get("myResourceType"), theResourceName));
		}
		if (theResourceId != null) {
			predicates.add(builder.equal(from.get("myResourceId"), theResourceId));
		}

		cq.where(builder.and(predicates.toArray(new Predicate[0])));

		cq.orderBy(builder.desc(from.get("myUpdated")));
		TypedQuery<Tuple> q = myEntityManager.createQuery(cq);
		if (theLimit != null && theLimit < myConfig.getHardSearchLimit()) {
			q.setMaxResults(theLimit);
		} else {
			q.setMaxResults(myConfig.getHardSearchLimit());
		}
		for (Tuple next : q.getResultList()) {
			Long id = next.get(0, Long.class);
			Date updated = (Date) next.get(1);
			tuples.add(new HistoryTuple(true, updated, id));
		}
	}

	protected MetaDt toMetaDt(Collection<TagDefinition> tagDefinitions) {
		MetaDt retVal = new MetaDt();
		for (TagDefinition next : tagDefinitions) {
			switch (next.getTagType()) {
			case PROFILE:
				retVal.addProfile(next.getCode());
				break;
			case SECURITY_LABEL:
				retVal.addSecurity().setSystem(next.getSystem()).setCode(next.getCode()).setDisplay(next.getDisplay());
				break;
			case TAG:
				retVal.addTag().setSystem(next.getSystem()).setCode(next.getCode()).setDisplay(next.getDisplay());
				break;
			}
		}
		return retVal;
	}

	public void setContext(FhirContext theContext) {
		myContext = theContext;
		switch (myContext.getVersion().getVersion()) {
		case DSTU2:
			mySearchParamExtractor = new SearchParamExtractorDstu2(theContext);
			break;
		case DSTU1:
			mySearchParamExtractor = new SearchParamExtractorDstu1(theContext);
			break;
		case DSTU2_HL7ORG:
		case DEV:
			throw new IllegalStateException("Don't know how to handle version: " + myContext.getVersion().getVersion());
		}
	}

	protected ResourceTable toEntity(IResource theResource) {
		ResourceTable retVal = new ResourceTable();

		populateResourceIntoEntity(theResource, retVal);

		return retVal;
	}

	protected IBaseResource toResource(BaseHasResource theEntity, boolean theForHistoryOperation) {
		RuntimeResourceDefinition type = myContext.getResourceDefinition(theEntity.getResourceType());
		return toResource(type.getImplementingClass(), theEntity, theForHistoryOperation);
	}

	@SuppressWarnings("unchecked")
	protected <R extends IBaseResource> R toResource(Class<R> theResourceType, BaseHasResource theEntity, boolean theForHistoryOperation) {
		String resourceText = null;
		switch (theEntity.getEncoding()) {
		case JSON:
			try {
				resourceText = new String(theEntity.getResource(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new Error("Should not happen", e);
			}
			break;
		case JSONC:
			resourceText = GZipUtil.decompress(theEntity.getResource());
			break;
		}

		IParser parser = theEntity.getEncoding().newParser(getContext(theEntity.getFhirVersion()));
		R retVal;
		try {
			retVal = parser.parseResource(theResourceType, resourceText);
		} catch (Exception e) {
			StringBuilder b = new StringBuilder();
			b.append("Failed to parse database resource[");
			b.append(theResourceType);
			b.append("/");
			b.append(theEntity.getIdDt().getIdPart());
			b.append(" (pid ");
			b.append(theEntity.getId());
			b.append(", version ");
			b.append(myContext.getVersion().getVersion());
			b.append("): ");
			b.append(e.getMessage());
			String msg = b.toString();
			ourLog.error(msg, e);
			throw new DataFormatException(msg, e);
		}

		IResource res = (IResource) retVal;
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

		res.setId(theEntity.getIdDt());

		ResourceMetadataKeyEnum.VERSION.put(res, Long.toString(theEntity.getVersion()));
		ResourceMetadataKeyEnum.PUBLISHED.put(res, theEntity.getPublished());
		ResourceMetadataKeyEnum.UPDATED.put(res, theEntity.getUpdated());
		IDao.RESOURCE_PID.put(res, theEntity.getId());

		if (theEntity.getTitle() != null) {
			ResourceMetadataKeyEnum.TITLE.put(res, theEntity.getTitle());
		}

		Collection<? extends BaseTag> tags = theEntity.getTags();
		if (theEntity.isHasTags()) {
			TagList tagList = new TagList();
			List<BaseCodingDt> securityLabels = new ArrayList<BaseCodingDt>();
			List<IdDt> profiles = new ArrayList<IdDt>();
			for (BaseTag next : tags) {
				switch (next.getTag().getTagType()) {
				case PROFILE:
					profiles.add(new IdDt(next.getTag().getCode()));
					break;
				case SECURITY_LABEL:
					BaseCodingDt secLabel = myContext.getVersion().newCodingDt();
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
				ResourceMetadataKeyEnum.SECURITY_LABELS.put(res, securityLabels);
			}
			if (profiles.size() > 0) {
				ResourceMetadataKeyEnum.PROFILES.put(res, profiles);
			}
		}

		return retVal;
	}

	protected String toResourceName(Class<? extends IResource> theResourceType) {
		return myContext.getResourceDefinition(theResourceType).getName();
	}

	protected String toResourceName(IResource theResource) {
		return myContext.getResourceDefinition(theResource).getName();
	}

	protected Long translateForcedIdToPid(IIdType theId) {
		if (isValidPid(theId)) {
			return theId.getIdPartAsLong();
		} else {
			TypedQuery<ForcedId> q = myEntityManager.createNamedQuery("Q_GET_FORCED_ID", ForcedId.class);
			q.setParameter("ID", theId.getIdPart());
			try {
				return q.getSingleResult().getResourcePid();
			} catch (NoResultException e) {
				throw new ResourceNotFoundException(theId);
			}
		}
	}

	protected String translatePidIdToForcedId(Long theId) {
		ForcedId forcedId = myEntityManager.find(ForcedId.class, theId);
		if (forcedId != null) {
			return forcedId.getForcedId();
		} else {
			return theId.toString();
		}
	}

	protected ResourceTable updateEntity(final IResource theResource, ResourceTable entity, boolean theUpdateHistory, Date theDeletedTimestampOrNull, Date theUpdateTime) {
		return updateEntity(theResource, entity, theUpdateHistory, theDeletedTimestampOrNull, true, true, theUpdateTime);
	}

	@SuppressWarnings("unchecked")
	protected ResourceTable updateEntity(final IResource theResource, ResourceTable theEntity, boolean theUpdateHistory, Date theDeletedTimestampOrNull, boolean thePerformIndexing,
			boolean theUpdateVersion, Date theUpdateTime) {

		/*
		 * This should be the very first thing..
		 */
		if (theResource != null) {
			validateResourceForStorage((T) theResource, theEntity);
			String resourceType = myContext.getResourceDefinition(theResource).getName();
			if (isNotBlank(theEntity.getResourceType()) && !theEntity.getResourceType().equals(resourceType)) {
				throw new UnprocessableEntityException(
						"Existing resource ID[" + theEntity.getIdDt().toUnqualifiedVersionless() + "] is of type[" + theEntity.getResourceType() + "] - Cannot update with [" + resourceType + "]");
			}
		}

		if (theEntity.getPublished() == null) {
			theEntity.setPublished(theUpdateTime);
		}

		if (theUpdateHistory) {
			final ResourceHistoryTable historyEntry = theEntity.toHistory();
			myEntityManager.persist(historyEntry);
		}

		if (theUpdateVersion) {
			theEntity.setVersion(theEntity.getVersion() + 1);
		}

		boolean paramsStringPopulated = theEntity.isParamsStringPopulated();
		boolean paramsTokenPopulated = theEntity.isParamsTokenPopulated();
		boolean paramsNumberPopulated = theEntity.isParamsNumberPopulated();
		boolean paramsQuantityPopulated = theEntity.isParamsQuantityPopulated();
		boolean paramsDatePopulated = theEntity.isParamsDatePopulated();
		boolean paramsCoordsPopulated = theEntity.isParamsCoordsPopulated();
		boolean paramsUriPopulated = theEntity.isParamsUriPopulated();
		boolean hasLinks = theEntity.isHasLinks();

		Collection<ResourceIndexedSearchParamString> paramsString = new ArrayList<ResourceIndexedSearchParamString>(theEntity.getParamsString());
		Collection<ResourceIndexedSearchParamToken> paramsToken = new ArrayList<ResourceIndexedSearchParamToken>(theEntity.getParamsToken());
		Collection<ResourceIndexedSearchParamNumber> paramsNumber = new ArrayList<ResourceIndexedSearchParamNumber>(theEntity.getParamsNumber());
		Collection<ResourceIndexedSearchParamQuantity> paramsQuantity = new ArrayList<ResourceIndexedSearchParamQuantity>(theEntity.getParamsQuantity());
		Collection<ResourceIndexedSearchParamDate> paramsDate = new ArrayList<ResourceIndexedSearchParamDate>(theEntity.getParamsDate());
		Collection<ResourceIndexedSearchParamUri> paramsUri = new ArrayList<ResourceIndexedSearchParamUri>(theEntity.getParamsUri());
		Collection<ResourceIndexedSearchParamCoords> paramsCoords = new ArrayList<ResourceIndexedSearchParamCoords>(theEntity.getParamsCoords());
		Collection<ResourceLink> resourceLinks = new ArrayList<ResourceLink>(theEntity.getResourceLinks());

		Set<ResourceIndexedSearchParamString> stringParams = null;
		Set<ResourceIndexedSearchParamToken> tokenParams = null;
		Set<ResourceIndexedSearchParamNumber> numberParams = null;
		Set<ResourceIndexedSearchParamQuantity> quantityParams = null;
		Set<ResourceIndexedSearchParamDate> dateParams = null;
		Set<ResourceIndexedSearchParamUri> uriParams = null;
		Set<ResourceIndexedSearchParamCoords> coordsParams = null;
		Set<ResourceLink> links = null;

		if (theDeletedTimestampOrNull != null) {

			stringParams = Collections.emptySet();
			tokenParams = Collections.emptySet();
			numberParams = Collections.emptySet();
			quantityParams = Collections.emptySet();
			dateParams = Collections.emptySet();
			uriParams = Collections.emptySet();
			coordsParams = Collections.emptySet();
			links = Collections.emptySet();
			theEntity.setDeleted(theDeletedTimestampOrNull);
			theEntity.setUpdated(theDeletedTimestampOrNull);

		} else {

			theEntity.setDeleted(null);

			if (thePerformIndexing) {

				stringParams = extractSearchParamStrings(theEntity, theResource);
				numberParams = extractSearchParamNumber(theEntity, theResource);
				quantityParams = extractSearchParamQuantity(theEntity, theResource);
				dateParams = extractSearchParamDates(theEntity, theResource);
				uriParams = extractSearchParamUri(theEntity, theResource);
				coordsParams = extractSearchParamCoords(theEntity, theResource);

				// ourLog.info("Indexing resource: {}", entity.getId());
				ourLog.trace("Storing string indexes: {}", stringParams);

				tokenParams = new HashSet<ResourceIndexedSearchParamToken>();
				for (BaseResourceIndexedSearchParam next : extractSearchParamTokens(theEntity, theResource)) {
					if (next instanceof ResourceIndexedSearchParamToken) {
						tokenParams.add((ResourceIndexedSearchParamToken) next);
					} else {
						stringParams.add((ResourceIndexedSearchParamString) next);
					}
				}

				links = extractResourceLinks(theEntity, theResource);
				populateResourceIntoEntity(theResource, theEntity);

				theEntity.setUpdated(theUpdateTime);
				theEntity.setLanguage(theResource.getLanguage().getValue());
				theEntity.setParamsString(stringParams);
				theEntity.setParamsStringPopulated(stringParams.isEmpty() == false);
				theEntity.setParamsToken(tokenParams);
				theEntity.setParamsTokenPopulated(tokenParams.isEmpty() == false);
				theEntity.setParamsNumber(numberParams);
				theEntity.setParamsNumberPopulated(numberParams.isEmpty() == false);
				theEntity.setParamsQuantity(quantityParams);
				theEntity.setParamsQuantityPopulated(quantityParams.isEmpty() == false);
				theEntity.setParamsDate(dateParams);
				theEntity.setParamsDatePopulated(dateParams.isEmpty() == false);
				theEntity.setParamsUri(uriParams);
				theEntity.setParamsUriPopulated(uriParams.isEmpty() == false);
				theEntity.setParamsCoords(coordsParams);
				theEntity.setParamsCoordsPopulated(coordsParams.isEmpty() == false);
				theEntity.setResourceLinks(links);
				theEntity.setHasLinks(links.isEmpty() == false);
				theEntity.setIndexStatus(INDEX_STATUS_INDEXED);

			} else {

				populateResourceIntoEntity(theResource, theEntity);
				theEntity.setUpdated(theUpdateTime);
				theEntity.setLanguage(theResource.getLanguage().getValue());
				theEntity.setIndexStatus(null);

			}

		}

		if (theEntity.getId() == null) {
			myEntityManager.persist(theEntity);

			if (theEntity.getForcedId() != null) {
				myEntityManager.persist(theEntity.getForcedId());
			}

			postPersist(theEntity, (T) theResource);

		} else {
			theEntity = myEntityManager.merge(theEntity);

			postUpdate(theEntity, (T) theResource);
		}

		if (thePerformIndexing) {

			if (paramsStringPopulated) {
				for (ResourceIndexedSearchParamString next : paramsString) {
					myEntityManager.remove(next);
				}
			}
			for (ResourceIndexedSearchParamString next : stringParams) {
				myEntityManager.persist(next);
			}

			if (paramsTokenPopulated) {
				for (ResourceIndexedSearchParamToken next : paramsToken) {
					myEntityManager.remove(next);
				}
			}
			for (ResourceIndexedSearchParamToken next : tokenParams) {
				myEntityManager.persist(next);
			}

			if (paramsNumberPopulated) {
				for (ResourceIndexedSearchParamNumber next : paramsNumber) {
					myEntityManager.remove(next);
				}
			}
			for (ResourceIndexedSearchParamNumber next : numberParams) {
				myEntityManager.persist(next);
			}

			if (paramsQuantityPopulated) {
				for (ResourceIndexedSearchParamQuantity next : paramsQuantity) {
					myEntityManager.remove(next);
				}
			}
			for (ResourceIndexedSearchParamQuantity next : quantityParams) {
				myEntityManager.persist(next);
			}

			// Store date SP's
			if (paramsDatePopulated) {
				for (ResourceIndexedSearchParamDate next : paramsDate) {
					myEntityManager.remove(next);
				}
			}
			for (ResourceIndexedSearchParamDate next : dateParams) {
				myEntityManager.persist(next);
			}

			// Store URI SP's
			if (paramsUriPopulated) {
				for (ResourceIndexedSearchParamUri next : paramsUri) {
					myEntityManager.remove(next);
				}
			}
			for (ResourceIndexedSearchParamUri next : uriParams) {
				myEntityManager.persist(next);
			}

			// Store Coords SP's
			if (paramsCoordsPopulated) {
				for (ResourceIndexedSearchParamCoords next : paramsCoords) {
					myEntityManager.remove(next);
				}
			}
			for (ResourceIndexedSearchParamCoords next : coordsParams) {
				myEntityManager.persist(next);
			}

			if (hasLinks) {
				for (ResourceLink next : resourceLinks) {
					myEntityManager.remove(next);
				}
			}
			for (ResourceLink next : links) {
				myEntityManager.persist(next);
			}

		} // if thePerformIndexing

		myEntityManager.flush();

		if (theResource != null) {
			theResource.setId(theEntity.getIdDt());
		}

		return theEntity;
	}

	/**
	 * Subclasses may override to provide behaviour. Called when a resource has been inserved into the database for the first time.
	 * 
	 * @param theEntity
	 *           The resource
	 * @param theResource
	 *           The resource being persisted
	 */
	protected void postUpdate(ResourceTable theEntity, T theResource) {
		// nothing
	}

	/**
	 * Subclasses may override to provide behaviour. Called when a resource has been inserved into the database for the first time.
	 * 
	 * @param theEntity
	 *           The resource
	 * @param theResource
	 *           The resource being persisted
	 */
	protected void postPersist(ResourceTable theEntity, T theResource) {
		// nothing
	}

	/**
	 * This method is invoked immediately before storing a new resource, or an update to an existing resource to allow the DAO to ensure that it is valid for persistence. By default, checks for the
	 * "subsetted" tag and rejects resources which have it. Subclasses should call the superclass implementation to preserve this check.
	 * 
	 * @param theResource
	 *           The resource that is about to be persisted
	 * @param theEntityToSave
	 *           TODO
	 */
	protected void validateResourceForStorage(T theResource, ResourceTable theEntityToSave) {
		IResource res = (IResource) theResource;
		TagList tagList = ResourceMetadataKeyEnum.TAG_LIST.get(res);
		if (tagList != null) {
			Tag tag = tagList.getTag(Constants.TAG_SUBSETTED_SYSTEM, Constants.TAG_SUBSETTED_CODE);
			if (tag != null) {
				throw new UnprocessableEntityException("Resource contains the 'subsetted' tag, and must not be stored as it may contain a subset of available data");
			}
		}
	}

	protected static String normalizeString(String theString) {
		char[] out = new char[theString.length()];
		theString = Normalizer.normalize(theString, Normalizer.Form.NFD);
		int j = 0;
		for (int i = 0, n = theString.length(); i < n; ++i) {
			char c = theString.charAt(i);
			if (c <= '\u007F') {
				out[j++] = c;
			}
		}
		return new String(out).toUpperCase();
	}

}
