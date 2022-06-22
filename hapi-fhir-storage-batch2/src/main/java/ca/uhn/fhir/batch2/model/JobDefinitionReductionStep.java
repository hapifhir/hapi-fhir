package ca.uhn.fhir.batch2.model;

import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.model.api.IModelJson;

import javax.annotation.Nonnull;

public class JobDefinitionReductionStep<PT extends IModelJson, IT extends IModelJson, OT extends IModelJson>
	extends JobDefinitionStep<PT, IT, OT> {


	public JobDefinitionReductionStep(@Nonnull String theStepId,
												 @Nonnull String theStepDescription,
												 @Nonnull IReductionStepWorker<PT, IT, OT> theJobStepWorker,
												 @Nonnull Class<IT> theInputType,
												 @Nonnull Class<OT> theOutputType) {
		super(theStepId, theStepDescription, (IJobStepWorker<PT, IT, OT>) theJobStepWorker, theInputType, theOutputType);
	}

	@Override
	public boolean isReductionStep() {
		return true;
	}
}
