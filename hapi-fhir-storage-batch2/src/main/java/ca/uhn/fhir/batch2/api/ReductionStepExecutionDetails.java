package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.batch2.model.ListResult;
import ca.uhn.fhir.model.api.IModelJson;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ReductionStepExecutionDetails<PT extends IModelJson, IT extends IModelJson, OT extends IModelJson>
	extends StepExecutionDetails<PT, ListResult<IT>> {

	public ReductionStepExecutionDetails(@Nonnull PT theParameters,
													 @Nullable ListResult<IT> theData,
													 @Nonnull String theInstanceId,
													 @Nonnull String theChunkId) {
		super(theParameters, theData, theInstanceId, theChunkId);
	}
}
