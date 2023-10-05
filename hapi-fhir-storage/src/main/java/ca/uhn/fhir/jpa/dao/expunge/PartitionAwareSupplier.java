package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.rest.api.server.RequestDetails;

import java.util.function.Supplier;
import javax.validation.constraints.NotNull;

/**
 * Utility class wrapping a supplier in a transaction with the purpose of performing the supply operation with a
 * partitioned aware context.
 */
public class PartitionAwareSupplier {
	private final HapiTransactionService myTransactionService;
	private final RequestDetails myRequestDetails;

	@NotNull
	public PartitionAwareSupplier(HapiTransactionService theTxService, RequestDetails theRequestDetails) {
		myTransactionService = theTxService;
		myRequestDetails = theRequestDetails;
	}

	@NotNull
	public <T> T supplyInPartitionedContext(Supplier<T> theResourcePersistentIdSupplier) {
		return myTransactionService.withRequest(myRequestDetails).execute(tx -> theResourcePersistentIdSupplier.get());
	}
}
