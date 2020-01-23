package ca.uhn.fhir.jpa.dao;

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

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.predicate.*;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamUriDao;
import ca.uhn.fhir.jpa.dao.data.IResourceSearchViewDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTagDao;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.entity.ResourceSearchView;
import ca.uhn.fhir.jpa.interceptor.JpaPreResourceAccessDetails;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import ca.uhn.fhir.jpa.model.entity.*;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.searchparam.JpaRuntimeSearchParam;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.ResourceMetaParams;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.searchparam.util.SourceParam;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.Validate;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.query.Query;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.*;

/**
 * The SearchBuilder is responsible for actually forming the SQL query that handles
 * searches for resources
 */
@Component
@Scope("prototype")
public class SearchBuilder implements ISearchBuilder {

	/**
	 * See loadResourcesByPid
	 * for an explanation of why we use the constant 800
	 */
	// NB: keep public
	public static final int MAXIMUM_PAGE_SIZE = 800;

	private static final List<ResourcePersistentId> EMPTY_LONG_LIST = Collections.unmodifiableList(new ArrayList<>());
	private static final Logger ourLog = LoggerFactory.getLogger(SearchBuilder.class);
	private static ResourcePersistentId NO_MORE = new ResourcePersistentId(-1L);
	private final boolean myDontUseHashesForSearch;
	private final DaoConfig myDaoConfig;
	@Autowired
	protected IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	protected IResourceTagDao myResourceTagDao;
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;
	@Autowired
	private IResourceSearchViewDao myResourceSearchViewDao;
	@Autowired
	private FhirContext myContext;
	@Autowired
	private IdHelperService myIdHelperService;
	@Autowired(required = false)
	private IFulltextSearchSvc myFulltextSearchSvc;
	@Autowired
	private IResourceIndexedSearchParamUriDao myResourceIndexedSearchParamUriDao;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	private ITermReadSvc myTerminologySvc;
	@Autowired
	private MatchUrlService myMatchUrlService;
	@Autowired
	private PredicateBuilderFactory myPredicateBuilderFactory;

	private List<ResourcePersistentId> myAlsoIncludePids;
	private CriteriaBuilder myBuilder;
	private BaseHapiFhirDao<?> myCallingDao;
	private IndexJoins myIndexJoins = new IndexJoins();
	private SearchParameterMap myParams;
	private ArrayList<Predicate> myPredicates;
	private String myResourceName;
	private AbstractQuery<Long> myResourceTableQuery;
	private Root<ResourceTable> myResourceTableRoot;
	private Class<? extends IBaseResource> myResourceType;
	private String mySearchUuid;
	private int myFetchSize;
	private Integer myMaxResultsToFetch;
	private Set<ResourcePersistentId> myPidSet;
	private boolean myHaveIndexJoins = false;
	private PredicateBuilderCoords myPredicateBuilderCoords;
	private PredicateBuilderDate myPredicateBuilderDate;
	private PredicateBuilderNumber myPredicateBuilderNumber;
	private PredicateBuilderQuantity myPredicateBuilderQuantity;
	private PredicateBuilderString myPredicateBuilderString;
	private PredicateBuilderTag myPredicateBuilderTag;
	private PredicateBuilderToken myPredicateBuilderToken;
	private PredicateBuilderUri myPredicateBuilderUri;

	/**
	 * Constructor
	 */
	SearchBuilder(BaseHapiFhirDao<?> theDao) {
		myCallingDao = theDao;
		myDaoConfig = theDao.getConfig();
		myDontUseHashesForSearch = myDaoConfig.getDisableHashBasedSearches();
	}

	@PostConstruct
	public void postConstruct() {
		myPredicateBuilderCoords = myPredicateBuilderFactory.newPredicateBuilderCoords(this);
		myPredicateBuilderDate = myPredicateBuilderFactory.newPredicateBuilderDate(this);
		myPredicateBuilderNumber = myPredicateBuilderFactory.newPredicateBuilderNumber(this);
		myPredicateBuilderQuantity = myPredicateBuilderFactory.newPredicateBuilderQuantity(this);
		myPredicateBuilderString = myPredicateBuilderFactory.newPredicateBuilderString(this);
		myPredicateBuilderTag = myPredicateBuilderFactory.newPredicateBuilderTag(this);
		myPredicateBuilderToken = myPredicateBuilderFactory.newPredicateBuilderToken(this);
		myPredicateBuilderUri = myPredicateBuilderFactory.newPredicateBuilderUri(this);
	}

	@Override
	public void setMaxResultsToFetch(Integer theMaxResultsToFetch) {
		myMaxResultsToFetch = theMaxResultsToFetch;
	}

	private Predicate addPredicateReference(String theResourceName,
														 String theParamName,
														 List<? extends IQueryParameterType> theList,
														 RequestDetails theRequest) {
		return addPredicateReference(theResourceName,
			theParamName,
			theList,
			null, theRequest);
	}

	/**
	 * Add reference predicate to the current search
	 */
	private Predicate addPredicateReference(String theResourceName,
														 String theParamName,
														 List<? extends IQueryParameterType> theList,
														 SearchFilterParser.CompareOperation operation,
														 RequestDetails theRequest) {

		assert theParamName.contains(".") == false;

		if ((operation != null) &&
			(operation != SearchFilterParser.CompareOperation.eq) &&
			(operation != SearchFilterParser.CompareOperation.ne)) {
			throw new InvalidRequestException("Invalid operator specified for reference predicate.  Supported operators for reference predicate are \"eq\" and \"ne\".");
		}

		if (theList.get(0).getMissing() != null) {
			addPredicateParamMissing(theResourceName, theParamName, theList.get(0).getMissing());
			return null;
		}

		Join<ResourceTable, ResourceLink> join = createJoin(SearchBuilderJoinEnum.REFERENCE, theParamName);

		List<IIdType> targetIds = new ArrayList<>();
		List<String> targetQualifiedUrls = new ArrayList<>();

		for (int orIdx = 0; orIdx < theList.size(); orIdx++) {
			IQueryParameterType nextOr = theList.get(orIdx);

			if (nextOr instanceof ReferenceParam) {
				ReferenceParam ref = (ReferenceParam) nextOr;

				if (isBlank(ref.getChain())) {

					/*
					 * Handle non-chained search, e.g. Patient?organization=Organization/123
					 */

					IIdType dt = new IdDt(ref.getBaseUrl(), ref.getResourceType(), ref.getIdPart(), null);

					if (dt.hasBaseUrl()) {
						if (myDaoConfig.getTreatBaseUrlsAsLocal().contains(dt.getBaseUrl())) {
							dt = dt.toUnqualified();
							targetIds.add(dt);
						} else {
							targetQualifiedUrls.add(dt.getValue());
						}
					} else {
						targetIds.add(dt);
					}

				} else {

					/*
					 * Handle chained search, e.g. Patient?organization.name=Kwik-e-mart
					 */

					return addPredicateReferenceWithChain(theResourceName, theParamName, theList, join, new ArrayList<>(), ref, theRequest);

				}

			} else {
				throw new IllegalArgumentException("Invalid token type (expecting ReferenceParam): " + nextOr.getClass());
			}

		}

		List<Predicate> codePredicates = new ArrayList<>();

		// Resources by ID
		List<ResourcePersistentId> targetPids = myIdHelperService.translateForcedIdToPids(targetIds, theRequest);
		if (!targetPids.isEmpty()) {
			ourLog.debug("Searching for resource link with target PIDs: {}", targetPids);
			Predicate pathPredicate;
			if ((operation == null) || (operation == SearchFilterParser.CompareOperation.eq)) {
				pathPredicate = createResourceLinkPathPredicate(theResourceName, theParamName, join);
			} else {
				pathPredicate = createResourceLinkPathPredicate(theResourceName, theParamName, join).not();
			}
			Predicate pidPredicate;
			if ((operation == null) || (operation == SearchFilterParser.CompareOperation.eq)) {
				pidPredicate = join.get("myTargetResourcePid").in(ResourcePersistentId.toLongList(targetPids));
			} else {
				pidPredicate = join.get("myTargetResourcePid").in(ResourcePersistentId.toLongList(targetPids)).not();
			}
			codePredicates.add(myBuilder.and(pathPredicate, pidPredicate));
		}

		// Resources by fully qualified URL
		if (!targetQualifiedUrls.isEmpty()) {
			ourLog.debug("Searching for resource link with target URLs: {}", targetQualifiedUrls);
			Predicate pathPredicate;
			if ((operation == null) || (operation == SearchFilterParser.CompareOperation.eq)) {
				pathPredicate = createResourceLinkPathPredicate(theResourceName, theParamName, join);
			} else {
				pathPredicate = createResourceLinkPathPredicate(theResourceName, theParamName, join).not();
			}
			Predicate pidPredicate;
			if ((operation == null) || (operation == SearchFilterParser.CompareOperation.eq)) {
				pidPredicate = join.get("myTargetResourceUrl").in(targetQualifiedUrls);
			} else {
				pidPredicate = join.get("myTargetResourceUrl").in(targetQualifiedUrls).not();
			}
			codePredicates.add(myBuilder.and(pathPredicate, pidPredicate));
		}

		if (codePredicates.size() > 0) {
			Predicate predicate = myBuilder.or(toArray(codePredicates));
			myPredicates.add(predicate);
			return predicate;
		} else {
			// Add a predicate that will never match
			Predicate pidPredicate = join.get("myTargetResourcePid").in(-1L);
			myPredicates.clear();
			myPredicates.add(pidPredicate);
			return pidPredicate;
		}
	}

