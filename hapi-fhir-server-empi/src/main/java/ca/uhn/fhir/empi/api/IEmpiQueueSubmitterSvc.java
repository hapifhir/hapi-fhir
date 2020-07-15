package ca.uhn.fhir.empi.api;

import org.hl7.fhir.instance.model.api.IBaseResource;

public interface IEmpiQueueSubmitterSvc {

	void manuallySubmitResourceToEmpi(IBaseResource theResource);
}
