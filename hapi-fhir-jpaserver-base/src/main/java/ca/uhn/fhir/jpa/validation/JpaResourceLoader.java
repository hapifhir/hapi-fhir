package ca.uhn.fhir.jpa.validation;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.validation.IResourceLoader;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JpaResourceLoader implements IResourceLoader {
	@Autowired
	DaoRegistry myDaoRegistry;

	@Override
	public <T extends IBaseResource> T load(Class<T> theType, IIdType theId) throws ResourceNotFoundException {
		return myDaoRegistry.getResourceDao(theType).read(theId);
	}
}