	private Predicate addPredicateReferenceWithChain(String theResourceName, String theParamName, List<? extends IQueryParameterType> theList, Join<ResourceTable, ResourceLink> theJoin, List<Predicate> theCodePredicates, ReferenceParam theRef, RequestDetails theRequest) {
		final List<Class<? extends IBaseResource>> resourceTypes;
		if (!theRef.hasResourceType()) {

			RuntimeSearchParam param = mySearchParamRegistry.getActiveSearchParam(theResourceName, theParamName);
			resourceTypes = new ArrayList<>();

			if (param.hasTargets()) {
				Set<String> targetTypes = param.getTargets();
				for (String next : targetTypes) {
					resourceTypes.add(myContext.getResourceDefinition(next).getImplementingClass());
				}
			}

			if (resourceTypes.isEmpty()) {
				RuntimeResourceDefinition resourceDef = myContext.getResourceDefinition(theResourceName);
				RuntimeSearchParam searchParamByName = mySearchParamRegistry.getSearchParamByName(resourceDef, theParamName);
				if (searchParamByName == null) {
					throw new InternalErrorException("Could not find parameter " + theParamName);
				}
				String paramPath = searchParamByName.getPath();
				if (paramPath.endsWith(".as(Reference)")) {
					paramPath = paramPath.substring(0, paramPath.length() - ".as(Reference)".length()) + "Reference";
				}

				if (paramPath.contains(".extension(")) {
					int startIdx = paramPath.indexOf(".extension(");
					int endIdx = paramPath.indexOf(')', startIdx);
					if (startIdx != -1 && endIdx != -1) {
						paramPath = paramPath.substring(0, startIdx + 10) + paramPath.substring(endIdx + 1);
					}
				}

				BaseRuntimeChildDefinition def = myContext.newTerser().getDefinition(myResourceType, paramPath);
				if (def instanceof RuntimeChildChoiceDefinition) {
					RuntimeChildChoiceDefinition choiceDef = (RuntimeChildChoiceDefinition) def;
					resourceTypes.addAll(choiceDef.getResourceTypes());
				} else if (def instanceof RuntimeChildResourceDefinition) {
					RuntimeChildResourceDefinition resDef = (RuntimeChildResourceDefinition) def;
					resourceTypes.addAll(resDef.getResourceTypes());
					if (resourceTypes.size() == 1) {
						if (resourceTypes.get(0).isInterface()) {
							throw new InvalidRequestException("Unable to perform search for unqualified chain '" + theParamName + "' as this SearchParameter does not declare any target types. Add a qualifier of the form '" + theParamName + ":[ResourceType]' to perform this search.");
						}
					}
				} else {
					throw new ConfigurationException("Property " + paramPath + " of type " + myResourceName + " is not a resource: " + def.getClass());
				}
			}

			if (resourceTypes.isEmpty()) {
				for (BaseRuntimeElementDefinition<?> next : myContext.getElementDefinitions()) {
					if (next instanceof RuntimeResourceDefinition) {
						RuntimeResourceDefinition nextResDef = (RuntimeResourceDefinition) next;
						resourceTypes.add(nextResDef.getImplementingClass());
					}
				}
			}

		} else {
			try {
				RuntimeResourceDefinition resDef = myContext.getResourceDefinition(theRef.getResourceType());
				resourceTypes = new ArrayList<>(1);
				resourceTypes.add(resDef.getImplementingClass());
			} catch (DataFormatException e) {
				throw new InvalidRequestException("Invalid resource type: " + theRef.getResourceType());
			}
		}

		boolean foundChainMatch = false;

		for (Class<? extends IBaseResource> nextType : resourceTypes) {

			String chain = theRef.getChain();
			String remainingChain = null;
			int chainDotIndex = chain.indexOf('.');
			if (chainDotIndex != -1) {
				remainingChain = chain.substring(chainDotIndex + 1);
				chain = chain.substring(0, chainDotIndex);
			}

			RuntimeResourceDefinition typeDef = myContext.getResourceDefinition(nextType);
			String subResourceName = typeDef.getName();

			IFhirResourceDao<?> dao = myCallingDao.getDao(nextType);
			if (dao == null) {
				ourLog.debug("Don't have a DAO for type {}", nextType.getSimpleName());
				continue;
			}

			int qualifierIndex = chain.indexOf(':');
			String qualifier = null;
			if (qualifierIndex != -1) {
				qualifier = chain.substring(qualifierIndex);
				chain = chain.substring(0, qualifierIndex);
			}

			boolean isMeta = ResourceMetaParams.RESOURCE_META_PARAMS.containsKey(chain);
			RuntimeSearchParam param = null;
			if (!isMeta) {
				param = mySearchParamRegistry.getSearchParamByName(typeDef, chain);
				if (param == null) {
					ourLog.debug("Type {} doesn't have search param {}", nextType.getSimpleName(), param);
					continue;
				}
			}

			ArrayList<IQueryParameterType> orValues = Lists.newArrayList();

			for (IQueryParameterType next : theList) {
				String nextValue = next.getValueAsQueryToken(myContext);
				IQueryParameterType chainValue = mapReferenceChainToRawParamType(remainingChain, param, theParamName, qualifier, nextType, chain, isMeta, nextValue);
				if (chainValue == null) {
					continue;
				}
				foundChainMatch = true;
				orValues.add(chainValue);
			}

			Subquery<Long> subQ = createLinkSubquery(foundChainMatch, chain, subResourceName, orValues, theRequest);

			Predicate pathPredicate = createResourceLinkPathPredicate(theResourceName, theParamName, theJoin);
			Predicate pidPredicate = theJoin.get("myTargetResourcePid").in(subQ);
			Predicate andPredicate = myBuilder.and(pathPredicate, pidPredicate);
			theCodePredicates.add(andPredicate);

		}

		if (!foundChainMatch) {
			throw new InvalidRequestException(myContext.getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "invalidParameterChain", theParamName + '.' + theRef.getChain()));
		}

