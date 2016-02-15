package ca.uhn.fhir.jpa.dao;

import static org.apache.commons.lang3.StringUtils.defaultString;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeChildResourceDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamUriDao;
import ca.uhn.fhir.jpa.dao.data.ISearchResultDao;
import ca.uhn.fhir.jpa.entity.BaseHasResource;
import ca.uhn.fhir.jpa.entity.BaseResourceIndexedSearchParam;
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
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.base.composite.BaseIdentifierDt;
import ca.uhn.fhir.model.base.composite.BaseQuantityDt;
import ca.uhn.fhir.model.dstu.resource.BaseResource;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.method.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.CompositeParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.param.UriParamQualifierEnum;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public class SearchBuilder {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchBuilder.class);

	private BaseHapiFhirDao<?> myCallingDao;
	private FhirContext myContext;
	private EntityManager myEntityManager;
	private PlatformTransactionManager myPlatformTransactionManager;
	private String myResourceName;
	private Class<? extends IBaseResource> myResourceType;
	private ISearchDao mySearchDao;
	private ISearchResultDao mySearchResultDao;
	private IResourceIndexedSearchParamUriDao myResourceIndexedSearchParamUriDao;

	public SearchBuilder(FhirContext theFhirContext, EntityManager theEntityManager, PlatformTransactionManager thePlatformTransactionManager, ISearchDao theSearchDao, ISearchResultDao theSearchResultDao, BaseHapiFhirDao theDao, IResourceIndexedSearchParamUriDao theResourceIndexedSearchParamUriDao) {
		myContext = theFhirContext;
		myEntityManager = theEntityManager;
		myPlatformTransactionManager = thePlatformTransactionManager;
		mySearchDao = theSearchDao;
		mySearchResultDao = theSearchResultDao;
		myCallingDao = theDao;
		myResourceIndexedSearchParamUriDao = theResourceIndexedSearchParamUriDao;
	}

	private Set<Long> addPredicateComposite(RuntimeSearchParam theParamDef, Set<Long> thePids, List<? extends IQueryParameterType> theNextAnd) {
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

		RuntimeSearchParam left = theParamDef.getCompositeOf().get(0);
		IQueryParameterType leftValue = cp.getLeftValue();
		Predicate leftPredicate = createCompositeParamPart(builder, from, left, leftValue);

		RuntimeSearchParam right = theParamDef.getCompositeOf().get(1);
		IQueryParameterType rightValue = cp.getRightValue();
		Predicate rightPredicate = createCompositeParamPart(builder, from, right, rightValue);

		Predicate type = builder.equal(from.get("myResourceType"), myResourceName);
		if (thePids.size() > 0) {
			Predicate inPids = (from.get("myResourcePid").in(thePids));
			cq.where(builder.and(type, leftPredicate, rightPredicate, inPids));
		} else {
			cq.where(builder.and(type, leftPredicate, rightPredicate));
		}

		TypedQuery<Long> q = myEntityManager.createQuery(cq);
		return new HashSet<Long>(q.getResultList());

	}

	private Set<Long> addPredicateDate(String theParamName, Set<Long> thePids, List<? extends IQueryParameterType> theList) {
		if (theList == null || theList.isEmpty()) {
			return thePids;
		}

		if (Boolean.TRUE.equals(theList.get(0).getMissing())) {
			return addPredicateParamMissing(thePids, "myParamsDate", theParamName, ResourceIndexedSearchParamDate.class);
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

		Predicate type = builder.equal(from.get("myResourceType"), myResourceName);
		Predicate name = builder.equal(from.get("myParamName"), theParamName);
		if (thePids.size() > 0) {
			Predicate inPids = (from.get("myResourcePid").in(thePids));
			cq.where(builder.and(type, name, masterCodePredicate, inPids));
		} else {
			cq.where(builder.and(type, name, masterCodePredicate));
		}

		TypedQuery<Long> q = myEntityManager.createQuery(cq);
		return new HashSet<Long>(q.getResultList());
	}

	private Set<Long> addPredicateId(Set<Long> theExistingPids, Set<Long> thePids, DateRangeParam theLastUpdated) {
		if (thePids == null || thePids.isEmpty()) {
			return Collections.emptySet();
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceTable> from = cq.from(ResourceTable.class);
		cq.select(from.get("myId").as(Long.class));

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(builder.equal(from.get("myResourceType"), myResourceName));
		predicates.add(from.get("myId").in(thePids));
		predicates.addAll(createLastUpdatedPredicates(theLastUpdated, builder, from));

		cq.where(toArray(predicates));

		TypedQuery<Long> q = myEntityManager.createQuery(cq);
		HashSet<Long> found = new HashSet<Long>(q.getResultList());
		if (!theExistingPids.isEmpty()) {
			theExistingPids.retainAll(found);
			return theExistingPids;
		} else {
			return found;
		}
	}

	private Set<Long> addPredicateLanguage(Set<Long> thePids, List<List<? extends IQueryParameterType>> theList, DateRangeParam theLastUpdated) {
		Set<Long> retVal = thePids;
		if (theList == null || theList.isEmpty()) {
			return retVal;
		}
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
				return retVal;
			}

			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(builder.equal(from.get("myResourceType"), myResourceName));
			predicates.add(from.get("myLanguage").as(String.class).in(values));

			if (retVal.size() > 0) {
				Predicate inPids = (from.get("myId").in(retVal));
				predicates.add(inPids);
			}

			predicates.add(builder.isNull(from.get("myDeleted")));

			cq.where(toArray(predicates));

			TypedQuery<Long> q = myEntityManager.createQuery(cq);
			retVal = new HashSet<Long>(q.getResultList());
			if (retVal.isEmpty()) {
				return retVal;
			}
		}

		return retVal;
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
			Predicate name = createResourceLinkPathPredicate(theParamName, theBuilder, from);
			codePredicates.add(theBuilder.and(name, singleCode));
			missingFalse = true;
		}
		return missingFalse;
	}

	private Set<Long> addPredicateNumber(String theParamName, Set<Long> thePids, List<? extends IQueryParameterType> theList) {
		if (theList == null || theList.isEmpty()) {
			return thePids;
		}

		if (Boolean.TRUE.equals(theList.get(0).getMissing())) {
			return addPredicateParamMissing(thePids, "myParamsNumber", theParamName, ResourceIndexedSearchParamNumber.class);
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
					return thePids;
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

		Predicate masterCodePredicate = builder.or(toArray(codePredicates));

		Predicate type = builder.equal(from.get("myResourceType"), myResourceName);
		Predicate name = builder.equal(from.get("myParamName"), theParamName);
		if (thePids.size() > 0) {
			Predicate inPids = (from.get("myResourcePid").in(thePids));
			cq.where(builder.and(type, name, masterCodePredicate, inPids));
		} else {
			cq.where(builder.and(type, name, masterCodePredicate));
		}

		TypedQuery<Long> q = myEntityManager.createQuery(cq);
		return new HashSet<Long>(q.getResultList());
	}

	private Set<Long> addPredicateParamMissing(Set<Long> thePids, String joinName, String theParamName, Class<? extends BaseResourceIndexedSearchParam> theParamTable) {
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

		Predicate joinPredicate = builder.not(builder.in(from.get("myId")).value(subQ));
		Predicate typePredicate = builder.equal(from.get("myResourceType"), myResourceName);
		Predicate notDeletedPredicate = builder.isNull(from.get("myDeleted"));

		if (thePids.size() > 0) {
			Predicate inPids = (from.get("myId").in(thePids));
			cq.where(builder.and(inPids, typePredicate, joinPredicate, notDeletedPredicate));
		} else {
			cq.where(builder.and(typePredicate, joinPredicate, notDeletedPredicate));
		}

		ourLog.info("Adding :missing qualifier for parameter '{}'", theParamName);

		TypedQuery<Long> q = myEntityManager.createQuery(cq);
		List<Long> resultList = q.getResultList();
		HashSet<Long> retVal = new HashSet<Long>(resultList);
		return retVal;
	}

	private Set<Long> addPredicateParamMissingResourceLink(Set<Long> thePids, String joinName, String theParamName) {
		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceTable> from = cq.from(ResourceTable.class);
		cq.select(from.get("myId").as(Long.class));

		Subquery<Long> subQ = cq.subquery(Long.class);
		Root<ResourceLink> subQfrom = subQ.from(ResourceLink.class);
		subQ.select(subQfrom.get("mySourceResourcePid").as(Long.class));

		// subQ.where(builder.equal(subQfrom.get("myParamName"), theParamName));
		Predicate path = createResourceLinkPathPredicate(theParamName, builder, subQfrom);
		subQ.where(path);

		Predicate joinPredicate = builder.not(builder.in(from.get("myId")).value(subQ));
		Predicate typePredicate = builder.equal(from.get("myResourceType"), myResourceName);

		if (thePids.size() > 0) {
			Predicate inPids = (from.get("myId").in(thePids));
			cq.where(builder.and(inPids, typePredicate, joinPredicate));
		} else {
			cq.where(builder.and(typePredicate, joinPredicate));
		}

		TypedQuery<Long> q = myEntityManager.createQuery(cq);
		List<Long> resultList = q.getResultList();
		HashSet<Long> retVal = new HashSet<Long>(resultList);
		return retVal;
	}

	private Set<Long> addPredicateQuantity(String theParamName, Set<Long> thePids, List<? extends IQueryParameterType> theList) {
		if (theList == null || theList.isEmpty()) {
			return thePids;
		}

		if (Boolean.TRUE.equals(theList.get(0).getMissing())) {
			return addPredicateParamMissing(thePids, "myParamsQuantity", theParamName, ResourceIndexedSearchParamQuantity.class);
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceIndexedSearchParamQuantity> from = cq.from(ResourceIndexedSearchParamQuantity.class);
		cq.select(from.get("myResourcePid").as(Long.class));

		List<Predicate> codePredicates = new ArrayList<Predicate>();
		for (IQueryParameterType nextOr : theList) {
			IQueryParameterType params = nextOr;

			if (addPredicateMissingFalseIfPresent(builder, theParamName, from, codePredicates, nextOr)) {
				continue;
			}

			String systemValue;
			String unitsValue;
			ParamPrefixEnum cmpValue;
			BigDecimal valueValue;
			String valueString;

			if (params instanceof BaseQuantityDt) {
				BaseQuantityDt param = (BaseQuantityDt) params;
				systemValue = param.getSystemElement().getValueAsString();
				unitsValue = param.getUnitsElement().getValueAsString();
				cmpValue = ParamPrefixEnum.forDstu1Value(param.getComparatorElement().getValueAsString());
				valueValue = param.getValueElement().getValue();
				valueString = param.getValueElement().getValueAsString();
			} else if (params instanceof QuantityParam) {
				QuantityParam param = (QuantityParam) params;
				systemValue = param.getSystem();
				unitsValue = param.getUnits();
				cmpValue = param.getPrefix();
				valueValue = param.getValue();
				valueString = param.getValueAsString();
			} else {
				throw new IllegalArgumentException("Invalid quantity type: " + params.getClass());
			}

			Predicate system = null;
			if (!isBlank(systemValue)) {
				system = builder.equal(from.get("mySystem"), systemValue);
			}

			Predicate code = null;
			if (!isBlank(unitsValue)) {
				code = builder.equal(from.get("myUnits"), unitsValue);
			}

			cmpValue = ObjectUtils.defaultIfNull(cmpValue, ParamPrefixEnum.EQUAL);
			final Expression<BigDecimal> path = from.get("myValue");
			String invalidMessageName = "invalidQuantityPrefix";

			Predicate num = createPredicateNumeric(builder, params, cmpValue, valueValue, path, invalidMessageName, valueString);

			if (system == null && code == null) {
				codePredicates.add(num);
			} else if (system == null) {
				Predicate singleCode = builder.and(code, num);
				codePredicates.add(singleCode);
			} else if (code == null) {
				Predicate singleCode = builder.and(system, num);
				codePredicates.add(singleCode);
			} else {
				Predicate singleCode = builder.and(system, code, num);
				codePredicates.add(singleCode);
			}
		}

		Predicate masterCodePredicate = builder.or(toArray(codePredicates));

		Predicate type = builder.equal(from.get("myResourceType"), myResourceName);
		Predicate name = builder.equal(from.get("myParamName"), theParamName);
		if (thePids.size() > 0) {
			Predicate inPids = (from.get("myResourcePid").in(thePids));
			cq.where(builder.and(type, name, masterCodePredicate, inPids));
		} else {
			cq.where(builder.and(type, name, masterCodePredicate));
		}

		TypedQuery<Long> q = myEntityManager.createQuery(cq);
		return new HashSet<Long>(q.getResultList());
	}

	private Set<Long> addPredicateReference(String theParamName, Set<Long> thePids, List<? extends IQueryParameterType> theList) {
		assert theParamName.contains(".") == false;

		Set<Long> pidsToRetain = thePids;
		if (theList == null || theList.isEmpty()) {
			return pidsToRetain;
		}

		if (Boolean.TRUE.equals(theList.get(0).getMissing())) {
			return addPredicateParamMissingResourceLink(thePids, "myResourceLinks", theParamName);
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
					String resourceId = ref.getValueAsQueryToken(myContext);
					if (resourceId.contains("/")) {
						IIdType dt = new IdDt(resourceId);
						resourceId = dt.getIdPart();
					}
					Long targetPid = myCallingDao.translateForcedIdToPid(new IdDt(resourceId));
					ourLog.debug("Searching for resource link with target PID: {}", targetPid);
					Predicate eq = builder.equal(from.get("myTargetResourcePid"), targetPid);

					codePredicates.add(eq);

				} else {

					String paramPath = myContext.getResourceDefinition(myResourceType).getSearchParam(theParamName).getPath();
					BaseRuntimeChildDefinition def = myContext.newTerser().getDefinition(myResourceType, paramPath);
					if (!(def instanceof RuntimeChildResourceDefinition)) {
						throw new ConfigurationException("Property " + paramPath + " of type " + myResourceName + " is not a resource: " + def.getClass());
					}
					List<Class<? extends IBaseResource>> resourceTypes;

					String resourceId;
					if (!ref.getValue().matches("[a-zA-Z]+\\/.*")) {
						RuntimeChildResourceDefinition resDef = (RuntimeChildResourceDefinition) def;
						resourceTypes = resDef.getResourceTypes();
						resourceId = ref.getValue();
					} else {
						resourceTypes = new ArrayList<Class<? extends IBaseResource>>();
						RuntimeResourceDefinition resDef = myContext.getResourceDefinition(ref.getResourceType());
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
							param = typeDef.getSearchParam(chain);
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
							chainValue.setValueAsQueryToken(qualifier, resourceId);
							((ReferenceParam) chainValue).setChain(remainingChain);
						} else if (isMeta) {
							IQueryParameterType type = BaseHapiFhirDao.newInstanceType(chain);
							type.setValueAsQueryToken(qualifier, resourceId);
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

		Predicate masterCodePredicate = builder.or(toArray(codePredicates));

		Predicate type = createResourceLinkPathPredicate(theParamName, builder, from);
		if (pidsToRetain.size() > 0) {
			Predicate inPids = (from.get("mySourceResourcePid").in(pidsToRetain));
			cq.where(builder.and(type, masterCodePredicate, inPids));
		} else {
			cq.where(builder.and(type, masterCodePredicate));
		}

		TypedQuery<Long> q = myEntityManager.createQuery(cq);
		return new HashSet<Long>(q.getResultList());
	}

	private Set<Long> addPredicateString(String theParamName, Set<Long> thePids, List<? extends IQueryParameterType> theList) {
		if (theList == null || theList.isEmpty()) {
			return thePids;
		}

		if (Boolean.TRUE.equals(theList.get(0).getMissing())) {
			return addPredicateParamMissing(thePids, "myParamsString", theParamName, ResourceIndexedSearchParamString.class);
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

		Predicate masterCodePredicate = builder.or(toArray(codePredicates));

		Predicate type = builder.equal(from.get("myResourceType"), myResourceName);
		Predicate name = builder.equal(from.get("myParamName"), theParamName);
		if (thePids.size() > 0) {
			Predicate inPids = (from.get("myResourcePid").in(thePids));
			cq.where(builder.and(type, name, masterCodePredicate, inPids));
		} else {
			cq.where(builder.and(type, name, masterCodePredicate));
		}

		TypedQuery<Long> q = myEntityManager.createQuery(cq);
		return new HashSet<Long>(q.getResultList());
	}

	private Set<Long> addPredicateTag(Set<Long> thePids, List<List<? extends IQueryParameterType>> theList, String theParamName, DateRangeParam theLastUpdated) {
		Set<Long> pids = thePids;
		if (theList == null || theList.isEmpty()) {
			return pids;
		}

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
			CriteriaQuery<Long> cq = builder.createQuery(Long.class);
			Root<ResourceTag> from = cq.from(ResourceTag.class);
			cq.select(from.get("myResourceId").as(Long.class));

			List<Predicate> andPredicates = new ArrayList<Predicate>();
			andPredicates.add(builder.equal(from.get("myResourceType"), myResourceName));

			List<Predicate> orPredicates = new ArrayList<Predicate>();
			for (IQueryParameterType nextOrParams : nextAndParams) {
				String code;
				String system;
				if (nextOrParams instanceof TokenParam) {
					TokenParam nextParam = (TokenParam) nextOrParams;
					code = nextParam.getValue();
					system = nextParam.getSystem();
				} else {
					UriParam nextParam = (UriParam) nextOrParams;
					code = nextParam.getValue();
					system = null;
				}
				From<ResourceTag, TagDefinition> defJoin = from.join("myTag");
				Predicate typePrediate = builder.equal(defJoin.get("myTagType"), tagType);
				Predicate codePrediate = builder.equal(defJoin.get("myCode"), code);
				if (isBlank(code)) {
					continue;
				}
				if (isNotBlank(system)) {
					Predicate systemPrediate = builder.equal(defJoin.get("mySystem"), system);
					orPredicates.add(builder.and(typePrediate, systemPrediate, codePrediate));
				} else {
					orPredicates.add(builder.and(typePrediate, codePrediate));
				}

			}
			if (orPredicates.isEmpty() == false) {
				andPredicates.add(builder.or(toArray(orPredicates)));
			}

			From<ResourceTag, ResourceTable> defJoin = from.join("myResource");
			Predicate notDeletedPredicatePrediate = builder.isNull(defJoin.get("myDeleted"));
			andPredicates.add(notDeletedPredicatePrediate);
			if (theLastUpdated != null) {
				andPredicates.addAll(createLastUpdatedPredicates(theLastUpdated, builder, defJoin));
			}

			Predicate masterCodePredicate = builder.and(toArray(andPredicates));

			if (pids.size() > 0) {
				Predicate inPids = (from.get("myResourceId").in(pids));
				cq.where(builder.and(masterCodePredicate, inPids));
			} else {
				cq.where(masterCodePredicate);
			}

			TypedQuery<Long> q = myEntityManager.createQuery(cq);
			pids = new HashSet<Long>(q.getResultList());
		}

		return pids;
	}

	private Set<Long> addPredicateToken(String theParamName, Set<Long> thePids, List<? extends IQueryParameterType> theList) {
		if (theList == null || theList.isEmpty()) {
			return thePids;
		}

		if (Boolean.TRUE.equals(theList.get(0).getMissing())) {
			return addPredicateParamMissing(thePids, "myParamsToken", theParamName, ResourceIndexedSearchParamToken.class);
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
					return addPredicateString(theParamName, thePids, theList);
				}
			}

			Predicate singleCode = createPredicateToken(nextOr, theParamName, builder, from);
			codePredicates.add(singleCode);
		}

		Predicate masterCodePredicate = builder.or(toArray(codePredicates));

		Predicate type = builder.equal(from.get("myResourceType"), myResourceName);
		Predicate name = builder.equal(from.get("myParamName"), theParamName);
		if (thePids.size() > 0) {
			Predicate inPids = (from.get("myResourcePid").in(thePids));
			cq.where(builder.and(type, name, masterCodePredicate, inPids));
		} else {
			cq.where(builder.and(type, name, masterCodePredicate));
		}

		TypedQuery<Long> q = myEntityManager.createQuery(cq);
		return new HashSet<Long>(q.getResultList());
	}

	private Set<Long> addPredicateUri(String theParamName, Set<Long> thePids, List<? extends IQueryParameterType> theList) {
		if (theList == null || theList.isEmpty()) {
			return thePids;
		}

		if (Boolean.TRUE.equals(theList.get(0).getMissing())) {
			return addPredicateParamMissing(thePids, "myParamsUri", theParamName, ResourceIndexedSearchParamUri.class);
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
					return thePids;
				}

				Path<Object> fromObj = from.get("myUri");
				Predicate predicate;
				if (param.getQualifier() == UriParamQualifierEnum.ABOVE) {

					/*
					 * :above is an inefficient query- It means that the user is supplying a more specific URL
					 * (say http://example.com/foo/bar/baz) and that we should match on any URLs that are
					 * less specific but otherwise the same. For example http://example.com and http://example.com/foo
					 * would both match.
					 * 
					 * We do this by querying the DB for all candidate URIs and then manually checking
					 * each one. This isn't very efficient, but this is also probably not a very common
					 * type of query to do.
					 * 
					 * If we ever need to make this more efficient, lucene could certainly be used
					 * as an optimization.
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
			return new HashSet<Long>();
		}

		Predicate masterCodePredicate = builder.or(toArray(codePredicates));

		Predicate type = builder.equal(from.get("myResourceType"), myResourceName);
		Predicate name = builder.equal(from.get("myParamName"), theParamName);
		if (thePids.size() > 0) {
			Predicate inPids = (from.get("myResourcePid").in(thePids));
			cq.where(builder.and(type, name, masterCodePredicate, inPids));
		} else {
			cq.where(builder.and(type, name, masterCodePredicate));
		}

		TypedQuery<Long> q = myEntityManager.createQuery(cq);
		return new HashSet<Long>(q.getResultList());
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
		}

		if (retVal == null) {
			throw new InvalidRequestException("Don't know how to handle composite parameter with type of " + left.getParamType());
		}

		return retVal;
	}

	private List<Predicate> createLastUpdatedPredicates(final DateRangeParam theLastUpdated, CriteriaBuilder builder, From<?, ResourceTable> from) {
		List<Predicate> lastUpdatedPredicates = new ArrayList<Predicate>();
		if (theLastUpdated != null) {
			if (theLastUpdated.getLowerBoundAsInstant() != null) {
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
			if (theRange.getLowerBound().getPrefix() == ParamPrefixEnum.STARTS_AFTER) {
				lb = gt;
			} else {
				lb = theBuilder.or(gt, lt);
			}
		}

		Predicate ub = null;
		if (upperBound != null) {
			Predicate gt = theBuilder.lessThanOrEqualTo(theFrom.<Date> get("myValueLow"), upperBound);
			Predicate lt = theBuilder.lessThanOrEqualTo(theFrom.<Date> get("myValueHigh"), upperBound);
			if (theRange.getUpperBound().getPrefix() == ParamPrefixEnum.ENDS_BEFORE) {
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

	// private Set<Long> addPredicateComposite(String theParamName, Set<Long> thePids, List<? extends
	// IQueryParameterType> theList) {
	// }

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

	private static String createLeftMatchLikeExpression(String likeExpression) {
		return likeExpression.replace("%", "[%]") + "%";
	}

	private Predicate createPredicateToken(IQueryParameterType theParameter, String theParamName, CriteriaBuilder theBuilder, From<ResourceIndexedSearchParamToken, ResourceIndexedSearchParamToken> theFrom) {
		String code;
		String system;
		if (theParameter instanceof TokenParam) {
			TokenParam id = (TokenParam) theParameter;
			system = id.getSystem();
			code = id.getValue();
		} else if (theParameter instanceof BaseIdentifierDt) {
			BaseIdentifierDt id = (BaseIdentifierDt) theParameter;
			system = id.getSystemElement().getValueAsString();
			code = id.getValueElement().getValue();
		} else if (theParameter instanceof BaseCodingDt) {
			BaseCodingDt id = (BaseCodingDt) theParameter;
			system = id.getSystemElement().getValueAsString();
			code = id.getCodeElement().getValue();
		} else {
			throw new IllegalArgumentException("Invalid token type: " + theParameter.getClass());
		}

		if (system != null && system.length() > ResourceIndexedSearchParamToken.MAX_LENGTH) {
			throw new InvalidRequestException("Parameter[" + theParamName + "] has system (" + system.length() + ") that is longer than maximum allowed (" + ResourceIndexedSearchParamToken.MAX_LENGTH + "): " + system);
		}
		if (code != null && code.length() > ResourceIndexedSearchParamToken.MAX_LENGTH) {
			throw new InvalidRequestException("Parameter[" + theParamName + "] has code (" + code.length() + ") that is longer than maximum allowed (" + ResourceIndexedSearchParamToken.MAX_LENGTH + "): " + code);
		}

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
			singleCodePredicates.add(theBuilder.isNull(theFrom.get("myValue")));
		}
		Predicate singleCode = theBuilder.and(toArray(singleCodePredicates));
		return singleCode;
	}

	private Predicate createResourceLinkPathPredicate(String theParamName, CriteriaBuilder builder, Root<? extends ResourceLink> from) {
		RuntimeSearchParam param = myContext.getResourceDefinition(myResourceType).getSearchParam(theParamName);
		List<String> path = param.getPathsSplit();
		Predicate type = from.get("mySourcePath").in(path);
		return type;
	}

	private TypedQuery<Tuple> createSearchAllByTypeQuery(DateRangeParam theLastUpdated) {
		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = builder.createTupleQuery();
		Root<ResourceTable> from = cq.from(ResourceTable.class);
		cq.multiselect(from.get("myId").as(Long.class));
		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(builder.equal(from.get("myResourceType"), myResourceName));
		predicates.add(builder.isNull(from.get("myDeleted")));

		if (theLastUpdated != null) {
			predicates.addAll(createLastUpdatedPredicates(theLastUpdated, builder, from));
		}

		cq.where(toArray(predicates));

		TypedQuery<Tuple> query = myEntityManager.createQuery(cq);
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

		RuntimeResourceDefinition resourceDef = myContext.getResourceDefinition(myResourceType);
		RuntimeSearchParam param = resourceDef.getSearchParam(theSort.getParamName());
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

	private List<Long> filterResourceIdsByLastUpdated(Collection<Long> thePids, final DateRangeParam theLastUpdated) {
		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceTable> from = cq.from(ResourceTable.class);
		cq.select(from.get("myId").as(Long.class));

		List<Predicate> lastUpdatedPredicates = createLastUpdatedPredicates(theLastUpdated, builder, from);
		lastUpdatedPredicates.add(0, from.get("myId").in(thePids));

		cq.where(SearchBuilder.toArray(lastUpdatedPredicates));
		TypedQuery<Long> query = myEntityManager.createQuery(cq);
		List<Long> resultList = query.getResultList();
		return resultList;
	}

	private void loadResourcesByPid(Collection<Long> theIncludePids, List<IBaseResource> theResourceListToPopulate, Set<Long> theRevIncludedPids, boolean theForHistoryOperation) {
		if (theIncludePids.isEmpty()) {
			return;
		}

		Map<Long, Integer> position = new HashMap<Long, Integer>();
		for (Long next : theIncludePids) {
			position.put(next, theResourceListToPopulate.size());
			theResourceListToPopulate.add(null);
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<ResourceTable> cq = builder.createQuery(ResourceTable.class);
		Root<ResourceTable> from = cq.from(ResourceTable.class);
		cq.where(from.get("myId").in(theIncludePids));
		TypedQuery<ResourceTable> q = myEntityManager.createQuery(cq);

		for (ResourceTable next : q.getResultList()) {
			Class<? extends IBaseResource> resourceType = myContext.getResourceDefinition(next.getResourceType()).getImplementingClass();
			IBaseResource resource = (IBaseResource) myCallingDao.toResource(resourceType, next, theForHistoryOperation);
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
	private HashSet<Long> loadReverseIncludes(Collection<Long> theMatches, Set<Include> theRevIncludes, boolean theReverseMode, DateRangeParam theLastUpdated) {
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
					TypedQuery<ResourceLink> q = myEntityManager.createQuery(sql, ResourceLink.class);
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
					if (myContext.getVersion().getVersion() == FhirVersionEnum.DSTU1) {
						paths = Collections.singletonList(nextInclude.getValue());
					} else {
						String resType = nextInclude.getParamType();
						if (isBlank(resType)) {
							continue;
						}
						RuntimeResourceDefinition def = myContext.getResourceDefinition(resType);
						if (def == null) {
							ourLog.warn("Unknown resource type in include/revinclude=" + nextInclude.getValue());
							continue;
						}

						String paramName = nextInclude.getParamName();
						RuntimeSearchParam param = isNotBlank(paramName) ? def.getSearchParam(paramName) : null;
						if (param == null) {
							ourLog.warn("Unknown param name in include/revinclude=" + nextInclude.getValue());
							continue;
						}

						paths = param.getPathsSplit();
					}

					String targetResourceType = defaultString(nextInclude.getParamTargetType(), null);
					for (String nextPath : paths) {
						String sql;
						if (targetResourceType != null) {
							sql = "SELECT r FROM ResourceLink r WHERE r.mySourcePath = :src_path AND r." + searchFieldName + " IN (:target_pids) AND r.myTargetResourceType = :target_resource_type";
						} else {
							sql = "SELECT r FROM ResourceLink r WHERE r.mySourcePath = :src_path AND r." + searchFieldName + " IN (:target_pids)";
						}
						TypedQuery<ResourceLink> q = myEntityManager.createQuery(sql, ResourceLink.class);
						q.setParameter("src_path", nextPath);
						q.setParameter("target_pids", nextRoundMatches);
						if (targetResourceType != null) {
							q.setParameter("target_resource_type", targetResourceType);
						}
						List<ResourceLink> results = q.getResultList();
						for (ResourceLink resourceLink : results) {
							if (theReverseMode) {
								pidsToInclude.add(resourceLink.getSourceResourcePid());
							} else {
								pidsToInclude.add(resourceLink.getTargetResourcePid());
							}
						}
					}
				}
			}

			if (theLastUpdated != null && (theLastUpdated.getLowerBoundAsInstant() != null || theLastUpdated.getUpperBoundAsInstant() != null)) {
				pidsToInclude = new HashSet<Long>(filterResourceIdsByLastUpdated(pidsToInclude, theLastUpdated));
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

	private List<Long> processSort(final SearchParameterMap theParams, Collection<Long> theLoadPids) {
		final List<Long> pids;
		// Set<Long> loadPids = theLoadPids;
		if (theParams.getSort() != null && isNotBlank(theParams.getSort().getParamName())) {
			List<Order> orders = new ArrayList<Order>();
			List<Predicate> predicates = new ArrayList<Predicate>();
			CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
			CriteriaQuery<Tuple> cq = builder.createTupleQuery();
			Root<ResourceTable> from = cq.from(ResourceTable.class);
			predicates.add(from.get("myId").in(theLoadPids));
			createSort(builder, from, theParams.getSort(), orders, predicates);
			if (orders.size() > 0) {
				Collection<Long> originalPids = theLoadPids;
				LinkedHashSet<Long> loadPids = new LinkedHashSet<Long>();
				cq.multiselect(from.get("myId").as(Long.class));
				cq.where(toArray(predicates));
				cq.orderBy(orders);

				TypedQuery<Tuple> query = myEntityManager.createQuery(cq);

				for (Tuple next : query.getResultList()) {
					loadPids.add(next.get(0, Long.class));
				}

				ourLog.debug("Sort PID order is now: {}", loadPids);

				pids = new ArrayList<Long>(loadPids);

				// Any ressources which weren't matched by the sort get added to the bottom
				for (Long next : originalPids) {
					if (loadPids.contains(next) == false) {
						pids.add(next);
					}
				}

			} else {
				pids = toList(theLoadPids);
			}
		} else {
			pids = toList(theLoadPids);
		}
		return pids;
	}

	public IBundleProvider search(final SearchParameterMap theParams) {
		StopWatch w = new StopWatch();
		final InstantDt now = InstantDt.withCurrentTime();

		DateRangeParam lu = theParams.getLastUpdated();
		if (lu != null && lu.isEmpty()) {
			lu = null;
		}

		Collection<Long> loadPids;
		if (theParams.getEverythingMode() != null) {

			Long pid = null;
			if (theParams.get(BaseResource.SP_RES_ID) != null) {
				StringParam idParm = (StringParam) theParams.get(BaseResource.SP_RES_ID).get(0).get(0);
				pid = BaseHapiFhirDao.translateForcedIdToPid(new IdDt(idParm.getValue()), myEntityManager);
			}

			loadPids = new HashSet<Long>();
			if (theParams.containsKey(Constants.PARAM_CONTENT) || theParams.containsKey(Constants.PARAM_TEXT)) {
				List<Long> pids = mySearchDao.everything(myResourceName, theParams);
				// if (pid != null) {
				// loadPids.add(pid);
				// }
				loadPids.addAll(pids);
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
				for (Tuple next : query.getResultList()) {
					loadPids.add(next.get(0, Long.class));
					Long nextLong = next.get(1, Long.class);
					if (nextLong != null) {
						loadPids.add(nextLong);
					}
				}
			}

		} else if (theParams.isEmpty()) {

			loadPids = new HashSet<Long>();
			TypedQuery<Tuple> query = createSearchAllByTypeQuery(lu);
			lu = null;
			for (Tuple next : query.getResultList()) {
				loadPids.add(next.get(0, Long.class));
			}
			if (loadPids.isEmpty()) {
				return new SimpleBundleProvider();
			}

		} else {

			List<Long> searchResultPids;
			if (mySearchDao == null) {
				if (theParams.containsKey(Constants.PARAM_TEXT)) {
					throw new InvalidRequestException("Fulltext search is not enabled on this service, can not process parameter: " + Constants.PARAM_TEXT);
				} else if (theParams.containsKey(Constants.PARAM_CONTENT)) {
					throw new InvalidRequestException("Fulltext search is not enabled on this service, can not process parameter: " + Constants.PARAM_CONTENT);
				}
				searchResultPids = null;
			} else {
				searchResultPids = mySearchDao.search(myResourceName, theParams);
			}
			if (theParams.isEmpty()) {
				loadPids = searchResultPids;
			} else {
				loadPids = searchForIdsWithAndOr(theParams, searchResultPids, lu);
			}
			if (loadPids.isEmpty()) {
				return new SimpleBundleProvider();
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

		// Handle _lastUpdated
		if (lu != null) {
			List<Long> resultList = filterResourceIdsByLastUpdated(loadPids, lu);
			loadPids.clear();
			for (Long next : resultList) {
				loadPids.add(next);
			}

			if (loadPids.isEmpty()) {
				return new SimpleBundleProvider();
			}
		}

		// Handle sorting if any was provided
		final List<Long> pids = processSort(theParams, loadPids);

		// Load _revinclude resources
		final Set<Long> revIncludedPids;
		if (theParams.getEverythingMode() == null) {
			if (theParams.getRevIncludes() != null && theParams.getRevIncludes().isEmpty() == false) {
				revIncludedPids = loadReverseIncludes(pids, theParams.getRevIncludes(), true, lu);
			} else {
				revIncludedPids = new HashSet<Long>();
			}
		} else {
			revIncludedPids = new HashSet<Long>();
		}

		ourLog.debug("Search returned PIDs: {}", pids);

		final int totalCount = pids.size();

		IBundleProvider retVal = new IBundleProvider() {
			@Override
			public InstantDt getPublished() {
				return now;
			}

			@Override
			public List<IBaseResource> getResources(final int theFromIndex, final int theToIndex) {
				TransactionTemplate template = new TransactionTemplate(myPlatformTransactionManager);
				return template.execute(new TransactionCallback<List<IBaseResource>>() {
					@Override
					public List<IBaseResource> doInTransaction(TransactionStatus theStatus) {
						List<Long> pidsSubList = pids.subList(theFromIndex, theToIndex);

						// Load includes
						pidsSubList = new ArrayList<Long>(pidsSubList);
						revIncludedPids.addAll(loadReverseIncludes(pidsSubList, theParams.getIncludes(), false, theParams.getLastUpdated()));

						// Execute the query and make sure we return distinct results
						List<IBaseResource> resources = new ArrayList<IBaseResource>();
						loadResourcesByPid(pidsSubList, resources, revIncludedPids, false);

						return resources;
					}

				});
			}

			@Override
			public Integer preferredPageSize() {
				return theParams.getCount();
			}

			@Override
			public int size() {
				return totalCount;
			}
		};

		ourLog.info(" {} on {} in {}ms", new Object[] { myResourceName, theParams, w.getMillisAndRestart() });
		return retVal;
	}

	public Set<Long> searchForIdsWithAndOr(SearchParameterMap theParams, Collection<Long> theInitialPids, DateRangeParam theLastUpdated) {
		SearchParameterMap params = theParams;
		if (params == null) {
			params = new SearchParameterMap();
		}

		RuntimeResourceDefinition resourceDef = myContext.getResourceDefinition(myResourceType);

		Set<Long> pids = new HashSet<Long>();
		if (theInitialPids != null) {
			pids.addAll(theInitialPids);
		}

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
								return new HashSet<Long>();
							}
						}

						pids = addPredicateId(pids, joinPids, theLastUpdated);
						if (pids.isEmpty()) {
							return new HashSet<Long>();
						}

						if (pids.isEmpty()) {
							pids.addAll(joinPids);
						} else {
							pids.retainAll(joinPids);
						}
					}
				}

			} else if (nextParamName.equals(BaseResource.SP_RES_LANGUAGE)) {

				pids = addPredicateLanguage(pids, nextParamEntry.getValue(), theLastUpdated);

			} else if (nextParamName.equals(Constants.PARAM_TAG) || nextParamName.equals(Constants.PARAM_PROFILE) || nextParamName.equals(Constants.PARAM_SECURITY)) {

				pids = addPredicateTag(pids, nextParamEntry.getValue(), nextParamName, theLastUpdated);

			} else {

				RuntimeSearchParam nextParamDef = resourceDef.getSearchParam(nextParamName);
				if (nextParamDef != null) {
					switch (nextParamDef.getParamType()) {
					case DATE:
						for (List<? extends IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
							pids = addPredicateDate(nextParamName, pids, nextAnd);
							if (pids.isEmpty()) {
								return new HashSet<Long>();
							}
						}
						break;
					case QUANTITY:
						for (List<? extends IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
							pids = addPredicateQuantity(nextParamName, pids, nextAnd);
							if (pids.isEmpty()) {
								return new HashSet<Long>();
							}
						}
						break;
					case REFERENCE:
						for (List<? extends IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
							pids = addPredicateReference(nextParamName, pids, nextAnd);
							if (pids.isEmpty()) {
								return new HashSet<Long>();
							}
						}
						break;
					case STRING:
						for (List<? extends IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
							pids = addPredicateString(nextParamName, pids, nextAnd);
							if (pids.isEmpty()) {
								return new HashSet<Long>();
							}
						}
						break;
					case TOKEN:
						for (List<? extends IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
							pids = addPredicateToken(nextParamName, pids, nextAnd);
							if (pids.isEmpty()) {
								return new HashSet<Long>();
							}
						}
						break;
					case NUMBER:
						for (List<? extends IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
							pids = addPredicateNumber(nextParamName, pids, nextAnd);
							if (pids.isEmpty()) {
								return new HashSet<Long>();
							}
						}
						break;
					case COMPOSITE:
						for (List<? extends IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
							pids = addPredicateComposite(nextParamDef, pids, nextAnd);
							if (pids.isEmpty()) {
								return new HashSet<Long>();
							}
						}
						break;
					case URI:
						for (List<? extends IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
							pids = addPredicateUri(nextParamName, pids, nextAnd);
							if (pids.isEmpty()) {
								return new HashSet<Long>();
							}
						}
						break;
					}
				}
			}
		}

		return pids;
	}

	public void setType(Class<? extends IBaseResource> theResourceType, String theResourceName) {
		myResourceType = theResourceType;
		myResourceName = theResourceName;
	}

	private List<Long> toList(Collection<Long> theLoadPids) {
		final List<Long> pids;
		if (theLoadPids instanceof List) {
			pids = (List<Long>) theLoadPids;
		} else {
			pids = new ArrayList<Long>(theLoadPids);
		}
		return pids;
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

		qp.setValueAsQueryToken(theQualifier, theValueAsQueryToken); // aaaa
		return qp;
	}

	/**
	 * Figures out the tolerance for a search. For example, if the user is searching for
	 * <code>4.00</code>, this method returns <code>0.005</code> because we shold actually
	 * match values which are <code>4 (+/-) 0.005</code> according to the FHIR specs.
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

	static Predicate[] toArray(List<Predicate> thePredicates) {
		return thePredicates.toArray(new Predicate[thePredicates.size()]);
	}

}
