package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.jpa.api.svc.IResourceSearchUrlSvc;
import ca.uhn.fhir.jpa.dao.data.IResourceSearchUrlDao;
import ca.uhn.fhir.jpa.model.entity.ResourceSearchUrlEntity;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceSearchUrlSvcImpl implements IResourceSearchUrlSvc {

	@Autowired
	private IResourceSearchUrlDao myResourceSearchUrlDao;

	@Override
	public void saveResourceSearchUrl(String theCanonicalizedUrlForStorage, IResourcePersistentId theResourcePersistentId) {
		ResourceSearchUrlEntity searchUrlEntity = ResourceSearchUrlEntity.from(theCanonicalizedUrlForStorage, (Long)theResourcePersistentId.getId());
		myResourceSearchUrlDao.save(searchUrlEntity);

	}
}
