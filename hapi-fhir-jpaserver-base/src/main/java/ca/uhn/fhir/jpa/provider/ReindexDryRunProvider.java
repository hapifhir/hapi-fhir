package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.jpa.search.reindex.IReindexDryRunService;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.List;

public class ReindexDryRunProvider {


	private final IReindexDryRunService myReindexDryRunService;

	public ReindexDryRunProvider(IReindexDryRunService theReindexDryRunService) {
		myReindexDryRunService = theReindexDryRunService;
	}

	@Operation(name = ProviderConstants.OPERATION_REINDEX, idempotent = false)
	public IBaseParameters Reindex(
		@OperationParam(name = ProviderConstants.OPERATION_REINDEX_PARAM_URL, typeName = "string", min = 0, max = OperationParam.MAX_UNLIMITED) List<IPrimitiveType<String>> theUrlsToReindex,
		RequestDetails theRequestDetails
	)
	{
		return null;
	}

}
