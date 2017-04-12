package ca.uhn.fhir.jpa.dao;

import static org.apache.commons.lang3.StringUtils.defaultString;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.jpa.dao.data.IForcedIdDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamUriDao;
import ca.uhn.fhir.jpa.dao.data.ISearchResultDao;
import ca.uhn.fhir.jpa.entity.*;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProvider;
import ca.uhn.fhir.jpa.term.IHapiTerminologySvc;
import ca.uhn.fhir.jpa.term.VersionIndependentConcept;
import ca.uhn.fhir.jpa.util.StopWatch;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.base.composite.BaseIdentifierDt;
import ca.uhn.fhir.model.base.composite.BaseQuantityDt;
import ca.uhn.fhir.model.dstu.resource.BaseResource;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.method.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.UrlUtil;

public class SearchBuilder {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchBuilder.class);

	private BaseHapiFhirDao<?> myCallingDao;
	private FhirContext myContext;
	private EntityManager myEntityManager;
	private IForcedIdDao myForcedIdDao;
	private SearchParameterMap myParams;
	private Collection<Long> myPids;
	private PlatformTransactionManager myPlatformTransactionManager;
	private IResourceIndexedSearchParamUriDao myResourceIndexedSearchParamUriDao;
	private String myResourceName;
	private Class<? extends IBaseResource> myResourceType;
	private IFulltextSearchSvc mySearchDao;
	private Search mySearchEntity;
	private ISearchResultDao mySearchResultDao;
	private ISearchParamRegistry mySearchParamRegistry;

	private IHapiTerminologySvc myTerminologySvc;

	public SearchBuilder(FhirContext theFhirContext, EntityManager theEntityManager, PlatformTransactionManager thePlatformTransactionManager, IFulltextSearchSvc theSearchDao, ISearchResultDao theSearchResultDao, BaseHapiFhirDao<?> theDao,
			IResourceIndexedSearchParamUriDao theResourceIndexedSearchParamUriDao, IForcedIdDao theForcedIdDao, IHapiTerminologySvc theTerminologySvc, ISearchParamRegistry theSearchParamRegistry) {
		myContext = theFhirContext;
		myEntityManager = theEntityManager;
		myPlatformTransactionManager = thePlatformTransactionManager;
		mySearchDao = theSearchDao;
		mySearchResultDao = theSearchResultDao;
		myCallingDao = theDao;
		myResourceIndexedSearchParamUriDao = theResourceIndexedSearchParamUriDao;
		myForcedIdDao = theForcedIdDao;
		myTerminologySvc = theTerminologySvc;
		mySearchParamRegistry = theSearchParamRegistry;
	}

	private void addPredicateComposite(RuntimeSearchParam theParamDef, List<? extends IQueryParameterType> theNextAnd) {
		// TODO: fail if missing is set for a composite query

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceTable> from = cq.from(ResourceTable.class);
		cq.select(from.get("myId").as(Long.class));

		IQueryParameterType or = theNextAnd.get(0);
		if (!(or instanceof CompositeParam<?, ?>)) {
			throw new InvalidRequestException("Invalid type for composite param (must be " + CompositeParam.class.getSimpleName() + ": " + or.getClass());
		}
		CompositeParam<?, ?> cp = (CompositeParam<?, ?>) or;

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(builder.equal(from.get("myResourceType"), myResourceName));

		RuntimeSearchParam left = theParamDef.getCompositeOf().get(0);
		IQueryParameterType leftValue = cp.getLeftValue();
		predicates.add(createCompositeParamPart(builder, from, left, leftValue));

		RuntimeSearchParam right = theParamDef.getCompositeOf().get(1);
		IQueryParameterType rightValue = cp.getRightValue();
		predicates.add(createCompositeParamPart(builder, from, right, rightValue));

		createPredicateResourceId(builder, cq, predicates, from.get("myId").as(Long.class));
		cq.where(builder.and(toArray(predicates)));

		TypedQuery<Long> q = myEntityManager.createQuery(cq);
		doSetPids(q.getResultList());

	}

	private void addPredicateDate(String theParamName, List<? extends IQueryParameterType> theList) {

		if (Boolean.TRUE.equals(theList.get(0).getMissing())) {
			addPredicateParamMissing("myParamsDate", theParamName, ResourceIndexedSearchParamDate.class);
			return;
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceIndexedSearchParamDate> from = cq.from(ResourceIndexedSearchParamDate.class);
		cq.select(from.get("myResourcePid").as(Long.class));

		List<Predicate> codePredicates = new ArrayList<Predicate>();
		for (IQueryParameterType nextOr : theList) {
			if (addPredicateMissingFalseIfPresent(builder, theParamName, from, codePredicates, nextOr)) {
				continue;
			}

			IQueryParameterType params = nextOr;
			Predicate p = createPredicateDate(builder, from, params);
			codePredicates.add(p);
		}

		Predicate masterCodePredicate = builder.or(toArray(codePredicates));

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(builder.equal(from.get("myResourceType"), myResourceName));
		predicates.add(builder.equal(from.get("myParamName"), theParamName));
		createPredicateResourceId(builder, cq, predicates, from.get("myResourcePid").as(Long.class));
		createPredicateLastUpdatedForIndexedSearchParam(builder, from, predicates);
		predicates.add(masterCodePredicate);

		cq.where(builder.and(toArray(predicates)));

		TypedQuery<Long> q = myEntityManager.createQuery(cq);
		doSetPids(q.getResultList());
	}

	private void addPredicateHas(List<List<? extends IQueryParameterType>> theHasParameters, DateRangeParam theLastUpdated) {

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
				valueBuilder.append(UrlUtil.escape(next.getValueAsQueryToken(myContext)));
				targetResourceType = next.getTargetResourceType();
				owningParameter = next.getOwningFieldName();
				parameterName = next.getParameterName();
			}

			if (valueBuilder.length() == 0) {
				continue;
			}

			String matchUrl = targetResourceType + '?' + UrlUtil.escape(parameterName) + '=' + valueBuilder.toString();
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
				doSetPids(new ArrayList<Long>());
				return;
			}

			CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
			CriteriaQuery<Long> cq = builder.createQuery(Long.class);
			Root<ResourceLink> from = cq.from(ResourceLink.class);
			cq.select(from.get("myTargetResourcePid").as(Long.class));

			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(builder.equal(from.get("mySourceResourceType"), targetResourceType));
			predicates.add(from.get("mySourceResourcePid").in(match));
			predicates.add(createResourceLinkPathPredicate(myCallingDao, myContext, owningParameter, from, resourceType));
			predicates.add(builder.equal(from.get("myTargetResourceType"), myResourceName));
			createPredicateResourceId(builder, cq, predicates, from.get("myId").as(Long.class));
			createPredicateLastUpdatedForResourceLink(builder, from, predicates);

			cq.where(toArray(predicates));

			TypedQuery<Long> q = myEntityManager.createQuery(cq);
			doSetPids(q.getResultList());
			if (doHaveNoResults()) {
				return;
			}

			return;
		}
	}

	private void addPredicateId(Set<Long> thePids) {
		if (thePids == null || thePids.isEmpty()) {
			return;
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceTable> from = cq.from(ResourceTable.class);
		cq.select(from.get("myId").as(Long.class));

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(builder.equal(from.get("myResourceType"), myResourceName));
		predicates.add(from.get("myId").in(thePids));
		createPredicateResourceId(builder, cq, predicates, from.get("myId").as(Long.class));
		createPredicateLastUpdatedForResourceTable(builder, from, predicates);

		cq.where(toArray(predicates));

		TypedQuery<Long> q = myEntityManager.createQuery(cq);
		doSetPids(q.getResultList());
	}

	private void addPredicateLanguage(List<List<? extends IQueryParameterType>> theList) {
		for (List<? extends IQueryParameterType> nextList : theList) {

			CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
			CriteriaQuery<Long> cq = builder.createQuery(Long.class);
			Root<ResourceTable> from = cq.from(ResourceTable.class);
			cq.select(from.get("myId").as(Long.class));

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

			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(builder.equal(from.get("myResourceType"), myResourceName));
			predicates.add(from.get("myLanguage").as(String.class).in(values));
			createPredicateResourceId(builder, cq, predicates, from.get("myId").as(Long.class));
			createPredicateLastUpdatedForResourceTable(builder, from, predicates);

			predicates.add(builder.isNull(from.get("myDeleted")));

			cq.where(toArray(predicates));

			TypedQuery<Long> q = myEntityManager.createQuery(cq);
			doSetPids(q.getResultList());
			if (doHaveNoResults()) {
				return;
			}
		}

		return;
	}

	private boolean addPredicateMissingFalseIfPresent(CriteriaBuilder theBuilder, String theParamName, Root<? extends BaseResourceIndexedSearchParam> from, List<Predicate> codePredicates, IQueryParameterType nextOr) {
		boolean missingFalse = false;
		if (nextOr.getMissing() != null) {
			if (nextOr.getMissing().booleanValue() == true) {
				throw new InvalidRequestException(myContext.getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "multipleParamsWithSameNameOneIsMissingTrue", theParamName));
			}
			Predicate singleCode = from.get("myId").isNotNull();
			Predicate name = theBuilder.equal(from.get("myParamName"), theParamName);
			codePredicates.add(theBuilder.and(name, singleCode));
			missingFalse = true;
		}
		return missingFalse;
	}

	private boolean addPredicateMissingFalseIfPresentForResourceLink(CriteriaBuilder theBuilder, String theParamName, Root<? extends ResourceLink> from, List<Predicate> codePredicates, IQueryParameterType nextOr) {
		boolean missingFalse = false;
		if (nextOr.getMissing() != null) {
			if (nextOr.getMissing().booleanValue() == true) {
				throw new InvalidRequestException(myContext.getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "multipleParamsWithSameNameOneIsMissingTrue", theParamName));
			}
			Predicate singleCode = from.get("mySourceResource").isNotNull();
			Predicate name = createResourceLinkPathPredicate(theParamName, from);
			codePredicates.add(theBuilder.and(name, singleCode));
			missingFalse = true;
		}
		return missingFalse;
	}

	private void addPredicateNumber(String theParamName, List<? extends IQueryParameterType> theList) {

		if (Boolean.TRUE.equals(theList.get(0).getMissing())) {
			addPredicateParamMissing("myParamsNumber", theParamName, ResourceIndexedSearchParamNumber.class);
			return;
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceIndexedSearchParamNumber> from = cq.from(ResourceIndexedSearchParamNumber.class);
		cq.select(from.get("myResourcePid").as(Long.class));

		List<Predicate> codePredicates = new ArrayList<Predicate>();
		for (IQueryParameterType nextOr : theList) {
			IQueryParameterType params = nextOr;

			if (addPredicateMissingFalseIfPresent(builder, theParamName, from, codePredicates, nextOr)) {
				continue;
			}

			if (params instanceof NumberParam) {
				NumberParam param = (NumberParam) params;

				BigDecimal value = param.getValue();
				if (value == null) {
					continue;
				}

				final Expression<BigDecimal> fromObj = from.get("myValue");
				ParamPrefixEnum prefix = ObjectUtils.defaultIfNull(param.getPrefix(), ParamPrefixEnum.EQUAL);
				String invalidMessageName = "invalidNumberPrefix";
				String valueAsString = param.getValue().toPlainString();

				Predicate num = createPredicateNumeric(builder, params, prefix, value, fromObj, invalidMessageName, valueAsString);
				codePredicates.add(num);

			} else {
				throw new IllegalArgumentException("Invalid token type: " + params.getClass());
			}

		}

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(builder.equal(from.get("myResourceType"), myResourceName));
		predicates.add(builder.equal(from.get("myParamName"), theParamName));
		predicates.add(builder.or(toArray(codePredicates)));
		createPredicateResourceId(builder, cq, predicates, from.get("myResourcePid").as(Long.class));
		createPredicateLastUpdatedForIndexedSearchParam(builder, from, predicates);

		cq.where(builder.and(toArray(predicates)));

		TypedQuery<Long> q = myEntityManager.createQuery(cq);
		doSetPids(q.getResultList());
	}

	private void addPredicateParamMissing(String joinName, String theParamName, Class<? extends BaseResourceIndexedSearchParam> theParamTable) {
		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceTable> from = cq.from(ResourceTable.class);
		cq.select(from.get("myId").as(Long.class));

		Subquery<Long> subQ = cq.subquery(Long.class);
		Root<? extends BaseResourceIndexedSearchParam> subQfrom = subQ.from(theParamTable);
		subQ.select(subQfrom.get("myResourcePid").as(Long.class));
		Predicate subQname = builder.equal(subQfrom.get("myParamName"), theParamName);
		Predicate subQtype = builder.equal(subQfrom.get("myResourceType"), myResourceName);
		subQ.where(builder.and(subQtype, subQname));

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(builder.not(builder.in(from.get("myId")).value(subQ)));
		predicates.add(builder.equal(from.get("myResourceType"), myResourceName));
		predicates.add(builder.isNull(from.get("myDeleted")));
		createPredicateResourceId(builder, cq, predicates, from.get("myId").as(Long.class));

		cq.where(builder.and(toArray(predicates)));

		ourLog.info("Adding :missing qualifier for parameter '{}'", theParamName);

		TypedQuery<Long> q = myEntityManager.createQuery(cq);
		doSetPids(q.getResultList());
	}

	private void addPredicateParamMissingResourceLink(String joinName, String theParamName) {
		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceTable> from = cq.from(ResourceTable.class);
		cq.select(from.get("myId").as(Long.class));

		Subquery<Long> subQ = cq.subquery(Long.class);
		Root<ResourceLink> subQfrom = subQ.from(ResourceLink.class);
		subQ.select(subQfrom.get("mySourceResourcePid").as(Long.class));

		// subQ.where(builder.equal(subQfrom.get("myParamName"), theParamName));
		Predicate path = createResourceLinkPathPredicate(theParamName, subQfrom);
		subQ.where(path);

		List<Predicate> predicates = new ArrayList<Predicate>();
		createPredicateResourceId(builder, cq, predicates, from.get("myId").as(Long.class));
		predicates.add(builder.not(builder.in(from.get("myId")).value(subQ)));
		predicates.add(builder.equal(from.get("myResourceType"), myResourceName));

		cq.where(builder.and(toArray(predicates)));

		TypedQuery<Long> q = myEntityManager.createQuery(cq);
		List<Long> resultList = q.getResultList();
		doSetPids(new HashSet<Long>(resultList));
	}

	private void addPredicateQuantity(String theParamName, List<? extends IQueryParameterType> theList) {
		if (Boolean.TRUE.equals(theList.get(0).getMissing())) {
			addPredicateParamMissing("myParamsQuantity", theParamName, ResourceIndexedSearchParamQuantity.class);
			return;
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceIndexedSearchParamQuantity> from = cq.from(ResourceIndexedSearchParamQuantity.class);
		cq.select(from.get("myResourcePid").as(Long.class));

		List<Predicate> codePredicates = new ArrayList<Predicate>();
		for (IQueryParameterType nextOr : theList) {
			if (addPredicateMissingFalseIfPresent(builder, theParamName, from, codePredicates, nextOr)) {
				continue;
			}

			Predicate singleCode = createPredicateQuantity(builder, from, nextOr);
			codePredicates.add(singleCode);
		}

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(builder.equal(from.get("myResourceType"), myResourceName));
		predicates.add(builder.equal(from.get("myParamName"), theParamName));
		predicates.add(builder.or(toArray(codePredicates)));
		createPredicateResourceId(builder, cq, predicates, from.get("myResourcePid").as(Long.class));
		createPredicateLastUpdatedForIndexedSearchParam(builder, from, predicates);

		cq.where(builder.and(toArray(predicates)));

		TypedQuery<Long> q = myEntityManager.createQuery(cq);
		doSetPids(new HashSet<Long>(q.getResultList()));
	}

	private void addPredicateReference(String theParamName, List<? extends IQueryParameterType> theList) {
		assert theParamName.contains(".") == false;

		if (Boolean.TRUE.equals(theList.get(0).getMissing())) {
			addPredicateParamMissingResourceLink("myResourceLinks", theParamName);
			return;
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceLink> from = cq.from(ResourceLink.class);
		cq.select(from.get("mySourceResourcePid").as(Long.class));

		List<Predicate> codePredicates = new ArrayList<Predicate>();

		for (IQueryParameterType nextOr : theList) {
			IQueryParameterType params = nextOr;

			if (addPredicateMissingFalseIfPresentForResourceLink(builder, theParamName, from, codePredicates, nextOr)) {
				continue;
			}

			if (params instanceof ReferenceParam) {
				ReferenceParam ref = (ReferenceParam) params;

				if (isBlank(ref.getChain())) {
					IIdType dt = new IdDt(ref.getBaseUrl(), ref.getResourceType(), ref.getIdPart(), null);
					
					if (dt.hasBaseUrl()) {
						if (myCallingDao.getConfig().getTreatBaseUrlsAsLocal().contains(dt.getBaseUrl())) {
							dt = dt.toUnqualified();
						} else {
							ourLog.debug("Searching for resource link with target URL: {}", dt.getValue());
							Predicate eq = builder.equal(from.get("myTargetResourceUrl"), dt.getValue());
							codePredicates.add(eq);
							continue;
						}
					}
					
					List<Long> targetPid;
					try {
						targetPid = myCallingDao.translateForcedIdToPids(dt);
					} catch (ResourceNotFoundException e) {
						doSetPids(new ArrayList<Long>());
						return;
					}
					for (Long next : targetPid) {
						ourLog.debug("Searching for resource link with target PID: {}", next);
						Predicate eq = builder.equal(from.get("myTargetResourcePid"), next);
						codePredicates.add(eq);
					}
				} else {
					
					List<Class<? extends IBaseResource>> resourceTypes;
					String resourceId;
					if (!ref.getValue().matches("[a-zA-Z]+\\/.*")) {
						
						RuntimeResourceDefinition resourceDef = myContext.getResourceDefinition(myResourceType);
						String paramPath = myCallingDao.getSearchParamByName(resourceDef, theParamName).getPath();
						if (paramPath.endsWith(".as(Reference)")) {
							paramPath = paramPath.substring(0, paramPath.length() - ".as(Reference)".length()) + "Reference";
						}
						
						BaseRuntimeChildDefinition def = myContext.newTerser().getDefinition(myResourceType, paramPath);
						if (def instanceof RuntimeChildChoiceDefinition) {
							RuntimeChildChoiceDefinition choiceDef = (RuntimeChildChoiceDefinition)def;
							resourceTypes = choiceDef.getResourceTypes();
						} else if (def instanceof RuntimeChildResourceDefinition) {
							RuntimeChildResourceDefinition resDef = (RuntimeChildResourceDefinition) def;
							resourceTypes = resDef.getResourceTypes();
						} else {
							throw new ConfigurationException("Property " + paramPath + " of type " + myResourceName + " is not a resource: " + def.getClass());
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
								ourLog.debug("Type {} parameter {} is not a reference, can not chain {}", new Object[] { nextType.getSimpleName(), chain, remainingChain });
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

						Set<Long> pids = dao.searchForIds(chain, chainValue);
						if (pids.isEmpty()) {
							continue;
						}

						Predicate eq = from.get("myTargetResourcePid").in(pids);
						codePredicates.add(eq);

					}

					if (!foundChainMatch) {
						throw new InvalidRequestException(myContext.getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "invalidParameterChain", theParamName + '.' + ref.getChain()));
					}
				}

			} else {
				throw new IllegalArgumentException("Invalid token type (expecting ReferenceParam): " + params.getClass());
			}

		}

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(createResourceLinkPathPredicate(theParamName, from));
		predicates.add(builder.or(toArray(codePredicates)));
		createPredicateResourceId(builder, cq, predicates, from.get("mySourceResourcePid").as(Long.class));
		createPredicateLastUpdatedForResourceLink(builder, from, predicates);

		cq.where(builder.and(toArray(predicates)));

		TypedQuery<Long> q = myEntityManager.createQuery(cq);
		doSetPids(new HashSet<Long>(q.getResultList()));
	}

	private void addPredicateString(String theParamName, List<? extends IQueryParameterType> theList) {
		if (Boolean.TRUE.equals(theList.get(0).getMissing())) {
			addPredicateParamMissing("myParamsString", theParamName, ResourceIndexedSearchParamString.class);
			return;
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceIndexedSearchParamString> from = cq.from(ResourceIndexedSearchParamString.class);
		cq.select(from.get("myResourcePid").as(Long.class));

		List<Predicate> codePredicates = new ArrayList<Predicate>();
		for (IQueryParameterType nextOr : theList) {
			IQueryParameterType theParameter = nextOr;
			if (addPredicateMissingFalseIfPresent(builder, theParamName, from, codePredicates, nextOr)) {
				continue;
			}

			Predicate singleCode = createPredicateString(theParameter, theParamName, builder, from);
			codePredicates.add(singleCode);
		}

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(builder.equal(from.get("myResourceType"), myResourceName));
		predicates.add(builder.equal(from.get("myParamName"), theParamName));
		predicates.add(builder.or(toArray(codePredicates)));

		createPredicateResourceId(builder, cq, predicates, from.get("myResourcePid").as(Long.class));
		createPredicateLastUpdatedForIndexedSearchParam(builder, from, predicates);

		cq.where(builder.and(toArray(predicates)));

		TypedQuery<Long> q = myEntityManager.createQuery(cq);
		doSetPids(new HashSet<Long>(q.getResultList()));
	}

	private void addPredicateTag(List<List<? extends IQueryParameterType>> theList, String theParamName, DateRangeParam theLastUpdated) {
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

		/*
		 * CriteriaBuilder builder = myEntityManager.getCriteriaBuilder(); CriteriaQuery<Long> cq =
		 * builder.createQuery(Long.class); Root<ResourceTable> from = cq.from(ResourceTable.class);
		 * cq.select(from.get("myId").as(Long.class));
		 * 
		 * Subquery<Long> subQ = cq.subquery(Long.class); Root<? extends BaseResourceIndexedSearchParam> subQfrom =
		 * subQ.from(theParamTable); subQ.select(subQfrom.get("myResourcePid").as(Long.class));
		 * Predicate subQname = builder.equal(subQfrom.get("myParamName"), theParamName); Predicate subQtype =
		 * builder.equal(subQfrom.get("myResourceType"), myResourceName);
		 * subQ.where(builder.and(subQtype, subQname));
		 * 
		 * List<Predicate> predicates = new ArrayList<Predicate>();
		 * predicates.add(builder.not(builder.in(from.get("myId")).value(subQ)));
		 * predicates.add(builder.equal(from.get("myResourceType"),
		 * myResourceName)); predicates.add(builder.isNull(from.get("myDeleted"))); createPredicateResourceId(builder, cq,
		 * predicates, from.get("myId").as(Long.class));
		 */

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

			CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();

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

				CriteriaQuery<Long> cq = builder.createQuery(Long.class);
				Root<ResourceTable> newFrom = cq.from(ResourceTable.class);

				Subquery<Long> subQ = cq.subquery(Long.class);
				Root<ResourceTag> subQfrom = subQ.from(ResourceTag.class);
				subQ.select(subQfrom.get("myResourceId").as(Long.class));

				cq.select(newFrom.get("myId").as(Long.class));

				List<Predicate> andPredicates = new ArrayList<Predicate>();
				andPredicates = new ArrayList<Predicate>();
				andPredicates.add(builder.equal(newFrom.get("myResourceType"), myResourceName));
				andPredicates.add(builder.not(builder.in(newFrom.get("myId")).value(subQ)));

				Subquery<Long> defJoin = subQ.subquery(Long.class);
				Root<TagDefinition> defJoinFrom = defJoin.from(TagDefinition.class);
				defJoin.select(defJoinFrom.get("myId").as(Long.class));

				subQ.where(subQfrom.get("myTagId").as(Long.class).in(defJoin));

				List<Predicate> orPredicates = createPredicateTagList(defJoinFrom, builder, tagType, tokens);
				defJoin.where(toArray(orPredicates));

				cq.where(toArray(andPredicates));

				TypedQuery<Long> q = myEntityManager.createQuery(cq);
				Set<Long> pids = new HashSet<Long>(q.getResultList());
				doSetPids(pids);
				continue;
			}

			CriteriaQuery<Long> cq = builder.createQuery(Long.class);
			Root<ResourceTag> from = cq.from(ResourceTag.class);
			List<Predicate> andPredicates = new ArrayList<Predicate>();
			andPredicates.add(builder.equal(from.get("myResourceType"), myResourceName));
			From<ResourceTag, TagDefinition> defJoin = from.join("myTag");

			Join<?, ResourceTable> defJoin2 = from.join("myResource");

			Predicate notDeletedPredicatePrediate = builder.isNull(defJoin2.get("myDeleted"));
			andPredicates.add(notDeletedPredicatePrediate);

			List<Predicate> orPredicates = createPredicateTagList(defJoin, builder, tagType, tokens);
			andPredicates.add(builder.or(toArray(orPredicates)));

			if (theLastUpdated != null) {
				andPredicates.addAll(createLastUpdatedPredicates(theLastUpdated, builder, defJoin2));
			}

			createPredicateResourceId(builder, cq, andPredicates, from.get("myResourceId").as(Long.class));
			Predicate masterCodePredicate = builder.and(toArray(andPredicates));

			cq.select(from.get("myResourceId").as(Long.class));
			cq.where(masterCodePredicate);

			TypedQuery<Long> q = myEntityManager.createQuery(cq);
			Set<Long> pids = new HashSet<Long>(q.getResultList());
			doSetPids(pids);
		}

	}

	private void addPredicateToken(String theParamName, List<? extends IQueryParameterType> theList) {

		if (Boolean.TRUE.equals(theList.get(0).getMissing())) {
			addPredicateParamMissing("myParamsToken", theParamName, ResourceIndexedSearchParamToken.class);
			return;
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceIndexedSearchParamToken> from = cq.from(ResourceIndexedSearchParamToken.class);
		cq.select(from.get("myResourcePid").as(Long.class));

		List<Predicate> codePredicates = new ArrayList<Predicate>();
		for (IQueryParameterType nextOr : theList) {
			if (addPredicateMissingFalseIfPresent(builder, theParamName, from, codePredicates, nextOr)) {
				continue;
			}

			if (nextOr instanceof TokenParam) {
				TokenParam id = (TokenParam) nextOr;
				if (id.isText()) {
					addPredicateString(theParamName, theList);
					continue;
				}
			}

			Predicate singleCode = createPredicateToken(nextOr, theParamName, builder, from);
			if (singleCode == null) {
				doSetPids(new ArrayList<Long>());
				return;
			}
			codePredicates.add(singleCode);
		}

		if (codePredicates.isEmpty()) {
			return;
		}

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(builder.equal(from.get("myResourceType"), myResourceName));
		predicates.add(builder.equal(from.get("myParamName"), theParamName));
		predicates.add(builder.or(toArray(codePredicates)));
		createPredicateResourceId(builder, cq, predicates, from.get("myResourcePid").as(Long.class));

		cq.where(builder.and(toArray(predicates)));

		TypedQuery<Long> q = myEntityManager.createQuery(cq);
		doSetPids(new HashSet<Long>(q.getResultList()));
	}

	private void addPredicateUri(String theParamName, List<? extends IQueryParameterType> theList) {
		if (Boolean.TRUE.equals(theList.get(0).getMissing())) {
			addPredicateParamMissing("myParamsUri", theParamName, ResourceIndexedSearchParamUri.class);
			return;
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceIndexedSearchParamUri> from = cq.from(ResourceIndexedSearchParamUri.class);
		cq.select(from.get("myResourcePid").as(Long.class));

		List<Predicate> codePredicates = new ArrayList<Predicate>();
		for (IQueryParameterType nextOr : theList) {
			IQueryParameterType params = nextOr;

			if (addPredicateMissingFalseIfPresent(builder, theParamName, from, codePredicates, nextOr)) {
				continue;
			}

			if (params instanceof UriParam) {
				UriParam param = (UriParam) params;

				String value = param.getValue();
				if (value == null) {
					continue;
				}

				Path<Object> fromObj = from.get("myUri");
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
					List<String> toFind = new ArrayList<String>();
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

					predicate = fromObj.as(String.class).in(toFind);

				} else if (param.getQualifier() == UriParamQualifierEnum.BELOW) {
					predicate = builder.like(fromObj.as(String.class), createLeftMatchLikeExpression(value));
				} else {
					predicate = builder.equal(fromObj.as(String.class), value);
				}
				codePredicates.add(predicate);
			} else {
				throw new IllegalArgumentException("Invalid URI type: " + params.getClass());
			}

		}

		if (codePredicates.isEmpty()) {
			doSetPids(new HashSet<Long>());
			return;
		}

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(builder.equal(from.get("myResourceType"), myResourceName));
		predicates.add(builder.equal(from.get("myParamName"), theParamName));
		predicates.add(builder.or(toArray(codePredicates)));
		createPredicateResourceId(builder, cq, predicates, from.get("myResourcePid").as(Long.class));

		cq.where(builder.and(toArray(predicates)));

		TypedQuery<Long> q = myEntityManager.createQuery(cq);
		doSetPids(new HashSet<Long>(q.getResultList()));
	}

	private Predicate createCompositeParamPart(CriteriaBuilder builder, Root<ResourceTable> from, RuntimeSearchParam left, IQueryParameterType leftValue) {
		Predicate retVal = null;
		switch (left.getParamType()) {
		case STRING: {
			From<ResourceIndexedSearchParamString, ResourceIndexedSearchParamString> stringJoin = from.join("myParamsString", JoinType.INNER);
			retVal = createPredicateString(leftValue, left.getName(), builder, stringJoin);
			break;
		}
		case TOKEN: {
			From<ResourceIndexedSearchParamToken, ResourceIndexedSearchParamToken> tokenJoin = from.join("myParamsToken", JoinType.INNER);
			retVal = createPredicateToken(leftValue, left.getName(), builder, tokenJoin);
			break;
		}
		case DATE: {
			From<ResourceIndexedSearchParamDate, ResourceIndexedSearchParamDate> dateJoin = from.join("myParamsDate", JoinType.INNER);
			retVal = createPredicateDate(builder, dateJoin, leftValue);
			break;
		}
		case QUANTITY: {
			From<ResourceIndexedSearchParamQuantity, ResourceIndexedSearchParamQuantity> dateJoin = from.join("myParamsQuantity", JoinType.INNER);
			retVal = createPredicateQuantity(builder, dateJoin, leftValue);
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
			throw new InvalidRequestException("Don't know how to handle composite parameter with type of " + left.getParamType());
		}

		return retVal;
	}

	private Predicate createPredicateDate(CriteriaBuilder theBuilder, From<ResourceIndexedSearchParamDate, ResourceIndexedSearchParamDate> theFrom, IQueryParameterType theParam) {
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
		return p;
	}

	private Predicate createPredicateDateFromRange(CriteriaBuilder theBuilder, From<ResourceIndexedSearchParamDate, ResourceIndexedSearchParamDate> theFrom, DateRangeParam theRange) {
		Date lowerBound = theRange.getLowerBoundAsInstant();
		Date upperBound = theRange.getUpperBoundAsInstant();

		Predicate lb = null;
		if (lowerBound != null) {
			Predicate gt = theBuilder.greaterThanOrEqualTo(theFrom.<Date> get("myValueLow"), lowerBound);
			Predicate lt = theBuilder.greaterThanOrEqualTo(theFrom.<Date> get("myValueHigh"), lowerBound);
			if (theRange.getLowerBound().getPrefix() == ParamPrefixEnum.STARTS_AFTER || theRange.getLowerBound().getPrefix() == ParamPrefixEnum.EQUAL) {
				lb = gt;
			} else {
				lb = theBuilder.or(gt, lt);
			}
		}

		Predicate ub = null;
		if (upperBound != null) {
			Predicate gt = theBuilder.lessThanOrEqualTo(theFrom.<Date> get("myValueLow"), upperBound);
			Predicate lt = theBuilder.lessThanOrEqualTo(theFrom.<Date> get("myValueHigh"), upperBound);
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

	private void createPredicateLastUpdatedForIndexedSearchParam(CriteriaBuilder builder, Root<? extends BaseResourceIndexedSearchParam> from, List<Predicate> predicates) {
		DateRangeParam lastUpdated = myParams.getLastUpdatedAndRemove();
		if (lastUpdated != null) {
			From<BaseResourceIndexedSearchParam, ResourceTable> defJoin = from.join("myResource", JoinType.INNER);
			List<Predicate> lastUpdatedPredicates = createLastUpdatedPredicates(lastUpdated, builder, defJoin);
			predicates.addAll(lastUpdatedPredicates);
		}
	}

	private void createPredicateLastUpdatedForResourceLink(CriteriaBuilder builder, Root<ResourceLink> from, List<Predicate> predicates) {
		DateRangeParam lastUpdated = myParams.getLastUpdatedAndRemove();
		if (lastUpdated != null) {
			From<BaseResourceIndexedSearchParam, ResourceTable> defJoin = from.join("mySourceResource", JoinType.INNER);
			List<Predicate> lastUpdatedPredicates = createLastUpdatedPredicates(lastUpdated, builder, defJoin);
			predicates.addAll(lastUpdatedPredicates);
		}
	}

	private void createPredicateLastUpdatedForResourceTable(CriteriaBuilder builder, Root<ResourceTable> from, List<Predicate> predicates) {
		predicates.addAll(createLastUpdatedPredicates(myParams.getLastUpdatedAndRemove(), builder, from));
	}

	private Predicate createPredicateNumeric(CriteriaBuilder builder, IQueryParameterType params, ParamPrefixEnum cmpValue, BigDecimal valueValue, final Expression<BigDecimal> path, String invalidMessageName, String theValueString) {
		Predicate num;
		switch (cmpValue) {
		case GREATERTHAN:
			num = builder.gt(path, valueValue);
			break;
		case GREATERTHAN_OR_EQUALS:
			num = builder.ge(path, valueValue);
			break;
		case LESSTHAN:
			num = builder.lt(path, valueValue);
			break;
		case LESSTHAN_OR_EQUALS:
			num = builder.le(path, valueValue);
			break;
		case APPROXIMATE:
		case EQUAL:
		case NOT_EQUAL:
			BigDecimal mul = calculateFuzzAmount(cmpValue, valueValue);
			BigDecimal low = valueValue.subtract(mul, MathContext.DECIMAL64);
			BigDecimal high = valueValue.add(mul, MathContext.DECIMAL64);
			Predicate lowPred;
			Predicate highPred;
			if (cmpValue != ParamPrefixEnum.NOT_EQUAL) {
				lowPred = builder.ge(path.as(BigDecimal.class), low);
				highPred = builder.le(path.as(BigDecimal.class), high);
				num = builder.and(lowPred, highPred);
				ourLog.trace("Searching for {} <= val <= {}", low, high);
			} else {
				// Prefix was "ne", so reverse it!
				lowPred = builder.lt(path.as(BigDecimal.class), low);
				highPred = builder.gt(path.as(BigDecimal.class), high);
				num = builder.or(lowPred, highPred);
			}
			break;
		default:
			String msg = myContext.getLocalizer().getMessage(SearchBuilder.class, invalidMessageName, cmpValue.getValue(), params.getValueAsQueryToken(myContext));
			throw new InvalidRequestException(msg);
		}
		return num;
	}

	private Predicate createPredicateQuantity(CriteriaBuilder theBuilder, From<ResourceIndexedSearchParamQuantity, ResourceIndexedSearchParamQuantity> theFrom, IQueryParameterType theParam) {
		String systemValue;
		String unitsValue;
		ParamPrefixEnum cmpValue;
		BigDecimal valueValue;
		String valueString;

		if (theParam instanceof BaseQuantityDt) {
			BaseQuantityDt param = (BaseQuantityDt) theParam;
			systemValue = param.getSystemElement().getValueAsString();
			unitsValue = param.getUnitsElement().getValueAsString();
			cmpValue = ParamPrefixEnum.forDstu1Value(param.getComparatorElement().getValueAsString());
			valueValue = param.getValueElement().getValue();
			valueString = param.getValueElement().getValueAsString();
		} else if (theParam instanceof QuantityParam) {
			QuantityParam param = (QuantityParam) theParam;
			systemValue = param.getSystem();
			unitsValue = param.getUnits();
			cmpValue = param.getPrefix();
			valueValue = param.getValue();
			valueString = param.getValueAsString();
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

		Predicate num = createPredicateNumeric(theBuilder, theParam, cmpValue, valueValue, path, invalidMessageName, valueString);

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

		return singleCode;
	}

	private void createPredicateResourceId(CriteriaBuilder builder, CriteriaQuery<?> cq, List<Predicate> thePredicates, Expression<Long> theExpression) {
		if (myParams.isPersistResults()) {
			if (mySearchEntity.getTotalCount() > -1) {
				Subquery<Long> subQ = cq.subquery(Long.class);
				Root<SearchResult> subQfrom = subQ.from(SearchResult.class);
				subQ.select(subQfrom.get("myResourcePid").as(Long.class));
				Predicate subQname = builder.equal(subQfrom.get("mySearch"), mySearchEntity);
				subQ.where(subQname);

				thePredicates.add(theExpression.in(subQ));
			}
		} else {
			if (myPids != null) {
				thePredicates.add(theExpression.in(myPids));
			}
		}

	}

	private Predicate createPredicateString(IQueryParameterType theParameter, String theParamName, CriteriaBuilder theBuilder, From<ResourceIndexedSearchParamString, ResourceIndexedSearchParamString> theFrom) {
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
		} else if (theParameter instanceof IPrimitiveDatatype<?>) {
			IPrimitiveDatatype<?> id = (IPrimitiveDatatype<?>) theParameter;
			rawSearchTerm = id.getValueAsString();
		} else {
			throw new IllegalArgumentException("Invalid token type: " + theParameter.getClass());
		}

		if (rawSearchTerm.length() > ResourceIndexedSearchParamString.MAX_LENGTH) {
			throw new InvalidRequestException("Parameter[" + theParamName + "] has length (" + rawSearchTerm.length() + ") that is longer than maximum allowed (" + ResourceIndexedSearchParamString.MAX_LENGTH + "): " + rawSearchTerm);
		}

		String likeExpression = BaseHapiFhirDao.normalizeString(rawSearchTerm);
		likeExpression = createLeftMatchLikeExpression(likeExpression);

		Predicate singleCode = theBuilder.like(theFrom.get("myValueNormalized").as(String.class), likeExpression);
		if (theParameter instanceof StringParam && ((StringParam) theParameter).isExact()) {
			Predicate exactCode = theBuilder.equal(theFrom.get("myValueExact"), rawSearchTerm);
			singleCode = theBuilder.and(singleCode, exactCode);
		}
		return singleCode;
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

	private Predicate createPredicateToken(IQueryParameterType theParameter, String theParamName, CriteriaBuilder theBuilder, From<ResourceIndexedSearchParamToken, ResourceIndexedSearchParamToken> theFrom) {
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
			throw new InvalidRequestException("Parameter[" + theParamName + "] has system (" + system.length() + ") that is longer than maximum allowed (" + ResourceIndexedSearchParamToken.MAX_LENGTH + "): " + system);
		}

		if (code != null && code.length() > ResourceIndexedSearchParamToken.MAX_LENGTH) {
			throw new InvalidRequestException("Parameter[" + theParamName + "] has code (" + code.length() + ") that is longer than maximum allowed (" + ResourceIndexedSearchParamToken.MAX_LENGTH + "): " + code);
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
		
		if (codes != null) {
			if (codes.isEmpty()) {
				return null;
			}
			List<Predicate> orPredicates = new ArrayList<Predicate>();
			for (VersionIndependentConcept nextCode : codes) {
				Predicate systemPredicate = theBuilder.equal(theFrom.get("mySystem"), nextCode.getSystem());
				Predicate codePredicate = theBuilder.equal(theFrom.get("myValue"), nextCode.getCode());
				orPredicates.add(theBuilder.and(systemPredicate, codePredicate));
			}

			return theBuilder.or(orPredicates.toArray(new Predicate[orPredicates.size()]));
		}

		/*
		 * Ok, this is a normal query
		 */

		ArrayList<Predicate> singleCodePredicates = (new ArrayList<Predicate>());
		if (StringUtils.isNotBlank(system)) {
			singleCodePredicates.add(theBuilder.equal(theFrom.get("mySystem"), system));
		} else if (system == null) {
			// don't check the system
		} else {
			// If the system is "", we only match on null systems
			singleCodePredicates.add(theBuilder.isNull(theFrom.get("mySystem")));
		}

		if (StringUtils.isNotBlank(code)) {
			singleCodePredicates.add(theBuilder.equal(theFrom.get("myValue"), code));
		} else {
			/*
			 * As of HAPI FHIR 1.5, if the client searched for a token with a system but no specified value this means to
			 * match all tokens with the given value.
			 * 
			 * I'm not sure I agree with this, but hey.. FHIR-I voted and this was the result :)
			 */
			// singleCodePredicates.add(theBuilder.isNull(theFrom.get("myValue")));
		}

		Predicate singleCode = theBuilder.and(toArray(singleCodePredicates));
		return singleCode;
	}

	private Predicate createResourceLinkPathPredicate(String theParamName, Root<? extends ResourceLink> from) {
		return createResourceLinkPathPredicate(myCallingDao, myContext, theParamName, from, myResourceType);
	}

	private TypedQuery<Long> createSearchAllByTypeQuery(DateRangeParam theLastUpdated) {
		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceTable> from = cq.from(ResourceTable.class);
		cq.select(from.get("myId").as(Long.class));
		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(builder.equal(from.get("myResourceType"), myResourceName));
		predicates.add(builder.isNull(from.get("myDeleted")));

		if (theLastUpdated != null) {
			predicates.addAll(createLastUpdatedPredicates(theLastUpdated, builder, from));
		}

		cq.where(toArray(predicates));

		TypedQuery<Long> query = myEntityManager.createQuery(cq);
		return query;
	}

	private void createSort(CriteriaBuilder theBuilder, Root<ResourceTable> theFrom, SortSpec theSort, List<Order> theOrders, List<Predicate> thePredicates) {
		if (theSort == null || isBlank(theSort.getParamName())) {
			return;
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

			createSort(theBuilder, theFrom, theSort.getChain(), theOrders, thePredicates);
			return;
		}

		if (Constants.PARAM_LASTUPDATED.equals(theSort.getParamName())) {
			if (theSort.getOrder() == null || theSort.getOrder() == SortOrderEnum.ASC) {
				theOrders.add(theBuilder.asc(theFrom.get("myUpdated")));
			} else {
				theOrders.add(theBuilder.desc(theFrom.get("myUpdated")));
			}

			createSort(theBuilder, theFrom, theSort.getChain(), theOrders, thePredicates);
			return;
		}

		RuntimeResourceDefinition resourceDef = myContext.getResourceDefinition(myResourceName);
		RuntimeSearchParam param = myCallingDao.getSearchParamByName(resourceDef, theSort.getParamName());
		if (param == null) {
			throw new InvalidRequestException("Unknown sort parameter '" + theSort.getParamName() + "'");
		}

		String joinAttrName;
		String[] sortAttrName;

		switch (param.getParamType()) {
		case STRING:
			joinAttrName = "myParamsString";
			sortAttrName = new String[] { "myValueExact" };
			break;
		case DATE:
			joinAttrName = "myParamsDate";
			sortAttrName = new String[] { "myValueLow" };
			break;
		case REFERENCE:
			joinAttrName = "myResourceLinks";
			sortAttrName = new String[] { "myTargetResourcePid" };
			break;
		case TOKEN:
			joinAttrName = "myParamsToken";
			sortAttrName = new String[] { "mySystem", "myValue" };
			break;
		case NUMBER:
			joinAttrName = "myParamsNumber";
			sortAttrName = new String[] { "myValue" };
			break;
		case URI:
			joinAttrName = "myParamsUri";
			sortAttrName = new String[] { "myUri" };
			break;
		case QUANTITY:
			joinAttrName = "myParamsQuantity";
			sortAttrName = new String[] { "myValue" };
			break;
		default:
			throw new InvalidRequestException("This server does not support _sort specifications of type " + param.getParamType() + " - Can't serve _sort=" + theSort.getParamName());
		}

		From<?, ?> stringJoin = theFrom.join(joinAttrName, JoinType.INNER);

		if (param.getParamType() == RestSearchParameterTypeEnum.REFERENCE) {
			thePredicates.add(stringJoin.get("mySourcePath").as(String.class).in(param.getPathsSplit()));
		} else {
			thePredicates.add(theBuilder.equal(stringJoin.get("myParamName"), theSort.getParamName()));
		}

		// Predicate p = theBuilder.equal(stringJoin.get("myParamName"), theSort.getParamName());
		// Predicate pn = theBuilder.isNull(stringJoin.get("myParamName"));
		// thePredicates.add(theBuilder.or(p, pn));

		for (String next : sortAttrName) {
			if (theSort.getOrder() == null || theSort.getOrder() == SortOrderEnum.ASC) {
				theOrders.add(theBuilder.asc(stringJoin.get(next)));
			} else {
				theOrders.add(theBuilder.desc(stringJoin.get(next)));
			}
		}

		createSort(theBuilder, theFrom, theSort.getChain(), theOrders, thePredicates);
	}

	private String determineSystemIfMissing(String theParamName, String code, String system) {
		if (system == null) {
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
					List<VersionIndependentConcept> candidateCodes = myTerminologySvc.expandValueSet(valueSetUris.iterator().next());
					for (VersionIndependentConcept nextCandidate : candidateCodes) {
						if (nextCandidate.getCode().equals(code)) {
							system = nextCandidate.getSystem();
							break;
						}
					}
				}
			}
		}
		return system;
	}

	public Set<Long> doGetPids() {
		if (myParams.isPersistResults()) {
			HashSet<Long> retVal = new HashSet<Long>();

			for (SearchResult next : mySearchResultDao.findWithSearchUuid(mySearchEntity)) {
				retVal.add(next.getResourcePid());
			}
			return retVal;

		} else {
			return new HashSet<Long>(myPids);
		}
	}

	private boolean doHaveNoResults() {
		if (myParams.isPersistResults()) {
			return mySearchEntity.getTotalCount() == 0;
		} else {
			return myPids != null && myPids.isEmpty();
		}
	}

	private void doInitializeSearch() {
		if (mySearchEntity == null) {
			reinitializeSearch();
		}
	}

	private IBundleProvider doReturnProvider() {
		if (myParams.isPersistResults()) {
			return new PersistedJpaBundleProvider(mySearchEntity.getUuid(), myCallingDao);
		} else {
			if (myPids == null) {
				return new SimpleBundleProvider();
			} else {
				return new BundleProviderInMemory(myPids);
			}
		}
	}

	private void doSetPids(Collection<Long> thePids) {
		if (myParams.isPersistResults()) {
			if (mySearchEntity.getTotalCount() != null) {
				reinitializeSearch();
			}

			LinkedHashSet<SearchResult> results = new LinkedHashSet<SearchResult>();
			int index = 0;
			for (Long next : thePids) {
				SearchResult nextResult = new SearchResult(mySearchEntity);
				nextResult.setResourcePid(next);
				nextResult.setOrder(index);
				results.add(nextResult);
				index++;
			}
			mySearchResultDao.save(results);

			mySearchEntity.setTotalCount(results.size());
			mySearchEntity = myEntityManager.merge(mySearchEntity);

			myEntityManager.flush();

		} else {
			myPids = thePids;
		}
	}

	private void filterResourceIdsByLastUpdated(final DateRangeParam theLastUpdated) {
		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceTable> from = cq.from(ResourceTable.class);
		cq.select(from.get("myId").as(Long.class));

		List<Predicate> lastUpdatedPredicates = createLastUpdatedPredicates(theLastUpdated, builder, from);
		createPredicateResourceId(builder, cq, lastUpdatedPredicates, from.get("myId").as(Long.class));

		cq.where(SearchBuilder.toArray(lastUpdatedPredicates));
		TypedQuery<Long> query = myEntityManager.createQuery(cq);

		List<Long> resultList = query.getResultList();
		doSetPids(resultList);
	}

	private void loadResourcesByPid(Collection<Long> theIncludePids, List<IBaseResource> theResourceListToPopulate, Set<Long> theRevIncludedPids, boolean theForHistoryOperation) {
		EntityManager entityManager = myEntityManager;
		FhirContext context = myContext;
		BaseHapiFhirDao<?> dao = myCallingDao;

		loadResourcesByPid(theIncludePids, theResourceListToPopulate, theRevIncludedPids, theForHistoryOperation, entityManager, context, dao);
	}

	private void processSort(final SearchParameterMap theParams) {

		// Set<Long> loadPids = theLoadPids;
		if (theParams.getSort() != null && isNotBlank(theParams.getSort().getParamName())) {
			List<Order> orders = new ArrayList<Order>();
			List<Predicate> predicates = new ArrayList<Predicate>();
			CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
			CriteriaQuery<Tuple> cq = builder.createTupleQuery();
			Root<ResourceTable> from = cq.from(ResourceTable.class);

			createPredicateResourceId(builder, cq, predicates, from.get("myId").as(Long.class));

			createSort(builder, from, theParams.getSort(), orders, predicates);

			if (orders.size() > 0) {

				// TODO: why do we need the existing list for this join to work?
				Collection<Long> originalPids = doGetPids();

				LinkedHashSet<Long> loadPids = new LinkedHashSet<Long>();
				cq.multiselect(from.get("myId").as(Long.class));
				cq.where(toArray(predicates));
				cq.orderBy(orders);

				TypedQuery<Tuple> query = myEntityManager.createQuery(cq);

				for (Tuple next : query.getResultList()) {
					loadPids.add(next.get(0, Long.class));
				}

				ourLog.debug("Sort PID order is now: {}", loadPids);

				ArrayList<Long> pids = new ArrayList<Long>(loadPids);

				// Any ressources which weren't matched by the sort get added to the bottom
				for (Long next : originalPids) {
					if (loadPids.contains(next) == false) {
						pids.add(next);
					}
				}

				doSetPids(pids);
			}
		}

	}

	private void reinitializeSearch() {
		mySearchEntity = new Search();
		mySearchEntity.setUuid(UUID.randomUUID().toString());
		mySearchEntity.setCreated(new Date());
		mySearchEntity.setTotalCount(-1);
		mySearchEntity.setPreferredPageSize(myParams.getCount());
		mySearchEntity.setSearchType(myParams.getEverythingMode() != null ? SearchTypeEnum.EVERYTHING : SearchTypeEnum.SEARCH);
		mySearchEntity.setLastUpdated(myParams.getLastUpdated());

		for (Include next : myParams.getIncludes()) {
			mySearchEntity.getIncludes().add(new SearchInclude(mySearchEntity, next.getValue(), false, next.isRecurse()));
		}
		for (Include next : myParams.getRevIncludes()) {
			mySearchEntity.getIncludes().add(new SearchInclude(mySearchEntity, next.getValue(), true, next.isRecurse()));
		}

		if (myParams.isPersistResults()) {
			myEntityManager.persist(mySearchEntity);
			for (SearchInclude next : mySearchEntity.getIncludes()) {
				myEntityManager.persist(next);
			}
		}
	}

	public IBundleProvider search(final SearchParameterMap theParams) {
		myParams = theParams;
		StopWatch w = new StopWatch();

		doInitializeSearch();

		DateRangeParam lu = theParams.getLastUpdated();

		// Collection<Long> loadPids;
		if (theParams.getEverythingMode() != null) {

			Long pid = null;
			if (theParams.get(BaseResource.SP_RES_ID) != null) {
				StringParam idParm = (StringParam) theParams.get(BaseResource.SP_RES_ID).get(0).get(0);
				pid = BaseHapiFhirDao.translateForcedIdToPid(myResourceName, idParm.getValue(), myForcedIdDao);
			}

			if (theParams.containsKey(Constants.PARAM_CONTENT) || theParams.containsKey(Constants.PARAM_TEXT)) {
				List<Long> pids = mySearchDao.everything(myResourceName, theParams);
				if (pids.isEmpty()) {
					return doReturnProvider();
				}

				doSetPids(pids);

			} else {
				CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
				CriteriaQuery<Tuple> cq = builder.createTupleQuery();
				Root<ResourceTable> from = cq.from(ResourceTable.class);
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (pid != null) {
					predicates.add(builder.equal(from.get("myId"), pid));
				}
				predicates.add(builder.equal(from.get("myResourceType"), myResourceName));
				predicates.add(builder.isNull(from.get("myDeleted")));
				cq.where(builder.and(SearchBuilder.toArray(predicates)));

				Join<Object, Object> join = from.join("myIncomingResourceLinks", JoinType.LEFT);
				cq.multiselect(from.get("myId").as(Long.class), join.get("mySourceResourcePid").as(Long.class));

				TypedQuery<Tuple> query = myEntityManager.createQuery(cq);
				Set<Long> pids = new HashSet<Long>();
				for (Tuple next : query.getResultList()) {
					pids.add(next.get(0, Long.class));
					Long nextLong = next.get(1, Long.class);
					if (nextLong != null) {
						pids.add(nextLong);
					}
				}
				doSetPids(pids);

			}

		} else if (theParams.isEmpty()) {

			TypedQuery<Long> query = createSearchAllByTypeQuery(lu);
			doSetPids(query.getResultList());

		} else {

			if (mySearchDao == null) {
				if (theParams.containsKey(Constants.PARAM_TEXT)) {
					throw new InvalidRequestException("Fulltext search is not enabled on this service, can not process parameter: " + Constants.PARAM_TEXT);
				} else if (theParams.containsKey(Constants.PARAM_CONTENT)) {
					throw new InvalidRequestException("Fulltext search is not enabled on this service, can not process parameter: " + Constants.PARAM_CONTENT);
				}
			} else {
				List<Long> searchResultPids = mySearchDao.search(myResourceName, theParams);
				if (searchResultPids != null) {
					if (searchResultPids.isEmpty()) {
						return doReturnProvider();
					}
					doSetPids(searchResultPids);
				}
			}

			if (!theParams.isEmpty()) {
				searchForIdsWithAndOr(theParams, lu);
			}

		}

		// // Load _include and _revinclude before filter and sort in everything mode
		// if (theParams.getEverythingMode() != null) {
		// if (theParams.getRevIncludes() != null && theParams.getRevIncludes().isEmpty() == false) {
		// loadPids.addAll(loadReverseIncludes(loadPids, theParams.getRevIncludes(), true,
		// theParams.getEverythingMode()));
		// loadPids.addAll(loadReverseIncludes(loadPids, theParams.getIncludes(), false, theParams.getEverythingMode()));
		// }
		// }

		if (doHaveNoResults()) {
			return doReturnProvider();
		}

		// Handle _lastUpdated
		if (lu != null) {
			filterResourceIdsByLastUpdated(lu);

			if (doHaveNoResults()) {
				return doReturnProvider();
			}
		}

		// Handle sorting if any was provided
		processSort(theParams);

		ourLog.info(" {} on {} in {}ms", new Object[] { myResourceName, theParams, w.getMillisAndRestart() });
		return doReturnProvider();
	}

	private void searchForIdsWithAndOr(SearchParameterMap theParams, DateRangeParam theLastUpdated) {
		SearchParameterMap params = theParams;
		if (params == null) {
			params = new SearchParameterMap();
		}
		myParams = theParams;

		doInitializeSearch();

//		RuntimeResourceDefinition resourceDef = myContext.getResourceDefinition(myResourceType);

		for (Entry<String, List<List<? extends IQueryParameterType>>> nextParamEntry : params.entrySet()) {
			String nextParamName = nextParamEntry.getKey();
			if (nextParamName.equals(BaseResource.SP_RES_ID)) {

				if (nextParamEntry.getValue().isEmpty()) {
					continue;
				} else {
					for (List<? extends IQueryParameterType> nextValue : nextParamEntry.getValue()) {
						Set<Long> joinPids = new HashSet<Long>();
						if (nextValue == null || nextValue.size() == 0) {
							continue;
						} else {
							for (IQueryParameterType next : nextValue) {
								String value = next.getValueAsQueryToken(myContext);
								IIdType valueId = new IdDt(value);

								try {
									BaseHasResource entity = myCallingDao.readEntity(valueId);
									if (entity.getDeleted() != null) {
										continue;
									}
									joinPids.add(entity.getId());
								} catch (ResourceNotFoundException e) {
									// This isn't an error, just means no result found
								}
							}
							if (joinPids.isEmpty()) {
								doSetPids(new HashSet<Long>());
								return;
							}
						}

						addPredicateId(joinPids);
						if (doHaveNoResults()) {
							return;
						}
					}
				}

			} else if (nextParamName.equals(BaseResource.SP_RES_LANGUAGE)) {

				addPredicateLanguage(nextParamEntry.getValue());

			} else if (nextParamName.equals(Constants.PARAM_HAS)) {

				addPredicateHas(nextParamEntry.getValue(), theLastUpdated);

			} else if (nextParamName.equals(Constants.PARAM_TAG) || nextParamName.equals(Constants.PARAM_PROFILE) || nextParamName.equals(Constants.PARAM_SECURITY)) {

				addPredicateTag(nextParamEntry.getValue(), nextParamName, theLastUpdated);

			} else {

				RuntimeSearchParam nextParamDef = mySearchParamRegistry.getActiveSearchParam(myResourceName, nextParamName);
				if (nextParamDef != null) {
					switch (nextParamDef.getParamType()) {
					case DATE:
						for (List<? extends IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
							addPredicateDate(nextParamName, nextAnd);
							if (doHaveNoResults()) {
								return;
							}
						}
						break;
					case QUANTITY:
						for (List<? extends IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
							addPredicateQuantity(nextParamName, nextAnd);
							if (doHaveNoResults()) {
								return;
							}
						}
						break;
					case REFERENCE:
						for (List<? extends IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
							addPredicateReference(nextParamName, nextAnd);
							if (doHaveNoResults()) {
								return;
							}
						}
						break;
					case STRING:
						for (List<? extends IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
							addPredicateString(nextParamName, nextAnd);
							if (doHaveNoResults()) {
								return;
							}
						}
						break;
					case TOKEN:
						for (List<? extends IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
							addPredicateToken(nextParamName, nextAnd);
							if (doHaveNoResults()) {
								return;
							}
						}
						break;
					case NUMBER:
						for (List<? extends IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
							addPredicateNumber(nextParamName, nextAnd);
							if (doHaveNoResults()) {
								return;
							}
						}
						break;
					case COMPOSITE:
						for (List<? extends IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
							addPredicateComposite(nextParamDef, nextAnd);
							if (doHaveNoResults()) {
								return;
							}
						}
						break;
					case URI:
						for (List<? extends IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
							addPredicateUri(nextParamName, nextAnd);
							if (doHaveNoResults()) {
								return;
							}
						}
						break;
					case HAS: 
						// should not happen
						break;
					}
				}
			} 

			if (doHaveNoResults()) {
				return;
			}

		}

	}
	
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
		List<Predicate> lastUpdatedPredicates = new ArrayList<Predicate>();
		if (theLastUpdated != null) {
			if (theLastUpdated.getLowerBoundAsInstant() != null) {
				ourLog.info("LastUpdated lower bound: {}", new InstantDt(theLastUpdated.getLowerBoundAsInstant()));
				Predicate predicateLower = builder.greaterThanOrEqualTo(from.<Date> get("myUpdated"), theLastUpdated.getLowerBoundAsInstant());
				lastUpdatedPredicates.add(predicateLower);
			}
			if (theLastUpdated.getUpperBoundAsInstant() != null) {
				Predicate predicateUpper = builder.lessThanOrEqualTo(from.<Date> get("myUpdated"), theLastUpdated.getUpperBoundAsInstant());
				lastUpdatedPredicates.add(predicateUpper);
			}
		}
		return lastUpdatedPredicates;
	}

	private static String createLeftMatchLikeExpression(String likeExpression) {
		return likeExpression.replace("%", "[%]") + "%";
	}

	private static Predicate createResourceLinkPathPredicate(IDao theCallingDao, FhirContext theContext, String theParamName, Root<? extends ResourceLink> from, Class<? extends IBaseResource> resourceType) {
		RuntimeResourceDefinition resourceDef = theContext.getResourceDefinition(resourceType);
		RuntimeSearchParam param = theCallingDao.getSearchParamByName(resourceDef, theParamName);
		List<String> path = param.getPathsSplit();
		Predicate type = from.get("mySourcePath").in(path);
		return type;
	}

	private static List<Long> filterResourceIdsByLastUpdated(EntityManager theEntityManager, final DateRangeParam theLastUpdated, Collection<Long> thePids) {
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

	public static void loadResourcesByPid(Collection<Long> theIncludePids, List<IBaseResource> theResourceListToPopulate, Set<Long> theRevIncludedPids, boolean theForHistoryOperation, EntityManager entityManager, FhirContext context, IDao theDao) {
		if (theIncludePids.isEmpty()) {
			return;
		}

		Map<Long, Integer> position = new HashMap<Long, Integer>();
		for (Long next : theIncludePids) {
			position.put(next, theResourceListToPopulate.size());
			theResourceListToPopulate.add(null);
		}

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ResourceTable> cq = builder.createQuery(ResourceTable.class);
		Root<ResourceTable> from = cq.from(ResourceTable.class);
		cq.where(from.get("myId").in(theIncludePids));
		TypedQuery<ResourceTable> q = entityManager.createQuery(cq);

		for (ResourceTable next : q.getResultList()) {
			Class<? extends IBaseResource> resourceType = context.getResourceDefinition(next.getResourceType()).getImplementingClass();
			IBaseResource resource = (IBaseResource) theDao.toResource(resourceType, next, theForHistoryOperation);
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

	/**
	 * THIS SHOULD RETURN HASHSET and not jsut Set because we add to it later (so it can't be Collections.emptySet())
	 * 
	 * @param theLastUpdated
	 */
	public static HashSet<Long> loadReverseIncludes(IDao theCallingDao, FhirContext theContext, EntityManager theEntityManager, Collection<Long> theMatches, Set<Include> theRevIncludes, boolean theReverseMode, DateRangeParam theLastUpdated) {
		if (theMatches.size() == 0) {
			return new HashSet<Long>();
		}
		if (theRevIncludes == null || theRevIncludes.isEmpty()) {
			return new HashSet<Long>();
		}
		String searchFieldName = theReverseMode ? "myTargetResourcePid" : "mySourceResourcePid";

		Collection<Long> nextRoundMatches = theMatches;
		HashSet<Long> allAdded = new HashSet<Long>();
		HashSet<Long> original = new HashSet<Long>(theMatches);
		ArrayList<Include> includes = new ArrayList<Include>(theRevIncludes);

		int roundCounts = 0;
		StopWatch w = new StopWatch();

		boolean addedSomeThisRound;
		do {
			roundCounts++;

			HashSet<Long> pidsToInclude = new HashSet<Long>();
			Set<Long> nextRoundOmit = new HashSet<Long>();

			for (Iterator<Include> iter = includes.iterator(); iter.hasNext();) {
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
							// if (theEverythingModeEnum.isEncounter()) {
							// if (resourceLink.getSourcePath().equals("Encounter.subject") ||
							// resourceLink.getSourcePath().equals("Encounter.patient")) {
							// nextRoundOmit.add(resourceLink.getSourceResourcePid());
							// }
							// }
							pidsToInclude.add(resourceLink.getSourceResourcePid());
						} else {
							pidsToInclude.add(resourceLink.getTargetResourcePid());
						}
					}
				} else {

					List<String> paths;
					RuntimeSearchParam param = null;
					if (theContext.getVersion().getVersion() == FhirVersionEnum.DSTU1) {
						paths = Collections.singletonList(nextInclude.getValue());
					} else {
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
					}

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
				pidsToInclude = new HashSet<Long>(filterResourceIdsByLastUpdated(theEntityManager, theLastUpdated, pidsToInclude));
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

		ourLog.info("Loaded {} {} in {} rounds and {} ms", new Object[] { allAdded.size(), theReverseMode ? "_revincludes" : "_includes", roundCounts, w.getMillisAndRestart() });

		return allAdded;
	}

	static Predicate[] toArray(List<Predicate> thePredicates) {
		return thePredicates.toArray(new Predicate[thePredicates.size()]);
	}

	private final class BundleProviderInMemory implements IBundleProvider {
		private final ArrayList<Long> myPids;

		private BundleProviderInMemory(Collection<Long> thePids) {
			final ArrayList<Long> pids;
			if (!(thePids instanceof List)) {
				pids = new ArrayList<Long>(thePids);
			} else {
				pids = (ArrayList<Long>) thePids;
			}
			myPids = pids;
		}

		@Override
		public InstantDt getPublished() {
			return new InstantDt(mySearchEntity.getCreated());
		}

		@Override
		public List<IBaseResource> getResources(final int theFromIndex, final int theToIndex) {
			TransactionTemplate template = new TransactionTemplate(myPlatformTransactionManager);
			return template.execute(new TransactionCallback<List<IBaseResource>>() {
				@Override
				public List<IBaseResource> doInTransaction(TransactionStatus theStatus) {
					List<Long> pidsSubList = myPids.subList(theFromIndex, theToIndex);

					// Load includes
					pidsSubList = new ArrayList<Long>(pidsSubList);

					Set<Long> revIncludedPids = new HashSet<Long>();
					if (myParams.getEverythingMode() == null) {
						revIncludedPids.addAll(loadReverseIncludes(myCallingDao, myContext, myEntityManager, pidsSubList, myParams.getRevIncludes(), true, myParams.getLastUpdated()));
					}
					revIncludedPids.addAll(loadReverseIncludes(myCallingDao, myContext, myEntityManager, pidsSubList, myParams.getIncludes(), false, myParams.getLastUpdated()));

					// Execute the query and make sure we return distinct results
					List<IBaseResource> resources = new ArrayList<IBaseResource>();
					loadResourcesByPid(pidsSubList, resources, revIncludedPids, false);

					return resources;
				}

			});
		}

		@Override
		public Integer preferredPageSize() {
			return myParams.getCount();
		}

		@Override
		public int size() {
			return myPids.size();
		}

		@Override
		public String getUuid() {
			return null;
		}
	}

}
