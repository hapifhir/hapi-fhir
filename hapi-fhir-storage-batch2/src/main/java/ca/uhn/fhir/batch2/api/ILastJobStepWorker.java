package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.model.api.IModelJson;

public interface ILastJobStepWorker<PT extends IModelJson, IT extends IModelJson> extends IJobStepWorker<PT, IT, VoidModel> {
}
