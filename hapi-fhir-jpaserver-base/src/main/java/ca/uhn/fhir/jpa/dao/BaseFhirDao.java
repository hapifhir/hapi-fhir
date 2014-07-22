package ca.uhn.fhir.jpa.dao;

import static org.apache.commons.lang3.StringUtils.*;

import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.measure.quantity.Quantity;
import javax.measure.unit.NonSI;
import javax.measure.unit.Unit;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.entity.BaseHasResource;
import ca.uhn.fhir.jpa.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.entity.BaseTag;
import ca.uhn.fhir.jpa.entity.ForcedId;
import ca.uhn.fhir.jpa.entity.ResourceEncodingEnum;
import ca.uhn.fhir.jpa.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.entity.ResourceHistoryTag;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.entity.ResourceLink;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.ResourceTag;
import ca.uhn.fhir.jpa.entity.TagDefinition;
import ca.uhn.fhir.jpa.util.StopWatch;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.composite.AddressDt;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.composite.ContactDt;
import ca.uhn.fhir.model.dstu.composite.DurationDt;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.PeriodDt;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.SearchParamTypeEnum;
import ca.uhn.fhir.model.primitive.BaseDateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.FhirTerser;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public abstract class BaseFhirDao implements IDao {

	public static final String UCUM_NS = "http://unitsofmeasure.org";

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseFhirDao.class);

	@Autowired(required = true)
	private DaoConfig myConfig;

	@Autowired(required = true)
	private FhirContext myContext;

	@PersistenceContext(name = "FHIR_UT", type = PersistenceContextType.TRANSACTION, unitName = "FHIR_UT")
	private EntityManager myEntityManager;
	private List<IDaoListener> myListeners = new ArrayList<IDaoListener>();

	@Autowired
	private List<IFhirResourceDao<?>> myResourceDaos;

	private Map<Class<? extends IResource>, IFhirResourceDao<?>> myResourceTypeToDao;

	protected void notifyWriteCompleted() {
		for (IDaoListener next : myListeners) {
			next.writeCompleted();
		}
	}

	public FhirContext getContext() {
		return myContext;
	}

	@Override
	public void registerDaoListener(IDaoListener theListener) {
		Validate.notNull(theListener, "theListener");
		myListeners.add(theListener);
	}

	public void setContext(FhirContext theContext) {
		myContext = theContext;
	}

	private void findMatchingTagIds(String theResourceName, IdDt theResourceId, Set<Long> tagIds, Class<? extends BaseTag> entityClass) {
		{
			CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
			CriteriaQuery<Tuple> cq = builder.createTupleQuery();
			Root<? extends BaseTag> from = cq.from(entityClass);
			cq.multiselect(from.get("myTagId").as(Long.class)).distinct(true);

			if (theResourceName != null) {
				Predicate typePredicate = builder.equal(from.get("myResourceType"), theResourceName);
				if (theResourceId != null) {
					cq.where(typePredicate, builder.equal(from.get("myResourceId"), translateForcedIdToPid(theResourceId)));
				} else {
					cq.where(typePredicate);
				}
			}

			TypedQuery<Tuple> query = myEntityManager.createQuery(cq);
			for (Tuple next : query.getResultList()) {
				tagIds.add(next.get(0, Long.class));
			}
		}
	}

	private void searchHistoryCurrentVersion(List<HistoryTuple> theTuples, List<BaseHasResource> theRetVal) {
		Collection<HistoryTuple> tuples = Collections2.filter(theTuples, new com.google.common.base.Predicate<HistoryTuple>() {
			@Override
			public boolean apply(HistoryTuple theInput) {
				return theInput.isHistory() == false;
			}
		});
		Collection<Long> ids = Collections2.transform(tuples, new Function<HistoryTuple, Long>() {
			@Override
			public Long apply(HistoryTuple theInput) {
				return theInput.getId();
			}
		});
		if (ids.isEmpty()) {
			return;
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<ResourceTable> cq = builder.createQuery(ResourceTable.class);
		Root<ResourceTable> from = cq.from(ResourceTable.class);
		cq.where(from.get("myId").in(ids));

		cq.orderBy(builder.desc(from.get("myUpdated")));
		TypedQuery<ResourceTable> q = myEntityManager.createQuery(cq);
		for (ResourceTable next : q.getResultList()) {
			theRetVal.add(next);
		}
	}

	private void searchHistoryCurrentVersion(String theResourceName, Long theId, Date theSince, Date theEnd, Integer theLimit, List<HistoryTuple> tuples) {
		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = builder.createTupleQuery();
		Root<?> from = cq.from(ResourceTable.class);
		cq.multiselect(from.get("myId").as(Long.class), from.get("myUpdated").as(Date.class));

		List<Predicate> predicates = new ArrayList<Predicate>();
		if (theSince != null) {
			Predicate low = builder.greaterThanOrEqualTo(from.<Date> get("myUpdated"), theSince);
			predicates.add(low);
		}

		Predicate high = builder.lessThan(from.<Date> get("myUpdated"), theEnd);
		predicates.add(high);

		if (theResourceName != null) {
			predicates.add(builder.equal(from.get("myResourceType"), theResourceName));
		}
		if (theId != null) {
			predicates.add(builder.equal(from.get("myId"), theId));
		}

		cq.where(builder.and(predicates.toArray(new Predicate[0])));

		cq.orderBy(builder.desc(from.get("myUpdated")));
		TypedQuery<Tuple> q = myEntityManager.createQuery(cq);
		if (theLimit != null && theLimit < myConfig.getHardSearchLimit()) {
			q.setMaxResults(theLimit);
		} else {
			q.setMaxResults(myConfig.getHardSearchLimit());
		}
		for (Tuple next : q.getResultList()) {
			long id = next.get(0, Long.class);
			Date updated = next.get(1, Date.class);
			tuples.add(new HistoryTuple(false, updated, id));
		}
	}

	private void searchHistoryHistory(List<HistoryTuple> theTuples, List<BaseHasResource> theRetVal) {
		Collection<HistoryTuple> tuples = Collections2.filter(theTuples, new com.google.common.base.Predicate<HistoryTuple>() {
			@Override
			public boolean apply(HistoryTuple theInput) {
				return theInput.isHistory() == true;
			}
		});
		Collection<Long> ids = Collections2.transform(tuples, new Function<HistoryTuple, Long>() {
			@Override
			public Long apply(HistoryTuple theInput) {
				return (Long) theInput.getId();
			}
		});
		if (ids.isEmpty()) {
			return;
		}

		ourLog.info("Retrieving {} history elements from ResourceHistoryTable", ids.size());

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<ResourceHistoryTable> cq = builder.createQuery(ResourceHistoryTable.class);
		Root<ResourceHistoryTable> from = cq.from(ResourceHistoryTable.class);
		cq.where(from.get("myId").in(ids));

		cq.orderBy(builder.desc(from.get("myUpdated")));
		TypedQuery<ResourceHistoryTable> q = myEntityManager.createQuery(cq);
		for (ResourceHistoryTable next : q.getResultList()) {
			theRetVal.add(next);
		}
	}

	private void searchHistoryHistory(String theResourceName, Long theResourceId, Date theSince, Date theEnd, Integer theLimit, List<HistoryTuple> tuples) {
		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = builder.createTupleQuery();
		Root<?> from = cq.from(ResourceHistoryTable.class);
		cq.multiselect(from.get("myId").as(Long.class), from.get("myUpdated").as(Date.class));

		List<Predicate> predicates = new ArrayList<Predicate>();
		if (theSince != null) {
			Predicate low = builder.greaterThanOrEqualTo(from.<Date> get("myUpdated"), theSince);
			predicates.add(low);
		}

		Predicate high = builder.lessThan(from.<Date> get("myUpdated"), theEnd);
		predicates.add(high);

		if (theResourceName != null) {
			predicates.add(builder.equal(from.get("myResourceType"), theResourceName));
		}
		if (theResourceId != null) {
			predicates.add(builder.equal(from.get("myResourceId"), theResourceId));
		}

		cq.where(builder.and(predicates.toArray(new Predicate[0])));

		cq.orderBy(builder.desc(from.get("myUpdated")));
		TypedQuery<Tuple> q = myEntityManager.createQuery(cq);
		if (theLimit != null && theLimit < myConfig.getHardSearchLimit()) {
			q.setMaxResults(theLimit);
		} else {
			q.setMaxResults(myConfig.getHardSearchLimit());
		}
		for (Tuple next : q.getResultList()) {
			Long id = next.get(0, Long.class);
			Date updated = (Date) next.get(1);
			tuples.add(new HistoryTuple(true, updated, id));
		}
	}

	protected List<ResourceLink> extractResourceLinks(ResourceTable theEntity, IResource theResource) {
		ArrayList<ResourceLink> retVal = new ArrayList<ResourceLink>();

		RuntimeResourceDefinition def = getContext().getResourceDefinition(theResource);
		FhirTerser t = getContext().newTerser();
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
					if (nextValue.getReference().getValue().startsWith("#")) {
						// This is a contained resource reference
						continue;
					}

					String typeString = nextValue.getReference().getResourceType();
					if (isBlank(typeString)) {
						throw new InvalidRequestException("Invalid resource reference found at path[" + nextPath + "] - Does not contain resource type - " + nextValue.getReference().getValue());
					}
					Class<? extends IResource> type = getContext().getResourceDefinition(typeString).getImplementingClass();
					String id = nextValue.getReference().getIdPart();
					if (StringUtils.isBlank(id)) {
						continue;
					}

					IFhirResourceDao<?> dao = getDao(type);
					if (dao == null) {
						throw new InvalidRequestException("This server is not able to handle resources of type: " + nextValue.getReference().getResourceType());
					}
					Long valueOf;
					try {
						valueOf = translateForcedIdToPid(nextValue.getReference());
					} catch (Exception e) {
						String resName = getContext().getResourceDefinition(type).getName();
						throw new InvalidRequestException("Resource " + resName + "/" + id + " not found, specified in path: " + nextPath + " (this is an invalid ID, must be numeric on this server)");
					}
					ResourceTable target = myEntityManager.find(ResourceTable.class, valueOf);
					if (target == null) {
						String resName = getContext().getResourceDefinition(type).getName();
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

		theEntity.setHasLinks(retVal.size() > 0);

		return retVal;
	}

	protected List<ResourceIndexedSearchParamDate> extractSearchParamDates(ResourceTable theEntity, IResource theResource) {
		ArrayList<ResourceIndexedSearchParamDate> retVal = new ArrayList<ResourceIndexedSearchParamDate>();

		RuntimeResourceDefinition def = getContext().getResourceDefinition(theResource);
		FhirTerser t = getContext().newTerser();
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
				} else if (nextObject instanceof PeriodDt) {
					PeriodDt nextValue = (PeriodDt) nextObject;
					if (nextValue.isEmpty()) {
						continue;
					}
					nextEntity = new ResourceIndexedSearchParamDate(nextSpDef.getName(), nextValue.getStart().getValue(), nextValue.getEnd().getValue());
				} else {
					if (!multiType) {
						throw new ConfigurationException("Search param " + nextSpDef.getName() + " is of unexpected datatype: " + nextObject.getClass());
					} else {
						continue;
					}
				}
				if (nextEntity != null) {
					nextEntity.setResource(theEntity);
					retVal.add(nextEntity);
				}
			}
		}

		theEntity.setParamsDatePopulated(retVal.size() > 0);

		return retVal;
	}

	protected ArrayList<ResourceIndexedSearchParamNumber> extractSearchParamNumber(ResourceTable theEntity, IResource theResource) {
		ArrayList<ResourceIndexedSearchParamNumber> retVal = new ArrayList<ResourceIndexedSearchParamNumber>();

		RuntimeResourceDefinition def = getContext().getResourceDefinition(theResource);
		FhirTerser t = getContext().newTerser();
		for (RuntimeSearchParam nextSpDef : def.getSearchParams()) {
			if (nextSpDef.getParamType() != SearchParamTypeEnum.NUMBER) {
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

				if (nextObject instanceof DurationDt) {
					DurationDt nextValue = (DurationDt) nextObject;
					if (nextValue.getValue().isEmpty()) {
						continue;
					}
					
					if (new UriDt(UCUM_NS).equals(nextValue.getSystem())) {
						if (isNotBlank(nextValue.getCode().getValue())) {
							
							Unit<? extends Quantity> unit = Unit.valueOf(nextValue.getCode().getValue());
							javax.measure.converter.UnitConverter dayConverter = unit.getConverterTo(NonSI.DAY);
							double dayValue = dayConverter.convert(nextValue.getValue().getValue().doubleValue());
							DurationDt newValue = new DurationDt();
							newValue.setSystem(UCUM_NS);
							newValue.setCode(NonSI.DAY.toString());
							newValue.setValue(dayValue);
							nextValue=newValue;

							/*
							@SuppressWarnings("unchecked")
							PhysicsUnit<? extends org.unitsofmeasurement.quantity.Quantity<?>> unit = (PhysicsUnit<? extends org.unitsofmeasurement.quantity.Quantity<?>>) UCUMFormat.getCaseInsensitiveInstance().parse(nextValue.getCode().getValue(), null);
							if (unit.isCompatible(UCUM.DAY)) {
								@SuppressWarnings("unchecked")
								PhysicsUnit<org.unitsofmeasurement.quantity.Time> timeUnit = (PhysicsUnit<Time>) unit;
								UnitConverter conv = timeUnit.getConverterTo(UCUM.DAY);
								double dayValue = conv.convert(nextValue.getValue().getValue().doubleValue());
								DurationDt newValue = new DurationDt();
								newValue.setSystem(UCUM_NS);
								newValue.setCode(UCUM.DAY.getSymbol());
								newValue.setValue(dayValue);
								nextValue=newValue;
							}
							*/
						}
					}
					
					ResourceIndexedSearchParamNumber nextEntity = new ResourceIndexedSearchParamNumber(resourceName, nextValue.getValue().getValue());
					nextEntity.setResource(theEntity);
					retVal.add(nextEntity);
				}else				if (nextObject instanceof QuantityDt) {
					QuantityDt nextValue = (QuantityDt) nextObject;
					if (nextValue.getValue().isEmpty()) {
						continue;
					}
					
					ResourceIndexedSearchParamNumber nextEntity = new ResourceIndexedSearchParamNumber(resourceName, nextValue.getValue().getValue());
					nextEntity.setResource(theEntity);
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

		theEntity.setParamsNumberPopulated(retVal.size() > 0);

		return retVal;
	}


	protected List<ResourceIndexedSearchParamQuantity> extractSearchParamQuantity(ResourceTable theEntity, IResource theResource) {
		ArrayList<ResourceIndexedSearchParamQuantity> retVal = new ArrayList<ResourceIndexedSearchParamQuantity>();

		RuntimeResourceDefinition def = getContext().getResourceDefinition(theResource);
		FhirTerser t = getContext().newTerser();
		for (RuntimeSearchParam nextSpDef : def.getSearchParams()) {
			if (nextSpDef.getParamType() != SearchParamTypeEnum.QUANTITY) {
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
					if (nextValue.getValue().isEmpty()) {
						continue;
					}
					
					ResourceIndexedSearchParamQuantity nextEntity = new ResourceIndexedSearchParamQuantity(resourceName, nextValue.getValue().getValue(), nextValue.getSystem().getValueAsString(), nextValue.getUnits().getValue());
					nextEntity.setResource(theEntity);
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

		theEntity.setParamsNumberPopulated(retVal.size() > 0);

		return retVal;
	}

	
	protected List<ResourceIndexedSearchParamString> extractSearchParamStrings(ResourceTable theEntity, IResource theResource) {
		ArrayList<ResourceIndexedSearchParamString> retVal = new ArrayList<ResourceIndexedSearchParamString>();

		RuntimeResourceDefinition def = getContext().getResourceDefinition(theResource);
		FhirTerser t = getContext().newTerser();
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
					String searchTerm = nextValue.getValueAsString();
					if (searchTerm.length() > ResourceIndexedSearchParamString.MAX_LENGTH) {
						searchTerm = searchTerm.substring(0, ResourceIndexedSearchParamString.MAX_LENGTH);
					}

					ResourceIndexedSearchParamString nextEntity = new ResourceIndexedSearchParamString(resourceName, normalizeString(searchTerm), searchTerm);
					nextEntity.setResource(theEntity);
					retVal.add(nextEntity);
				} else {
					if (nextObject instanceof HumanNameDt) {
						ArrayList<StringDt> allNames = new ArrayList<StringDt>();
						HumanNameDt nextHumanName = (HumanNameDt) nextObject;
						allNames.addAll(nextHumanName.getFamily());
						allNames.addAll(nextHumanName.getGiven());
						for (StringDt nextName : allNames) {
							if (nextName.isEmpty()) {
								continue;
							}
							ResourceIndexedSearchParamString nextEntity = new ResourceIndexedSearchParamString(resourceName, normalizeString(nextName.getValueAsString()), nextName.getValueAsString());
							nextEntity.setResource(theEntity);
							retVal.add(nextEntity);
						}
					} else if (nextObject instanceof AddressDt) {
						ArrayList<StringDt> allNames = new ArrayList<StringDt>();
						AddressDt nextAddress = (AddressDt) nextObject;
						allNames.addAll(nextAddress.getLine());
						allNames.add(nextAddress.getCity());
						allNames.add(nextAddress.getState());
						allNames.add(nextAddress.getCountry());
						allNames.add(nextAddress.getZip());
						for (StringDt nextName : allNames) {
							if (nextName.isEmpty()) {
								continue;
							}
							ResourceIndexedSearchParamString nextEntity = new ResourceIndexedSearchParamString(resourceName, normalizeString(nextName.getValueAsString()), nextName.getValueAsString());
							nextEntity.setResource(theEntity);
							retVal.add(nextEntity);
						}
					} else if (nextObject instanceof ContactDt) {
						ContactDt nextContact = (ContactDt) nextObject;
						if (nextContact.getValue().isEmpty() == false) {
							ResourceIndexedSearchParamString nextEntity = new ResourceIndexedSearchParamString(resourceName, normalizeString(nextContact.getValue().getValueAsString()), nextContact.getValue().getValueAsString());
							nextEntity.setResource(theEntity);
							retVal.add(nextEntity);
						}
					} else {
						if (!multiType) {
							throw new ConfigurationException("Search param " + resourceName + " is of unexpected datatype: " + nextObject.getClass());
						}
					}
				}
			}
		}

		theEntity.setParamsStringPopulated(retVal.size() > 0);

		return retVal;
	}

	protected List<BaseResourceIndexedSearchParam> extractSearchParamTokens(ResourceTable theEntity, IResource theResource) {
		ArrayList<BaseResourceIndexedSearchParam> retVal = new ArrayList<BaseResourceIndexedSearchParam>();

		RuntimeResourceDefinition def = getContext().getResourceDefinition(theResource);
		FhirTerser t = getContext().newTerser();
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
			List<String> systems = new ArrayList<String>();
			List<String> codes = new ArrayList<String>();
			for (Object nextObject : values) {
				if (nextObject instanceof IdentifierDt) {
					IdentifierDt nextValue = (IdentifierDt) nextObject;
					if (nextValue.isEmpty()) {
						continue;
					}
					systems.add(nextValue.getSystem().getValueAsString());
					codes.add(nextValue.getValue().getValue());
				} else if (nextObject instanceof IPrimitiveDatatype<?>) {
					IPrimitiveDatatype<?> nextValue = (IPrimitiveDatatype<?>) nextObject;
					if (nextValue.isEmpty()) {
						continue;
					}
					systems.add(null);
					codes.add(nextValue.getValueAsString());
				} else if (nextObject instanceof CodeableConceptDt) {
					CodeableConceptDt nextCC = (CodeableConceptDt) nextObject;
					if (!nextCC.getText().isEmpty()) {
						ResourceIndexedSearchParamString nextEntity = new ResourceIndexedSearchParamString(nextSpDef.getName(), normalizeString(nextCC.getText().getValue()), nextCC.getText().getValue());
						nextEntity.setResource(theEntity);
						retVal.add(nextEntity);
					}

					for (CodingDt nextCoding : nextCC.getCoding()) {
						if (nextCoding.isEmpty()) {
							continue;
						}

						String nextSystem = nextCoding.getSystem().getValueAsString();
						String nextCode = nextCoding.getCode().getValue();
						if (isNotBlank(nextSystem) || isNotBlank(nextCode)) {
							systems.add(nextSystem);
							codes.add(nextCode);
						}

						if (!nextCoding.getDisplay().isEmpty()) {
							systems.add(null);
							codes.add(nextCoding.getDisplay().getValue());
						}

					}
				} else {
					if (!multiType) {
						throw new ConfigurationException("Search param " + nextSpDef.getName() + " is of unexpected datatype: " + nextObject.getClass());
					} else {
						continue;
					}
				}
			}

			assert systems.size() == codes.size() : "Systems contains " + systems + ", codes contains: " + codes;

			Set<Pair<String, String>> haveValues = new HashSet<Pair<String, String>>();
			for (int i = 0; i < systems.size(); i++) {
				String system = systems.get(i);
				String code = codes.get(i);
				if (isBlank(system) && isBlank(code)) {
					continue;
				}

				if (system != null && system.length() > ResourceIndexedSearchParamToken.MAX_LENGTH) {
					system = system.substring(0, ResourceIndexedSearchParamToken.MAX_LENGTH);
				}
				if (code != null && code.length() > ResourceIndexedSearchParamToken.MAX_LENGTH) {
					code = code.substring(0, ResourceIndexedSearchParamToken.MAX_LENGTH);
				}

				Pair<String, String> nextPair = Pair.of(system, code);
				if (haveValues.contains(nextPair)) {
					continue;
				}
				haveValues.add(nextPair);

				ResourceIndexedSearchParamToken nextEntity;
				nextEntity = new ResourceIndexedSearchParamToken(nextSpDef.getName(), system, code);
				nextEntity.setResource(theEntity);
				retVal.add(nextEntity);

			}

		}

		theEntity.setParamsTokenPopulated(retVal.size() > 0);

		return retVal;
	}

	protected DaoConfig getConfig() {
		return myConfig;
	}

	protected IFhirResourceDao<? extends IResource> getDao(Class<? extends IResource> theType) {
		if (myResourceTypeToDao == null) {
			myResourceTypeToDao = new HashMap<Class<? extends IResource>, IFhirResourceDao<?>>();
			for (IFhirResourceDao<?> next : myResourceDaos) {
				myResourceTypeToDao.put(next.getResourceType(), next);
			}

			if (this instanceof IFhirResourceDao<?>) {
				IFhirResourceDao<?> thiz = (IFhirResourceDao<?>) this;
				myResourceTypeToDao.put(thiz.getResourceType(), thiz);
			}

		}

		return myResourceTypeToDao.get(theType);
	}

	protected TagDefinition getTag(String theScheme, String theTerm, String theLabel) {
		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<TagDefinition> cq = builder.createQuery(TagDefinition.class);
		Root<TagDefinition> from = cq.from(TagDefinition.class);
		cq.where(builder.and(builder.equal(from.get("myScheme"), theScheme), builder.equal(from.get("myTerm"), theTerm)));
		TypedQuery<TagDefinition> q = myEntityManager.createQuery(cq);
		try {
			return q.getSingleResult();
		} catch (NoResultException e) {
			TagDefinition retVal = new TagDefinition(theTerm, theLabel, theScheme);
			myEntityManager.persist(retVal);
			return retVal;
		}
	}

	protected TagList getTags(Class<? extends IResource> theResourceType, IdDt theResourceId) {
		String resourceName = null;
		if (theResourceType != null) {
			resourceName = toResourceName(theResourceType);
			if (theResourceId != null && theResourceId.hasVersionIdPart()) {
				IFhirResourceDao<? extends IResource> dao = getDao(theResourceType);
				BaseHasResource entity = dao.readEntity(theResourceId);
				TagList retVal = new TagList();
				for (BaseTag next : entity.getTags()) {
					retVal.add(next.getTag().toTag());
				}
				return retVal;
			}
		}

		Set<Long> tagIds = new HashSet<Long>();
		findMatchingTagIds(resourceName, theResourceId, tagIds, ResourceTag.class);
		findMatchingTagIds(resourceName, theResourceId, tagIds, ResourceHistoryTag.class);
		if (tagIds.isEmpty()) {
			return new TagList();
		}
		{
			CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
			CriteriaQuery<TagDefinition> cq = builder.createQuery(TagDefinition.class);
			Root<TagDefinition> from = cq.from(TagDefinition.class);
			cq.where(from.get("myId").in(tagIds));
			cq.orderBy(builder.asc(from.get("myScheme")), builder.asc(from.get("myTerm")));
			TypedQuery<TagDefinition> q = myEntityManager.createQuery(cq);
			q.setMaxResults(getConfig().getHardTagListLimit());

			TagList retVal = new TagList();
			for (TagDefinition next : q.getResultList()) {
				retVal.add(next.toTag());
			}

			return retVal;
		}
	}

	@Autowired
	private PlatformTransactionManager myPlatformTransactionManager;

	protected IBundleProvider history(String theResourceName, Long theId, Date theSince) {
		final List<HistoryTuple> tuples = new ArrayList<HistoryTuple>();

		final InstantDt end = createHistoryToTimestamp();

		StopWatch timer = new StopWatch();

		int limit = 10000;

		// Get list of IDs
		searchHistoryCurrentVersion(theResourceName, theId, theSince, end.getValue(), limit, tuples);
		assert tuples.size() < 2 || !tuples.get(tuples.size() - 2).getUpdated().before(tuples.get(tuples.size() - 1).getUpdated());
		ourLog.info("Retrieved {} history IDs from current versions in {} ms", tuples.size(), timer.getMillisAndRestart());

		searchHistoryHistory(theResourceName, theId, theSince, end.getValue(), limit, tuples);
		assert tuples.size() < 2 || !tuples.get(tuples.size() - 2).getUpdated().before(tuples.get(tuples.size() - 1).getUpdated());
		ourLog.info("Retrieved {} history IDs from previous versions in {} ms", tuples.size(), timer.getMillisAndRestart());

		// Sort merged list
		Collections.sort(tuples, Collections.reverseOrder());
		assert tuples.size() < 2 || !tuples.get(tuples.size() - 2).getUpdated().before(tuples.get(tuples.size() - 1).getUpdated());

		return new IBundleProvider() {

			@Override
			public InstantDt getPublished() {
				return end;
			}

			@Override
			public List<IResource> getResources(final int theFromIndex, final int theToIndex) {
				final StopWatch timer = new StopWatch();
				TransactionTemplate template = new TransactionTemplate(myPlatformTransactionManager);
				return template.execute(new TransactionCallback<List<IResource>>() {
					@Override
					public List<IResource> doInTransaction(TransactionStatus theStatus) {
						List<BaseHasResource> resEntities = Lists.newArrayList();

						List<HistoryTuple> tupleSubList = tuples.subList(theFromIndex, theToIndex);
						searchHistoryCurrentVersion(tupleSubList, resEntities);
						ourLog.info("Loaded history from current versions in {} ms", timer.getMillisAndRestart());

						searchHistoryHistory(tupleSubList, resEntities);
						ourLog.info("Loaded history from previous versions in {} ms", timer.getMillisAndRestart());

						Collections.sort(resEntities, new Comparator<BaseHasResource>() {
							@Override
							public int compare(BaseHasResource theO1, BaseHasResource theO2) {
								return theO2.getUpdated().getValue().compareTo(theO1.getUpdated().getValue());
							}
						});

						int limit = theToIndex - theFromIndex;
						if (resEntities.size() > limit) {
							resEntities = resEntities.subList(0, limit);
						}

						ArrayList<IResource> retVal = new ArrayList<IResource>();
						for (BaseHasResource next : resEntities) {
							retVal.add(toResource(next));
						}
						return retVal;
					}
				});
			}

			@Override
			public int size() {
				return tuples.size();
			}
		};
	}

	protected List<IResource> loadResourcesById(Set<IdDt> theIncludePids) {
		Set<Long> pids = new HashSet<Long>();
		for (IdDt next : theIncludePids) {
			pids.add(next.getIdPartAsLong());
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<ResourceTable> cq = builder.createQuery(ResourceTable.class);
		Root<ResourceTable> from = cq.from(ResourceTable.class);
		// cq.where(builder.equal(from.get("myResourceType"),
		// getContext().getResourceDefinition(myResourceType).getName()));
		// if (theIncludePids != null) {
		cq.where(from.get("myId").in(pids));
		// }
		TypedQuery<ResourceTable> q = myEntityManager.createQuery(cq);

		ArrayList<IResource> retVal = new ArrayList<IResource>();
		for (ResourceTable next : q.getResultList()) {
			IResource resource = toResource(next);
			retVal.add(resource);
		}
		
		return retVal;
	}

	protected String normalizeString(String theString) {
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

	protected void populateResourceIntoEntity(IResource theResource, ResourceTable theEntity) {

		if (theEntity.getPublished().isEmpty()) {
			theEntity.setPublished(new Date());
		}
		theEntity.setUpdated(new Date());

		theEntity.setResourceType(toResourceName(theResource));

		List<ResourceReferenceDt> refs = myContext.newTerser().getAllPopulatedChildElementsOfType(theResource, ResourceReferenceDt.class);
		for (ResourceReferenceDt nextRef : refs) {
			if (nextRef.getReference().isEmpty() == false) {
				if (nextRef.getReference().hasVersionIdPart()) {
					nextRef.setReference(nextRef.getReference().toUnqualifiedVersionless());
				}
			}
		}

		String encoded = myConfig.getResourceEncoding().newParser(myContext).encodeResourceToString(theResource);
		ResourceEncodingEnum encoding = myConfig.getResourceEncoding();
		theEntity.setEncoding(encoding);
		try {
			switch (encoding) {
			case JSON:
				theEntity.setResource(encoded.getBytes("UTF-8"));
				break;
			case JSONC:
				theEntity.setResource(GZipUtil.compress(encoded));
				break;
			}
		} catch (UnsupportedEncodingException e) {
			throw new InternalErrorException(e);
		}

		TagList tagList = (TagList) theResource.getResourceMetadata().get(ResourceMetadataKeyEnum.TAG_LIST);
		if (tagList != null) {
			for (Tag next : tagList) {
				TagDefinition tag = getTag(next.getScheme(), next.getTerm(), next.getLabel());
				theEntity.addTag(tag);
				theEntity.setHasTags(true);
			}
		}

		String title = ResourceMetadataKeyEnum.TITLE.get(theResource);
		if (title != null && title.length() > BaseHasResource.MAX_TITLE_LENGTH) {
			title = title.substring(0, BaseHasResource.MAX_TITLE_LENGTH);
		}
		theEntity.setTitle(title);

	}

	protected ResourceTable toEntity(IResource theResource) {
		ResourceTable retVal = new ResourceTable();

		populateResourceIntoEntity(theResource, retVal);

		return retVal;
	}

	protected void createForcedIdIfNeeded(ResourceTable entity, IdDt id) {
		if (id.isEmpty() == false && id.hasIdPart()) {
			if (isValidPid(id)) {
				return;
			}
			ForcedId fid = new ForcedId();
			fid.setForcedId(id.getIdPart());
			fid.setResource(entity);
			entity.setForcedId(fid);
		}
	}

	protected IResource toResource(BaseHasResource theEntity) {
		RuntimeResourceDefinition type = myContext.getResourceDefinition(theEntity.getResourceType());
		return toResource(type.getImplementingClass(), theEntity);
	}

	protected Long translateForcedIdToPid(IdDt theId) {
		if (isValidPid(theId)) {
			return theId.getIdPartAsLong();
		} else {
			TypedQuery<ForcedId> q = myEntityManager.createNamedQuery("Q_GET_FORCED_ID", ForcedId.class);
			q.setParameter("ID", theId.getIdPart());
			try {
				return q.getSingleResult().getResourcePid();
			} catch (NoResultException e) {
				throw new ResourceNotFoundException(theId);
			}
		}
	}

	protected boolean isValidPid(IdDt theId) {
		String idPart = theId.getIdPart();
		for (int i = 0; i < idPart.length(); i++) {
			char nextChar = idPart.charAt(i);
			if (nextChar < '0' || nextChar > '9') {
				return false;
			}
		}
		return true;
	}

	protected <T extends IResource> T toResource(Class<T> theResourceType, BaseHasResource theEntity) {
		String resourceText = null;
		switch (theEntity.getEncoding()) {
		case JSON:
			try {
				resourceText = new String(theEntity.getResource(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new Error("Should not happen", e);
			}
			break;
		case JSONC:
			resourceText = GZipUtil.decompress(theEntity.getResource());
			break;
		}

		IParser parser = theEntity.getEncoding().newParser(getContext());
		T retVal = parser.parseResource(theResourceType, resourceText);

		retVal.setId(theEntity.getIdDt());

		retVal.getResourceMetadata().put(ResourceMetadataKeyEnum.VERSION_ID, theEntity.getVersion());
		retVal.getResourceMetadata().put(ResourceMetadataKeyEnum.PUBLISHED, theEntity.getPublished());
		retVal.getResourceMetadata().put(ResourceMetadataKeyEnum.UPDATED, theEntity.getUpdated());

		if (theEntity.getTitle() != null) {
			ResourceMetadataKeyEnum.TITLE.put(retVal, theEntity.getTitle());
		}

		if (theEntity.getDeleted() != null) {
			ResourceMetadataKeyEnum.DELETED_AT.put(retVal, new InstantDt(theEntity.getDeleted()));
		}

		Collection<? extends BaseTag> tags = theEntity.getTags();
		if (theEntity.isHasTags()) {
			TagList tagList = new TagList();
			for (BaseTag next : tags) {
				tagList.add(new Tag(next.getTag().getScheme(), next.getTag().getTerm(), next.getTag().getLabel()));
			}
			retVal.getResourceMetadata().put(ResourceMetadataKeyEnum.TAG_LIST, tagList);
		}
		return retVal;
	}

	protected String toResourceName(Class<? extends IResource> theResourceType) {
		return myContext.getResourceDefinition(theResourceType).getName();
	}

	protected String toResourceName(IResource theResource) {
		return myContext.getResourceDefinition(theResource).getName();
	}

	protected ResourceTable updateEntity(final IResource theResource, ResourceTable entity, boolean theUpdateHistory, boolean theDelete) {
		if (entity.getPublished() == null) {
			entity.setPublished(new Date());
		}

		if (theUpdateHistory) {
			final ResourceHistoryTable historyEntry = entity.toHistory();
			myEntityManager.persist(historyEntry);
		}

		entity.setVersion(entity.getVersion() + 1);

		Collection<ResourceIndexedSearchParamString> paramsString = new ArrayList<ResourceIndexedSearchParamString>(entity.getParamsString());
		Collection<ResourceIndexedSearchParamToken> paramsToken = new ArrayList<ResourceIndexedSearchParamToken>(entity.getParamsToken());
		Collection<ResourceIndexedSearchParamNumber> paramsNumber = new ArrayList<ResourceIndexedSearchParamNumber>(entity.getParamsNumber());
		Collection<ResourceIndexedSearchParamQuantity> paramsQuantity = new ArrayList<ResourceIndexedSearchParamQuantity>(entity.getParamsQuantity());
		Collection<ResourceIndexedSearchParamDate> paramsDate = new ArrayList<ResourceIndexedSearchParamDate>(entity.getParamsDate());
		Collection<ResourceLink> resourceLinks = new ArrayList<ResourceLink>(entity.getResourceLinks());

		final List<ResourceIndexedSearchParamString> stringParams;
		final List<ResourceIndexedSearchParamToken> tokenParams;
		final List<ResourceIndexedSearchParamNumber> numberParams;
		final List<ResourceIndexedSearchParamQuantity> quantityParams;
		final List<ResourceIndexedSearchParamDate> dateParams;
		final List<ResourceLink> links;
		if (theDelete) {

			stringParams = Collections.emptyList();
			tokenParams = Collections.emptyList();
			numberParams = Collections.emptyList();
			quantityParams = Collections.emptyList();
			dateParams = Collections.emptyList();
			links = Collections.emptyList();
			entity.setDeleted(new Date());
			entity.setUpdated(new Date());

		} else {

			stringParams = extractSearchParamStrings(entity, theResource);
			numberParams = extractSearchParamNumber(entity, theResource);
			quantityParams = extractSearchParamQuantity(entity, theResource);
			dateParams = extractSearchParamDates(entity, theResource);

			tokenParams = new ArrayList<ResourceIndexedSearchParamToken>();
			for (BaseResourceIndexedSearchParam next : extractSearchParamTokens(entity, theResource)) {
				if (next instanceof ResourceIndexedSearchParamToken) {
					tokenParams.add((ResourceIndexedSearchParamToken) next);
				} else {
					stringParams.add((ResourceIndexedSearchParamString) next);
				}
			}

			links = extractResourceLinks(entity, theResource);
			populateResourceIntoEntity(theResource, entity);

			entity.setUpdated(new Date());
			entity.setParamsString(stringParams);
			entity.setParamsStringPopulated(stringParams.isEmpty() == false);
			entity.setParamsToken(tokenParams);
			entity.setParamsTokenPopulated(tokenParams.isEmpty() == false);
			entity.setParamsNumber(numberParams);
			entity.setParamsNumberPopulated(numberParams.isEmpty() == false);
			entity.setParamsQuantity(quantityParams);
			entity.setParamsQuantityPopulated(quantityParams.isEmpty() == false);
			entity.setParamsDate(dateParams);
			entity.setParamsDatePopulated(dateParams.isEmpty() == false);
			entity.setResourceLinks(links);
			entity.setHasLinks(links.isEmpty() == false);

		}

		if (entity.getId() == null) {
			myEntityManager.persist(entity);

			if (entity.getForcedId() != null) {
				myEntityManager.persist(entity.getForcedId());
			}

		} else {
			entity = myEntityManager.merge(entity);
		}

		if (entity.isParamsStringPopulated()) {
			for (ResourceIndexedSearchParamString next : paramsString) {
				myEntityManager.remove(next);
			}
		}
		for (ResourceIndexedSearchParamString next : stringParams) {
			myEntityManager.persist(next);
		}

		if (entity.isParamsTokenPopulated()) {
			for (ResourceIndexedSearchParamToken next : paramsToken) {
				myEntityManager.remove(next);
			}
		}
		for (ResourceIndexedSearchParamToken next : tokenParams) {
			myEntityManager.persist(next);
		}

		if (entity.isParamsNumberPopulated()) {
			for (ResourceIndexedSearchParamNumber next : paramsNumber) {
				myEntityManager.remove(next);
			}
		}
		for (ResourceIndexedSearchParamNumber next : numberParams) {
			myEntityManager.persist(next);
		}

		if (entity.isParamsQuantityPopulated()) {
			for (ResourceIndexedSearchParamQuantity next : paramsQuantity) {
				myEntityManager.remove(next);
			}
		}
		for (ResourceIndexedSearchParamQuantity next : quantityParams) {
			myEntityManager.persist(next);
		}

		if (entity.isParamsDatePopulated()) {
			for (ResourceIndexedSearchParamDate next : paramsDate) {
				myEntityManager.remove(next);
			}
		}
		for (ResourceIndexedSearchParamDate next : dateParams) {
			myEntityManager.persist(next);
		}

		if (entity.isHasLinks()) {
			for (ResourceLink next : resourceLinks) {
				myEntityManager.remove(next);
			}
		}
		for (ResourceLink next : links) {
			myEntityManager.persist(next);
		}

		myEntityManager.flush();

		if (theResource != null) {
			theResource.setId(entity.getIdDt());
		}

		return entity;
	}

	InstantDt createHistoryToTimestamp() {
		// final InstantDt end = new InstantDt(DateUtils.addSeconds(DateUtils.truncate(new Date(), Calendar.SECOND),
		// -1));
		return InstantDt.withCurrentTime();
	}

	
	
}
