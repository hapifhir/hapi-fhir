package ca.uhn.fhir.rest.server.provider;

import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IDeleteExpungeJobSubmitter;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.List;

public class DeleteExpungeProvider {
	private final IDeleteExpungeJobSubmitter myDeleteExpungeJobSubmitter;

	public DeleteExpungeProvider(IDeleteExpungeJobSubmitter theDeleteExpungeJobSubmitter) {
		myDeleteExpungeJobSubmitter = theDeleteExpungeJobSubmitter;
	}

	@Operation(name = ProviderConstants.OPERATION_DELETE_EXPUNGE, idempotent = false)
	public IBaseParameters deleteExpunge(
		@OperationParam(name = ProviderConstants.OPERATION_DELETE_EXPUNGE_URL, typeName = "string", min = 1) List<IPrimitiveType<String>> theUrlsToExpungeDelete,
		RequestDetails theRequestDetails
	) {
		return myDeleteExpungeJobSubmitter.submitJob(theUrlsToExpungeDelete);
	}
}
