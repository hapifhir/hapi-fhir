package ca.uhn.fhir.jpa.subscription.module.standalone;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.subscription.module.subscriber.IResourceRetriever;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FhirClientResourceRetriever implements IResourceRetriever {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirClientResourceRetriever.class);

	@Autowired
	FhirContext myFhirContext;
	@Autowired
	IGenericClient myClient;

	@Override
	public IBaseResource getResource(IIdType payloadId) throws ResourceGoneException {
		RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(payloadId.getResourceType());

		return myClient.search().forResource(resourceDef.getName()).withIdAndCompartment(payloadId.getIdPart(), payloadId.getResourceType()).execute();
	}
}
