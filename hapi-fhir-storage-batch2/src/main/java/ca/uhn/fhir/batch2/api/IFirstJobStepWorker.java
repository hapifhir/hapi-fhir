package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.model.api.IModelJson;

public interface IFirstJobStepWorker<PT extends IModelJson, OT extends IModelJson> extends IJobStepWorker<PT, VoidModel, OT> {
}
