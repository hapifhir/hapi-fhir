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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IJpaDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.model.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.patch.FhirPatch;
import ca.uhn.fhir.jpa.patch.JsonPatchUtils;
import ca.uhn.fhir.jpa.patch.XmlPatchUtils;
import ca.uhn.fhir.jpa.update.UpdateParameters;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.DeleteCascadeModeEnum;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IDeleteExpungeJobSubmitter;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseStorageResourceDao<T extends IBaseResource> extends BaseStorageDao
		implements IFhirResourceDao<T>, IJpaDao<T> {
	public static final StrictErrorHandler STRICT_ERROR_HANDLER = new StrictErrorHandler();

	@Autowired
	protected abstract HapiTransactionService getTransactionService();

	@Autowired
	protected abstract MatchResourceUrlService getMatchResourceUrlService();

	@Autowired
	protected abstract IStorageResourceParser getStorageResourceParser();

	@Autowired
	protected abstract IDeleteExpungeJobSubmitter getDeleteExpungeJobSubmitter();

	@Autowired
	protected abstract IRequestPartitionHelperSvc getRequestPartitionHelperService();

	@Override
	public DaoMethodOutcome patch(
			IIdType theId,
			String theConditionalUrl,
			PatchTypeEnum thePatchType,
			String thePatchBody,
			IBaseParameters theFhirPatchBody,
			RequestDetails theRequestDetails) {
		TransactionDetails transactionDetails = new TransactionDetails();
		return getTransactionService()
				.withRequest(theRequestDetails)
				.withTransactionDetails(transactionDetails)
				.execute(tx -> patchInTransaction(
						theId,
						theConditionalUrl,
						true,
						thePatchType,
						thePatchBody,
						theFhirPatchBody,
						theRequestDetails,
						transactionDetails));
	}

	@Override
	public DaoMethodOutcome patchInTransaction(
			IIdType theId,
			String theConditionalUrl,
			boolean thePerformIndexing,
			PatchTypeEnum thePatchType,
			String thePatchBody,
			IBaseParameters theFhirPatchBody,
			RequestDetails theRequestDetails,
			TransactionDetails theTransactionDetails) {
		assert TransactionSynchronizationManager.isActualTransactionActive();

		boolean isHistoryRewrite = myStorageSettings.isUpdateWithHistoryRewriteEnabled()
				&& theRequestDetails != null
				&& theRequestDetails.isRewriteHistory();

		if (isHistoryRewrite && !theId.hasVersionIdPart()) {
			throw new InvalidRequestException(
					Msg.code(2717) + "Invalid resource ID for rewrite history: ID must contain a history version");
		}

		RequestPartitionId theRequestPartitionId = getRequestPartitionHelperService()
				.determineReadPartitionForRequestForSearchType(theRequestDetails, getResourceName());

		IBasePersistedResource entityToUpdate;
		IIdType resourceId;
		if (isNotBlank(theConditionalUrl)) {
			entityToUpdate = getEntityToPatchWithMatchUrlCache(
					theConditionalUrl, theRequestDetails, theTransactionDetails, theRequestPartitionId);
			resourceId = entityToUpdate.getIdDt();
		} else {
			resourceId = theId;

			if (isHistoryRewrite) {
				entityToUpdate = readEntity(theId, theRequestDetails);
			} else {
				entityToUpdate = readEntityLatestVersion(theId, theRequestDetails, theTransactionDetails);
				validateIsCurrentVersionOrThrow(theId, entityToUpdate);
			}
		}

		validateResourceIsNotDeletedOrThrow(entityToUpdate);
		validateResourceType(entityToUpdate, getResourceName());

		IBaseResource resourceToUpdate = getStorageResourceParser().toResource(entityToUpdate, false);
		if (resourceToUpdate == null) {
			// If this is null, we are presumably in a FHIR transaction bundle with both a create and a patch on the
			// same
			// resource. This is weird but not impossible.
			resourceToUpdate = theTransactionDetails.getResolvedResource(resourceId);
		}

		IBaseResource destination =
				applyPatchToResource(thePatchType, thePatchBody, theFhirPatchBody, resourceToUpdate);
		@SuppressWarnings("unchecked")
		T destinationCasted = (T) destination;

		preProcessResourceForStorage(destinationCasted, theRequestDetails, theTransactionDetails, true);

		if (isHistoryRewrite) {
			return getTransactionService()
					.withRequest(theRequestDetails)
					.withTransactionDetails(theTransactionDetails)
					.execute(tx -> doUpdateWithHistoryRewrite(
							destinationCasted,
							theRequestDetails,
							theTransactionDetails,
							theRequestPartitionId,
							RestOperationTypeEnum.PATCH));
		}

		UpdateParameters updateParameters = new UpdateParameters<>()
				.setRequestDetails(theRequestDetails)
				.setResourceIdToUpdate(resourceId)
				.setMatchUrl(theConditionalUrl)
				.setShouldPerformIndexing(thePerformIndexing)
				.setShouldForceUpdateVersion(false)
				.setResource(destinationCasted)
				.setEntity(entityToUpdate)
				.setOperationType(RestOperationTypeEnum.PATCH)
				.setTransactionDetails(theTransactionDetails)
				.setShouldForcePopulateOldResourceForProcessing(false);

		return doUpdateForUpdateOrPatch(updateParameters);
	}

	private static void validateIsCurrentVersionOrThrow(IIdType theId, IBasePersistedResource theEntityToUpdate) {
		if (theId.hasVersionIdPart() && theId.getVersionIdPartAsLong() != theEntityToUpdate.getVersion()) {
			throw new ResourceVersionConflictException(Msg.code(974) + "Version " + theId.getVersionIdPart()
					+ " is not the most recent version of this resource, unable to apply patch");
		}
	}

	DaoMethodOutcome doUpdateWithHistoryRewrite(
			T theResource,
			RequestDetails theRequest,
			TransactionDetails theTransactionDetails,
			RequestPartitionId theRequestPartitionId,
			RestOperationTypeEnum theRestOperationType) {

		throw new UnsupportedOperationException(Msg.code(2718) + "Patch with history rewrite is unsupported.");
	}

	private void validateResourceIsNotDeletedOrThrow(IBasePersistedResource theEntityToUpdate) {
		if (theEntityToUpdate.isDeleted()) {
			throw createResourceGoneException(theEntityToUpdate);
		}
	}

	private IBaseResource applyPatchToResource(
			PatchTypeEnum thePatchType,
			String thePatchBody,
			IBaseParameters theFhirPatchBody,
			IBaseResource theResourceToUpdate) {
		IBaseResource destination;
		switch (thePatchType) {
			case JSON_PATCH:
				destination = JsonPatchUtils.apply(getContext(), theResourceToUpdate, thePatchBody);
				break;
			case XML_PATCH:
				destination = XmlPatchUtils.apply(getContext(), theResourceToUpdate, thePatchBody);
				break;
			case FHIR_PATCH_XML:
			case FHIR_PATCH_JSON:
			default:
				IBaseParameters fhirPatchJson = theFhirPatchBody;
				new FhirPatch(getContext()).apply(theResourceToUpdate, fhirPatchJson);
				destination = theResourceToUpdate;
				break;
		}
		return destination;
	}

	private IBasePersistedResource getEntityToPatchWithMatchUrlCache(
			String theConditionalUrl,
			RequestDetails theRequestDetails,
			TransactionDetails theTransactionDetails,
			RequestPartitionId theRequestPartitionId) {
		IBasePersistedResource theEntityToUpdate;

		Set<IResourcePersistentId> match = getMatchResourceUrlService()
				.processMatchUrl(
						theConditionalUrl,
						getResourceType(),
						theTransactionDetails,
						theRequestDetails,
						theRequestPartitionId);
		if (match.size() > 1) {
			String msg = getContext()
					.getLocalizer()
					.getMessageSanitized(
							BaseStorageDao.class,
							"transactionOperationWithMultipleMatchFailure",
							"PATCH",
							getResourceName(),
							theConditionalUrl,
							match.size());
			throw new PreconditionFailedException(Msg.code(972) + msg);
		} else if (match.size() == 1) {
			IResourcePersistentId pid = match.iterator().next();
			theEntityToUpdate = readEntityLatestVersion(pid, theRequestDetails, theTransactionDetails);

		} else {
			String msg = getContext()
					.getLocalizer()
					.getMessageSanitized(BaseStorageDao.class, "invalidMatchUrlNoMatches", theConditionalUrl);
			throw new ResourceNotFoundException(Msg.code(973) + msg);
		}
		return theEntityToUpdate;
	}

	@Override
	@Nonnull
	public abstract Class<T> getResourceType();

	@Override
	@Nonnull
	protected abstract String getResourceName();

	protected abstract IBasePersistedResource readEntityLatestVersion(
			IResourcePersistentId thePersistentId,
			RequestDetails theRequestDetails,
			TransactionDetails theTransactionDetails);

	protected abstract IBasePersistedResource readEntityLatestVersion(
			IIdType theId, RequestDetails theRequestDetails, TransactionDetails theTransactionDetails);

	protected DaoMethodOutcome doUpdateForUpdateOrPatch(UpdateParameters<T> theUpdateParameters) {

		if (theUpdateParameters.getResourceIdToUpdate().hasVersionIdPart()
				&& Long.parseLong(theUpdateParameters.getResourceIdToUpdate().getVersionIdPart())
						!= theUpdateParameters.getEntity().getVersion()) {
			throw new ResourceVersionConflictException(Msg.code(989) + "Trying to update "
					+ theUpdateParameters.getResourceIdToUpdate() + " but this is not the current version");
		}

		if (theUpdateParameters.getResourceIdToUpdate().hasResourceType()
				&& !theUpdateParameters
						.getResourceIdToUpdate()
						.getResourceType()
						.equals(getResourceName())) {
			throw new UnprocessableEntityException(Msg.code(990) + "Invalid resource ID["
					+ theUpdateParameters.getEntity().getIdDt().toUnqualifiedVersionless() + "] of type["
					+ theUpdateParameters.getEntity().getResourceType()
					+ "] - Does not match expected [" + getResourceName() + "]");
		}

		IBaseResource oldResource;
		if (getStorageSettings().isMassIngestionMode()
				&& !theUpdateParameters.shouldForcePopulateOldResourceForProcessing()) {
			oldResource = null;
		} else {
			oldResource = getStorageResourceParser().toResource(theUpdateParameters.getEntity(), false);
		}

		/*
		 * Mark the entity as not deleted - This is also done in the actual updateInternal()
		 * method later on so it usually doesn't matter whether we do it here, but in the
		 * case of a transaction with multiple PUTs we don't get there until later so
		 * having this here means that a transaction can have a reference in one
		 * resource to another resource in the same transaction that is being
		 * un-deleted by the transaction. Wacky use case, sure. But it's real.
		 *
		 * See SystemProviderR4Test#testTransactionReSavesPreviouslyDeletedResources
		 * for a test that needs this.
		 */
		boolean wasDeleted = theUpdateParameters.getEntity().isDeleted();
		theUpdateParameters.getEntity().setNotDeleted();

		/*
		 * If we aren't indexing, that means we're doing this inside a transaction.
		 * The transaction will do the actual storage to the database a bit later on,
		 * after placeholder IDs have been replaced, by calling {@link #updateInternal}
		 * directly. So we just bail now.
		 */
		if (!theUpdateParameters.shouldPerformIndexing()) {
			theUpdateParameters
					.getResource()
					.setId(theUpdateParameters.getEntity().getIdDt().getValue());
			DaoMethodOutcome outcome = toMethodOutcome(
							theUpdateParameters.getRequest(),
							theUpdateParameters.getEntity(),
							theUpdateParameters.getResource(),
							theUpdateParameters.getMatchUrl(),
							theUpdateParameters.getOperationType())
					.setCreated(wasDeleted);
			outcome.setPreviousResource(oldResource);
			if (!outcome.isNop()) {
				// Technically this may not end up being right since we might not increment if the
				// contents turn out to be the same
				outcome.setId(outcome.getId()
						.withVersion(Long.toString(outcome.getId().getVersionIdPartAsLong() + 1)));
			}
			return outcome;
		}

		/*
		 * Otherwise, we're not in a transaction
		 */
		return updateInternal(
				theUpdateParameters.getRequest(),
				theUpdateParameters.getResource(),
				theUpdateParameters.getMatchUrl(),
				theUpdateParameters.shouldPerformIndexing(),
				theUpdateParameters.shouldForceUpdateVersion(),
				theUpdateParameters.getEntity(),
				theUpdateParameters.getResourceIdToUpdate(),
				oldResource,
				theUpdateParameters.getOperationType(),
				theUpdateParameters.getTransactionDetails());
	}

	public static void validateResourceType(IBasePersistedResource<?> theEntity, String theResourceName) {
		if (!theResourceName.equals(theEntity.getResourceType())) {
			throw new ResourceNotFoundException(Msg.code(935) + "Resource with ID "
					+ theEntity.getIdDt().getIdPart() + " exists but it is not of type " + theResourceName
					+ ", found resource of type " + theEntity.getResourceType());
		}
	}

	protected DeleteMethodOutcome deleteExpunge(String theUrl, RequestDetails theRequest) {
		if (!getStorageSettings().canDeleteExpunge()) {
			throw new MethodNotAllowedException(Msg.code(963) + "_expunge is not enabled on this server: "
					+ getStorageSettings().cannotDeleteExpungeReason());
		}

		RestfulServerUtils.DeleteCascadeDetails cascadeDelete =
				RestfulServerUtils.extractDeleteCascadeParameter(theRequest);
		boolean cascade = false;
		Integer cascadeMaxRounds = null;
		if (cascadeDelete.getMode() == DeleteCascadeModeEnum.DELETE) {
			cascade = true;
			cascadeMaxRounds = cascadeDelete.getMaxRounds();
			if (cascadeMaxRounds == null) {
				cascadeMaxRounds = myStorageSettings.getMaximumDeleteConflictQueryCount();
			} else if (myStorageSettings.getMaximumDeleteConflictQueryCount() != null
					&& myStorageSettings.getMaximumDeleteConflictQueryCount() < cascadeMaxRounds) {
				cascadeMaxRounds = myStorageSettings.getMaximumDeleteConflictQueryCount();
			}
		}

		List<String> urlsToDeleteExpunge = Collections.singletonList(theUrl);
		try {
			String jobId = getDeleteExpungeJobSubmitter()
					.submitJob(
							getStorageSettings().getExpungeBatchSize(),
							urlsToDeleteExpunge,
							cascade,
							cascadeMaxRounds,
							theRequest);
			return new DeleteMethodOutcome(createInfoOperationOutcome("Delete job submitted with id " + jobId));
		} catch (InvalidRequestException e) {
			throw new InvalidRequestException(Msg.code(965) + "Invalid Delete Expunge Request: " + e.getMessage(), e);
		}
	}
}
