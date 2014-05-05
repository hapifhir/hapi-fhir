package ca.uhn.fhir.jpa.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.entity.BaseHasResource;
import ca.uhn.fhir.jpa.entity.BaseResourceTable;
import ca.uhn.fhir.jpa.entity.BaseTag;
import ca.uhn.fhir.jpa.entity.ResourceHistoryTable;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.FhirTerser;

public class FhirResourceDao<T extends IResource, X extends BaseResourceTable<T>> implements IFhirResourceDao<T> {

	private FhirContext myCtx;
	@PersistenceContext(name = "FHIR_UT", type = PersistenceContextType.TRANSACTION, unitName = "FHIR_UT")
	private EntityManager myEntityManager;

	@Autowired
	private PlatformTransactionManager myPlatformTransactionManager;

	private Class<T> myResourceType;
	private Class<X> myTableType;

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public MethodOutcome create(T theResource) {

		final X entity = toEntity(theResource);

		entity.setPublished(new Date());
		entity.setUpdated(entity.getPublished());

		TransactionTemplate template = new TransactionTemplate(myPlatformTransactionManager);
		template.execute(new TransactionCallback<X>() {
			@Override
			public X doInTransaction(TransactionStatus theStatus) {
				myEntityManager.persist(entity);
				return entity;
			}
		});

		MethodOutcome outcome = toMethodOutcome(entity);
		return outcome;
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

	@Required
	public void setTableType(Class<X> theTableType) {
		myTableType = theTableType;
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

	private X readEntity(IdDt theId) {
		X entity = (X) myEntityManager.find(myTableType, theId.asLong());
		if (entity == null) {
			throw new ResourceNotFoundException(theId);
		}
		return entity;
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
		if (theEntity.getTags().size()>0) {
			TagList tagList = new TagList();
			for (BaseTag next : theEntity.getTags()) {
				tagList.add(new Tag(next.getTerm(), next.getLabel(), next.getScheme()));
			}
			retVal.getResourceMetadata().put(ResourceMetadataKeyEnum.TAG_LIST, tagList);
		}
		return retVal;
	}

	@Override
	public List<T> search(Map<String, IQueryParameterType> theParams) {
		Map<String, IQueryParameterType> params = theParams;
		if (params == null) {
			params = Collections.emptyMap();
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<X> criteriaQuery = builder.createQuery(myTableType);
		criteriaQuery.from(myTableType);

		RuntimeResourceDefinition resourceDef = myCtx.getResourceDefinition(myResourceType);

		// criteriaQuery.where(builder.equal(builder.parameter(ResourceHistoryTable.class,
		// "myPk.myId"), theId.asLong()));
		// criteriaQuery.where(builder.equal(builder.parameter(ResourceHistoryTable.class,
		// "myPk.myResourceType"), resourceType));

		TypedQuery<X> q = myEntityManager.createQuery(criteriaQuery);
		List<T> retVal = new ArrayList<>();
		for (X next : q.getResultList()) {
			T resource = toResource(next);
			boolean shouldAdd = params.isEmpty();

			for (Entry<String, IQueryParameterType> nextParamEntry : params.entrySet()) {
				RuntimeSearchParam param = resourceDef.getSearchParam(nextParamEntry.getKey());
				FhirTerser terser = myCtx.newTerser();
				String path = param.getPath();
				List<Object> values = terser.getValues(resource, path);
				for (Object nextValue : values) {
					if (nextValue.equals(nextParamEntry.getValue())) {
						shouldAdd = true;
						break;
					}
				}
			}

			if (shouldAdd) {
				retVal.add(resource);
			}
		}
		return retVal;
	}

	@Override
	public List<T> search(String theSpName, IQueryParameterType theValue) {
		return search(Collections.singletonMap(theSpName, theValue));
	}

}
