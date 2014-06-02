package ca.uhn.fhir.jpa.dao;

import static org.apache.commons.lang3.StringUtils.*;

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
import org.springframework.beans.factory.annotation.Autowired;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.entity.BaseHasResource;
import ca.uhn.fhir.jpa.entity.BaseTag;
import ca.uhn.fhir.jpa.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.entity.ResourceHistoryTablePk;
import ca.uhn.fhir.jpa.entity.ResourceHistoryTag;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.entity.ResourceLink;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.ResourceTag;
import ca.uhn.fhir.jpa.entity.TagDefinition;
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
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.SearchParamTypeEnum;
import ca.uhn.fhir.model.primitive.BaseDateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.FhirTerser;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public abstract class BaseFhirDao {
	private FhirContext myContext = new FhirContext();
	@PersistenceContext(name = "FHIR_UT", type = PersistenceContextType.TRANSACTION, unitName = "FHIR_UT")
	private EntityManager myEntityManager;
	@Autowired
	private List<IFhirResourceDao<?>> myResourceDaos;

	private Map<Class<? extends IResource>, IFhirResourceDao<?>> myResourceTypeToDao;

	public FhirContext getContext() {
		return myContext;
	}

	public void setContext(FhirContext theContext) {
		myContext = theContext;
	}

	protected DaoConfig getConfig() {
		return myConfig;
	}

	@Autowired(required = true)
	private DaoConfig myConfig;

	private void searchHistoryCurrentVersion(String theResourceName, Long theId, Date theSince, Integer theLimit, List<HistoryTuple> tuples) {
		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = builder.createTupleQuery();
		Root<?> from = cq.from(ResourceTable.class);
		cq.multiselect(from.get("myId").as(Long.class), from.get("myUpdated").as(Date.class));

		List<Predicate> predicates = new ArrayList<Predicate>();
		if (theSince != null) {
			Predicate low = builder.greaterThanOrEqualTo(from.<Date> get("myUpdated"), theSince);
			predicates.add(low);
		}

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
			long id = (Long) next.get(0);
			Date updated = (Date) next.get(1);
			tuples.add(new HistoryTuple(ResourceTable.class, updated, id));
		}
	}

	private void searchHistoryCurrentVersion(List<HistoryTuple> theTuples, List<BaseHasResource> theRetVal) {
		Collection<HistoryTuple> tuples = Collections2.filter(theTuples, new com.google.common.base.Predicate<HistoryTuple>() {
			@Override
			public boolean apply(HistoryTuple theInput) {
				return theInput.getTable().equals(ResourceTable.class);
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

	private void searchHistoryHistory(String theResourceName, Long theId, Date theSince, Integer theLimit, List<HistoryTuple> tuples) {
		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = builder.createTupleQuery();
		Root<?> from = cq.from(ResourceHistoryTable.class);
		cq.multiselect(from.get("myPk").as(ResourceHistoryTablePk.class), from.get("myUpdated").as(Date.class));

		List<Predicate> predicates = new ArrayList<Predicate>();
		if (theSince != null) {
			Predicate low = builder.greaterThanOrEqualTo(from.<Date> get("myUpdated"), theSince);
			predicates.add(low);
		}

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
			ResourceHistoryTablePk id = (ResourceHistoryTablePk) next.get(0);
			Date updated = (Date) next.get(1);
			tuples.add(new HistoryTuple(ResourceHistoryTable.class, updated, id));
		}
	}

	private void searchHistoryHistory(List<HistoryTuple> theTuples, List<BaseHasResource> theRetVal) {
		Collection<HistoryTuple> tuples = Collections2.filter(theTuples, new com.google.common.base.Predicate<HistoryTuple>() {
			@Override
			public boolean apply(HistoryTuple theInput) {
				return theInput.getTable().equals(ResourceHistoryTable.class);
			}
		});
		Collection<ResourceHistoryTablePk> ids = Collections2.transform(tuples, new Function<HistoryTuple, ResourceHistoryTablePk>() {
			@Override
			public ResourceHistoryTablePk apply(HistoryTuple theInput) {
				return (ResourceHistoryTablePk) theInput.getId();
			}
		});
		if (ids.isEmpty()) {
			return;
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<ResourceHistoryTable> cq = builder.createQuery(ResourceHistoryTable.class);
		Root<ResourceHistoryTable> from = cq.from(ResourceHistoryTable.class);
		cq.where(from.get("myPk").in(ids));

		cq.orderBy(builder.desc(from.get("myUpdated")));
		TypedQuery<ResourceHistoryTable> q = myEntityManager.createQuery(cq);
		for (ResourceHistoryTable next : q.getResultList()) {
			theRetVal.add(next);
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

					String typeString = nextValue.getResourceId().getResourceType();
					if (isBlank(typeString)) {
						throw new InvalidRequestException("Invalid resource reference found at path[" + nextPath + "] - Does not contain resource type - " + nextValue.getReference().getValue());
					}
					Class<? extends IResource> type = getContext().getResourceDefinition(typeString).getImplementingClass();
					String id = nextValue.getResourceId().getUnqualifiedId();
					if (StringUtils.isBlank(id)) {
						continue;
					}

					IFhirResourceDao<?> dao = getDao(type);
					if (dao == null) {
						throw new InvalidRequestException("This server is not able to handle resources of type: " + nextValue.getResourceId().getResourceType());
					}
					Long valueOf;
					try {
						valueOf = Long.valueOf(id);
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
					ResourceIndexedSearchParamNumber nextEntity = new ResourceIndexedSearchParamNumber(resourceName, nextValue.getValue().getValue(), nextValue.getSystem().getValueAsString(), nextValue.getUnits().getValue());
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
					ResourceIndexedSearchParamString nextEntity = new ResourceIndexedSearchParamString(resourceName, normalizeString(nextValue.getValueAsString()), nextValue.getValueAsString());
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

	protected List<ResourceIndexedSearchParamToken> extractSearchParamTokens(ResourceTable theEntity, IResource theResource) {
		ArrayList<ResourceIndexedSearchParamToken> retVal = new ArrayList<ResourceIndexedSearchParamToken>();

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
						nextEntity.setResource(theEntity);
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
					nextEntity.setResource(theEntity);
					retVal.add(nextEntity);
				}
			}
		}

		theEntity.setParamsTokenPopulated(retVal.size() > 0);

		return retVal;
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

	protected ArrayList<IResource> history(String theResourceName, Long theId, Date theSince, Integer theLimit) {
		List<HistoryTuple> tuples = new ArrayList<HistoryTuple>();

		// Get list of IDs
		searchHistoryCurrentVersion(theResourceName, theId, theSince, theLimit, tuples);
		assert tuples.size() < 2 || !tuples.get(tuples.size() - 2).getUpdated().before(tuples.get(tuples.size() - 1).getUpdated());
		searchHistoryHistory(theResourceName, theId, theSince, theLimit, tuples);
		assert tuples.size() < 2 || !tuples.get(tuples.size() - 2).getUpdated().before(tuples.get(tuples.size() - 1).getUpdated());

		// Sort merged list
		Collections.sort(tuples, Collections.reverseOrder());
		assert tuples.size() < 2 || !tuples.get(tuples.size() - 2).getUpdated().before(tuples.get(tuples.size() - 1).getUpdated());

		// Pull actual resources
		List<BaseHasResource> resEntities = Lists.newArrayList();
		searchHistoryCurrentVersion(tuples, resEntities);
		searchHistoryHistory(tuples, resEntities);

		Collections.sort(resEntities, new Comparator<BaseHasResource>() {
			@Override
			public int compare(BaseHasResource theO1, BaseHasResource theO2) {
				return theO2.getUpdated().getValue().compareTo(theO1.getUpdated().getValue());
			}
		});

		int limit;
		if (theLimit != null && theLimit < myConfig.getHardSearchLimit()) {
			limit = theLimit;
		} else {
			limit = myConfig.getHardSearchLimit();
		}
		if (resEntities.size() > limit) {
			resEntities = resEntities.subList(0, limit);
		}

		ArrayList<IResource> retVal = new ArrayList<IResource>();
		for (BaseHasResource next : resEntities) {
			retVal.add(toResource(next));
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

	protected void populateResourceIntoEntity(IResource theResource, ResourceTable theEntity) {

		if (theEntity.getPublished().isEmpty()) {
			theEntity.setPublished(new Date());
		}
		theEntity.setUpdated(new Date());

		theEntity.setResourceType(toResourceName(theResource));
		theEntity.setResource(getContext().newJsonParser().encodeResourceToString(theResource));
		theEntity.setEncoding(EncodingEnum.JSON);

		TagList tagList = (TagList) theResource.getResourceMetadata().get(ResourceMetadataKeyEnum.TAG_LIST);
		if (tagList != null) {
			for (Tag next : tagList) {
				TagDefinition tag = getTag(next.getScheme(), next.getTerm(), next.getLabel());
				theEntity.addTag(tag);
			}
		}

	}

	protected ResourceTable toEntity(IResource theResource) {
		ResourceTable retVal = new ResourceTable();

		populateResourceIntoEntity(theResource, retVal);

		return retVal;
	}

	protected IResource toResource(BaseHasResource theEntity) {
		RuntimeResourceDefinition type = myContext.getResourceDefinition(theEntity.getResourceType());
		return toResource(type.getImplementingClass(), theEntity);
	}

	protected <T extends IResource> T toResource(Class<T> theResourceType, BaseHasResource theEntity) {
		String resourceText = theEntity.getResource();
		IParser parser = theEntity.getEncoding().newParser(getContext());
		T retVal = parser.parseResource(theResourceType, resourceText);
		retVal.setId(theEntity.getIdDt());
		retVal.getResourceMetadata().put(ResourceMetadataKeyEnum.VERSION_ID, theEntity.getVersion());
		retVal.getResourceMetadata().put(ResourceMetadataKeyEnum.PUBLISHED, theEntity.getPublished());
		retVal.getResourceMetadata().put(ResourceMetadataKeyEnum.UPDATED, theEntity.getUpdated());
		if (theEntity.getTags().size() > 0) {
			TagList tagList = new TagList();
			for (BaseTag next : theEntity.getTags()) {
				tagList.add(new Tag(next.getTag().getTerm(), next.getTag().getLabel(), next.getTag().getScheme()));
			}
			retVal.getResourceMetadata().put(ResourceMetadataKeyEnum.TAG_LIST, tagList);
		}
		return retVal;
	}

	protected String toResourceName(IResource theResource) {
		return myContext.getResourceDefinition(theResource).getName();
	}
	protected String toResourceName(Class<? extends IResource> theResourceType) {
		return myContext.getResourceDefinition(theResourceType).getName();
	}

	
	protected ResourceTable updateEntity(final IResource theResource, ResourceTable entity, boolean theUpdateHistory) {
		if (entity.getPublished() == null) {
			entity.setPublished(new Date());
		}

		if (theUpdateHistory) {
			final ResourceHistoryTable historyEntry = entity.toHistory(getContext());
			myEntityManager.persist(historyEntry);
		}

		entity.setVersion(entity.getVersion() + 1);

		final List<ResourceIndexedSearchParamString> stringParams = extractSearchParamStrings(entity, theResource);
		final List<ResourceIndexedSearchParamToken> tokenParams = extractSearchParamTokens(entity, theResource);
		final List<ResourceIndexedSearchParamNumber> numberParams = extractSearchParamNumber(entity, theResource);
		final List<ResourceIndexedSearchParamDate> dateParams = extractSearchParamDates(entity, theResource);
		final List<ResourceLink> links = extractResourceLinks(entity, theResource);

		populateResourceIntoEntity(theResource, entity);

		entity.setUpdated(new Date());

		if (entity.getId() == null) {
			myEntityManager.persist(entity);
		} else {
			entity = myEntityManager.merge(entity);
		}

		if (entity.isParamsStringPopulated()) {
			for (ResourceIndexedSearchParamString next : entity.getParamsString()) {
				myEntityManager.remove(next);
			}
		}
		for (ResourceIndexedSearchParamString next : stringParams) {
			myEntityManager.persist(next);
		}

		if (entity.isParamsTokenPopulated()) {
			for (ResourceIndexedSearchParamToken next : entity.getParamsToken()) {
				myEntityManager.remove(next);
			}
		}
		for (ResourceIndexedSearchParamToken next : tokenParams) {
			myEntityManager.persist(next);
		}

		if (entity.isParamsNumberPopulated()) {
			for (ResourceIndexedSearchParamNumber next : entity.getParamsNumber()) {
				myEntityManager.remove(next);
			}
		}
		for (ResourceIndexedSearchParamNumber next : numberParams) {
			myEntityManager.persist(next);
		}

		if (entity.isParamsDatePopulated()) {
			for (ResourceIndexedSearchParamDate next : entity.getParamsDate()) {
				myEntityManager.remove(next);
			}
		}
		for (ResourceIndexedSearchParamDate next : dateParams) {
			myEntityManager.persist(next);
		}

		if (entity.isHasLinks()) {
			for (ResourceLink next : entity.getResourceLinks()) {
				myEntityManager.remove(next);
			}
		}
		for (ResourceLink next : links) {
			myEntityManager.persist(next);
		}

		myEntityManager.flush();
		theResource.setId(new IdDt(entity.getResourceType(), entity.getId().toString(), Long.toString(entity.getVersion())));

		return entity;
	}

	protected TagList getTags(Class<? extends IResource> theResourceType, IdDt theResourceId) {
		String resourceName=null;
		if (theResourceType != null) {
			resourceName = toResourceName(theResourceType);
			if (theResourceId != null && theResourceId.hasUnqualifiedVersionId()) {
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

	private void findMatchingTagIds(String theResourceName, IdDt theResourceId, Set<Long> tagIds, Class<? extends BaseTag> entityClass) {
		{
			CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
			CriteriaQuery<Tuple> cq = builder.createTupleQuery();
			Root<? extends BaseTag> from = cq.from(entityClass);
			cq.multiselect(from.get("myTagId").as(Long.class)).distinct(true);

			if (theResourceName != null) {
				Predicate typePredicate = builder.equal(from.get("myResourceType"), theResourceName);
				if (theResourceId != null) {
					cq.where(typePredicate, builder.equal(from.get("myResourceId"), theResourceId.asLong()));
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

}
