package ca.uhn.fhir.empi.api;

import org.hl7.fhir.instance.model.api.IBaseResource;

public interface IEmpiChannelSubmitterSvc {

	/**
	 * TODO GGG write javadoc
	 * @param theResource
	 */
	void submitResourceToEmpiChannel(IBaseResource theResource);
}
