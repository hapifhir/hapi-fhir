package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.StorageResponseCodeEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.util.FhirTerser;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains utility methods for working with HAPI FHIR Transactions (referring to the FHIR
 * "transaction" operation, as opposed to working with database transactions).
 */
public class TransactionUtil {

	/**
	 * Non instantiable
	 */
	private TransactionUtil() {
		super();
	}

	/**
	 * This method accepts a {@literal Bundle} which was returned by a call to HAPI FHIR's
	 * transaction/batch processor. HAPI FHIR has specific codes and extensions it will
	 * always put into the OperationOutcomes returned by the transaction processor, so
	 * this method parses these and returns a more machine-processable interpretation.
	 * <p>
	 * This method should only be called for Bundles returned by HAPI FHIR's transaction
	 * processor, results will have no meaning for any other input.
	 * </p>
	 *
	 * @since 8.2.0
	 */
	public static TransactionResponse parseTransactionResponse(
		FhirContext theContext, IBaseBundle theTransactionResponseBundle) {
		FhirTerser terser = theContext.newTerser();
		List<StorageOutcome> storageOutcomes = new ArrayList<>();

		List<IBase> entries = terser.getValues(theTransactionResponseBundle, "entry");
		for (IBase entry : entries) {

			IBase response = terser.getSingleValueOrNull(entry, "response", IBase.class);
			if (response != null) {

				// As long as we're parsing a bundle from HAPI, this will never be used. But let's be
				// defensive just in case.
				int statusCode = 0;
				String statusString = terser.getSinglePrimitiveValueOrNull(response, "status");
				if (statusString != null) {
					int statusSpaceIdx = statusString.indexOf(' ');
					statusCode = Integer.parseInt(statusString.substring(0, statusSpaceIdx));
				}

				List<IBase> issues = terser.getValues(response, "outcome.issue");
				IIdType groupSourceId = null;
				for (int issueIndex = 0; issueIndex < issues.size(); issueIndex++) {
					IBase issue = issues.get(issueIndex);
					IIdType sourceId = null;

					String outcomeSystem = terser.getSinglePrimitiveValueOrNull(issue, "details.coding.system");
					StorageResponseCodeEnum responseCode = null;
					if (StorageResponseCodeEnum.SYSTEM.equals(outcomeSystem)) {
						String outcomeCode = terser.getSinglePrimitiveValueOrNull(issue, "details.coding.code");
						responseCode = StorageResponseCodeEnum.valueOf(outcomeCode);
					}

					String errorMessage = null;
					String issueCode = terser.getSinglePrimitiveValueOrNull(issue, "code");
					if (IssueTypeEnum.EXCEPTION.getCode().equals(issueCode)) {
						errorMessage = terser.getSinglePrimitiveValueOrNull(issue, "diagnostics");
					}

					IIdType targetId = null;
					if (responseCode == StorageResponseCodeEnum.AUTOMATICALLY_CREATED_PLACEHOLDER_RESOURCE) {
						/*
						 * The first issue on a transaction response OO will have the details about the
						 * processing of the actual input resource that was in the input transaction bundle.
						 * However, if any automatically created placeholders were created during the
						 * processing of that resource, details about those will be placed in subsequent
						 * issues.
						 */

						/*
						TODO: uncomment this when branch ja_20250217_tx_log_provenance merges
						targetId = ((IBaseHasExtensions) issue)
							.getExtension().stream()
							.filter(t -> HapiExtensions.EXTENSION_PLACEHOLDER_ID.equals(t.getUrl()))
							.findFirst()
							.map(t -> (IIdType) t.getValue())
							.orElse(null);
						*/
						sourceId = groupSourceId;
					} else {
						targetId = theContext
							.getVersion()
							.newIdType(terser.getSinglePrimitiveValueOrNull(entry, "response.location"));
						if (issueIndex == 0) {
							groupSourceId = targetId;
						}
					}

					StorageOutcome outcome =
						new StorageOutcome(statusCode, responseCode, targetId, sourceId, errorMessage);
					storageOutcomes.add(outcome);
				}
			}
		}

		return new TransactionResponse(storageOutcomes);
	}

	/**
	 * @see #parseTransactionResponse(FhirContext, IBaseBundle)
	 */
	public static class TransactionResponse {

		private final List<StorageOutcome> myStorageOutcomes;

		public TransactionResponse(List<StorageOutcome> theStorageOutcomes) {
			myStorageOutcomes = theStorageOutcomes;
		}

		public List<StorageOutcome> getStorageOutcomes() {
			return myStorageOutcomes;
		}
	}

	/**
	 * @see #parseTransactionResponse(FhirContext, IBaseBundle)
	 */
	public static class StorageOutcome {
		private final StorageResponseCodeEnum myStorageResponseCode;
		private final IIdType myTargetId;
		private final IIdType mySourceId;
		private final int myStatusCode;
		private final String myErrorMessage;

		public StorageOutcome(
			int theStatusCode,
			StorageResponseCodeEnum theStorageResponseCode,
			IIdType theTargetId,
			IIdType theSourceId,
			String theErrorMessage) {
			myStatusCode = theStatusCode;
			myStorageResponseCode = theStorageResponseCode;
			myTargetId = theTargetId;
			mySourceId = theSourceId;
			myErrorMessage = theErrorMessage;
		}

		public String getErrorMessage() {
			return myErrorMessage;
		}

		public int getStatusCode() {
			return myStatusCode;
		}

		public StorageResponseCodeEnum getStorageResponseCode() {
			return myStorageResponseCode;
		}

		public IIdType getTargetId() {
			return myTargetId;
		}

		public IIdType getSourceId() {
			return mySourceId;
		}
	}

}
