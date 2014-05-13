package ca.uhn.fhir.jpa.dao;

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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.entity.BaseHasResource;
import ca.uhn.fhir.jpa.entity.BaseResourceTable;
import ca.uhn.fhir.jpa.entity.BaseTag;
import ca.uhn.fhir.jpa.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamToken;
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
import ca.uhn.fhir.model.dstu.valueset.SearchParamTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.FhirTerser;

public class CopyOfFhirResourceDao<T extends IResource, X extends BaseResourceTable<T>> implements IFhirResourceDao<T> {

	private FhirContext myCtx;
	@PersistenceContext(name = "FHIR_UT", type = PersistenceContextType.TRANSACTION, unitName = "FHIR_UT")
	private EntityManager myEntityManager;

	@Autowired
	private PlatformTransactionManager myPlatformTransactionManager;

	private Class<T> myResourceType;
	private Class<X> myTableType;

	private void addTokenPredicate(CriteriaQuery<X> theCriteriaQuery, List<IQueryParameterType> theOrParams, Root<X> theFrom, CriteriaBuilder theBuilder) {
		if (theOrParams == null || theOrParams.isEmpty()) {
			return;
		}

		if (theOrParams.size() > 1) {
			throw new UnsupportedOperationException("Multiple values not yet supported"); // TODO: implement
		}

		IQueryParameterType params = theOrParams.get(0);

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

		Join<Object, Object> join = theFrom.join("myParamsToken", JoinType.LEFT);
		ArrayList<Predicate> predicates = (new ArrayList<Predicate>());

		if (system != null) {
			predicates.add(theBuilder.equal(join.get("mySystem"), system));
		}

		if (code != null) {
			predicates.add(theBuilder.equal(join.get("myValue"), code));
		}

		theCriteriaQuery.where(predicates.toArray(new Predicate[0]));
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public MethodOutcome create(T theResource) {

		final X entity = toEntity(theResource);

		entity.setPublished(new Date());
		entity.setUpdated(entity.getPublished());

		final List<ResourceIndexedSearchParamString> stringParams = extractSearchParamStrings(entity, theResource);
		final List<ResourceIndexedSearchParamToken> tokenParams = extractSearchParamTokens(entity, theResource);

		TransactionTemplate template = new TransactionTemplate(myPlatformTransactionManager);
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
				return entity;
			}
		});

		MethodOutcome outcome = toMethodOutcome(entity);
		return outcome;
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
				continue; // TODO: implement phoenetic, and any others that have no path
			}

			String nextPath = nextSpDef.getPath();
			List<Object> values = t.getValues(theResource, nextPath);
			for (Object nextObject : values) {
				if (((IDatatype) nextObject).isEmpty()) {
					continue;
				}
				if (nextObject instanceof IPrimitiveDatatype<?>) {
					IPrimitiveDatatype<?> nextValue = (IPrimitiveDatatype<?>) nextObject;
					ResourceIndexedSearchParamString nextEntity = new ResourceIndexedSearchParamString(nextSpDef.getName(), nextValue.getValueAsString());
					nextEntity.setResource(theEntity);
					retVal.add(nextEntity);
				} else if (nextObject instanceof HumanNameDt) {
					for (StringDt nextName : ((HumanNameDt) nextObject).getFamily()) {
						if (nextName.isEmpty()) {
							continue;
						}
						ResourceIndexedSearchParamString nextEntity = new ResourceIndexedSearchParamString(nextSpDef.getName(), nextName.getValueAsString());
						nextEntity.setResource(theEntity);
						retVal.add(nextEntity);
					}
				} else {
					throw new ConfigurationException("Search param " + nextSpDef.getName() + " is of unexpected datatype: " + nextObject.getClass());
				}
			}
		}

		return retVal;
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
					throw new ConfigurationException("Search param " + nextSpDef.getName() + " is of unexpected datatype: " + nextObject.getClass());
				}
				if (nextEntity != null) {
					nextEntity.setResource(theEntity);
					retVal.add(nextEntity);
				}
			}
		}

		return retVal;
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
	public List<T> search(String theSpName, IQueryParameterType theValue) {
		return search(Collections.singletonMap(theSpName, theValue));
	}

	@Override
	public List<T> searchWithAndOr(Map<String, List<List<IQueryParameterType>>> theParams) {
		Map<String, List<List<IQueryParameterType>>> params = theParams;
		if (params == null) {
			params = Collections.emptyMap();
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<X> cq = builder.createQuery(myTableType);
		Root<X> from = cq.from(myTableType);

		RuntimeResourceDefinition resourceDef = myCtx.getResourceDefinition(myResourceType);

		for (Entry<String, List<List<IQueryParameterType>>> nextParamEntry : params.entrySet()) {
			String nextParamName = nextParamEntry.getKey();
			RuntimeSearchParam nextParamDef = resourceDef.getSearchParam(nextParamName);
			if (nextParamDef != null) {
				if (nextParamDef.getParamType() == SearchParamTypeEnum.TOKEN) {
					for (List<IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
						addTokenPredicate(cq, nextAnd, from, builder);
					}
				}
			}
		}

		// Execute the query and make sure we return distinct results
		
		Set<Long> pids = new HashSet<Long>();
		
		TypedQuery<X> q = myEntityManager.createQuery(cq);
		List<T> retVal = new ArrayList<>();
		for (X next : q.getResultList()) {
			T resource = toResource(next);
			if (pids.contains(next.getIdAsLong())) {
				continue;
			}else {
				pids.add(next.getIdAsLong());
			}
			
			retVal.add(resource);
		}
		
		return retVal;
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
