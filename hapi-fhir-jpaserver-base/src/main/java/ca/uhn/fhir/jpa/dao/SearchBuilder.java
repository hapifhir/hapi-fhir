package ca.uhn.fhir.jpa.dao;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeChildResourceDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
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
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.base.composite.BaseIdentifierDt;
import ca.uhn.fhir.model.base.composite.BaseQuantityDt;
import ca.uhn.fhir.model.dstu.resource.BaseResource;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.method.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.CompositeParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class SearchBuilder {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchBuilder.class);
	
	private EntityManager myEntityManager;
	private FhirContext myContext;

	public SearchBuilder(FhirContext theFhirContext, EntityManager theEntityManager) {
		myContext = theFhirContext;
		myEntityManager = theEntityManager;
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
			lb = theBuilder.or(gt, lt);

			// Predicate gin = builder.isNull(from.get("myValueLow"));
			// Predicate lbo = builder.or(gt, gin);
			// Predicate lin = builder.isNull(from.get("myValueHigh"));
			// Predicate hbo = builder.or(lt, lin);
			// lb = builder.and(lbo, hbo);
		}

		Predicate ub = null;
		if (upperBound != null) {
			Predicate gt = theBuilder.lessThanOrEqualTo(theFrom.<Date> get("myValueLow"), upperBound);
			Predicate lt = theBuilder.lessThanOrEqualTo(theFrom.<Date> get("myValueHigh"), upperBound);
			ub = theBuilder.or(gt, lt);

			// Predicate gin = builder.isNull(from.get("myValueLow"));
			// Predicate lbo = builder.or(gt, gin);
			// Predicate lin = builder.isNull(from.get("myValueHigh"));
			// Predicate ubo = builder.or(lt, lin);
			// ub = builder.and(ubo, lbo);

		}

		if (lb != null && ub != null) {
			return (theBuilder.and(lb, ub));
		} else if (lb != null) {
			return (lb);
		} else {
			return (ub);
		}
	}

	private Predicate createPredicateString(IQueryParameterType theParameter, String theParamName, CriteriaBuilder theBuilder,
			From<ResourceIndexedSearchParamString, ResourceIndexedSearchParamString> theFrom) {
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
			throw new InvalidRequestException("Parameter[" + theParamName + "] has length (" + rawSearchTerm.length() + ") that is longer than maximum allowed ("
					+ ResourceIndexedSearchParamString.MAX_LENGTH + "): " + rawSearchTerm);
		}

		String likeExpression = BaseHapiFhirDao.normalizeString(rawSearchTerm);
		likeExpression = likeExpression.replace("%", "[%]") + "%";

		Predicate singleCode = theBuilder.like(theFrom.get("myValueNormalized").as(String.class), likeExpression);
		if (theParameter instanceof StringParam && ((StringParam) theParameter).isExact()) {
			Predicate exactCode = theBuilder.equal(theFrom.get("myValueExact"), rawSearchTerm);
			singleCode = theBuilder.and(singleCode, exactCode);
		}
		return singleCode;
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

	private List<Long> processSort(final SearchParameterMap theParams, Collection<Long> theLoadPids) {
		final List<Long> pids;
//		Set<Long> loadPids = theLoadPids;
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
	private Predicate createPredicateToken(IQueryParameterType theParameter, String theParamName, CriteriaBuilder theBuilder,
			From<ResourceIndexedSearchParamToken, ResourceIndexedSearchParamToken> theFrom) {
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
			throw new InvalidRequestException(
					"Parameter[" + theParamName + "] has system (" + system.length() + ") that is longer than maximum allowed (" + ResourceIndexedSearchParamToken.MAX_LENGTH + "): " + system);
		}
		if (code != null && code.length() > ResourceIndexedSearchParamToken.MAX_LENGTH) {
			throw new InvalidRequestException(
					"Parameter[" + theParamName + "] has code (" + code.length() + ") that is longer than maximum allowed (" + ResourceIndexedSearchParamToken.MAX_LENGTH + "): " + code);
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
		RuntimeSearchParam param = myContext.getResourceDefinition(getResourceType()).getSearchParam(theParamName);
		List<String> path = param.getPathsSplit();
		Predicate type = from.get("mySourcePath").in(path);
		return type;
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

	// private Set<Long> addPredicateComposite(String theParamName, Set<Long> thePids, List<? extends
	// IQueryParameterType> theList) {
	// }

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

	private boolean addPredicateMissingFalseIfPresent(CriteriaBuilder theBuilder, String theParamName, Root<? extends BaseResourceIndexedSearchParam> from, List<Predicate> codePredicates,
			IQueryParameterType nextOr) {
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

	private boolean addPredicateMissingFalseIfPresentForResourceLink(CriteriaBuilder theBuilder, String theParamName, Root<? extends ResourceLink> from, List<Predicate> codePredicates,
			IQueryParameterType nextOr) {
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

				Path<Object> fromObj = from.get("myValue");
				if (param.getComparator() == null) {
					double mul = value.doubleValue() * 1.01;
					double low = value.doubleValue() - mul;
					double high = value.doubleValue() + mul;
					Predicate lowPred = builder.ge(fromObj.as(Long.class), low);
					Predicate highPred = builder.le(fromObj.as(Long.class), high);
					codePredicates.add(builder.and(lowPred, highPred));
				} else {
					switch (param.getComparator()) {
					case GREATERTHAN:
						codePredicates.add(builder.greaterThan(fromObj.as(BigDecimal.class), value));
						break;
					case GREATERTHAN_OR_EQUALS:
						codePredicates.add(builder.ge(fromObj.as(BigDecimal.class), value));
						break;
					case LESSTHAN:
						codePredicates.add(builder.lessThan(fromObj.as(BigDecimal.class), value));
						break;
					case LESSTHAN_OR_EQUALS:
						codePredicates.add(builder.le(fromObj.as(BigDecimal.class), value));
						break;
					}
				}
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
		String resourceType = myContext.getResourceDefinition(getResourceType()).getName();

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceTable> from = cq.from(ResourceTable.class);
		cq.select(from.get("myId").as(Long.class));

		Subquery<Long> subQ = cq.subquery(Long.class);
		Root<? extends BaseResourceIndexedSearchParam> subQfrom = subQ.from(theParamTable);
		subQ.select(subQfrom.get("myResourcePid").as(Long.class));
		Predicate subQname = builder.equal(subQfrom.get("myParamName"), theParamName);
		Predicate subQtype = builder.equal(subQfrom.get("myResourceType"), resourceType);
		subQ.where(builder.and(subQtype, subQname));

		Predicate joinPredicate = builder.not(builder.in(from.get("myId")).value(subQ));
		Predicate typePredicate = builder.equal(from.get("myResourceType"), resourceType);
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
			QuantityCompararatorEnum cmpValue;
			BigDecimal valueValue;
			boolean approx = false;

			if (params instanceof BaseQuantityDt) {
				BaseQuantityDt param = (BaseQuantityDt) params;
				systemValue = param.getSystemElement().getValueAsString();
				unitsValue = param.getUnitsElement().getValueAsString();
				cmpValue = QuantityCompararatorEnum.VALUESET_BINDER.fromCodeString(param.getComparatorElement().getValueAsString());
				valueValue = param.getValueElement().getValue();
			} else if (params instanceof QuantityParam) {
				QuantityParam param = (QuantityParam) params;
				systemValue = param.getSystem().getValueAsString();
				unitsValue = param.getUnits();
				cmpValue = param.getComparator();
				valueValue = param.getValue().getValue();
				approx = param.isApproximate();
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

			Predicate num;
			if (cmpValue == null) {
				BigDecimal mul = approx ? new BigDecimal(0.1) : new BigDecimal(0.01);
				BigDecimal low = valueValue.subtract(valueValue.multiply(mul));
				BigDecimal high = valueValue.add(valueValue.multiply(mul));
				Predicate lowPred = builder.gt(from.get("myValue").as(BigDecimal.class), low);
				Predicate highPred = builder.lt(from.get("myValue").as(BigDecimal.class), high);
				num = builder.and(lowPred, highPred);
			} else {
				switch (cmpValue) {
				case GREATERTHAN:
					Expression<Number> path = from.get("myValue");
					num = builder.gt(path, valueValue);
					break;
				case GREATERTHAN_OR_EQUALS:
					path = from.get("myValue");
					num = builder.ge(path, valueValue);
					break;
				case LESSTHAN:
					path = from.get("myValue");
					num = builder.lt(path, valueValue);
					break;
				case LESSTHAN_OR_EQUALS:
					path = from.get("myValue");
					num = builder.le(path, valueValue);
					break;
				default:
					throw new IllegalStateException(cmpValue.getCode());
				}
			}

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
					String resourceId = ref.getValueAsQueryToken();
					if (resourceId.contains("/")) {
						IIdType dt = new IdDt(resourceId);
						resourceId = dt.getIdPart();
					}
					Long targetPid = translateForcedIdToPid(new IdDt(resourceId));
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

						IFhirResourceDao<?> dao = getDao(nextType);
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

						boolean isMeta = RESOURCE_META_PARAMS.containsKey(chain);
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
							IQueryParameterType type = newInstanceType(chain);
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
						throw new InvalidRequestException("Invalid " + theParamName + " parameter (must supply a value/code and not just a system): " + nextParam.getValueAsQueryToken());
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

	static Predicate[] toArray(List<Predicate> thePredicates) {
		return thePredicates.toArray(new Predicate[thePredicates.size()]);
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
				codePredicates.add(builder.equal(fromObj.as(String.class), value));
			} else {
				throw new IllegalArgumentException("Invalid URI type: " + params.getClass());
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

	
}
