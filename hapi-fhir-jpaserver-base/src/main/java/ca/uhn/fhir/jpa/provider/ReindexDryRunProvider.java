package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.jpa.search.reindex.IReindexDryRunService;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.List;

public class ReindexDryRunProvider {

	private final IReindexDryRunService myReindexDryRunService;

	public ReindexDryRunProvider(IReindexDryRunService theReindexDryRunService) {
		myReindexDryRunService = theReindexDryRunService;
	}

//	@Operation(name = ProviderConstants.OPERATION_REINDEX_DRYRUN, idempotent = true, global = true)
//	public IBaseParameters reindexDryRun(
//		@IdParam IIdType theId,
//		RequestDetails theRequestDetails
//	)
//	{
//		return myReindexDryRunService.reindexDryRun(theRequestDetails, theId);
//	}

}