		Predicate predicate = myBuilder.or(toArray(theCodePredicates));
		myPredicates.add(predicate);
		return predicate;
	}

	private Subquery<Long> createLinkSubquery(boolean theFoundChainMatch, String theChain, String theSubResourceName, List<IQueryParameterType> theOrValues, RequestDetails theRequest) {
		Subquery<Long> subQ = myResourceTableQuery.subquery(Long.class);
		Root<ResourceTable> subQfrom = subQ.from(ResourceTable.class);
		subQ.select(subQfrom.get("myId").as(Long.class));

		List<List<IQueryParameterType>> andOrParams = new ArrayList<>();
		andOrParams.add(theOrValues);

		/*
		 * We're doing a chain call, so push the current query root
		 * and predicate list down and put new ones at the top of the
		 * stack and run a subquery
		 */
		Root<ResourceTable> stackRoot = myResourceTableRoot;
		ArrayList<Predicate> stackPredicates = myPredicates;
		Map<SearchBuilderJoinKey, Join<?, ?>> stackIndexJoins = myIndexJoins;
		myResourceTableRoot = subQfrom;
		myPredicates = Lists.newArrayList();
		myIndexJoins = Maps.newHashMap();

		// Create the subquery predicates
		myPredicates.add(myBuilder.equal(myResourceTableRoot.get("myResourceType"), theSubResourceName));
		myPredicates.add(myBuilder.isNull(myResourceTableRoot.get("myDeleted")));

		if (theFoundChainMatch) {
			searchForIdsWithAndOr(theSubResourceName, theChain, andOrParams, theRequest);
			subQ.where(toArray(myPredicates));
		}

		/*
		 * Pop the old query root and predicate list back
		 */
		myResourceTableRoot = stackRoot;
		myPredicates = stackPredicates;
		myIndexJoins = stackIndexJoins;
		return subQ;
	}

	private void searchForIdsWithAndOr(String theResourceName, String theParamName, List<List<IQueryParameterType>> theAndOrParams, RequestDetails theRequest) {

		if (theAndOrParams.isEmpty()) {
			return;
		}

		switch (theParamName) {
			case IAnyResource.SP_RES_ID:
				addPredicateResourceId(theResourceName, theAndOrParams, theRequest);
				break;

			case IAnyResource.SP_RES_LANGUAGE:
				addPredicateLanguage(theAndOrParams);
				break;

			case Constants.PARAM_HAS:
				addPredicateHas(theAndOrParams, theRequest);
				break;

			case Constants.PARAM_TAG:
			case Constants.PARAM_PROFILE:
			case Constants.PARAM_SECURITY:
				addPredicateTag(theAndOrParams, theParamName);
				break;

			case Constants.PARAM_SOURCE:
				addPredicateSource(theAndOrParams, theRequest);
				break;

			default:

				RuntimeSearchParam nextParamDef = mySearchParamRegistry.getActiveSearchParam(theResourceName, theParamName);
				if (nextParamDef != null) {
					switch (nextParamDef.getParamType()) {
						case DATE:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								addPredicateDate(theResourceName, theParamName, nextAnd);
							}
							break;
						case QUANTITY:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								addPredicateQuantity(theResourceName, theParamName, nextAnd);
							}
							break;
						case REFERENCE:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								addPredicateReference(theResourceName, theParamName, nextAnd, theRequest);
							}
							break;
						case STRING:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								addPredicateString(theResourceName, theParamName, nextAnd);
							}
							break;
						case TOKEN:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								if ("Location.position".equals(nextParamDef.getPath())) {
									addPredicateCoords(theResourceName, theParamName, nextAnd);
								} else {
									addPredicateToken(theResourceName, theParamName, nextAnd);
								}
							}
							break;
						case NUMBER:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								addPredicateNumber(theResourceName, theParamName, nextAnd);
							}
							break;
						case COMPOSITE:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								addPredicateComposite(theResourceName, nextParamDef, nextAnd);
							}
							break;
						case URI:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								addPredicateUri(theResourceName, theParamName, nextAnd);
							}
							break;
						case HAS:
						case SPECIAL:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								if ("Location.position".equals(nextParamDef.getPath())) {
									addPredicateCoords(theResourceName, theParamName, nextAnd);
								}
							}
							break;
					}
				} else {
					if (Constants.PARAM_CONTENT.equals(theParamName) || Constants.PARAM_TEXT.equals(theParamName)) {
						// These are handled later
					} else if (Constants.PARAM_FILTER.equals(theParamName)) {
						// Parse the predicates enumerated in the _filter separated by AND or OR...
						if (theAndOrParams.get(0).get(0) instanceof StringParam) {
							String filterString = ((StringParam) theAndOrParams.get(0).get(0)).getValue();
							SearchFilterParser.Filter filter;
							try {
								filter = SearchFilterParser.parse(filterString);
							} catch (SearchFilterParser.FilterSyntaxException theE) {
								throw new InvalidRequestException("Error parsing _filter syntax: " + theE.getMessage());
							}
							if (filter != null) {

								if (!myDaoConfig.isFilterParameterEnabled()) {
									throw new InvalidRequestException(Constants.PARAM_FILTER + " parameter is disabled on this server");
								}

								// TODO: we clear the predicates below because the filter builds up
								// its own collection of predicates. It'd probably be good at some
								// point to do something more fancy...
								ArrayList<Predicate> holdPredicates = new ArrayList<>(myPredicates);

								Predicate filterPredicate = processFilter(filter, theResourceName, theRequest);
								myPredicates.clear();
								myPredicates.addAll(holdPredicates);
								myPredicates.add(filterPredicate);
							}
						}


					} else {
						throw new InvalidRequestException("Unknown search parameter " + theParamName + " for resource type " + theResourceName);
					}
				}
				break;
		}
	}

	private void addPredicateCoords(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd) {
		// FIXME KHS consolidate these predicate builders (e.g. all these mathods should have the same name)
		myPredicateBuilderCoords.addPredicateCoords(theResourceName, theParamName, theNextAnd);
	}

	private void addPredicateDate(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd) {
		myPredicateBuilderDate.addPredicateDate(theResourceName, theParamName, theNextAnd, null);
	}

	private Predicate addPredicateDate(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd, SearchFilterParser.CompareOperation theOperation) {
		return myPredicateBuilderDate.addPredicateDate(theResourceName, theParamName, theNextAnd, theOperation);
	}

	private void addPredicateNumber(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd) {
		myPredicateBuilderNumber.addPredicateNumber(theResourceName, theParamName, theNextAnd, null);
	}

	private Predicate addPredicateNumber(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd, SearchFilterParser.CompareOperation theOperation) {
		return myPredicateBuilderNumber.addPredicateNumber(theResourceName, theParamName, theNextAnd, theOperation);
	}

	private void addPredicateQuantity(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd) {
		myPredicateBuilderQuantity.addPredicateQuantity(theResourceName, theParamName, theNextAnd, null);
	}

	private Predicate addPredicateQuantity(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd, SearchFilterParser.CompareOperation theOperation) {
		return myPredicateBuilderQuantity.addPredicateQuantity(theResourceName, theParamName, theNextAnd, theOperation);
	}

	private void addPredicateString(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd) {
		myPredicateBuilderString.addPredicateString(theResourceName, theParamName, theNextAnd, SearchFilterParser.CompareOperation.sw);
	}

	private Predicate addPredicateString(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd, SearchFilterParser.CompareOperation theOperation) {
		return myPredicateBuilderString.addPredicateString(theResourceName, theParamName, theNextAnd, theOperation);
	}

	private void addPredicateTag(List<List<IQueryParameterType>> theAndOrParams, String theParamName) {
		myPredicateBuilderTag.addPredicateTag(theAndOrParams, theParamName);
	}

	private void addPredicateToken(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd) {
		myPredicateBuilderToken.addPredicateToken(theResourceName, theParamName, theNextAnd, null);
	}

	private Predicate addPredicateToken(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd, SearchFilterParser.CompareOperation theOperation) {
		return myPredicateBuilderToken.addPredicateToken(theResourceName, theParamName, theNextAnd, theOperation);
	}

	private void addPredicateUri(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd) {
		myPredicateBuilderUri.addPredicateUri(theResourceName, theParamName, theNextAnd, SearchFilterParser.CompareOperation.eq);
	}

	private Predicate addPredicateUri(String theResourceName, String theName, List<? extends IQueryParameterType> theSingletonList, SearchFilterParser.CompareOperation theOperation) {
		return myPredicateBuilderUri.addPredicateUri(theResourceName, theName, theSingletonList, theOperation);
	}

	private Predicate addPredicateLanguage(List<List<IQueryParameterType>> theList) {
		return addPredicateLanguage(theList,
			null);
	}

	private Predicate addPredicateLanguage(List<List<IQueryParameterType>> theList,
														SearchFilterParser.CompareOperation operation) {
		for (List<? extends IQueryParameterType> nextList : theList) {

			Set<String> values = new HashSet<>();
			for (IQueryParameterType next : nextList) {
				if (next instanceof StringParam) {
					String nextValue = ((StringParam) next).getValue();
					if (isBlank(nextValue)) {
						continue;
					}
					values.add(nextValue);
				} else {
					throw new InternalErrorException("Language parameter must be of type " + StringParam.class.getCanonicalName() + " - Got " + next.getClass().getCanonicalName());
				}
			}

			if (values.isEmpty()) {
				continue;
			}

			Predicate predicate = null;
			if ((operation == null) ||
				(operation == SearchFilterParser.CompareOperation.eq)) {
				predicate = myResourceTableRoot.get("myLanguage").as(String.class).in(values);
			} else if (operation == SearchFilterParser.CompareOperation.ne) {
				predicate = myResourceTableRoot.get("myLanguage").as(String.class).in(values).not();
			} else {
				throw new InvalidRequestException("Unsupported operator specified in language query, only \"eq\" and \"ne\" are supported");
			}
			myPredicates.add(predicate);
			if (operation != null) {
				return predicate;
			}
		}

		return null;
	}

	private void addPredicateSource(List<List<IQueryParameterType>> theAndOrParams, RequestDetails theRequest) {
		for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
			addPredicateSource(nextAnd, SearchFilterParser.CompareOperation.eq, theRequest);
		}
	}

	private Predicate addPredicateSource(List<? extends IQueryParameterType> theList, SearchFilterParser.CompareOperation theOperation, RequestDetails theRequest) {
		if (myDaoConfig.getStoreMetaSourceInformation() == DaoConfig.StoreMetaSourceInformationEnum.NONE) {
			String msg = myContext.getLocalizer().getMessage(SearchBuilder.class, "sourceParamDisabled");
			throw new InvalidRequestException(msg);
		}

		Join<ResourceTable, ResourceHistoryProvenanceEntity> join = myResourceTableRoot.join("myProvenance", JoinType.LEFT);

		List<Predicate> codePredicates = new ArrayList<>();

		for (IQueryParameterType nextParameter : theList) {
			SourceParam sourceParameter = new SourceParam(nextParameter.getValueAsQueryToken(myContext));
			String sourceUri = sourceParameter.getSourceUri();
			String requestId = sourceParameter.getRequestId();
			Predicate sourceUriPredicate = myBuilder.equal(join.get("mySourceUri"), sourceUri);
			Predicate requestIdPredicate = myBuilder.equal(join.get("myRequestId"), requestId);
			if (isNotBlank(sourceUri) && isNotBlank(requestId)) {
				codePredicates.add(myBuilder.and(sourceUriPredicate, requestIdPredicate));
			} else if (isNotBlank(sourceUri)) {
				codePredicates.add(sourceUriPredicate);
			} else if (isNotBlank(requestId)) {
				codePredicates.add(requestIdPredicate);
			}
		}

		Predicate retVal = myBuilder.or(toArray(codePredicates));
		myPredicates.add(retVal);
		return retVal;
	}


	private void addPredicateResourceId(String theResourceName, List<List<IQueryParameterType>> theValues, RequestDetails theRequest) {
		addPredicateResourceId(theValues, theResourceName, null, theRequest);
	}


	private Predicate addPredicateResourceId(List<List<IQueryParameterType>> theValues, String theResourceName, SearchFilterParser.CompareOperation theOperation, RequestDetails theRequest) {

		Predicate nextPredicate = createPredicateResourceId(myResourceTableRoot, theResourceName, theValues, theOperation, theRequest);

		if (nextPredicate != null) {
			myPredicates.add(nextPredicate);
			return nextPredicate;
		}

		return null;
	}

	@org.jetbrains.annotations.Nullable
	private Predicate createPredicateResourceId(Root<ResourceTable> theRoot, String theResourceName, List<List<IQueryParameterType>> theValues, SearchFilterParser.CompareOperation theOperation, RequestDetails theRequest) {
		Predicate nextPredicate = null;

		Set<ResourcePersistentId> allOrPids = null;

		for (List<? extends IQueryParameterType> nextValue : theValues) {
			Set<ResourcePersistentId> orPids = new HashSet<>();
			boolean haveValue = false;
			for (IQueryParameterType next : nextValue) {
				String value = next.getValueAsQueryToken(myContext);
				if (value != null && value.startsWith("|")) {
					value = value.substring(1);
				}

				IdType valueAsId = new IdType(value);
				if (isNotBlank(value)) {
					haveValue = true;
					try {
						ResourcePersistentId pid = myIdHelperService.translateForcedIdToPid(theResourceName, valueAsId.getIdPart(), theRequest);
						orPids.add(pid);
					} catch (ResourceNotFoundException e) {
						// This is not an error in a search, it just results in no matchesFhirResourceDaoR4InterceptorTest
						ourLog.debug("Resource ID {} was requested but does not exist", valueAsId.getIdPart());
					}
				}
			}
			if (haveValue) {
				if (allOrPids == null) {
					allOrPids = orPids;
				} else {
					allOrPids.retainAll(orPids);
				}

			}
		}

		if (allOrPids != null && allOrPids.isEmpty()) {

			// This will never match
			nextPredicate = myBuilder.equal(theRoot.get("myId").as(Long.class), -1);

		} else if (allOrPids != null) {

			SearchFilterParser.CompareOperation operation = defaultIfNull(theOperation, SearchFilterParser.CompareOperation.eq);
			assert operation == SearchFilterParser.CompareOperation.eq || operation == SearchFilterParser.CompareOperation.ne;
			List<Predicate> codePredicates = new ArrayList<>();
			switch (operation) {
				default:
				case eq:
					codePredicates.add(theRoot.get("myId").as(Long.class).in(ResourcePersistentId.toLongList(allOrPids)));
					codePredicates.add(myBuilder.equal(myResourceTableRoot.get("myResourceType"), theResourceName));
					nextPredicate = myBuilder.and(toArray(codePredicates));
					break;
				case ne:
					codePredicates.add(theRoot.get("myId").as(Long.class).in(ResourcePersistentId.toLongList(allOrPids)).not());
					codePredicates.add(myBuilder.equal(myResourceTableRoot.get("myResourceType"), theResourceName));
					nextPredicate = myBuilder.and(toArray(codePredicates));
					break;
			}

		}

		return nextPredicate;
	}

	private void searchForIdsWithAndOr(@Nonnull SearchParameterMap theParams, RequestDetails theRequest) {
		myParams = theParams;

		// Remove any empty parameters
		theParams.clean();

		/*
		 * Check if there is a unique key associated with the set
		 * of parameters passed in
		 */
		boolean couldBeEligibleForCompositeUniqueSpProcessing =
			myDaoConfig.isUniqueIndexesEnabled() &&
				myParams.getEverythingMode() == null &&
				myParams.isAllParametersHaveNoModifier();
		if (couldBeEligibleForCompositeUniqueSpProcessing) {
			attemptCompositeUniqueSpProcessing(theParams, theRequest);
		}

		// Handle each parameter
		for (Map.Entry<String, List<List<IQueryParameterType>>> nextParamEntry : myParams.entrySet()) {
			String nextParamName = nextParamEntry.getKey();
			List<List<IQueryParameterType>> andOrParams = nextParamEntry.getValue();
			searchForIdsWithAndOr(myResourceName, nextParamName, andOrParams, theRequest);
		}

	}

	private IQueryParameterType mapReferenceChainToRawParamType(String remainingChain, RuntimeSearchParam param, String theParamName, String qualifier, Class<? extends IBaseResource> nextType, String chain, boolean isMeta, String resourceId) {
		IQueryParameterType chainValue;
		if (remainingChain != null) {
			if (param == null || param.getParamType() != RestSearchParameterTypeEnum.REFERENCE) {
				ourLog.debug("Type {} parameter {} is not a reference, can not chain {}", nextType.getSimpleName(), chain, remainingChain);
				return null;
			}

			chainValue = new ReferenceParam();
			chainValue.setValueAsQueryToken(myContext, theParamName, qualifier, resourceId);
			((ReferenceParam) chainValue).setChain(remainingChain);
		} else if (isMeta) {
			IQueryParameterType type = myMatchUrlService.newInstanceType(chain);
			type.setValueAsQueryToken(myContext, theParamName, qualifier, resourceId);
			chainValue = type;
		} else {
			chainValue = toParameterType(param, qualifier, resourceId);
		}

		return chainValue;
	}

	private Predicate createResourceLinkPathPredicate(String theResourceName, String theParamName, From<?, ? extends ResourceLink> from) {
		return createResourceLinkPathPredicate(myContext, theParamName, from, theResourceName);
	}

	private Predicate createResourceLinkPathPredicate(FhirContext theContext, String theParamName, From<?, ? extends ResourceLink> theFrom,
																	  String theResourceType) {
		RuntimeResourceDefinition resourceDef = theContext.getResourceDefinition(theResourceType);
		RuntimeSearchParam param = mySearchParamRegistry.getSearchParamByName(resourceDef, theParamName);
		List<String> path = param.getPathsSplit();

		/*
		 * SearchParameters can declare paths on multiple resource
		 * types. Here we only want the ones that actually apply.
		 */
		path = new ArrayList<>(path);

		ListIterator<String> iter = path.listIterator();
		while (iter.hasNext()) {
			String nextPath = trim(iter.next());
			if (!nextPath.contains(theResourceType + ".")) {
				iter.remove();
			}
		}

		return theFrom.get("mySourcePath").in(path);
	}


	private void addPredicateHas(List<List<IQueryParameterType>> theHasParameters, RequestDetails theRequest) {

		for (List<? extends IQueryParameterType> nextOrList : theHasParameters) {

			String targetResourceType = null;
			String paramReference = null;
			String parameterName = null;

			String paramName = null;
			List<QualifiedParamList> parameters = new ArrayList<>();
			for (IQueryParameterType nextParam : nextOrList) {
				HasParam next = (HasParam) nextParam;
				targetResourceType = next.getTargetResourceType();
				paramReference = next.getReferenceFieldName();
				parameterName = next.getParameterName();
				paramName = parameterName.replaceAll("\\..*", "");
				parameters.add(QualifiedParamList.singleton(null, next.getValueAsQueryToken(myContext)));
			}

			if (paramName == null) {
				continue;
			}

			RuntimeResourceDefinition targetResourceDefinition;
			try {
				targetResourceDefinition = myContext.getResourceDefinition(targetResourceType);
			} catch (DataFormatException e) {
				throw new InvalidRequestException("Invalid resource type: " + targetResourceType);
			}

			assert parameterName != null;
			RuntimeSearchParam owningParameterDef = mySearchParamRegistry.getSearchParamByName(targetResourceDefinition, paramName);
			if (owningParameterDef == null) {
				throw new InvalidRequestException("Unknown parameter name: " + targetResourceType + ':' + parameterName);
			}

			owningParameterDef = mySearchParamRegistry.getSearchParamByName(targetResourceDefinition, paramReference);
			if (owningParameterDef == null) {
				throw new InvalidRequestException("Unknown parameter name: " + targetResourceType + ':' + paramReference);
			}

			RuntimeSearchParam paramDef = mySearchParamRegistry.getSearchParamByName(targetResourceDefinition, paramName);

			IQueryParameterAnd<IQueryParameterOr<IQueryParameterType>> parsedParam = (IQueryParameterAnd<IQueryParameterOr<IQueryParameterType>>) ParameterUtil.parseQueryParams(myContext, paramDef, paramName, parameters);

			ArrayList<IQueryParameterType> orValues = Lists.newArrayList();

			for (IQueryParameterOr<IQueryParameterType> next : parsedParam.getValuesAsQueryTokens()) {
				orValues.addAll(next.getValuesAsQueryTokens());
			}

			Subquery<Long> subQ = createLinkSubquery(true, parameterName, targetResourceType, orValues, theRequest);

			Join<ResourceTable, ResourceLink> join = myResourceTableRoot.join("myResourceLinksAsTarget", JoinType.LEFT);
			Predicate pathPredicate = createResourceLinkPathPredicate(targetResourceType, paramReference, join);
			Predicate pidPredicate = join.get("mySourceResourcePid").in(subQ);
			Predicate andPredicate = myBuilder.and(pathPredicate, pidPredicate);
			myPredicates.add(andPredicate);
		}
	}

	private void addPredicateComposite(String theResourceName, RuntimeSearchParam theParamDef, List<? extends IQueryParameterType> theNextAnd) {
		// TODO: fail if missing is set for a composite query

		IQueryParameterType or = theNextAnd.get(0);
		if (!(or instanceof CompositeParam<?, ?>)) {
			throw new InvalidRequestException("Invalid type for composite param (must be " + CompositeParam.class.getSimpleName() + ": " + or.getClass());
		}
		CompositeParam<?, ?> cp = (CompositeParam<?, ?>) or;

		RuntimeSearchParam left = theParamDef.getCompositeOf().get(0);
		IQueryParameterType leftValue = cp.getLeftValue();
		myPredicates.add(createCompositeParamPart(theResourceName, myResourceTableRoot, left, leftValue));

		RuntimeSearchParam right = theParamDef.getCompositeOf().get(1);
		IQueryParameterType rightValue = cp.getRightValue();
		myPredicates.add(createCompositeParamPart(theResourceName, myResourceTableRoot, right, rightValue));

	}

	private Predicate createCompositeParamPart(String theResourceName, Root<ResourceTable> theRoot, RuntimeSearchParam theParam, IQueryParameterType leftValue) {
		Predicate retVal = null;
		switch (theParam.getParamType()) {
			case STRING: {
				From<ResourceIndexedSearchParamString, ResourceIndexedSearchParamString> stringJoin = theRoot.join("myParamsString", JoinType.INNER);
				retVal = myPredicateBuilderString.createPredicateString(leftValue, theResourceName, theParam.getName(), myBuilder, stringJoin);
				break;
			}
			case TOKEN: {
				From<ResourceIndexedSearchParamToken, ResourceIndexedSearchParamToken> tokenJoin = theRoot.join("myParamsToken", JoinType.INNER);
				List<IQueryParameterType> tokens = Collections.singletonList(leftValue);
				Collection<Predicate> tokenPredicates = myPredicateBuilderToken.createPredicateToken(tokens, theResourceName, theParam.getName(), myBuilder, tokenJoin);
				retVal = myBuilder.and(tokenPredicates.toArray(new Predicate[0]));
				break;
			}
			case DATE: {
				From<ResourceIndexedSearchParamDate, ResourceIndexedSearchParamDate> dateJoin = theRoot.join("myParamsDate", JoinType.INNER);
				retVal = myPredicateBuilderDate.createPredicateDate(leftValue, theResourceName, theParam.getName(), myBuilder, dateJoin);
				break;
			}
			case QUANTITY: {
				From<ResourceIndexedSearchParamQuantity, ResourceIndexedSearchParamQuantity> dateJoin = theRoot.join("myParamsQuantity", JoinType.INNER);
				retVal = myPredicateBuilderQuantity.createPredicateQuantity(leftValue, theResourceName, theParam.getName(), myBuilder, dateJoin);
				break;
			}
			case COMPOSITE:
			case HAS:
			case NUMBER:
			case REFERENCE:
			case URI:
			case SPECIAL:
				break;
		}

		if (retVal == null) {
			throw new InvalidRequestException("Don't know how to handle composite parameter with type of " + theParam.getParamType());
		}

		return retVal;
	}



	@Override
	public Iterator<Long> createCountQuery(SearchParameterMap theParams, String theSearchUuid, RequestDetails theRequest) {
		myParams = theParams;
		myBuilder = myEntityManager.getCriteriaBuilder();
		mySearchUuid = theSearchUuid;

		TypedQuery<Long> query = createQuery(null, null, true, theRequest);
		return new CountQueryIterator(query);
	}

	/**
	 * @param thePidSet May be null
	 */
	@Override
	public void setPreviouslyAddedResourcePids(@Nullable List<ResourcePersistentId> thePidSet) {
		myPidSet = new HashSet<>(thePidSet);
	}

	@Override
	public IResultIterator createQuery(SearchParameterMap theParams, SearchRuntimeDetails theSearchRuntimeDetails, RequestDetails theRequest) {
		myParams = theParams;
		myBuilder = myEntityManager.getCriteriaBuilder();
		mySearchUuid = theSearchRuntimeDetails.getSearchUuid();

		if (myPidSet == null) {
			myPidSet = new HashSet<>();
		}

		return new QueryIterator(theSearchRuntimeDetails, theRequest);
	}

	private TypedQuery<Long> createQuery(SortSpec sort, Integer theMaximumResults, boolean theCount, RequestDetails theRequest) {
		myPredicates = new ArrayList<>();

		CriteriaQuery<Long> outerQuery;
		/*
		 * Sort
		 *
		 * If we have a sort, we wrap the criteria search (the search that actually
		 * finds the appropriate resources) in an outer search which is then sorted
		 */
		if (sort != null) {
			assert !theCount;

			outerQuery = myBuilder.createQuery(Long.class);
			myResourceTableQuery = outerQuery;
			myResourceTableRoot = myResourceTableQuery.from(ResourceTable.class);
			if (theCount) {
				outerQuery.multiselect(myBuilder.countDistinct(myResourceTableRoot));
			} else {
				outerQuery.multiselect(myResourceTableRoot.get("myId").as(Long.class));
			}

			List<Order> orders = Lists.newArrayList();
			List<Predicate> predicates = myPredicates; // Lists.newArrayList();

			createSort(myBuilder, myResourceTableRoot, sort, orders, predicates);
			if (orders.size() > 0) {
				outerQuery.orderBy(orders);
			}

		} else {

			outerQuery = myBuilder.createQuery(Long.class);
			myResourceTableQuery = outerQuery;
			myResourceTableRoot = myResourceTableQuery.from(ResourceTable.class);
			if (theCount) {
				outerQuery.multiselect(myBuilder.countDistinct(myResourceTableRoot));
			} else {
				outerQuery.multiselect(myResourceTableRoot.get("myId").as(Long.class));
				outerQuery.distinct(true);
			}

		}

		if (myParams.getEverythingMode() != null) {
			Join<ResourceTable, ResourceLink> join = myResourceTableRoot.join("myResourceLinks", JoinType.LEFT);

			if (myParams.get(IAnyResource.SP_RES_ID) != null) {
				StringParam idParm = (StringParam) myParams.get(IAnyResource.SP_RES_ID).get(0).get(0);
				ResourcePersistentId pid = myIdHelperService.translateForcedIdToPid(myResourceName, idParm.getValue(), theRequest);
				if (myAlsoIncludePids == null) {
					myAlsoIncludePids = new ArrayList<>(1);
				}
				myAlsoIncludePids.add(pid);
				myPredicates.add(myBuilder.equal(join.get("myTargetResourcePid").as(Long.class), pid.getIdAsLong()));
			} else {
				Predicate targetTypePredicate = myBuilder.equal(join.get("myTargetResourceType").as(String.class), myResourceName);
				Predicate sourceTypePredicate = myBuilder.equal(myResourceTableRoot.get("myResourceType").as(String.class), myResourceName);
				myPredicates.add(myBuilder.or(sourceTypePredicate, targetTypePredicate));
			}

		} else {
			// Normal search
			searchForIdsWithAndOr(myParams, theRequest);
		}

		/*
		 * Fulltext search
		 */
		if (myParams.containsKey(Constants.PARAM_CONTENT) || myParams.containsKey(Constants.PARAM_TEXT)) {
			if (myFulltextSearchSvc == null) {
				if (myParams.containsKey(Constants.PARAM_TEXT)) {
					throw new InvalidRequestException("Fulltext search is not enabled on this service, can not process parameter: " + Constants.PARAM_TEXT);
				} else if (myParams.containsKey(Constants.PARAM_CONTENT)) {
					throw new InvalidRequestException("Fulltext search is not enabled on this service, can not process parameter: " + Constants.PARAM_CONTENT);
				}
			}

			List<ResourcePersistentId> pids;
			if (myParams.getEverythingMode() != null) {
				pids = myFulltextSearchSvc.everything(myResourceName, myParams, theRequest);
			} else {
				pids = myFulltextSearchSvc.search(myResourceName, myParams);
			}
			if (pids.isEmpty()) {
				// Will never match
				pids = Collections.singletonList(new ResourcePersistentId(-1L));
			}

			myPredicates.add(myResourceTableRoot.get("myId").as(Long.class).in(ResourcePersistentId.toLongList(pids)));
		}

		/*
		 * Add a predicate to make sure we only include non-deleted resources, and only include
		 * resources of the right type.
		 *
		 * If we have any joins to index tables, we get this behaviour already guaranteed so we don't
		 * need an explicit predicate for it.
		 */
		if (!myHaveIndexJoins) {
			if (myParams.getEverythingMode() == null) {
				myPredicates.add(myBuilder.equal(myResourceTableRoot.get("myResourceType"), myResourceName));
			}
			myPredicates.add(myBuilder.isNull(myResourceTableRoot.get("myDeleted")));
		}

		// Last updated
		DateRangeParam lu = myParams.getLastUpdated();
		List<Predicate> lastUpdatedPredicates = createLastUpdatedPredicates(lu, myBuilder, myResourceTableRoot);
		myPredicates.addAll(lastUpdatedPredicates);

		myResourceTableQuery.where(myBuilder.and(SearchBuilder.toArray(myPredicates)));

		/*
		 * Now perform the search
		 */
		final TypedQuery<Long> query = myEntityManager.createQuery(outerQuery);

		if (theMaximumResults != null) {
			query.setMaxResults(theMaximumResults);
		}

		return query;
	}

	/**
	 * @return Returns {@literal true} if any search parameter sorts were found, or false if
	 * no sorts were found, or only non-search parameters ones (e.g. _id, _lastUpdated)
	 */
	private boolean createSort(CriteriaBuilder theBuilder, Root<ResourceTable> theFrom, SortSpec theSort, List<Order> theOrders, List<Predicate> thePredicates) {
		if (theSort == null || isBlank(theSort.getParamName())) {
			return false;
		}

		if (IAnyResource.SP_RES_ID.equals(theSort.getParamName())) {
			From<?, ?> forcedIdJoin = theFrom.join("myForcedId", JoinType.LEFT);
			if (theSort.getOrder() == null || theSort.getOrder() == SortOrderEnum.ASC) {
				theOrders.add(theBuilder.asc(forcedIdJoin.get("myForcedId")));
				theOrders.add(theBuilder.asc(theFrom.get("myId")));
			} else {
				theOrders.add(theBuilder.desc(forcedIdJoin.get("myForcedId")));
				theOrders.add(theBuilder.desc(theFrom.get("myId")));
			}

			return createSort(theBuilder, theFrom, theSort.getChain(), theOrders, thePredicates);
		}

		if (Constants.PARAM_LASTUPDATED.equals(theSort.getParamName())) {
			if (theSort.getOrder() == null || theSort.getOrder() == SortOrderEnum.ASC) {
				theOrders.add(theBuilder.asc(theFrom.get("myUpdated")));
			} else {
				theOrders.add(theBuilder.desc(theFrom.get("myUpdated")));
			}

			return createSort(theBuilder, theFrom, theSort.getChain(), theOrders, thePredicates);
		}

		RuntimeResourceDefinition resourceDef = myContext.getResourceDefinition(myResourceName);
		RuntimeSearchParam param = mySearchParamRegistry.getSearchParamByName(resourceDef, theSort.getParamName());
		if (param == null) {
			throw new InvalidRequestException("Unknown sort parameter '" + theSort.getParamName() + "'");
		}

		String joinAttrName;
		String[] sortAttrName;
		SearchBuilderJoinEnum joinType;

		switch (param.getParamType()) {
			case STRING:
				joinAttrName = "myParamsString";
				sortAttrName = new String[]{"myValueExact"};
				joinType = SearchBuilderJoinEnum.STRING;
				break;
			case DATE:
				joinAttrName = "myParamsDate";
				sortAttrName = new String[]{"myValueLow"};
				joinType = SearchBuilderJoinEnum.DATE;
				break;
			case REFERENCE:
				joinAttrName = "myResourceLinks";
				sortAttrName = new String[]{"myTargetResourcePid"};
				joinType = SearchBuilderJoinEnum.REFERENCE;
				break;
			case TOKEN:
				joinAttrName = "myParamsToken";
				sortAttrName = new String[]{"mySystem", "myValue"};
				joinType = SearchBuilderJoinEnum.TOKEN;
				break;
			case NUMBER:
				joinAttrName = "myParamsNumber";
				sortAttrName = new String[]{"myValue"};
				joinType = SearchBuilderJoinEnum.NUMBER;
				break;
			case URI:
				joinAttrName = "myParamsUri";
				sortAttrName = new String[]{"myUri"};
				joinType = SearchBuilderJoinEnum.URI;
				break;
			case QUANTITY:
				joinAttrName = "myParamsQuantity";
				sortAttrName = new String[]{"myValue"};
				joinType = SearchBuilderJoinEnum.QUANTITY;
				break;
			case SPECIAL:
			case COMPOSITE:
			case HAS:
			default:
				throw new InvalidRequestException("This server does not support _sort specifications of type " + param.getParamType() + " - Can't serve _sort=" + theSort.getParamName());
		}

		/*
		 * If we've already got a join for the specific parameter we're
		 * sorting on, we'll also sort with it. Otherwise we need a new join.
		 */
		SearchBuilderJoinKey key = new SearchBuilderJoinKey(theSort.getParamName(), joinType);
		Join<?, ?> join = myIndexJoins.get(key);
		if (join == null) {
			join = theFrom.join(joinAttrName, JoinType.LEFT);

			if (param.getParamType() == RestSearchParameterTypeEnum.REFERENCE) {
				thePredicates.add(join.get("mySourcePath").as(String.class).in(param.getPathsSplit()));
			} else {
				if (myDontUseHashesForSearch) {
					Predicate joinParam1 = theBuilder.equal(join.get("myParamName"), theSort.getParamName());
					thePredicates.add(joinParam1);
				} else {
					Long hashIdentity = BaseResourceIndexedSearchParam.calculateHashIdentity(myResourceName, theSort.getParamName());
					Predicate joinParam1 = theBuilder.equal(join.get("myHashIdentity"), hashIdentity);
					thePredicates.add(joinParam1);
				}
			}
		} else {
			ourLog.debug("Reusing join for {}", theSort.getParamName());
		}

		for (String next : sortAttrName) {
			if (theSort.getOrder() == null || theSort.getOrder() == SortOrderEnum.ASC) {
				theOrders.add(theBuilder.asc(join.get(next)));
			} else {
				theOrders.add(theBuilder.desc(join.get(next)));
			}
		}

		createSort(theBuilder, theFrom, theSort.getChain(), theOrders, thePredicates);

		return true;
	}


	private void doLoadPids(Collection<ResourcePersistentId> thePids, Collection<ResourcePersistentId> theIncludedPids, List<IBaseResource> theResourceListToPopulate, boolean theForHistoryOperation,
									Map<ResourcePersistentId, Integer> thePosition, RequestDetails theRequest) {

		// -- get the resource from the searchView
		Collection<ResourceSearchView> resourceSearchViewList = myResourceSearchViewDao.findByResourceIds(ResourcePersistentId.toLongList(thePids));

		//-- preload all tags with tag definition if any
		Map<ResourcePersistentId, Collection<ResourceTag>> tagMap = getResourceTagMap(resourceSearchViewList);

		ResourcePersistentId resourceId;
		for (ResourceSearchView next : resourceSearchViewList) {

			Class<? extends IBaseResource> resourceType = myContext.getResourceDefinition(next.getResourceType()).getImplementingClass();

			resourceId = new ResourcePersistentId(next.getId());

			IBaseResource resource = myCallingDao.toResource(resourceType, next, tagMap.get(resourceId), theForHistoryOperation);
			if (resource == null) {
				ourLog.warn("Unable to find resource {}/{}/_history/{} in database", next.getResourceType(), next.getIdDt().getIdPart(), next.getVersion());
				continue;
			}
			Integer index = thePosition.get(resourceId);
			if (index == null) {
				ourLog.warn("Got back unexpected resource PID {}", resourceId);
				continue;
			}

			if (resource instanceof IResource) {
				if (theIncludedPids.contains(resourceId)) {
					ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put((IResource) resource, BundleEntrySearchModeEnum.INCLUDE);
				} else {
					ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put((IResource) resource, BundleEntrySearchModeEnum.MATCH);
				}
			} else {
				if (theIncludedPids.contains(resourceId)) {
					ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put((IAnyResource) resource, BundleEntrySearchModeEnum.INCLUDE.getCode());
				} else {
					ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put((IAnyResource) resource, BundleEntrySearchModeEnum.MATCH.getCode());
				}
			}

			theResourceListToPopulate.set(index, resource);
		}
	}

	private Map<ResourcePersistentId, Collection<ResourceTag>> getResourceTagMap(Collection<ResourceSearchView> theResourceSearchViewList) {

		List<Long> idList = new ArrayList<>(theResourceSearchViewList.size());

		//-- find all resource has tags
		for (ResourceSearchView resource : theResourceSearchViewList) {
			if (resource.isHasTags())
				idList.add(resource.getId());
		}

		Map<ResourcePersistentId, Collection<ResourceTag>> tagMap = new HashMap<>();

		//-- no tags
		if (idList.size() == 0)
			return tagMap;

		//-- get all tags for the idList
		Collection<ResourceTag> tagList = myResourceTagDao.findByResourceIds(idList);

		//-- build the map, key = resourceId, value = list of ResourceTag
		ResourcePersistentId resourceId;
		Collection<ResourceTag> tagCol;
		for (ResourceTag tag : tagList) {

			resourceId = new ResourcePersistentId(tag.getResourceId());
			tagCol = tagMap.get(resourceId);
			if (tagCol == null) {
				tagCol = new ArrayList<>();
				tagCol.add(tag);
				tagMap.put(resourceId, tagCol);
			} else {
				tagCol.add(tag);
			}
		}

		return tagMap;
	}

	@Override
	public void loadResourcesByPid(Collection<ResourcePersistentId> thePids, Collection<ResourcePersistentId> theIncludedPids, List<IBaseResource> theResourceListToPopulate, boolean theForHistoryOperation, RequestDetails theDetails) {
		if (thePids.isEmpty()) {
			ourLog.debug("The include pids are empty");
			// return;
		}

		// Dupes will cause a crash later anyhow, but this is expensive so only do it
		// when running asserts
		assert new HashSet<>(thePids).size() == thePids.size() : "PID list contains duplicates: " + thePids;

		Map<ResourcePersistentId, Integer> position = new HashMap<>();
		for (ResourcePersistentId next : thePids) {
			position.put(next, theResourceListToPopulate.size());
			theResourceListToPopulate.add(null);
		}

		/*
		 * As always, Oracle can't handle things that other databases don't mind.. In this
		 * case it doesn't like more than ~1000 IDs in a single load, so we break this up
		 * if it's lots of IDs. I suppose maybe we should be doing this as a join anyhow
		 * but this should work too. Sigh.
		 */
		List<ResourcePersistentId> pids = new ArrayList<>(thePids);
		for (int i = 0; i < pids.size(); i += MAXIMUM_PAGE_SIZE) {
			int to = i + MAXIMUM_PAGE_SIZE;
			to = Math.min(to, pids.size());
			List<ResourcePersistentId> pidsSubList = pids.subList(i, to);
			doLoadPids(pidsSubList, theIncludedPids, theResourceListToPopulate, theForHistoryOperation, position, theDetails);
		}

	}

	/**
	 * THIS SHOULD RETURN HASHSET and not just Set because we add to it later
	 * so it can't be Collections.emptySet() or some such thing
	 */
	@Override
	public HashSet<ResourcePersistentId> loadIncludes(FhirContext theContext, EntityManager theEntityManager, Collection<ResourcePersistentId> theMatches, Set<Include> theRevIncludes,
																	  boolean theReverseMode, DateRangeParam theLastUpdated, String theSearchIdOrDescription, RequestDetails theRequest) {
		if (theMatches.size() == 0) {
			return new HashSet<>();
		}
		if (theRevIncludes == null || theRevIncludes.isEmpty()) {
			return new HashSet<>();
		}
		String searchFieldName = theReverseMode ? "myTargetResourcePid" : "mySourceResourcePid";

		Collection<ResourcePersistentId> nextRoundMatches = theMatches;
		HashSet<ResourcePersistentId> allAdded = new HashSet<>();
		HashSet<ResourcePersistentId> original = new HashSet<>(theMatches);
		ArrayList<Include> includes = new ArrayList<>(theRevIncludes);

		int roundCounts = 0;
		StopWatch w = new StopWatch();

		boolean addedSomeThisRound;
		do {
			roundCounts++;

			HashSet<ResourcePersistentId> pidsToInclude = new HashSet<>();

			for (Iterator<Include> iter = includes.iterator(); iter.hasNext(); ) {
				Include nextInclude = iter.next();
				if (nextInclude.isRecurse() == false) {
					iter.remove();
				}

				boolean matchAll = "*".equals(nextInclude.getValue());
				if (matchAll) {
					String sql;
					sql = "SELECT r FROM ResourceLink r WHERE r." + searchFieldName + " IN (:target_pids) ";
					List<Collection<ResourcePersistentId>> partitions = partition(nextRoundMatches, MAXIMUM_PAGE_SIZE);
					for (Collection<ResourcePersistentId> nextPartition : partitions) {
						TypedQuery<ResourceLink> q = theEntityManager.createQuery(sql, ResourceLink.class);
						q.setParameter("target_pids", ResourcePersistentId.toLongList(nextPartition));
						List<ResourceLink> results = q.getResultList();
						for (ResourceLink resourceLink : results) {
							if (theReverseMode) {
								pidsToInclude.add(new ResourcePersistentId(resourceLink.getSourceResourcePid()));
							} else {
								pidsToInclude.add(new ResourcePersistentId(resourceLink.getTargetResourcePid()));
							}
						}
					}
				} else {

					List<String> paths;
					RuntimeSearchParam param;
					String resType = nextInclude.getParamType();
					if (isBlank(resType)) {
						continue;
					}
					RuntimeResourceDefinition def = theContext.getResourceDefinition(resType);
					if (def == null) {
						ourLog.warn("Unknown resource type in include/revinclude=" + nextInclude.getValue());
						continue;
					}

					String paramName = nextInclude.getParamName();
					if (isNotBlank(paramName)) {
						param = mySearchParamRegistry.getSearchParamByName(def, paramName);
					} else {
						param = null;
					}
					if (param == null) {
						ourLog.warn("Unknown param name in include/revinclude=" + nextInclude.getValue());
						continue;
					}

					paths = param.getPathsSplit();

					String targetResourceType = defaultString(nextInclude.getParamTargetType(), null);
					for (String nextPath : paths) {
						String sql;

						boolean haveTargetTypesDefinedByParam = param.hasTargets();
						if (targetResourceType != null) {
							sql = "SELECT r FROM ResourceLink r WHERE r.mySourcePath = :src_path AND r." + searchFieldName + " IN (:target_pids) AND r.myTargetResourceType = :target_resource_type";
						} else if (haveTargetTypesDefinedByParam) {
							sql = "SELECT r FROM ResourceLink r WHERE r.mySourcePath = :src_path AND r." + searchFieldName + " IN (:target_pids) AND r.myTargetResourceType in (:target_resource_types)";
						} else {
							sql = "SELECT r FROM ResourceLink r WHERE r.mySourcePath = :src_path AND r." + searchFieldName + " IN (:target_pids)";
						}

						List<Collection<ResourcePersistentId>> partitions = partition(nextRoundMatches, MAXIMUM_PAGE_SIZE);
						for (Collection<ResourcePersistentId> nextPartition : partitions) {
							TypedQuery<ResourceLink> q = theEntityManager.createQuery(sql, ResourceLink.class);
							q.setParameter("src_path", nextPath);
							q.setParameter("target_pids", ResourcePersistentId.toLongList(nextPartition));
							if (targetResourceType != null) {
								q.setParameter("target_resource_type", targetResourceType);
							} else if (haveTargetTypesDefinedByParam) {
								q.setParameter("target_resource_types", param.getTargets());
							}
							List<ResourceLink> results = q.getResultList();
							for (ResourceLink resourceLink : results) {
								if (theReverseMode) {
									Long pid = resourceLink.getSourceResourcePid();
									if (pid != null) {
										pidsToInclude.add(new ResourcePersistentId(pid));
									}
								} else {
									Long pid = resourceLink.getTargetResourcePid();
									if (pid != null) {
										pidsToInclude.add(new ResourcePersistentId(pid));
									}
								}
							}
						}
					}
				}
			}

			if (theReverseMode) {
				if (theLastUpdated != null && (theLastUpdated.getLowerBoundAsInstant() != null || theLastUpdated.getUpperBoundAsInstant() != null)) {
					pidsToInclude = new HashSet<>(filterResourceIdsByLastUpdated(theEntityManager, theLastUpdated, pidsToInclude));
				}
			}
			for (ResourcePersistentId next : pidsToInclude) {
				if (original.contains(next) == false && allAdded.contains(next) == false) {
					theMatches.add(next);
				}
			}

			addedSomeThisRound = allAdded.addAll(pidsToInclude);
			nextRoundMatches = pidsToInclude;
		} while (includes.size() > 0 && nextRoundMatches.size() > 0 && addedSomeThisRound);

		allAdded.removeAll(original);

		ourLog.info("Loaded {} {} in {} rounds and {} ms for search {}", allAdded.size(), theReverseMode ? "_revincludes" : "_includes", roundCounts, w.getMillisAndRestart(), theSearchIdOrDescription);

		// Interceptor call: STORAGE_PREACCESS_RESOURCES
		// This can be used to remove results from the search result details before
		// the user has a chance to know that they were in the results
		if (allAdded.size() > 0) {
			List<ResourcePersistentId> includedPidList = new ArrayList<>(allAdded);
			JpaPreResourceAccessDetails accessDetails = new JpaPreResourceAccessDetails(includedPidList, () -> this);
			HookParams params = new HookParams()
				.add(IPreResourceAccessDetails.class, accessDetails)
				.add(RequestDetails.class, theRequest)
				.addIfMatchesType(ServletRequestDetails.class, theRequest);
			JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PREACCESS_RESOURCES, params);

			for (int i = includedPidList.size() - 1; i >= 0; i--) {
				if (accessDetails.isDontReturnResourceAtIndex(i)) {
					ResourcePersistentId value = includedPidList.remove(i);
					if (value != null) {
						theMatches.remove(value);
					}
				}
			}

			allAdded = new HashSet<>(includedPidList);
		}

		return allAdded;
	}

	private List<Collection<ResourcePersistentId>> partition(Collection<ResourcePersistentId> theNextRoundMatches, int theMaxLoad) {
		if (theNextRoundMatches.size() <= theMaxLoad) {
			return Collections.singletonList(theNextRoundMatches);
		} else {

			List<Collection<ResourcePersistentId>> retVal = new ArrayList<>();
			Collection<ResourcePersistentId> current = null;
			for (ResourcePersistentId next : theNextRoundMatches) {
				if (current == null) {
					current = new ArrayList<>(theMaxLoad);
					retVal.add(current);
				}

				current.add(next);

				if (current.size() >= theMaxLoad) {
					current = null;
				}
			}

			return retVal;
		}
	}

	private void attemptCompositeUniqueSpProcessing(@Nonnull SearchParameterMap theParams, RequestDetails theRequest) {
		// Since we're going to remove elements below
		theParams.values().forEach(nextAndList -> ensureSubListsAreWritable(nextAndList));

		List<JpaRuntimeSearchParam> activeUniqueSearchParams = mySearchParamRegistry.getActiveUniqueSearchParams(myResourceName, theParams.keySet());
		if (activeUniqueSearchParams.size() > 0) {

			StringBuilder sb = new StringBuilder();
			sb.append(myResourceName);
			sb.append("?");

			boolean first = true;

			ArrayList<String> keys = new ArrayList<>(theParams.keySet());
			Collections.sort(keys);
			for (String nextParamName : keys) {
				List<List<IQueryParameterType>> nextValues = theParams.get(nextParamName);

				nextParamName = UrlUtil.escapeUrlParam(nextParamName);
				if (nextValues.get(0).size() != 1) {
					sb = null;
					break;
				}

				// Reference params are only eligible for using a composite index if they
				// are qualified
				RuntimeSearchParam nextParamDef = mySearchParamRegistry.getActiveSearchParam(myResourceName, nextParamName);
				if (nextParamDef.getParamType() == RestSearchParameterTypeEnum.REFERENCE) {
					ReferenceParam param = (ReferenceParam) nextValues.get(0).get(0);
					if (isBlank(param.getResourceType())) {
						sb = null;
						break;
					}
				}

				List<? extends IQueryParameterType> nextAnd = nextValues.remove(0);
				IQueryParameterType nextOr = nextAnd.remove(0);
				String nextOrValue = nextOr.getValueAsQueryToken(myContext);
				nextOrValue = UrlUtil.escapeUrlParam(nextOrValue);

				if (first) {
					first = false;
				} else {
					sb.append('&');
				}

				sb.append(nextParamName).append('=').append(nextOrValue);

			}

			if (sb != null) {
				String indexString = sb.toString();
				ourLog.debug("Checking for unique index for query: {}", indexString);

				// Interceptor broadcast: JPA_PERFTRACE_INFO
				StorageProcessingMessage msg = new StorageProcessingMessage()
					.setMessage("Using unique index for query for search: " + indexString);
				HookParams params = new HookParams()
					.add(RequestDetails.class, theRequest)
					.addIfMatchesType(ServletRequestDetails.class, theRequest)
					.add(StorageProcessingMessage.class, msg);
				JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.JPA_PERFTRACE_INFO, params);

				addPredicateCompositeStringUnique(theParams, indexString);
			}
		}
	}

	private <T> void ensureSubListsAreWritable(List<List<T>> theListOfLists) {
		for (int i = 0; i < theListOfLists.size(); i++) {
			List<T> oldSubList = theListOfLists.get(i);
			if (!(oldSubList instanceof ArrayList)) {
				List<T> newSubList = new ArrayList<>(oldSubList);
				theListOfLists.set(i, newSubList);
			}
		}
	}

	private void addPredicateCompositeStringUnique(@Nonnull SearchParameterMap theParams, String theIndexdString) {
		myHaveIndexJoins = true;

		Join<ResourceTable, ResourceIndexedCompositeStringUnique> join = myResourceTableRoot.join("myParamsCompositeStringUnique", JoinType.LEFT);
		Predicate predicate = myBuilder.equal(join.get("myIndexString"), theIndexdString);
		myPredicates.add(predicate);

		// Remove any empty parameters remaining after this
		theParams.clean();
	}

	private Predicate processFilterParameter(SearchFilterParser.FilterParameter theFilter,
														  String theResourceName, RequestDetails theRequest) {

		RuntimeSearchParam searchParam = mySearchParamRegistry.getActiveSearchParam(theResourceName, theFilter.getParamPath().getName());

		if (searchParam == null) {
			throw new InvalidRequestException("Invalid search parameter specified, " + theFilter.getParamPath().getName() + ", for resource type " + theResourceName);
		} else if (searchParam.getName().equals(IAnyResource.SP_RES_ID)) {
			if (searchParam.getParamType() == RestSearchParameterTypeEnum.TOKEN) {
				TokenParam param = new TokenParam();
				param.setValueAsQueryToken(null,
					null,
					null,
					theFilter.getValue());
				return addPredicateResourceId(Collections.singletonList(Collections.singletonList(param)), myResourceName, theFilter.getOperation(), theRequest);
			} else {
				throw new InvalidRequestException("Unexpected search parameter type encountered, expected token type for _id search");
			}
		} else if (searchParam.getName().equals(IAnyResource.SP_RES_LANGUAGE)) {
			if (searchParam.getParamType() == RestSearchParameterTypeEnum.STRING) {
				return addPredicateLanguage(Collections.singletonList(Collections.singletonList(new StringParam(theFilter.getValue()))),
					theFilter.getOperation());
			} else {
				throw new InvalidRequestException("Unexpected search parameter type encountered, expected string type for language search");
			}
		} else if (searchParam.getName().equals(Constants.PARAM_SOURCE)) {
			if (searchParam.getParamType() == RestSearchParameterTypeEnum.TOKEN) {
				TokenParam param = new TokenParam();
				param.setValueAsQueryToken(null, null, null, theFilter.getValue());
				return addPredicateSource(Collections.singletonList(param), theFilter.getOperation(), theRequest);
			} else {
				throw new InvalidRequestException("Unexpected search parameter type encountered, expected token type for _id search");
			}
		}
//		else if ((searchParam.getName().equals(Constants.PARAM_TAG)) ||
//			(searchParam.equals(Constants.PARAM_SECURITY))) {
//			TokenParam param = new TokenParam();
//			param.setValueAsQueryToken(null,
//				null,
//				null,
//				((SearchFilterParser.FilterParameter) theFilter).getValue());
//			return addPredicateTag(Collections.singletonList(Collections.singletonList(param)),
//				searchParam.getName());
//		}
//		else if (searchParam.equals(Constants.PARAM_PROFILE)) {
//			addPredicateTag(Collections.singletonList(Collections.singletonList(new UriParam(((SearchFilterParser.FilterParameter) theFilter).getValue()))),
//				searchParam.getName());
//		}
		else {
			RestSearchParameterTypeEnum typeEnum = searchParam.getParamType();
			if (typeEnum == RestSearchParameterTypeEnum.URI) {
				return addPredicateUri(theResourceName,
					theFilter.getParamPath().getName(),
					Collections.singletonList(new UriParam(theFilter.getValue())),
					theFilter.getOperation());
			} else if (typeEnum == RestSearchParameterTypeEnum.STRING) {
				return addPredicateString(theResourceName,
					theFilter.getParamPath().getName(),
					Collections.singletonList(new StringParam(theFilter.getValue())),
					theFilter.getOperation());
			} else if (typeEnum == RestSearchParameterTypeEnum.DATE) {
				return addPredicateDate(theResourceName,
					theFilter.getParamPath().getName(),
					Collections.singletonList(new DateParam(theFilter.getValue())),
					theFilter.getOperation());
			} else if (typeEnum == RestSearchParameterTypeEnum.NUMBER) {
				return addPredicateNumber(theResourceName,
					theFilter.getParamPath().getName(),
					Collections.singletonList(new NumberParam(theFilter.getValue())),
					theFilter.getOperation());
			} else if (typeEnum == RestSearchParameterTypeEnum.REFERENCE) {
				String paramName = theFilter.getParamPath().getName();
				SearchFilterParser.CompareOperation operation = theFilter.getOperation();
				String resourceType = null; // The value can either have (Patient/123) or not have (123) a resource type, either way it's not needed here
				String chain = (theFilter.getParamPath().getNext() != null) ? theFilter.getParamPath().getNext().toString() : null;
				String value = theFilter.getValue();
				ReferenceParam referenceParam = new ReferenceParam(resourceType, chain, value);
				return addPredicateReference(theResourceName, paramName, Collections.singletonList(referenceParam), operation, theRequest);
			} else if (typeEnum == RestSearchParameterTypeEnum.QUANTITY) {
				return addPredicateQuantity(theResourceName,
					theFilter.getParamPath().getName(),
					Collections.singletonList(new QuantityParam(theFilter.getValue())),
					theFilter.getOperation());
			} else if (typeEnum == RestSearchParameterTypeEnum.COMPOSITE) {
				throw new InvalidRequestException("Composite search parameters not currently supported with _filter clauses");
			} else if (typeEnum == RestSearchParameterTypeEnum.TOKEN) {
				TokenParam param = new TokenParam();
				param.setValueAsQueryToken(null,
					null,
					null,
					theFilter.getValue());
				return addPredicateToken(theResourceName,
					theFilter.getParamPath().getName(),
					Collections.singletonList(param),
					theFilter.getOperation());
			}
		}
		return null;
	}

	private Predicate processFilter(SearchFilterParser.Filter theFilter,
											  String theResourceName, RequestDetails theRequest) {

		if (theFilter instanceof SearchFilterParser.FilterParameter) {
			return processFilterParameter((SearchFilterParser.FilterParameter) theFilter,
				theResourceName, theRequest);
		} else if (theFilter instanceof SearchFilterParser.FilterLogical) {
			// Left side
			Predicate xPredicate = processFilter(((SearchFilterParser.FilterLogical) theFilter).getFilter1(),
				theResourceName, theRequest);

			// Right side
			Predicate yPredicate = processFilter(((SearchFilterParser.FilterLogical) theFilter).getFilter2(),
				theResourceName, theRequest);

			if (((SearchFilterParser.FilterLogical) theFilter).getOperation() == SearchFilterParser.FilterLogicalOperation.and) {
				return myBuilder.and(xPredicate, yPredicate);
			} else if (((SearchFilterParser.FilterLogical) theFilter).getOperation() == SearchFilterParser.FilterLogicalOperation.or) {
				return myBuilder.or(xPredicate, yPredicate);
			}
		} else if (theFilter instanceof SearchFilterParser.FilterParameterGroup) {
			return processFilter(((SearchFilterParser.FilterParameterGroup) theFilter).getContained(),
				theResourceName, theRequest);
		}
		return null;
	}

	@Override
	public void setFetchSize(int theFetchSize) {
		myFetchSize = theFetchSize;
	}

	// FIXME KHS move this into constructor and make these final
	@Override
	public void setType(Class<? extends IBaseResource> theResourceType, String theResourceName) {
		myResourceType = theResourceType;
		myResourceName = theResourceName;
	}

	private IQueryParameterType toParameterType(RuntimeSearchParam theParam) {
		IQueryParameterType qp;
		switch (theParam.getParamType()) {
			case DATE:
				qp = new DateParam();
				break;
			case NUMBER:
				qp = new NumberParam();
				break;
			case QUANTITY:
				qp = new QuantityParam();
				break;
			case STRING:
				qp = new StringParam();
				break;
			case TOKEN:
				qp = new TokenParam();
				break;
			case COMPOSITE:
				List<RuntimeSearchParam> compositeOf = theParam.getCompositeOf();
				if (compositeOf.size() != 2) {
					throw new InternalErrorException("Parameter " + theParam.getName() + " has " + compositeOf.size() + " composite parts. Don't know how handlt this.");
				}
				IQueryParameterType leftParam = toParameterType(compositeOf.get(0));
				IQueryParameterType rightParam = toParameterType(compositeOf.get(1));
				qp = new CompositeParam<>(leftParam, rightParam);
				break;
			case REFERENCE:
				qp = new ReferenceParam();
				break;
			case SPECIAL:
			case URI:
			case HAS:
			default:
				throw new InternalErrorException("Don't know how to convert param type: " + theParam.getParamType());
		}
		return qp;
	}

	private IQueryParameterType toParameterType(RuntimeSearchParam theParam, String theQualifier, String theValueAsQueryToken) {
		IQueryParameterType qp = toParameterType(theParam);

		qp.setValueAsQueryToken(myContext, theParam.getName(), theQualifier, theValueAsQueryToken);
		return qp;
	}

	@VisibleForTesting
	void setParamsForUnitTest(SearchParameterMap theParams) {
		myParams = theParams;
	}

	public SearchParameterMap getParams() {
		return myParams;
	}

	@VisibleForTesting
	void setEntityManagerForUnitTest(EntityManager theEntityManager) {
		myEntityManager = theEntityManager;
	}

	public CriteriaBuilder getBuilder() {
		return myBuilder;
	}

	public Root<ResourceTable> getResourceTableRoot() {
		return myResourceTableRoot;
	}

	public IndexJoins getIndexJoins() {
		return myIndexJoins;
	}

	public ArrayList<Predicate> getPredicates() {
		return myPredicates;
	}

	public AbstractQuery<Long> getResourceTableQuery() {
		return myResourceTableQuery;
	}

	public Class<? extends IBaseResource> getResourceType() {
		return myResourceType;
	}

	public String getResourceName() {
		return myResourceName;
	}

	public PredicateBuilderString getPredicateBuilderString() {
		return myPredicateBuilderString;
	}

	public BaseHapiFhirDao<?> getCallingDao() {
		return myCallingDao;
	}

	public enum HandlerTypeEnum {
		UNIQUE_INDEX, STANDARD_QUERY
	}

	public class IncludesIterator extends BaseIterator<ResourcePersistentId> implements Iterator<ResourcePersistentId> {

		private final RequestDetails myRequest;
		private Iterator<ResourcePersistentId> myCurrentIterator;
		private Set<ResourcePersistentId> myCurrentPids;
		private ResourcePersistentId myNext;
		private int myPageSize = myDaoConfig.getEverythingIncludesFetchPageSize();

		IncludesIterator(Set<ResourcePersistentId> thePidSet, RequestDetails theRequest) {
			myCurrentPids = new HashSet<>(thePidSet);
			myCurrentIterator = EMPTY_LONG_LIST.iterator();
			myRequest = theRequest;
		}

		private void fetchNext() {
			while (myNext == null) {

				if (myCurrentIterator.hasNext()) {
					myNext = myCurrentIterator.next();
					break;
				}

				Set<Include> includes = Collections.singleton(new Include("*", true));
				Set<ResourcePersistentId> newPids = loadIncludes(myContext, myEntityManager, myCurrentPids, includes, false, getParams().getLastUpdated(), mySearchUuid, myRequest);
				if (newPids.isEmpty()) {
					myNext = NO_MORE;
					break;
				}
				myCurrentIterator = newPids.iterator();
			}
		}

		@Override
		public boolean hasNext() {
			fetchNext();
			return !NO_MORE.equals(myNext);
		}

		@Override
		public ResourcePersistentId next() {
			fetchNext();
			ResourcePersistentId retVal = myNext;
			myNext = null;
			return retVal;
		}

	}

	private final class QueryIterator extends BaseIterator<ResourcePersistentId> implements IResultIterator {

		private final SearchRuntimeDetails mySearchRuntimeDetails;
		private final RequestDetails myRequest;
		private final boolean myHaveRawSqlHooks;
		private final boolean myHavePerftraceFoundIdHook;
		private boolean myFirst = true;
		private IncludesIterator myIncludesIterator;
		private ResourcePersistentId myNext;
		private Iterator<ResourcePersistentId> myPreResultsIterator;
		private ScrollableResultsIterator<Long> myResultsIterator;
		private SortSpec mySort;
		private boolean myStillNeedToFetchIncludes;
		private int mySkipCount = 0;

		private QueryIterator(SearchRuntimeDetails theSearchRuntimeDetails, RequestDetails theRequest) {
			mySearchRuntimeDetails = theSearchRuntimeDetails;
			mySort = myParams.getSort();
			myRequest = theRequest;

			// Includes are processed inline for $everything query
			if (myParams.getEverythingMode() != null) {
				myStillNeedToFetchIncludes = true;
			}

			myHavePerftraceFoundIdHook = JpaInterceptorBroadcaster.hasHooks(Pointcut.JPA_PERFTRACE_SEARCH_FOUND_ID, myInterceptorBroadcaster, myRequest);
			myHaveRawSqlHooks = JpaInterceptorBroadcaster.hasHooks(Pointcut.JPA_PERFTRACE_RAW_SQL, myInterceptorBroadcaster, myRequest);

		}

		private void fetchNext() {

			try {
				if (myHaveRawSqlHooks) {
					CurrentThreadCaptureQueriesListener.startCapturing();
				}

				// If we don't have a query yet, create one
				if (myResultsIterator == null) {
					if (myMaxResultsToFetch == null) {
						myMaxResultsToFetch = myDaoConfig.getFetchSizeDefaultMaximum();
					}

					final TypedQuery<Long> query = createQuery(mySort, myMaxResultsToFetch, false, myRequest);

					mySearchRuntimeDetails.setQueryStopwatch(new StopWatch());

					Query<Long> hibernateQuery = (Query<Long>) query;
					hibernateQuery.setFetchSize(myFetchSize);
					ScrollableResults scroll = hibernateQuery.scroll(ScrollMode.FORWARD_ONLY);
					myResultsIterator = new ScrollableResultsIterator<>(scroll);

					// If the query resulted in extra results being requested
					if (myAlsoIncludePids != null) {
						myPreResultsIterator = myAlsoIncludePids.iterator();
					}
				}

				if (myNext == null) {

					if (myPreResultsIterator != null && myPreResultsIterator.hasNext()) {
						while (myPreResultsIterator.hasNext()) {
							ResourcePersistentId next = myPreResultsIterator.next();
							if (next != null)
								if (myPidSet.add(next)) {
									myNext = next;
									break;
								}
						}
					}

					if (myNext == null) {
						while (myResultsIterator.hasNext()) {
							Long nextLong = myResultsIterator.next();
							if (myHavePerftraceFoundIdHook) {
								HookParams params = new HookParams()
									.add(Integer.class, System.identityHashCode(this))
									.add(Object.class, nextLong);
								JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, myRequest, Pointcut.JPA_PERFTRACE_SEARCH_FOUND_ID, params);
							}

							if (nextLong != null) {
								ResourcePersistentId next = new ResourcePersistentId(nextLong);
								if (myPidSet.add(next)) {
									myNext = next;
									break;
								} else {
									mySkipCount++;
								}
							}
						}
					}

					if (myNext == null) {
						if (myStillNeedToFetchIncludes) {
							myIncludesIterator = new IncludesIterator(myPidSet, myRequest);
							myStillNeedToFetchIncludes = false;
						}
						if (myIncludesIterator != null) {
							while (myIncludesIterator.hasNext()) {
								ResourcePersistentId next = myIncludesIterator.next();
								if (next != null)
									if (myPidSet.add(next)) {
										myNext = next;
										break;
									}
							}
							if (myNext == null) {
								myNext = NO_MORE;
							}
						} else {
							myNext = NO_MORE;
						}
					}

				} // if we need to fetch the next result

				mySearchRuntimeDetails.setFoundMatchesCount(myPidSet.size());

			} finally {
				if (myHaveRawSqlHooks) {
					SqlQueryList capturedQueries = CurrentThreadCaptureQueriesListener.getCurrentQueueAndStopCapturing();
					HookParams params = new HookParams()
						.add(RequestDetails.class, myRequest)
						.addIfMatchesType(ServletRequestDetails.class, myRequest)
						.add(SqlQueryList.class, capturedQueries);
					JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, myRequest, Pointcut.JPA_PERFTRACE_RAW_SQL, params);
				}
			}

			if (myFirst) {
				HookParams params = new HookParams()
					.add(RequestDetails.class, myRequest)
					.addIfMatchesType(ServletRequestDetails.class, myRequest)
					.add(SearchRuntimeDetails.class, mySearchRuntimeDetails);
				JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, myRequest, Pointcut.JPA_PERFTRACE_SEARCH_FIRST_RESULT_LOADED, params);
				myFirst = false;
			}

			if (NO_MORE.equals(myNext)) {
				HookParams params = new HookParams()
					.add(RequestDetails.class, myRequest)
					.addIfMatchesType(ServletRequestDetails.class, myRequest)
					.add(SearchRuntimeDetails.class, mySearchRuntimeDetails);
				JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, myRequest, Pointcut.JPA_PERFTRACE_SEARCH_SELECT_COMPLETE, params);
			}

		}

		@Override
		public boolean hasNext() {
			if (myNext == null) {
				fetchNext();
			}
			return !NO_MORE.equals(myNext);
		}

		@Override
		public ResourcePersistentId next() {
			fetchNext();
			ResourcePersistentId retVal = myNext;
			myNext = null;
			Validate.isTrue(!NO_MORE.equals(retVal), "No more elements");
			return retVal;
		}

		@Override
		public int getSkippedCount() {
			return mySkipCount;
		}

		@Override
		public void close() {
			if (myResultsIterator != null) {
				myResultsIterator.close();
			}
		}
	}

	private static class CountQueryIterator implements Iterator<Long> {
		private final TypedQuery<Long> myQuery;
		private boolean myCountLoaded;
		private Long myCount;

		CountQueryIterator(TypedQuery<Long> theQuery) {
			myQuery = theQuery;
		}

		@Override
		public boolean hasNext() {
			boolean retVal = myCount != null;
			if (!retVal) {
				if (myCountLoaded == false) {
					myCount = myQuery.getSingleResult();
					retVal = true;
					myCountLoaded = true;
				}
			}
			return retVal;
		}

		@Override
		public Long next() {
			Validate.isTrue(hasNext());
			Validate.isTrue(myCount != null);
			Long retVal = myCount;
			myCount = null;
			return retVal;
		}
	}

	private static List<Predicate> createLastUpdatedPredicates(final DateRangeParam theLastUpdated, CriteriaBuilder builder, From<?, ResourceTable> from) {
		List<Predicate> lastUpdatedPredicates = new ArrayList<>();
		if (theLastUpdated != null) {
			if (theLastUpdated.getLowerBoundAsInstant() != null) {
				ourLog.debug("LastUpdated lower bound: {}", new InstantDt(theLastUpdated.getLowerBoundAsInstant()));
				Predicate predicateLower = builder.greaterThanOrEqualTo(from.get("myUpdated"), theLastUpdated.getLowerBoundAsInstant());
				lastUpdatedPredicates.add(predicateLower);
			}
			if (theLastUpdated.getUpperBoundAsInstant() != null) {
				Predicate predicateUpper = builder.lessThanOrEqualTo(from.get("myUpdated"), theLastUpdated.getUpperBoundAsInstant());
				lastUpdatedPredicates.add(predicateUpper);
			}
		}
		return lastUpdatedPredicates;
	}

	private static List<ResourcePersistentId> filterResourceIdsByLastUpdated(EntityManager theEntityManager, final DateRangeParam theLastUpdated, Collection<ResourcePersistentId> thePids) {
		if (thePids.isEmpty()) {
			return Collections.emptyList();
		}
		CriteriaBuilder builder = theEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceTable> from = cq.from(ResourceTable.class);
		cq.select(from.get("myId").as(Long.class));

		List<Predicate> lastUpdatedPredicates = createLastUpdatedPredicates(theLastUpdated, builder, from);
		lastUpdatedPredicates.add(from.get("myId").as(Long.class).in(ResourcePersistentId.toLongList(thePids)));

		cq.where(SearchBuilder.toArray(lastUpdatedPredicates));
		TypedQuery<Long> query = theEntityManager.createQuery(cq);

		return ResourcePersistentId.fromLongList(query.getResultList());
	}

	private static Predicate[] toArray(List<Predicate> thePredicates) {
		return thePredicates.toArray(new Predicate[0]);
	}
}
