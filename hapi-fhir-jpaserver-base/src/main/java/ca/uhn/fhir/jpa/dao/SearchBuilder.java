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
import ca.uhn.fhir.jpa.dao.data.IForcedIdDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamUriDao;
import ca.uhn.fhir.jpa.entity.*;
import ca.uhn.fhir.jpa.search.JpaRuntimeSearchParam;
import ca.uhn.fhir.jpa.term.IHapiTerminologySvc;
import ca.uhn.fhir.jpa.term.VersionIndependentConcept;
import ca.uhn.fhir.jpa.util.BaseIterator;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.base.composite.BaseIdentifierDt;
import ca.uhn.fhir.model.base.composite.BaseQuantityDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.engine.spi.EntityKey;
import org.hibernate.engine.spi.PersistenceContext;
import org.hibernate.internal.SessionImpl;
import org.hibernate.query.Query;
import org.hl7.fhir.dstu3.model.BaseResource;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.criteria.CriteriaBuilder.In;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.Map.Entry;

import static org.apache.commons.lang3.StringUtils.*;

/**
 * The SearchBuilder is responsible for actually forming the SQL query that handles
 * searchs for resources
 */
public class SearchBuilder implements ISearchBuilder {

	private static final List<Long> EMPTY_LONG_LIST = Collections.unmodifiableList(new ArrayList<Long>());
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchBuilder.class);
	private static Long NO_MORE = -1L;
	private static HandlerTypeEnum ourLastHandlerMechanismForUnitTest;
	private List<Long> myAlsoIncludePids;
	private CriteriaBuilder myBuilder;
	private BaseHapiFhirDao<?> myCallingDao;
	private FhirContext myContext;
	private EntityManager myEntityManager;
	private IForcedIdDao myForcedIdDao;
	private IFulltextSearchSvc myFulltextSearchSvc;
	private Map<JoinKey, Join<?, ?>> myIndexJoins = Maps.newHashMap();
	private SearchParameterMap myParams;
	private ArrayList<Predicate> myPredicates;
	private IResourceIndexedSearchParamUriDao myResourceIndexedSearchParamUriDao;
	private String myResourceName;
	private AbstractQuery<Long> myResourceTableQuery;
	private Root<ResourceTable> myResourceTableRoot;
	private Class<? extends IBaseResource> myResourceType;
	private ISearchParamRegistry mySearchParamRegistry;
	private String mySearchUuid;
	private IHapiTerminologySvc myTerminologySvc;
	private int myFetchSize;

	/**
	 * Constructor
	 */
	public SearchBuilder(FhirContext theFhirContext, EntityManager theEntityManager, IFulltextSearchSvc theFulltextSearchSvc,
								BaseHapiFhirDao<?> theDao,
								IResourceIndexedSearchParamUriDao theResourceIndexedSearchParamUriDao, IForcedIdDao theForcedIdDao, IHapiTerminologySvc theTerminologySvc, ISearchParamRegistry theSearchParamRegistry) {
		myContext = theFhirContext;
		myEntityManager = theEntityManager;
		myFulltextSearchSvc = theFulltextSearchSvc;
		myCallingDao = theDao;
		myResourceIndexedSearchParamUriDao = theResourceIndexedSearchParamUriDao;
		myForcedIdDao = theForcedIdDao;
		myTerminologySvc = theTerminologySvc;
		mySearchParamRegistry = theSearchParamRegistry;
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

		Join<ResourceTable, ResourceIndexedSearchParamDate> join = createOrReuseJoin(JoinEnum.DATE, theParamName);

		if (theList.get(0).getMissing() != null) {
			Boolean missing = theList.get(0).getMissing();
			addPredicateParamMissing(theResourceName, theParamName, missing, join);
			return;
		}

		List<Predicate> codePredicates = new ArrayList<>();
		for (IQueryParameterType nextOr : theList) {
			IQueryParameterType params = nextOr;
			Predicate p = createPredicateDate(params, theResourceName, theParamName, myBuilder, join);
			codePredicates.add(p);
		}

		Predicate orPredicates = myBuilder.or(toArray(codePredicates));
		myPredicates.add(orPredicates);

	}

	private void addPredicateHas(List<List<? extends IQueryParameterType>> theHasParameters) {

		for (List<? extends IQueryParameterType> nextOrList : theHasParameters) {

			StringBuilder valueBuilder = new StringBuilder();
			String targetResourceType = null;
			String owningParameter = null;
			String parameterName = null;
			for (IQueryParameterType nextParam : nextOrList) {
				HasParam next = (HasParam) nextParam;
				if (valueBuilder.length() > 0) {
					valueBuilder.append(',');
				}
				valueBuilder.append(UrlUtil.escapeUrlParam(next.getValueAsQueryToken(myContext)));
				targetResourceType = next.getTargetResourceType();
				owningParameter = next.getOwningFieldName();
				parameterName = next.getParameterName();
			}

			if (valueBuilder.length() == 0) {
				continue;
			}

			String matchUrl = targetResourceType + '?' + UrlUtil.escapeUrlParam(parameterName) + '=' + valueBuilder.toString();
			RuntimeResourceDefinition targetResourceDefinition;
			try {
				targetResourceDefinition = myContext.getResourceDefinition(targetResourceType);
			} catch (DataFormatException e) {
				throw new InvalidRequestException("Invalid resource type: " + targetResourceType);
			}

			String paramName = parameterName.replaceAll("\\..*", "");
			RuntimeSearchParam owningParameterDef = myCallingDao.getSearchParamByName(targetResourceDefinition, paramName);
			if (owningParameterDef == null) {
				throw new InvalidRequestException("Unknown parameter name: " + targetResourceType + ':' + parameterName);
			}

			owningParameterDef = myCallingDao.getSearchParamByName(targetResourceDefinition, owningParameter);
			if (owningParameterDef == null) {
				throw new InvalidRequestException("Unknown parameter name: " + targetResourceType + ':' + owningParameter);
			}

			Class<? extends IBaseResource> resourceType = targetResourceDefinition.getImplementingClass();
			Set<Long> match = myCallingDao.processMatchUrl(matchUrl, resourceType);
			if (match.isEmpty()) {
				// Pick a PID that can never match
				match = Collections.singleton(-1L);
			}

			Join<ResourceTable, ResourceLink> join = myResourceTableRoot.join("myIncomingResourceLinks", JoinType.LEFT);

			Predicate predicate = join.get("mySourceResourcePid").in(match);
			myPredicates.add(predicate);
		}
	}

	private void addPredicateLanguage(List<List<? extends IQueryParameterType>> theList) {
		for (List<? extends IQueryParameterType> nextList : theList) {

			Set<String> values = new HashSet<String>();
			for (IQueryParameterType next : nextList) {
				if (next instanceof StringParam) {
					String nextValue = ((StringParam) next).getValue();
					if (isBlank(nextValue)) {
						continue;
					}
					values.add(nextValue);
				} else {
					throw new InternalErrorException("Lanugage parameter must be of type " + StringParam.class.getCanonicalName() + " - Got " + next.getClass().getCanonicalName());
				}
			}

			if (values.isEmpty()) {
				continue;
			}

			Predicate predicate = myResourceTableRoot.get("myLanguage").as(String.class).in(values);
			myPredicates.add(predicate);
		}

		return;
	}

	private void addPredicateNumber(String theResourceName, String theParamName, List<? extends IQueryParameterType> theList) {

		Join<ResourceTable, ResourceIndexedSearchParamNumber> join = createOrReuseJoin(JoinEnum.NUMBER, theParamName);

		if (theList.get(0).getMissing() != null) {
			addPredicateParamMissing(theResourceName, theParamName, theList.get(0).getMissing(), join);
			return;
		}

		List<Predicate> codePredicates = new ArrayList<Predicate>();
		for (IQueryParameterType nextOr : theList) {
			IQueryParameterType params = nextOr;

			if (params instanceof NumberParam) {
				NumberParam param = (NumberParam) params;

				BigDecimal value = param.getValue();
				if (value == null) {
					continue;
				}

				final Expression<BigDecimal> fromObj = join.get("myValue");
				ParamPrefixEnum prefix = ObjectUtils.defaultIfNull(param.getPrefix(), ParamPrefixEnum.EQUAL);
				String invalidMessageName = "invalidNumberPrefix";

				Predicate num = createPredicateNumeric(theResourceName, theParamName, join, myBuilder, params, prefix, value, fromObj, invalidMessageName);
				codePredicates.add(num);

			} else {
				throw new IllegalArgumentException("Invalid token type: " + params.getClass());
			}

		}

		myPredicates.add(myBuilder.or(toArray(codePredicates)));
	}

	private void addPredicateParamMissing(String theResourceName, String theParamName, boolean theMissing) {
		Join<ResourceTable, SearchParamPresent> paramPresentJoin = myResourceTableRoot.join("mySearchParamPresents", JoinType.LEFT);
		Join<SearchParamPresent, SearchParam> paramJoin = paramPresentJoin.join("mySearchParam", JoinType.LEFT);

		myPredicates.add(myBuilder.equal(paramJoin.get("myResourceName"), theResourceName));
		myPredicates.add(myBuilder.equal(paramJoin.get("myParamName"), theParamName));
		myPredicates.add(myBuilder.equal(paramPresentJoin.get("myPresent"), !theMissing));
	}

	private void addPredicateParamMissing(String theResourceName, String theParamName, boolean theMissing, Join<ResourceTable, ? extends BaseResourceIndexedSearchParam> theJoin) {

		myPredicates.add(myBuilder.equal(theJoin.get("myResourceType"), theResourceName));
		myPredicates.add(myBuilder.equal(theJoin.get("myParamName"), theParamName));
		myPredicates.add(myBuilder.equal(theJoin.get("myMissing"), theMissing));
	}

	private void addPredicateQuantity(String theResourceName, String theParamName, List<? extends IQueryParameterType> theList) {
		Join<ResourceTable, ResourceIndexedSearchParamQuantity> join = createOrReuseJoin(JoinEnum.QUANTITY, theParamName);

		if (theList.get(0).getMissing() != null) {
			addPredicateParamMissing(theResourceName, theParamName, theList.get(0).getMissing(), join);
			return;
		}

		List<Predicate> codePredicates = new ArrayList<Predicate>();
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

		Join<ResourceTable, ResourceLink> join = createOrReuseJoin(JoinEnum.REFERENCE, theParamName);

		List<Predicate> codePredicates = new ArrayList<Predicate>();

		for (IQueryParameterType nextOr : theList) {

			if (nextOr instanceof ReferenceParam) {
				ReferenceParam ref = (ReferenceParam) nextOr;

				if (isBlank(ref.getChain())) {
					IIdType dt = new IdDt(ref.getBaseUrl(), ref.getResourceType(), ref.getIdPart(), null);

					if (dt.hasBaseUrl()) {
						if (myCallingDao.getConfig().getTreatBaseUrlsAsLocal().contains(dt.getBaseUrl())) {
							dt = dt.toUnqualified();
						} else {
							ourLog.debug("Searching for resource link with target URL: {}", dt.getValue());
							Predicate eq = myBuilder.equal(join.get("myTargetResourceUrl"), dt.getValue());
							codePredicates.add(eq);
							continue;
						}
					}

					List<Long> targetPid;
					try {
						targetPid = myCallingDao.translateForcedIdToPids(dt);
					} catch (ResourceNotFoundException e) {
						// Use a PID that will never exist
						targetPid = Collections.singletonList(-1L);
					}
					for (Long next : targetPid) {
						ourLog.debug("Searching for resource link with target PID: {}", next);

						Predicate pathPredicate = createResourceLinkPathPredicate(theResourceName, theParamName, join);
						Predicate pidPredicate = myBuilder.equal(join.get("myTargetResourcePid"), next);
						codePredicates.add(myBuilder.and(pathPredicate, pidPredicate));
					}

				} else {

					final List<Class<? extends IBaseResource>> resourceTypes;
					String resourceId;
					if (!ref.getValue().matches("[a-zA-Z]+\\/.*")) {

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
							RuntimeSearchParam searchParamByName = myCallingDao.getSearchParamByName(resourceDef, theParamName);
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

						resourceId = ref.getValue();

					} else {
						RuntimeResourceDefinition resDef = myContext.getResourceDefinition(ref.getResourceType());
						resourceTypes = new ArrayList<Class<? extends IBaseResource>>(1);
						resourceTypes.add(resDef.getImplementingClass());
						resourceId = ref.getIdPart();
					}

					boolean foundChainMatch = false;

					String chain = ref.getChain();
					String remainingChain = null;
					int chainDotIndex = chain.indexOf('.');
					if (chainDotIndex != -1) {
						remainingChain = chain.substring(chainDotIndex + 1);
						chain = chain.substring(0, chainDotIndex);
					}

					for (Class<? extends IBaseResource> nextType : resourceTypes) {
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

						boolean isMeta = BaseHapiFhirDao.RESOURCE_META_PARAMS.containsKey(chain);
						RuntimeSearchParam param = null;
						if (!isMeta) {
							param = myCallingDao.getSearchParamByName(typeDef, chain);
							if (param == null) {
								ourLog.debug("Type {} doesn't have search param {}", nextType.getSimpleName(), param);
								continue;
							}
						}

						IQueryParameterType chainValue;
						if (remainingChain != null) {
							if (param == null || param.getParamType() != RestSearchParameterTypeEnum.REFERENCE) {
								ourLog.debug("Type {} parameter {} is not a reference, can not chain {}", new Object[] {nextType.getSimpleName(), chain, remainingChain});
								continue;
							}

							chainValue = new ReferenceParam();
							chainValue.setValueAsQueryToken(myContext, theParamName, qualifier, resourceId);
							((ReferenceParam) chainValue).setChain(remainingChain);
						} else if (isMeta) {
							IQueryParameterType type = BaseHapiFhirDao.newInstanceType(chain);
							type.setValueAsQueryToken(myContext, theParamName, qualifier, resourceId);
							chainValue = type;
						} else {
							chainValue = toParameterType(param, qualifier, resourceId);
						}

						foundChainMatch = true;

						Subquery<Long> subQ = myResourceTableQuery.subquery(Long.class);
						Root<ResourceTable> subQfrom = subQ.from(ResourceTable.class);
						subQ.select(subQfrom.get("myId").as(Long.class));

						List<List<? extends IQueryParameterType>> andOrParams = new ArrayList<List<? extends IQueryParameterType>>();
						andOrParams.add(Collections.singletonList(chainValue));

						/*
						 * We're doing a chain call, so push the current query root
						 * and predicate list down and put new ones at the top of the
						 * stack and run a subuery
						 */
						Root<ResourceTable> stackRoot = myResourceTableRoot;
						ArrayList<Predicate> stackPredicates = myPredicates;
						Map<JoinKey, Join<?, ?>> stackIndexJoins = myIndexJoins;
						myResourceTableRoot = subQfrom;
						myPredicates = Lists.newArrayList();
						myIndexJoins = Maps.newHashMap();

						// Create the subquery predicates
						myPredicates.add(myBuilder.equal(myResourceTableRoot.get("myResourceType"), subResourceName));
						myPredicates.add(myBuilder.isNull(myResourceTableRoot.get("myDeleted")));
						searchForIdsWithAndOr(subResourceName, chain, andOrParams);

						subQ.where(toArray(myPredicates));

						/*
						 * Pop the old query root and predicate list back
						 */
						myResourceTableRoot = stackRoot;
						myPredicates = stackPredicates;
						myIndexJoins = stackIndexJoins;

						Predicate pathPredicate = createResourceLinkPathPredicate(theResourceName, theParamName, join);
						Predicate pidPredicate = join.get("myTargetResourcePid").in(subQ);
						codePredicates.add(myBuilder.and(pathPredicate, pidPredicate));

					}

					if (!foundChainMatch) {
						throw new InvalidRequestException(myContext.getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "invalidParameterChain", theParamName + '.' + ref.getChain()));
					}
				}

			} else {
				throw new IllegalArgumentException("Invalid token type (expecting ReferenceParam): " + nextOr.getClass());
			}

		}

		myPredicates.add(myBuilder.or(toArray(codePredicates)));
	}

	private void addPredicateResourceId(List<List<? extends IQueryParameterType>> theValues) {
		for (List<? extends IQueryParameterType> nextValue : theValues) {
			Set<Long> orPids = new HashSet<Long>();
			for (IQueryParameterType next : nextValue) {
				String value = next.getValueAsQueryToken(myContext);
				if (value != null && value.startsWith("|")) {
					value = value.substring(1);
				}

				IdDt valueAsId = new IdDt(value);
				if (isNotBlank(value)) {
					if (valueAsId.isIdPartValidLong()) {
						orPids.add(valueAsId.getIdPartAsLong());
					} else {
						try {
							BaseHasResource entity = myCallingDao.readEntity(valueAsId);
							if (entity.getDeleted() == null) {
								orPids.add(entity.getId());
							}
						} catch (ResourceNotFoundException e) {
							/*
							 * This isn't an error, just means no result found
							 * that matches the ID the client provided
							 */
						}
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

		Join<ResourceTable, ResourceIndexedSearchParamString> join = createOrReuseJoin(JoinEnum.STRING, theParamName);

		if (theList.get(0).getMissing() != null) {
			addPredicateParamMissing(theResourceName, theParamName, theList.get(0).getMissing(), join);
			return;
		}

		List<Predicate> codePredicates = new ArrayList<Predicate>();
		for (IQueryParameterType nextOr : theList) {
			IQueryParameterType theParameter = nextOr;
			Predicate singleCode = createPredicateString(theParameter, theResourceName, theParamName, myBuilder, join);
			codePredicates.add(singleCode);
		}

		myPredicates.add(myBuilder.or(toArray(codePredicates)));

	}

	private void addPredicateTag(List<List<? extends IQueryParameterType>> theList, String theParamName) {
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

		Join<ResourceTable, ResourceIndexedSearchParamToken> join = createOrReuseJoin(JoinEnum.TOKEN, theParamName);

		if (theList.get(0).getMissing() != null) {
			addPredicateParamMissing(theResourceName, theParamName, theList.get(0).getMissing(), join);
			return;
		}

		List<Predicate> codePredicates = new ArrayList<Predicate>();
		for (IQueryParameterType nextOr : theList) {

			if (nextOr instanceof TokenParam) {
				TokenParam id = (TokenParam) nextOr;
				if (id.isText()) {
					addPredicateString(theResourceName, theParamName, theList);
					continue;
				}
			}

			Predicate singleCode = createPredicateToken(nextOr, theResourceName, theParamName, myBuilder, join);
			codePredicates.add(singleCode);
		}

		if (codePredicates.isEmpty()) {
			return;
		}

		Predicate spPredicate = myBuilder.or(toArray(codePredicates));
		myPredicates.add(spPredicate);
	}

	private void addPredicateUri(String theResourceName, String theParamName, List<? extends IQueryParameterType> theList) {

		Join<ResourceTable, ResourceIndexedSearchParamUri> join = createOrReuseJoin(JoinEnum.URI, theParamName);

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

				Predicate predicate;
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

					predicate = join.get("myUri").as(String.class).in(toFind);

				} else if (param.getQualifier() == UriParamQualifierEnum.BELOW) {
					predicate = myBuilder.like(join.get("myUri").as(String.class), createLeftMatchLikeExpression(value));
				} else {
					predicate = myBuilder.equal(join.get("myUri").as(String.class), value);
				}
				codePredicates.add(predicate);
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

		Predicate outerPredicate = combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, join, orPredicate);
		myPredicates.add(outerPredicate);
	}

	private Predicate combineParamIndexPredicateWithParamNamePredicate(String theResourceName, String theParamName, From<?, ? extends BaseResourceIndexedSearchParam> theFrom, Predicate thePredicate) {
		Predicate resourceTypePredicate = myBuilder.equal(theFrom.get("myResourceType"), theResourceName);
		Predicate paramNamePredicate = myBuilder.equal(theFrom.get("myParamName"), theParamName);
		Predicate outerPredicate = myBuilder.and(resourceTypePredicate, paramNamePredicate, thePredicate);
		return outerPredicate;
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
				retVal = createPredicateToken(leftValue, theResourceName, theParam.getName(), myBuilder, tokenJoin);
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
				break;
		}

		if (retVal == null) {
			throw new InvalidRequestException("Don't know how to handle composite parameter with type of " + theParam.getParamType());
		}

		return retVal;
	}

	@SuppressWarnings("unchecked")
	private <T> Join<ResourceTable, T> createOrReuseJoin(JoinEnum theType, String theSearchParameterName) {
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
		if (!myIndexJoins.containsKey(key)) {
			myIndexJoins.put(key, join);
		}

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
			default:
				String msg = myContext.getLocalizer().getMessage(SearchBuilder.class, invalidMessageName, thePrefix.getValue(), theParam.getValueAsQueryToken(myContext));
				throw new InvalidRequestException(msg);
		}

		if (theParamName == null) {
			return num;
		}
		return combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, theFrom, num);
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
				if (!myCallingDao.getConfig().isAllowContainsSearches()) {
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

		String likeExpression = BaseHapiFhirDao.normalizeString(rawSearchTerm);
		if (theParameter instanceof StringParam &&
			((StringParam) theParameter).isContains() &&
			myCallingDao.getConfig().isAllowContainsSearches()) {
			likeExpression = createLeftAndRightMatchLikeExpression(likeExpression);
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

	private Predicate createPredicateToken(IQueryParameterType theParameter, String theResourceName, String theParamName, CriteriaBuilder theBuilder,
														From<?, ResourceIndexedSearchParamToken> theFrom) {
		String code;
		String system;
		TokenParamModifier modifier = null;
		if (theParameter instanceof TokenParam) {
			TokenParam id = (TokenParam) theParameter;
			system = id.getSystem();
			code = (id.getValue());
			modifier = id.getModifier();
		} else if (theParameter instanceof BaseIdentifierDt) {
			BaseIdentifierDt id = (BaseIdentifierDt) theParameter;
			system = id.getSystemElement().getValueAsString();
			code = (id.getValueElement().getValue());
		} else if (theParameter instanceof BaseCodingDt) {
			BaseCodingDt id = (BaseCodingDt) theParameter;
			system = id.getSystemElement().getValueAsString();
			code = (id.getCodeElement().getValue());
		} else {
			throw new IllegalArgumentException("Invalid token type: " + theParameter.getClass());
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

		List<VersionIndependentConcept> codes = null;
		if (modifier == TokenParamModifier.IN) {
			codes = myTerminologySvc.expandValueSet(code);
		} else if (modifier == TokenParamModifier.ABOVE) {
			system = determineSystemIfMissing(theParamName, code, system);
			codes = myTerminologySvc.findCodesAbove(system, code);
		} else if (modifier == TokenParamModifier.BELOW) {
			system = determineSystemIfMissing(theParamName, code, system);
			codes = myTerminologySvc.findCodesBelow(system, code);
		}

		ArrayList<Predicate> singleCodePredicates = new ArrayList<>();
		if (codes != null) {

			if (codes.isEmpty()) {

				// This will never match anything
				Predicate codePredicate = theBuilder.isNull(theFrom.get("myMissing"));
				singleCodePredicates.add(codePredicate);

			} else {
				List<Predicate> orPredicates = new ArrayList<Predicate>();
				Map<String, List<VersionIndependentConcept>> map = new HashMap<String, List<VersionIndependentConcept>>();
				for (VersionIndependentConcept nextCode : codes) {
					List<VersionIndependentConcept> systemCodes = map.get(nextCode.getSystem());
					if (null == systemCodes) {
						systemCodes = new ArrayList<>();
						map.put(nextCode.getSystem(), systemCodes);
					}
					systemCodes.add(nextCode);
				}
				// Use "in" in case of large numbers of codes due to param modifiers
				final Path<String> systemExpression = theFrom.get("mySystem");
				final Path<String> valueExpression = theFrom.get("myValue");
				for (Map.Entry<String, List<VersionIndependentConcept>> entry : map.entrySet()) {
					Predicate systemPredicate = theBuilder.equal(systemExpression, entry.getKey());
					In<String> codePredicate = theBuilder.in(valueExpression);
					for (VersionIndependentConcept nextCode : entry.getValue()) {
						codePredicate.value(nextCode.getCode());
					}
					orPredicates.add(theBuilder.and(systemPredicate, codePredicate));
				}

				singleCodePredicates.add(theBuilder.or(orPredicates.toArray(new Predicate[orPredicates.size()])));
			}

		} else {

			/*
			 * Ok, this is a normal query
			 */

			if (StringUtils.isNotBlank(system)) {
				if (modifier != null && modifier == TokenParamModifier.NOT) {
					singleCodePredicates.add(theBuilder.notEqual(theFrom.get("mySystem"), system));
				} else {
					singleCodePredicates.add(theBuilder.equal(theFrom.get("mySystem"), system));
				}
			} else if (system == null) {
				// don't check the system
			} else {
				// If the system is "", we only match on null systems
				singleCodePredicates.add(theBuilder.isNull(theFrom.get("mySystem")));
			}

			if (StringUtils.isNotBlank(code)) {
				if (modifier != null && modifier == TokenParamModifier.NOT) {
					singleCodePredicates.add(theBuilder.notEqual(theFrom.get("myValue"), code));
				} else {
					singleCodePredicates.add(theBuilder.equal(theFrom.get("myValue"), code));
				}
			} else {
				/*
				 * As of HAPI FHIR 1.5, if the client searched for a token with a system but no specified value this means to
				 * match all tokens with the given value.
				 *
				 * I'm not sure I agree with this, but hey.. FHIR-I voted and this was the result :)
				 */
				// singleCodePredicates.add(theBuilder.isNull(theFrom.get("myValue")));
			}
		}

		Predicate singleCode = theBuilder.and(toArray(singleCodePredicates));
		return combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, theFrom, singleCode);
	}

	@Override
	public Iterator<Long> createQuery(SearchParameterMap theParams, String theSearchUuid) {
		myParams = theParams;
		myBuilder = myEntityManager.getCriteriaBuilder();
		mySearchUuid = theSearchUuid;

		/*
		 * Check if there is a unique key associated with the set
		 * of parameters passed in
		 */
		ourLog.debug("Checking for unique index for query: {}", theParams.toNormalizedQueryString(myContext));
		if (myCallingDao.getConfig().isUniqueIndexesEnabled()) {
			if (myParams.getIncludes().isEmpty()) {
				if (myParams.getRevIncludes().isEmpty()) {
					if (myParams.getEverythingMode() == null) {
						if (myParams.isAllParametersHaveNoModifier()) {
							Set<String> paramNames = theParams.keySet();
							if (paramNames.isEmpty() == false) {
								List<JpaRuntimeSearchParam> searchParams = mySearchParamRegistry.getActiveUniqueSearchParams(myResourceName, paramNames);
								if (searchParams.size() > 0) {
									List<List<String>> params = new ArrayList<>();
									for (Entry<String, List<List<? extends IQueryParameterType>>> nextParamNameToValues : theParams.entrySet()) {
										String nextParamName = nextParamNameToValues.getKey();
										nextParamName = UrlUtil.escapeUrlParam(nextParamName);
										for (List<? extends IQueryParameterType> nextAnd : nextParamNameToValues.getValue()) {
											ArrayList<String> nextValueList = new ArrayList<>();
											params.add(nextValueList);
											for (IQueryParameterType nextOr : nextAnd) {
												String nextOrValue = nextOr.getValueAsQueryToken(myContext);
												nextOrValue = UrlUtil.escapeUrlParam(nextOrValue);
												nextValueList.add(nextParamName + "=" + nextOrValue);
											}
										}
									}

									Set<String> uniqueQueryStrings = BaseHapiFhirDao.extractCompositeStringUniquesValueChains(myResourceName, params);
									ourLastHandlerMechanismForUnitTest = HandlerTypeEnum.UNIQUE_INDEX;
									return new UniqueIndexIterator(uniqueQueryStrings);

								}
							}
						}
					}
				}
			}
		}

		ourLastHandlerMechanismForUnitTest = HandlerTypeEnum.STANDARD_QUERY;
		return new QueryIterator();
	}

	private TypedQuery<Long> createQuery(SortSpec sort, Integer theMaximumResults) {
		CriteriaQuery<Long> outerQuery;
		/*
		 * Sort
		 *
		 * If we have a sort, we wrap the criteria search (the search that actually
		 * finds the appropriate resources) in an outer search which is then sorted
		 */
		if (sort != null) {

			outerQuery = myBuilder.createQuery(Long.class);
			Root<ResourceTable> outerQueryFrom = outerQuery.from(ResourceTable.class);

			List<Order> orders = Lists.newArrayList();
			List<Predicate> predicates = Lists.newArrayList();

			createSort(myBuilder, outerQueryFrom, sort, orders, predicates);
			if (orders.size() > 0) {
				outerQuery.orderBy(orders);
			}

			Subquery<Long> subQ = outerQuery.subquery(Long.class);
			Root<ResourceTable> subQfrom = subQ.from(ResourceTable.class);

			myResourceTableQuery = subQ;
			myResourceTableRoot = subQfrom;

			Expression<Long> selectExpr = subQfrom.get("myId").as(Long.class);
			subQ.select(selectExpr);

			predicates.add(0, myBuilder.in(outerQueryFrom.get("myId").as(Long.class)).value(subQ));

			outerQuery.multiselect(outerQueryFrom.get("myId").as(Long.class));
			outerQuery.where(predicates.toArray(new Predicate[0]));

		} else {

			outerQuery = myBuilder.createQuery(Long.class);
			myResourceTableQuery = outerQuery;
			myResourceTableRoot = myResourceTableQuery.from(ResourceTable.class);
			outerQuery.multiselect(myResourceTableRoot.get("myId").as(Long.class));

		}

		myPredicates = new ArrayList<>();

		if (myParams.getEverythingMode() != null) {
			Join<ResourceTable, ResourceLink> join = myResourceTableRoot.join("myResourceLinks", JoinType.LEFT);

			if (myParams.get(BaseResource.SP_RES_ID) != null) {
				StringParam idParm = (StringParam) myParams.get(BaseResource.SP_RES_ID).get(0).get(0);
				Long pid = BaseHapiFhirDao.translateForcedIdToPid(myResourceName, idParm.getValue(), myForcedIdDao);
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
		if (myIndexJoins.isEmpty()) {
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
		return createResourceLinkPathPredicate(myCallingDao, myContext, theParamName, from, theResourceName);
	}

	/**
	 * @return Returns {@literal true} if any search parameter sorts were found, or false if
	 * no sorts were found, or only non-search parameters ones (e.g. _id, _lastUpdated)
	 */
	private boolean createSort(CriteriaBuilder theBuilder, Root<ResourceTable> theFrom, SortSpec theSort, List<Order> theOrders, List<Predicate> thePredicates) {
		if (theSort == null || isBlank(theSort.getParamName())) {
			return false;
		}

		if (BaseResource.SP_RES_ID.equals(theSort.getParamName())) {
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
		RuntimeSearchParam param = myCallingDao.getSearchParamByName(resourceDef, theSort.getParamName());
		if (param == null) {
			throw new InvalidRequestException("Unknown sort parameter '" + theSort.getParamName() + "'");
		}

		String joinAttrName;
		String[] sortAttrName;
		JoinEnum joinType;

		switch (param.getParamType()) {
			case STRING:
				joinAttrName = "myParamsString";
				sortAttrName = new String[] {"myValueExact"};
				joinType = JoinEnum.STRING;
				break;
			case DATE:
				joinAttrName = "myParamsDate";
				sortAttrName = new String[] {"myValueLow"};
				joinType = JoinEnum.DATE;
				break;
			case REFERENCE:
				joinAttrName = "myResourceLinks";
				sortAttrName = new String[] {"myTargetResourcePid"};
				joinType = JoinEnum.REFERENCE;
				break;
			case TOKEN:
				joinAttrName = "myParamsToken";
				sortAttrName = new String[] {"mySystem", "myValue"};
				joinType = JoinEnum.TOKEN;
				break;
			case NUMBER:
				joinAttrName = "myParamsNumber";
				sortAttrName = new String[] {"myValue"};
				joinType = JoinEnum.NUMBER;
				break;
			case URI:
				joinAttrName = "myParamsUri";
				sortAttrName = new String[] {"myUri"};
				joinType = JoinEnum.URI;
				break;
			case QUANTITY:
				joinAttrName = "myParamsQuantity";
				sortAttrName = new String[] {"myValue"};
				joinType = JoinEnum.QUANTITY;
				break;
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
				Predicate joinParam1 = theBuilder.equal(join.get("myParamName"), theSort.getParamName());
				thePredicates.add(joinParam1);
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
			RuntimeSearchParam param = myCallingDao.getSearchParamByName(resourceDef, theParamName);
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

	private void doLoadPids(List<IBaseResource> theResourceListToPopulate, Set<Long> theRevIncludedPids, boolean theForHistoryOperation, EntityManager entityManager, FhirContext context, IDao theDao,
									Map<Long, Integer> position, Collection<Long> pids) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ResourceTable> cq = builder.createQuery(ResourceTable.class);
		Root<ResourceTable> from = cq.from(ResourceTable.class);
		cq.where(from.get("myId").in(pids));
		TypedQuery<ResourceTable> q = entityManager.createQuery(cq);

		List<ResourceTable> resultList = q.getResultList();

		for (ResourceTable next : resultList) {
			Class<? extends IBaseResource> resourceType = context.getResourceDefinition(next.getResourceType()).getImplementingClass();
			IBaseResource resource = theDao.toResource(resourceType, next, theForHistoryOperation);
			if (resource == null) {
				ourLog.warn("Unable to find resource {}/{}/_history/{} in database", next.getResourceType(), next.getIdDt().getIdPart(), next.getVersion());
				continue;
			}
			Integer index = position.get(next.getId());
			if (index == null) {
				ourLog.warn("Got back unexpected resource PID {}", next.getId());
				continue;
			}

			if (resource instanceof IResource) {
				if (theRevIncludedPids.contains(next.getId())) {
					ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put((IResource) resource, BundleEntrySearchModeEnum.INCLUDE);
				} else {
					ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put((IResource) resource, BundleEntrySearchModeEnum.MATCH);
				}
			} else {
				if (theRevIncludedPids.contains(next.getId())) {
					ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put((IAnyResource) resource, BundleEntrySearchModeEnum.INCLUDE.getCode());
				} else {
					ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put((IAnyResource) resource, BundleEntrySearchModeEnum.MATCH.getCode());
				}
			}

			theResourceListToPopulate.set(index, resource);
		}
	}

	@Override
	public void loadResourcesByPid(Collection<Long> theIncludePids, List<IBaseResource> theResourceListToPopulate, Set<Long> theRevIncludedPids, boolean theForHistoryOperation,
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
		int maxLoad = 800;
		List<Long> pids = new ArrayList<>(theIncludePids);
		for (int i = 0; i < pids.size(); i += maxLoad) {
			int to = i + maxLoad;
			to = Math.min(to, pids.size());
			List<Long> pidsSubList = pids.subList(i, to);
			doLoadPids(theResourceListToPopulate, theRevIncludedPids, theForHistoryOperation, entityManager, context, theDao, position, pidsSubList);
		}

	}

	/**
	 * THIS SHOULD RETURN HASHSET and not jsut Set because we add to it later (so it can't be Collections.emptySet())
	 *
	 * @param theLastUpdated
	 */
	@Override
	public HashSet<Long> loadReverseIncludes(IDao theCallingDao, FhirContext theContext, EntityManager theEntityManager, Collection<Long> theMatches, Set<Include> theRevIncludes,
														  boolean theReverseMode, DateRangeParam theLastUpdated) {
		if (theMatches.size() == 0) {
			return new HashSet<Long>();
		}
		if (theRevIncludes == null || theRevIncludes.isEmpty()) {
			return new HashSet<Long>();
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
			Set<Long> nextRoundOmit = new HashSet<>();

			for (Iterator<Include> iter = includes.iterator(); iter.hasNext(); ) {
				Include nextInclude = iter.next();
				if (nextInclude.isRecurse() == false) {
					iter.remove();
				}

				boolean matchAll = "*".equals(nextInclude.getValue());
				if (matchAll) {
					String sql;
					sql = "SELECT r FROM ResourceLink r WHERE r." + searchFieldName + " IN (:target_pids)";
					TypedQuery<ResourceLink> q = theEntityManager.createQuery(sql, ResourceLink.class);
					q.setParameter("target_pids", nextRoundMatches);
					List<ResourceLink> results = q.getResultList();
					for (ResourceLink resourceLink : results) {
						if (theReverseMode) {
							pidsToInclude.add(resourceLink.getSourceResourcePid());
						} else {
							pidsToInclude.add(resourceLink.getTargetResourcePid());
						}
					}
				} else {

					List<String> paths;
					RuntimeSearchParam param = null;
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
						param = theCallingDao.getSearchParamByName(def, paramName);
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
						boolean haveTargetTypesDefinedByParam = param != null && param.getTargets() != null && param.getTargets().isEmpty() == false;
						if (targetResourceType != null) {
							sql = "SELECT r FROM ResourceLink r WHERE r.mySourcePath = :src_path AND r." + searchFieldName + " IN (:target_pids) AND r.myTargetResourceType = :target_resource_type";
						} else if (haveTargetTypesDefinedByParam) {
							sql = "SELECT r FROM ResourceLink r WHERE r.mySourcePath = :src_path AND r." + searchFieldName + " IN (:target_pids) AND r.myTargetResourceType in (:target_resource_types)";
						} else {
							sql = "SELECT r FROM ResourceLink r WHERE r.mySourcePath = :src_path AND r." + searchFieldName + " IN (:target_pids)";
						}
						TypedQuery<ResourceLink> q = theEntityManager.createQuery(sql, ResourceLink.class);
						q.setParameter("src_path", nextPath);
						q.setParameter("target_pids", nextRoundMatches);
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

			if (theLastUpdated != null && (theLastUpdated.getLowerBoundAsInstant() != null || theLastUpdated.getUpperBoundAsInstant() != null)) {
				pidsToInclude = new HashSet<>(filterResourceIdsByLastUpdated(theEntityManager, theLastUpdated, pidsToInclude));
			}
			for (Long next : pidsToInclude) {
				if (original.contains(next) == false && allAdded.contains(next) == false) {
					theMatches.add(next);
				}
			}

			pidsToInclude.removeAll(nextRoundOmit);

			addedSomeThisRound = allAdded.addAll(pidsToInclude);
			nextRoundMatches = pidsToInclude;
		} while (includes.size() > 0 && nextRoundMatches.size() > 0 && addedSomeThisRound);

		ourLog.info("Loaded {} {} in {} rounds and {} ms", new Object[] {allAdded.size(), theReverseMode ? "_revincludes" : "_includes", roundCounts, w.getMillisAndRestart()});

		return allAdded;
	}

	private void searchForIdsWithAndOr(SearchParameterMap theParams) {
		SearchParameterMap params = theParams;
		if (params == null) {
			params = new SearchParameterMap();
		}
		myParams = theParams;

		for (Entry<String, List<List<? extends IQueryParameterType>>> nextParamEntry : params.entrySet()) {
			String nextParamName = nextParamEntry.getKey();
			List<List<? extends IQueryParameterType>> andOrParams = nextParamEntry.getValue();
			searchForIdsWithAndOr(myResourceName, nextParamName, andOrParams);

		}

	}

	private void searchForIdsWithAndOr(String theResourceName, String theParamName, List<List<? extends IQueryParameterType>> theAndOrParams) {

		for (int andListIdx = 0; andListIdx < theAndOrParams.size(); andListIdx++) {
			List<? extends IQueryParameterType> nextOrList = theAndOrParams.get(andListIdx);

			for (int orListIdx = 0; orListIdx < nextOrList.size(); orListIdx++) {
				IQueryParameterType nextOr = nextOrList.get(orListIdx);
				boolean hasNoValue = false;
				if (nextOr.getMissing() != null) {
					continue;
				}
				if (nextOr instanceof QuantityParam) {
					if (isBlank(((QuantityParam) nextOr).getValueAsString())) {
						hasNoValue = true;
					}
				}

				if (hasNoValue) {
					ourLog.debug("Ignoring empty parameter: {}", theParamName);
					nextOrList.remove(orListIdx);
					orListIdx--;
				}
			}

			if (nextOrList.isEmpty()) {
				theAndOrParams.remove(andListIdx);
				andListIdx--;
			}
		}

		if (theAndOrParams.isEmpty()) {
			return;
		}

		if (theParamName.equals(BaseResource.SP_RES_ID)) {

			addPredicateResourceId(theAndOrParams);

		} else if (theParamName.equals(BaseResource.SP_RES_LANGUAGE)) {

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
				qp = new CompositeParam<IQueryParameterType, IQueryParameterType>(leftParam, rightParam);
				break;
			case REFERENCE:
				qp = new ReferenceParam();
				break;
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

	private static Predicate createResourceLinkPathPredicate(IDao theCallingDao, FhirContext theContext, String theParamName, From<?, ? extends ResourceLink> theFrom,
																				String theResourceType) {
		RuntimeResourceDefinition resourceDef = theContext.getResourceDefinition(theResourceType);
		RuntimeSearchParam param = theCallingDao.getSearchParamByName(resourceDef, theParamName);
		List<String> path = param.getPathsSplit();

		/*
		 * SearchParameters can declare paths on multiple resources
		 * types. Here we only want the ones that actually apply.
		 */
		for (Iterator<String> iter = path.iterator(); iter.hasNext(); ) {
			if (!iter.next().startsWith(theResourceType + ".")) {
				iter.remove();
			}
		}
		return theFrom.get("mySourcePath").in(path);
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

		List<Long> resultList = query.getResultList();
		return resultList;
	}

	@VisibleForTesting
	public static HandlerTypeEnum getLastHandlerMechanismForUnitTest() {
		ourLog.info("Retrieving last handler mechanism: {}", ourLastHandlerMechanismForUnitTest);
		return ourLastHandlerMechanismForUnitTest;
	}

	@VisibleForTesting
	public static void resetLastHandlerMechanismForUnitTest() {
		ourLog.info("Clearing last handler mechanism (was {})", ourLastHandlerMechanismForUnitTest);
		ourLastHandlerMechanismForUnitTest = null;
	}

	static Predicate[] toArray(List<Predicate> thePredicates) {
		return thePredicates.toArray(new Predicate[thePredicates.size()]);
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
		private int myPageSize = myCallingDao.getConfig().getEverythingIncludesFetchPageSize();

		public IncludesIterator(Set<Long> thePidSet) {
			myCurrentPids = new ArrayList<Long>(thePidSet);
			myCurrentIterator = EMPTY_LONG_LIST.iterator();
			myCurrentOffset = 0;
		}

		private void fetchNext() {
			while (myNext == null) {

				if (myCurrentIterator.hasNext()) {
					myNext = myCurrentIterator.next();
					break;
				}

				if (!myCurrentIterator.hasNext()) {
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
					Set<Long> newPids = loadReverseIncludes(myCallingDao, myContext, myEntityManager, pidsToScan, includes, false, myParams.getLastUpdated());
					myCurrentIterator = newPids.iterator();
				}

			}
		}

		@Override
		public boolean hasNext() {
			fetchNext();
			return myNext != NO_MORE;
		}

		@Override
		public Long next() {
			fetchNext();
			Long retVal = myNext;
			myNext = null;
			return retVal;
		}

	}

	private final class QueryIterator extends BaseIterator<Long> implements Iterator<Long> {

		private final Set<Long> myPidSet = new HashSet<Long>();
		private boolean myFirst = true;
		private IncludesIterator myIncludesIterator;
		private Long myNext;
		private Iterator<Long> myPreResultsIterator;
		private Iterator<Long> myResultsIterator;
		private SortSpec mySort;
		private boolean myStillNeedToFetchIncludes;
		private StopWatch myStopwatch = null;

		private QueryIterator() {
			mySort = myParams.getSort();

			// Includes are processed inline for $everything query
			if (myParams.getEverythingMode() != null) {
				myStillNeedToFetchIncludes = true;
			}
		}

		private void fetchNext() {

			if (myFirst) {
				myStopwatch = new StopWatch();
			}

			// If we don't have a query yet, create one
			if (myResultsIterator == null) {
				Integer maximumResults = myCallingDao.getConfig().getFetchSizeDefaultMaximum();

				final TypedQuery<Long> query = createQuery(mySort, maximumResults);

				Query<Long> hibernateQuery = (Query<Long>) query;
				hibernateQuery.setFetchSize(myFetchSize);
				ScrollableResults scroll = hibernateQuery.scroll(ScrollMode.FORWARD_ONLY);
				myResultsIterator = new ScrollableResultsIterator(scroll);

				// If the query resulted in extra results being requested
				if (myAlsoIncludePids != null) {
					myPreResultsIterator = myAlsoIncludePids.iterator();
				}
			}

			if (myNext == null) {

				if (myPreResultsIterator != null && myPreResultsIterator.hasNext()) {
					while (myPreResultsIterator.hasNext()) {
						Long next = myPreResultsIterator.next();
						if (next != null && myPidSet.add(next)) {
							myNext = next;
							break;
						}
					}
				}

				if (myNext == null) {
					while (myResultsIterator.hasNext()) {
						Long next = myResultsIterator.next();
						if (next != null && myPidSet.add(next)) {
							myNext = next;
							break;
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
							if (next != null && myPidSet.add(next)) {
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

			if (myFirst) {
				ourLog.debug("Initial query result returned in {}ms for query {}", myStopwatch.getMillis(), mySearchUuid);
				myFirst = false;
			}

			if (myNext == NO_MORE) {
				ourLog.debug("Query found {} matches in {}ms for query {}", myPidSet.size(), myStopwatch.getMillis(), mySearchUuid);
			}

		}

		@Override
		public boolean hasNext() {
			if (myNext == null) {
				fetchNext();
			}
			return myNext != NO_MORE;
		}

		@Override
		public Long next() {
			fetchNext();
			Long retVal = myNext;
			myNext = null;
			Validate.isTrue(retVal != NO_MORE, "No more elements");
			return retVal;
		}
	}

	public class ScrollableResultsIterator extends BaseIterator<Long> implements Iterator<Long> {

		private Long myNext;
		private ScrollableResults myScroll;

		public ScrollableResultsIterator(ScrollableResults theScroll) {
			myScroll = theScroll;
		}

		private void ensureHaveNext() {
			if (myNext == null) {
				if (myScroll.next()) {
					myNext = (Long) myScroll.get(0);
				} else {
					myNext = NO_MORE;
				}
			}
		}

		@Override
		public boolean hasNext() {
			ensureHaveNext();
			return myNext != NO_MORE;
		}

		@Override
		public Long next() {
			ensureHaveNext();
			Validate.isTrue(myNext != NO_MORE);
			Long next = myNext;
			myNext = null;
			return next;
		}

	}

	private class UniqueIndexIterator implements Iterator<Long> {
		private final Set<String> myUniqueQueryStrings;
		private Iterator<Long> myWrap = null;

		public UniqueIndexIterator(Set<String> theUniqueQueryStrings) {
			myUniqueQueryStrings = theUniqueQueryStrings;
		}

		private void ensureHaveQuery() {
			if (myWrap == null) {
				ourLog.debug("Searching for unique index matches over {} candidate query strings", myUniqueQueryStrings.size());
				StopWatch sw = new StopWatch();
				Collection<Long> resourcePids = myCallingDao.getResourceIndexedCompositeStringUniqueDao().findResourcePidsByQueryStrings(myUniqueQueryStrings);
				ourLog.debug("Found {} unique index matches in {}ms", resourcePids.size(), sw.getMillis());
				myWrap = resourcePids.iterator();
			}
		}

		@Override
		public boolean hasNext() {
			ensureHaveQuery();
			return myWrap.hasNext();
		}

		@Override
		public Long next() {
			ensureHaveQuery();
			return myWrap.next();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	private static class JoinKey {
		private final JoinEnum myJoinType;
		private final String myParamName;

		public JoinKey(String theParamName, JoinEnum theJoinType) {
			super();
			myParamName = theParamName;
			myJoinType = theJoinType;
		}

		@Override
		public boolean equals(Object theObj) {
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

}
