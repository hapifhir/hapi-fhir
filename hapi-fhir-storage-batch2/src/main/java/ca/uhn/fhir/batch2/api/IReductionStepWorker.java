package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.batch2.model.ListResult;
import ca.uhn.fhir.model.api.IModelJson;

public interface IReductionStepWorker<PT extends IModelJson, IT extends IModelJson, OT extends IModelJson>
	extends IJobStepWorker<PT, ListResult<IT>, OT> {

}
