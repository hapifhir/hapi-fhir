package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.subscription.module.cache.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.module.subscriber.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DaoResourceProvider implements IResourceProvider {
	private static final Logger ourLog = LoggerFactory.getLogger(ActiveSubscription.class);

	@Autowired
	FhirContext myFhirContext;
	@Autowired
	DaoRegistry myDaoRegistry;

	@Override
	public IBaseResource getResource(IIdType payloadId) throws ResourceGoneException {
		RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(payloadId.getResourceType());
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(resourceDef.getImplementingClass());
		return dao.read(payloadId.toVersionless());
	}
}
