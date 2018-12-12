package ca.uhn.fhir.jpa.subscription.module;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.subscription.module.cache.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.module.subscriber.IResourceProvider;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceProviderFhirClient implements IResourceProvider {
	private static final Logger ourLog = LoggerFactory.getLogger(ActiveSubscription.class);

	@Autowired
	FhirContext myFhirContext;

	private final IGenericClient myClient;

	public ResourceProviderFhirClient(IGenericClient theClient) {
		myClient = theClient;
	}

	@Override
	public IBaseResource getResource(IIdType payloadId) throws ResourceGoneException {
		RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(payloadId.getResourceType());

		return myClient.search().forResource(resourceDef.getName()).withIdAndCompartment(payloadId.getIdPart(), payloadId.getResourceType()).execute();
	}
}
