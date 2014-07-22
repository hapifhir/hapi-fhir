package ca.uhn.fhir.jpa.dao;

import static org.apache.commons.lang3.StringUtils.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
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
import ca.uhn.fhir.context.RuntimeChildResourceDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.entity.BaseHasResource;
import ca.uhn.fhir.jpa.entity.BaseTag;
import ca.uhn.fhir.jpa.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.entity.ResourceLink;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.TagDefinition;
import ca.uhn.fhir.jpa.util.StopWatch;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.dstu.valueset.SearchParamTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.FhirTerser;
import example.QuickUsage.MyClientInterface;

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

	private Set<Long> addPredicateDate(String theParamName, Set<Long> thePids, List<? extends IQueryParameterType> theList) {
		if (theList == null || theList.isEmpty()) {
			return thePids;
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceIndexedSearchParamDate> from = cq.from(ResourceIndexedSearchParamDate.class);
		cq.select(from.get("myResourcePid").as(Long.class));

		List<Predicate> codePredicates = new ArrayList<Predicate>();
		for (IQueryParameterType nextOr : theList) {
			IQueryParameterType params = nextOr;

			if (params instanceof DateParam) {
				DateParam date = (DateParam) params;
				if (!date.isEmpty()) {
					DateRangeParam range = new DateRangeParam(date);
					addPredicateDateFromRange(builder, from, codePredicates, range);
				}
			} else if (params instanceof DateRangeParam) {
				DateRangeParam range = (DateRangeParam) params;
				addPredicateDateFromRange(builder, from, codePredicates, range);
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

	private void addPredicateDateFromRange(CriteriaBuilder builder, Root<ResourceIndexedSearchParamDate> from, List<Predicate> codePredicates, DateRangeParam range) {
		Date lowerBound = range.getLowerBoundAsInstant();
		Date upperBound = range.getUpperBoundAsInstant();

		Predicate lb = null;
		if (lowerBound != null) {
			Predicate gt = builder.greaterThanOrEqualTo(from.<Date> get("myValueLow"), lowerBound);
			Predicate lt = builder.greaterThanOrEqualTo(from.<Date> get("myValueHigh"), lowerBound);
			lb = builder.or(gt, lt);

			// Predicate gin = builder.isNull(from.get("myValueLow"));
			// Predicate lbo = builder.or(gt, gin);
			// Predicate lin = builder.isNull(from.get("myValueHigh"));
			// Predicate hbo = builder.or(lt, lin);
			// lb = builder.and(lbo, hbo);
		}

		Predicate ub = null;
		if (upperBound != null) {
			Predicate gt = builder.lessThanOrEqualTo(from.<Date> get("myValueLow"), upperBound);
			Predicate lt = builder.lessThanOrEqualTo(from.<Date> get("myValueHigh"), upperBound);
			ub = builder.or(gt, lt);

			// Predicate gin = builder.isNull(from.get("myValueLow"));
			// Predicate lbo = builder.or(gt, gin);
			// Predicate lin = builder.isNull(from.get("myValueHigh"));
			// Predicate ubo = builder.or(lt, lin);
			// ub = builder.and(ubo, lbo);

		}

		if (lb != null && ub != null) {
			codePredicates.add(builder.and(lb, ub));
		} else if (lb != null) {
			codePredicates.add(lb);
		} else {
			codePredicates.add(ub);
		}
	}

	private Set<Long> addPredicateId(String theParamName, Set<Long> theExistingPids, Set<Long> thePids) {
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

	// private Set<Long> addPredicateComposite(String theParamName, Set<Long> thePids, List<? extends IQueryParameterType> theList) {
	// }

	private Set<Long> addPredicateQuantity(String theParamName, Set<Long> thePids, List<? extends IQueryParameterType> theList) {
		if (theList == null || theList.isEmpty()) {
			return thePids;
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceIndexedSearchParamQuantity> from = cq.from(ResourceIndexedSearchParamQuantity.class);
		cq.select(from.get("myResourcePid").as(Long.class));

		List<Predicate> codePredicates = new ArrayList<Predicate>();
		for (IQueryParameterType nextOr : theList) {
			IQueryParameterType params = nextOr;

			String systemValue;
			String unitsValue;
			QuantityCompararatorEnum cmpValue;
			BigDecimal valueValue;
			boolean approx = false;

			if (params instanceof QuantityDt) {
				QuantityDt param = (QuantityDt) params;
				systemValue = param.getSystem().getValueAsString();
				unitsValue = param.getUnits().getValueAsString();
				cmpValue = param.getComparator().getValueAsEnum();
				valueValue = param.getValue().getValue();
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

			Predicate system;
			if (isBlank(systemValue)) {
				system = builder.isNull(from.get("mySystem"));
			} else {
				system = builder.equal(from.get("mySystem"), systemValue);
			}

			Predicate code;
			if (isBlank(unitsValue)) {
				code = builder.isNull(from.get("myUnits"));
			} else {
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

			Predicate singleCode = builder.and(system, code, num);
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

	private Set<Long> addPredicateReference(String theParamName, Set<Long> thePids, List<? extends IQueryParameterType> theList) {
		assert theParamName.contains(".") == false;

		Set<Long> pidsToRetain = thePids;
		if (theList == null || theList.isEmpty()) {
			return pidsToRetain;
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceLink> from = cq.from(ResourceLink.class);
		cq.select(from.get("mySourceResourcePid").as(Long.class));

		List<Predicate> codePredicates = new ArrayList<Predicate>();

		for (IQueryParameterType nextOr : theList) {
			IQueryParameterType params = nextOr;

			if (params instanceof ReferenceParam) {
				ReferenceParam ref = (ReferenceParam) params;

				String resourceId = ref.getValueAsQueryToken();
				if (resourceId.contains("/")) {
					IdDt dt = new IdDt(resourceId);
					resourceId = dt.getIdPart();
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

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceIndexedSearchParamString> from = cq.from(ResourceIndexedSearchParamString.class);
		cq.select(from.get("myResourcePid").as(Long.class));

		List<Predicate> codePredicates = new ArrayList<Predicate>();
		for (IQueryParameterType nextOr : theList) {
			IQueryParameterType params = nextOr;

			String rawSearchTerm;
			if (params instanceof TokenParam) {
				TokenParam id = (TokenParam) params;
				if (!id.isText()) {
					throw new IllegalStateException("Trying to process a text search on a non-text token parameter");
				}
				rawSearchTerm = id.getValue();
			} else if (params instanceof StringParam) {
				StringParam id = (StringParam) params;
				rawSearchTerm = id.getValue();
			} else if (params instanceof IPrimitiveDatatype<?>) {
				IPrimitiveDatatype<?> id = (IPrimitiveDatatype<?>) params;
				rawSearchTerm = id.getValueAsString();
			} else {
				throw new IllegalArgumentException("Invalid token type: " + params.getClass());
			}

			if (rawSearchTerm.length() > ResourceIndexedSearchParamString.MAX_LENGTH) {
				throw new InvalidRequestException("Parameter[" + theParamName + "] has length (" + rawSearchTerm.length() + ") that is longer than maximum allowed ("
						+ ResourceIndexedSearchParamString.MAX_LENGTH + "): " + rawSearchTerm);
			}

			String likeExpression = normalizeString(rawSearchTerm);
			likeExpression = likeExpression.replace("%", "[%]") + "%";

			Predicate singleCode = builder.like(from.get("myValueNormalized").as(String.class), likeExpression);
			if (params instanceof StringParam && ((StringParam) params).isExact()) {
				Predicate exactCode = builder.equal(from.get("myValueExact"), rawSearchTerm);
				singleCode = builder.and(singleCode, exactCode);
			}
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

	private Set<Long> addPredicateNumber(String theParamName, Set<Long> thePids, List<? extends IQueryParameterType> theList) {
		if (theList == null || theList.isEmpty()) {
			return thePids;
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceIndexedSearchParamNumber> from = cq.from(ResourceIndexedSearchParamNumber.class);
		cq.select(from.get("myResourcePid").as(Long.class));

		List<Predicate> codePredicates = new ArrayList<Predicate>();
		for (IQueryParameterType nextOr : theList) {
			IQueryParameterType params = nextOr;

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

	private Set<Long> addPredicateToken(String theParamName, Set<Long> thePids, List<? extends IQueryParameterType> theList) {
		if (theList == null || theList.isEmpty()) {
			return thePids;
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceIndexedSearchParamToken> from = cq.from(ResourceIndexedSearchParamToken.class);
		cq.select(from.get("myResourcePid").as(Long.class));

		List<Predicate> codePredicates = new ArrayList<Predicate>();
		for (IQueryParameterType nextOr : theList) {
			IQueryParameterType params = nextOr;

			String code;
			String system;
			if (params instanceof TokenParam) {
				TokenParam id = (TokenParam) params;
				if (id.isText()) {
					return addPredicateString(theParamName, thePids, theList);
				}
				system = id.getSystem();
				code = id.getValue();
			} else if (params instanceof IdentifierDt) {
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

			if (system != null && system.length() > ResourceIndexedSearchParamToken.MAX_LENGTH) {
				throw new InvalidRequestException("Parameter[" + theParamName + "] has system (" + system.length() + ") that is longer than maximum allowed ("
						+ ResourceIndexedSearchParamToken.MAX_LENGTH + "): " + system);
			}
			if (code != null && code.length() > ResourceIndexedSearchParamToken.MAX_LENGTH) {
				throw new InvalidRequestException("Parameter[" + theParamName + "] has code (" + code.length() + ") that is longer than maximum allowed (" + ResourceIndexedSearchParamToken.MAX_LENGTH
						+ "): " + code);
			}

			ArrayList<Predicate> singleCodePredicates = (new ArrayList<Predicate>());
			if (StringUtils.isNotBlank(system)) {
				singleCodePredicates.add(builder.equal(from.get("mySystem"), system));
			} else if (system == null) {
				// don't check the system
			} else {
				// If the system is "", we only match on null systems
				singleCodePredicates.add(builder.isNull(from.get("mySystem")));
			}
			if (StringUtils.isNotBlank(code)) {
				singleCodePredicates.add(builder.equal(from.get("myValue"), code));
			} else {
				singleCodePredicates.add(builder.isNull(from.get("myValue")));
			}
			Predicate singleCode = builder.and(singleCodePredicates.toArray(new Predicate[0]));
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

	@Override
	public void addTag(IdDt theId, String theScheme, String theTerm, String theLabel) {
		StopWatch w = new StopWatch();
		BaseHasResource entity = readEntity(theId);
		if (entity == null) {
			throw new ResourceNotFoundException(theId);
		}

		for (BaseTag next : new ArrayList<BaseTag>(entity.getTags())) {
			if (next.getTag().getScheme().equals(theScheme) && next.getTag().getTerm().equals(theTerm)) {
				return;
			}
		}

		entity.setHasTags(true);

		TagDefinition def = getTag(theScheme, theTerm, theLabel);
		BaseTag newEntity = entity.addTag(def);

		myEntityManager.persist(newEntity);
		myEntityManager.merge(entity);
		notifyWriteCompleted();
		ourLog.info("Processed addTag {}/{} on {} in {}ms", new Object[] { theScheme, theTerm, theId, w.getMillisAndRestart() });
	}

	@Override
	public MethodOutcome create(final T theResource) {
		StopWatch w = new StopWatch();
		ResourceTable entity = new ResourceTable();
		entity.setResourceType(toResourceName(theResource));

		if (theResource.getId().isEmpty() == false) {
			if (isValidPid(theResource.getId())) {
				throw new UnprocessableEntityException(
						"This server cannot create an entity with a user-specified numeric ID - Client should not specify an ID when creating a new resource, or should include at least one letter in the ID to force a client-defined ID");
			}
			createForcedIdIfNeeded(entity, theResource.getId());

			if (entity.getForcedId() != null) {
				try {
					translateForcedIdToPid(theResource.getId());
					throw new UnprocessableEntityException("Can not create entity with ID[" + theResource.getId().getValue() + "], constraint violation occurred");
				} catch (ResourceNotFoundException e) {
					// good, this ID doesn't exist so we can create it
				}
			}

		}

		updateEntity(theResource, entity, false, false);

		MethodOutcome outcome = toMethodOutcome(entity);
		notifyWriteCompleted();
		ourLog.info("Processed create on {} in {}ms", myResourceName, w.getMillisAndRestart());
		return outcome;
	}

	@Override
	public MethodOutcome delete(IdDt theId) {
		StopWatch w = new StopWatch();
		final ResourceTable entity = readEntityLatestVersion(theId);
		if (theId.hasVersionIdPart() && theId.getVersionIdPartAsLong().longValue() != entity.getVersion()) {
			throw new InvalidRequestException("Trying to update " + theId + " but this is not the current version");
		}

		ResourceTable savedEntity = updateEntity(null, entity, true, true);

		notifyWriteCompleted();

		ourLog.info("Processed delete on {} in {}ms", theId.getValue(), w.getMillisAndRestart());
		return toMethodOutcome(savedEntity);
	}

	@Override
	public TagList getAllResourceTags() {
		StopWatch w = new StopWatch();
		TagList tags = super.getTags(myResourceType, null);
		ourLog.info("Processed getTags on {} in {}ms", myResourceName, w.getMillisAndRestart());
		return tags;
	}

	public Class<T> getResourceType() {
		return myResourceType;
	}

	@Override
	public TagList getTags(IdDt theResourceId) {
		StopWatch w = new StopWatch();
		TagList retVal = super.getTags(myResourceType, theResourceId);
		ourLog.info("Processed getTags on {} in {}ms", theResourceId, w.getMillisAndRestart());
		return retVal;
	}

	@Override
	public IBundleProvider history(Date theSince) {
		StopWatch w = new StopWatch();
		IBundleProvider retVal = super.history(myResourceName, null, theSince);
		ourLog.info("Processed history on {} in {}ms", myResourceName, w.getMillisAndRestart());
		return retVal;
	}

	@Override
	public IBundleProvider history(final IdDt theId, final Date theSince) {
		final InstantDt end = createHistoryToTimestamp();
		final String resourceType = getContext().getResourceDefinition(myResourceType).getName();

		T currentTmp;
		try {
			currentTmp = read(theId.toVersionless());
			if (ResourceMetadataKeyEnum.UPDATED.get(currentTmp).after(end.getValue())) {
				currentTmp = null;
			}
		} catch (ResourceNotFoundException e) {
			currentTmp = null;
		}

		final T current = currentTmp;

		String querySring = "SELECT count(h) FROM ResourceHistoryTable h " + "WHERE h.myResourceId = :PID AND h.myResourceType = :RESTYPE" + " AND h.myUpdated < :END"
				+ (theSince != null ? " AND h.myUpdated >= :SINCE" : "");
		TypedQuery<Long> countQuery = myEntityManager.createQuery(querySring, Long.class);
		countQuery.setParameter("PID", theId.getIdPartAsLong());
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
			public List<IResource> getResources(int theFromIndex, int theToIndex) {
				ArrayList<IResource> retVal = new ArrayList<IResource>();
				if (theFromIndex == 0 && current != null) {
					retVal.add(current);
				}

				TypedQuery<ResourceHistoryTable> q = myEntityManager.createQuery(
						"SELECT h FROM ResourceHistoryTable h WHERE h.myResourceId = :PID AND h.myResourceType = :RESTYPE AND h.myUpdated < :END "
								+ (theSince != null ? " AND h.myUpdated >= :SINCE" : "") + " ORDER BY h.myUpdated ASC", ResourceHistoryTable.class);
				q.setParameter("PID", theId.getIdPartAsLong());
				q.setParameter("RESTYPE", resourceType);
				q.setParameter("END", end.getValue(), TemporalType.TIMESTAMP);
				if (theSince != null) {
					q.setParameter("SINCE", theSince, TemporalType.TIMESTAMP);
				}

				q.setFirstResult(Math.max(0, theFromIndex - offset));
				q.setMaxResults(theToIndex - theFromIndex);

				List<ResourceHistoryTable> results = q.getResultList();
				for (ResourceHistoryTable next : results) {
					if (retVal.size() == (theToIndex - theFromIndex)) {
						break;
					}
					retVal.add(toResource(myResourceType, next));
				}

				return retVal;
			}

			@Override
			public int size() {
				return count;
			}
		};

	}

	@Override
	public IBundleProvider history(Long theId, Date theSince) {
		StopWatch w = new StopWatch();
		IBundleProvider retVal = super.history(myResourceName, theId, theSince);
		ourLog.info("Processed history on {} in {}ms", theId, w.getMillisAndRestart());
		return retVal;
	}

	private void loadResourcesByPid(Collection<Long> theIncludePids, List<IResource> theResourceListToPopulate) {
		if (theIncludePids.isEmpty()) {
			return;
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<ResourceTable> cq = builder.createQuery(ResourceTable.class);
		Root<ResourceTable> from = cq.from(ResourceTable.class);
		cq.where(builder.equal(from.get("myResourceType"), getContext().getResourceDefinition(myResourceType).getName()));
		if (theIncludePids != null) {
			cq.where(from.get("myId").in(theIncludePids));
		}
		TypedQuery<ResourceTable> q = myEntityManager.createQuery(cq);

		for (ResourceTable next : q.getResultList()) {
			T resource = toResource(myResourceType, next);
			theResourceListToPopulate.add(resource);
		}
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
			if (sp.getParamType() != SearchParamTypeEnum.TOKEN) {
				throw new ConfigurationException("Search param on resource[" + myResourceName + "] for secondary key[" + mySecondaryPrimaryKeyParamName
						+ "] is not a token type, only token is supported");
			}
		}

	}

	@Override
	public T read(IdDt theId) {
		validateResourceTypeAndThrowIllegalArgumentException(theId);

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

	private void validateResourceType(BaseHasResource entity) {
		if (!myResourceName.equals(entity.getResourceType())) {
			throw new ResourceNotFoundException("Resource with ID " + entity.getIdDt().getIdPart() + " exists but it is not of type " + myResourceName + ", found resource of type "
					+ entity.getResourceType());
		}
	}

	private void validateResourceTypeAndThrowIllegalArgumentException(IdDt theId) {
		if (theId.hasResourceType() && !theId.getResourceType().equals(myResourceName)) {
			throw new IllegalArgumentException("Incorrect resource type (" + theId.getResourceType() + ") for this DAO, wanted: " + myResourceName);
		}
	}

	@Override
	public BaseHasResource readEntity(IdDt theId) {
		validateResourceTypeAndThrowIllegalArgumentException(theId);

		Long pid = translateForcedIdToPid(theId);
		BaseHasResource entity = myEntityManager.find(ResourceTable.class, pid);
		if (theId.hasVersionIdPart()) {
			if (entity.getVersion() != theId.getVersionIdPartAsLong()) {
				entity = null;
			}
		}

		if (entity == null) {
			if (theId.hasVersionIdPart()) {
				TypedQuery<ResourceHistoryTable> q = myEntityManager.createQuery(
						"SELECT t from ResourceHistoryTable t WHERE t.myResourceId = :RID AND t.myResourceType = :RTYP AND t.myResourceVersion = :RVER", ResourceHistoryTable.class);
				q.setParameter("RID", theId.getIdPartAsLong());
				q.setParameter("RTYP", myResourceName);
				q.setParameter("RVER", theId.getVersionIdPartAsLong());
				entity = q.getSingleResult();
			}
			if (entity == null) {
				throw new ResourceNotFoundException(theId);
			}
		}

		validateResourceType(entity);

		return entity;
	}

	private ResourceTable readEntityLatestVersion(IdDt theId) {
		ResourceTable entity = myEntityManager.find(ResourceTable.class, translateForcedIdToPid(theId));
		if (entity == null) {
			throw new ResourceNotFoundException(theId);
		}
		return entity;
	}

	@Override
	public void removeTag(IdDt theId, String theScheme, String theTerm) {
		StopWatch w = new StopWatch();
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
		StopWatch w = new StopWatch();
		final InstantDt now = InstantDt.withCurrentTime();

		Set<Long> loadPids;
		if (theParams.isEmpty()) {
			loadPids = new HashSet<Long>();
			CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
			CriteriaQuery<Tuple> cq = builder.createTupleQuery();
			Root<ResourceTable> from = cq.from(ResourceTable.class);
			cq.multiselect(from.get("myId").as(Long.class));
			cq.where(builder.equal(from.get("myResourceType"), myResourceName));

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

		final ArrayList<Long> pids = new ArrayList<Long>(loadPids);

		IBundleProvider retVal = new IBundleProvider() {

			@Override
			public InstantDt getPublished() {
				return now;
			}

			@Override
			public List<IResource> getResources(final int theFromIndex, final int theToIndex) {
				TransactionTemplate template = new TransactionTemplate(myPlatformTransactionManager);
				return template.execute(new TransactionCallback<List<IResource>>() {
					@Override
					public List<IResource> doInTransaction(TransactionStatus theStatus) {
						List<Long> pidsSubList = pids.subList(theFromIndex, theToIndex);

						// Execute the query and make sure we return distinct results
						List<IResource> retVal = new ArrayList<IResource>();
						loadResourcesByPid(pidsSubList, retVal);

						// Load _include resources
						if (theParams.getIncludes() != null && theParams.getIncludes().isEmpty() == false) {
							Set<IdDt> previouslyLoadedPids = new HashSet<IdDt>();

							Set<IdDt> includePids = new HashSet<IdDt>();
							List<IResource> resources = retVal;
							do {
								includePids.clear();

								FhirTerser t = getContext().newTerser();
								for (Include next : theParams.getIncludes()) {
									for (IResource nextResource : resources) {
										RuntimeResourceDefinition def = getContext().getResourceDefinition(nextResource);
										if (!next.getValue().startsWith(def.getName() + ".")) {
											continue;
										}
										
										List<Object> values = t.getValues(nextResource, next.getValue());
										for (Object object : values) {
											if (object == null) {
												continue;
											}
											if (!(object instanceof ResourceReferenceDt)) {
												throw new InvalidRequestException("Path '" + next.getValue() + "' produced non ResourceReferenceDt value: " + object.getClass());
											}
											ResourceReferenceDt rr = (ResourceReferenceDt) object;
											if (rr.getReference().isEmpty()) {
												continue;
											}
											if (rr.getReference().isLocal()) {
												continue;
											}

											IdDt nextId = rr.getReference().toUnqualified();
											if (!previouslyLoadedPids.contains(nextId)) {
												includePids.add(nextId);
												previouslyLoadedPids.add(nextId);
											}
										}
									}
								}

								if (!includePids.isEmpty()) {
									ourLog.info("Loading {} included resources", includePids.size());
									resources = loadResourcesById(includePids);
									retVal.addAll(resources);
								}
							} while (includePids.size() > 0 && previouslyLoadedPids.size() < getConfig().getIncludeLimit());
							
							if (previouslyLoadedPids.size() >= getConfig().getIncludeLimit()) {
								OperationOutcome oo = new OperationOutcome();
								oo.addIssue().setSeverity(IssueSeverityEnum.WARNING).setDetails("Not all _include resources were actually included as the request surpassed the limit of " + getConfig().getIncludeLimit() + " resources");
								retVal.add(0, oo);
							}
						}

						
						return retVal;
					}
				});
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
							IdDt valueId = new IdDt(value);
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

					pids = addPredicateId(nextParamName, pids, joinPids);
					if (pids.isEmpty()) {
						return new HashSet<Long>();
					}

					if (pids.isEmpty()) {
						pids.addAll(joinPids);
					} else {
						pids.retainAll(joinPids);
					}

				}
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
						throw new IllegalArgumentException("Don't know how to handle parameter of type: " + nextParamDef.getParamType());
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
	 * If set, the given param will be treated as a secondary primary key, and multiple resources will not be able to share the same value.
	 */
	public void setSecondaryPrimaryKeyParamName(String theSecondaryPrimaryKeyParamName) {
		mySecondaryPrimaryKeyParamName = theSecondaryPrimaryKeyParamName;
	}

	private MethodOutcome toMethodOutcome(final ResourceTable entity) {
		MethodOutcome outcome = new MethodOutcome();
		outcome.setId(entity.getIdDt());
		return outcome;
	}

	private IQueryParameterType toParameterType(SearchParamTypeEnum theParamType, String theValueAsQueryToken) {
		switch (theParamType) {
		case DATE:
			return new DateParam(theValueAsQueryToken);
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
		StopWatch w = new StopWatch();

		// TransactionTemplate template = new TransactionTemplate(myPlatformTransactionManager);
		// ResourceTable savedEntity = template.execute(new TransactionCallback<ResourceTable>() {
		// @Override
		// public ResourceTable doInTransaction(TransactionStatus theStatus) {
		// final ResourceTable entity = readEntity(theId);
		// return updateEntity(theResource, entity,true);
		// }
		// });

		final ResourceTable entity = readEntityLatestVersion(theId);
		if (theId.hasVersionIdPart() && theId.getVersionIdPartAsLong().longValue() != entity.getVersion()) {
			throw new InvalidRequestException("Trying to update " + theId + " but this is not the current version");
		}

		ResourceTable savedEntity = updateEntity(theResource, entity, true, false);

		notifyWriteCompleted();
		ourLog.info("Processed update on {} in {}ms", theId.getValue(), w.getMillisAndRestart());
		return toMethodOutcome(savedEntity);
	}

}
