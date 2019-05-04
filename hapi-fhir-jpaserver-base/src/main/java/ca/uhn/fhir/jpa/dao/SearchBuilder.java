package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedCompositeStringUniqueDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamUriDao;
import ca.uhn.fhir.jpa.dao.data.IResourceSearchViewDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTagDao;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.dao.r4.MatchResourceUrlService;
import ca.uhn.fhir.jpa.entity.ResourceSearchView;
import ca.uhn.fhir.jpa.model.entity.*;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.model.util.StringNormalizer;
import ca.uhn.fhir.jpa.searchparam.JpaRuntimeSearchParam;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.ResourceMetaParams;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.term.IHapiTerminologySvc;
import ca.uhn.fhir.jpa.term.VersionIndependentConcept;
import ca.uhn.fhir.jpa.util.BaseIterator;
import ca.uhn.fhir.jpa.util.ScrollableResultsIterator;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.base.composite.BaseIdentifierDt;
import ca.uhn.fhir.model.base.composite.BaseQuantityDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.hibernate.query.criteria.internal.predicate.BooleanStaticAssertionPredicate;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.*;

/**
 * The SearchBuilder is responsible for actually forming the SQL query that handles
 * searches for resources
 */
@Component
@Scope("prototype")
public class SearchBuilder implements ISearchBuilder {

