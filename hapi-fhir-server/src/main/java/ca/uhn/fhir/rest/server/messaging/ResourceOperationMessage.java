package ca.uhn.fhir.rest.server.messaging;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class ResourceOperationMessage extends BaseResourceModifiedMessage {
	public ResourceOperationMessage() {
	}

	public ResourceOperationMessage(FhirContext theFhirContext, IBaseResource theResource, OperationTypeEnum theOperationType) {
		super(theFhirContext, theResource, theOperationType);
	}

	public ResourceOperationMessage(FhirContext theFhirContext, IBaseResource theNewResource, OperationTypeEnum theOperationType, RequestDetails theRequest) {
		super(theFhirContext, theNewResource, theOperationType, theRequest);
	}
}
