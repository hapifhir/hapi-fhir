package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.model.api.IModelJson;

public interface IJobCompletionHandler<PT extends IModelJson> {

	void jobComplete(JobCompletionDetails<PT> theDetails);

}
