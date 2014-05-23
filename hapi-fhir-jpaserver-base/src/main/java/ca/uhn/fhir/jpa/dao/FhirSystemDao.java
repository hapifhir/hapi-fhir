package ca.uhn.fhir.jpa.dao;

import static org.apache.commons.lang3.StringUtils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.entity.ResourceLink;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.util.FhirTerser;

public class FhirSystemDao extends BaseFhirDao implements IFhirSystemDao {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirSystemDao.class);

	@PersistenceContext(name = "FHIR_UT", type = PersistenceContextType.TRANSACTION, unitName = "FHIR_UT")
	private EntityManager myEntityManager;

	private FhirContext myContext = new FhirContext();

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public List<IResource> transaction(List<IResource> theResources) {
		ourLog.info("Beginning transaction with {} resources", theResources.size());

		FhirTerser terser = myContext.newTerser();

		Map<IdDt, IdDt> idConversions = new HashMap<>();
		List<ResourceTable> persistedResources = new ArrayList<>();
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
			} else if (!nextId.isValidLong()) {
				entity = null;
			} else {
				entity = myEntityManager.find(ResourceTable.class, nextId.asLong());
			}
			
			if (entity == null) {
				entity = toEntity(nextResource);
				myEntityManager.persist(entity);
				myEntityManager.flush();
			}
			
			idConversions.put(nextId, new IdDt(resourceName + '/' + entity.getId()));
			persistedResources.add(entity);

		}

		for (IResource nextResource : theResources) {
			List<ResourceReferenceDt> allRefs = terser.getAllPopulatedChildElementsOfType(nextResource, ResourceReferenceDt.class);
			for (ResourceReferenceDt nextRef : allRefs) {
				IdDt nextId = nextRef.getResourceId();
				if (idConversions.containsKey(nextId)) {
					IdDt newId = idConversions.get(nextId);
					ourLog.info(" * Replacing resource ref {} with {}", nextId, newId);
					nextRef.setResourceId(newId);
				}
			}
		}

		for (int i = 0; i < theResources.size(); i++) {
			IResource resource = theResources.get(i);
			ResourceTable table = persistedResources.get(i);
			updateEntity(resource, table, table.getId() != null);
		}

		return null;
	}

}
