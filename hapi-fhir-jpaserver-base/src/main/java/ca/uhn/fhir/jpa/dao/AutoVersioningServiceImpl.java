package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoVersioningServiceImpl implements IAutoVersioningService {

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Override
	public Map<IIdType, ResourcePersistentId> getExistingAutoversionsForIds(RequestPartitionId theRequestPartitionId, Collection<IIdType> theIds) {
		HashMap<IIdType, ResourcePersistentId> idToPID = new HashMap<>();
		HashMap<String, List<IIdType>> resourceTypeToIds = new HashMap<>();

		for (IIdType id : theIds) {
			String resourceType = id.getResourceType();
			if (!resourceTypeToIds.containsKey(resourceType)) {
				resourceTypeToIds.put(resourceType, new ArrayList<>());
			}
			resourceTypeToIds.get(resourceType).add(id);
		}

		for (String resourceType : resourceTypeToIds.keySet()) {
			IFhirResourceDao dao = myDaoRegistry.getResourceDao(resourceType);

			Map<IIdType, ResourcePersistentId> idAndPID = dao.getIdsOfExistingResources(theRequestPartitionId,
				resourceTypeToIds.get(resourceType));
			idToPID.putAll(idAndPID);
		}

		return idToPID;
	}
}
