package ca.uhn.fhir.jpa.dao;

import static org.apache.commons.lang3.StringUtils.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.RuntimeChildResourceDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.entity.BaseHasResource;
import ca.uhn.fhir.jpa.entity.BaseTag;
import ca.uhn.fhir.jpa.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.entity.ResourceHistoryTablePk;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.entity.ResourceLink;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.TagDefinition;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.valueset.SearchParamTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.QualifiedDateParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

@Transactional(propagation = Propagation.REQUIRED)
public class FhirResourceDao<T extends IResource> extends BaseFhirDao implements IFhirResourceDao<T> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDao.class);

	@PersistenceContext()
	private EntityManager myEntityManager;

	@Autowired
	private PlatformTransactionManager myPlatformTransactionManager;

	private String myResourceName;
	private Class<T> myResourceType;
	private String mySecondaryPrimaryKeyParamName;

	private Set<Long> addPredicateDate(Set<Long> thePids, List<IQueryParameterType> theOrParams) {
		if (theOrParams == null || theOrParams.isEmpty()) {
			return thePids;
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceIndexedSearchParamDate> from = cq.from(ResourceIndexedSearchParamDate.class);
		cq.select(from.get("myResourcePid").as(Long.class));

		List<Predicate> codePredicates = new ArrayList<Predicate>();
		for (IQueryParameterType nextOr : theOrParams) {
			IQueryParameterType params = nextOr;

			if (params instanceof QualifiedDateParam) {
				QualifiedDateParam id = (QualifiedDateParam) params;
				DateRangeParam range = new DateRangeParam(id);
				addPredicateDateFromRange(builder, from, codePredicates, range);
			} else if (params instanceof DateRangeParam) {
				DateRangeParam range = (DateRangeParam) params;
				addPredicateDateFromRange(builder, from, codePredicates, range);
			} else {
				throw new IllegalArgumentException("Invalid token type: " + params.getClass());
			}

		}

		Predicate masterCodePredicate = builder.or(codePredicates.toArray(new Predicate[0]));

		Predicate type = builder.equal(from.get("myResourceType"), myResourceName);
		if (thePids.size() > 0) {
			Predicate inPids = (from.get("myResourcePid").in(thePids));
			cq.where(builder.and(type, inPids, masterCodePredicate));
		} else {
			cq.where(builder.and(type, masterCodePredicate));
		}

		TypedQuery<Long> q = myEntityManager.createQuery(cq);
		return new HashSet<Long>(q.getResultList());
	}

	private void addPredicateDateFromRange(CriteriaBuilder builder, Root<ResourceIndexedSearchParamDate> from, List<Predicate> codePredicates, DateRangeParam range) {
		Date lowerBound = range.getLowerBoundAsInstant();
		Date upperBound = range.getUpperBoundAsInstant();

		Predicate lb = null;
		if (lowerBound != null) {
			Predicate gt = builder.greaterThanOrEqualTo(from.<Date> get("myValueLow"), lowerBound);
			Predicate gin = builder.isNull(from.get("myValueLow"));
			Predicate lbo = builder.or(gt, gin);

			Predicate lt = builder.greaterThanOrEqualTo(from.<Date> get("myValueHigh"), lowerBound);
			Predicate lin = builder.isNull(from.get("myValueHigh"));
			Predicate hbo = builder.or(lt, lin);

			lb = builder.and(lbo, hbo);
		}

		Predicate ub = null;
		if (upperBound != null) {
			Predicate gt = builder.lessThanOrEqualTo(from.<Date> get("myValueLow"), upperBound);
			Predicate gin = builder.isNull(from.get("myValueLow"));
			Predicate lbo = builder.or(gt, gin);

			Predicate lt = builder.lessThanOrEqualTo(from.<Date> get("myValueHigh"), upperBound);
			Predicate lin = builder.isNull(from.get("myValueHigh"));
			Predicate ubo = builder.or(lt, lin);

			ub = builder.and(ubo, lbo);
		}

		if (lb != null && ub != null) {
			codePredicates.add(builder.and(lb, ub));
		} else if (lb != null) {
			codePredicates.add(lb);
		} else {
			codePredicates.add(ub);
		}
	}

	private Set<Long> addPredicateQuantity(Set<Long> thePids, List<IQueryParameterType> theOrParams) {
		if (theOrParams == null || theOrParams.isEmpty()) {
			return thePids;
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceIndexedSearchParamNumber> from = cq.from(ResourceIndexedSearchParamNumber.class);
		cq.select(from.get("myResourcePid").as(Long.class));

		List<Predicate> codePredicates = new ArrayList<Predicate>();
		for (IQueryParameterType nextOr : theOrParams) {
			IQueryParameterType params = nextOr;

			if (params instanceof QuantityDt) {
				QuantityDt id = (QuantityDt) params;

				Predicate system;
				if (id.getSystem().isEmpty()) {
					system = builder.isNull(from.get("mySystem"));
				} else {
					system = builder.equal(from.get("mySystem"), id.getSystem().getValueAsString());
				}

				Predicate code;
				if (id.getCode().isEmpty()) {
					code = builder.isNull(from.get("myUnits"));
				} else {
					code = builder.equal(from.get("myUnits"), id.getUnits().getValueAsString());
				}

				Predicate num;
				if (id.getComparator().getValueAsEnum() == null) {
					num = builder.equal(from.get("myValue"), id.getValue().getValue());
				} else {
					switch (id.getComparator().getValueAsEnum()) {
					case GREATERTHAN:
						Expression<Number> path = from.get("myValue");
						Number value = id.getValue().getValue();
						num = builder.gt(path, value);
						break;
					case GREATERTHAN_OR_EQUALS:
						path = from.get("myValue");
						value = id.getValue().getValue();
						num = builder.ge(path, value);
						break;
					case LESSTHAN:
						path = from.get("myValue");
						value = id.getValue().getValue();
						num = builder.lt(path, value);
						break;
					case LESSTHAN_OR_EQUALS:
						path = from.get("myValue");
						value = id.getValue().getValue();
						num = builder.le(path, value);
						break;
					default:
						throw new IllegalStateException(id.getComparator().getValueAsString());
					}
				}

				Predicate singleCode = builder.and(system, code, num);
				codePredicates.add(singleCode);

			} else {
				throw new IllegalArgumentException("Invalid token type: " + params.getClass());
			}

		}

		Predicate masterCodePredicate = builder.or(codePredicates.toArray(new Predicate[0]));

		Predicate type = builder.equal(from.get("myResourceType"), myResourceName);
		if (thePids.size() > 0) {
			Predicate inPids = (from.get("myResourcePid").in(thePids));
			cq.where(builder.and(type, inPids, masterCodePredicate));
		} else {
			cq.where(builder.and(type, masterCodePredicate));
		}

		TypedQuery<Long> q = myEntityManager.createQuery(cq);
		return new HashSet<Long>(q.getResultList());
	}

	private Set<Long> addPredicateReference(String theParamName, Set<Long> thePids, List<IQueryParameterType> theOrParams) {
		assert theParamName.contains(".") == false;

		Set<Long> pidsToRetain = thePids;
		if (theOrParams == null || theOrParams.isEmpty()) {
			return pidsToRetain;
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceLink> from = cq.from(ResourceLink.class);
		cq.select(from.get("mySourceResourcePid").as(Long.class));

		List<Predicate> codePredicates = new ArrayList<Predicate>();

		for (IQueryParameterType nextOr : theOrParams) {
			IQueryParameterType params = nextOr;

			if (params instanceof ReferenceParam) {
				ReferenceParam ref = (ReferenceParam) params;

				String resourceId = ref.getValueAsQueryToken();
				if (resourceId.contains("/")) {
					IdDt dt = new IdDt(resourceId);
					resourceId = dt.getUnqualifiedId();
				}

				if (isBlank(ref.getChain())) {
					Long targetPid = Long.valueOf(resourceId);
					ourLog.info("Searching for resource link with target PID: {}", targetPid);
					Predicate eq = builder.equal(from.get("myTargetResourcePid"), targetPid);
					codePredicates.add(eq);
				} else {
					String chain = getContext().getResourceDefinition(myResourceType).getSearchParam(theParamName).getPath();
					BaseRuntimeChildDefinition def = getContext().newTerser().getDefinition(myResourceType, chain);
					if (!(def instanceof RuntimeChildResourceDefinition)) {
						throw new ConfigurationException("Property " + chain + " of type " + myResourceName + " is not a resource: " + def.getClass());
					}
					List<Class<? extends IResource>> resourceTypes;
					if (isBlank(ref.getResourceType())) {
						RuntimeChildResourceDefinition resDef = (RuntimeChildResourceDefinition) def;
						resourceTypes = resDef.getResourceTypes();
					} else {
						resourceTypes = new ArrayList<Class<? extends IResource>>();
						RuntimeResourceDefinition resDef = getContext().getResourceDefinition(ref.getResourceType());
						resourceTypes.add(resDef.getImplementingClass());
					}
					for (Class<? extends IResource> nextType : resourceTypes) {
						RuntimeResourceDefinition typeDef = getContext().getResourceDefinition(nextType);
						RuntimeSearchParam param = typeDef.getSearchParam(ref.getChain());
						if (param == null) {
							ourLog.debug("Type {} doesn't have search param {}", nextType.getSimpleName(), param);
							continue;
						}
						IFhirResourceDao<?> dao = getDao(nextType);
						if (dao == null) {
							ourLog.debug("Don't have a DAO for type {}", nextType.getSimpleName(), param);
							continue;
						}

						IQueryParameterType chainValue = toParameterType(param.getParamType(), resourceId);
						Set<Long> pids = dao.searchForIds(ref.getChain(), chainValue);
						if (pids.isEmpty()) {
							continue;
						}

						Predicate eq = from.get("myTargetResourcePid").in(pids);
						codePredicates.add(eq);

					}
				}

			} else {
				throw new IllegalArgumentException("Invalid token type: " + params.getClass());
			}

		}

		Predicate masterCodePredicate = builder.or(codePredicates.toArray(new Predicate[0]));

		RuntimeSearchParam param = getContext().getResourceDefinition(getResourceType()).getSearchParam(theParamName);
		String path = param.getPath();

		Predicate type = builder.equal(from.get("mySourcePath"), path);
		if (pidsToRetain.size() > 0) {
			Predicate inPids = (from.get("mySourceResourcePid").in(pidsToRetain));
			cq.where(builder.and(type, inPids, masterCodePredicate));
		} else {
			cq.where(builder.and(type, masterCodePredicate));
		}

		TypedQuery<Long> q = myEntityManager.createQuery(cq);
		return new HashSet<Long>(q.getResultList());
	}

	private Set<Long> addPredicateString(Set<Long> thePids, List<IQueryParameterType> theOrParams) {
		if (theOrParams == null || theOrParams.isEmpty()) {
			return thePids;
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceIndexedSearchParamString> from = cq.from(ResourceIndexedSearchParamString.class);
		cq.select(from.get("myResourcePid").as(Long.class));

		List<Predicate> codePredicates = new ArrayList<Predicate>();
		for (IQueryParameterType nextOr : theOrParams) {
			IQueryParameterType params = nextOr;

			String string;
			if (params instanceof IPrimitiveDatatype<?>) {
				IPrimitiveDatatype<?> id = (IPrimitiveDatatype<?>) params;
				string = id.getValueAsString();
			} else {
				throw new IllegalArgumentException("Invalid token type: " + params.getClass());
			}

			Predicate singleCode = builder.equal(from.get("myValueNormalized"), normalizeString(string));
			if (params instanceof StringParam && ((StringParam) params).isExact()) {
				Predicate exactCode = builder.equal(from.get("myValueExact"), string);
				singleCode = builder.and(singleCode, exactCode);
			}
			codePredicates.add(singleCode);
		}

		Predicate masterCodePredicate = builder.or(codePredicates.toArray(new Predicate[0]));

		Predicate type = builder.equal(from.get("myResourceType"), myResourceName);
		if (thePids.size() > 0) {
			Predicate inPids = (from.get("myResourcePid").in(thePids));
			cq.where(builder.and(type, inPids, masterCodePredicate));
		} else {
			cq.where(builder.and(type, masterCodePredicate));
		}

		TypedQuery<Long> q = myEntityManager.createQuery(cq);
		return new HashSet<Long>(q.getResultList());
	}

	private Set<Long> addPredicateToken(Set<Long> thePids, List<IQueryParameterType> theOrParams) {
		if (theOrParams == null || theOrParams.isEmpty()) {
			return thePids;
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceIndexedSearchParamToken> from = cq.from(ResourceIndexedSearchParamToken.class);
		cq.select(from.get("myResourcePid").as(Long.class));

		List<Predicate> codePredicates = new ArrayList<Predicate>();
		for (IQueryParameterType nextOr : theOrParams) {
			IQueryParameterType params = nextOr;

			String code;
			String system;
			if (params instanceof IdentifierDt) {
				IdentifierDt id = (IdentifierDt) params;
				system = id.getSystem().getValueAsString();
				code = id.getValue().getValue();
			} else if (params instanceof CodingDt) {
				CodingDt id = (CodingDt) params;
				system = id.getSystem().getValueAsString();
				code = id.getCode().getValue();
			} else {
				throw new IllegalArgumentException("Invalid token type: " + params.getClass());
			}

			ArrayList<Predicate> singleCodePredicates = (new ArrayList<Predicate>());
			if (system != null) {
				singleCodePredicates.add(builder.equal(from.get("mySystem"), system));
			}
			if (code != null) {
				singleCodePredicates.add(builder.equal(from.get("myValue"), code));
			}
			Predicate singleCode = builder.and(singleCodePredicates.toArray(new Predicate[0]));
			codePredicates.add(singleCode);
		}

		Predicate masterCodePredicate = builder.or(codePredicates.toArray(new Predicate[0]));

		Predicate type = builder.equal(from.get("myResourceType"), myResourceName);
		if (thePids.size() > 0) {
			Predicate inPids = (from.get("myResourcePid").in(thePids));
			cq.where(builder.and(type, inPids, masterCodePredicate));
		} else {
			cq.where(builder.and(type, masterCodePredicate));
		}

		TypedQuery<Long> q = myEntityManager.createQuery(cq);
		return new HashSet<Long>(q.getResultList());
	}

	@Override
	public void addTag(IdDt theId, String theScheme, String theTerm, String theLabel) {
		BaseHasResource entity = readEntity(theId);
		if (entity == null) {
			throw new ResourceNotFoundException(theId);
		}

		for (BaseTag next : new ArrayList<BaseTag>(entity.getTags())) {
			if (next.getTag().getScheme().equals(theScheme) && next.getTag().getTerm().equals(theTerm)) {
				return;
			}
		}

		TagDefinition def = getTag(theScheme, theTerm, theLabel);
		BaseTag newEntity = entity.addTag(def);

		myEntityManager.persist(newEntity);
		myEntityManager.merge(entity);
	}

	@Override
	public MethodOutcome create(final T theResource) {
		ResourceTable entity = new ResourceTable();
		entity.setResourceType(toResourceName(theResource));

		updateEntity(theResource, entity, false);

		MethodOutcome outcome = toMethodOutcome(entity);
		return outcome;
	}

	@Override
	public TagList getAllResourceTags() {
		return super.getTags(myResourceType, null);
	}

	public Class<T> getResourceType() {
		return myResourceType;
	}

	@Override
	public TagList getTags(IdDt theResourceId) {
		return super.getTags(myResourceType, theResourceId);
	}

	@Override
	public List<T> history() {
		return null;
	}

	@Override
	public List<IResource> history(Date theSince, Integer theLimit) {
		return super.history(myResourceName, null, theSince, theLimit);
	}

	@Override
	public List<T> history(IdDt theId) {
		ArrayList<T> retVal = new ArrayList<T>();

		String resourceType = getContext().getResourceDefinition(myResourceType).getName();
		TypedQuery<ResourceHistoryTable> q = myEntityManager.createQuery(ResourceHistoryTable.Q_GETALL, ResourceHistoryTable.class);
		q.setParameter("PID", theId.asLong());
		q.setParameter("RESTYPE", resourceType);

		// TypedQuery<ResourceHistoryTable> query =
		// myEntityManager.createQuery(criteriaQuery);
		List<ResourceHistoryTable> results = q.getResultList();
		for (ResourceHistoryTable next : results) {
			retVal.add(toResource(myResourceType, next));
		}

		try {
			retVal.add(read(theId.withoutVersion()));
		} catch (ResourceNotFoundException e) {
			// ignore
		}

		if (retVal.isEmpty()) {
			throw new ResourceNotFoundException(theId);
		}

		return retVal;
	}

	@Override
	public List<IResource> history(Long theId, Date theSince, Integer theLimit) {
		return super.history(myResourceName, theId, theSince, theLimit);
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
			if (sp.getParamType()!=SearchParamTypeEnum.TOKEN) {
				throw new ConfigurationException("Search param on resource[" + myResourceName + "] for secondary key[" + mySecondaryPrimaryKeyParamName + "] is not a token type, only token is supported");
			}
		}

	}

	@Override
	public T read(IdDt theId) {
		BaseHasResource entity = readEntity(theId);

		T retVal = toResource(myResourceType, entity);
		return retVal;
	}

	@Override
	public BaseHasResource readEntity(IdDt theId) {
		BaseHasResource entity = myEntityManager.find(ResourceTable.class, theId.asLong());
		if (theId.hasUnqualifiedVersionId()) {
			if (entity.getVersion() != theId.getUnqualifiedVersionIdAsLong()) {
				entity = null;
			}
		}

		if (entity == null) {
			if (theId.hasUnqualifiedVersionId()) {
				entity = myEntityManager.find(ResourceHistoryTable.class, new ResourceHistoryTablePk(myResourceName, theId.asLong(), theId.getUnqualifiedVersionIdAsLong()));
			}
			if (entity == null) {
				throw new ResourceNotFoundException(theId);
			}
		}
		return entity;
	}

	private ResourceTable readEntityLatestVersion(IdDt theId) {
		ResourceTable entity = myEntityManager.find(ResourceTable.class, theId.asLong());
		if (entity == null) {
			throw new ResourceNotFoundException(theId);
		}
		return entity;
	}

	@Override
	public void removeTag(IdDt theId, String theScheme, String theTerm) {
		BaseHasResource entity = readEntity(theId);
		if (entity == null) {
			throw new ResourceNotFoundException(theId);
		}

		for (BaseTag next : new ArrayList<BaseTag>(entity.getTags())) {
			if (next.getTag().getScheme().equals(theScheme) && next.getTag().getTerm().equals(theTerm)) {
				myEntityManager.remove(next);
				entity.getTags().remove(next);
			}
		}

		myEntityManager.merge(entity);
	}

	@Override
	public List<T> search(Map<String, IQueryParameterType> theParams) {
		SearchParameterMap map = new SearchParameterMap();
		for (Entry<String, IQueryParameterType> nextEntry : theParams.entrySet()) {
			map.put(nextEntry.getKey(), new ArrayList<List<IQueryParameterType>>());
			map.get(nextEntry.getKey()).add(Collections.singletonList(nextEntry.getValue()));
		}
		return search(map);
	}

	@Override
	public List<T> search(SearchParameterMap theParams) {

		Set<Long> pids;
		if (theParams.isEmpty()) {
			pids = null;
		} else {
			pids = searchForIdsWithAndOr(theParams);
			if (pids.isEmpty()) {
				return new ArrayList<T>();
			}
		}

		// Execute the query and make sure we return distinct results
		{
			CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
			CriteriaQuery<ResourceTable> cq = builder.createQuery(ResourceTable.class);
			Root<ResourceTable> from = cq.from(ResourceTable.class);
			cq.where(builder.equal(from.get("myResourceType"), getContext().getResourceDefinition(myResourceType).getName()));
			if (!theParams.isEmpty()) {
				cq.where(from.get("myId").in(pids));
			}
			TypedQuery<ResourceTable> q = myEntityManager.createQuery(cq);

			List<T> retVal = new ArrayList<T>();
			for (ResourceTable next : q.getResultList()) {
				T resource = toResource(myResourceType, next);
				retVal.add(resource);
			}
			return retVal;
		}
	}

	@Override
	public List<T> search(String theParameterName, IQueryParameterType theValue) {
		return search(Collections.singletonMap(theParameterName, theValue));
	}

	@Override
	public Set<Long> searchForIds(Map<String, IQueryParameterType> theParams) {
		Map<String, List<List<IQueryParameterType>>> map = new HashMap<String, List<List<IQueryParameterType>>>();
		for (Entry<String, IQueryParameterType> nextEntry : theParams.entrySet()) {
			map.put(nextEntry.getKey(), new ArrayList<List<IQueryParameterType>>());
			map.get(nextEntry.getKey()).add(Collections.singletonList(nextEntry.getValue()));
		}
		return searchForIdsWithAndOr(map);
	}

	@Override
	public Set<Long> searchForIds(String theParameterName, IQueryParameterType theValue) {
		return searchForIds(Collections.singletonMap(theParameterName, theValue));
	}

	@Override
	public Set<Long> searchForIdsWithAndOr(Map<String, List<List<IQueryParameterType>>> theParams) {
		Map<String, List<List<IQueryParameterType>>> params = theParams;
		if (params == null) {
			params = Collections.emptyMap();
		}

		RuntimeResourceDefinition resourceDef = getContext().getResourceDefinition(myResourceType);

		Set<Long> pids = new HashSet<Long>();

		for (Entry<String, List<List<IQueryParameterType>>> nextParamEntry : params.entrySet()) {
			String nextParamName = nextParamEntry.getKey();
			RuntimeSearchParam nextParamDef = resourceDef.getSearchParam(nextParamName);
			if (nextParamDef != null) {
				if (nextParamDef.getParamType() == SearchParamTypeEnum.TOKEN) {
					for (List<IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
						pids = addPredicateToken(pids, nextAnd);
						if (pids.isEmpty()) {
							return new HashSet<Long>();
						}
					}
				} else if (nextParamDef.getParamType() == SearchParamTypeEnum.STRING) {
					for (List<IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
						pids = addPredicateString(pids, nextAnd);
						if (pids.isEmpty()) {
							return new HashSet<Long>();
						}
					}
				} else if (nextParamDef.getParamType() == SearchParamTypeEnum.QUANTITY) {
					for (List<IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
						pids = addPredicateQuantity(pids, nextAnd);
						if (pids.isEmpty()) {
							return new HashSet<Long>();
						}
					}
				} else if (nextParamDef.getParamType() == SearchParamTypeEnum.DATE) {
					for (List<IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
						pids = addPredicateDate(pids, nextAnd);
						if (pids.isEmpty()) {
							return new HashSet<Long>();
						}
					}
				} else if (nextParamDef.getParamType() == SearchParamTypeEnum.REFERENCE) {
					for (List<IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
						pids = addPredicateReference(nextParamName, pids, nextAnd);
						if (pids.isEmpty()) {
							return new HashSet<Long>();
						}
					}
				} else {
					throw new IllegalArgumentException("Don't know how to handle parameter of type: " + nextParamDef.getParamType());
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
	 * If set, the given param will be treated as a secondary primary key, and multiple resources will not be able to share the same value.
	 */
	public void setSecondaryPrimaryKeyParamName(String theSecondaryPrimaryKeyParamName) {
		mySecondaryPrimaryKeyParamName = theSecondaryPrimaryKeyParamName;
	}

	private MethodOutcome toMethodOutcome(final ResourceTable entity) {
		MethodOutcome outcome = new MethodOutcome();
		outcome.setId(new IdDt(entity.getResourceType() + '/' + entity.getId() + '/' + Constants.PARAM_HISTORY + '/' + entity.getVersion()));
		outcome.setVersionId(new IdDt(entity.getVersion()));
		return outcome;
	}

	private IQueryParameterType toParameterType(SearchParamTypeEnum theParamType, String theValueAsQueryToken) {
		switch (theParamType) {
		case DATE:
			return new QualifiedDateParam(theValueAsQueryToken);
		case NUMBER:
			QuantityDt qt = new QuantityDt();
			qt.setValueAsQueryToken(null, theValueAsQueryToken);
			return qt;
		case QUANTITY:
			qt = new QuantityDt();
			qt.setValueAsQueryToken(null, theValueAsQueryToken);
			return qt;
		case STRING:
			StringDt st = new StringDt();
			st.setValueAsQueryToken(null, theValueAsQueryToken);
			return st;
		case TOKEN:
			IdentifierDt id = new IdentifierDt();
			id.setValueAsQueryToken(null, theValueAsQueryToken);
			return id;
		case COMPOSITE:
		case REFERENCE:
		default:
			throw new IllegalStateException("Don't know how to convert param type: " + theParamType);
		}
	}

	@Override
	public MethodOutcome update(final T theResource, final IdDt theId) {

		// TransactionTemplate template = new TransactionTemplate(myPlatformTransactionManager);
		// ResourceTable savedEntity = template.execute(new TransactionCallback<ResourceTable>() {
		// @Override
		// public ResourceTable doInTransaction(TransactionStatus theStatus) {
		// final ResourceTable entity = readEntity(theId);
		// return updateEntity(theResource, entity,true);
		// }
		// });

		final ResourceTable entity = readEntityLatestVersion(theId);
		ResourceTable savedEntity = updateEntity(theResource, entity, true);

		return toMethodOutcome(savedEntity);
	}

}