	private static final List<Long> EMPTY_LONG_LIST = Collections.unmodifiableList(new ArrayList<>());
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchBuilder.class);
	/**
	 * @see #loadResourcesByPid(Collection, List, Set, boolean, EntityManager, FhirContext, IDao)
	 * for an explanation of why we use the constant 800
	 */
	private static final int MAXIMUM_PAGE_SIZE = 800;
	private static Long NO_MORE = -1L;
	private static HandlerTypeEnum ourLastHandlerMechanismForUnitTest;
	private static SearchParameterMap ourLastHandlerParamsForUnitTest;
	private static String ourLastHandlerThreadForUnitTest;
	private static boolean ourTrackHandlersForUnitTest;
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
	private IHapiTerminologySvc myTerminologySvc;
	@Autowired
	private MatchResourceUrlService myMatchResourceUrlService;
	@Autowired
	private MatchUrlService myMatchUrlService;
	@Autowired
	private IResourceIndexedCompositeStringUniqueDao myResourceIndexedCompositeStringUniqueDao;
	private List<Long> myAlsoIncludePids;
	private CriteriaBuilder myBuilder;
	private BaseHapiFhirDao<?> myCallingDao;
	private Map<JoinKey, Join<?, ?>> myIndexJoins = Maps.newHashMap();
	private SearchParameterMap myParams;
	private ArrayList<Predicate> myPredicates;
	private String myResourceName;
	private AbstractQuery<Long> myResourceTableQuery;
	private Root<ResourceTable> myResourceTableRoot;
	private Class<? extends IBaseResource> myResourceType;
	private String mySearchUuid;
	private int myFetchSize;
	private Integer myMaxResultsToFetch;
	private Set<Long> myPidSet;
	private boolean myHaveIndexJoins = false;

	/**
	 * Constructor
	 */
	SearchBuilder(BaseHapiFhirDao<?> theDao) {
		myCallingDao = theDao;
		myDaoConfig = theDao.getConfig();
		myDontUseHashesForSearch = myDaoConfig.getDisableHashBasedSearches();
	}

	@Override
	public void setMaxResultsToFetch(Integer theMaxResultsToFetch) {
		myMaxResultsToFetch = theMaxResultsToFetch;
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

	private void addPredicateDate(String theResourceName, String theParamName, List<? extends IQueryParameterType> theList) {

		Join<ResourceTable, ResourceIndexedSearchParamDate> join = createJoin(JoinEnum.DATE, theParamName);

		if (theList.get(0).getMissing() != null) {
			Boolean missing = theList.get(0).getMissing();
			addPredicateParamMissing(theResourceName, theParamName, missing, join);
			return;
		}

		List<Predicate> codePredicates = new ArrayList<>();
		for (IQueryParameterType nextOr : theList) {
			Predicate p = createPredicateDate(nextOr, theResourceName, theParamName, myBuilder, join);
			codePredicates.add(p);
		}

		Predicate orPredicates = myBuilder.or(toArray(codePredicates));
		myPredicates.add(orPredicates);

	}

	private void addPredicateHas(List<List<IQueryParameterType>> theHasParameters) {

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
				parameters.add(QualifiedParamList.singleton(paramName, next.getValueAsQueryToken(myContext)));
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

			Subquery<Long> subQ = createLinkSubquery(true, parameterName, targetResourceType, orValues);

			Join<ResourceTable, ResourceLink> join = myResourceTableRoot.join("myResourceLinksAsTarget", JoinType.LEFT);
			Predicate pathPredicate = createResourceLinkPathPredicate(targetResourceType, paramReference, join);
			Predicate pidPredicate = join.get("mySourceResourcePid").in(subQ);
			Predicate andPredicate = myBuilder.and(pathPredicate, pidPredicate);
			myPredicates.add(andPredicate);
		}
	}

	private void addPredicateLanguage(List<List<IQueryParameterType>> theList) {
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

			Predicate predicate = myResourceTableRoot.get("myLanguage").as(String.class).in(values);
			myPredicates.add(predicate);
		}

	}

	private void addPredicateNumber(String theResourceName, String theParamName, List<? extends IQueryParameterType> theList) {

		Join<ResourceTable, ResourceIndexedSearchParamNumber> join = createJoin(JoinEnum.NUMBER, theParamName);

		if (theList.get(0).getMissing() != null) {
			addPredicateParamMissing(theResourceName, theParamName, theList.get(0).getMissing(), join);
			return;
		}

		List<Predicate> codePredicates = new ArrayList<>();
		for (IQueryParameterType nextOr : theList) {

			if (nextOr instanceof NumberParam) {
				NumberParam param = (NumberParam) nextOr;

				BigDecimal value = param.getValue();
				if (value == null) {
					continue;
				}

				final Expression<BigDecimal> fromObj = join.get("myValue");
				ParamPrefixEnum prefix = ObjectUtils.defaultIfNull(param.getPrefix(), ParamPrefixEnum.EQUAL);
				String invalidMessageName = "invalidNumberPrefix";

				Predicate predicateNumeric = createPredicateNumeric(theResourceName, theParamName, join, myBuilder, nextOr, prefix, value, fromObj, invalidMessageName);
				Predicate predicateOuter = combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, join, predicateNumeric);
				codePredicates.add(predicateOuter);

			} else {
				throw new IllegalArgumentException("Invalid token type: " + nextOr.getClass());
			}

		}

		myPredicates.add(myBuilder.or(toArray(codePredicates)));
	}

	private void addPredicateParamMissing(String theResourceName, String theParamName, boolean theMissing) {
//		if (myDontUseHashesForSearch) {
//			Join<ResourceTable, SearchParamPresent> paramPresentJoin = myResourceTableRoot.join("mySearchParamPresents", JoinType.LEFT);
//			Join<Object, Object> paramJoin = paramPresentJoin.join("mySearchParam", JoinType.LEFT);
//
//			myPredicates.add(myBuilder.equal(paramJoin.get("myResourceName"), theResourceName));
//			myPredicates.add(myBuilder.equal(paramJoin.get("myParamName"), theParamName));
//			myPredicates.add(myBuilder.equal(paramPresentJoin.get("myPresent"), !theMissing));
//		}

		Join<ResourceTable, SearchParamPresent> paramPresentJoin = myResourceTableRoot.join("mySearchParamPresents", JoinType.LEFT);

		Expression<Long> hashPresence = paramPresentJoin.get("myHashPresence").as(Long.class);
		Long hash = SearchParamPresent.calculateHashPresence(theResourceName, theParamName, !theMissing);
		myPredicates.add(myBuilder.equal(hashPresence, hash));
	}

	private void addPredicateParamMissing(String theResourceName, String theParamName, boolean theMissing, Join<ResourceTable, ? extends BaseResourceIndexedSearchParam> theJoin) {

		myPredicates.add(myBuilder.equal(theJoin.get("myResourceType"), theResourceName));
		myPredicates.add(myBuilder.equal(theJoin.get("myParamName"), theParamName));
		myPredicates.add(myBuilder.equal(theJoin.get("myMissing"), theMissing));
	}

	private void addPredicateQuantity(String theResourceName, String theParamName, List<? extends IQueryParameterType> theList) {
		Join<ResourceTable, ResourceIndexedSearchParamQuantity> join = createJoin(JoinEnum.QUANTITY, theParamName);

		if (theList.get(0).getMissing() != null) {
			addPredicateParamMissing(theResourceName, theParamName, theList.get(0).getMissing(), join);
			return;
		}

		List<Predicate> codePredicates = new ArrayList<>();
		for (IQueryParameterType nextOr : theList) {

			Predicate singleCode = createPredicateQuantity(nextOr, theResourceName, theParamName, myBuilder, join);
			codePredicates.add(singleCode);
		}

		myPredicates.add(myBuilder.or(toArray(codePredicates)));
	}

	/**
	 * Add reference predicate to the current search
	 */
	private void addPredicateReference(String theResourceName, String theParamName, List<? extends IQueryParameterType> theList) {
		assert theParamName.contains(".") == false;

		if (theList.get(0).getMissing() != null) {
			addPredicateParamMissing(theResourceName, theParamName, theList.get(0).getMissing());
			return;
		}

		Join<ResourceTable, ResourceLink> join = createJoin(JoinEnum.REFERENCE, theParamName);

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

					addPredicateReferenceWithChain(theResourceName, theParamName, theList, join, new ArrayList<>(), ref);
					return;

				}

			} else {
				throw new IllegalArgumentException("Invalid token type (expecting ReferenceParam): " + nextOr.getClass());
			}

		}

		List<Predicate> codePredicates = new ArrayList<>();

		// Resources by ID
		List<Long> targetPids = myIdHelperService.translateForcedIdToPids(targetIds);
		if (!targetPids.isEmpty()) {
			ourLog.debug("Searching for resource link with target PIDs: {}", targetPids);
			Predicate pathPredicate = createResourceLinkPathPredicate(theResourceName, theParamName, join);
			Predicate pidPredicate = join.get("myTargetResourcePid").in(targetPids);
			codePredicates.add(myBuilder.and(pathPredicate, pidPredicate));
		}

		// Resources by fully qualified URL
		if (!targetQualifiedUrls.isEmpty()) {
			ourLog.debug("Searching for resource link with target URLs: {}", targetQualifiedUrls);
			Predicate pathPredicate = createResourceLinkPathPredicate(theResourceName, theParamName, join);
			Predicate pidPredicate = join.get("myTargetResourceUrl").in(targetQualifiedUrls);
			codePredicates.add(myBuilder.and(pathPredicate, pidPredicate));
		}

		if (codePredicates.size() > 0) {
			myPredicates.add(myBuilder.or(toArray(codePredicates)));
		} else {
			// Add a predicate that will never match
			Predicate pidPredicate = join.get("myTargetResourcePid").in(-1L);
			myPredicates.clear();
			myPredicates.add(pidPredicate);
		}
	}

	private void addPredicateReferenceWithChain(String theResourceName, String theParamName, List<? extends IQueryParameterType> theList, Join<ResourceTable, ResourceLink> theJoin, List<Predicate> theCodePredicates, ReferenceParam theRef) {
		final List<Class<? extends IBaseResource>> resourceTypes;
		String resourceId;
		if (!theRef.getValue().matches("[a-zA-Z]+/.*")) {

			RuntimeSearchParam param = mySearchParamRegistry.getActiveSearchParam(theResourceName, theParamName);
			resourceTypes = new ArrayList<>();

			Set<String> targetTypes = param.getTargets();

			if (targetTypes != null && !targetTypes.isEmpty()) {
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

			resourceId = theRef.getValue();

		} else {
			try {
				RuntimeResourceDefinition resDef = myContext.getResourceDefinition(theRef.getResourceType());
				resourceTypes = new ArrayList<>(1);
				resourceTypes.add(resDef.getImplementingClass());
				resourceId = theRef.getIdPart();
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

			Subquery<Long> subQ = createLinkSubquery(foundChainMatch, chain, subResourceName, orValues);

			Predicate pathPredicate = createResourceLinkPathPredicate(theResourceName, theParamName, theJoin);
			Predicate pidPredicate = theJoin.get("myTargetResourcePid").in(subQ);
			Predicate andPredicate = myBuilder.and(pathPredicate, pidPredicate);
			theCodePredicates.add(andPredicate);

		}

		if (!foundChainMatch) {
			throw new InvalidRequestException(myContext.getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "invalidParameterChain", theParamName + '.' + theRef.getChain()));
		}

		myPredicates.add(myBuilder.or(toArray(theCodePredicates)));
	}

	private Subquery<Long> createLinkSubquery(boolean theFoundChainMatch, String theChain, String theSubResourceName, List<IQueryParameterType> theOrValues) {
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
		Map<JoinKey, Join<?, ?>> stackIndexJoins = myIndexJoins;
		myResourceTableRoot = subQfrom;
		myPredicates = Lists.newArrayList();
		myIndexJoins = Maps.newHashMap();

		// Create the subquery predicates
		myPredicates.add(myBuilder.equal(myResourceTableRoot.get("myResourceType"), theSubResourceName));
		myPredicates.add(myBuilder.isNull(myResourceTableRoot.get("myDeleted")));

		if (theFoundChainMatch) {
			searchForIdsWithAndOr(theSubResourceName, theChain, andOrParams);
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

	private void addPredicateResourceId(List<List<IQueryParameterType>> theValues) {
		for (List<? extends IQueryParameterType> nextValue : theValues) {
			Set<Long> orPids = new HashSet<>();
			for (IQueryParameterType next : nextValue) {
				String value = next.getValueAsQueryToken(myContext);
				if (value != null && value.startsWith("|")) {
					value = value.substring(1);
				}

				IdType valueAsId = new IdType(value);
				if (isNotBlank(value)) {
					try {
						Long pid = myIdHelperService.translateForcedIdToPid(myResourceName, valueAsId.getIdPart());
						orPids.add(pid);
					} catch (ResourceNotFoundException e) {
						// This is not an error in a search, it just results in no matchesFhirResourceDaoR4InterceptorTest
						ourLog.debug("Resource ID {} was requested but does not exist", valueAsId.getIdPart());
					}
				}
			}

			if (orPids.size() > 0) {
				Predicate nextPredicate = myResourceTableRoot.get("myId").as(Long.class).in(orPids);
				myPredicates.add(nextPredicate);
			} else {
				// This will never match
				Predicate nextPredicate = myBuilder.equal(myResourceTableRoot.get("myId").as(Long.class), -1);
				myPredicates.add(nextPredicate);
			}

		}
	}

	private void addPredicateString(String theResourceName, String theParamName, List<? extends IQueryParameterType> theList) {

		Join<ResourceTable, ResourceIndexedSearchParamString> join = createJoin(JoinEnum.STRING, theParamName);

		if (theList.get(0).getMissing() != null) {
			addPredicateParamMissing(theResourceName, theParamName, theList.get(0).getMissing(), join);
			return;
		}

		List<Predicate> codePredicates = new ArrayList<>();
		for (IQueryParameterType nextOr : theList) {
			Predicate singleCode = createPredicateString(nextOr, theResourceName, theParamName, myBuilder, join);
			codePredicates.add(singleCode);
		}

		myPredicates.add(myBuilder.or(toArray(codePredicates)));

	}

	private void addPredicateTag(List<List<IQueryParameterType>> theList, String theParamName) {
		TagTypeEnum tagType;
		if (Constants.PARAM_TAG.equals(theParamName)) {
			tagType = TagTypeEnum.TAG;
		} else if (Constants.PARAM_PROFILE.equals(theParamName)) {
			tagType = TagTypeEnum.PROFILE;
		} else if (Constants.PARAM_SECURITY.equals(theParamName)) {
			tagType = TagTypeEnum.SECURITY_LABEL;
		} else {
			throw new IllegalArgumentException("Param name: " + theParamName); // shouldn't happen
		}

		List<Pair<String, String>> notTags = Lists.newArrayList();
		for (List<? extends IQueryParameterType> nextAndParams : theList) {
			for (IQueryParameterType nextOrParams : nextAndParams) {
				if (nextOrParams instanceof TokenParam) {
					TokenParam param = (TokenParam) nextOrParams;
					if (param.getModifier() == TokenParamModifier.NOT) {
						if (isNotBlank(param.getSystem()) || isNotBlank(param.getValue())) {
							notTags.add(Pair.of(param.getSystem(), param.getValue()));
						}
					}
				}
			}
		}

		/*
		 * We have a parameter of ResourceType?_tag:not=foo This means match resources that don't have the given tag(s)
		 */
		if (notTags.isEmpty() == false) {
			// CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
			// CriteriaQuery<Long> cq = builder.createQuery(Long.class);
			// Root<ResourceTable> from = cq.from(ResourceTable.class);
			// cq.select(from.get("myId").as(Long.class));
			//
			// Subquery<Long> subQ = cq.subquery(Long.class);
			// Root<ResourceTag> subQfrom = subQ.from(ResourceTag.class);
			// subQ.select(subQfrom.get("myResourceId").as(Long.class));
			// Predicate subQname = builder.equal(subQfrom.get("myParamName"), theParamName);
			// Predicate subQtype = builder.equal(subQfrom.get("myResourceType"), myResourceName);
			// subQ.where(builder.and(subQtype, subQname));
			//
			// List<Predicate> predicates = new ArrayList<Predicate>();
			// predicates.add(builder.not(builder.in(from.get("myId")).value(subQ)));
			// predicates.add(builder.equal(from.get("myResourceType"), myResourceName));
			// predicates.add(builder.isNull(from.get("myDeleted")));
			// createPredicateResourceId(builder, cq, predicates, from.get("myId").as(Long.class));
		}

		for (List<? extends IQueryParameterType> nextAndParams : theList) {
			boolean haveTags = false;
			for (IQueryParameterType nextParamUncasted : nextAndParams) {
				if (nextParamUncasted instanceof TokenParam) {
					TokenParam nextParam = (TokenParam) nextParamUncasted;
					if (isNotBlank(nextParam.getValue())) {
						haveTags = true;
					} else if (isNotBlank(nextParam.getSystem())) {
						throw new InvalidRequestException("Invalid " + theParamName + " parameter (must supply a value/code and not just a system): " + nextParam.getValueAsQueryToken(myContext));
					}
				} else {
					UriParam nextParam = (UriParam) nextParamUncasted;
					if (isNotBlank(nextParam.getValue())) {
						haveTags = true;
					}
				}
			}
			if (!haveTags) {
				continue;
			}

			boolean paramInverted = false;
			List<Pair<String, String>> tokens = Lists.newArrayList();
			for (IQueryParameterType nextOrParams : nextAndParams) {
				String code;
				String system;
				if (nextOrParams instanceof TokenParam) {
					TokenParam nextParam = (TokenParam) nextOrParams;
					code = nextParam.getValue();
					system = nextParam.getSystem();
					if (nextParam.getModifier() == TokenParamModifier.NOT) {
						paramInverted = true;
					}
				} else {
					UriParam nextParam = (UriParam) nextOrParams;
					code = nextParam.getValue();
					system = null;
				}

				if (isNotBlank(code)) {
					tokens.add(Pair.of(system, code));
				}
			}

			if (tokens.isEmpty()) {
				continue;
			}

			if (paramInverted) {
				ourLog.debug("Searching for _tag:not");

				Subquery<Long> subQ = myResourceTableQuery.subquery(Long.class);
				Root<ResourceTag> subQfrom = subQ.from(ResourceTag.class);
				subQ.select(subQfrom.get("myResourceId").as(Long.class));

				myPredicates.add(myBuilder.not(myBuilder.in(myResourceTableRoot.get("myId")).value(subQ)));

				Subquery<Long> defJoin = subQ.subquery(Long.class);
				Root<TagDefinition> defJoinFrom = defJoin.from(TagDefinition.class);
				defJoin.select(defJoinFrom.get("myId").as(Long.class));

				subQ.where(subQfrom.get("myTagId").as(Long.class).in(defJoin));

				List<Predicate> orPredicates = createPredicateTagList(defJoinFrom, myBuilder, tagType, tokens);
				defJoin.where(toArray(orPredicates));

				continue;
			}

			Join<ResourceTable, ResourceTag> tagJoin = myResourceTableRoot.join("myTags", JoinType.LEFT);
			From<ResourceTag, TagDefinition> defJoin = tagJoin.join("myTag");

			List<Predicate> orPredicates = createPredicateTagList(defJoin, myBuilder, tagType, tokens);
			myPredicates.add(myBuilder.or(toArray(orPredicates)));

		}

	}

	private void addPredicateToken(String theResourceName, String theParamName, List<? extends IQueryParameterType> theList) {

		if (theList.get(0).getMissing() != null) {
			Join<ResourceTable, ResourceIndexedSearchParamToken> join = createJoin(JoinEnum.TOKEN, theParamName);
			addPredicateParamMissing(theResourceName, theParamName, theList.get(0).getMissing(), join);
			return;
		}

		List<Predicate> codePredicates = new ArrayList<>();
		List<IQueryParameterType> tokens = new ArrayList<>();
		for (IQueryParameterType nextOr : theList) {

			if (nextOr instanceof TokenParam) {
				TokenParam id = (TokenParam) nextOr;
				if (id.isText()) {
					addPredicateString(theResourceName, theParamName, theList);
					break;
				}
			}

			tokens.add(nextOr);
		}

		if (tokens.isEmpty()) {
			return;
		}

		Join<ResourceTable, ResourceIndexedSearchParamToken> join = createJoin(JoinEnum.TOKEN, theParamName);
		List<Predicate> singleCode = createPredicateToken(tokens, theResourceName, theParamName, myBuilder, join);
		codePredicates.addAll(singleCode);

		Predicate spPredicate = myBuilder.or(toArray(codePredicates));
		myPredicates.add(spPredicate);
	}

	private void addPredicateUri(String theResourceName, String theParamName, List<? extends IQueryParameterType> theList) {

		Join<ResourceTable, ResourceIndexedSearchParamUri> join = createJoin(JoinEnum.URI, theParamName);

		if (theList.get(0).getMissing() != null) {
			addPredicateParamMissing(theResourceName, theParamName, theList.get(0).getMissing(), join);
			return;
		}

		List<Predicate> codePredicates = new ArrayList<>();
		for (IQueryParameterType nextOr : theList) {

			if (nextOr instanceof UriParam) {
				UriParam param = (UriParam) nextOr;

				String value = param.getValue();
				if (value == null) {
					continue;
				}

				if (param.getQualifier() == UriParamQualifierEnum.ABOVE) {

					/*
					 * :above is an inefficient query- It means that the user is supplying a more specific URL (say
					 * http://example.com/foo/bar/baz) and that we should match on any URLs that are less
					 * specific but otherwise the same. For example http://example.com and http://example.com/foo would both
					 * match.
					 *
					 * We do this by querying the DB for all candidate URIs and then manually checking each one. This isn't
					 * very efficient, but this is also probably not a very common type of query to do.
					 *
					 * If we ever need to make this more efficient, lucene could certainly be used as an optimization.
					 */
					ourLog.info("Searching for candidate URI:above parameters for Resource[{}] param[{}]", myResourceName, theParamName);
					Collection<String> candidates = myResourceIndexedSearchParamUriDao.findAllByResourceTypeAndParamName(myResourceName, theParamName);
					List<String> toFind = new ArrayList<>();
					for (String next : candidates) {
						if (value.length() >= next.length()) {
							if (value.substring(0, next.length()).equals(next)) {
								toFind.add(next);
							}
						}
					}

					if (toFind.isEmpty()) {
						continue;
					}

					Predicate uriPredicate = join.get("myUri").as(String.class).in(toFind);
					Predicate hashAndUriPredicate = combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, join, uriPredicate);
					codePredicates.add(hashAndUriPredicate);

				} else if (param.getQualifier() == UriParamQualifierEnum.BELOW) {

					Predicate uriPredicate = myBuilder.like(join.get("myUri").as(String.class), createLeftMatchLikeExpression(value));
					Predicate hashAndUriPredicate = combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, join, uriPredicate);
					codePredicates.add(hashAndUriPredicate);

				} else {

					if (myDontUseHashesForSearch) {

						Predicate predicate = myBuilder.equal(join.get("myUri").as(String.class), value);
						codePredicates.add(predicate);

					} else {

						long hashUri = ResourceIndexedSearchParamUri.calculateHashUri(theResourceName, theParamName, value);
						Predicate hashPredicate = myBuilder.equal(join.get("myHashUri"), hashUri);
						codePredicates.add(hashPredicate);

					}
				}

			} else {
				throw new IllegalArgumentException("Invalid URI type: " + nextOr.getClass());
			}

		}

		/*
		 * If we haven't found any of the requested URIs in the candidates, then we'll
		 * just add a predicate that can never match
		 */
		if (codePredicates.isEmpty()) {
			Predicate predicate = myBuilder.isNull(join.get("myMissing").as(String.class));
			myPredicates.add(predicate);
			return;
		}

		Predicate orPredicate = myBuilder.or(toArray(codePredicates));
		myPredicates.add(orPredicate);
	}

	private Predicate combineParamIndexPredicateWithParamNamePredicate(String theResourceName, String theParamName, From<?, ? extends BaseResourceIndexedSearchParam> theFrom, Predicate thePredicate) {
		if (myDontUseHashesForSearch) {
			Predicate resourceTypePredicate = myBuilder.equal(theFrom.get("myResourceType"), theResourceName);
			Predicate paramNamePredicate = myBuilder.equal(theFrom.get("myParamName"), theParamName);
			Predicate outerPredicate = myBuilder.and(resourceTypePredicate, paramNamePredicate, thePredicate);
			return outerPredicate;
		}

		long hashIdentity = BaseResourceIndexedSearchParam.calculateHashIdentity(theResourceName, theParamName);
		Predicate hashIdentityPredicate = myBuilder.equal(theFrom.get("myHashIdentity"), hashIdentity);
		return myBuilder.and(hashIdentityPredicate, thePredicate);
	}

	private Predicate createCompositeParamPart(String theResourceName, Root<ResourceTable> theRoot, RuntimeSearchParam theParam, IQueryParameterType leftValue) {
		Predicate retVal = null;
		switch (theParam.getParamType()) {
			case STRING: {
				From<ResourceIndexedSearchParamString, ResourceIndexedSearchParamString> stringJoin = theRoot.join("myParamsString", JoinType.INNER);
				retVal = createPredicateString(leftValue, theResourceName, theParam.getName(), myBuilder, stringJoin);
				break;
			}
			case TOKEN: {
				From<ResourceIndexedSearchParamToken, ResourceIndexedSearchParamToken> tokenJoin = theRoot.join("myParamsToken", JoinType.INNER);
				List<IQueryParameterType> tokens = Collections.singletonList(leftValue);
				List<Predicate> tokenPredicates = createPredicateToken(tokens, theResourceName, theParam.getName(), myBuilder, tokenJoin);
				retVal = myBuilder.and(tokenPredicates.toArray(new Predicate[0]));
				break;
			}
			case DATE: {
				From<ResourceIndexedSearchParamDate, ResourceIndexedSearchParamDate> dateJoin = theRoot.join("myParamsDate", JoinType.INNER);
				retVal = createPredicateDate(leftValue, theResourceName, theParam.getName(), myBuilder, dateJoin);
				break;
			}
			case QUANTITY: {
				From<ResourceIndexedSearchParamQuantity, ResourceIndexedSearchParamQuantity> dateJoin = theRoot.join("myParamsQuantity", JoinType.INNER);
				retVal = createPredicateQuantity(leftValue, theResourceName, theParam.getName(), myBuilder, dateJoin);
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

	@SuppressWarnings("unchecked")
	private <T> Join<ResourceTable, T> createJoin(JoinEnum theType, String theSearchParameterName) {
		Join<ResourceTable, ResourceIndexedSearchParamDate> join = null;
		switch (theType) {
			case DATE:
				join = myResourceTableRoot.join("myParamsDate", JoinType.LEFT);
				break;
			case NUMBER:
				join = myResourceTableRoot.join("myParamsNumber", JoinType.LEFT);
				break;
			case QUANTITY:
				join = myResourceTableRoot.join("myParamsQuantity", JoinType.LEFT);
				break;
			case REFERENCE:
				join = myResourceTableRoot.join("myResourceLinks", JoinType.LEFT);
				break;
			case STRING:
				join = myResourceTableRoot.join("myParamsString", JoinType.LEFT);
				break;
			case URI:
				join = myResourceTableRoot.join("myParamsUri", JoinType.LEFT);
				break;
			case TOKEN:
				join = myResourceTableRoot.join("myParamsToken", JoinType.LEFT);
				break;
		}

		JoinKey key = new JoinKey(theSearchParameterName, theType);
		myIndexJoins.put(key, join);
		myHaveIndexJoins = true;

		return (Join<ResourceTable, T>) join;
	}

	private Predicate createPredicateDate(IQueryParameterType theParam, String theResourceName, String theParamName, CriteriaBuilder theBuilder, From<?, ResourceIndexedSearchParamDate> theFrom) {
		Predicate p;
		if (theParam instanceof DateParam) {
			DateParam date = (DateParam) theParam;
			if (!date.isEmpty()) {
				DateRangeParam range = new DateRangeParam(date);
				p = createPredicateDateFromRange(theBuilder, theFrom, range);
			} else {
				// TODO: handle missing date param?
				p = null;
			}
		} else if (theParam instanceof DateRangeParam) {
			DateRangeParam range = (DateRangeParam) theParam;
			p = createPredicateDateFromRange(theBuilder, theFrom, range);
		} else {
			throw new IllegalArgumentException("Invalid token type: " + theParam.getClass());
		}

		return combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, theFrom, p);
	}

	private Predicate createPredicateDateFromRange(CriteriaBuilder theBuilder, From<?, ResourceIndexedSearchParamDate> theFrom, DateRangeParam theRange) {
		Date lowerBound = theRange.getLowerBoundAsInstant();
		Date upperBound = theRange.getUpperBoundAsInstant();

		Predicate lb = null;
		if (lowerBound != null) {
			Predicate gt = theBuilder.greaterThanOrEqualTo(theFrom.get("myValueLow"), lowerBound);
			Predicate lt = theBuilder.greaterThanOrEqualTo(theFrom.get("myValueHigh"), lowerBound);
			if (theRange.getLowerBound().getPrefix() == ParamPrefixEnum.STARTS_AFTER || theRange.getLowerBound().getPrefix() == ParamPrefixEnum.EQUAL) {
				lb = gt;
			} else {
				lb = theBuilder.or(gt, lt);
			}
		}

		Predicate ub = null;
		if (upperBound != null) {
			Predicate gt = theBuilder.lessThanOrEqualTo(theFrom.get("myValueLow"), upperBound);
			Predicate lt = theBuilder.lessThanOrEqualTo(theFrom.get("myValueHigh"), upperBound);
			if (theRange.getUpperBound().getPrefix() == ParamPrefixEnum.ENDS_BEFORE || theRange.getUpperBound().getPrefix() == ParamPrefixEnum.EQUAL) {
				ub = lt;
			} else {
				ub = theBuilder.or(gt, lt);
			}
		}

		ourLog.trace("Date range is {} - {}", lowerBound, upperBound);

		if (lb != null && ub != null) {
			return (theBuilder.and(lb, ub));
		} else if (lb != null) {
			return (lb);
		} else {
			return (ub);
		}
	}

	private Predicate createPredicateNumeric(String theResourceName, String theParamName, From<?, ? extends BaseResourceIndexedSearchParam> theFrom, CriteriaBuilder builder,
														  IQueryParameterType theParam, ParamPrefixEnum thePrefix, BigDecimal theValue, final Expression<BigDecimal> thePath,
														  String invalidMessageName) {
		Predicate num;
		switch (thePrefix) {
			case GREATERTHAN:
				num = builder.gt(thePath, theValue);
				break;
			case GREATERTHAN_OR_EQUALS:
				num = builder.ge(thePath, theValue);
				break;
			case LESSTHAN:
				num = builder.lt(thePath, theValue);
				break;
			case LESSTHAN_OR_EQUALS:
				num = builder.le(thePath, theValue);
				break;
			case APPROXIMATE:
			case EQUAL:
			case NOT_EQUAL:
				BigDecimal mul = calculateFuzzAmount(thePrefix, theValue);
				BigDecimal low = theValue.subtract(mul, MathContext.DECIMAL64);
				BigDecimal high = theValue.add(mul, MathContext.DECIMAL64);
				Predicate lowPred;
				Predicate highPred;
				if (thePrefix != ParamPrefixEnum.NOT_EQUAL) {
					lowPred = builder.ge(thePath.as(BigDecimal.class), low);
					highPred = builder.le(thePath.as(BigDecimal.class), high);
					num = builder.and(lowPred, highPred);
					ourLog.trace("Searching for {} <= val <= {}", low, high);
				} else {
					// Prefix was "ne", so reverse it!
					lowPred = builder.lt(thePath.as(BigDecimal.class), low);
					highPred = builder.gt(thePath.as(BigDecimal.class), high);
					num = builder.or(lowPred, highPred);
				}
				break;
			case ENDS_BEFORE:
			case STARTS_AFTER:
			default:
				String msg = myContext.getLocalizer().getMessage(SearchBuilder.class, invalidMessageName, thePrefix.getValue(), theParam.getValueAsQueryToken(myContext));
				throw new InvalidRequestException(msg);
		}

		if (theParamName == null) {
			return num;
		}
		return num;
	}

	private Predicate createPredicateQuantity(IQueryParameterType theParam, String theResourceName, String theParamName, CriteriaBuilder theBuilder,
															From<?, ResourceIndexedSearchParamQuantity> theFrom) {
		String systemValue;
		String unitsValue;
		ParamPrefixEnum cmpValue;
		BigDecimal valueValue;

		if (theParam instanceof BaseQuantityDt) {
			BaseQuantityDt param = (BaseQuantityDt) theParam;
			systemValue = param.getSystemElement().getValueAsString();
			unitsValue = param.getUnitsElement().getValueAsString();
			cmpValue = ParamPrefixEnum.forValue(param.getComparatorElement().getValueAsString());
			valueValue = param.getValueElement().getValue();
		} else if (theParam instanceof QuantityParam) {
			QuantityParam param = (QuantityParam) theParam;
			systemValue = param.getSystem();
			unitsValue = param.getUnits();
			cmpValue = param.getPrefix();
			valueValue = param.getValue();
		} else {
			throw new IllegalArgumentException("Invalid quantity type: " + theParam.getClass());
		}

		if (myDontUseHashesForSearch) {
			Predicate system = null;
			if (!isBlank(systemValue)) {
				system = theBuilder.equal(theFrom.get("mySystem"), systemValue);
			}

			Predicate code = null;
			if (!isBlank(unitsValue)) {
				code = theBuilder.equal(theFrom.get("myUnits"), unitsValue);
			}

			cmpValue = ObjectUtils.defaultIfNull(cmpValue, ParamPrefixEnum.EQUAL);
			final Expression<BigDecimal> path = theFrom.get("myValue");
			String invalidMessageName = "invalidQuantityPrefix";

			Predicate num = createPredicateNumeric(theResourceName, null, theFrom, theBuilder, theParam, cmpValue, valueValue, path, invalidMessageName);

			Predicate singleCode;
			if (system == null && code == null) {
				singleCode = num;
			} else if (system == null) {
				singleCode = theBuilder.and(code, num);
			} else if (code == null) {
				singleCode = theBuilder.and(system, num);
			} else {
				singleCode = theBuilder.and(system, code, num);
			}

			return combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, theFrom, singleCode);
		}

		Predicate hashPredicate;
		if (!isBlank(systemValue) && !isBlank(unitsValue)) {
			long hash = ResourceIndexedSearchParamQuantity.calculateHashSystemAndUnits(theResourceName, theParamName, systemValue, unitsValue);
			hashPredicate = myBuilder.equal(theFrom.get("myHashIdentitySystemAndUnits"), hash);
		} else if (!isBlank(unitsValue)) {
			long hash = ResourceIndexedSearchParamQuantity.calculateHashUnits(theResourceName, theParamName, unitsValue);
			hashPredicate = myBuilder.equal(theFrom.get("myHashIdentityAndUnits"), hash);
		} else {
			long hash = BaseResourceIndexedSearchParam.calculateHashIdentity(theResourceName, theParamName);
			hashPredicate = myBuilder.equal(theFrom.get("myHashIdentity"), hash);
		}

		cmpValue = ObjectUtils.defaultIfNull(cmpValue, ParamPrefixEnum.EQUAL);
		final Expression<BigDecimal> path = theFrom.get("myValue");
		String invalidMessageName = "invalidQuantityPrefix";

		Predicate numericPredicate = createPredicateNumeric(theResourceName, null, theFrom, theBuilder, theParam, cmpValue, valueValue, path, invalidMessageName);

		return theBuilder.and(hashPredicate, numericPredicate);
	}

	private Predicate createPredicateString(IQueryParameterType theParameter, String theResourceName, String theParamName, CriteriaBuilder theBuilder,
														 From<?, ResourceIndexedSearchParamString> theFrom) {
		String rawSearchTerm;
		if (theParameter instanceof TokenParam) {
			TokenParam id = (TokenParam) theParameter;
			if (!id.isText()) {
				throw new IllegalStateException("Trying to process a text search on a non-text token parameter");
			}
			rawSearchTerm = id.getValue();
		} else if (theParameter instanceof StringParam) {
			StringParam id = (StringParam) theParameter;
			rawSearchTerm = id.getValue();
			if (id.isContains()) {
				if (!myDaoConfig.isAllowContainsSearches()) {
					throw new MethodNotAllowedException(":contains modifier is disabled on this server");
				}
			}
		} else if (theParameter instanceof IPrimitiveDatatype<?>) {
			IPrimitiveDatatype<?> id = (IPrimitiveDatatype<?>) theParameter;
			rawSearchTerm = id.getValueAsString();
		} else {
			throw new IllegalArgumentException("Invalid token type: " + theParameter.getClass());
		}

		if (rawSearchTerm.length() > ResourceIndexedSearchParamString.MAX_LENGTH) {
			throw new InvalidRequestException("Parameter[" + theParamName + "] has length (" + rawSearchTerm.length() + ") that is longer than maximum allowed ("
				+ ResourceIndexedSearchParamString.MAX_LENGTH + "): " + rawSearchTerm);
		}

		if (myDontUseHashesForSearch) {
			String likeExpression = StringNormalizer.normalizeString(rawSearchTerm);
			if (myDaoConfig.isAllowContainsSearches()) {
				if (theParameter instanceof StringParam) {
					if (((StringParam) theParameter).isContains()) {
						likeExpression = createLeftAndRightMatchLikeExpression(likeExpression);
					} else {
						likeExpression = createLeftMatchLikeExpression(likeExpression);
					}
				} else {
					likeExpression = createLeftMatchLikeExpression(likeExpression);
				}
			} else {
				likeExpression = createLeftMatchLikeExpression(likeExpression);
			}

			Predicate singleCode = theBuilder.like(theFrom.get("myValueNormalized").as(String.class), likeExpression);
			if (theParameter instanceof StringParam && ((StringParam) theParameter).isExact()) {
				Predicate exactCode = theBuilder.equal(theFrom.get("myValueExact"), rawSearchTerm);
				singleCode = theBuilder.and(singleCode, exactCode);
			}

			return combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, theFrom, singleCode);
		}

		boolean exactMatch = theParameter instanceof StringParam && ((StringParam) theParameter).isExact();
		if (exactMatch) {

			// Exact match

			Long hash = ResourceIndexedSearchParamString.calculateHashExact(theResourceName, theParamName, rawSearchTerm);
			return theBuilder.equal(theFrom.get("myHashExact").as(Long.class), hash);

		} else {

			// Normalized Match

			String normalizedString = StringNormalizer.normalizeString(rawSearchTerm);
			String likeExpression;
			if (theParameter instanceof StringParam &&
				((StringParam) theParameter).isContains() &&
				myDaoConfig.isAllowContainsSearches()) {
				likeExpression = createLeftAndRightMatchLikeExpression(normalizedString);
			} else {
				likeExpression = createLeftMatchLikeExpression(normalizedString);
			}

			Long hash = ResourceIndexedSearchParamString.calculateHashNormalized(myDaoConfig.getModelConfig(), theResourceName, theParamName, normalizedString);
			Predicate hashCode = theBuilder.equal(theFrom.get("myHashNormalizedPrefix").as(Long.class), hash);
			Predicate singleCode = theBuilder.like(theFrom.get("myValueNormalized").as(String.class), likeExpression);
			return theBuilder.and(hashCode, singleCode);

		}
	}

	private List<Predicate> createPredicateTagList(Path<TagDefinition> theDefJoin, CriteriaBuilder theBuilder, TagTypeEnum theTagType, List<Pair<String, String>> theTokens) {
		Predicate typePrediate = theBuilder.equal(theDefJoin.get("myTagType"), theTagType);

		List<Predicate> orPredicates = Lists.newArrayList();
		for (Pair<String, String> next : theTokens) {
			Predicate codePrediate = theBuilder.equal(theDefJoin.get("myCode"), next.getRight());
			if (isNotBlank(next.getLeft())) {
				Predicate systemPrediate = theBuilder.equal(theDefJoin.get("mySystem"), next.getLeft());
				orPredicates.add(theBuilder.and(typePrediate, systemPrediate, codePrediate));
			} else {
				orPredicates.add(theBuilder.and(typePrediate, codePrediate));
			}
		}
		return orPredicates;
	}

	private List<Predicate> createPredicateToken(Collection<IQueryParameterType> theParameters, String theResourceName, String theParamName, CriteriaBuilder theBuilder,
																From<?, ResourceIndexedSearchParamToken> theFrom) {
		final List<VersionIndependentConcept> codes = new ArrayList<>();

		TokenParamModifier modifier = null;
		for (IQueryParameterType nextParameter : theParameters) {

			String code;
			String system;
			if (nextParameter instanceof TokenParam) {
				TokenParam id = (TokenParam) nextParameter;
				system = id.getSystem();
				code = (id.getValue());
				modifier = id.getModifier();
			} else if (nextParameter instanceof BaseIdentifierDt) {
				BaseIdentifierDt id = (BaseIdentifierDt) nextParameter;
				system = id.getSystemElement().getValueAsString();
				code = (id.getValueElement().getValue());
			} else if (nextParameter instanceof BaseCodingDt) {
				BaseCodingDt id = (BaseCodingDt) nextParameter;
				system = id.getSystemElement().getValueAsString();
				code = (id.getCodeElement().getValue());
			} else if (nextParameter instanceof NumberParam) {
				NumberParam number = (NumberParam) nextParameter;
				system = null;
				code = number.getValueAsQueryToken(myContext);
			} else {
				throw new IllegalArgumentException("Invalid token type: " + nextParameter.getClass());
			}

			if (system != null && system.length() > ResourceIndexedSearchParamToken.MAX_LENGTH) {
				throw new InvalidRequestException(
					"Parameter[" + theParamName + "] has system (" + system.length() + ") that is longer than maximum allowed (" + ResourceIndexedSearchParamToken.MAX_LENGTH + "): " + system);
			}

			if (code != null && code.length() > ResourceIndexedSearchParamToken.MAX_LENGTH) {
				throw new InvalidRequestException(
					"Parameter[" + theParamName + "] has code (" + code.length() + ") that is longer than maximum allowed (" + ResourceIndexedSearchParamToken.MAX_LENGTH + "): " + code);
			}

			/*
			 * Process token modifiers (:in, :below, :above)
			 */

			if (modifier == TokenParamModifier.IN) {
				codes.addAll(myTerminologySvc.expandValueSet(code));
			} else if (modifier == TokenParamModifier.ABOVE) {
				system = determineSystemIfMissing(theParamName, code, system);
				codes.addAll(myTerminologySvc.findCodesAbove(system, code));
			} else if (modifier == TokenParamModifier.BELOW) {
				system = determineSystemIfMissing(theParamName, code, system);
				codes.addAll(myTerminologySvc.findCodesBelow(system, code));
			} else {
				codes.add(new VersionIndependentConcept(system, code));
			}

		}

		List<VersionIndependentConcept> sortedCodesList = codes
			.stream()
			.filter(t -> t.getCode() != null || t.getSystem() != null)
			.sorted()
			.distinct()
			.collect(Collectors.toList());

		if (codes.isEmpty()) {
			// This will never match anything
			return Collections.singletonList(new BooleanStaticAssertionPredicate((CriteriaBuilderImpl) theBuilder, false));
		}

		List<Predicate> retVal = new ArrayList<>();

		// System only
		List<VersionIndependentConcept> systemOnlyCodes = sortedCodesList.stream().filter(t -> isBlank(t.getCode())).collect(Collectors.toList());
		if (!systemOnlyCodes.isEmpty()) {
			retVal.add(addPredicateToken(theResourceName, theParamName, theBuilder, theFrom, systemOnlyCodes, modifier, TokenModeEnum.SYSTEM_ONLY));
		}

		// Code only
		List<VersionIndependentConcept> codeOnlyCodes = sortedCodesList.stream().filter(t -> t.getSystem() == null).collect(Collectors.toList());
		if (!codeOnlyCodes.isEmpty()) {
			retVal.add(addPredicateToken(theResourceName, theParamName, theBuilder, theFrom, codeOnlyCodes, modifier, TokenModeEnum.VALUE_ONLY));
		}

		// System and code
		List<VersionIndependentConcept> systemAndCodeCodes = sortedCodesList.stream().filter(t -> isNotBlank(t.getCode()) && t.getSystem() != null).collect(Collectors.toList());
		if (!systemAndCodeCodes.isEmpty()) {
			retVal.add(addPredicateToken(theResourceName, theParamName, theBuilder, theFrom, systemAndCodeCodes, modifier, TokenModeEnum.SYSTEM_AND_VALUE));
		}

		return retVal;
	}

	private Predicate addPredicateToken(String theResourceName, String theParamName, CriteriaBuilder theBuilder, From<?, ResourceIndexedSearchParamToken> theFrom, List<VersionIndependentConcept> theTokens, TokenParamModifier theModifier, TokenModeEnum theTokenMode) {
		if (myDontUseHashesForSearch) {
			final Path<String> systemExpression = theFrom.get("mySystem");
			final Path<String> valueExpression = theFrom.get("myValue");

			List<Predicate> orPredicates = new ArrayList<>();
			switch (theTokenMode) {
				case SYSTEM_ONLY: {
					List<String> systems = theTokens.stream().map(t -> t.getSystem()).collect(Collectors.toList());
					Predicate orPredicate = systemExpression.in(systems);
					orPredicates.add(orPredicate);
					break;
				}
				case VALUE_ONLY:
					List<String> codes = theTokens.stream().map(t -> t.getCode()).collect(Collectors.toList());
					Predicate orPredicate = valueExpression.in(codes);
					orPredicates.add(orPredicate);
					break;
				case SYSTEM_AND_VALUE:
					for (VersionIndependentConcept next : theTokens) {
						orPredicates.add(theBuilder.and(
							toEqualOrIsNullPredicate(systemExpression, next.getSystem()),
							toEqualOrIsNullPredicate(valueExpression, next.getCode())
						));
					}
					break;
			}

			Predicate or = theBuilder.or(orPredicates.toArray(new Predicate[0]));
			if (theModifier == TokenParamModifier.NOT) {
				or = theBuilder.not(or);
			}

			return combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, theFrom, or);
		}

		/*
		 * Note: A null system value means "match any system", but
		 * an empty-string system value means "match values that
		 * explicitly have no system".
		 */
		Expression<Long> hashField;
		List<Long> values;
		switch (theTokenMode) {
			case SYSTEM_ONLY:
				hashField = theFrom.get("myHashSystem").as(Long.class);
				values = theTokens
					.stream()
					.map(t -> ResourceIndexedSearchParamToken.calculateHashSystem(theResourceName, theParamName, t.getSystem()))
					.collect(Collectors.toList());
				break;
			case VALUE_ONLY:
				hashField = theFrom.get("myHashValue").as(Long.class);
				values = theTokens
					.stream()
					.map(t -> ResourceIndexedSearchParamToken.calculateHashValue(theResourceName, theParamName, t.getCode()))
					.collect(Collectors.toList());
				break;
			case SYSTEM_AND_VALUE:
			default:
				hashField = theFrom.get("myHashSystemAndValue").as(Long.class);
				values = theTokens
					.stream()
					.map(t -> ResourceIndexedSearchParamToken.calculateHashSystemAndValue(theResourceName, theParamName, t.getSystem(), t.getCode()))
					.collect(Collectors.toList());
				break;
		}

		Predicate predicate = hashField.in(values);
		if (theModifier == TokenParamModifier.NOT) {
			Predicate identityPredicate = theBuilder.equal(theFrom.get("myHashIdentity").as(Long.class), BaseResourceIndexedSearchParam.calculateHashIdentity(theResourceName, theParamName));
			Predicate disjunctionPredicate = theBuilder.not(predicate);
			predicate = theBuilder.and(identityPredicate, disjunctionPredicate);
		}
		return predicate;
	}

	private <T> Expression<Boolean> toEqualOrIsNullPredicate(Path<T> theExpression, T theCode) {
		if (theCode == null) {
			return myBuilder.isNull(theExpression);
		}
		return myBuilder.equal(theExpression, theCode);
	}

	@Override
	public Iterator<Long> createCountQuery(SearchParameterMap theParams, String theSearchUuid) {
		myParams = theParams;
		myBuilder = myEntityManager.getCriteriaBuilder();
		mySearchUuid = theSearchUuid;

		TypedQuery<Long> query = createQuery(null, null, true);
		return new CountQueryIterator(query);
	}

	/**
	 * @param thePidSet May be null
	 */
	@Override
	public void setPreviouslyAddedResourcePids(@Nullable List<Long> thePidSet) {
		myPidSet = new HashSet<>(thePidSet);
	}

	@Override
	public IResultIterator createQuery(SearchParameterMap theParams, SearchRuntimeDetails theSearchRuntimeDetails) {
		myParams = theParams;
		myBuilder = myEntityManager.getCriteriaBuilder();
		mySearchUuid = theSearchRuntimeDetails.getSearchUuid();

		if (ourTrackHandlersForUnitTest) {
			ourLastHandlerParamsForUnitTest = theParams;
			ourLastHandlerMechanismForUnitTest = HandlerTypeEnum.STANDARD_QUERY;
			ourLastHandlerThreadForUnitTest = Thread.currentThread().getName();
		}

		if (myPidSet == null) {
			myPidSet = new HashSet<>();
		}

		return new QueryIterator(theSearchRuntimeDetails);
	}

	private TypedQuery<Long> createQuery(SortSpec sort, Integer theMaximumResults, boolean theCount) {
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
			}

		}

		if (myParams.getEverythingMode() != null) {
			Join<ResourceTable, ResourceLink> join = myResourceTableRoot.join("myResourceLinks", JoinType.LEFT);

			if (myParams.get(IAnyResource.SP_RES_ID) != null) {
				StringParam idParm = (StringParam) myParams.get(IAnyResource.SP_RES_ID).get(0).get(0);
				Long pid = myIdHelperService.translateForcedIdToPid(myResourceName, idParm.getValue());
				if (myAlsoIncludePids == null) {
					myAlsoIncludePids = new ArrayList<>(1);
				}
				myAlsoIncludePids.add(pid);
				myPredicates.add(myBuilder.equal(join.get("myTargetResourcePid").as(Long.class), pid));
			} else {
				Predicate targetTypePredicate = myBuilder.equal(join.get("myTargetResourceType").as(String.class), myResourceName);
				Predicate sourceTypePredicate = myBuilder.equal(myResourceTableRoot.get("myResourceType").as(String.class), myResourceName);
				myPredicates.add(myBuilder.or(sourceTypePredicate, targetTypePredicate));
			}

		} else {
			// Normal search
			searchForIdsWithAndOr(myParams);
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

			List<Long> pids;
			if (myParams.getEverythingMode() != null) {
				pids = myFulltextSearchSvc.everything(myResourceName, myParams);
			} else {
				pids = myFulltextSearchSvc.search(myResourceName, myParams);
			}
			if (pids.isEmpty()) {
				// Will never match
				pids = Collections.singletonList(-1L);
			}

			myPredicates.add(myResourceTableRoot.get("myId").as(Long.class).in(pids));
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

	private Predicate createResourceLinkPathPredicate(String theResourceName, String theParamName, From<?, ? extends ResourceLink> from) {
		return createResourceLinkPathPredicate(myContext, theParamName, from, theResourceName);
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
		JoinEnum joinType;

		switch (param.getParamType()) {
			case STRING:
				joinAttrName = "myParamsString";
				sortAttrName = new String[]{"myValueExact"};
				joinType = JoinEnum.STRING;
				break;
			case DATE:
				joinAttrName = "myParamsDate";
				sortAttrName = new String[]{"myValueLow"};
				joinType = JoinEnum.DATE;
				break;
			case REFERENCE:
				joinAttrName = "myResourceLinks";
				sortAttrName = new String[]{"myTargetResourcePid"};
				joinType = JoinEnum.REFERENCE;
				break;
			case TOKEN:
				joinAttrName = "myParamsToken";
				sortAttrName = new String[]{"mySystem", "myValue"};
				joinType = JoinEnum.TOKEN;
				break;
			case NUMBER:
				joinAttrName = "myParamsNumber";
				sortAttrName = new String[]{"myValue"};
				joinType = JoinEnum.NUMBER;
				break;
			case URI:
				joinAttrName = "myParamsUri";
				sortAttrName = new String[]{"myUri"};
				joinType = JoinEnum.URI;
				break;
			case QUANTITY:
				joinAttrName = "myParamsQuantity";
				sortAttrName = new String[]{"myValue"};
				joinType = JoinEnum.QUANTITY;
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
		JoinKey key = new JoinKey(theSort.getParamName(), joinType);
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

	private String determineSystemIfMissing(String theParamName, String code, String theSystem) {
		String retVal = theSystem;
		if (retVal == null) {
			RuntimeResourceDefinition resourceDef = myContext.getResourceDefinition(myResourceName);
			RuntimeSearchParam param = mySearchParamRegistry.getSearchParamByName(resourceDef, theParamName);
			if (param != null) {
				Set<String> valueSetUris = Sets.newHashSet();
				for (String nextPath : param.getPathsSplit()) {
					BaseRuntimeChildDefinition def = myContext.newTerser().getDefinition(myResourceType, nextPath);
					if (def instanceof BaseRuntimeDeclaredChildDefinition) {
						String valueSet = ((BaseRuntimeDeclaredChildDefinition) def).getBindingValueSet();
						if (isNotBlank(valueSet)) {
							valueSetUris.add(valueSet);
						}
					}
				}
				if (valueSetUris.size() == 1) {
					String valueSet = valueSetUris.iterator().next();
					List<VersionIndependentConcept> candidateCodes = myTerminologySvc.expandValueSet(valueSet);
					for (VersionIndependentConcept nextCandidate : candidateCodes) {
						if (nextCandidate.getCode().equals(code)) {
							retVal = nextCandidate.getSystem();
							break;
						}
					}
				}
			}
		}
		return retVal;
	}

	private void doLoadPids(List<IBaseResource> theResourceListToPopulate, Set<Long> theIncludedPids, boolean theForHistoryOperation, EntityManager theEntityManager, FhirContext theContext, IDao theDao,
									Map<Long, Integer> thePosition, Collection<Long> thePids) {

		// -- get the resource from the searchView
		Collection<ResourceSearchView> resourceSearchViewList = myResourceSearchViewDao.findByResourceIds(thePids);

		//-- preload all tags with tag definition if any
		Map<Long, Collection<ResourceTag>> tagMap = getResourceTagMap(resourceSearchViewList);

		Long resourceId;
		for (ResourceSearchView next : resourceSearchViewList) {

			Class<? extends IBaseResource> resourceType = theContext.getResourceDefinition(next.getResourceType()).getImplementingClass();

			resourceId = next.getId();

			IBaseResource resource = theDao.toResource(resourceType, next, tagMap.get(resourceId), theForHistoryOperation);
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

			// Interceptor broadcast: RESOURCE_MAY_BE_RETURNED
			HookParams params = new HookParams()
				.add(IBaseResource.class, resource)
				.add(RequestDetails.class, null)
				.add(ServletRequestDetails.class, null);
			myInterceptorBroadcaster.callHooks(Pointcut.STORAGE_PREACCESS_RESOURCE, params);

			theResourceListToPopulate.set(index, resource);
		}
	}

	private Map<Long, Collection<ResourceTag>> getResourceTagMap(Collection<ResourceSearchView> theResourceSearchViewList) {

		List<Long> idList = new ArrayList<>(theResourceSearchViewList.size());

		//-- find all resource has tags
		for (ResourceSearchView resource : theResourceSearchViewList) {
			if (resource.isHasTags())
				idList.add(resource.getId());
		}

		Map<Long, Collection<ResourceTag>> tagMap = new HashMap<>();

		//-- no tags
		if (idList.size() == 0)
			return tagMap;

		//-- get all tags for the idList
		Collection<ResourceTag> tagList = myResourceTagDao.findByResourceIds(idList);

		//-- build the map, key = resourceId, value = list of ResourceTag
		Long resourceId;
		Collection<ResourceTag> tagCol;
		for (ResourceTag tag : tagList) {

			resourceId = tag.getResourceId();
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
	public void loadResourcesByPid(Collection<Long> theIncludePids, List<IBaseResource> theResourceListToPopulate, Set<Long> theIncludedPids, boolean theForHistoryOperation,
											 EntityManager entityManager, FhirContext context, IDao theDao) {
		if (theIncludePids.isEmpty()) {
			ourLog.debug("The include pids are empty");
			// return;
		}

		// Dupes will cause a crash later anyhow, but this is expensive so only do it
		// when running asserts
		assert new HashSet<>(theIncludePids).size() == theIncludePids.size() : "PID list contains duplicates: " + theIncludePids;

		Map<Long, Integer> position = new HashMap<>();
		for (Long next : theIncludePids) {
			position.put(next, theResourceListToPopulate.size());
			theResourceListToPopulate.add(null);
		}

		/*
		 * As always, Oracle can't handle things that other databases don't mind.. In this
		 * case it doesn't like more than ~1000 IDs in a single load, so we break this up
		 * if it's lots of IDs. I suppose maybe we should be doing this as a join anyhow
		 * but this should work too. Sigh.
		 */
		List<Long> pids = new ArrayList<>(theIncludePids);
		for (int i = 0; i < pids.size(); i += MAXIMUM_PAGE_SIZE) {
			int to = i + MAXIMUM_PAGE_SIZE;
			to = Math.min(to, pids.size());
			List<Long> pidsSubList = pids.subList(i, to);
			doLoadPids(theResourceListToPopulate, theIncludedPids, theForHistoryOperation, entityManager, context, theDao, position, pidsSubList);
		}

	}

	/**
	 * THIS SHOULD RETURN HASHSET and not just Set because we add to it later
	 * so it can't be Collections.emptySet() or some such thing
	 */
	@Override
	public HashSet<Long> loadIncludes(FhirContext theContext, EntityManager theEntityManager, Collection<Long> theMatches, Set<Include> theRevIncludes,
												 boolean theReverseMode, DateRangeParam theLastUpdated, String theSearchIdOrDescription) {
		if (theMatches.size() == 0) {
			return new HashSet<>();
		}
		if (theRevIncludes == null || theRevIncludes.isEmpty()) {
			return new HashSet<>();
		}
		String searchFieldName = theReverseMode ? "myTargetResourcePid" : "mySourceResourcePid";

		Collection<Long> nextRoundMatches = theMatches;
		HashSet<Long> allAdded = new HashSet<>();
		HashSet<Long> original = new HashSet<>(theMatches);
		ArrayList<Include> includes = new ArrayList<>(theRevIncludes);

		int roundCounts = 0;
		StopWatch w = new StopWatch();

		boolean addedSomeThisRound;
		do {
			roundCounts++;

			HashSet<Long> pidsToInclude = new HashSet<>();

			for (Iterator<Include> iter = includes.iterator(); iter.hasNext(); ) {
				Include nextInclude = iter.next();
				if (nextInclude.isRecurse() == false) {
					iter.remove();
				}

				boolean matchAll = "*".equals(nextInclude.getValue());
				if (matchAll) {
					String sql;
					sql = "SELECT r FROM ResourceLink r WHERE r." + searchFieldName + " IN (:target_pids) ";
					List<Collection<Long>> partitions = partition(nextRoundMatches, MAXIMUM_PAGE_SIZE);
					for (Collection<Long> nextPartition : partitions) {
						TypedQuery<ResourceLink> q = theEntityManager.createQuery(sql, ResourceLink.class);
						q.setParameter("target_pids", nextPartition);
						List<ResourceLink> results = q.getResultList();
						for (ResourceLink resourceLink : results) {
							if (theReverseMode) {
								pidsToInclude.add(resourceLink.getSourceResourcePid());
							} else {
								pidsToInclude.add(resourceLink.getTargetResourcePid());
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

						boolean haveTargetTypesDefinedByParam = param.getTargets() != null && param.getTargets().isEmpty() == false;
						if (targetResourceType != null) {
							sql = "SELECT r FROM ResourceLink r WHERE r.mySourcePath = :src_path AND r." + searchFieldName + " IN (:target_pids) AND r.myTargetResourceType = :target_resource_type";
						} else if (haveTargetTypesDefinedByParam) {
							sql = "SELECT r FROM ResourceLink r WHERE r.mySourcePath = :src_path AND r." + searchFieldName + " IN (:target_pids) AND r.myTargetResourceType in (:target_resource_types)";
						} else {
							sql = "SELECT r FROM ResourceLink r WHERE r.mySourcePath = :src_path AND r." + searchFieldName + " IN (:target_pids)";
						}

						List<Collection<Long>> partitions = partition(nextRoundMatches, MAXIMUM_PAGE_SIZE);
						for (Collection<Long> nextPartition : partitions) {
							TypedQuery<ResourceLink> q = theEntityManager.createQuery(sql, ResourceLink.class);
							q.setParameter("src_path", nextPath);
							q.setParameter("target_pids", nextPartition);
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
										pidsToInclude.add(pid);
									}
								} else {
									Long pid = resourceLink.getTargetResourcePid();
									if (pid != null) {
										pidsToInclude.add(pid);
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
			for (Long next : pidsToInclude) {
				if (original.contains(next) == false && allAdded.contains(next) == false) {
					theMatches.add(next);
				}
			}

			addedSomeThisRound = allAdded.addAll(pidsToInclude);
			nextRoundMatches = pidsToInclude;
		} while (includes.size() > 0 && nextRoundMatches.size() > 0 && addedSomeThisRound);

		ourLog.info("Loaded {} {} in {} rounds and {} ms for search {}", allAdded.size(), theReverseMode ? "_revincludes" : "_includes", roundCounts, w.getMillisAndRestart(), theSearchIdOrDescription);

		return allAdded;
	}

	private List<Collection<Long>> partition(Collection<Long> theNextRoundMatches, int theMaxLoad) {
		if (theNextRoundMatches.size() <= theMaxLoad) {
			return Collections.singletonList(theNextRoundMatches);
		} else {

			List<Collection<Long>> retVal = new ArrayList<>();
			Collection<Long> current = null;
			for (Long next : theNextRoundMatches) {
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

	private void searchForIdsWithAndOr(@Nonnull SearchParameterMap theParams) {
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
					if (ourTrackHandlersForUnitTest) {
						ourLastHandlerMechanismForUnitTest = HandlerTypeEnum.UNIQUE_INDEX;
					}
					addPredicateCompositeStringUnique(theParams, indexString);
				}
			}
		}

		// Handle each parameter
		for (Entry<String, List<List<IQueryParameterType>>> nextParamEntry : myParams.entrySet()) {
			String nextParamName = nextParamEntry.getKey();
			List<List<IQueryParameterType>> andOrParams = nextParamEntry.getValue();
			searchForIdsWithAndOr(myResourceName, nextParamName, andOrParams);
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

	private void searchForIdsWithAndOr(String theResourceName, String theParamName, List<List<IQueryParameterType>> theAndOrParams) {

		if (theAndOrParams.isEmpty()) {
			return;
		}

		if (theParamName.equals(IAnyResource.SP_RES_ID)) {

			addPredicateResourceId(theAndOrParams);

		} else if (theParamName.equals(IAnyResource.SP_RES_LANGUAGE)) {

			addPredicateLanguage(theAndOrParams);

		} else if (theParamName.equals(Constants.PARAM_HAS)) {

			addPredicateHas(theAndOrParams);

		} else if (theParamName.equals(Constants.PARAM_TAG) || theParamName.equals(Constants.PARAM_PROFILE) || theParamName.equals(Constants.PARAM_SECURITY)) {

			addPredicateTag(theAndOrParams, theParamName);

		} else {

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
							addPredicateReference(theResourceName, theParamName, nextAnd);
						}
						break;
					case STRING:
						for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
							addPredicateString(theResourceName, theParamName, nextAnd);
						}
						break;
					case TOKEN:
						for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
							addPredicateToken(theResourceName, theParamName, nextAnd);
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
						// should not happen
						break;
				}
			} else {
				if (Constants.PARAM_CONTENT.equals(theParamName) || Constants.PARAM_TEXT.equals(theParamName)) {
					// These are handled later
				} else {
					throw new InvalidRequestException("Unknown search parameter " + theParamName + " for resource type " + theResourceName);
				}
			}
		}
	}

	@Override
	public void setFetchSize(int theFetchSize) {
		myFetchSize = theFetchSize;
	}

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

	private enum TokenModeEnum {
		SYSTEM_ONLY,
		VALUE_ONLY,
		SYSTEM_AND_VALUE
	}

	public enum HandlerTypeEnum {
		UNIQUE_INDEX, STANDARD_QUERY
	}

	private enum JoinEnum {
		DATE,
		NUMBER,
		QUANTITY,
		REFERENCE,
		STRING,
		TOKEN,
		URI

	}

	public class IncludesIterator extends BaseIterator<Long> implements Iterator<Long> {

		private Iterator<Long> myCurrentIterator;
		private int myCurrentOffset;
		private ArrayList<Long> myCurrentPids;
		private Long myNext;
		private int myPageSize = myDaoConfig.getEverythingIncludesFetchPageSize();

		IncludesIterator(Set<Long> thePidSet) {
			myCurrentPids = new ArrayList<>(thePidSet);
			myCurrentIterator = EMPTY_LONG_LIST.iterator();
			myCurrentOffset = 0;
		}

		private void fetchNext() {
			while (myNext == null) {

				if (myCurrentIterator.hasNext()) {
					myNext = myCurrentIterator.next();
					break;
				}

				int start = myCurrentOffset;
				int end = myCurrentOffset + myPageSize;
				if (end > myCurrentPids.size()) {
					end = myCurrentPids.size();
				}
				if (end - start <= 0) {
					myNext = NO_MORE;
					break;
				}
				myCurrentOffset = end;
				Collection<Long> pidsToScan = myCurrentPids.subList(start, end);
				Set<Include> includes = Collections.singleton(new Include("*", true));
				Set<Long> newPids = loadIncludes(myContext, myEntityManager, pidsToScan, includes, false, myParams.getLastUpdated(), mySearchUuid);
				myCurrentIterator = newPids.iterator();

			}
		}

		@Override
		public boolean hasNext() {
			fetchNext();
			return !NO_MORE.equals(myNext);
		}

		@Override
		public Long next() {
			fetchNext();
			Long retVal = myNext;
			myNext = null;
			return retVal;
		}

	}

	private final class QueryIterator extends BaseIterator<Long> implements IResultIterator {

		private final SearchRuntimeDetails mySearchRuntimeDetails;
		private boolean myFirst = true;
		private IncludesIterator myIncludesIterator;
		private Long myNext;
		private Iterator<Long> myPreResultsIterator;
		private ScrollableResultsIterator<Long> myResultsIterator;
		private SortSpec mySort;
		private boolean myStillNeedToFetchIncludes;
		private int mySkipCount = 0;

		private QueryIterator(SearchRuntimeDetails theSearchRuntimeDetails) {
			mySearchRuntimeDetails = theSearchRuntimeDetails;
			mySort = myParams.getSort();

			// Includes are processed inline for $everything query
			if (myParams.getEverythingMode() != null) {
				myStillNeedToFetchIncludes = true;
			}
		}

		private void fetchNext() {

			// If we don't have a query yet, create one
			if (myResultsIterator == null) {
				if (myMaxResultsToFetch == null) {
					myMaxResultsToFetch = myDaoConfig.getFetchSizeDefaultMaximum();
				}

				final TypedQuery<Long> query = createQuery(mySort, myMaxResultsToFetch, false);

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
						Long next = myPreResultsIterator.next();
						if (next != null)
							if (myPidSet.add(next)) {
								myNext = next;
								break;
							}
					}
				}

				if (myNext == null) {
					while (myResultsIterator.hasNext()) {
						Long next = myResultsIterator.next();
						if (next != null) {
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
						myIncludesIterator = new IncludesIterator(myPidSet);
						myStillNeedToFetchIncludes = false;
					}
					if (myIncludesIterator != null) {
						while (myIncludesIterator.hasNext()) {
							Long next = myIncludesIterator.next();
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

			if (myFirst) {
				HookParams params = new HookParams();
				params.add(SearchRuntimeDetails.class, mySearchRuntimeDetails);
				myInterceptorBroadcaster.callHooks(Pointcut.JPA_PERFTRACE_SEARCH_FIRST_RESULT_LOADED, params);
				myFirst = false;
			}

			if (NO_MORE.equals(myNext)) {
				HookParams params = new HookParams();
				params.add(SearchRuntimeDetails.class, mySearchRuntimeDetails);
				myInterceptorBroadcaster.callHooks(Pointcut.JPA_PERFTRACE_SEARCH_SELECT_COMPLETE, params);
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
		public Long next() {
			fetchNext();
			Long retVal = myNext;
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

	private static class JoinKey {
		private final JoinEnum myJoinType;
		private final String myParamName;

		JoinKey(String theParamName, JoinEnum theJoinType) {
			super();
			myParamName = theParamName;
			myJoinType = theJoinType;
		}

		@Override
		public boolean equals(Object theObj) {
			if (!(theObj instanceof JoinKey)) {
				return false;
			}
			JoinKey obj = (JoinKey) theObj;
			return new EqualsBuilder()
				.append(myParamName, obj.myParamName)
				.append(myJoinType, obj.myJoinType)
				.isEquals();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder()
				.append(myParamName)
				.append(myJoinType)
				.toHashCode();
		}
	}

	/**
	 * Figures out the tolerance for a search. For example, if the user is searching for <code>4.00</code>, this method
	 * returns <code>0.005</code> because we shold actually match values which are
	 * <code>4 (+/-) 0.005</code> according to the FHIR specs.
	 */
	static BigDecimal calculateFuzzAmount(ParamPrefixEnum cmpValue, BigDecimal theValue) {
		if (cmpValue == ParamPrefixEnum.APPROXIMATE) {
			return theValue.multiply(new BigDecimal(0.1));
		} else {
			String plainString = theValue.toPlainString();
			int dotIdx = plainString.indexOf('.');
			if (dotIdx == -1) {
				return new BigDecimal(0.5);
			}

			int precision = plainString.length() - (dotIdx);
			double mul = Math.pow(10, -precision);
			double val = mul * 5.0d;
			return new BigDecimal(val);
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

	private static String createLeftAndRightMatchLikeExpression(String likeExpression) {
		return "%" + likeExpression.replace("%", "[%]") + "%";
	}

	private static String createLeftMatchLikeExpression(String likeExpression) {
		return likeExpression.replace("%", "[%]") + "%";
	}

	private static List<Long> filterResourceIdsByLastUpdated(EntityManager theEntityManager, final DateRangeParam theLastUpdated, Collection<Long> thePids) {
		if (thePids.isEmpty()) {
			return Collections.emptyList();
		}
		CriteriaBuilder builder = theEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceTable> from = cq.from(ResourceTable.class);
		cq.select(from.get("myId").as(Long.class));

		List<Predicate> lastUpdatedPredicates = createLastUpdatedPredicates(theLastUpdated, builder, from);
		lastUpdatedPredicates.add(from.get("myId").as(Long.class).in(thePids));

		cq.where(SearchBuilder.toArray(lastUpdatedPredicates));
		TypedQuery<Long> query = theEntityManager.createQuery(cq);

		return query.getResultList();
	}

	@VisibleForTesting
	public static HandlerTypeEnum getLastHandlerMechanismForUnitTest() {
		return ourLastHandlerMechanismForUnitTest;
	}

	@VisibleForTesting
	public static String getLastHandlerParamsForUnitTest() {
		return ourLastHandlerParamsForUnitTest.toString() + " on thread [" + ourLastHandlerThreadForUnitTest + "]";
	}

	@VisibleForTesting
	public static void resetLastHandlerMechanismForUnitTest() {
		ourLastHandlerMechanismForUnitTest = null;
		ourLastHandlerParamsForUnitTest = null;
		ourLastHandlerThreadForUnitTest = null;
		ourTrackHandlersForUnitTest = true;
	}

	private static Predicate[] toArray(List<Predicate> thePredicates) {
		return thePredicates.toArray(new Predicate[0]);
	}

}
