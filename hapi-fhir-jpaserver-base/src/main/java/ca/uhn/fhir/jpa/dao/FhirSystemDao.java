package ca.uhn.fhir.jpa.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.util.StopWatch;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.FhirTerser;

public class FhirSystemDao extends BaseFhirDao implements IFhirSystemDao {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirSystemDao.class);

	@PersistenceContext()
	private EntityManager myEntityManager;

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void transaction(List<IResource> theResources) {
		ourLog.info("Beginning transaction with {} resources", theResources.size());
		long start = System.currentTimeMillis();

		for (int i =0; i <theResources.size();i++) {
			IResource res = theResources.get(i);
			if(res.getId().hasIdPart() && !res.getId().hasResourceType()) {
				res.setId(new IdDt(toResourceName(res.getClass()), res.getId().getIdPart()));
			}
		}
		
		FhirTerser terser = getContext().newTerser();

		int creations = 0;
		int updates = 0;

		Map<IdDt, IdDt> idConversions = new HashMap<IdDt, IdDt>();

		List<ResourceTable> persistedResources = new ArrayList<ResourceTable>();

		for (IResource nextResource : theResources) {
			IdDt nextId = nextResource.getId();
			if (nextId == null) {
				nextId = new IdDt();
			}

			String resourceName = toResourceName(nextResource);

			// IFhirResourceDao<? extends IResource> dao = getDao(nextResource.getClass());
			// if (dao == null) {
			// throw new InvalidRequestException("This server is not able to handle resources of type: " +
			// nextResource.getResourceId().getResourceType());
			// }

			ResourceTable entity;
			if (nextId.isEmpty()) {
				entity = null;
			} else {
				try {
					Long pid = translateForcedIdToPid(nextId);
					entity = myEntityManager.find(ResourceTable.class, pid);
				} catch (ResourceNotFoundException e) {
					entity = null;
				}
			}

			if (entity == null) {
				entity = toEntity(nextResource);
				createForcedIdIfNeeded(entity, nextId);
				myEntityManager.persist(entity);
				if (entity.getForcedId() != null) {
					myEntityManager.persist(entity.getForcedId());
				}
				// myEntityManager.flush();
				creations++;
				ourLog.info("Resource Type[{}] with ID[{}] does not exist, creating it", resourceName, nextId);
			} else {
				updates++;
				ourLog.info("Resource Type[{}] with ID[{}] exists, updating it", resourceName, nextId);
			}

			persistedResources.add(entity);

		}

		ourLog.info("Flushing transaction to database");
		myEntityManager.flush();

		for (int i = 0; i < persistedResources.size(); i++) {
			ResourceTable entity = persistedResources.get(i);
			String resourceName = toResourceName(theResources.get(i));
			IdDt nextId = theResources.get(i).getId();

			IdDt newId = entity.getIdDt().toUnqualifiedVersionless();
			if (nextId == null || nextId.isEmpty()) {
				ourLog.info("Transaction resource (with no preexisting ID) has been assigned new ID[{}]", nextId, newId);
			} else {
				if (nextId.toUnqualifiedVersionless().equals(entity.getIdDt().toUnqualifiedVersionless())) {
					ourLog.info("Transaction resource ID[{}] is being updated", newId);
				} else {
					if (!nextId.getIdPart().startsWith("#")) {
						nextId = new IdDt(resourceName + '/' + nextId.getIdPart());
						ourLog.info("Transaction resource ID[{}] has been assigned new ID[{}]", nextId, newId);
						idConversions.put(nextId, newId);
					}
				}
			}

		}

		for (IResource nextResource : theResources) {
			List<ResourceReferenceDt> allRefs = terser.getAllPopulatedChildElementsOfType(nextResource, ResourceReferenceDt.class);
			for (ResourceReferenceDt nextRef : allRefs) {
				IdDt nextId = nextRef.getReference();
				if (idConversions.containsKey(nextId)) {
					IdDt newId = idConversions.get(nextId);
					ourLog.info(" * Replacing resource ref {} with {}", nextId, newId);
					nextRef.setReference(newId);
				} else {
					ourLog.debug(" * Reference [{}] does not exist in bundle", nextId);
				}
			}
		}

		ourLog.info("Re-flushing updated resource references and extracting search criteria");

		for (int i = 0; i < theResources.size(); i++) {
			IResource resource = theResources.get(i);
			ResourceTable table = persistedResources.get(i);
			updateEntity(resource, table, table.getId() != null, false);
		}

		long delay = System.currentTimeMillis() - start;
		ourLog.info("Transaction completed in {}ms with {} creations and {} updates", new Object[] { delay, creations, updates });

		notifyWriteCompleted();
	}

	@Override
	public IBundleProvider history(Date theSince) {
		StopWatch w = new StopWatch();
		IBundleProvider retVal = super.history(null, null, theSince);
		ourLog.info("Processed global history in {}ms", w.getMillisAndRestart());
		return retVal;
	}

	@Override
	public TagList getAllTags() {
		StopWatch w = new StopWatch();
		TagList retVal = super.getTags(null, null);
		ourLog.info("Processed getAllTags in {}ms", w.getMillisAndRestart());
		return retVal;
	}

	@Override
	public Map<String, Long> getResourceCounts() {
		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = builder.createTupleQuery();
		Root<?> from = cq.from(ResourceTable.class);
		cq.multiselect(from.get("myResourceType").as(String.class), builder.count(from.get("myResourceType")).as(Long.class));
		cq.groupBy(from.get("myResourceType"));

		TypedQuery<Tuple> q = myEntityManager.createQuery(cq);

		Map<String, Long> retVal = new HashMap<String, Long>();
		for (Tuple next : q.getResultList()) {
			String resourceName = next.get(0, String.class);
			Long count = next.get(1, Long.class);
			retVal.put(resourceName, count);
		}
		return retVal;
	}

}
