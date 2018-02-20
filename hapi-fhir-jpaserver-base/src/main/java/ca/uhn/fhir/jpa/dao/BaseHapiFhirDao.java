package ca.uhn.fhir.jpa.dao;

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
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.jpa.dao.data.*;
import ca.uhn.fhir.jpa.entity.*;
import ca.uhn.fhir.jpa.search.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.search.JpaRuntimeSearchParam;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProvider;
import ca.uhn.fhir.jpa.sp.ISearchParamPresenceSvc;
import ca.uhn.fhir.jpa.term.IHapiTerminologySvc;
import ca.uhn.fhir.jpa.util.DeleteConflict;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.valueset.BundleEntryTransactionMethodEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.LenientErrorHandler;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.rest.server.exceptions.*;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.*;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Sets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.r4.model.BaseResource;
import org.hl7.fhir.r4.model.Bundle.HTTPVerb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import java.io.CharArrayWriter;
import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.util.*;
import java.util.Map.Entry;

import static org.apache.commons.lang3.StringUtils.*;

public abstract class BaseHapiFhirDao<T extends IBaseResource> implements IDao {

	public static final long INDEX_STATUS_INDEXED = Long.valueOf(1L);
	public static final long INDEX_STATUS_INDEXING_FAILED = Long.valueOf(2L);
	public static final String NS_JPA_PROFILE = "https://github.com/jamesagnew/hapi-fhir/ns/jpa/profile";
	public static final String OO_SEVERITY_ERROR = "error";
	public static final String OO_SEVERITY_INFO = "information";
	public static final String OO_SEVERITY_WARN = "warning";
	public static final String UCUM_NS = "http://unitsofmeasure.org";
	static final Set<String> EXCLUDE_ELEMENTS_IN_ENCODED;
	/**
	 * These are parameters which are supported by {@link BaseHapiFhirResourceDao#searchForIds(SearchParameterMap)}
	 */
	static final Map<String, Class<? extends IQueryParameterAnd<?>>> RESOURCE_META_AND_PARAMS;
	/**
	 * These are parameters which are supported by {@link BaseHapiFhirResourceDao#searchForIds(SearchParameterMap)}
	 */
	static final Map<String, Class<? extends IQueryParameterType>> RESOURCE_META_PARAMS;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseHapiFhirDao.class);
	private static final Map<FhirVersionEnum, FhirContext> ourRetrievalContexts = new HashMap<FhirVersionEnum, FhirContext>();
	private static final String PROCESSING_SUB_REQUEST = "BaseHapiFhirDao.processingSubRequest";
	private static boolean ourValidationDisabledForUnitTest;

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

		HashSet<String> excludeElementsInEncoded = new HashSet<String>();
		excludeElementsInEncoded.add("id");
		excludeElementsInEncoded.add("meta");
		EXCLUDE_ELEMENTS_IN_ENCODED = Collections.unmodifiableSet(excludeElementsInEncoded);
	}

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;
	@Autowired
	protected IForcedIdDao myForcedIdDao;
	@Autowired(required = false)
	protected IFulltextSearchSvc myFulltextSearchSvc;
	@Autowired()
	protected IResourceIndexedSearchParamUriDao myResourceIndexedSearchParamUriDao;
	@Autowired
	protected ISearchCoordinatorSvc mySearchCoordinatorSvc;
	@Autowired
	protected ISearchParamRegistry mySerarchParamRegistry;
	@Autowired()
	protected IHapiTerminologySvc myTerminologySvc;
	@Autowired(required = true)
	private DaoConfig myConfig;
	private FhirContext myContext;
	@Autowired
	private PlatformTransactionManager myPlatformTransactionManager;
	@Autowired
	private List<IFhirResourceDao<?>> myResourceDaos;
	@Autowired
	private IResourceHistoryTableDao myResourceHistoryTableDao;
	private Map<Class<? extends IBaseResource>, IFhirResourceDao<?>> myResourceTypeToDao;
	@Autowired
	private ISearchDao mySearchDao;
	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;
	@Autowired
	private ISearchParamPresenceSvc mySearchParamPresenceSvc;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	private ISearchResultDao mySearchResultDao;
	@Autowired
	private IResourceIndexedCompositeStringUniqueDao myResourceIndexedCompositeStringUniqueDao;

	protected void clearRequestAsProcessingSubRequest(ServletRequestDetails theRequestDetails) {
		if (theRequestDetails != null) {
			theRequestDetails.getUserData().remove(PROCESSING_SUB_REQUEST);
		}
	}

	protected void createForcedIdIfNeeded(ResourceTable theEntity, IIdType theId) {
		if (theId.isEmpty() == false && theId.hasIdPart()) {
			if (isValidPid(theId)) {
				return;
			}

			ForcedId fid = new ForcedId();
			fid.setResourceType(theEntity.getResourceType());
			fid.setForcedId(theId.getIdPart());
			fid.setResource(theEntity);
			theEntity.setForcedId(fid);
		}
	}

	private Set<ResourceIndexedCompositeStringUnique> extractCompositeStringUniques(ResourceTable theEntity, Set<ResourceIndexedSearchParamString> theStringParams, Set<ResourceIndexedSearchParamToken> theTokenParams, Set<ResourceIndexedSearchParamNumber> theNumberParams, Set<ResourceIndexedSearchParamQuantity> theQuantityParams, Set<ResourceIndexedSearchParamDate> theDateParams, Set<ResourceIndexedSearchParamUri> theUriParams, Set<ResourceLink> theLinks) {
		Set<ResourceIndexedCompositeStringUnique> compositeStringUniques;
		compositeStringUniques = new HashSet<>();
		List<JpaRuntimeSearchParam> uniqueSearchParams = mySearchParamRegistry.getActiveUniqueSearchParams(theEntity.getResourceType());
		for (JpaRuntimeSearchParam next : uniqueSearchParams) {

			List<List<String>> partsChoices = new ArrayList<>();

			for (RuntimeSearchParam nextCompositeOf : next.getCompositeOf()) {
				Set<? extends BaseResourceIndexedSearchParam> paramsListForCompositePart = null;
				Set<ResourceLink> linksForCompositePart = null;
				Set<String> linksForCompositePartWantPaths = null;
				switch (nextCompositeOf.getParamType()) {
					case NUMBER:
						paramsListForCompositePart = theNumberParams;
						break;
					case DATE:
						paramsListForCompositePart = theDateParams;
						break;
					case STRING:
						paramsListForCompositePart = theStringParams;
						break;
					case TOKEN:
						paramsListForCompositePart = theTokenParams;
						break;
					case REFERENCE:
						linksForCompositePart = theLinks;
						linksForCompositePartWantPaths = new HashSet<>();
						linksForCompositePartWantPaths.addAll(nextCompositeOf.getPathsSplit());
						break;
					case QUANTITY:
						paramsListForCompositePart = theQuantityParams;
						break;
					case URI:
						paramsListForCompositePart = theUriParams;
						break;
					case COMPOSITE:
					case HAS:
						break;
				}

				ArrayList<String> nextChoicesList = new ArrayList<>();
				partsChoices.add(nextChoicesList);

				String key = UrlUtil.escapeUrlParam(nextCompositeOf.getName());
				if (paramsListForCompositePart != null) {
					for (BaseResourceIndexedSearchParam nextParam : paramsListForCompositePart) {
						if (nextParam.getParamName().equals(nextCompositeOf.getName())) {
							IQueryParameterType nextParamAsClientParam = nextParam.toQueryParameterType();
							String value = nextParamAsClientParam.getValueAsQueryToken(getContext());
							if (isNotBlank(value)) {
								value = UrlUtil.escapeUrlParam(value);
								nextChoicesList.add(key + "=" + value);
							}
						}
					}
				}
				if (linksForCompositePart != null) {
					for (ResourceLink nextLink : linksForCompositePart) {
						if (linksForCompositePartWantPaths.contains(nextLink.getSourcePath())) {
							String value = nextLink.getTargetResource().getIdDt().toUnqualifiedVersionless().getValue();
							if (isNotBlank(value)) {
								value = UrlUtil.escapeUrlParam(value);
								nextChoicesList.add(key + "=" + value);
							}
						}
					}
				}
			}

			Set<String> queryStringsToPopulate = extractCompositeStringUniquesValueChains(theEntity.getResourceType(), partsChoices);

			for (String nextQueryString : queryStringsToPopulate) {
				if (isNotBlank(nextQueryString)) {
					compositeStringUniques.add(new ResourceIndexedCompositeStringUnique(theEntity, nextQueryString));
				}
			}
		}

		return compositeStringUniques;
	}

	/**
	 * @return Returns a set containing all of the parameter names that
	 * were found to have a value
	 */
	@SuppressWarnings("unchecked")
	protected Set<String> extractResourceLinks(ResourceTable theEntity, IBaseResource theResource, Set<ResourceLink> theLinks, Date theUpdateTime) {
		HashSet<String> retVal = new HashSet<>();

		/*
		 * For now we don't try to load any of the links in a bundle if it's the actual bundle we're storing..
		 */
		if (theResource instanceof IBaseBundle) {
			return Collections.emptySet();
		}

		Map<String, RuntimeSearchParam> searchParams = mySearchParamRegistry.getActiveSearchParams(toResourceName(theResource.getClass()));
		for (RuntimeSearchParam nextSpDef : searchParams.values()) {

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

			List<PathAndRef> refs = mySearchParamExtractor.extractResourceLinks(theResource, nextSpDef);
			for (PathAndRef nextPathAndRef : refs) {
				Object nextObject = nextPathAndRef.getRef();

				/*
				 * A search parameter on an extension field that contains
				 * references should index those references
				 */
				if (nextObject instanceof IBaseExtension<?, ?>) {
					nextObject = ((IBaseExtension<?, ?>) nextObject).getValue();
				}

				IIdType nextId;
				if (nextObject instanceof IBaseReference) {
					IBaseReference nextValue = (IBaseReference) nextObject;
					if (nextValue.isEmpty()) {
						continue;
					}
					nextId = nextValue.getReferenceElement();

					/*
					 * This can only really happen if the DAO is being called
					 * programatically with a Bundle (not through the FHIR REST API)
					 * but Smile does this
					 */
					if (nextId.isEmpty() && nextValue.getResource() != null) {
						nextId = nextValue.getResource().getIdElement();
					}

					if (nextId.isEmpty() || nextId.getValue().startsWith("#")) {
						// This is a blank or contained resource reference
						continue;
					}
				} else if (nextObject instanceof IBaseResource) {
					nextId = ((IBaseResource) nextObject).getIdElement();
					if (nextId == null || nextId.hasIdPart() == false) {
						continue;
					}
				} else if (myContext.getElementDefinition((Class<? extends IBase>) nextObject.getClass()).getName().equals("uri")) {
					continue;
				} else {
					if (!multiType) {
						if (nextSpDef.getName().equals("sourceuri")) {
							continue; // TODO: disable this eventually - ConceptMap:sourceuri is of type reference but points to a URI
						}
						throw new ConfigurationException("Search param " + nextSpDef.getName() + " is of unexpected datatype: " + nextObject.getClass());
					} else {
						continue;
					}
				}

				retVal.add(nextSpDef.getName());

				if (isLogicalReference(nextId)) {
					ResourceLink resourceLink = new ResourceLink(nextPathAndRef.getPath(), theEntity, nextId, theUpdateTime);
					if (theLinks.add(resourceLink)) {
						ourLog.debug("Indexing remote resource reference URL: {}", nextId);
					}
					continue;
				}

				String baseUrl = nextId.getBaseUrl();
				String typeString = nextId.getResourceType();
				if (isBlank(typeString)) {
					throw new InvalidRequestException("Invalid resource reference found at path[" + nextPathsUnsplit + "] - Does not contain resource type - " + nextId.getValue());
				}
				RuntimeResourceDefinition resourceDefinition;
				try {
					resourceDefinition = getContext().getResourceDefinition(typeString);
				} catch (DataFormatException e) {
					throw new InvalidRequestException(
						"Invalid resource reference found at path[" + nextPathsUnsplit + "] - Resource type is unknown or not supported on this server - " + nextId.getValue());
				}

				if (isNotBlank(baseUrl)) {
					if (!getConfig().getTreatBaseUrlsAsLocal().contains(baseUrl) && !getConfig().isAllowExternalReferences()) {
						String msg = getContext().getLocalizer().getMessage(BaseHapiFhirDao.class, "externalReferenceNotAllowed", nextId.getValue());
						throw new InvalidRequestException(msg);
					} else {
						ResourceLink resourceLink = new ResourceLink(nextPathAndRef.getPath(), theEntity, nextId, theUpdateTime);
						if (theLinks.add(resourceLink)) {
							ourLog.info("Indexing remote resource reference URL: {}", nextId);
						}
						continue;
					}
				}

				Class<? extends IBaseResource> type = resourceDefinition.getImplementingClass();
				String id = nextId.getIdPart();
				if (StringUtils.isBlank(id)) {
					throw new InvalidRequestException("Invalid resource reference found at path[" + nextPathsUnsplit + "] - Does not contain resource ID - " + nextId.getValue());
				}

				IFhirResourceDao<?> dao = getDao(type);
				if (dao == null) {
					StringBuilder b = new StringBuilder();
					b.append("This server (version ");
					b.append(myContext.getVersion().getVersion());
					b.append(") is not able to handle resources of type[");
					b.append(nextId.getResourceType());
					b.append("] - Valid resource types for this server: ");
					b.append(myResourceTypeToDao.keySet().toString());

					throw new InvalidRequestException(b.toString());
				}
				Long valueOf;
				try {
					valueOf = translateForcedIdToPid(typeString, id);
				} catch (ResourceNotFoundException e) {
					if (myConfig.isEnforceReferentialIntegrityOnWrite() == false) {
						continue;
					}
					RuntimeResourceDefinition missingResourceDef = getContext().getResourceDefinition(type);
					String resName = missingResourceDef.getName();

					if (getConfig().isAutoCreatePlaceholderReferenceTargets()) {
						IBaseResource newResource = missingResourceDef.newInstance();
						newResource.setId(resName + "/" + id);
						IFhirResourceDao<IBaseResource> placeholderResourceDao = (IFhirResourceDao<IBaseResource>) getDao(newResource.getClass());
						ourLog.info("Automatically creating empty placeholder resource: {}", newResource.getIdElement().getValue());
						valueOf = placeholderResourceDao.update(newResource).getEntity().getId();
					} else {
						throw new InvalidRequestException("Resource " + resName + "/" + id + " not found, specified in path: " + nextPathsUnsplit);
					}
				}
				ResourceTable target = myEntityManager.find(ResourceTable.class, valueOf);
				RuntimeResourceDefinition targetResourceDef = getContext().getResourceDefinition(type);
				if (target == null) {
					String resName = targetResourceDef.getName();
					throw new InvalidRequestException("Resource " + resName + "/" + id + " not found, specified in path: " + nextPathsUnsplit);
				}

				if (!typeString.equals(target.getResourceType())) {
					throw new UnprocessableEntityException(
						"Resource contains reference to " + nextId.getValue() + " but resource with ID " + nextId.getIdPart() + " is actually of type " + target.getResourceType());
				}

				if (target.getDeleted() != null) {
					String resName = targetResourceDef.getName();
					throw new InvalidRequestException("Resource " + resName + "/" + id + " is deleted, specified in path: " + nextPathsUnsplit);
				}

				if (nextSpDef.getTargets() != null && !nextSpDef.getTargets().contains(typeString)) {
					continue;
				}

				ResourceLink resourceLink = new ResourceLink(nextPathAndRef.getPath(), theEntity, target, theUpdateTime);
				theLinks.add(resourceLink);
			}

		}

		theEntity.setHasLinks(theLinks.size() > 0);

		return retVal;
	}

	protected Set<ResourceIndexedSearchParamCoords> extractSearchParamCoords(ResourceTable theEntity, IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamCoords(theEntity, theResource);
	}

	protected Set<ResourceIndexedSearchParamDate> extractSearchParamDates(ResourceTable theEntity, IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamDates(theEntity, theResource);
	}

	protected Set<ResourceIndexedSearchParamNumber> extractSearchParamNumber(ResourceTable theEntity, IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamNumber(theEntity, theResource);
	}

	protected Set<ResourceIndexedSearchParamQuantity> extractSearchParamQuantity(ResourceTable theEntity, IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamQuantity(theEntity, theResource);
	}

	protected Set<ResourceIndexedSearchParamString> extractSearchParamStrings(ResourceTable theEntity, IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamStrings(theEntity, theResource);
	}

	protected Set<BaseResourceIndexedSearchParam> extractSearchParamTokens(ResourceTable theEntity, IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamTokens(theEntity, theResource);
	}

	protected Set<ResourceIndexedSearchParamUri> extractSearchParamUri(ResourceTable theEntity, IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamUri(theEntity, theResource);
	}

	private void extractTagsHapi(IResource theResource, ResourceTable theEntity, Set<TagDefinition> allDefs) {
		TagList tagList = ResourceMetadataKeyEnum.TAG_LIST.get(theResource);
		if (tagList != null) {
			for (Tag next : tagList) {
				TagDefinition tag = getTagOrNull(TagTypeEnum.TAG, next.getScheme(), next.getTerm(), next.getLabel());
				if (tag != null) {
					allDefs.add(tag);
					theEntity.addTag(tag);
					theEntity.setHasTags(true);
				}
			}
		}

		List<BaseCodingDt> securityLabels = ResourceMetadataKeyEnum.SECURITY_LABELS.get(theResource);
		if (securityLabels != null) {
			for (BaseCodingDt next : securityLabels) {
				TagDefinition tag = getTagOrNull(TagTypeEnum.SECURITY_LABEL, next.getSystemElement().getValue(), next.getCodeElement().getValue(), next.getDisplayElement().getValue());
				if (tag != null) {
					allDefs.add(tag);
					theEntity.addTag(tag);
					theEntity.setHasTags(true);
				}
			}
		}

		List<IdDt> profiles = ResourceMetadataKeyEnum.PROFILES.get(theResource);
		if (profiles != null) {
			for (IIdType next : profiles) {
				TagDefinition tag = getTagOrNull(TagTypeEnum.PROFILE, NS_JPA_PROFILE, next.getValue(), null);
				if (tag != null) {
					allDefs.add(tag);
					theEntity.addTag(tag);
					theEntity.setHasTags(true);
				}
			}
		}
	}

	private void extractTagsRi(IAnyResource theResource, ResourceTable theEntity, Set<TagDefinition> allDefs) {
		List<? extends IBaseCoding> tagList = theResource.getMeta().getTag();
		if (tagList != null) {
			for (IBaseCoding next : tagList) {
				TagDefinition tag = getTagOrNull(TagTypeEnum.TAG, next.getSystem(), next.getCode(), next.getDisplay());
				if (tag != null) {
					allDefs.add(tag);
					theEntity.addTag(tag);
					theEntity.setHasTags(true);
				}
			}
		}

		List<? extends IBaseCoding> securityLabels = theResource.getMeta().getSecurity();
		if (securityLabels != null) {
			for (IBaseCoding next : securityLabels) {
				TagDefinition tag = getTagOrNull(TagTypeEnum.SECURITY_LABEL, next.getSystem(), next.getCode(), next.getDisplay());
				if (tag != null) {
					allDefs.add(tag);
					theEntity.addTag(tag);
					theEntity.setHasTags(true);
				}
			}
		}

		List<? extends IPrimitiveType<String>> profiles = theResource.getMeta().getProfile();
		if (profiles != null) {
			for (IPrimitiveType<String> next : profiles) {
				TagDefinition tag = getTagOrNull(TagTypeEnum.PROFILE, NS_JPA_PROFILE, next.getValue(), null);
				if (tag != null) {
					allDefs.add(tag);
					theEntity.addTag(tag);
					theEntity.setHasTags(true);
				}
			}
		}
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
					cq.where(typePredicate, builder.equal(from.get("myResourceId"), translateForcedIdToPid(theResourceName, theResourceId.getIdPart())));
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

	@SuppressWarnings("unchecked")
	private <RT extends BaseResourceIndexedSearchParam> void findMissingSearchParams(ResourceTable theEntity, Set<Entry<String, RuntimeSearchParam>> activeSearchParams, RestSearchParameterTypeEnum type,
																												Set<RT> paramCollection) {
		for (Entry<String, RuntimeSearchParam> nextEntry : activeSearchParams) {
			String nextParamName = nextEntry.getKey();
			if (nextEntry.getValue().getParamType() == type) {
				boolean haveParam = false;
				for (BaseResourceIndexedSearchParam nextParam : paramCollection) {
					if (nextParam.getParamName().equals(nextParamName)) {
						haveParam = true;
						break;
					}
				}

				if (!haveParam) {
					BaseResourceIndexedSearchParam param;
					switch (type) {
						case DATE:
							param = new ResourceIndexedSearchParamDate();
							break;
						case NUMBER:
							param = new ResourceIndexedSearchParamNumber();
							break;
						case QUANTITY:
							param = new ResourceIndexedSearchParamQuantity();
							break;
						case STRING:
							param = new ResourceIndexedSearchParamString();
							break;
						case TOKEN:
							param = new ResourceIndexedSearchParamToken();
							break;
						case URI:
							param = new ResourceIndexedSearchParamUri();
							break;
						case COMPOSITE:
						case HAS:
						case REFERENCE:
						default:
							continue;
					}
					param.setResource(theEntity);
					param.setMissing(true);
					param.setParamName(nextParamName);
					paramCollection.add((RT) param);
				}
			}
		}
	}

	private Set<TagDefinition> getAllTagDefinitions(ResourceTable theEntity) {
		HashSet<TagDefinition> retVal = Sets.newHashSet();
		for (ResourceTag next : theEntity.getTags()) {
			retVal.add(next.getTag());
		}
		return retVal;
	}

	protected DaoConfig getConfig() {
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

	@SuppressWarnings("unchecked")
	public <R extends IBaseResource> IFhirResourceDao<R> getDao(Class<R> theType) {
		if (myResourceTypeToDao == null) {
			Map<Class<? extends IBaseResource>, IFhirResourceDao<?>> theResourceTypeToDao = new HashMap<Class<? extends IBaseResource>, IFhirResourceDao<?>>();
			for (IFhirResourceDao<?> next : myResourceDaos) {
				theResourceTypeToDao.put(next.getResourceType(), next);
			}

			if (this instanceof IFhirResourceDao<?>) {
				IFhirResourceDao<?> thiz = (IFhirResourceDao<?>) this;
				theResourceTypeToDao.put(thiz.getResourceType(), thiz);
			}

			myResourceTypeToDao = theResourceTypeToDao;
		}

		IFhirResourceDao<R> dao = (IFhirResourceDao<R>) myResourceTypeToDao.get(theType);
		return dao;
	}

	public IResourceIndexedCompositeStringUniqueDao getResourceIndexedCompositeStringUniqueDao() {
		return myResourceIndexedCompositeStringUniqueDao;
	}

	@Override
	public RuntimeSearchParam getSearchParamByName(RuntimeResourceDefinition theResourceDef, String theParamName) {
		Map<String, RuntimeSearchParam> params = mySearchParamRegistry.getActiveSearchParams(theResourceDef.getName());
		return params.get(theParamName);
	}

	@Override
	public Collection<RuntimeSearchParam> getSearchParamsByResourceType(RuntimeResourceDefinition theResourceDef) {
		return mySearchParamRegistry.getActiveSearchParams(theResourceDef.getName()).values();
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

	protected TagList getTags(Class<? extends IBaseResource> theResourceType, IIdType theResourceId) {
		String resourceName = null;
		if (theResourceType != null) {
			resourceName = toResourceName(theResourceType);
			if (theResourceId != null && theResourceId.hasVersionIdPart()) {
				IFhirResourceDao<? extends IBaseResource> dao = getDao(theResourceType);
				BaseHasResource entity = dao.readEntity(theResourceId);
				TagList retVal = new TagList();
				for (BaseTag next : entity.getTags()) {
					retVal.add(next.getTag().toTag());
				}
				return retVal;
			}
		}

		Set<Long> tagIds = new HashSet<>();
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

	protected IBundleProvider history(String theResourceName, Long theId, Date theSince, Date theUntil) {

		String resourceName = defaultIfBlank(theResourceName, null);

		Search search = new Search();
		search.setCreated(new Date());
		search.setSearchLastReturned(new Date());
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

		search = mySearchDao.save(search);

		return new PersistedJpaBundleProvider(search.getUuid(), this);
	}

	@Override
	public void injectDependenciesIntoBundleProvider(PersistedJpaBundleProvider theProvider) {
		theProvider.setContext(getContext());
		theProvider.setEntityManager(myEntityManager);
		theProvider.setPlatformTransactionManager(myPlatformTransactionManager);
		theProvider.setSearchDao(mySearchDao);
		theProvider.setSearchCoordinatorSvc(mySearchCoordinatorSvc);
	}

	protected boolean isLogicalReference(IIdType theId) {
		Set<String> treatReferencesAsLogical = myConfig.getTreatReferencesAsLogical();
		if (treatReferencesAsLogical != null) {
			for (String nextLogicalRef : treatReferencesAsLogical) {
				nextLogicalRef = trim(nextLogicalRef);
				if (nextLogicalRef.charAt(nextLogicalRef.length() - 1) == '*') {
					if (theId.getValue().startsWith(nextLogicalRef.substring(0, nextLogicalRef.length() - 1))) {
						return true;
					}
				} else {
					if (theId.getValue().equals(nextLogicalRef)) {
						return true;
					}
				}
			}

		}
		return false;
	}

	protected void markRequestAsProcessingSubRequest(ServletRequestDetails theRequestDetails) {
		if (theRequestDetails != null) {
			theRequestDetails.getUserData().put(PROCESSING_SUB_REQUEST, Boolean.TRUE);
		}
	}

	@Override
	public SearchBuilder newSearchBuilder() {
		SearchBuilder builder = new SearchBuilder(getContext(), myEntityManager, myFulltextSearchSvc, this, myResourceIndexedSearchParamUriDao,
			myForcedIdDao,
			myTerminologySvc, mySerarchParamRegistry);
		return builder;
	}

	protected void notifyInterceptors(RestOperationTypeEnum theOperationType, ActionRequestDetails theRequestDetails) {
		if (theRequestDetails.getId() != null && theRequestDetails.getId().hasResourceType() && isNotBlank(theRequestDetails.getResourceType())) {
			if (theRequestDetails.getId().getResourceType().equals(theRequestDetails.getResourceType()) == false) {
				throw new InternalErrorException(
					"Inconsistent server state - Resource types don't match: " + theRequestDetails.getId().getResourceType() + " / " + theRequestDetails.getResourceType());
			}
		}

		if (theRequestDetails.getUserData().get(PROCESSING_SUB_REQUEST) == Boolean.TRUE) {
			theRequestDetails.notifyIncomingRequestPreHandled(theOperationType);
		}
		List<IServerInterceptor> interceptors = getConfig().getInterceptors();
		for (IServerInterceptor next : interceptors) {
			next.incomingRequestPreHandled(theOperationType, theRequestDetails);
		}
	}

	public String parseContentTextIntoWords(IBaseResource theResource) {
		StringBuilder retVal = new StringBuilder();
		@SuppressWarnings("rawtypes")
		List<IPrimitiveType> childElements = getContext().newTerser().getAllPopulatedChildElementsOfType(theResource, IPrimitiveType.class);
		for (@SuppressWarnings("rawtypes")
			IPrimitiveType nextType : childElements) {
			if (nextType instanceof StringDt || nextType.getClass().getSimpleName().equals("StringType")) {
				String nextValue = nextType.getValueAsString();
				if (isNotBlank(nextValue)) {
					retVal.append(nextValue.replace("\n", " ").replace("\r", " "));
					retVal.append("\n");
				}
			}
		}
		return retVal.toString();
	}

	@Override
	public void populateFullTextFields(final IBaseResource theResource, ResourceTable theEntity) {
		if (theEntity.getDeleted() != null) {
			theEntity.setNarrativeTextParsedIntoWords(null);
			theEntity.setContentTextParsedIntoWords(null);
		} else {
			theEntity.setNarrativeTextParsedIntoWords(parseNarrativeTextIntoWords(theResource));
			theEntity.setContentTextParsedIntoWords(parseContentTextIntoWords(theResource));
		}
	}

	private void populateResourceIdFromEntity(BaseHasResource theEntity, final IBaseResource theResource) {
		IIdType id = theEntity.getIdDt();
		if (getContext().getVersion().getVersion().isRi()) {
			id = getContext().getVersion().newIdType().setValue(id.getValue());
		}
		theResource.setId(id);
	}

	/**
	 * Returns true if the resource has changed (either the contents or the tags)
	 */
	protected EncodedResource populateResourceIntoEntity(IBaseResource theResource, ResourceTable theEntity, boolean theUpdateHash) {
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
			IParser parser = encoding.newParser(myContext);
			parser.setDontEncodeElements(EXCLUDE_ELEMENTS_IN_ENCODED);
			String encoded = parser.encodeResourceToString(theResource);

			theEntity.setFhirVersion(myContext.getVersion().getVersion());
			switch (encoding) {
				case JSON:
					bytes = encoded.getBytes(Charsets.UTF_8);
					break;
				default:
				case JSONC:
					bytes = GZipUtil.compress(encoded);
					break;
			}

			ourLog.debug("Encoded {} chars of resource body as {} bytes", encoded.length(), bytes.length);

			if (theUpdateHash) {
				HashFunction sha256 = Hashing.sha256();
				String hashSha256 = sha256.hashBytes(bytes).toString();
				if (hashSha256.equals(theEntity.getHashSha256()) == false) {
					changed = true;
				}
				theEntity.setHashSha256(hashSha256);
			}

			Set<TagDefinition> allDefs = new HashSet<>();

			theEntity.setHasTags(false);

			Set<TagDefinition> allTagsOld = getAllTagDefinitions(theEntity);

			if (theResource instanceof IResource) {
				extractTagsHapi((IResource) theResource, theEntity, allDefs);
			} else {
				extractTagsRi((IAnyResource) theResource, theEntity, allDefs);
			}

			RuntimeResourceDefinition def = myContext.getResourceDefinition(theResource);
			if (def.isStandardType() == false) {
				String profile = def.getResourceProfile("");
				if (isNotBlank(profile)) {
					TagDefinition tag = getTagOrNull(TagTypeEnum.PROFILE, NS_JPA_PROFILE, profile, null);
					if (tag != null) {
						allDefs.add(tag);
						theEntity.addTag(tag);
						theEntity.setHasTags(true);
					}
				}
			}

			ArrayList<ResourceTag> existingTags = new ArrayList<>();
			if (theEntity.isHasTags()) {
				existingTags.addAll(theEntity.getTags());
			}
			for (ResourceTag next : existingTags) {
				TagDefinition nextDef = next.getTag();
				if (!allDefs.contains(nextDef)) {
					if (shouldDroppedTagBeRemovedOnUpdate(theEntity, next)) {
						theEntity.getTags().remove(next);
					}
				}
			}

			Set<TagDefinition> allTagsNew = getAllTagDefinitions(theEntity);
			if (!allTagsOld.equals(allTagsNew)) {
				changed = true;
			}

		} else {
			theEntity.setHashSha256(null);
			bytes = null;
			encoding = ResourceEncodingEnum.DEL;
		}

		if (changed == false) {
			if (theEntity.getId() == null) {
				changed = true;
			} else {
				ResourceHistoryTable currentHistoryVersion = myResourceHistoryTableDao.findForIdAndVersion(theEntity.getId(), theEntity.getVersion());
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
	private <R extends IBaseResource> R populateResourceMetadataHapi(Class<R> theResourceType, BaseHasResource theEntity, boolean theForHistoryOperation, IResource res) {
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

		res.setId(theEntity.getIdDt());

		ResourceMetadataKeyEnum.VERSION.put(res, Long.toString(theEntity.getVersion()));
		ResourceMetadataKeyEnum.PUBLISHED.put(res, theEntity.getPublished());
		ResourceMetadataKeyEnum.UPDATED.put(res, theEntity.getUpdated());
		IDao.RESOURCE_PID.put(res, theEntity.getId());

		Collection<? extends BaseTag> tags = theEntity.getTags();
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
	private <R extends IBaseResource> R populateResourceMetadataRi(Class<R> theResourceType, BaseHasResource theEntity, boolean theForHistoryOperation, IAnyResource res) {
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

		populateResourceIdFromEntity(theEntity, res);

		res.getMeta().setLastUpdated(theEntity.getUpdatedDate());
		IDao.RESOURCE_PID.put(res, theEntity.getId());

		Collection<? extends BaseTag> tags = theEntity.getTags();

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

	@Override
	public <R extends IBaseResource> Set<Long> processMatchUrl(String theMatchUrl, Class<R> theResourceType) {
		RuntimeResourceDefinition resourceDef = getContext().getResourceDefinition(theResourceType);

		SearchParameterMap paramMap = translateMatchUrl(this, myContext, theMatchUrl, resourceDef);
		paramMap.setLoadSynchronous(true);

		if (paramMap.isEmpty() && paramMap.getLastUpdated() == null) {
			throw new InvalidRequestException("Invalid match URL[" + theMatchUrl + "] - URL has no search parameters");
		}

		IFhirResourceDao<R> dao = getDao(theResourceType);
		if (dao == null) {
			throw new InternalErrorException("No DAO for resource type: " + theResourceType.getName());
		}

		return dao.searchForIds(paramMap);
	}

	@CoverageIgnore
	public BaseHasResource readEntity(IIdType theValueId) {
		throw new NotImplementedException("");
	}

	private <T> Collection<T> removeCommon(Collection<T> theInput, Collection<T> theToRemove) {
		assert theInput != theToRemove;

		if (theInput.isEmpty()) {
			return theInput;
		}

		ArrayList<T> retVal = new ArrayList<>(theInput);
		retVal.removeAll(theToRemove);
		return retVal;
	}

	private void setUpdatedTime(Collection<? extends BaseResourceIndexedSearchParam> theParams, Date theUpdateTime) {
		for (BaseResourceIndexedSearchParam nextSearchParam : theParams) {
			nextSearchParam.setUpdated(theUpdateTime);
		}
	}

	/**
	 * This method is called when an update to an existing resource detects that the resource supplied for update is missing a tag/profile/security label that the currently persisted resource holds.
	 * <p>
	 * The default implementation removes any profile declarations, but leaves tags and security labels in place. Subclasses may choose to override and change this behaviour.
	 * </p>
	 * <p>
	 * See <a href="http://hl7.org/fhir/2015Sep/resource.html#1.11.3.7">Updates to Tags, Profiles, and Security Labels</a> for a description of the logic that the default behaviour folows.
	 * </p>
	 *
	 * @param theEntity The entity being updated (Do not modify the entity! Undefined behaviour will occur!)
	 * @param theTag    The tag
	 * @return Returns <code>true</code> if the tag should be removed
	 */
	protected boolean shouldDroppedTagBeRemovedOnUpdate(ResourceTable theEntity, ResourceTag theTag) {
		if (theTag.getTag().getTagType() == TagTypeEnum.PROFILE) {
			return true;
		}
		return false;
	}

	@Override
	public IBaseResource toResource(BaseHasResource theEntity, boolean theForHistoryOperation) {
		RuntimeResourceDefinition type = myContext.getResourceDefinition(theEntity.getResourceType());
		Class<? extends IBaseResource> resourceType = type.getImplementingClass();
		return toResource(resourceType, theEntity, theForHistoryOperation);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R extends IBaseResource> R toResource(Class<R> theResourceType, BaseHasResource theEntity, boolean theForHistoryOperation) {

		ResourceHistoryTable history;
		if (theEntity instanceof ResourceHistoryTable) {
			history = (ResourceHistoryTable) theEntity;
		} else {
			history = myResourceHistoryTableDao.findForIdAndVersion(theEntity.getId(), theEntity.getVersion());
		}

		if (history == null) {
			return null;
		}

		byte[] resourceBytes = history.getResource();
		ResourceEncodingEnum resourceEncoding = history.getEncoding();

		String resourceText = null;
		switch (resourceEncoding) {
			case JSON:
				try {
					resourceText = new String(resourceBytes, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					throw new Error("Should not happen", e);
				}
				break;
			case JSONC:
				resourceText = GZipUtil.decompress(resourceBytes);
				break;
			case DEL:
				break;
		}

		/*
		 * Use the appropriate custom type if one is specified in the context
		 */
		Class<R> resourceType = theResourceType;
		if (myContext.hasDefaultTypeForProfile()) {
			for (BaseTag nextTag : theEntity.getTags()) {
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

		R retVal;
		if (resourceEncoding != ResourceEncodingEnum.DEL) {
			IParser parser = resourceEncoding.newParser(getContext(theEntity.getFhirVersion()));
			parser.setParserErrorHandler(new LenientErrorHandler(false).setErrorOnInvalidValue(false));

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

		if (retVal instanceof IResource) {
			IResource res = (IResource) retVal;
			retVal = populateResourceMetadataHapi(resourceType, theEntity, theForHistoryOperation, res);
		} else {
			IAnyResource res = (IAnyResource) retVal;
			retVal = populateResourceMetadataRi(resourceType, theEntity, theForHistoryOperation, res);
		}


		return retVal;
	}

	protected String toResourceName(Class<? extends IBaseResource> theResourceType) {
		return myContext.getResourceDefinition(theResourceType).getName();
	}

	String toResourceName(IBaseResource theResource) {
		return myContext.getResourceDefinition(theResource).getName();
	}

	Long translateForcedIdToPid(String theResourceName, String theResourceId) {
		return translateForcedIdToPids(new IdDt(theResourceName, theResourceId), myForcedIdDao).get(0);
	}

	protected List<Long> translateForcedIdToPids(IIdType theId) {
		return translateForcedIdToPids(theId, myForcedIdDao);
	}

	private String translatePidIdToForcedId(String theResourceType, Long theId) {
		ForcedId forcedId = myForcedIdDao.findByResourcePid(theId);
		if (forcedId != null) {
			return forcedId.getResourceType() + '/' + forcedId.getForcedId();
		} else {
			return theResourceType + '/' + theId.toString();
		}
	}

	@SuppressWarnings("unchecked")
	protected ResourceTable updateEntity(final IBaseResource theResource, ResourceTable theEntity, Date theDeletedTimestampOrNull, boolean thePerformIndexing,
													 boolean theUpdateVersion, Date theUpdateTime, boolean theForceUpdate, boolean theCreateNewHistoryEntry) {
		ourLog.debug("Starting entity update");

		/*
		 * This should be the very first thing..
		 */
		if (theResource != null) {
			if (thePerformIndexing) {
				if (!ourValidationDisabledForUnitTest) {
					validateResourceForStorage((T) theResource, theEntity);
				}
			}
			String resourceType = myContext.getResourceDefinition(theResource).getName();
			if (isNotBlank(theEntity.getResourceType()) && !theEntity.getResourceType().equals(resourceType)) {
				throw new UnprocessableEntityException(
					"Existing resource ID[" + theEntity.getIdDt().toUnqualifiedVersionless() + "] is of type[" + theEntity.getResourceType() + "] - Cannot update with [" + resourceType + "]");
			}
		}

		if (theEntity.getPublished() == null) {
			ourLog.debug("Entity has published time: {}", new InstantDt(theUpdateTime));

			theEntity.setPublished(theUpdateTime);
		}

		Collection<ResourceIndexedSearchParamString> existingStringParams = new ArrayList<>();
		if (theEntity.isParamsStringPopulated()) {
			existingStringParams.addAll(theEntity.getParamsString());
		}
		Collection<ResourceIndexedSearchParamToken> existingTokenParams = new ArrayList<>();
		if (theEntity.isParamsTokenPopulated()) {
			existingTokenParams.addAll(theEntity.getParamsToken());
		}
		Collection<ResourceIndexedSearchParamNumber> existingNumberParams = new ArrayList<>();
		if (theEntity.isParamsNumberPopulated()) {
			existingNumberParams.addAll(theEntity.getParamsNumber());
		}
		Collection<ResourceIndexedSearchParamQuantity> existingQuantityParams = new ArrayList<>();
		if (theEntity.isParamsQuantityPopulated()) {
			existingQuantityParams.addAll(theEntity.getParamsQuantity());
		}
		Collection<ResourceIndexedSearchParamDate> existingDateParams = new ArrayList<>();
		if (theEntity.isParamsDatePopulated()) {
			existingDateParams.addAll(theEntity.getParamsDate());
		}
		Collection<ResourceIndexedSearchParamUri> existingUriParams = new ArrayList<>();
		if (theEntity.isParamsUriPopulated()) {
			existingUriParams.addAll(theEntity.getParamsUri());
		}
		Collection<ResourceIndexedSearchParamCoords> existingCoordsParams = new ArrayList<>();
		if (theEntity.isParamsCoordsPopulated()) {
			existingCoordsParams.addAll(theEntity.getParamsCoords());
		}
		Collection<ResourceLink> existingResourceLinks = new ArrayList<>();
		if (theEntity.isHasLinks()) {
			existingResourceLinks.addAll(theEntity.getResourceLinks());
		}

		Collection<ResourceIndexedCompositeStringUnique> existingCompositeStringUniques = new ArrayList<>();
		if (theEntity.isParamsCompositeStringUniquePresent()) {
			existingCompositeStringUniques.addAll(theEntity.getParamsCompositeStringUnique());
		}

		Set<ResourceIndexedSearchParamString> stringParams = null;
		Set<ResourceIndexedSearchParamToken> tokenParams = null;
		Set<ResourceIndexedSearchParamNumber> numberParams = null;
		Set<ResourceIndexedSearchParamQuantity> quantityParams = null;
		Set<ResourceIndexedSearchParamDate> dateParams = null;
		Set<ResourceIndexedSearchParamUri> uriParams = null;
		Set<ResourceIndexedSearchParamCoords> coordsParams = null;
		Set<ResourceIndexedCompositeStringUnique> compositeStringUniques = null;
		Set<ResourceLink> links = null;

		Set<String> populatedResourceLinkParameters = Collections.emptySet();
		EncodedResource changed;
		if (theDeletedTimestampOrNull != null) {

			stringParams = Collections.emptySet();
			tokenParams = Collections.emptySet();
			numberParams = Collections.emptySet();
			quantityParams = Collections.emptySet();
			dateParams = Collections.emptySet();
			uriParams = Collections.emptySet();
			coordsParams = Collections.emptySet();
			links = Collections.emptySet();
			compositeStringUniques = Collections.emptySet();

			theEntity.setDeleted(theDeletedTimestampOrNull);
			theEntity.setUpdated(theDeletedTimestampOrNull);
			theEntity.setNarrativeTextParsedIntoWords(null);
			theEntity.setContentTextParsedIntoWords(null);
			theEntity.setHashSha256(null);
			changed = populateResourceIntoEntity(theResource, theEntity, true);

		} else {

			theEntity.setDeleted(null);

			if (thePerformIndexing) {

				stringParams = extractSearchParamStrings(theEntity, theResource);
				numberParams = extractSearchParamNumber(theEntity, theResource);
				quantityParams = extractSearchParamQuantity(theEntity, theResource);
				dateParams = extractSearchParamDates(theEntity, theResource);
				uriParams = extractSearchParamUri(theEntity, theResource);
				coordsParams = extractSearchParamCoords(theEntity, theResource);

				ourLog.trace("Storing date indexes: {}", dateParams);

				tokenParams = new HashSet<>();
				for (BaseResourceIndexedSearchParam next : extractSearchParamTokens(theEntity, theResource)) {
					if (next instanceof ResourceIndexedSearchParamToken) {
						tokenParams.add((ResourceIndexedSearchParamToken) next);
					} else {
						stringParams.add((ResourceIndexedSearchParamString) next);
					}
				}

				Set<Entry<String, RuntimeSearchParam>> activeSearchParams = mySearchParamRegistry.getActiveSearchParams(theEntity.getResourceType()).entrySet();
				if (myConfig.getIndexMissingFields() == DaoConfig.IndexEnabledEnum.ENABLED) {
					findMissingSearchParams(theEntity, activeSearchParams, RestSearchParameterTypeEnum.STRING, stringParams);
					findMissingSearchParams(theEntity, activeSearchParams, RestSearchParameterTypeEnum.NUMBER, numberParams);
					findMissingSearchParams(theEntity, activeSearchParams, RestSearchParameterTypeEnum.QUANTITY, quantityParams);
					findMissingSearchParams(theEntity, activeSearchParams, RestSearchParameterTypeEnum.DATE, dateParams);
					findMissingSearchParams(theEntity, activeSearchParams, RestSearchParameterTypeEnum.URI, uriParams);
					findMissingSearchParams(theEntity, activeSearchParams, RestSearchParameterTypeEnum.TOKEN, tokenParams);
				}

				setUpdatedTime(stringParams, theUpdateTime);
				setUpdatedTime(numberParams, theUpdateTime);
				setUpdatedTime(quantityParams, theUpdateTime);
				setUpdatedTime(dateParams, theUpdateTime);
				setUpdatedTime(uriParams, theUpdateTime);
				setUpdatedTime(coordsParams, theUpdateTime);
				setUpdatedTime(tokenParams, theUpdateTime);

				/*
				 * Handle references within the resource that are match URLs, for example references like "Patient?identifier=foo". These match URLs are resolved and replaced with the ID of the
				 * matching resource.
				 */
				if (myConfig.isAllowInlineMatchUrlReferences()) {
					FhirTerser terser = getContext().newTerser();
					List<IBaseReference> allRefs = terser.getAllPopulatedChildElementsOfType(theResource, IBaseReference.class);
					for (IBaseReference nextRef : allRefs) {
						IIdType nextId = nextRef.getReferenceElement();
						String nextIdText = nextId.getValue();
						if (nextIdText == null) {
							continue;
						}
						int qmIndex = nextIdText.indexOf('?');
						if (qmIndex != -1) {
							for (int i = qmIndex - 1; i >= 0; i--) {
								if (nextIdText.charAt(i) == '/') {
									if (i < nextIdText.length() - 1 && nextIdText.charAt(i + 1) == '?') {
										// Just in case the URL is in the form Patient/?foo=bar
										continue;
									}
									nextIdText = nextIdText.substring(i + 1);
									break;
								}
							}
							String resourceTypeString = nextIdText.substring(0, nextIdText.indexOf('?')).replace("/", "");
							RuntimeResourceDefinition matchResourceDef = getContext().getResourceDefinition(resourceTypeString);
							if (matchResourceDef == null) {
								String msg = getContext().getLocalizer().getMessage(BaseHapiFhirDao.class, "invalidMatchUrlInvalidResourceType", nextId.getValue(), resourceTypeString);
								throw new InvalidRequestException(msg);
							}
							Class<? extends IBaseResource> matchResourceType = matchResourceDef.getImplementingClass();
							Set<Long> matches = processMatchUrl(nextIdText, matchResourceType);
							if (matches.isEmpty()) {
								String msg = getContext().getLocalizer().getMessage(BaseHapiFhirDao.class, "invalidMatchUrlNoMatches", nextId.getValue());
								throw new ResourceNotFoundException(msg);
							}
							if (matches.size() > 1) {
								String msg = getContext().getLocalizer().getMessage(BaseHapiFhirDao.class, "invalidMatchUrlMultipleMatches", nextId.getValue());
								throw new PreconditionFailedException(msg);
							}
							Long next = matches.iterator().next();
							String newId = translatePidIdToForcedId(resourceTypeString, next);
							ourLog.info("Replacing inline match URL[{}] with ID[{}}", nextId.getValue(), newId);
							nextRef.setReference(newId);
						}
					}
				}

				links = new HashSet<>();
				populatedResourceLinkParameters = extractResourceLinks(theEntity, theResource, links, theUpdateTime);

				/*
				 * If the existing resource already has links and those match links we still want, use them instead of removing them and re adding them
				 */
				for (Iterator<ResourceLink> existingLinkIter = existingResourceLinks.iterator(); existingLinkIter.hasNext(); ) {
					ResourceLink nextExisting = existingLinkIter.next();
					if (links.remove(nextExisting)) {
						existingLinkIter.remove();
						links.add(nextExisting);
					}
				}

				/*
				 * Handle composites
				 */
				compositeStringUniques = extractCompositeStringUniques(theEntity, stringParams, tokenParams, numberParams, quantityParams, dateParams, uriParams, links);

				changed = populateResourceIntoEntity(theResource, theEntity, true);

				theEntity.setUpdated(theUpdateTime);
				if (theResource instanceof IResource) {
					theEntity.setLanguage(((IResource) theResource).getLanguage().getValue());
				} else {
					theEntity.setLanguage(((IAnyResource) theResource).getLanguageElement().getValue());
				}
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
				theEntity.setParamsCompositeStringUniquePresent(compositeStringUniques.isEmpty() == false);
				theEntity.setResourceLinks(links);
				theEntity.setHasLinks(links.isEmpty() == false);
				theEntity.setIndexStatus(INDEX_STATUS_INDEXED);
				populateFullTextFields(theResource, theEntity);

			} else {

				changed = populateResourceIntoEntity(theResource, theEntity, false);

				theEntity.setUpdated(theUpdateTime);
				// theEntity.setLanguage(theResource.getLanguage().getValue());
				theEntity.setIndexStatus(null);

			}

		}

		if (!changed.isChanged() && !theForceUpdate && myConfig.isSuppressUpdatesWithNoChange()) {
			ourLog.info("Resource {} has not changed", theEntity.getIdDt().toUnqualified().getValue());
			if (theResource != null) {
				populateResourceIdFromEntity(theEntity, theResource);
			}
			theEntity.setUnchangedInCurrentOperation(true);
			return theEntity;
		}

		if (theUpdateVersion) {
			theEntity.setVersion(theEntity.getVersion() + 1);
		}

		/*
		 * Save the resource itself
		 */
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

		/*
		 * Create history entry
		 */
		if (theCreateNewHistoryEntry) {
			final ResourceHistoryTable historyEntry = theEntity.toHistory();
//			if (theEntity.getVersion() > 1) {
//				existing = myResourceHistoryTableDao.findForIdAndVersion(theEntity.getId(), theEntity.getVersion());
//				ourLog.warn("Reusing existing history entry entity {}", theEntity.getIdDt().getValue());
//			}
			historyEntry.setEncoding(changed.getEncoding());
			historyEntry.setResource(changed.getResource());

			ourLog.info("Saving history entry {}", historyEntry.getIdDt());
			myResourceHistoryTableDao.save(historyEntry);
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
		if (thePerformIndexing) {
			Map<String, Boolean> presentSearchParams = new HashMap<>();
			for (String nextKey : populatedResourceLinkParameters) {
				presentSearchParams.put(nextKey, Boolean.TRUE);
			}
			Set<Entry<String, RuntimeSearchParam>> activeSearchParams = mySearchParamRegistry.getActiveSearchParams(theEntity.getResourceType()).entrySet();
			for (Entry<String, RuntimeSearchParam> nextSpEntry : activeSearchParams) {
				if (nextSpEntry.getValue().getParamType() == RestSearchParameterTypeEnum.REFERENCE) {
					if (!presentSearchParams.containsKey(nextSpEntry.getKey())) {
						presentSearchParams.put(nextSpEntry.getKey(), Boolean.FALSE);
					}
				}
			}
			mySearchParamPresenceSvc.updatePresence(theEntity, presentSearchParams);
		}

		/*
		 * Indexing
		 */
		if (thePerformIndexing) {

			for (ResourceIndexedSearchParamString next : removeCommon(existingStringParams, stringParams)) {
				myEntityManager.remove(next);
				theEntity.getParamsString().remove(next);
			}
			for (ResourceIndexedSearchParamString next : removeCommon(stringParams, existingStringParams)) {
				myEntityManager.persist(next);
			}

			for (ResourceIndexedSearchParamToken next : removeCommon(existingTokenParams, tokenParams)) {
				myEntityManager.remove(next);
				theEntity.getParamsToken().remove(next);
			}
			for (ResourceIndexedSearchParamToken next : removeCommon(tokenParams, existingTokenParams)) {
				myEntityManager.persist(next);
			}

			for (ResourceIndexedSearchParamNumber next : removeCommon(existingNumberParams, numberParams)) {
				myEntityManager.remove(next);
				theEntity.getParamsNumber().remove(next);
			}
			for (ResourceIndexedSearchParamNumber next : removeCommon(numberParams, existingNumberParams)) {
				myEntityManager.persist(next);
			}

			for (ResourceIndexedSearchParamQuantity next : removeCommon(existingQuantityParams, quantityParams)) {
				myEntityManager.remove(next);
				theEntity.getParamsQuantity().remove(next);
			}
			for (ResourceIndexedSearchParamQuantity next : removeCommon(quantityParams, existingQuantityParams)) {
				myEntityManager.persist(next);
			}

			// Store date SP's
			for (ResourceIndexedSearchParamDate next : removeCommon(existingDateParams, dateParams)) {
				myEntityManager.remove(next);
				theEntity.getParamsDate().remove(next);
			}
			for (ResourceIndexedSearchParamDate next : removeCommon(dateParams, existingDateParams)) {
				myEntityManager.persist(next);
			}

			// Store URI SP's
			for (ResourceIndexedSearchParamUri next : removeCommon(existingUriParams, uriParams)) {
				myEntityManager.remove(next);
				theEntity.getParamsUri().remove(next);
			}
			for (ResourceIndexedSearchParamUri next : removeCommon(uriParams, existingUriParams)) {
				myEntityManager.persist(next);
			}

			// Store Coords SP's
			for (ResourceIndexedSearchParamCoords next : removeCommon(existingCoordsParams, coordsParams)) {
				myEntityManager.remove(next);
				theEntity.getParamsCoords().remove(next);
			}
			for (ResourceIndexedSearchParamCoords next : removeCommon(coordsParams, existingCoordsParams)) {
				myEntityManager.persist(next);
			}

			// Store resource links
			for (ResourceLink next : removeCommon(existingResourceLinks, links)) {
				myEntityManager.remove(next);
				theEntity.getResourceLinks().remove(next);
			}
			for (ResourceLink next : removeCommon(links, existingResourceLinks)) {
				myEntityManager.persist(next);
			}
			// make sure links are indexed
			theEntity.setResourceLinks(links);

			// Store composite string uniques
			if (getConfig().isUniqueIndexesEnabled()) {
				for (ResourceIndexedCompositeStringUnique next : removeCommon(existingCompositeStringUniques, compositeStringUniques)) {
					ourLog.info("Removing unique index: {}", next);
					myEntityManager.remove(next);
					theEntity.getParamsCompositeStringUnique().remove(next);
				}
				for (ResourceIndexedCompositeStringUnique next : removeCommon(compositeStringUniques, existingCompositeStringUniques)) {
					if (myConfig.isUniqueIndexesCheckedBeforeSave()) {
						ResourceIndexedCompositeStringUnique existing = myResourceIndexedCompositeStringUniqueDao.findByQueryString(next.getIndexString());
						if (existing != null) {
							throw new PreconditionFailedException("Can not create resource of type " + theEntity.getResourceType() + " as it would create a duplicate index matching query: " + next.getIndexString() + " (existing index belongs to " + existing.getResource().getIdDt().toUnqualifiedVersionless().getValue() + ")");
						}
					}
					ourLog.info("Persisting unique index: {}", next);
					myEntityManager.persist(next);
				}
			}

		} // if thePerformIndexing

		theEntity = myEntityManager.merge(theEntity);

		if (theResource != null) {
			populateResourceIdFromEntity(theEntity, theResource);
		}

		return theEntity;
	}

	protected ResourceTable updateEntity(IBaseResource theResource, ResourceTable entity, Date theDeletedTimestampOrNull, Date theUpdateTime) {
		return updateEntity(theResource, entity, theDeletedTimestampOrNull, true, true, theUpdateTime, false, true);
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
				Set<String> validTypes = new HashSet<String>();
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

	protected void validateDeleteConflictsEmptyOrThrowException(List<DeleteConflict> theDeleteConflicts) {
		if (theDeleteConflicts.isEmpty()) {
			return;
		}

		IBaseOperationOutcome oo = OperationOutcomeUtil.newInstance(getContext());
		String firstMsg = null;
		for (DeleteConflict next : theDeleteConflicts) {
			StringBuilder b = new StringBuilder();
			b.append("Unable to delete ");
			b.append(next.getTargetId().toUnqualifiedVersionless().getValue());
			b.append(" because at least one resource has a reference to this resource. First reference found was resource ");
			b.append(next.getTargetId().toUnqualifiedVersionless().getValue());
			b.append(" in path ");
			b.append(next.getSourcePath());
			String msg = b.toString();
			if (firstMsg == null) {
				firstMsg = msg;
			}
			OperationOutcomeUtil.addIssue(getContext(), oo, OO_SEVERITY_ERROR, msg, null, "processing");
		}

		throw new ResourceVersionConflictException(firstMsg, oo);
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
				tag = tagList.getTag(Constants.TAG_SUBSETTED_SYSTEM, Constants.TAG_SUBSETTED_CODE);
				totalMetaCount += tagList.size();
			}
			List<IdDt> profileList = ResourceMetadataKeyEnum.PROFILES.get(res);
			if (profileList != null) {
				totalMetaCount += profileList.size();
			}
		} else {
			IAnyResource res = (IAnyResource) theResource;
			tag = res.getMeta().getTag(Constants.TAG_SUBSETTED_SYSTEM, Constants.TAG_SUBSETTED_CODE);
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

	/**
	 * This method is used to create a set of all possible combinations of
	 * parameters across a set of search parameters. An example of why
	 * this is needed:
	 * <p>
	 * Let's say we have a unique index on (Patient:gender AND Patient:name).
	 * Then we pass in <code>SMITH, John</code> with a gender of <code>male</code>.
	 * </p>
	 * <p>
	 * In this case, because the name parameter matches both first and last name,
	 * we now need two unique indexes:
	 * <ul>
	 * <li>Patient?gender=male&amp;name=SMITH</li>
	 * <li>Patient?gender=male&amp;name=JOHN</li>
	 * </ul>
	 * </p>
	 * <p>
	 * So this recursive algorithm calculates those
	 * </p>
	 *
	 * @param theResourceType E.g. <code>Patient
	 * @param thePartsChoices E.g. <code>[[gender=male], [name=SMITH, name=JOHN]]</code>
	 */
	public static Set<String> extractCompositeStringUniquesValueChains(String theResourceType, List<List<String>> thePartsChoices) {

		for (List<String> next : thePartsChoices) {
			for (Iterator<String> iter = next.iterator(); iter.hasNext(); ) {
				if (isBlank(iter.next())) {
					iter.remove();
				}
			}
			if (next.isEmpty()) {
				return Collections.emptySet();
			}
		}

		if (thePartsChoices.isEmpty()) {
			return Collections.emptySet();
		}

		Collections.sort(thePartsChoices, new Comparator<List<String>>() {
			@Override
			public int compare(List<String> o1, List<String> o2) {
				String str1 = null;
				String str2 = null;
				if (o1.size() > 0) {
					str1 = o1.get(0);
				}
				if (o2.size() > 0) {
					str2 = o2.get(0);
				}
				return StringUtils.compare(str1, str2);
			}
		});

		List<String> values = new ArrayList<>();
		Set<String> queryStringsToPopulate = new HashSet<>();
		extractCompositeStringUniquesValueChains(theResourceType, thePartsChoices, values, queryStringsToPopulate);
		return queryStringsToPopulate;
	}

	private static void extractCompositeStringUniquesValueChains(String theResourceType, List<List<String>> thePartsChoices, List<String> theValues, Set<String> theQueryStringsToPopulate) {
		if (thePartsChoices.size() > 0) {
			List<String> nextList = thePartsChoices.get(0);
			Collections.sort(nextList);
			for (String nextChoice : nextList) {
				theValues.add(nextChoice);
				extractCompositeStringUniquesValueChains(theResourceType, thePartsChoices.subList(1, thePartsChoices.size()), theValues, theQueryStringsToPopulate);
				theValues.remove(theValues.size() - 1);
			}
		} else {
			if (theValues.size() > 0) {
				StringBuilder uniqueString = new StringBuilder();
				uniqueString.append(theResourceType);

				for (int i = 0; i < theValues.size(); i++) {
					uniqueString.append(i == 0 ? "?" : "&");
					uniqueString.append(theValues.get(i));
				}

				theQueryStringsToPopulate.add(uniqueString.toString());
			}
		}
	}

	protected static boolean isValidPid(IIdType theId) {
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

	public static String normalizeString(String theString) {
		CharArrayWriter outBuffer = new CharArrayWriter(theString.length());

		/*
		 * The following block of code is used to strip out diacritical marks from latin script
		 * and also convert to upper case. E.g. "j?mes" becomes "JAMES".
		 *
		 * See http://www.unicode.org/charts/PDF/U0300.pdf for the logic
		 * behind stripping 0300-036F
		 *
		 * See #454 for an issue where we were completely stripping non latin characters
		 * See #832 for an issue where we normalize korean characters, which are decomposed
		 */
		String string = Normalizer.normalize(theString, Normalizer.Form.NFD);
		for (int i = 0, n = string.length(); i < n; ++i) {
			char c = string.charAt(i);
			if (c >= '\u0300' && c <= '\u036F') {
				continue;
			} else {
				outBuffer.append(c);
			}
		}

		return new String(outBuffer.toCharArray()).toUpperCase();
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

	/**
	 * Do not call this method outside of unit tests
	 */
	@VisibleForTesting
	public static void setValidationDisabledForUnitTest(boolean theValidationDisabledForUnitTest) {
		ourValidationDisabledForUnitTest = theValidationDisabledForUnitTest;
	}

	private static List<BaseCodingDt> toBaseCodingList(List<IBaseCoding> theSecurityLabels) {
		ArrayList<BaseCodingDt> retVal = new ArrayList<BaseCodingDt>(theSecurityLabels.size());
		for (IBaseCoding next : theSecurityLabels) {
			retVal.add((BaseCodingDt) next);
		}
		return retVal;
	}

	protected static Long translateForcedIdToPid(String theResourceName, String theResourceId, IForcedIdDao theForcedIdDao) {
		return translateForcedIdToPids(new IdDt(theResourceName, theResourceId), theForcedIdDao).get(0);
	}

	static List<Long> translateForcedIdToPids(IIdType theId, IForcedIdDao theForcedIdDao) {
		Validate.isTrue(theId.hasIdPart());

		if (isValidPid(theId)) {
			return Collections.singletonList(theId.getIdPartAsLong());
		} else {
			List<ForcedId> forcedId;
			if (theId.hasResourceType()) {
				forcedId = theForcedIdDao.findByTypeAndForcedId(theId.getResourceType(), theId.getIdPart());
			} else {
				forcedId = theForcedIdDao.findByForcedId(theId.getIdPart());
			}

			if (forcedId.isEmpty() == false) {
				List<Long> retVal = new ArrayList<Long>(forcedId.size());
				for (ForcedId next : forcedId) {
					retVal.add(next.getResourcePid());
				}
				return retVal;
			} else {
				throw new ResourceNotFoundException(theId);
			}
		}
	}

	public static SearchParameterMap translateMatchUrl(IDao theCallingDao, FhirContext theContext, String theMatchUrl, RuntimeResourceDefinition resourceDef) {
		SearchParameterMap paramMap = new SearchParameterMap();
		List<NameValuePair> parameters = translateMatchUrl(theMatchUrl);

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
						p1.setValuesAsQueryTokens(theContext, nextParamName, paramList);
						paramMap.setLastUpdated(p1);
					}
				}
				continue;
			}

			if (Constants.PARAM_HAS.equals(nextParamName)) {
				IQueryParameterAnd<?> param = ParameterUtil.parseQueryParams(theContext, RestSearchParameterTypeEnum.HAS, nextParamName, paramList);
				paramMap.add(nextParamName, param);
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
				type.setValuesAsQueryTokens(theContext, nextParamName, (paramList));
				paramMap.add(nextParamName, type);
			} else if (nextParamName.startsWith("_")) {
				// ignore these since they aren't search params (e.g. _sort)
			} else {
				RuntimeSearchParam paramDef = theCallingDao.getSearchParamByName(resourceDef, nextParamName);
				if (paramDef == null) {
					throw new InvalidRequestException(
						"Failed to parse match URL[" + theMatchUrl + "] - Resource type " + resourceDef.getName() + " does not have a parameter with name: " + nextParamName);
				}

				IQueryParameterAnd<?> param = ParameterUtil.parseQueryParams(theContext, paramDef, nextParamName, paramList);
				paramMap.add(nextParamName, param);
			}
		}
		return paramMap;
	}

	public static List<NameValuePair> translateMatchUrl(String theMatchUrl) {
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
		return parameters;
	}

	public static void validateResourceType(BaseHasResource theEntity, String theResourceName) {
		if (!theResourceName.equals(theEntity.getResourceType())) {
			throw new ResourceNotFoundException(
				"Resource with ID " + theEntity.getIdDt().getIdPart() + " exists but it is not of type " + theResourceName + ", found resource of type " + theEntity.getResourceType());
		}
	}

}
