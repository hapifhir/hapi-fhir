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
import static org.apache.commons.lang3.StringUtils.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TemporalType;
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

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeChildResourceDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.entity.BaseHasResource;
import ca.uhn.fhir.jpa.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.entity.BaseTag;
import ca.uhn.fhir.jpa.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamToken;
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
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.base.composite.BaseIdentifierDt;
import ca.uhn.fhir.model.base.composite.BaseQuantityDt;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome.Issue;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
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
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.ObjectUtil;

@Transactional(propagation = Propagation.REQUIRED)
public abstract class BaseHapiFhirResourceDao<T extends IResource> extends BaseHapiFhirDao<T>implements IFhirResourceDao<T> {

	static final String OO_SEVERITY_WARN = "warning";
	static final String OO_SEVERITY_INFO = "information";
	static final String OO_SEVERITY_ERROR = "error";

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseHapiFhirResourceDao.class);

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager myEntityManager;

	@Autowired
	private PlatformTransactionManager myPlatformTransactionManager;

	private String myResourceName;
	private Class<T> myResourceType;
	private String mySecondaryPrimaryKeyParamName;

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

		Predicate masterCodePredicate = builder.or(codePredicates.toArray(new Predicate[0]));

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

	private Set<Long> addPredicateId(Set<Long> theExistingPids, Set<Long> thePids) {
		if (thePids == null || thePids.isEmpty()) {
			return Collections.emptySet();
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceTable> from = cq.from(ResourceTable.class);
		cq.select(from.get("myId").as(Long.class));

		Predicate typePredicate = builder.equal(from.get("myResourceType"), myResourceName);
		Predicate idPrecidate = from.get("myId").in(thePids);

		cq.where(builder.and(typePredicate, idPrecidate));

		TypedQuery<Long> q = myEntityManager.createQuery(cq);
		HashSet<Long> found = new HashSet<Long>(q.getResultList());
		if (!theExistingPids.isEmpty()) {
			theExistingPids.retainAll(found);
		}

		return found;
	}

	// private Set<Long> addPredicateComposite(String theParamName, Set<Long> thePids, List<? extends
	// IQueryParameterType> theList) {
	// }

	private Set<Long> addPredicateLanguage(Set<Long> thePids, List<List<? extends IQueryParameterType>> theList) {
		if (theList == null || theList.isEmpty()) {
			return thePids;
		}
		if (theList.size() > 1) {
			throw new InvalidRequestException("Language parameter can not have more than one AND value, found " + theList.size());
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceTable> from = cq.from(ResourceTable.class);
		cq.select(from.get("myId").as(Long.class));

		Set<String> values = new HashSet<String>();
		for (IQueryParameterType next : theList.get(0)) {
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
			return thePids;
		}

		Predicate typePredicate = builder.equal(from.get("myResourceType"), myResourceName);
		Predicate langPredicate = from.get("myLanguage").as(String.class).in(values);
		Predicate masterCodePredicate = builder.and(typePredicate, langPredicate);

		if (thePids.size() > 0) {
			Predicate inPids = (from.get("myId").in(thePids));
			cq.where(builder.and(masterCodePredicate, inPids));
		} else {
			cq.where(masterCodePredicate);
		}

		TypedQuery<Long> q = myEntityManager.createQuery(cq);
		return new HashSet<Long>(q.getResultList());
	}

	private Set<Long> addPredicateTag(Set<Long> thePids, List<List<? extends IQueryParameterType>> theList, String theParamName) {
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
			throw new IllegalArgumentException("Paramname: " + theParamName); // shouldn't happen
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
				andPredicates.add(builder.or(orPredicates.toArray(new Predicate[0])));
			}

			Predicate masterCodePredicate = builder.and(andPredicates.toArray(new Predicate[0]));

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

	@Override
	public Set<Long> processMatchUrl(String theMatchUrl) {
		return processMatchUrl(theMatchUrl, getResourceType());
	}

	private boolean addPredicateMissingFalseIfPresent(CriteriaBuilder theBuilder, String theParamName, Root<? extends BaseResourceIndexedSearchParam> from, List<Predicate> codePredicates, IQueryParameterType nextOr) {
		boolean missingFalse = false;
		if (nextOr.getMissing() != null) {
			if (nextOr.getMissing().booleanValue() == true) {
				throw new InvalidRequestException(getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "multipleParamsWithSameNameOneIsMissingTrue", theParamName));
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
				throw new InvalidRequestException(getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "multipleParamsWithSameNameOneIsMissingTrue", theParamName));
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

		Predicate masterCodePredicate = builder.or(codePredicates.toArray(new Predicate[0]));

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
		String resourceType = getContext().getResourceDefinition(getResourceType()).getName();

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

		Predicate masterCodePredicate = builder.or(codePredicates.toArray(new Predicate[0]));

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

				String resourceId = ref.getValueAsQueryToken();

				if (isBlank(ref.getChain())) {
					if (resourceId.contains("/")) {
						IIdType dt = new IdDt(resourceId);
						resourceId = dt.getIdPart();
					}
					Long targetPid = translateForcedIdToPid(new IdDt(resourceId));
					ourLog.info("Searching for resource link with target PID: {}", targetPid);
					Predicate eq = builder.equal(from.get("myTargetResourcePid"), targetPid);

					codePredicates.add(eq);

				} else {

					String paramPath = getContext().getResourceDefinition(myResourceType).getSearchParam(theParamName).getPath();
					BaseRuntimeChildDefinition def = getContext().newTerser().getDefinition(myResourceType, paramPath);
					if (!(def instanceof RuntimeChildResourceDefinition)) {
						throw new ConfigurationException("Property " + paramPath + " of type " + myResourceName + " is not a resource: " + def.getClass());
					}
					List<Class<? extends IBaseResource>> resourceTypes;
					if (isBlank(ref.getResourceType())) {
						RuntimeChildResourceDefinition resDef = (RuntimeChildResourceDefinition) def;
						resourceTypes = resDef.getResourceTypes();
					} else {
						resourceTypes = new ArrayList<Class<? extends IBaseResource>>();
						RuntimeResourceDefinition resDef = getContext().getResourceDefinition(ref.getResourceType());
						resourceTypes.add(resDef.getImplementingClass());
					}

					boolean foundChainMatch = false;
					for (Class<? extends IBaseResource> nextType : resourceTypes) {
						RuntimeResourceDefinition typeDef = getContext().getResourceDefinition(nextType);

						String chain = ref.getChain();
						String remainingChain = null;
						int chainDotIndex = chain.indexOf('.');
						if (chainDotIndex != -1) {
							remainingChain = chain.substring(chainDotIndex + 1);
							chain = chain.substring(0, chainDotIndex);
						}

						RuntimeSearchParam param = typeDef.getSearchParam(chain);
						if (param == null) {
							ourLog.debug("Type {} doesn't have search param {}", nextType.getSimpleName(), param);
							continue;
						}
						IFhirResourceDao<?> dao = getDao(nextType);
						if (dao == null) {
							ourLog.debug("Don't have a DAO for type {}", nextType.getSimpleName(), param);
							continue;
						}

						IQueryParameterType chainValue;
						if (remainingChain != null) {
							if (param.getParamType() != RestSearchParameterTypeEnum.REFERENCE) {
								ourLog.debug("Type {} parameter {} is not a reference, can not chain {}", new Object[] { nextType.getSimpleName(), chain, remainingChain });
								continue;
							}

							chainValue = new ReferenceParam();
							chainValue.setValueAsQueryToken(null, resourceId);
							((ReferenceParam) chainValue).setChain(remainingChain);
						} else {
							chainValue = toParameterType(param, resourceId);
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
						throw new InvalidRequestException(getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "invalidParameterChain", theParamName + '.' + ref.getChain()));
					}
				}

			} else {
				throw new IllegalArgumentException("Invalid token type (expecting ReferenceParam): " + params.getClass());
			}

		}

		Predicate masterCodePredicate = builder.or(codePredicates.toArray(new Predicate[0]));

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

		Predicate masterCodePredicate = builder.or(codePredicates.toArray(new Predicate[0]));

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

		Predicate masterCodePredicate = builder.or(codePredicates.toArray(new Predicate[0]));

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

	private List<IBaseResource> addResourcesAsIncludesById(List<IBaseResource> theListToPopulate, Set<? extends IIdType> includePids, List<IBaseResource> resources) {
		if (!includePids.isEmpty()) {
			ourLog.info("Loading {} included resources", includePids.size());
			resources = loadResourcesById(includePids);
			for (IBaseResource next : resources) {
				ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put((IResource) next, BundleEntrySearchModeEnum.INCLUDE);
			}
			theListToPopulate.addAll(resources);
		}
		return resources;
	}

	@Override
	public void addTag(IIdType theId, TagTypeEnum theTagType, String theScheme, String theTerm, String theLabel) {
		StopWatch w = new StopWatch();
		BaseHasResource entity = readEntity(theId);
		if (entity == null) {
			throw new ResourceNotFoundException(theId);
		}

		//@formatter:off
		for (BaseTag next : new ArrayList<BaseTag>(entity.getTags())) {
			if (ObjectUtil.equals(next.getTag().getTagType(), theTagType) && 
					ObjectUtil.equals(next.getTag().getSystem(), theScheme) && 
					ObjectUtil.equals(next.getTag().getCode(), theTerm)) {
				return;
			}
		}
		//@formatter:on

		entity.setHasTags(true);

		TagDefinition def = getTag(TagTypeEnum.TAG, theScheme, theTerm, theLabel);
		BaseTag newEntity = entity.addTag(def);

		myEntityManager.persist(newEntity);
		myEntityManager.merge(entity);
		notifyWriteCompleted();
		ourLog.info("Processed addTag {}/{} on {} in {}ms", new Object[] { theScheme, theTerm, theId, w.getMillisAndRestart() });
	}

	@Override
	public DaoMethodOutcome create(final T theResource) {
		return create(theResource, null, true);
	}

	@Override
	public DaoMethodOutcome create(final T theResource, String theIfNoneExist) {
		return create(theResource, theIfNoneExist, true);
	}

	@Override
	public DaoMethodOutcome create(T theResource, String theIfNoneExist, boolean thePerformIndexing) {
		if (isNotBlank(theResource.getId().getIdPart())) {
			if (getContext().getVersion().getVersion().equals(FhirVersionEnum.DSTU1)) {
				if (theResource.getId().isIdPartValidLong()) {
					String message = getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "failedToCreateWithClientAssignedNumericId", theResource.getId().getIdPart());
					throw new InvalidRequestException(message, createErrorOperationOutcome(message));
				}
			} else {
				String message = getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "failedToCreateWithClientAssignedId", theResource.getId().getIdPart());
				throw new InvalidRequestException(message, createErrorOperationOutcome(message));
			}
		}

		return doCreate(theResource, theIfNoneExist, thePerformIndexing);
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

	protected IBaseOperationOutcome createErrorOperationOutcome(String theMessage) {
		return createOperationOutcome(OO_SEVERITY_ERROR, theMessage);
	}

	protected IBaseOperationOutcome createInfoOperationOutcome(String theMessage) {
		return createOperationOutcome(OO_SEVERITY_INFO, theMessage);
	}

	protected abstract IBaseOperationOutcome createOperationOutcome(String theSeverity, String theMessage);

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

		String likeExpression = normalizeString(rawSearchTerm);
		likeExpression = likeExpression.replace("%", "[%]") + "%";

		Predicate singleCode = theBuilder.like(theFrom.get("myValueNormalized").as(String.class), likeExpression);
		if (theParameter instanceof StringParam && ((StringParam) theParameter).isExact()) {
			Predicate exactCode = theBuilder.equal(theFrom.get("myValueExact"), rawSearchTerm);
			singleCode = theBuilder.and(singleCode, exactCode);
		}
		return singleCode;
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
		Predicate singleCode = theBuilder.and(singleCodePredicates.toArray(new Predicate[0]));
		return singleCode;
	}

	private Predicate createResourceLinkPathPredicate(String theParamName, CriteriaBuilder builder, Root<? extends ResourceLink> from) {
		RuntimeSearchParam param = getContext().getResourceDefinition(getResourceType()).getSearchParam(theParamName);
		List<String> path = param.getPathsSplit();
		Predicate type = from.get("mySourcePath").in(path);
		return type;
	}

	private void createSort(CriteriaBuilder theBuilder, Root<ResourceTable> theFrom, SortSpec theSort, List<Order> theOrders, List<Predicate> thePredicates) {
		if (theSort == null || isBlank(theSort.getParamName())) {
			return;
		}

		if ("_id".equals(theSort.getParamName())) {
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

		RuntimeResourceDefinition resourceDef = getContext().getResourceDefinition(myResourceType);
		RuntimeSearchParam param = resourceDef.getSearchParam(theSort.getParamName());
		if (param == null) {
			throw new InvalidRequestException("Unknown sort parameter '" + theSort.getParamName() + "'");
		}

		String joinAttrName;
		String sortAttrName;

		switch (param.getParamType()) {
		case STRING:
			joinAttrName = "myParamsString";
			sortAttrName = "myValueExact";
			break;
		case DATE:
			joinAttrName = "myParamsDate";
			sortAttrName = "myValueLow";
			break;
		case REFERENCE:
			joinAttrName = "myResourceLinks";
			sortAttrName = "myTargetResourcePid";
			break;
		default:
			throw new NotImplementedException("This server does not support _sort specifications of type " + param.getParamType() + " - Can't serve _sort=" + theSort.getParamName());
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

		if (theSort.getOrder() == null || theSort.getOrder() == SortOrderEnum.ASC) {
			theOrders.add(theBuilder.asc(stringJoin.get(sortAttrName)));
		} else {
			theOrders.add(theBuilder.desc(stringJoin.get(sortAttrName)));
		}

		createSort(theBuilder, theFrom, theSort.getChain(), theOrders, thePredicates);
	}

	@Override
	public DaoMethodOutcome delete(IIdType theId) {
		StopWatch w = new StopWatch();
		final ResourceTable entity = readEntityLatestVersion(theId);
		if (theId.hasVersionIdPart() && Long.parseLong(theId.getVersionIdPart()) != entity.getVersion()) {
			throw new InvalidRequestException("Trying to update " + theId + " but this is not the current version");
		}

		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theId, theId.getResourceType());
		notifyInterceptors(RestOperationTypeEnum.DELETE, requestDetails);

		ResourceTable savedEntity = updateEntity(null, entity, true, new Date());

		notifyWriteCompleted();

		ourLog.info("Processed delete on {} in {}ms", theId.getValue(), w.getMillisAndRestart());
		return toMethodOutcome(savedEntity, null);
	}

	@Override
	public DaoMethodOutcome deleteByUrl(String theUrl) {
		StopWatch w = new StopWatch();

		Set<Long> resource = processMatchUrl(theUrl, myResourceType);
		if (resource.isEmpty()) {
			throw new ResourceNotFoundException(getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "unableToDeleteNotFound", theUrl));
		} else if (resource.size() > 1) {
			throw new ResourceNotFoundException(getContext().getLocalizer().getMessage(BaseHapiFhirDao.class, "transactionOperationWithMultipleMatchFailure", "DELETE", theUrl, resource.size()));
		}

		Long pid = resource.iterator().next();
		ResourceTable entity = myEntityManager.find(ResourceTable.class, pid);

		// Notify interceptors
		IdDt idToDelete = entity.getIdDt();
		ActionRequestDetails requestDetails = new ActionRequestDetails(idToDelete, idToDelete.getResourceType());
		notifyInterceptors(RestOperationTypeEnum.DELETE, requestDetails);

		// Perform delete
		ResourceTable savedEntity = updateEntity(null, entity, true, new Date());
		notifyWriteCompleted();

		ourLog.info("Processed delete on {} in {}ms", theUrl, w.getMillisAndRestart());
		return toMethodOutcome(savedEntity, null);
	}

	private DaoMethodOutcome doCreate(T theResource, String theIfNoneExist, boolean thePerformIndexing) {
		StopWatch w = new StopWatch();

		preProcessResourceForStorage(theResource);

		ResourceTable entity = new ResourceTable();
		entity.setResourceType(toResourceName(theResource));

		if (isNotBlank(theIfNoneExist)) {
			Set<Long> match = processMatchUrl(theIfNoneExist, myResourceType);
			if (match.size() > 1) {
				String msg = getContext().getLocalizer().getMessage(BaseHapiFhirDao.class, "transactionOperationWithMultipleMatchFailure", "CREATE", theIfNoneExist, match.size());
				throw new PreconditionFailedException(msg);
			} else if (match.size() == 1) {
				Long pid = match.iterator().next();
				entity = myEntityManager.find(ResourceTable.class, pid);
				return toMethodOutcome(entity, theResource).setCreated(false);
			}
		}

		if (isNotBlank(theResource.getId().getIdPart())) {
			if (isValidPid(theResource.getId())) {
				throw new UnprocessableEntityException("This server cannot create an entity with a user-specified numeric ID - Client should not specify an ID when creating a new resource, or should include at least one letter in the ID to force a client-defined ID");
			}
			createForcedIdIfNeeded(entity, theResource.getId());

			if (entity.getForcedId() != null) {
				try {
					translateForcedIdToPid(theResource.getId());
					throw new UnprocessableEntityException(getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "duplicateCreateForcedId", theResource.getId().getIdPart()));
				} catch (ResourceNotFoundException e) {
					// good, this ID doesn't exist so we can create it
				}
			}

		}

		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theResource.getId(), toResourceName(theResource), theResource);
		notifyInterceptors(RestOperationTypeEnum.CREATE, requestDetails);

		updateEntity(theResource, entity, false, null, thePerformIndexing, true);

		DaoMethodOutcome outcome = toMethodOutcome(entity, theResource).setCreated(true);

		notifyWriteCompleted();

		String msg = getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "successfulCreate", outcome.getId(), w.getMillisAndRestart());
		outcome.setOperationOutcome(createInfoOperationOutcome(msg));

		ourLog.info(msg);
		return outcome;
	}

	@Override
	public TagList getAllResourceTags() {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(null, null);
		notifyInterceptors(RestOperationTypeEnum.GET_TAGS, requestDetails);

		StopWatch w = new StopWatch();
		TagList tags = super.getTags(myResourceType, null);
		ourLog.info("Processed getTags on {} in {}ms", myResourceName, w.getMillisAndRestart());
		return tags;
	}

	protected abstract List<Object> getIncludeValues(FhirTerser theTerser, Include theInclude, IBaseResource theResource, RuntimeResourceDefinition theResourceDef);

	public Class<T> getResourceType() {
		return myResourceType;
	}

	public String getResourceName() {
		return myResourceName;
	}

	@Override
	public TagList getTags(IIdType theResourceId) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theResourceId, null);
		notifyInterceptors(RestOperationTypeEnum.GET_TAGS, requestDetails);

		StopWatch w = new StopWatch();
		TagList retVal = super.getTags(myResourceType, theResourceId);
		ourLog.info("Processed getTags on {} in {}ms", theResourceId, w.getMillisAndRestart());
		return retVal;
	}

	@Override
	public IBundleProvider history(Date theSince) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(null, null);
		notifyInterceptors(RestOperationTypeEnum.HISTORY_SYSTEM, requestDetails);

		StopWatch w = new StopWatch();
		IBundleProvider retVal = super.history(myResourceName, null, theSince);
		ourLog.info("Processed history on {} in {}ms", myResourceName, w.getMillisAndRestart());
		return retVal;
	}

	@Override
	public IBundleProvider history(final IIdType theId, final Date theSince) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theId, getResourceName());
		notifyInterceptors(RestOperationTypeEnum.HISTORY_INSTANCE, requestDetails);

		final InstantDt end = createHistoryToTimestamp();
		final String resourceType = getContext().getResourceDefinition(myResourceType).getName();

		T currentTmp;
		try {
			BaseHasResource entity = readEntity(theId.toVersionless(), false);
			validateResourceType(entity);
			currentTmp = toResource(myResourceType, entity);
			if (ResourceMetadataKeyEnum.UPDATED.get(currentTmp).after(end.getValue())) {
				currentTmp = null;
			}
		} catch (ResourceNotFoundException e) {
			currentTmp = null;
		}

		final T current = currentTmp;

		String querySring = "SELECT count(h) FROM ResourceHistoryTable h " + "WHERE h.myResourceId = :PID AND h.myResourceType = :RESTYPE" + " AND h.myUpdated < :END" + (theSince != null ? " AND h.myUpdated >= :SINCE" : "");
		TypedQuery<Long> countQuery = myEntityManager.createQuery(querySring, Long.class);
		countQuery.setParameter("PID", translateForcedIdToPid(theId));
		countQuery.setParameter("RESTYPE", resourceType);
		countQuery.setParameter("END", end.getValue(), TemporalType.TIMESTAMP);
		if (theSince != null) {
			countQuery.setParameter("SINCE", theSince, TemporalType.TIMESTAMP);
		}
		int historyCount = countQuery.getSingleResult().intValue();

		final int offset;
		final int count;
		if (current != null) {
			count = historyCount + 1;
			offset = 1;
		} else {
			offset = 0;
			count = historyCount;
		}

		if (count == 0) {
			throw new ResourceNotFoundException(theId);
		}

		return new IBundleProvider() {

			@Override
			public InstantDt getPublished() {
				return end;
			}

			@Override
			public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
				List<IBaseResource> retVal = new ArrayList<IBaseResource>();
				if (theFromIndex == 0 && current != null) {
					retVal.add(current);
				}

				TypedQuery<ResourceHistoryTable> q = myEntityManager.createQuery("SELECT h FROM ResourceHistoryTable h WHERE h.myResourceId = :PID AND h.myResourceType = :RESTYPE AND h.myUpdated < :END " + (theSince != null ? " AND h.myUpdated >= :SINCE" : "") + " ORDER BY h.myUpdated ASC",
						ResourceHistoryTable.class);
				q.setParameter("PID", translateForcedIdToPid(theId));
				q.setParameter("RESTYPE", resourceType);
				q.setParameter("END", end.getValue(), TemporalType.TIMESTAMP);
				if (theSince != null) {
					q.setParameter("SINCE", theSince, TemporalType.TIMESTAMP);
				}

				int firstResult = Math.max(0, theFromIndex - offset);
				q.setFirstResult(firstResult);

				int maxResults = (theToIndex - theFromIndex) + 1;
				q.setMaxResults(maxResults);

				List<ResourceHistoryTable> results = q.getResultList();
				for (ResourceHistoryTable next : results) {
					if (retVal.size() == maxResults) {
						break;
					}
					retVal.add(toResource(myResourceType, next));
				}

				return retVal;
			}

			@Override
			public Integer preferredPageSize() {
				return null;
			}

			@Override
			public int size() {
				return count;
			}
		};

	}

	@Override
	public IBundleProvider history(Long theId, Date theSince) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(null, getResourceName());
		notifyInterceptors(RestOperationTypeEnum.HISTORY_TYPE, requestDetails);

		StopWatch w = new StopWatch();
		IBundleProvider retVal = super.history(myResourceName, theId, theSince);
		ourLog.info("Processed history on {} in {}ms", theId, w.getMillisAndRestart());
		return retVal;
	}

	private void loadResourcesByPid(Collection<Long> theIncludePids, List<IBaseResource> theResourceListToPopulate, Set<Long> theRevIncludedPids) {
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
			Class<? extends IBaseResource> resourceType = getContext().getResourceDefinition(next.getResourceType()).getImplementingClass();
			IResource resource = (IResource) toResource(resourceType, next);
			Integer index = position.get(next.getId());
			if (index == null) {
				ourLog.warn("Got back unexpected resource PID {}", next.getId());
				continue;
			}

			if (theRevIncludedPids.contains(next.getId())) {
				ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(resource, BundleEntrySearchModeEnum.INCLUDE);
			} else {
				ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(resource, BundleEntrySearchModeEnum.MATCH);
			}

			theResourceListToPopulate.set(index, resource);
		}
	}

	private Set<Long> loadReverseIncludes(List<Long> theMatches, Set<Include> theRevIncludes) {
		if (theMatches.size() == 0) {
			return Collections.emptySet();
		}

		HashSet<Long> pidsToInclude = new HashSet<Long>();

		for (Include nextInclude : theRevIncludes) {
			boolean matchAll = "*".equals(nextInclude.getValue());
			if (matchAll) {
				String sql = "SELECT r FROM ResourceLink r WHERE r.myTargetResourcePid IN (:target_pids)";
				TypedQuery<ResourceLink> q = myEntityManager.createQuery(sql, ResourceLink.class);
				q.setParameter("target_pids", theMatches);
				List<ResourceLink> results = q.getResultList();
				for (ResourceLink resourceLink : results) {
					pidsToInclude.add(resourceLink.getSourceResourcePid());
				}
			} else {
				int colonIdx = nextInclude.getValue().indexOf(':');
				if (colonIdx < 2) {
					continue;
				}
				String resType = nextInclude.getValue().substring(0, colonIdx);
				RuntimeResourceDefinition def = getContext().getResourceDefinition(resType);
				if (def == null) {
					ourLog.warn("Unknown resource type in _revinclude=" + nextInclude.getValue());
					continue;
				}

				String paramName = nextInclude.getValue().substring(colonIdx + 1);
				RuntimeSearchParam param = def.getSearchParam(paramName);
				if (param == null) {
					ourLog.warn("Unknown param name in _revinclude=" + nextInclude.getValue());
					continue;
				}

				for (String nextPath : param.getPathsSplit()) {
					String sql = "SELECT r FROM ResourceLink r WHERE r.mySourcePath = :src_path AND r.myTargetResourcePid IN (:target_pids)";
					TypedQuery<ResourceLink> q = myEntityManager.createQuery(sql, ResourceLink.class);
					q.setParameter("src_path", nextPath);
					q.setParameter("target_pids", theMatches);
					List<ResourceLink> results = q.getResultList();
					for (ResourceLink resourceLink : results) {
						pidsToInclude.add(resourceLink.getSourceResourcePid());
					}
				}
			}
		}

		theMatches.addAll(pidsToInclude);
		return pidsToInclude;
	}

	@Override
	public MetaDt metaAddOperation(IIdType theResourceId, MetaDt theMetaAdd) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theResourceId, getResourceName());
		notifyInterceptors(RestOperationTypeEnum.META_ADD, requestDetails);

		StopWatch w = new StopWatch();
		BaseHasResource entity = readEntity(theResourceId);
		if (entity == null) {
			throw new ResourceNotFoundException(theResourceId);
		}

		List<TagDefinition> tags = toTagList(theMetaAdd);

		//@formatter:off
		for (TagDefinition nextDef : tags) {
			
			boolean hasTag = false;
			for (BaseTag next : new ArrayList<BaseTag>(entity.getTags())) {
				if (ObjectUtil.equals(next.getTag().getTagType(), nextDef.getTagType()) && 
						ObjectUtil.equals(next.getTag().getSystem(), nextDef.getSystem()) && 
						ObjectUtil.equals(next.getTag().getCode(), nextDef.getCode())) {
					hasTag = true;
					break;
				}
			}

			if (!hasTag) {
				entity.setHasTags(true);
				
				TagDefinition def = getTag(nextDef.getTagType(), nextDef.getSystem(), nextDef.getCode(), nextDef.getDisplay());
				BaseTag newEntity = entity.addTag(def);
				myEntityManager.persist(newEntity);
			}
		}
		//@formatter:on

		myEntityManager.merge(entity);
		notifyWriteCompleted();
		ourLog.info("Processed metaAddOperation on {} in {}ms", new Object[] { theResourceId, w.getMillisAndRestart() });

		return metaGetOperation(theResourceId);
	}

	@Override
	public MetaDt metaDeleteOperation(IIdType theResourceId, MetaDt theMetaDel) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theResourceId, getResourceName());
		notifyInterceptors(RestOperationTypeEnum.META_DELETE, requestDetails);

		StopWatch w = new StopWatch();
		BaseHasResource entity = readEntity(theResourceId);
		if (entity == null) {
			throw new ResourceNotFoundException(theResourceId);
		}

		List<TagDefinition> tags = toTagList(theMetaDel);

		//@formatter:off
		for (TagDefinition nextDef : tags) {
			for (BaseTag next : new ArrayList<BaseTag>(entity.getTags())) {
				if (ObjectUtil.equals(next.getTag().getTagType(), nextDef.getTagType()) && 
						ObjectUtil.equals(next.getTag().getSystem(), nextDef.getSystem()) && 
						ObjectUtil.equals(next.getTag().getCode(), nextDef.getCode())) {
					myEntityManager.remove(next);
					entity.getTags().remove(next);
				}
			}
		}
		//@formatter:on

		if (entity.getTags().isEmpty()) {
			entity.setHasTags(false);
		}

		myEntityManager.merge(entity);

		ourLog.info("Processed metaDeleteOperation on {} in {}ms", new Object[] { theResourceId.getValue(), w.getMillisAndRestart() });

		return metaGetOperation(theResourceId);
	}

	@Override
	public MetaDt metaGetOperation() {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(null, getResourceName());
		notifyInterceptors(RestOperationTypeEnum.META, requestDetails);

		String sql = "SELECT d FROM TagDefinition d WHERE d.myId IN (SELECT DISTINCT t.myTagId FROM ResourceTag t WHERE t.myResourceType = :res_type)";
		TypedQuery<TagDefinition> q = myEntityManager.createQuery(sql, TagDefinition.class);
		q.setParameter("res_type", myResourceName);
		List<TagDefinition> tagDefinitions = q.getResultList();

		MetaDt retVal = super.toMetaDt(tagDefinitions);

		return retVal;
	}

	@Override
	public MetaDt metaGetOperation(IIdType theId) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theId, getResourceName());
		notifyInterceptors(RestOperationTypeEnum.META, requestDetails);

		Long pid = super.translateForcedIdToPid(theId);

		String sql = "SELECT d FROM TagDefinition d WHERE d.myId IN (SELECT DISTINCT t.myTagId FROM ResourceTag t WHERE t.myResourceType = :res_type AND t.myResourceId = :res_id)";
		TypedQuery<TagDefinition> q = myEntityManager.createQuery(sql, TagDefinition.class);
		q.setParameter("res_type", myResourceName);
		q.setParameter("res_id", pid);
		List<TagDefinition> tagDefinitions = q.getResultList();

		MetaDt retVal = super.toMetaDt(tagDefinitions);

		return retVal;
	}

	@PostConstruct
	public void postConstruct() {
		RuntimeResourceDefinition def = getContext().getResourceDefinition(myResourceType);
		myResourceName = def.getName();

		if (mySecondaryPrimaryKeyParamName != null) {
			RuntimeSearchParam sp = def.getSearchParam(mySecondaryPrimaryKeyParamName);
			if (sp == null) {
				throw new ConfigurationException("Unknown search param on resource[" + myResourceName + "] for secondary key[" + mySecondaryPrimaryKeyParamName + "]");
			}
			if (sp.getParamType() != RestSearchParameterTypeEnum.TOKEN) {
				throw new ConfigurationException("Search param on resource[" + myResourceName + "] for secondary key[" + mySecondaryPrimaryKeyParamName + "] is not a token type, only token is supported");
			}
		}

	}

	/**
	 * May be implemented by subclasses to validate resources prior to storage
	 * 
	 * @param theResource
	 *           The resource that is about to be stored
	 */
	protected void preProcessResourceForStorage(T theResource) {
		// nothing by default
	}

	@Override
	public T read(IIdType theId) {
		validateResourceTypeAndThrowIllegalArgumentException(theId);

		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theId, getResourceName());
		RestOperationTypeEnum operationType = theId.hasVersionIdPart() ? RestOperationTypeEnum.VREAD : RestOperationTypeEnum.READ;
		notifyInterceptors(operationType, requestDetails);

		StopWatch w = new StopWatch();
		BaseHasResource entity = readEntity(theId);
		validateResourceType(entity);

		T retVal = toResource(myResourceType, entity);

		InstantDt deleted = ResourceMetadataKeyEnum.DELETED_AT.get(retVal);
		if (deleted != null && !deleted.isEmpty()) {
			throw new ResourceGoneException("Resource was deleted at " + deleted.getValueAsString());
		}

		ourLog.info("Processed read on {} in {}ms", theId.getValue(), w.getMillisAndRestart());
		return retVal;
	}

	@Override
	public BaseHasResource readEntity(IIdType theId) {
		boolean checkForForcedId = true;

		BaseHasResource entity = readEntity(theId, checkForForcedId);

		return entity;
	}

	@Override
	public BaseHasResource readEntity(IIdType theId, boolean theCheckForForcedId) {
		validateResourceTypeAndThrowIllegalArgumentException(theId);

		Long pid = translateForcedIdToPid(theId);
		BaseHasResource entity = myEntityManager.find(ResourceTable.class, pid);
		if (theId.hasVersionIdPart()) {
			if (entity.getVersion() != Long.parseLong(theId.getVersionIdPart())) {
				entity = null;
			}
		}

		if (entity == null) {
			if (theId.hasVersionIdPart()) {
				TypedQuery<ResourceHistoryTable> q = myEntityManager.createQuery("SELECT t from ResourceHistoryTable t WHERE t.myResourceId = :RID AND t.myResourceType = :RTYP AND t.myResourceVersion = :RVER", ResourceHistoryTable.class);
				q.setParameter("RID", pid);
				q.setParameter("RTYP", myResourceName);
				q.setParameter("RVER", Long.parseLong(theId.getVersionIdPart()));
				entity = q.getSingleResult();
			}
			if (entity == null) {
				throw new ResourceNotFoundException(theId);
			}
		}

		validateResourceType(entity);

		if (theCheckForForcedId) {
			validateGivenIdIsAppropriateToRetrieveResource(theId, entity);
		}
		return entity;
	}

	private ResourceTable readEntityLatestVersion(IIdType theId) {
		ResourceTable entity = myEntityManager.find(ResourceTable.class, translateForcedIdToPid(theId));
		if (entity == null) {
			throw new ResourceNotFoundException(theId);
		}
		validateGivenIdIsAppropriateToRetrieveResource(theId, entity);
		return entity;
	}

	@Override
	public void removeTag(IIdType theId, TagTypeEnum theTagType, String theScheme, String theTerm) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theId, getResourceName());
		notifyInterceptors(RestOperationTypeEnum.DELETE_TAGS, requestDetails);

		StopWatch w = new StopWatch();
		BaseHasResource entity = readEntity(theId);
		if (entity == null) {
			throw new ResourceNotFoundException(theId);
		}

		//@formatter:off
		for (BaseTag next : new ArrayList<BaseTag>(entity.getTags())) {
			if (ObjectUtil.equals(next.getTag().getTagType(), theTagType) && 
					ObjectUtil.equals(next.getTag().getSystem(), theScheme) && 
					ObjectUtil.equals(next.getTag().getCode(), theTerm)) {
				myEntityManager.remove(next);
				entity.getTags().remove(next);
			}
		}
		//@formatter:on

		if (entity.getTags().isEmpty()) {
			entity.setHasTags(false);
		}

		myEntityManager.merge(entity);

		ourLog.info("Processed remove tag {}/{} on {} in {}ms", new Object[] { theScheme, theTerm, theId.getValue(), w.getMillisAndRestart() });
	}

	@Override
	public IBundleProvider search(Map<String, IQueryParameterType> theParams) {
		SearchParameterMap map = new SearchParameterMap();
		for (Entry<String, IQueryParameterType> nextEntry : theParams.entrySet()) {
			map.add(nextEntry.getKey(), (nextEntry.getValue()));
		}
		return search(map);
	}

	@Override
	public IBundleProvider search(final SearchParameterMap theParams) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(null, getResourceName());
		notifyInterceptors(RestOperationTypeEnum.SEARCH_TYPE, requestDetails);

		StopWatch w = new StopWatch();
		final InstantDt now = InstantDt.withCurrentTime();

		Set<Long> loadPids;
		if (theParams.isEmpty()) {
			loadPids = new HashSet<Long>();
			CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
			CriteriaQuery<Tuple> cq = builder.createTupleQuery();
			Root<ResourceTable> from = cq.from(ResourceTable.class);
			cq.multiselect(from.get("myId").as(Long.class));
			Predicate typeEquals = builder.equal(from.get("myResourceType"), myResourceName);
			Predicate notDeleted = builder.isNull(from.get("myDeleted"));
			cq.where(builder.and(typeEquals, notDeleted));

			TypedQuery<Tuple> query = myEntityManager.createQuery(cq);
			for (Tuple next : query.getResultList()) {
				loadPids.add(next.get(0, Long.class));
			}
		} else {
			loadPids = searchForIdsWithAndOr(theParams);
			if (loadPids.isEmpty()) {
				return new SimpleBundleProvider();
			}
		}

		// Handle _lastUpdated
		DateRangeParam lu = theParams.getLastUpdated();
		if (lu != null && (lu.getLowerBoundAsInstant() != null || lu.getUpperBoundAsInstant() != null)) {

			CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
			CriteriaQuery<Long> cq = builder.createQuery(Long.class);
			Root<ResourceTable> from = cq.from(ResourceTable.class);
			cq.select(from.get("myId").as(Long.class));

			Predicate predicateIds = (from.get("myId").in(loadPids));
			Predicate predicateLower = lu.getLowerBoundAsInstant() != null ? builder.greaterThanOrEqualTo(from.<Date> get("myUpdated"), lu.getLowerBoundAsInstant()) : null;
			Predicate predicateUpper = lu.getUpperBoundAsInstant() != null ? builder.lessThanOrEqualTo(from.<Date> get("myUpdated"), lu.getUpperBoundAsInstant()) : null;
			if (predicateLower != null && predicateUpper != null) {
				cq.where(predicateIds, predicateLower, predicateUpper);
			} else if (predicateLower != null) {
				cq.where(predicateIds, predicateLower);
			} else {
				cq.where(predicateIds, predicateUpper);
			}
			TypedQuery<Long> query = myEntityManager.createQuery(cq);
			loadPids.clear();
			for (Long next : query.getResultList()) {
				loadPids.add(next);
			}
		}

		// Handle sorting if any was provided
		final List<Long> pids;
		if (theParams.getSort() != null && isNotBlank(theParams.getSort().getParamName())) {
			List<Order> orders = new ArrayList<Order>();
			List<Predicate> predicates = new ArrayList<Predicate>();
			CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
			CriteriaQuery<Tuple> cq = builder.createTupleQuery();
			Root<ResourceTable> from = cq.from(ResourceTable.class);
			predicates.add(from.get("myId").in(loadPids));
			createSort(builder, from, theParams.getSort(), orders, predicates);
			if (orders.size() > 0) {
				Set<Long> originalPids = loadPids;
				loadPids = new LinkedHashSet<Long>();
				cq.multiselect(from.get("myId").as(Long.class));
				cq.where(predicates.toArray(new Predicate[0]));
				cq.orderBy(orders);

				TypedQuery<Tuple> query = myEntityManager.createQuery(cq);

				for (Tuple next : query.getResultList()) {
					loadPids.add(next.get(0, Long.class));
				}

				ourLog.info("Sort PID order is now: {}", loadPids);

				pids = new ArrayList<Long>(loadPids);

				// Any ressources which weren't matched by the sort get added to the bottom
				for (Long next : originalPids) {
					if (loadPids.contains(next) == false) {
						pids.add(next);
					}
				}

			} else {
				pids = new ArrayList<Long>(loadPids);
			}
		} else {
			pids = new ArrayList<Long>(loadPids);
		}

		// Load _revinclude resources
		final Set<Long> revIncludedPids;
		if (theParams.getRevIncludes() != null && theParams.getRevIncludes().isEmpty() == false) {
			revIncludedPids = loadReverseIncludes(pids, theParams.getRevIncludes());
		} else {
			revIncludedPids = Collections.emptySet();
		}

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

						// Execute the query and make sure we return distinct results
						List<IBaseResource> retVal = new ArrayList<IBaseResource>();
						loadResourcesByPid(pidsSubList, retVal, revIncludedPids);

						/*
						 * Load _include resources - Note that _revincludes are handled differently than _include ones, as
						 * they are counted towards the total count and paged, so they are loaded outside the bundle provider
						 */
						if (theParams.getIncludes() != null && theParams.getIncludes().isEmpty() == false) {
							Set<IIdType> previouslyLoadedPids = new HashSet<IIdType>();
							for (IBaseResource next : retVal) {
								previouslyLoadedPids.add(next.getIdElement().toUnqualifiedVersionless());
							}

							Set<IIdType> includePids = new HashSet<IIdType>();
							List<IBaseResource> resources = retVal;
							do {
								includePids.clear();

								FhirTerser t = getContext().newTerser();
								for (Include next : theParams.getIncludes()) {
									for (IBaseResource nextResource : resources) {
										RuntimeResourceDefinition def = getContext().getResourceDefinition(nextResource);
										List<Object> values = getIncludeValues(t, next, nextResource, def);

										for (Object object : values) {
											if (object == null) {
												continue;
											}
											if (!(object instanceof BaseResourceReferenceDt)) {
												throw new InvalidRequestException("Path '" + next.getValue() + "' produced non ResourceReferenceDt value: " + object.getClass());
											}
											BaseResourceReferenceDt rr = (BaseResourceReferenceDt) object;
											if (rr.getReference().isEmpty()) {
												continue;
											}
											if (rr.getReference().isLocal()) {
												continue;
											}

											IIdType nextId = rr.getReference().toUnqualified();
											if (!previouslyLoadedPids.contains(nextId)) {
												includePids.add(nextId);
												previouslyLoadedPids.add(nextId);
											}
										}
									}
								}

								resources = addResourcesAsIncludesById(retVal, includePids, resources);
							} while (includePids.size() > 0 && previouslyLoadedPids.size() < getConfig().getIncludeLimit());

							if (previouslyLoadedPids.size() >= getConfig().getIncludeLimit()) {
								OperationOutcome oo = new OperationOutcome();
								Issue issue = oo.addIssue();
								issue.getSeverity().setValue(OO_SEVERITY_WARN);
								issue.setDetails("Not all _include resources were actually included as the request surpassed the limit of " + getConfig().getIncludeLimit() + " resources");
								retVal.add(0, oo);
							}
						}

						return retVal;
					}

				});
			}

			@Override
			public Integer preferredPageSize() {
				return theParams.getCount();
			}

			@Override
			public int size() {
				return pids.size();
			}
		};

		ourLog.info("Processed search for {} on {} in {}ms", new Object[] { myResourceName, theParams, w.getMillisAndRestart() });

		return retVal;
	}

	@Override
	public IBundleProvider search(String theParameterName, IQueryParameterType theValue) {
		return search(Collections.singletonMap(theParameterName, theValue));
	}

	@Override
	public Set<Long> searchForIds(Map<String, IQueryParameterType> theParams) {
		SearchParameterMap map = new SearchParameterMap();
		for (Entry<String, IQueryParameterType> nextEntry : theParams.entrySet()) {
			map.add(nextEntry.getKey(), (nextEntry.getValue()));
		}
		return searchForIdsWithAndOr(map);
	}

	@Override
	public Set<Long> searchForIds(String theParameterName, IQueryParameterType theValue) {
		return searchForIds(Collections.singletonMap(theParameterName, theValue));
	}

	@Override
	public Set<Long> searchForIdsWithAndOr(SearchParameterMap theParams) {
		SearchParameterMap params = theParams;
		if (params == null) {
			params = new SearchParameterMap();
		}

		RuntimeResourceDefinition resourceDef = getContext().getResourceDefinition(myResourceType);

		Set<Long> pids = new HashSet<Long>();

		for (Entry<String, List<List<? extends IQueryParameterType>>> nextParamEntry : params.entrySet()) {
			String nextParamName = nextParamEntry.getKey();
			if (nextParamName.equals("_id")) {

				if (nextParamEntry.getValue().isEmpty()) {
					continue;
				} else if (nextParamEntry.getValue().size() > 1) {
					throw new InvalidRequestException("AND queries not supported for _id (Multiple instances of this param found)");
				} else {
					Set<Long> joinPids = new HashSet<Long>();
					List<? extends IQueryParameterType> nextValue = nextParamEntry.getValue().get(0);
					if (nextValue == null || nextValue.size() == 0) {
						continue;
					} else {
						for (IQueryParameterType next : nextValue) {
							String value = next.getValueAsQueryToken();
							IIdType valueId = new IdDt(value);
							try {
								long valueLong = translateForcedIdToPid(valueId);
								joinPids.add(valueLong);
							} catch (ResourceNotFoundException e) {
								// This isn't an error, just means no result found
							}
						}
						if (joinPids.isEmpty()) {
							continue;
						}
					}

					pids = addPredicateId(pids, joinPids);
					if (pids.isEmpty()) {
						return new HashSet<Long>();
					}

					if (pids.isEmpty()) {
						pids.addAll(joinPids);
					} else {
						pids.retainAll(joinPids);
					}
				}

			} else if (nextParamName.equals("_language")) {

				pids = addPredicateLanguage(pids, nextParamEntry.getValue());

			} else if (nextParamName.equals(Constants.PARAM_TAG) || nextParamName.equals(Constants.PARAM_PROFILE) || nextParamName.equals(Constants.PARAM_SECURITY)) {

				pids = addPredicateTag(pids, nextParamEntry.getValue(), nextParamName);

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
					}
				}
			}
		}

		return pids;
	}

	@SuppressWarnings("unchecked")
	@Required
	public void setResourceType(Class<? extends IResource> theTableType) {
		myResourceType = (Class<T>) theTableType;
	}

	/**
	 * If set, the given param will be treated as a secondary primary key, and multiple resources will not be able to
	 * share the same value.
	 */
	public void setSecondaryPrimaryKeyParamName(String theSecondaryPrimaryKeyParamName) {
		mySecondaryPrimaryKeyParamName = theSecondaryPrimaryKeyParamName;
	}

	private DaoMethodOutcome toMethodOutcome(final ResourceTable theEntity, IResource theResource) {
		DaoMethodOutcome outcome = new DaoMethodOutcome();
		outcome.setId(theEntity.getIdDt());
		outcome.setEntity(theEntity);
		outcome.setResource(theResource);
		if (theResource != null) {
			theResource.setId(theEntity.getIdDt());
			ResourceMetadataKeyEnum.UPDATED.put(theResource, theEntity.getUpdated());
		}
		return outcome;
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

	private IQueryParameterType toParameterType(RuntimeSearchParam theParam, String theValueAsQueryToken) {
		IQueryParameterType qp = toParameterType(theParam);

		qp.setValueAsQueryToken(null, theValueAsQueryToken);
		return qp;
	}

	private ArrayList<TagDefinition> toTagList(MetaDt theMeta) {
		ArrayList<TagDefinition> retVal = new ArrayList<TagDefinition>();

		for (CodingDt next : theMeta.getTag()) {
			retVal.add(new TagDefinition(TagTypeEnum.TAG, next.getSystem(), next.getCode(), next.getDisplay()));
		}
		for (CodingDt next : theMeta.getSecurity()) {
			retVal.add(new TagDefinition(TagTypeEnum.SECURITY_LABEL, next.getSystem(), next.getCode(), next.getDisplay()));
		}
		for (UriDt next : theMeta.getProfile()) {
			retVal.add(new TagDefinition(TagTypeEnum.PROFILE, BaseHapiFhirDao.NS_JPA_PROFILE, next.getValue(), null));
		}

		return retVal;
	}

	@Override
	public DaoMethodOutcome update(T theResource) {
		return update(theResource, null);
	}

	@Override
	public DaoMethodOutcome update(T theResource, String theMatchUrl) {
		return update(theResource, theMatchUrl, true);
	}

	@Override
	public DaoMethodOutcome update(T theResource, String theMatchUrl, boolean thePerformIndexing) {
		StopWatch w = new StopWatch();

		preProcessResourceForStorage(theResource);

		final ResourceTable entity;

		IIdType resourceId;
		if (isNotBlank(theMatchUrl)) {
			Set<Long> match = processMatchUrl(theMatchUrl, myResourceType);
			if (match.size() > 1) {
				String msg = getContext().getLocalizer().getMessage(BaseHapiFhirDao.class, "transactionOperationWithMultipleMatchFailure", "UPDATE", theMatchUrl, match.size());
				throw new PreconditionFailedException(msg);
			} else if (match.size() == 1) {
				Long pid = match.iterator().next();
				entity = myEntityManager.find(ResourceTable.class, pid);
				resourceId = entity.getIdDt();
			} else {
				return create(theResource, null, thePerformIndexing);
			}
		} else {
			resourceId = theResource.getId();
			if (resourceId == null || isBlank(resourceId.getIdPart())) {
				throw new InvalidRequestException("Can not update a resource with no ID");
			}
			try {
				entity = readEntityLatestVersion(resourceId);
			} catch (ResourceNotFoundException e) {
				if (Character.isDigit(theResource.getId().getIdPart().charAt(0))) {
					throw new InvalidRequestException(getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "failedToCreateWithClientAssignedNumericId", theResource.getId().getIdPart()));
				}
				return doCreate(theResource, null, thePerformIndexing);
			}
		}

		if (resourceId.hasVersionIdPart() && Long.parseLong(resourceId.getVersionIdPart()) != entity.getVersion()) {
			throw new InvalidRequestException("Trying to update " + resourceId + " but this is not the current version");
		}

		if (resourceId.hasResourceType() && !resourceId.getResourceType().equals(getResourceName())) {
			throw new UnprocessableEntityException("Invalid resource ID[" + entity.getIdDt().toUnqualifiedVersionless() + "] of type[" + entity.getResourceType() + "] - Does not match expected [" + getResourceName() + "]");
		}

		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(resourceId, getResourceName(), theResource);
		notifyInterceptors(RestOperationTypeEnum.UPDATE, requestDetails);

		// Perform update
		ResourceTable savedEntity = updateEntity(theResource, entity, true, null, thePerformIndexing, true);

		notifyWriteCompleted();

		DaoMethodOutcome outcome = toMethodOutcome(savedEntity, theResource).setCreated(false);

		String msg = getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "successfulCreate", outcome.getId(), w.getMillisAndRestart());
		outcome.setOperationOutcome(createInfoOperationOutcome(msg));

		ourLog.info(msg);
		return outcome;
	}

	private void validateGivenIdIsAppropriateToRetrieveResource(IIdType theId, BaseHasResource entity) {
		if (entity.getForcedId() != null) {
			if (theId.isIdPartValidLong()) {
				// This means that the resource with the given numeric ID exists, but it has a "forced ID", meaning that
				// as far as the outside world is concerned, the given ID doesn't exist (it's just an internal pointer
				// to the
				// forced ID)
				throw new ResourceNotFoundException(theId);
			}
		}
	}

	private void validateResourceType(BaseHasResource entity) {
		if (!myResourceName.equals(entity.getResourceType())) {
			throw new ResourceNotFoundException("Resource with ID " + entity.getIdDt().getIdPart() + " exists but it is not of type " + myResourceName + ", found resource of type " + entity.getResourceType());
		}
	}

	private void validateResourceTypeAndThrowIllegalArgumentException(IIdType theId) {
		if (theId.hasResourceType() && !theId.getResourceType().equals(myResourceName)) {
			throw new IllegalArgumentException("Incorrect resource type (" + theId.getResourceType() + ") for this DAO, wanted: " + myResourceName);
		}
	}

}
