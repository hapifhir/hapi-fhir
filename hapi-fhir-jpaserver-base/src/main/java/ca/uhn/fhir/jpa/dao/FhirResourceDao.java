package ca.uhn.fhir.jpa.dao;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.text.Normalizer;
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
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
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
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeChildResourceDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.entity.BaseHasResource;
import ca.uhn.fhir.jpa.entity.BaseResourceTable;
import ca.uhn.fhir.jpa.entity.BaseTag;
import ca.uhn.fhir.jpa.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.entity.ResourceLink;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.SearchParamTypeEnum;
import ca.uhn.fhir.model.primitive.BaseDateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.QualifiedDateParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParameter;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.FhirTerser;

public class FhirResourceDao<T extends IResource, X extends BaseResourceTable<T>> implements IFhirResourceDao<T> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDao.class);
	private FhirContext myCtx;

	@PersistenceContext(name = "FHIR_UT", type = PersistenceContextType.TRANSACTION, unitName = "FHIR_UT")
	private EntityManager myEntityManager;

	@Autowired
	private PlatformTransactionManager myPlatformTransactionManager;
	@Autowired
	private List<IFhirResourceDao<?>> myResourceDaos;
	private String myResourceName;
	private Class<T> myResourceType;
	private Map<Class<? extends IResource>, IFhirResourceDao<?>> myResourceTypeToDao;

	private Class<X> myTableType;

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
		Predicate singleCode;
		Date lowerBound = range.getLowerBoundAsInstant();
		Date upperBound = range.getUpperBoundAsInstant();

		if (lowerBound != null && upperBound != null) {
			Predicate low = builder.greaterThanOrEqualTo(from.<Date> get("myValueLow"), lowerBound);
			Predicate high = builder.lessThanOrEqualTo(from.<Date> get("myValueHigh"), upperBound);
			singleCode = builder.and(low, high);
		} else if (lowerBound != null) {
			singleCode = builder.greaterThanOrEqualTo(from.<Date> get("myValueLow"), lowerBound);
		} else {
			singleCode = builder.lessThanOrEqualTo(from.<Date> get("myValueHigh"), upperBound);
		}

		codePredicates.add(singleCode);
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

				if (isBlank(ref.getChain())) {
					Long targetPid = Long.valueOf(ref.getValueAsQueryToken());
					ourLog.info("Searching for resource link with target PID: {}", targetPid);
					Predicate eq = builder.equal(from.get("myTargetResourcePid"), targetPid);
					codePredicates.add(eq);
				} else {
					// TODO: handle chain with resource type
					String chain = myCtx.getResourceDefinition(myResourceType).getSearchParam(theParamName).getPath();
					BaseRuntimeChildDefinition def = myCtx.newTerser().getDefinition(myResourceType, chain);
					if (!(def instanceof RuntimeChildResourceDefinition)) {
						throw new ConfigurationException("Property " + chain + " of type " + myResourceName + " is not a resource: " + def.getClass());
					}
					List<Class<? extends IResource>> resourceTypes;
					if (ref.getType() == null) {
						RuntimeChildResourceDefinition resDef = (RuntimeChildResourceDefinition) def;
						resourceTypes = resDef.getResourceTypes();
					} else {
						resourceTypes = new ArrayList<>();
						resourceTypes.add(ref.getType());
					}
					for (Class<? extends IResource> nextType : resourceTypes) {
						RuntimeResourceDefinition typeDef = myCtx.getResourceDefinition(nextType);
						RuntimeSearchParam param = typeDef.getSearchParam(ref.getChain());
						if (param == null) {
							ourLog.debug("Type {} doesn't have search param {}", nextType.getSimpleName(), param);
							continue;
						}
						IFhirResourceDao<?> dao = getResourceTypeToDao().get(nextType);
						if (dao == null) {
							ourLog.debug("Don't have a DAO for type {}", nextType.getSimpleName(), param);
							continue;
						}

						IQueryParameterType chainValue = toParameterType(param.getParamType(), ref.getValueAsQueryToken());
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

		Predicate type = builder.equal(from.get("mySourcePath"), myResourceName + "." + theParamName);
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
			if (params instanceof StringParameter && ((StringParameter) params).isExact()) {
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

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	@Override
	public MethodOutcome create(T theResource) {

		final X entity = toEntity(theResource);

		entity.setPublished(new Date());
		entity.setUpdated(entity.getPublished());

		final List<ResourceIndexedSearchParamString> stringParams = extractSearchParamStrings(entity, theResource);
		final List<ResourceIndexedSearchParamToken> tokenParams = extractSearchParamTokens(entity, theResource);
		final List<ResourceIndexedSearchParamNumber> numberParams = extractSearchParamNumber(entity, theResource);
		final List<ResourceIndexedSearchParamDate> dateParams = extractSearchParamDates(entity, theResource);
		final List<ResourceLink> links = extractResourceLinks(entity, theResource);

		ourLog.info("Saving links: {}", links);

		TransactionTemplate template = new TransactionTemplate(myPlatformTransactionManager);
		template.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
		template.setReadOnly(false);
		template.execute(new TransactionCallback<X>() {
			@Override
			public X doInTransaction(TransactionStatus theStatus) {
				myEntityManager.persist(entity);
				for (ResourceIndexedSearchParamString next : stringParams) {
					myEntityManager.persist(next);
				}
				for (ResourceIndexedSearchParamToken next : tokenParams) {
					myEntityManager.persist(next);
				}
				for (ResourceIndexedSearchParamNumber next : numberParams) {
					myEntityManager.persist(next);
				}
				for (ResourceIndexedSearchParamDate next : dateParams) {
					myEntityManager.persist(next);
				}
				for (ResourceLink next : links) {
					myEntityManager.persist(next);
				}
				return entity;
			}
		});

		MethodOutcome outcome = toMethodOutcome(entity);
		return outcome;
	}

	private List<ResourceLink> extractResourceLinks(X theEntity, T theResource) {
		ArrayList<ResourceLink> retVal = new ArrayList<ResourceLink>();

		RuntimeResourceDefinition def = myCtx.getResourceDefinition(theResource);
		FhirTerser t = myCtx.newTerser();
		for (RuntimeSearchParam nextSpDef : def.getSearchParams()) {
			if (nextSpDef.getParamType() != SearchParamTypeEnum.REFERENCE) {
				continue;
			}

			String nextPath = nextSpDef.getPath();

			boolean multiType = false;
			if (nextPath.endsWith("[x]")) {
				multiType = true;
			}

			List<Object> values = t.getValues(theResource, nextPath);
			for (Object nextObject : values) {
				if (nextObject == null) {
					continue;
				}

				ResourceLink nextEntity;
				if (nextObject instanceof ResourceReferenceDt) {
					ResourceReferenceDt nextValue = (ResourceReferenceDt) nextObject;
					if (nextValue.isEmpty()) {
						continue;
					}

					Class<? extends IResource> type = nextValue.getResourceType();
					String id = nextValue.getResourceId();
					if (StringUtils.isBlank(id)) {
						continue;
					}

					Map<Class<? extends IResource>, IFhirResourceDao<?>> resourceTypeToDao = getResourceTypeToDao();
					Class<? extends BaseResourceTable<?>> tableType = resourceTypeToDao.get(type).getTableType();
					Long valueOf;
					try {
						valueOf = Long.valueOf(id);
					} catch (Exception e) {
						String resName = myCtx.getResourceDefinition(type).getName();
						throw new InvalidRequestException("Resource ID " + resName + "/" + id + " is invalid (must be numeric), specified in path: " + nextPath);
					}
					BaseResourceTable<?> target = myEntityManager.find(tableType, valueOf);
					if (target == null) {
						String resName = myCtx.getResourceDefinition(type).getName();
						throw new InvalidRequestException("Resource " + resName + "/" + id + " not found, specified in path: " + nextPath);
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

		return retVal;
	}

	private List<ResourceIndexedSearchParamDate> extractSearchParamDates(X theEntity, T theResource) {
		ArrayList<ResourceIndexedSearchParamDate> retVal = new ArrayList<ResourceIndexedSearchParamDate>();

		RuntimeResourceDefinition def = myCtx.getResourceDefinition(theResource);
		FhirTerser t = myCtx.newTerser();
		for (RuntimeSearchParam nextSpDef : def.getSearchParams()) {
			if (nextSpDef.getParamType() != SearchParamTypeEnum.DATE) {
				continue;
			}

			String nextPath = nextSpDef.getPath();

			boolean multiType = false;
			if (nextPath.endsWith("[x]")) {
				multiType = true;
			}

			List<Object> values = t.getValues(theResource, nextPath);
			for (Object nextObject : values) {
				if (nextObject == null) {
					continue;
				}

				ResourceIndexedSearchParamDate nextEntity;
				if (nextObject instanceof BaseDateTimeDt) {
					BaseDateTimeDt nextValue = (BaseDateTimeDt) nextObject;
					if (nextValue.isEmpty()) {
						continue;
					}
					nextEntity = new ResourceIndexedSearchParamDate(nextSpDef.getName(), nextValue.getValue(), nextValue.getValue());
				} else {
					if (!multiType) {
						throw new ConfigurationException("Search param " + nextSpDef.getName() + " is of unexpected datatype: " + nextObject.getClass());
					} else {
						continue;
					}
				}
				if (nextEntity != null) {
					nextEntity.setResource(theEntity, def.getName());
					retVal.add(nextEntity);
				}
			}
		}

		return retVal;
	}

	private ArrayList<ResourceIndexedSearchParamNumber> extractSearchParamNumber(X theEntity, T theResource) {
		ArrayList<ResourceIndexedSearchParamNumber> retVal = new ArrayList<ResourceIndexedSearchParamNumber>();

		RuntimeResourceDefinition def = myCtx.getResourceDefinition(theResource);
		FhirTerser t = myCtx.newTerser();
		for (RuntimeSearchParam nextSpDef : def.getSearchParams()) {
			if (nextSpDef.getParamType() != SearchParamTypeEnum.NUMBER && nextSpDef.getParamType() != SearchParamTypeEnum.QUANTITY) {
				continue;
			}

			String nextPath = nextSpDef.getPath();
			List<Object> values = t.getValues(theResource, nextPath);
			for (Object nextObject : values) {
				if (nextObject == null || ((IDatatype) nextObject).isEmpty()) {
					continue;
				}

				String resourceName = nextSpDef.getName();
				boolean multiType = false;
				if (nextPath.endsWith("[x]")) {
					multiType = true;
				}

				if (nextObject instanceof QuantityDt) {
					QuantityDt nextValue = (QuantityDt) nextObject;
					ResourceIndexedSearchParamNumber nextEntity = new ResourceIndexedSearchParamNumber(resourceName, nextValue.getValue().getValue(), nextValue.getSystem().getValueAsString(),
							nextValue.getUnits().getValue());
					nextEntity.setResource(theEntity, def.getName());
					retVal.add(nextEntity);
				} else {
					if (!multiType) {
						throw new ConfigurationException("Search param " + resourceName + " is of unexpected datatype: " + nextObject.getClass());
					} else {
						continue;
					}
				}
			}
		}

		return retVal;
	}

	private List<ResourceIndexedSearchParamString> extractSearchParamStrings(X theEntity, T theResource) {
		ArrayList<ResourceIndexedSearchParamString> retVal = new ArrayList<ResourceIndexedSearchParamString>();

		RuntimeResourceDefinition def = myCtx.getResourceDefinition(theResource);
		FhirTerser t = myCtx.newTerser();
		for (RuntimeSearchParam nextSpDef : def.getSearchParams()) {
			if (nextSpDef.getParamType() != SearchParamTypeEnum.STRING) {
				continue;
			}
			if (nextSpDef.getPath().isEmpty()) {
				continue; // TODO: implement phoenetic, and any others that have
							// no path
			}

			String nextPath = nextSpDef.getPath();
			List<Object> values = t.getValues(theResource, nextPath);
			for (Object nextObject : values) {
				if (nextObject == null || ((IDatatype) nextObject).isEmpty()) {
					continue;
				}

				String resourceName = nextSpDef.getName();
				boolean multiType = false;
				if (nextPath.endsWith("[x]")) {
					multiType = true;
				}

				if (nextObject instanceof IPrimitiveDatatype<?>) {
					IPrimitiveDatatype<?> nextValue = (IPrimitiveDatatype<?>) nextObject;
					ResourceIndexedSearchParamString nextEntity = new ResourceIndexedSearchParamString(resourceName, normalizeString(nextValue.getValueAsString()), nextValue.getValueAsString());
					nextEntity.setResource(theEntity, def.getName());
					retVal.add(nextEntity);
				} else if (nextObject instanceof HumanNameDt) {
					ArrayList<StringDt> allNames = new ArrayList<>();
					allNames.addAll(((HumanNameDt) nextObject).getFamily());
					allNames.addAll(((HumanNameDt) nextObject).getGiven());
					for (StringDt nextName : allNames) {
						if (nextName.isEmpty()) {
							continue;
						}
						ResourceIndexedSearchParamString nextEntity = new ResourceIndexedSearchParamString(resourceName, normalizeString(nextName.getValueAsString()), nextName.getValueAsString());
						nextEntity.setResource(theEntity, def.getName());
						retVal.add(nextEntity);
					}
				} else {
					if (!multiType) {
						throw new ConfigurationException("Search param " + resourceName + " is of unexpected datatype: " + nextObject.getClass());
					}
				}
			}
		}

		return retVal;
	}

	private String normalizeString(String theString) {
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

	private List<ResourceIndexedSearchParamToken> extractSearchParamTokens(X theEntity, T theResource) {
		ArrayList<ResourceIndexedSearchParamToken> retVal = new ArrayList<ResourceIndexedSearchParamToken>();

		RuntimeResourceDefinition def = myCtx.getResourceDefinition(theResource);
		FhirTerser t = myCtx.newTerser();
		for (RuntimeSearchParam nextSpDef : def.getSearchParams()) {
			if (nextSpDef.getParamType() != SearchParamTypeEnum.TOKEN) {
				continue;
			}

			String nextPath = nextSpDef.getPath();
			if (nextPath.isEmpty()) {
				continue;
			}

			boolean multiType = false;
			if (nextPath.endsWith("[x]")) {
				multiType = true;
			}

			List<Object> values = t.getValues(theResource, nextPath);
			for (Object nextObject : values) {
				ResourceIndexedSearchParamToken nextEntity;
				if (nextObject instanceof IdentifierDt) {
					IdentifierDt nextValue = (IdentifierDt) nextObject;
					if (nextValue.isEmpty()) {
						continue;
					}
					nextEntity = new ResourceIndexedSearchParamToken(nextSpDef.getName(), nextValue.getSystem().getValueAsString(), nextValue.getValue().getValue());
				} else if (nextObject instanceof IPrimitiveDatatype<?>) {
					IPrimitiveDatatype<?> nextValue = (IPrimitiveDatatype<?>) nextObject;
					if (nextValue.isEmpty()) {
						continue;
					}
					nextEntity = new ResourceIndexedSearchParamToken(nextSpDef.getName(), null, nextValue.getValueAsString());
				} else if (nextObject instanceof CodeableConceptDt) {
					CodeableConceptDt nextCC = (CodeableConceptDt) nextObject;
					for (CodingDt nextCoding : nextCC.getCoding()) {
						if (nextCoding.isEmpty()) {
							continue;
						}
						nextEntity = new ResourceIndexedSearchParamToken(nextSpDef.getName(), nextCoding.getSystem().getValueAsString(), nextCoding.getCode().getValue());
						nextEntity.setResource(theEntity, def.getName());
						retVal.add(nextEntity);
					}
					nextEntity = null;
				} else {
					if (!multiType) {
						throw new ConfigurationException("Search param " + nextSpDef.getName() + " is of unexpected datatype: " + nextObject.getClass());
					} else {
						continue;
					}
				}
				if (nextEntity != null) {
					nextEntity.setResource(theEntity, def.getName());
					retVal.add(nextEntity);
				}
			}
		}

		return retVal;
	}

	public Class<T> getResourceType() {
		return myResourceType;
	}

	private Map<Class<? extends IResource>, IFhirResourceDao<?>> getResourceTypeToDao() {
		if (myResourceTypeToDao == null) {
			myResourceTypeToDao = new HashMap<>();
			for (IFhirResourceDao<?> next : myResourceDaos) {
				myResourceTypeToDao.put(next.getResourceType(), next);
			}
		}

		Map<Class<? extends IResource>, IFhirResourceDao<?>> resourceTypeToDao = myResourceTypeToDao;
		return resourceTypeToDao;
	}

	@Override
	public Class<X> getTableType() {
		return myTableType;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public List<T> history(IdDt theId) {
		ArrayList<T> retVal = new ArrayList<T>();

		String resourceType = myCtx.getResourceDefinition(myResourceType).getName();
		TypedQuery<ResourceHistoryTable> q = myEntityManager.createQuery(ResourceHistoryTable.Q_GETALL, ResourceHistoryTable.class);
		q.setParameter("PID", theId.asLong());
		q.setParameter("RESTYPE", resourceType);

		// TypedQuery<ResourceHistoryTable> query =
		// myEntityManager.createQuery(criteriaQuery);
		List<ResourceHistoryTable> results = q.getResultList();
		for (ResourceHistoryTable next : results) {
			retVal.add(toResource(next));
		}

		try {
			retVal.add(read(theId));
		} catch (ResourceNotFoundException e) {
			// ignore
		}

		if (retVal.isEmpty()) {
			throw new ResourceNotFoundException(theId);
		}

		return retVal;
	}

	private void populateResourceIntoEntity(T theResource, X retVal) {
		retVal.setResource(myCtx.newJsonParser().encodeResourceToString(theResource));
		retVal.setEncoding(EncodingEnum.JSON);

		TagList tagList = (TagList) theResource.getResourceMetadata().get(ResourceMetadataKeyEnum.TAG_LIST);
		if (tagList != null) {
			for (Tag next : tagList) {
				retVal.addTag(next.getTerm(), next.getLabel(), next.getScheme());
			}
		}

	}

	@PostConstruct
	public void postConstruct() throws Exception {
		myResourceType = myTableType.newInstance().getResourceType();
		myCtx = new FhirContext(myResourceType);
		myResourceName = myCtx.getResourceDefinition(myResourceType).getName();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public T read(IdDt theId) {
		X entity = readEntity(theId);

		T retVal = toResource(entity);
		return retVal;
	}

	private X readEntity(IdDt theId) {
		X entity = (X) myEntityManager.find(myTableType, theId.asLong());
		if (entity == null) {
			throw new ResourceNotFoundException(theId);
		}
		return entity;
	}

	@Override
	public List<T> search(Map<String, IQueryParameterType> theParams) {
		Map<String, List<List<IQueryParameterType>>> map = new HashMap<String, List<List<IQueryParameterType>>>();
		for (Entry<String, IQueryParameterType> nextEntry : theParams.entrySet()) {
			map.put(nextEntry.getKey(), new ArrayList<List<IQueryParameterType>>());
			map.get(nextEntry.getKey()).add(Collections.singletonList(nextEntry.getValue()));
		}
		return searchWithAndOr(map);
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
	public Set<Long> searchForIdsWithAndOr(Map<String, List<List<IQueryParameterType>>> theParams) {
		Map<String, List<List<IQueryParameterType>>> params = theParams;
		if (params == null) {
			params = Collections.emptyMap();
		}

		RuntimeResourceDefinition resourceDef = myCtx.getResourceDefinition(myResourceType);

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

	@Override
	public Set<Long> searchForIds(String theParameterName, IQueryParameterType theValue) {
		return searchForIds(Collections.singletonMap(theParameterName, theValue));
	}

	@Override
	public List<T> searchWithAndOr(Map<String, List<List<IQueryParameterType>>> theParams) {

		Set<Long> pids;
		if (theParams.isEmpty()) {
			pids = null;
		} else {
			pids = searchForIdsWithAndOr(theParams);
			if (pids.isEmpty()) {
				return new ArrayList<>();
			}
		}

		// Execute the query and make sure we return distinct results
		{
			CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
			CriteriaQuery<X> cq = builder.createQuery(myTableType);
			Root<X> from = cq.from(myTableType);
			if (!theParams.isEmpty()) {
				cq.where(from.get("myId").in(pids));
			}
			TypedQuery<X> q = myEntityManager.createQuery(cq);

			List<T> retVal = new ArrayList<>();
			for (X next : q.getResultList()) {
				T resource = toResource(next);
				retVal.add(resource);
			}
			return retVal;
		}
	}

	@Required
	public void setTableType(Class<X> theTableType) {
		myTableType = theTableType;
	}

	private X toEntity(T theResource) {
		X retVal;
		try {
			retVal = myTableType.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new InternalErrorException(e);
		}

		populateResourceIntoEntity(theResource, retVal);

		return retVal;
	}

	private MethodOutcome toMethodOutcome(final X entity) {
		MethodOutcome outcome = new MethodOutcome();
		outcome.setId(entity.getId());
		outcome.setVersionId(entity.getVersion());
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

	private T toResource(BaseHasResource theEntity) {
		String resourceText = theEntity.getResource();
		IParser parser = theEntity.getEncoding().newParser(myCtx);
		T retVal = parser.parseResource(myResourceType, resourceText);
		retVal.setId(theEntity.getId());
		retVal.getResourceMetadata().put(ResourceMetadataKeyEnum.VERSION_ID, theEntity.getVersion());
		retVal.getResourceMetadata().put(ResourceMetadataKeyEnum.PUBLISHED, theEntity.getPublished());
		retVal.getResourceMetadata().put(ResourceMetadataKeyEnum.UPDATED, theEntity.getUpdated());
		if (theEntity.getTags().size() > 0) {
			TagList tagList = new TagList();
			for (BaseTag next : theEntity.getTags()) {
				tagList.add(new Tag(next.getTerm(), next.getLabel(), next.getScheme()));
			}
			retVal.getResourceMetadata().put(ResourceMetadataKeyEnum.TAG_LIST, tagList);
		}
		return retVal;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public MethodOutcome update(final T theResource, final IdDt theId) {
		TransactionTemplate template = new TransactionTemplate(myPlatformTransactionManager);
		X savedEntity = template.execute(new TransactionCallback<X>() {
			@Override
			public X doInTransaction(TransactionStatus theStatus) {
				final X entity = readEntity(theId);
				final ResourceHistoryTable existing = entity.toHistory(myCtx);

				populateResourceIntoEntity(theResource, entity);
				myEntityManager.persist(existing);

				entity.setUpdated(new Date());
				myEntityManager.persist(entity);
				return entity;
			}
		});

		return toMethodOutcome(savedEntity);
	}

}
