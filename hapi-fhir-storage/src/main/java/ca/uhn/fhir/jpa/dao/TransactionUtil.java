/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.model.api.StorageResponseCodeEnum;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.MetaUtil;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
	 * This method only returns outcome details for standard write operations (create/update/patch)
	 * and will not include details for other kinds of operations (read/search/etc).
	 * </p>
	 * <p>
	 * This method should only be called for Bundles returned by HAPI FHIR's transaction
	 * processor, results will have no meaning for any other input.
	 * </p>
	 *
	 * @param theContext                   A FhirContext instance for the appropriate FHIR version
	 * @param theTransactionRequestBundle  The transaction request bundle which was processed in order to produce {@literal theTransactionResponseBundle}
	 * @param theTransactionResponseBundle The transaction response bundle. This bundle must be a transaction response produced by the same version of
	 *                                     HAPI FHIR, as this code looks for elements it will expect to be present.
	 * @since 8.2.0
	 */
	public static TransactionResponse parseTransactionResponse(
		FhirContext theContext, IBaseBundle theTransactionRequestBundle, IBaseBundle theTransactionResponseBundle) {
		FhirTerser terser = theContext.newTerser();
		List<StorageOutcome> storageOutcomes = new ArrayList<>();

		String bundleMetaSource = null;
		if (theContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.DSTU3)) {
			bundleMetaSource = MetaUtil.getSource(theContext, theTransactionRequestBundle);
		}

		List<IBase> requestEntries = terser.getValues(theTransactionRequestBundle, "entry");
		List<IBase> responseEntries = terser.getValues(theTransactionResponseBundle, "entry");
		for (int i = 0; i < responseEntries.size(); i++) {
			IBase requestEntry = requestEntries.get(i);
			IBase responseEntry = responseEntries.get(i);

			String requestVerb = terser.getSinglePrimitiveValueOrNull(requestEntry, "request.method");
			if ("GET".equals(requestVerb)) {
				continue;
			}

			IBaseResource requestResource = terser.getSingleValueOrNull(requestEntry, "resource", IBaseResource.class);
			String requestMetaSource = null;
			if (theContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.DSTU3)) {
				requestMetaSource = MetaUtil.getSource(theContext, requestResource);
			}
			if (isBlank(requestMetaSource)) {
				requestMetaSource = bundleMetaSource;
			}

			String requestFullUrlString = terser.getSinglePrimitiveValueOrNull(requestEntry, "fullUrl");
			IIdType requestFullUrl = null;
			if (requestFullUrlString != null) {
				requestFullUrl = theContext.getVersion().newIdType(requestFullUrlString);
				requestFullUrl = toUnqualified(requestFullUrl);
			}

			IBase responseResponse = terser.getSingleValueOrNull(responseEntry, "response", IBase.class);
			if (responseResponse != null) {

				// As long as we're parsing a bundle from HAPI, this will never be used. But let's be
				// defensive just in case.
				int statusCode = 0;
				String statusMessage = terser.getSinglePrimitiveValueOrNull(responseResponse, "status");
				if (statusMessage != null) {
					int statusSpaceIdx = statusMessage.indexOf(' ');
					if (statusSpaceIdx > 0) {
						statusCode = Integer.parseInt(statusMessage.substring(0, statusSpaceIdx));
					}
				}

				List<IBase> issues = Collections.emptyList();
				if (theContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.DSTU3)) {
					issues = terser.getValues(responseResponse, "outcome.issue");
				} else {
					IBaseResource responseResource =
						terser.getSingleValueOrNull(responseEntry, "resource", IBaseResource.class);
					if (responseResource instanceof IBaseOperationOutcome) {
						issues = terser.getValues(responseResource, "issue");
					}
				}
				IIdType groupSourceId = null;
				for (int issueIndex = 0; issueIndex < issues.size(); issueIndex++) {
					IBase issue = issues.get(issueIndex);
					IIdType sourceId = requestFullUrl;

					String outcomeSystem = terser.getSinglePrimitiveValueOrNull(issue, "details.coding.system");
					StorageResponseCodeEnum responseCode = null;
					if (StorageResponseCodeEnum.SYSTEM.equals(outcomeSystem)) {
						String outcomeCode = terser.getSinglePrimitiveValueOrNull(issue, "details.coding.code");
						responseCode = StorageResponseCodeEnum.valueOf(outcomeCode);
					}

					String errorMessage = null;
					String issueSeverityString = terser.getSinglePrimitiveValueOrNull(issue, "severity");
					if (isNotBlank(issueSeverityString)) {
						IValidationSupport.IssueSeverity issueSeverity =
							IValidationSupport.IssueSeverity.fromCode(issueSeverityString);
						if (issueSeverity != null) {
							if (issueSeverity.ordinal() <= IValidationSupport.IssueSeverity.ERROR.ordinal()) {
								errorMessage = terser.getSinglePrimitiveValueOrNull(issue, "diagnostics");
							}
						}
					}

					if (responseCode == null && statusCode >= 400 && statusCode <= 599) {
						responseCode = StorageResponseCodeEnum.FAILURE;
					}

					IIdType targetId;
					if (responseCode == StorageResponseCodeEnum.AUTOMATICALLY_CREATED_PLACEHOLDER_RESOURCE) {
						/*
						 * The first issue on a transaction response OO will have the details about the
						 * processing of the actual input resource that was in the input transaction bundle.
						 * However, if any automatically created placeholders were created during the
						 * processing of that resource, details about those will be placed in subsequent
						 * issues.
						 */

						targetId = ((IBaseHasExtensions) issue)
							.getExtension().stream()
							.filter(t -> HapiExtensions.EXTENSION_PLACEHOLDER_ID.equals(t.getUrl()))
							.findFirst()
							.map(t -> (IIdType) t.getValue())
							.orElse(null);
						sourceId = groupSourceId;
					} else {
						String responseLocation =
							terser.getSinglePrimitiveValueOrNull(responseEntry, "response.location");
						if (isNotBlank(responseLocation)) {
							targetId = theContext.getVersion().newIdType(responseLocation);
							if (issueIndex == 0) {
								groupSourceId = targetId;
							}
						} else {
							targetId = null;
						}
					}

					StorageOutcome outcome = new StorageOutcome(
						statusCode,
						statusMessage,
						responseCode,
						toUnqualified(sourceId),
						toUnqualified(targetId),
						errorMessage,
						requestMetaSource);
					storageOutcomes.add(outcome);
				}
			}
		}

		return new TransactionResponse(storageOutcomes);
	}

	private static IIdType toUnqualified(@Nullable IIdType theId) {
		if (theId != null && theId.hasBaseUrl()) {
			return theId.toUnqualified();
		}
		return theId;
	}

	/**
	 * @see #parseTransactionResponse(FhirContext, IBaseBundle, IBaseBundle)
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
	 * @see #parseTransactionResponse(FhirContext, IBaseBundle, IBaseBundle)
	 */
	public static class StorageOutcome {
		private final StorageResponseCodeEnum myStorageResponseCode;
		private final IIdType myTargetId;
		private final IIdType mySourceId;
		private final int myStatusCode;
		private final String myErrorMessage;
		private final String myRequestMetaSource;
		private final String myStatusMessage;

		public StorageOutcome(
			int theStatusCode,
			String theStatusMessage,
			StorageResponseCodeEnum theStorageResponseCode,
			IIdType theSourceId,
			IIdType theTargetId,
			String theErrorMessage,
			String theRequestMetaSource) {
			myStatusCode = theStatusCode;
			myStatusMessage = theStatusMessage;
			myStorageResponseCode = theStorageResponseCode;
			myTargetId = theTargetId;
			mySourceId = theSourceId;
			myErrorMessage = theErrorMessage;
			myRequestMetaSource = theRequestMetaSource;
		}

		/**
		 * @return Returns an error message if the specific action resulted in a failure. Returns {@literal null}
		 * 	otherwise.
		 */
		public String getErrorMessage() {
			return myErrorMessage;
		}

		/**
		 * @return Returns the HTTP status code
		 */
		public int getStatusCode() {
			return myStatusCode;
		}

		/**
		 * @return Returns the complete HTTP status message including the {@link #getStatusCode()} and the rest of the message. For example: {@literal 200 OK}
		 */
		public String getStatusMessage() {
			return myStatusMessage;
		}

		/**
		 * @return Contains a code identifying the specific outcome of this operation.
		 */
		public StorageResponseCodeEnum getStorageResponseCode() {
			return myStorageResponseCode;
		}

		/**
		 * @return Returns the ID of the resource as it was stored in the repository.
		 */
		public IIdType getTargetId() {
			return myTargetId;
		}

		/**
		 * @return Returns the ID of the resource in the request bundle in most cases. This could be an actual
		 * 	resource ID if the operation was an update by ID, or a placeholder UUID if placeholder IDs were in
		 * 	use in the bundle. If the {@link #getStorageResponseCode()} for this outcome is
		 *    {@link StorageResponseCodeEnum#AUTOMATICALLY_CREATED_PLACEHOLDER_RESOURCE}, the source ID will be the
		 * 	actual resolved and stored resource ID of the resource containing the reference which caused the
		 * 	placeholder to be created. The ID returned will be unqualified, meaning it has no base URL.
		 */
		public IIdType getSourceId() {
			return mySourceId;
		}

		/**
		 * @return Returns the <code>Resource.meta.source</code> value from the resource provided in the request
		 * 	bundle entry corresponding to this outcome.
		 */
		public String getRequestMetaSource() {
			return myRequestMetaSource;
		}
	}
}
