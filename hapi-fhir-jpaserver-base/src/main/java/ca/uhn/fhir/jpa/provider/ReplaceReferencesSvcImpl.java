/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferencesJobParameters;
import ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferencesProvenanceSvc;
import ca.uhn.fhir.batch2.util.Batch2TaskHelper;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.replacereferences.ReplaceReferencesPatchBundleSvc;
import ca.uhn.fhir.replacereferences.ReplaceReferencesRequest;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.util.StopLimitAccumulator;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferencesAppCtx.JOB_REPLACE_REFERENCES;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_OUTCOME;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_TASK;

public class ReplaceReferencesSvcImpl implements IReplaceReferencesSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(ReplaceReferencesSvcImpl.class);
	public static final String RESOURCE_TYPES_SYSTEM = "http://hl7.org/fhir/ValueSet/resource-types";
	private final DaoRegistry myDaoRegistry;
	private final HapiTransactionService myHapiTransactionService;
	private final IResourceLinkDao myResourceLinkDao;
	private final IJobCoordinator myJobCoordinator;
	private final ReplaceReferencesPatchBundleSvc myReplaceReferencesPatchBundleSvc;
	private final Batch2TaskHelper myBatch2TaskHelper;
	private final JpaStorageSettings myStorageSettings;
	private final ReplaceReferencesProvenanceSvc myReplaceReferencesProvenanceSvc;

	public ReplaceReferencesSvcImpl(
			DaoRegistry theDaoRegistry,
			HapiTransactionService theHapiTransactionService,
			IResourceLinkDao theResourceLinkDao,
			IJobCoordinator theJobCoordinator,
			ReplaceReferencesPatchBundleSvc theReplaceReferencesPatchBundleSvc,
			Batch2TaskHelper theBatch2TaskHelper,
			JpaStorageSettings theStorageSettings) {
		myDaoRegistry = theDaoRegistry;
		myHapiTransactionService = theHapiTransactionService;
		myResourceLinkDao = theResourceLinkDao;
		myJobCoordinator = theJobCoordinator;
		myReplaceReferencesPatchBundleSvc = theReplaceReferencesPatchBundleSvc;
		myBatch2TaskHelper = theBatch2TaskHelper;
		myStorageSettings = theStorageSettings;
		myReplaceReferencesProvenanceSvc = new ReplaceReferencesProvenanceSvc(theDaoRegistry);
	}

	@Override
	public IBaseParameters replaceReferences(
			ReplaceReferencesRequest theReplaceReferencesRequest, RequestDetails theRequestDetails) {
		theReplaceReferencesRequest.validateOrThrowInvalidParameterException();

		if (theRequestDetails.isPreferAsync()) {
			return replaceReferencesPreferAsync(theReplaceReferencesRequest, theRequestDetails);
		} else {
			return replaceReferencesPreferSync(theReplaceReferencesRequest, theRequestDetails);
		}
	}

	@Override
	public Integer countResourcesReferencingResource(IIdType theResourceId, RequestDetails theRequestDetails) {
		return myHapiTransactionService
				.withRequest(theRequestDetails)
				.execute(() -> myResourceLinkDao.countResourcesTargetingFhirTypeAndFhirId(
						theResourceId.getResourceType(), theResourceId.getIdPart()));
	}

	private IBaseParameters replaceReferencesPreferAsync(
			ReplaceReferencesRequest theReplaceReferencesRequest, RequestDetails theRequestDetails) {

		Task task = myBatch2TaskHelper.startJobAndCreateAssociatedTask(
				myDaoRegistry.getResourceDao(Task.class),
				theRequestDetails,
				myJobCoordinator,
				JOB_REPLACE_REFERENCES,
				new ReplaceReferencesJobParameters(
						theReplaceReferencesRequest, myStorageSettings.getDefaultTransactionEntriesForWrite()));

		Parameters retval = new Parameters();
		task.setIdElement(task.getIdElement().toUnqualifiedVersionless());
		task.getMeta().setVersionId(null);
		retval.addParameter()
				.setName(OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_TASK)
				.setResource(task);
		return retval;
	}

	/**
	 * Try to perform the operation synchronously. However if there is more than a page of results, fall back to asynchronous operation
	 */
	@Nonnull
	private IBaseParameters replaceReferencesPreferSync(
			ReplaceReferencesRequest theReplaceReferencesRequest, RequestDetails theRequestDetails) {

		Date startTime = new Date();
		// TODO KHS get partition from request
		StopLimitAccumulator<IdDt> accumulator = myHapiTransactionService
				.withRequest(theRequestDetails)
				.execute(() -> getAllPidsWithLimit(theReplaceReferencesRequest));

		if (accumulator.isTruncated()) {
			throw new PreconditionFailedException(Msg.code(2597) + "Number of resources with references to "
					+ theReplaceReferencesRequest.sourceId
					+ " exceeds the resource-limit "
					+ theReplaceReferencesRequest.resourceLimit
					+ ". Submit the request asynchronsly by adding the HTTP Header 'Prefer: respond-async'.");
		}

		Bundle result = myReplaceReferencesPatchBundleSvc.patchReferencingResources(
				theReplaceReferencesRequest, accumulator.getItemList(), theRequestDetails);

		if (theReplaceReferencesRequest.createProvenance) {
			myReplaceReferencesProvenanceSvc.createProvenance(
					theReplaceReferencesRequest.targetId,
					theReplaceReferencesRequest.sourceId,
					List.of(result),
					startTime,
					theRequestDetails);
		}

		Parameters retval = new Parameters();
		retval.addParameter()
				.setName(OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_OUTCOME)
				.setResource(result);
		return retval;
	}

	private @Nonnull StopLimitAccumulator<IdDt> getAllPidsWithLimit(
			ReplaceReferencesRequest theReplaceReferencesRequest) {

		Stream<IdDt> idStream = myResourceLinkDao.streamSourceIdsForTargetFhirId(
				theReplaceReferencesRequest.sourceId.getResourceType(),
				theReplaceReferencesRequest.sourceId.getIdPart());
		StopLimitAccumulator<IdDt> accumulator =
				StopLimitAccumulator.fromStreamAndLimit(idStream, theReplaceReferencesRequest.resourceLimit);
		return accumulator;
	}
}
