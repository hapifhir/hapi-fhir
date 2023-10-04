package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.function.Supplier;

/**
 * Utility class wrapping a supplier in a transaction with the purpose of performing the supply operation with a
 * partitioned aware context.
 */
public class PartitionAwareFinder {
	private final HapiTransactionService myTransactionService;
	private final RequestDetails myRequestDetails;

	@NotNull
	public PartitionAwareFinder(HapiTransactionService theTxService, RequestDetails theRequestDetails) {
		myTransactionService = theTxService;
		myRequestDetails = theRequestDetails;
	}
	@NotNull
	public List<IResourcePersistentId> supplyInPartitionedContext(Supplier<List<IResourcePersistentId>> theResourcePersistentIdSupplier) {
		return myTransactionService
			.withRequest(myRequestDetails)
			.execute(tx -> theResourcePersistentIdSupplier.get());
	}


}
