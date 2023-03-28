package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.jpa.search.reindex.IInstanceReindexService;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.annotation.Nonnull;

public class InstanceReindexProvider {

	private final IInstanceReindexService myInstanceReindexService;

	/**
	 * Constructor
	 */
	public InstanceReindexProvider(@Nonnull IInstanceReindexService theInstanceReindexService) {
		Validate.notNull(theInstanceReindexService);
		myInstanceReindexService = theInstanceReindexService;
	}

	@Operation(name = ProviderConstants.OPERATION_REINDEX_DRYRUN, idempotent = true, global = true)
	public IBaseParameters reindexInstanceDryRun(
		@IdParam IIdType theId,
		RequestDetails theRequestDetails
	) {
		return myInstanceReindexService.reindexDryRun(theRequestDetails, theId);
	}

	@Operation(name = ProviderConstants.OPERATION_REINDEX, idempotent = true, global = true)
	public IBaseParameters reindexInstance(
		@IdParam IIdType theId,
		RequestDetails theRequestDetails
	) {
		return myInstanceReindexService.reindex(theRequestDetails, theId);
	}

}
